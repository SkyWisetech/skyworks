package org.skywise.skyworks.common.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 13:49
 * @Description:
 */
@Data
public class ArticleVO {
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
     * 浏览量数
     */
    private Integer viewNum;

    /**
     * 一级标签ID
     */
    private Integer parentLabelId;
    private String parentLabelName;

    /**
     * 标签ID，逗号分割
     */
    private String label;


    private String labelName;
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

    Boolean isCollected;

    Boolean isUpvoted;

}
