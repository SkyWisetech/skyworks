package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 17:36
 * @Description: 是否是黑名单用户
 */
@Getter
@AllArgsConstructor
public enum BlackEnum {
    WHITE(0, "正常用户"),
    BLACK(1, "拉黑用户")
    ;

    final Integer code;
    final String msg;
}
