package org.skywise.skyworks.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 10:36
 * @Description: 解决跨域问题
 */
@Configuration // 一定不要忽略此注解
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域访问的路径
                .allowCredentials(true) // 是否发送 Cookie
                .allowedOriginPatterns("*") // 支持域
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"}) // 支持方法
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

    /* 某些版本的写法
   public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域访问的路径
                .allowedOrigins("*")//允许跨域访问的源
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许请求方法
                .maxAge(168000) // 预检间隔时间
                .allowedHeaders("*") //允许头部设置
                .allowCredentials(true); // 是否发送 Cookie
    }*/
}
