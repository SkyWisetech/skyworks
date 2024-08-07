package org.skywise.skyworks.common.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import org.skywise.skyworks.common.DTO.CollectOptDTO;
import org.skywise.skyworks.common.DTO.CommentOptDTO;
import org.skywise.skyworks.common.DTO.LabelDTO;
import org.skywise.skyworks.common.DTO.UpvoteOptDTO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.enums.CaffeineNameEnum;
import org.skywise.skyworks.common.enums.UpvoteEnum;
import org.skywise.skyworks.model.*;
import org.skywise.skyworks.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 15:42
 * @Description: 加载数据
 */
@Component
public class InitDataCaffeine {
    @Autowired
    private IArticleService articleService;

    @Autowired
    private IUpvoteService upvoteService;

    @Autowired
    private ICollectService collectService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDailyDataService dailyDataService;

    @Autowired
    private ILabelService labelService;

    @Resource
    private Cache<String, Long> totalDataCache;

    @Resource
    private Cache<Integer, Set<Integer>> labelCache;

    @Resource
    private Cache<Integer, String> labelNameCache;

    @Resource
    private Cache<Integer, Set<Integer>> upvoteArticleCache;

    @Resource
    private Cache<Integer, Set<Integer>> collectArticleCache;

    @Resource
    private Cache<Integer, Set<Integer>> userIdToCollectCache;

    @Resource
    private Cache<String, Set<Integer>> labelTopFourCache;

    @Resource
    private Cache<Integer, Integer> articleIdToViewNumCache;

    /**
     * 作用：项目启动时缓存所有文章的浏览量
     * 查询，修改：后续只对缓存进行修改
     * 保存：定时任务每天更新一次到数据库即可
     */
    private void loadArticleViewNum(List<Article> articleList) {
        // 文章的浏览总量
        long sum = 0;
        for (Article article : articleList) {
            int viewNum = article.getViewNum();
            articleIdToViewNumCache.put(article.getId(), viewNum);
            sum += viewNum;
        }
        // 浏览总量更新到缓存中
        totalDataCache.put(CaffeineNameEnum.VIEW_NUM.getName_en(), sum);
    }

    /**
     * 作用：项目启动时缓存每篇文章的收藏用户ID，缓存每个用户的收藏文章ID
     * 查询，修改：后续只对缓存进行修改
     * 保存：定时任务每天保存一次到数据库即可
     */
    @PostConstruct
    public void loadCollect() {
        // 缓存收藏该文章的所有用户[文章ID：用户ID列表]
        CollectOptDTO collectOptDTO = new CollectOptDTO();
        List<Collect> collectList = collectService.listCollect(collectOptDTO);
        for (Collect collect : collectList) {
            // 每篇文章的收藏用户ID列表
            Set<Integer> userIdSet = collectArticleCache.get(collect.getArticleId(), k -> new HashSet<>());
            userIdSet.add(collect.getUserId());
            collectArticleCache.put(collect.getArticleId(), userIdSet);

            // 用户的收藏文章ID列表
            Set<Integer> articleIdSet = userIdToCollectCache.get(collect.getUserId(), k -> new HashSet<>());
            articleIdSet.add(collect.getArticleId());
            userIdToCollectCache.put(collect.getUserId(), articleIdSet);
        }
    }

    /**
     * 作用：项目启动时缓存每篇文章的点赞用户ID
     * 查询，修改：后续只对缓存进行修改
     * 保存：定时任务每天保存一次到数据库即可
     */
    @PostConstruct
    public void loadUpvote() {
        // 缓存点赞该文章的所有用户[文章ID：用户ID列表]
        UpvoteOptDTO upvoteOptDTO = new UpvoteOptDTO();
        upvoteOptDTO.setType(UpvoteEnum.ARTICLE.getCode());
        List<Upvote> upvoteList = upvoteService.listUpvote(upvoteOptDTO);
        for (Upvote upvote : upvoteList) {
            Set<Integer> userIdSet = upvoteArticleCache.get(upvote.getArtComId(), k -> new HashSet<>());
            userIdSet.add(upvote.getUserId());
            upvoteArticleCache.put(upvote.getArtComId(), userIdSet);
        }
    }

    /**
     * 作用：项目启动时缓存文章数量、点赞数量、评论数量、用户总量、新增用户量、每月
     * 查询，修改：后续只对缓存进行修改
     * 保存：定时任务每天保存一次到数据库即可
     */
    @PostConstruct
    public void loadCountData() {
        //  文章总量、点赞总量、评论总量、用户总量、昨日新增用户、月新增用户 存入缓存中
        Long articleNum = articleService.getArticleCount();
        Long upvoteNum = upvoteService.getUpvoteCount();
        Long commentNum = commentService.getCommentCount(new CommentOptDTO());
        Long userNum = userService.getUserCount();
        List<DailyData> monthlyDataList = dailyDataService.listDailyData(LocalDate.now(), 30);
        Integer dailyNewUserNum = monthlyDataList.get(monthlyDataList.size() - 1).getNewUser();
        int monthlyNewUserNum = monthlyDataList.stream().mapToInt(DailyData::getNewUser).sum();

        totalDataCache.put(CaffeineNameEnum.ARTICLE_NUM.getName_en(), articleNum);
        totalDataCache.put(CaffeineNameEnum.UPVOTE_NUM.getName_en(), upvoteNum);
        totalDataCache.put(CaffeineNameEnum.COMMENT_NUM.getName_en(), commentNum);
        totalDataCache.put(CaffeineNameEnum.USER_NUM.getName_en(), userNum);
        totalDataCache.put(CaffeineNameEnum.DAILY_NEW_USER_NUM.getName_en(), dailyNewUserNum.longValue());
        totalDataCache.put(CaffeineNameEnum.MONTHLY_NEW_USER_NUM.getName_en(), (long) monthlyNewUserNum);
    }

    /**
     * 作用：项目启动时查询文章数量最多的四个标签
     * 查询：从缓存中查询
     * 更新：定时任务每天运行一遍，更新缓存
     */
    @PostConstruct
    public void loadLabelTopFour() {
        List<Article> articleList = articleService.listAllArticle();

        // 缓存文章浏览量
        this.loadArticleViewNum(articleList);

        // 缓存文章数量最多的，前四个标签
        Map<Integer, Set<Integer>> labelTopFourMap = new HashMap<>();
        for (Article article : articleList) {
            Set<Integer> articleIdSet = labelTopFourMap.getOrDefault(Integer.valueOf(article.getLabel()), new HashSet<>());
            articleIdSet.add(article.getId());
            labelTopFourMap.put(Integer.valueOf(article.getLabel()), articleIdSet);
        }

        // 排序
        List<Map.Entry<Integer, Set<Integer>>> entryList = new ArrayList<>(labelTopFourMap.entrySet());
        Collections.sort(entryList, (o1, o2) -> {
            // 降序排序
            if (o1.getValue().size() >= o2.getValue().size()) {
                return -1;
            }
            return 1;
        });
        // 获取文章最多的四个标签
        Set<Integer> labelIdSet = new HashSet<>();
        int maxNum = Math.min(entryList.size(), 4);
        for (int i = 0; i < maxNum; i++) {
            Map.Entry<Integer, Set<Integer>> entry = entryList.get(i);
            labelIdSet.add(entry.getKey());
        }
        labelTopFourCache.put(StrConstant.LABEL_TOP_FOUR, labelIdSet);
    }

    /**
     * 作用：项目启动时缓存一二级标签
     * 查询：从缓存中查询
     * 更新：新增修改标签时，同时修改缓存
     */
    @PostConstruct
    public void loadLabelOneToTwo() {
        // 缓存[一级标签:二级标签列表]
        List<Label> labelList = labelService.listLabel(new LabelDTO());
        for (Label label : labelList) {
            // 缓存标签名称
            labelNameCache.put(label.getId(), label.getLabelName());

            // 缓存一二级标签
            if (!label.getParentId().equals(0)) {
                Set<Integer> secondLabelSet = labelCache.get(label.getParentId(), k -> new HashSet<>());
                secondLabelSet.add(label.getId());
                labelCache.put(label.getParentId(), secondLabelSet);
            }
        }
    }

}
