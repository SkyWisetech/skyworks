package org.skywise.skyworks.common.config;

import org.skywise.skyworks.common.constant.StrConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: LIGHT
 * @Date: 2024/7/11 星期四 10:02
 * @Description: 图片上传，定义静态资源位置
 */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Value("${upload.upload-path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // .addResourceHandler("/image/**")：定义了一个URL模式，即以 "/static/" 开头的请求路径。任何匹配该模式的请求都将被映射到相应的静态资源。
        // .addResourceLocations("file:" + imagepath)：指定了实际存储静态资源的文件系统路径，"file:" 前缀告诉Spring MVC，这是一个文件系统路径。
        registry.addResourceHandler(StrConstant.IMAGE_PATH + "**")
                .addResourceLocations("file:" + uploadPath);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
