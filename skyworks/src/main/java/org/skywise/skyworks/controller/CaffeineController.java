package org.skywise.skyworks.controller;

import com.github.benmanes.caffeine.cache.Cache;
import org.skywise.skyworks.common.DTO.CaffeineDTO;
import org.skywise.skyworks.common.DTO.PageDTO;
import org.skywise.skyworks.common.DTO.Response;
import org.skywise.skyworks.common.VO.CollectPageVO;
import org.skywise.skyworks.common.VO.TotalNumVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.enums.CaffeineNameEnum;
import org.skywise.skyworks.common.utils.TokenUtil;
import org.skywise.skyworks.model.Article;
import org.skywise.skyworks.model.Label;
import org.skywise.skyworks.service.IArticleService;
import org.skywise.skyworks.service.ILabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 16:48
 * @Description:
 */
@RestController
@RequestMapping("/skyworks/caffeine/")
public class CaffeineController {
    @Autowired
    private ILabelService labelService;

    @Autowired
    private IArticleService articleService;

    @Resource
    private Cache<Integer, Set<Integer>> upvoteArticleCache;

    @Resource
    private Cache<Integer, Set<Integer>> collectArticleCache;

    @Resource
    private Cache<Integer, Set<Integer>> userIdToCollectCache;

    @Resource
    private Cache<String, Set<Integer>> labelTopFourCache;

    @Resource
    private Cache<String, Long> totalDataCache;

    /**
     * 点赞/取消点赞
     */
    @PostMapping("/upvoteArticle")
    public Response<Integer> upvoteArticle(@RequestBody CaffeineDTO caffeineDTO) {
        Integer userId = Integer.valueOf(TokenUtil.getUserId());
        Set<Integer> userIdSet = upvoteArticleCache.get(caffeineDTO.getArticleId(), k -> new HashSet<>());

        // 将用户ID添加到缓存中
        if (caffeineDTO.getIsUpvote()) {
            userIdSet.add(userId);
        } else {
            userIdSet.remove(userId);
        }
        upvoteArticleCache.put(caffeineDTO.getArticleId(), userIdSet);
        return Response.success(userIdSet.size());
    }

    /**
     * 获取点赞数量
     */
    @PostMapping("/getArticleCount")
    public Response<String> getArticleCount(@RequestBody CaffeineDTO caffeineDTO) {
        Integer userId = TokenUtil.getUserId();
        Set<Integer> userIdSet = collectArticleCache.get(caffeineDTO.getArticleId(), k -> new HashSet<>());
        String resMsg;

        // 将用户ID添加到缓存中
        if (caffeineDTO.getIsUpvote()) {
            userIdSet.add(userId);
            resMsg = StrConstant.UPVOTE_SUCCESS;
        } else {
            userIdSet.remove(userId);
            resMsg = StrConstant.CANCEL_UPVOTE_SUCCESS;
        }
        upvoteArticleCache.put(caffeineDTO.getArticleId(), userIdSet);
        return Response.successMsg(resMsg);
    }

    /**
     * 收藏文章/取消收藏文章
     */
    @PostMapping("/collectArticle")
    public Response<Integer> collectArticle(@RequestBody CaffeineDTO caffeineDTO) {
        Integer userId = TokenUtil.getUserId();
        Set<Integer> userIdSet = collectArticleCache.get(caffeineDTO.getArticleId(), k -> new HashSet<>());
        Set<Integer> articleIdSet = userIdToCollectCache.get(userId, k -> new HashSet<>());

        // 将用户ID添加到缓存中
        if (caffeineDTO.getIsCollect()) {
            userIdSet.add(userId);
            articleIdSet.add(caffeineDTO.getArticleId());
        } else {
            userIdSet.remove(userId);
            articleIdSet.remove(caffeineDTO.getArticleId());
        }
        collectArticleCache.put(caffeineDTO.getArticleId(), userIdSet);
        userIdToCollectCache.put(userId, articleIdSet);
        return Response.success(userIdSet.size());
    }

    /**
     * 获取收藏数量
     */
    @PostMapping("/collectArticleCount")
    public Response<Integer> collectArticleCount() {
        Integer userId = TokenUtil.getUserId();
        Set<Integer> articleIdSet = userIdToCollectCache.get(userId, k -> new HashSet<>());
        return Response.success(articleIdSet.size());
    }

    /**
     * 查看个人收藏文章详情
     */
    @PostMapping("/collectArticleList")
    public Response<CollectPageVO> collectArticleList(@RequestBody PageDTO pageDTO) {
        Integer userId = TokenUtil.getUserId();
        Set<Integer> articleIdSet = userIdToCollectCache.get(userId, k -> new HashSet<>());
        int start = (pageDTO.getPageNum() - 1) * pageDTO.getPageSize();
        int end = Math.min(pageDTO.getPageNum() * pageDTO.getPageSize(), articleIdSet.size());
        List<Integer> articleIdList = new ArrayList<>(articleIdSet).subList(start, end);
        List<Article> articleList = new ArrayList<>(articleIdList.size());
        if (!articleIdList.isEmpty()) {
            articleList = articleService.listArticleByIds(articleIdList);
        }

        // 分页
        CollectPageVO collectPageVO = new CollectPageVO();
        collectPageVO.setArticleList(articleList);
        collectPageVO.setPageNum(pageDTO.getPageNum());
        collectPageVO.setPageSize(pageDTO.getPageSize());
        collectPageVO.setTotalCount((long) articleIdSet.size());
        collectPageVO.setTotalPage((int) Math.ceil((double) articleIdSet.size() / pageDTO.getPageSize()));
        return Response.success(collectPageVO);
    }

    /**
     * 获取文章最多的四个标签
     */
    @PostMapping("/recommendLabel")
    public Response<List<Label>> recommendLabel() {

        Set<Integer> labelIdSet = labelTopFourCache.getIfPresent(StrConstant.LABEL_TOP_FOUR);
        List<Label> labelList = labelService.listLabelByIdList(new ArrayList<>(labelIdSet));
        return Response.success(labelList);
    }

    /**
     * 获取后台首页：文章、点赞、评论、用户总量
     */
    @PostMapping("/getTotalCount")
    public Response<TotalNumVO> getTotalCount() {
        TotalNumVO totalNumVO = new TotalNumVO();
        totalNumVO.setArticleTotalNum(totalDataCache.getIfPresent(CaffeineNameEnum.ARTICLE_NUM.getName_en()));
        totalNumVO.setUpvoteTotalNum(totalDataCache.getIfPresent(CaffeineNameEnum.UPVOTE_NUM.getName_en()));
        totalNumVO.setCommentTotalNum(totalDataCache.getIfPresent(CaffeineNameEnum.COMMENT_NUM.getName_en()));
        totalNumVO.setViewTotalNum(totalDataCache.getIfPresent(CaffeineNameEnum.VIEW_NUM.getName_en()));
        totalNumVO.setUserTotalNum(totalDataCache.getIfPresent(CaffeineNameEnum.USER_NUM.getName_en()));
        return Response.success(totalNumVO);
    }
}
