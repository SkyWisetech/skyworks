package org.skywise.skyworks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.skywise.skyworks.common.DTO.ArticleDTO;
import org.skywise.skyworks.common.DTO.ArticleOptDTO;
import org.skywise.skyworks.common.VO.ArticlePageVO;
import org.skywise.skyworks.common.VO.ArticleVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.enums.AdminEnum;
import org.skywise.skyworks.common.enums.DeleteEnum;
import org.skywise.skyworks.common.exception.ServiceException;
import org.skywise.skyworks.common.utils.TokenUtil;
import org.skywise.skyworks.mapper.ArticleMapper;
import org.skywise.skyworks.mapper.LabelMapper;
import org.skywise.skyworks.model.Article;
import org.skywise.skyworks.model.Label;
import org.skywise.skyworks.service.IArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 11:17
 * @Description: 文章管理
 */
@Service
public class ArticleServiceImpl implements IArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private LabelMapper labelMapper;

    @Resource
    private Cache<Integer, Set<Integer>> labelCache;

    @Resource
    private Cache<Integer, String> labelNameCache;

    @Resource
    private Cache<Integer, Set<Integer>> upvoteArticleCache;

    @Resource
    private Cache<Integer, Set<Integer>> collectArticleCache;

    @Resource
    private Cache<Integer, Integer> articleIdToViewNumCache;

    private List<ArticleVO> updateUpvoteAndCollectNum(List<Article> articleList) {
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (Article article : articleList) {
            ArticleVO articleVO = new ArticleVO();
            try {
                BeanUtils.copyProperties(articleVO, article);
                Set<Integer> upvoteUserIdList = upvoteArticleCache.get(article.getId(), k -> new HashSet<>());
                Set<Integer> collectUserIdList = collectArticleCache.get(article.getId(), k -> new HashSet<>());
                Integer viewNum = articleIdToViewNumCache.getIfPresent(article.getId());
                String labelName = labelNameCache.getIfPresent(Integer.valueOf(article.getLabel()));
                Integer userId = TokenUtil.getUserId();

                articleVO.setUpvoteNum(upvoteUserIdList.size());
                articleVO.setCollectNum(collectUserIdList.size());
                articleVO.setViewNum(viewNum);
                articleVO.setLabelName(labelName);
                articleVO.setImageName(article.getImageName());
                articleVO.setIsUpvoted(upvoteUserIdList.contains(userId));
                articleVO.setIsCollected(collectUserIdList.contains(userId));
                articleVOList.add(articleVO);
            } catch (Exception e) {
                log.error(e.getMessage());
//                throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
            }

        }
        return articleVOList;
    }

    @Override
    public List<Article> listAllArticle() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(Article.ID_COLUMN, Article.LABEL_COLUMN, Article.VIEW_NUM_COLUMN).eq(Article.IS_DELETE, DeleteEnum.NOT_DELETE.getCode());
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Article> listArticleByIds(List<Integer> ids) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(Article.ID_COLUMN, Article.TITLE)
                .in(Article.ID_COLUMN, ids);
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public Long getArticleCount() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Article.IS_DELETE, DeleteEnum.NOT_DELETE.getCode());
        return articleMapper.selectCount(queryWrapper);
    }

    @Override
    public ArticleVO getArticleById(Integer id) {
        Integer userId = TokenUtil.getUserId();
        Article article = articleMapper.selectById(id);
        Label secondLabel = labelMapper.selectById(Integer.parseInt(article.getLabel()));
        // 修改浏览量
        Integer viewNum = articleIdToViewNumCache.get(id, k -> 0);
        articleIdToViewNumCache.put(id, ++viewNum);
        Set<Integer> upvoteUserIdSet = upvoteArticleCache.get(article.getId(), k -> new HashSet<>());
        Set<Integer> collectUserIdSet = collectArticleCache.get(article.getId(), k -> new HashSet<>());
        int upvoteNum = upvoteUserIdSet.size();
        int collectNum = collectUserIdSet.size();
        boolean isUpvoted = upvoteUserIdSet.contains(userId);
        boolean isCollected = collectUserIdSet.contains(userId);
        ArticleVO articleVO = new ArticleVO();
        try {
            BeanUtils.copyProperties(articleVO, article);
            articleVO.setUpvoteNum(upvoteNum);
            articleVO.setCollectNum(collectNum);
            articleVO.setViewNum(viewNum);
            articleVO.setIsUpvoted(isUpvoted);
            articleVO.setIsCollected(isCollected);
            articleVO.setImageName(article.getImageName());
            articleVO.setLabelName(labelNameCache.getIfPresent(Integer.valueOf(article.getLabel())));
            articleVO.setParentLabelId(secondLabel.getParentId());
            articleVO.setParentLabelName(labelNameCache.getIfPresent(secondLabel.getParentId()));
            // 如果是前台用户查看，则将图片替换为前台域名的URL
            if(!userId.equals(AdminEnum.ADMIN_USER_ID.getCode())){
                String content = articleVO.getContent().replace("http://207.148.115.202:81", "https://theworksof.com");
                articleVO.setContent(content);
            }

        } catch (Exception e) {
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        return articleVO;
    }

    @Override
    public ArticlePageVO listArticle(ArticleOptDTO articleOptDTO) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        // 查询条件：标题、点赞数查询、标签
        queryWrapper.like(StringUtils.hasLength(articleOptDTO.getTitle()), Article::getTitle, articleOptDTO.getTitle())
                .ge(articleOptDTO.getUpvoteNum() != null, Article::getUpvoteNum, articleOptDTO.getUpvoteNum())
                .eq(articleOptDTO.getLabelId() != null, Article::getLabel, articleOptDTO.getLabelId())
                .eq(Article::getIsDelete, DeleteEnum.NOT_DELETE.getCode())
                .orderByDesc(Article::getPriority)
                .orderByDesc(Article::getUpdateTime);

        // 日期查询
        if (articleOptDTO.getStartDate() != null && articleOptDTO.getEndDate() != null) {
            LocalDate endDate = articleOptDTO.getEndDate().plusDays(1L);
            queryWrapper.gt(Article::getCreateTime, articleOptDTO.getStartDate())
                    .lt(Article::getCreateTime, endDate);
        }
        PageHelper.startPage(articleOptDTO.getPageNum(), articleOptDTO.getPageSize());
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        List<ArticleVO> articleVOList = this.updateUpvoteAndCollectNum(articleList);

        // 输出分页信息
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        ArticlePageVO articlePageVO = new ArticlePageVO();
        articlePageVO.setArticleList(articleVOList);
        articlePageVO.setPageNum(pageInfo.getPageNum());
        articlePageVO.setPageSize(pageInfo.getPageSize());
        articlePageVO.setTotalCount(pageInfo.getTotal());
        articlePageVO.setTotalPage(pageInfo.getPages());
        return articlePageVO;
    }

    @Override
    public ArticlePageVO listArticleByLabel(ArticleOptDTO articleOptDTO) {
        /*根据标签查询文章ID：
         * 如果是一级标签，需要查询所有二级标签，再查询文章
         * 如果是二级标签，返回空列表，自己加进去
         * */
        Set<Integer> labelIdList = labelCache.get(articleOptDTO.getLabelId(), k -> new HashSet<>());
        labelIdList.add(articleOptDTO.getLabelId());

        // 根据标签查询文章
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(Article.LABEL_COLUMN, labelIdList)
                .eq(Article.IS_DELETE, DeleteEnum.NOT_DELETE.getCode());
        queryWrapper.orderByDesc(Article.PRIORITY_COLUMN, Article.UPDATE_TIME_COLUMN, Article.UPVOTE_NUM_COLUMN, Article.COMMENT_NUM_COLUMN, Article.COLLECT_NUM_COLUMN);

        PageHelper.startPage(articleOptDTO.getPageNum(), articleOptDTO.getPageSize());
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        List<ArticleVO> articleVOList = new ArrayList<>();
        if (articleList.size() > 0) {
            articleVOList = this.updateUpvoteAndCollectNum(articleList);
        }

        // 输出分页信息
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        ArticlePageVO articlePageVO = new ArticlePageVO();
        articlePageVO.setArticleList(articleVOList);
        articlePageVO.setPageNum(articleOptDTO.getPageNum());
        articlePageVO.setPageSize(articleOptDTO.getPageSize());
        articlePageVO.setTotalCount(pageInfo.getTotal());
        articlePageVO.setTotalPage(pageInfo.getPages());
        return articlePageVO;
    }


    @Override
    public void insertArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        try {
            BeanUtils.copyProperties(article, articleDTO);
        } catch (Exception e) {
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        int result = articleMapper.insert(article);
        if (result != 1) {
            throw new ServiceException(StrConstant.INSERT_ARTICLE_FAIL);
        }
    }

    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setUpdateTime(LocalDateTime.now());
        try {
            BeanUtils.copyProperties(article, articleDTO);
        } catch (Exception e) {
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        int result = articleMapper.updateById(article);
        if (result != 1) {
            throw new ServiceException(StrConstant.UPDATE_ARTICLE_FAIL);
        }
    }

    @Override
    public void updateBatchUpvote(List<Article> articleList) {
        articleMapper.updateBatchUpvote(articleList);
    }

    @Override
    public void updateBatchCollect(List<Article> articleList) {
        articleMapper.updateBatchCollect(articleList);
    }

    @Override
    public void updateBatchView(List<Article> articleList) {
        articleMapper.updateBatchView(articleList);
    }

    @Override
    public void updateArticleInfo(ArticleOptDTO articleOptDTO) {
        Article article = new Article();
        article.setUpdateTime(LocalDateTime.now());
        try {
            BeanUtils.copyProperties(article, articleOptDTO);
        } catch (Exception e) {
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        int result = articleMapper.updateById(article);
        if (result != 1) {
            throw new ServiceException(StrConstant.UPDATE_ARTICLE_FAIL);
        }
    }

    @Override
    public void increaseCommentNum(Integer articleId) {
        int result = articleMapper.increaseCommentNum(articleId);
        if (result != 1) {
            throw new ServiceException(StrConstant.UPDATE_ARTICLE_FAIL);
        }
    }

    @Override
    public void deleteArticle(Integer id) {
        Article article = new Article();
        article.setId(id);
        article.setIsDelete(DeleteEnum.DELETE.getCode());
        int result = articleMapper.updateById(article);
        if (result != 1) {
            throw new ServiceException(StrConstant.DELETE_ARTICLE_FAIL);
        }
    }
}
