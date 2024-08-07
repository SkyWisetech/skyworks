package org.skywise.skyworks.common.config;

import org.skywise.skyworks.common.Interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: LIGHT
 * @Date: 2024/7/10 星期三 17:24
 * @Description: 拦截器，验证需要登录才能访问的端口
 */

//注册拦截器配置
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    //自定义的拦截器对象
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    /**
     * 重写 addInterceptors() 实现拦截器
     * 配置：要拦截的路径以及不拦截的路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)  //配置拦截规则
                .addPathPatterns("/**")  // 拦截所有请求，通过判断token是否合法来决定是否需要登录
                .excludePathPatterns(
                        "/skyworks/user/loginByVerifyCode", //用户验证码登录
                        "/skyworks/user/loginByPassword", //用户密码登录
                        "/skyworks/user/register", //用户注册
                        "/skyworks/user/adminLogin", //管理员登录
                        "/skyworks/user/updatePassword", //重置密码
                        "/skyworks/util/email/sendVerifyCode", //邮箱发送验证码
                        "/skyworks/article/listArticle", // 查看所有文章
                        "/skyworks/article/listArticleByLabel", //通过标签查询文章
                        "/skyworks/label/listLabel", //查询标签
                        "/skyworks/image/**" //访问图片
                );//允许匿名访问放行的请求
    }
}

