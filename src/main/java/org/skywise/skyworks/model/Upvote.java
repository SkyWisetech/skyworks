package org.skywise.skyworks.model;

import lombok.Data;

import java.util.Date;

@Data
public class Upvote {
    private Integer id;

    /**
     * 1文章点赞 2评论点赞
     */
    private Integer type;

    /**
     * 文章/评论ID
     */
    private Integer artComId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 创建日期
     */
    private Date createTime;

    public static final String TYPE_COLUMN = "type";

}