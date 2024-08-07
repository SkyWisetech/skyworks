package org.skywise.skyworks.common.DTO;

import lombok.Data;

/**
 * @Author: LIGHT
 * @Date: 2024/7/10 星期三 16:17
 * @Description:
 */
@Data
public class PageDTO {
    /**
     * 分页参数
     * */
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
