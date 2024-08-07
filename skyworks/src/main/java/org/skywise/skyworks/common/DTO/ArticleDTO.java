package org.skywise.skyworks.common.DTO;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 11:23
 * @Description: 新增文章
 */
@Data
public class ArticleDTO {
    Integer id;

    @NotBlank(message = "文章标题不能为空")
    String title;

    @NotBlank(message = "文章正文不能为空")
    String content;

    @NotBlank(message = "文章封面不能为空")
    String imageName;

    @NotBlank(message = "标签不能为空")
    String label;
}
