package org.skywise.skyworks.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 15:45
 * @Description: 缓存配置类
 */
@Configuration
@EnableCaching
public class CaffeineCacheConfig {
    /**
     * 文章的浏览量
     */
    @Bean
    public Cache<Integer, Integer> articleIdToViewNumCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(100000)
                .build();
    }

    /**
     * 文章总量、点赞总量、评论总量、用户总量、昨日新增用户、月新增用户 存入缓存中
     */
    @Bean
    public Cache<String, Long> totalDataCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(100000)
                .build();
    }

    /**
     * 缓存文章数量最多的，前四个标签
     */
    @Bean
    public Cache<String, Set<Integer>> labelTopFourCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }


    /**
     * 缓存[一级标签:二级标签列表]
     */
    @Bean
    public Cache<Integer, Set<Integer>> labelCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 缓存[标签ID:标签名称]
     */
    @Bean
    public Cache<Integer, String> labelNameCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 存储验证码
     */
    @Bean
    public Cache<String, String> verifyCodeCache() {
        return Caffeine.newBuilder()
                //最后一次写操作后经过指定时间过期
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 缓存[文章ID:点赞用户ID]
     */
    @Bean
    public Cache<Integer, Set<Integer>> upvoteArticleCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 缓存[文章ID:收藏用户ID]
     */
    @Bean
    public Cache<Integer, Set<Integer>> collectArticleCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 缓存[用户ID：收藏文章列表]
     */
    @Bean
    public Cache<Integer, Set<Integer>> userIdToCollectCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }
}

