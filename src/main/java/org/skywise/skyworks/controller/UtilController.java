package org.skywise.skyworks.controller;

import org.skywise.skyworks.common.DTO.Response;
import org.skywise.skyworks.common.DTO.UserOptDTO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.enums.ReturnCodeEnum;
import org.skywise.skyworks.service.IUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LIGHT
 * @Date: 2024/7/10 星期三 13:47
 * @Description: 发送短信
 */
@RestController
@RequestMapping("/skyworks/util/")
public class UtilController {

    @Autowired
    private IUtilService utilService;

    @PostMapping("/email/sendVerifyCode")
    public Response<String> sendVerifyCode(@RequestBody UserOptDTO userOptDTO) {
        // 邮箱不能为空
        if(!StringUtils.hasLength(userOptDTO.getEmail())){
            return Response.fail(ReturnCodeEnum.EMAIL_ERROR.getMsg());
        }

        // 根据邮箱发送验证码
        utilService.sendVerifyCode(userOptDTO.getEmail());
        return Response.successMsg(StrConstant.SEND_AUTH_CODE_SUCCESS);
    }

    @PostMapping("/image/uploadImage")
    public Response<String> uploadImage(@RequestParam("image") MultipartFile image) {
        // 图片不能为空
        if (image == null) {
            return Response.fail(ReturnCodeEnum.IMAGE_ERROR.getMsg());
        }
        String imageName = utilService.uploadImage(image);
        return Response.success(imageName);
    }

    @GetMapping("/image/getImage")
    public Response<String> getImage(@RequestParam("imageName") String imageName, HttpServletResponse response) {
        // 图片名称不能为空
        if (!StringUtils.hasLength(imageName)) {
            return Response.fail(ReturnCodeEnum.IMAGE_NAME_ERROR.getMsg());
        }
        utilService.getImage(imageName, response);
        return Response.successMsg(StrConstant.UPLOAD_IMAGE_SUCCESS);
    }
}
