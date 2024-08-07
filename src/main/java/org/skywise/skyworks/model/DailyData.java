package org.skywise.skyworks.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DailyData {
    private Integer id;

    /**
     * 新增文章数
     */
    private Integer newArticle;

    /**
     * 新增点赞数
     */
    private Integer newUpvote;

    /**
     * 新增评论数
     */
    private Integer newComment;

    /**
     * 新增用户数
     */
    private Integer newUser;

    /**
     * 新增浏览量
     */
    private Integer newView;

    /**
     * 统计日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String dailyDate;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm", timezone = "GMT+8")
    private Date createTime;

    public static final String DAILY_DATE_COLUMN = "daily_date";
    public static final String CREATE_TIME_COLUMN = "create_time";

}