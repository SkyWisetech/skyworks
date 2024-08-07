package org.skywise.skyworks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.skywise.skyworks.model.Article;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 11:18
 * @Description: 文章管理
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    @Update("UPDATE article SET comment_num = comment_num + 1 WHERE id = #{articleId}")
    int increaseCommentNum(@Param("articleId") Integer articleId);

    void updateBatchUpvote(List<Article> articleList);

    void updateBatchCollect(List<Article> articleList);

    void updateBatchView(List<Article> articleList);
}
