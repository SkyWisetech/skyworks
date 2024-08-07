package org.skywise.skyworks.common.VO;

import lombok.Data;

/**
 * @Author: LIGHT
 * @Date: 2024/7/23 星期二 14:55
 * @Description: 分页参数
 */
@Data
public class PageVO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long totalCount;
    private Integer totalPage;
}
