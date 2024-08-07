package org.skywise.skyworks.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LIGHT
 * @Date: 2024/7/11 星期四 10:40
 * @Description:
 */
public interface IUtilService {
    void sendVerifyCode(String email);

    String uploadImage(MultipartFile image);

    void getImage(String imageName, HttpServletResponse response);
}
