package org.skywise.skyworks.common.DTO;

import lombok.Data;

/**
 * @Author: LIGHT
 * @Date: 2024/7/11 星期四 17:36
 * @Description:
 */
@Data
public class CaffeineDTO {
    private Integer articleId;

    private Boolean isUpvote;

    private Boolean isCollect;
}
