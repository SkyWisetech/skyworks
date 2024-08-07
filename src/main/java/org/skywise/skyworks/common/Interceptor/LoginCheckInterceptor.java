package org.skywise.skyworks.common.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.skywise.skyworks.common.utils.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LIGHT
 * @Date: 2024/7/10 星期三 17:21
 * @Description:
 */

//自定义拦截器
@Component //当前拦截器对象由Spring创建和管理
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    //前置方式
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //1、判断是否需要拦截
        //拦截器取到请求先进行判断，如果是OPTIONS请求，则放行
        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("Method:OPTIONS");
            return true;
        }
        System.out.println("preHandle .... ");

        //2、解析token，如果解析失败，返回错误 结果（未登录）
        try {
            TokenUtil.saveUserInfo(request);
        } catch (Exception e) {
            return false;//不放行
        }

        //3、放行
        return true;
    }

}

