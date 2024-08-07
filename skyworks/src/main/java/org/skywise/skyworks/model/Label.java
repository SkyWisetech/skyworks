package org.skywise.skyworks.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Label {
    private Integer id;

    /**
     * 父级ID：0 一级标签 其余是二级标签
     */
    private Integer parentId;

    /**
     * 权重 首页文章排序，字段值从小到大，文章排版从左往右
     */
    private Integer priority;

    /**
     * 颜色-待使用
     */
    private Integer color;

    /**
     * 标签名
     */
    private String labelName;

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

    public static final String LABEL_ID = "id";

}