package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 15:00
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ParentEnum {
    IS_PARENT(0, "是父级"),
    NOT_PARENT(1, "不是父级"),
    ;

    final Integer code;
    final String msg;
}
