package org.skywise.skyworks.common.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 13:19
 * @Description: 查询文章
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleOptDTO extends PageDTO {
    /**
     * 标签ID
     */
    Integer labelId;

    /**
     * 评论ID
     */
    Integer commentId;

    /**
     * 文章ID
     */
    Integer articleId;

    /**
     * 标题
     */
    String title;

    /**
     * 点赞数
     */
    Integer upvoteNum;

    /**
     * 收藏数量
     */
    Integer collectNum;

    /**
     * 评论数量
     */
    Integer commentNum;

    /**
     * 文章创建日期起
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 文章创建日期止
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
