package org.skywise.skyworks.common.VO;

import lombok.Data;

/**
 * @Author: LIGHT
 * @Date: 2024/7/22 星期一 15:56
 * @Description:
 */
@Data
public class TotalNumVO {
    Long articleTotalNum;
    Long upvoteTotalNum;
    Long commentTotalNum;
    Long userTotalNum;
    Long viewTotalNum;
}
