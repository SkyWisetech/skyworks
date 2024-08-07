package org.skywise.skyworks.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 点赞数
     */
    private Integer upvoteNum;

    /**
     * 收藏数
     */
    private Integer collectNum;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 评论数
     */
    private Integer viewNum;

    /**
     * 标签ID，逗号分割
     */
    private String label;

    /**
     * 封面
     */
    private String imageName;

    /**
     * 正文
     */
    private String content;

    /**
     * 是否被删除：0否 1是
     */
    private Integer isDelete;

    /**
     * 权重
     */
    private Integer priority;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    public static final String ID_COLUMN = "id";
    public static final String TITLE = "title";
    public static final String LABEL_COLUMN = "label";
    public static final String IS_DELETE = "is_delete";
    public static final String UPDATE_TIME_COLUMN = "update_time";
    public static final String UPVOTE_NUM_COLUMN = "upvote_num";
    public static final String COMMENT_NUM_COLUMN = "comment_num";
    public static final String COLLECT_NUM_COLUMN = "collect_num";
    public static final String VIEW_NUM_COLUMN = "view_num";
    public static final String PRIORITY_COLUMN = "priority";
}