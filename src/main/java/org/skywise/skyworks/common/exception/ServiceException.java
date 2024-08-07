package org.skywise.skyworks.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 9:40
 * @Description:
 */
@Getter
public class ServiceException extends RuntimeException {
    private Integer code;
    private String msg;


    public ServiceException(Integer code, String msg) {
        this.msg= msg;
        this.code = code;
    }

    public ServiceException(String msg) {
        this.msg= msg;
        this.code = 400;
    }

}
