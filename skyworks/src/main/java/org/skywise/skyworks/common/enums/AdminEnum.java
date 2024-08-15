package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/8/9 星期五 15:31
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum AdminEnum {
    ADMIN_USER_ID(0, "管理员ID"),
    ADMIN_USER_NAME(1, "admin");

    final Integer code;
    final String msg;
}
