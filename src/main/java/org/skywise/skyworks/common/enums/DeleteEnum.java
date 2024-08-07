package org.skywise.skyworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 14:12
 * @Description: 是否被删除
 */
@Getter
@AllArgsConstructor
public enum DeleteEnum {
    NOT_DELETE(0, "没有被删除"),
    DELETE(1, "已经被删除"),
    ;

    final Integer code;
    final String msg;
}
