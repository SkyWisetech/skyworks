package org.skywise.skyworks.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.exception.ServiceException;
import org.skywise.skyworks.common.utils.VerifyCodeUtil;
import org.skywise.skyworks.service.IUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: LIGHT
 * @Date: 2024/7/11 星期四 10:40
 * @Description:
 */
@Service
public class UtilServiceImpl implements IUtilService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Cache<String, String> verifyCodeCache;

    @Value("${upload.upload-path}")
    private String uploadPath;

    @Override
    public void sendVerifyCode(String email) {
        // 获取随机6位验证码
        String verify = VerifyCodeUtil.generateVerifyCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1270762753@qq.com"); // 发送邮箱
        message.setTo(email); // 接受邮箱
        message.setSubject("天亦智云-邮箱验证"); // 标题

        // 内容
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>尊敬的用户您好：</h1><br>");
        builder.append("<h5> 您正在进行邮箱验证，本次验证码为：<span style='color:#ec0808;font-size: 20px;'>");
        builder.append(verify);
        builder.append("</span>，请在10分钟内进行使用。</h5>");
        builder.append("<h5>如非本人操作，请忽略此邮件，由此给您带来的不便请谅解！</h5> <h5 style='text-align: right;'></h5>");
        message.setText(builder.toString());
        System.out.println(builder);

        // 发送邮件
        try {
            mailSender.send(message);
        } catch (Exception e) {
            //TODO 异常打印
            throw new ServiceException(StrConstant.SEND_AUTH_CODE_FAIL);
        }

        // 将验证码存入内存中
        verifyCodeCache.put(email, verify);
    }

    @Override
    public String uploadImage(MultipartFile image) {
        // 重构图片名称
        String originalFilename = image.getOriginalFilename();
        // 使用Spring的工具类来获取文件扩展名
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        long timestamp = System.currentTimeMillis();
        String newImageName = timestamp + "." + fileExtension;
        System.out.println(newImageName);

        File imageDir = new File(uploadPath);
        // 保存图片
        try {
            image.transferTo(new File(imageDir.getAbsoluteFile(), newImageName));
        } catch (Exception e){
            System.out.println(e);
            throw new ServiceException(StrConstant.UPLOAD_IMAGE_FAIL);
        }
        return StrConstant.RETURN_IMAGE_PATH + newImageName;
    }

    @Override
    public void getImage(String imageName, HttpServletResponse response) {
        String imageUrl = uploadPath + imageName;
        System.out.println(imageUrl);

        FileInputStream in = null;
        OutputStream out = null;
        try {
            File file = new File(imageUrl);
            if (!file.exists()) {
                throw new ServiceException(StrConstant.GET_IMAGE_FAIL);
            }
            in = new FileInputStream(imageUrl);
            int i = in.available();
            byte[] buffer = new byte[i];
            in.read(buffer);
            //设置输出流内容格式为图片格式
            response.setContentType("image/jpeg");
            //response的响应的编码方式为utf-8
            response.setCharacterEncoding("utf-8");
            out = response.getOutputStream();
            out.write(buffer);
        } catch (Exception e) {
            throw new ServiceException(StrConstant.GET_IMAGE_FAIL);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                throw new ServiceException(StrConstant.GET_IMAGE_FAIL);
            }
        }
    }
}
