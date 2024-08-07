package org.skywise.skyworks.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id;

    /**
     * 父级ID：0表示评论文章 其他表示评价评论
     */
    private Integer parentId;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 评价详情
     */
    private String content;

    /**
     * 被赞数量
     */
    private Integer upvote;

    /**
     * 是否被删除：0否 1是
     */
    private Integer isDelete;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    public static final String COMMENT_ID = "id";
    public static final String ARTICLE_ID = "article_id";
    public static final String IS_DELETE = "is_delete";

}