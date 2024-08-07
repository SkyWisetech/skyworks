package org.skywise.skyworks.common.schedules;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.skywise.skyworks.common.DTO.CommentOptDTO;
import org.skywise.skyworks.common.DTO.UpvoteOptDTO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.enums.CaffeineNameEnum;
import org.skywise.skyworks.common.enums.UpvoteEnum;
import org.skywise.skyworks.model.Article;
import org.skywise.skyworks.model.Collect;
import org.skywise.skyworks.model.DailyData;
import org.skywise.skyworks.model.Upvote;
import org.skywise.skyworks.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: LIGHT
 * @Date: 2024/7/17 星期三 10:55
 * @Description:
 */
@Slf4j
@Component
public class CaffeineDailySchedule {
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

    @Resource
    private Cache<String, Long> totalDataCache;

    @Resource
    private Cache<Integer, Set<Integer>> upvoteArticleCache;

    @Resource
    private Cache<Integer, Set<Integer>> collectArticleCache;

    @Resource
    private Cache<String, Set<Integer>> labelTopFourCache;

    @Resource
    private Cache<Integer, Integer> articleIdToViewNumCache;

    /**
     * 作用：每天凌晨更新：文章总量、点赞总量、评论总量、用户总量、浏览总量、昨日新增
     * 保存：定时任务每天插入一条每日新增数据
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void updateDailyDataTask() {
        log.info("更新日期：{}，更新开始：文章总量、点赞总量、评论总量、用户总量、浏览总量、昨日新增", LocalDate.now());
        //  文章总量、点赞总量、评论总量、用户总量、昨日新增用户、月新增用户 存入缓存中
        Long articleNum = articleService.getArticleCount();
        Long upvoteNum = upvoteService.getUpvoteCount();
        Long commentNum = commentService.getCommentCount(new CommentOptDTO());
        Long userNum = userService.getUserCount();
        Long viewNum = articleIdToViewNumCache.asMap().values().stream().mapToLong(Integer::longValue).sum();

        // 每天插入一条新记录
        DailyData dailyData = new DailyData();
        Long newlyIncreaseArticle = articleNum - totalDataCache.getIfPresent(CaffeineNameEnum.ARTICLE_NUM.getName_en());
        Long newlyIncreaseUpvote = upvoteNum - totalDataCache.getIfPresent(CaffeineNameEnum.UPVOTE_NUM.getName_en());
        Long newlyIncreaseComment = commentNum - totalDataCache.getIfPresent(CaffeineNameEnum.COMMENT_NUM.getName_en());
        Long newlyIncreaseUser = userNum - totalDataCache.getIfPresent(CaffeineNameEnum.USER_NUM.getName_en());
        Long newlyIncreaseView = viewNum - totalDataCache.getIfPresent(CaffeineNameEnum.VIEW_NUM.getName_en());
        dailyData.setNewArticle(newlyIncreaseArticle.intValue());
        dailyData.setNewUpvote(newlyIncreaseUpvote.intValue());
        dailyData.setNewComment(newlyIncreaseComment.intValue());
        dailyData.setNewUser(newlyIncreaseUser.intValue());
        dailyData.setNewView(newlyIncreaseView.intValue());
        dailyData.setDailyDate(LocalDate.now().toString());
        dailyDataService.insertDailyData(dailyData);

        // 更新缓存
        totalDataCache.put(CaffeineNameEnum.ARTICLE_NUM.getName_en(), articleNum);
        totalDataCache.put(CaffeineNameEnum.UPVOTE_NUM.getName_en(), upvoteNum);
        totalDataCache.put(CaffeineNameEnum.COMMENT_NUM.getName_en(), commentNum);
        totalDataCache.put(CaffeineNameEnum.USER_NUM.getName_en(), userNum);
        totalDataCache.put(CaffeineNameEnum.VIEW_NUM.getName_en(), viewNum);
        log.info("更新日期：{}，更新结束：文章总量、点赞总量、评论总量、用户总量、浏览总量、昨日新增", LocalDate.now());
    }

    /**
     * 作用：每天凌晨将缓存中的点赞数更新到数据库中
     * 更新：每天运行一遍，保存数据到数据库
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void updateUpvoteNumDailyTask() {
        log.info("更新日期：{}，更新开始：缓存中的点赞数更新到数据库中", LocalDate.now());
        // 1、更新文章的点赞数
        List<Article> articleList = new ArrayList<>();
        Set<Integer> upvoteArticleIdSet = upvoteArticleCache.asMap().keySet();
        for (Integer articleId : upvoteArticleIdSet) {
            Article article = new Article();
            article.setId(articleId);
            article.setUpvoteNum(upvoteArticleCache.getIfPresent(articleId).size());
            articleList.add(article);
        }
        //更新所有文章的点赞数
        articleService.updateBatchUpvote(articleList);
        log.info("更新文章点赞数成功");

        // 2、更新点赞表：记录[文章ID：用户ID]
        UpvoteOptDTO upvoteOptDTO = new UpvoteOptDTO();
        upvoteOptDTO.setType(UpvoteEnum.ARTICLE.getCode());
        List<Upvote> upvoteList = upvoteService.listUpvote(upvoteOptDTO);

        // 遍历缓存中的所有键值对
        Map<Integer, Set<Integer>> upvoteArticleCacheMap = upvoteArticleCache.asMap();
        List<Upvote> newUpvoteList = new ArrayList<>();
        for (Map.Entry<Integer, Set<Integer>> entry : upvoteArticleCacheMap.entrySet()) {
            // 筛选出这篇文章下哪些用户点赞了
            List<Integer> userIdList = upvoteList.stream().filter(upvote -> upvote.getArtComId().equals(entry.getKey()))
                    .map(Upvote::getUserId).collect(Collectors.toList());
            Set<Integer> userIdCacheSet = entry.getValue();
            for (Integer userId : userIdCacheSet) {
                // 如果点赞不存在，则新增一条记录
                if (!userIdList.contains(userId)) {
                    Upvote upvote = new Upvote();
                    upvote.setType(UpvoteEnum.ARTICLE.getCode());
                    upvote.setArtComId(entry.getKey());
                    upvote.setId(userId);
                    newUpvoteList.add(upvote);
                }
            }
        }
        // 插入所有新记录
        if (!newUpvoteList.isEmpty()) {
            upvoteService.insertUpvoteList(newUpvoteList);
            log.info("插入用户点赞记录成功");
        }
        log.info("更新日期：{}，更新结束：缓存中的点赞数更新到数据库中", LocalDate.now());
    }

    /**
     * 作用：每天凌晨保存文章的收藏信息保存到数据库中
     * 更新：每天运行一遍，保存数据到数据库
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void updateCommentNumDailyTask() {
        log.info("更新日期：{}，更新开始：缓存中的文章收藏信息保存到数据库中", LocalDate.now());
        // 1、更新文章表：更新文章的收藏数
        List<Article> articleList = new ArrayList<>();
        Set<Integer> collectArticleIdSet = collectArticleCache.asMap().keySet();
        for (Integer articleId : collectArticleIdSet) {
            Article article = new Article();
            article.setId(articleId);
            article.setCollectNum(collectArticleCache.getIfPresent(articleId).size());
            articleList.add(article);
        }
        articleService.updateBatchCollect(articleList);
        log.info("更新文章收藏数成功");

        // 2、更新收藏表：记录[文章ID：用户ID]
        List<Collect> collectList = collectService.listCollect(null);

        // 遍历缓存中的所有键值对
        Map<Integer, Set<Integer>> collectArticleCacheMap = collectArticleCache.asMap();
        List<Collect> newCollectList = new ArrayList<>();
        for (Map.Entry<Integer, Set<Integer>> entry : collectArticleCacheMap.entrySet()) {
            // 筛选出这篇文章下哪些用户点赞了
            List<Integer> userIdList = collectList.stream().filter(collect -> collect.getArticleId().equals(entry.getKey()))
                    .map(Collect::getUserId).collect(Collectors.toList());
            Set<Integer> userIdCacheSet = entry.getValue();
            for (Integer userId : userIdCacheSet) {
                // 如果收藏不存在，则新增一条记录
                if (!userIdList.contains(userId)) {
                    Collect collect = new Collect();
                    collect.setUserId(userId);
                    collect.setArticleId(entry.getKey());
                    newCollectList.add(collect);
                }
            }
        }
        // 插入所有新记录
        if (!newCollectList.isEmpty()) {
            collectService.insertBatch(newCollectList);
            log.info("插入用户收藏记录成功");
        }
        log.info("更新日期：{}，更新结束：缓存中的文章收藏信息保存到数据库中", LocalDate.now());
    }

    /**
     * 作用：每天凌晨更新，更新签数量最多的四个标签保存到缓存中
     * 查询：从缓存中查询
     * 更新：每天运行一遍，更新缓存
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void updateLabelTopFourDailyTask() {
        log.info("更新日期：{}，更新开始：更新签数量最多的四个标签保存到缓存中", LocalDate.now());
        // 缓存文章数量最多的，前四个标签
        List<Article> articleList = articleService.listAllArticle();
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
        for (int i = 0; i < 5; i++) {
            Map.Entry<Integer, Set<Integer>> entry = entryList.get(i);
            labelIdSet.add(entry.getKey());
        }
        labelTopFourCache.put(StrConstant.LABEL_TOP_FOUR, labelIdSet);
        log.info("更新日期：{}，更新结束：更新签数量最多的四个标签保存到缓存中", LocalDate.now());
    }

    /**
     * 作用：每天凌晨更新，将文章的浏览量保存到数据库中
     * 保存：每天更新一次到数据库即可
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void updateArticleViewNumDailyTask() {
        log.info("更新日期：{}，更新开始：将文章的浏览量保存到数据库中", LocalDate.now());
        List<Article> articleList = new ArrayList<>();
        Set<Integer> articleIdSet = articleIdToViewNumCache.asMap().keySet();
        for (Integer articleId : articleIdSet) {
            Article article = new Article();
            article.setId(articleId);
            article.setViewNum(articleIdToViewNumCache.getIfPresent(articleId));
            articleList.add(article);
        }
        articleService.updateBatchView(articleList);
        log.info("更新日期：{}，更新结束：将文章的浏览量保存到数据库中", LocalDate.now());
    }
}
