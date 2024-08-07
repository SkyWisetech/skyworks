package org.skywise.skyworks.common.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.skywise.skyworks.model.User;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 14:48
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {
    Integer collectNum;

    Integer commentNum;
}
