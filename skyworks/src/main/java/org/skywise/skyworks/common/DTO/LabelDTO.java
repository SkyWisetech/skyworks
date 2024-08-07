package org.skywise.skyworks.common.DTO;

import lombok.Data;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 17:12
 * @Description:
 */
@Data
public class LabelDTO {
    private Integer id;

    private Integer parentId;

    private Integer priority;

    private Integer color;

    private String labelName;
}
