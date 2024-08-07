package org.skywise.skyworks.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.skywise.skyworks.common.DTO.Response;
import org.skywise.skyworks.common.enums.ReturnCodeEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 17:09
 * @Description:
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /* 添加校验参数异常处理 */
    @ExceptionHandler(BindException.class)
    public Response<String> bindExceptionHandler(BindException e) {
        return Response.fail(ReturnCodeEnum.ERROR.getCode(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 自定义抛出异常
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Response<String> handle(ServiceException se){
        return Response.fail(se.getCode(), se.getMsg());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Response<String> handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Response.fail(ReturnCodeEnum.UNKNOWN_ERROR.getCode(), e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Response<String> handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Response.fail(ReturnCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }
}