package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 13:23
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ClaimEnum {
    USER_ID("userId", "用户ID"),
    EMAIL("email", "邮箱");

    final String columnName;
    final String msg;
}
