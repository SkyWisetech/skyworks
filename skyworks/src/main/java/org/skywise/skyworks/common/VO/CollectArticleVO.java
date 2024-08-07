package org.skywise.skyworks.common.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.skywise.skyworks.common.DTO.PageDTO;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 15:21
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CollectArticleVO extends PageDTO {
    private Integer articleId;

    private String articleTitle;
}
