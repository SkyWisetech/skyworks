package org.skywise.skyworks.common.DTO;

import lombok.Data;
import org.skywise.skyworks.common.enums.ReturnCodeEnum;

import java.io.Serializable;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 10:34
 * @Description: 统一返回的类型，全局都需要，不能动
 */
@Data
public class Response<T> implements Serializable {
    Integer code = 200;
    String msg = "成功";
    private T data;

    public static <T> Response<T> success() {
        Response<T> response = new Response<>();
        response.setCode(ReturnCodeEnum.SUCCESS.getCode());
        response.setMsg(ReturnCodeEnum.SUCCESS.getMsg());
        return response;
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode(ReturnCodeEnum.SUCCESS.getCode());
        response.setMsg(ReturnCodeEnum.SUCCESS.getMsg());
        response.setData(data);
        return response;
    }

    public static <T> Response<T> successMsg(String msg) {
        Response<T> response = new Response<>();
        response.setCode(ReturnCodeEnum.SUCCESS.getCode());
        response.setMsg(msg);
        return response;
    }


    public static <T> Response<T> fail(Integer code, String msg) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static <T> Response<T> fail(String msg) {
        Response<T> response = new Response<>();
        response.setCode(400);
        response.setMsg(msg);
        return response;
    }

}
