package org.skywise.skyworks.common.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: LIGHT
 * @Date: 2024/7/24 星期三 10:47
 * @Description:
 */
@Data
public class CommentVO {
    private Integer parentId;
    private Integer id;
    private String title;
    private Integer userId;
    private String userName;
    private Integer articleId;
    private Integer upvoteNum;
    /**
     * 评价详情
     */
    private String content;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
