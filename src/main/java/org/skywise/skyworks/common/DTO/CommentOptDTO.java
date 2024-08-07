package org.skywise.skyworks.common.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 14:52
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentOptDTO extends PageDTO{
    private Integer articleId;

    private Integer parentId;

    private Integer userId;
}
