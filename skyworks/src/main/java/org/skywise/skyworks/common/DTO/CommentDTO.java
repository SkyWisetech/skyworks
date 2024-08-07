package org.skywise.skyworks.common.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 14:47
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDTO extends PageDTO{
    /**
     * 评论ID
     * */
    private Integer commentId;

    /**
     * 父级ID：0表示评论文章 其他表示评价评论
     */
    private Integer parentId;

    /**
     * 文章ID
     */
    @NotNull(message = "文章ID不能为空")
    private Integer articleId;

    /**
     * 评价详情
     */
    @NotBlank(message = "评论不能为空")
    private String content;

}
