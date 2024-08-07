package org.skywise.skyworks.service;

import org.skywise.skyworks.common.DTO.ArticleDTO;
import org.skywise.skyworks.common.DTO.ArticleOptDTO;
import org.skywise.skyworks.common.VO.ArticlePageVO;
import org.skywise.skyworks.common.VO.ArticleVO;
import org.skywise.skyworks.model.Article;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 11:17
 * @Description:
 */
public interface IArticleService {
    Long getArticleCount();

    ArticleVO getArticleById(Integer id);

    ArticlePageVO listArticle(ArticleOptDTO articleOptDTO);

    ArticlePageVO listArticleByLabel(ArticleOptDTO articleOptDTO);

    List<Article> listAllArticle();

    List<Article> listArticleByIds(List<Integer> ids);

    void insertArticle(ArticleDTO articleDTO);

    void updateArticle(ArticleDTO articleDTO);

    void updateBatchUpvote(List<Article> articleList);

    void updateBatchCollect(List<Article> articleList);

    void updateBatchView(List<Article> articleList);

    void updateArticleInfo(ArticleOptDTO articleOptDTO);

    void increaseCommentNum(Integer articleId);

    void deleteArticle(Integer id);

}
