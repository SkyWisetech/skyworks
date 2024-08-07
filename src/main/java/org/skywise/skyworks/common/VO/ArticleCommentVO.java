package org.skywise.skyworks.common.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/8/2 星期五 15:58
 * @Description:
 */
@Data
public class ArticleCommentVO {
    private Integer id;
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

    List<CommentVO> childCommentList = new ArrayList<>();
}
