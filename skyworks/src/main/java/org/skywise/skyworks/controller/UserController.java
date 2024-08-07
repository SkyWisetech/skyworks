package org.skywise.skyworks.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.skywise.skyworks.common.DTO.AdminDTO;
import org.skywise.skyworks.common.DTO.Response;
import org.skywise.skyworks.common.DTO.UserDTO;
import org.skywise.skyworks.common.DTO.UserOptDTO;
import org.skywise.skyworks.common.VO.UserPageVO;
import org.skywise.skyworks.common.VO.UserVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.enums.BlackEnum;
import org.skywise.skyworks.common.enums.ResMsgEnum;
import org.skywise.skyworks.common.enums.ReturnCodeEnum;
import org.skywise.skyworks.common.utils.JwtUtil;
import org.skywise.skyworks.common.utils.TokenUtil;
import org.skywise.skyworks.model.User;
import org.skywise.skyworks.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 9:14
 * @Description: 用户管理
 */
@Slf4j
@RestController
@RequestMapping("/skyworks/user/")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private Cache<String, String> verifyCodeCache;

    @Value("${token.secret}")
    private String secret;

    @Value("${admin.userName}")
    private String userName;

    @Value("${admin.password}")
    private String password;

    @PostMapping("/register")
    public Response<String> register(@Valid @RequestBody UserDTO userDTO) {
        //校验验证码是否正确
        String verifyCode = verifyCodeCache.getIfPresent(userDTO.getEmail());
        if (!userDTO.getVerifyCode().equals(verifyCode)) {
            return Response.fail(ReturnCodeEnum.VERIFY_CODE_ERROR.getCode(), ReturnCodeEnum.VERIFY_CODE_ERROR.getMsg());
        }

        // 校验密码和重复密码是否相同
        if (!userDTO.getPassword().equals(userDTO.getVerifyPassword())) {
            return Response.fail(ReturnCodeEnum.VERIFY_PASSWORD_ERROR.getCode(), ReturnCodeEnum.VERIFY_PASSWORD_ERROR.getMsg());
        }

        // 如果已经注册，则不能注册
        UserOptDTO userOptDTO = new UserOptDTO();
        userOptDTO.setEmail(userDTO.getEmail());
        List<User> userList = userService.listUsers(userOptDTO);
        if (!userList.isEmpty()) {
            return Response.fail(ReturnCodeEnum.REGISTER_ERROR.getMsg());
        }

        userService.insertUser(userDTO);
        return Response.successMsg(StrConstant.REGISTER_SUCCESS);
    }

    @PostMapping("/updatePassword")
    public Response<String> updatePassword(@Valid @RequestBody UserOptDTO userOptDTO) {
        // 看是否有注册
        UserOptDTO userInfo = new UserOptDTO();
        userInfo.setEmail(userOptDTO.getEmail());
        List<User> userList = userService.listUsers(userInfo);
        if (userList.isEmpty()) {
            return Response.fail(StrConstant.NOT_REGISTER_ERROR);
        }

        //校验验证码是否正确
        String verifyCode = verifyCodeCache.getIfPresent(userOptDTO.getEmail());
        if (!userOptDTO.getVerifyCode().equals(verifyCode)) {
            return Response.fail(ReturnCodeEnum.VERIFY_CODE_ERROR.getMsg());
        }

        // 校验密码和重复密码是否相同
        if (!userOptDTO.getPassword().equals(userOptDTO.getVerifyPassword())) {
            return Response.fail(ReturnCodeEnum.VERIFY_PASSWORD_ERROR.getMsg());
        }

        userService.updateUser(userOptDTO);
        return Response.successMsg(StrConstant.UPDATE_PASSWORD_SUCCESS);
    }

    @PostMapping("/loginByVerifyCode")
    public Response<String> loginByVerifyCode(@RequestBody UserOptDTO userOptDTO) {
        // 检查是否已经注册过
        List<User> userList = userService.listUsers(userOptDTO);
        if (userList.isEmpty()) {
            return Response.fail(ReturnCodeEnum.LOGIN_ERROR.getMsg());
        }

        // 验证码是否正确
        String verifyCode = verifyCodeCache.getIfPresent(userOptDTO.getEmail());
        if (!userOptDTO.getVerifyCode().equals(verifyCode)) {
            return Response.fail(ReturnCodeEnum.VERIFY_CODE_ERROR.getMsg());
        }

        // 检查是否进入了黑名单
        if (BlackEnum.BLACK.getCode().equals(userList.get(0).getIsBlack())) {
            return Response.fail(ReturnCodeEnum.BLACK_ERROR.getMsg());
        }

        // 验证成功后，生成token返回
        String token = JwtUtil.generateToken(userList.get(0).getId(), userList.get(0).getEmail(), secret);
        log.info("token: {}", token);
        log.info("user: {}", userList.get(0));
        return Response.success(token);
    }

    @PostMapping("/loginByPassword")
    public Response<String> loginByPassword(@RequestBody UserOptDTO userOptDTO) {
        List<User> userList = userService.listUsers(userOptDTO);
        if (!userList.isEmpty()) {
            // 检查是否进入了黑名单
            if (BlackEnum.BLACK.getCode().equals(userList.get(0).getIsBlack())) {
                return Response.fail(ReturnCodeEnum.BLACK_ERROR.getMsg());
            }
            // 生成token
            String token = JwtUtil.generateToken(userList.get(0).getId(), userList.get(0).getEmail(), secret);
//            log.info("token: {}", token);
//            log.info("user: {}", userList.get(0));
            return Response.success(token);
        }
        return Response.fail(StrConstant.PASSWORD_EMAIL_ERROR);
    }

    @PostMapping("/listUser")
    public Response<UserPageVO> listUser(@RequestBody UserOptDTO userOptDTO) {
        // 查询用户信息
        List<User> userList = userService.listUsers(userOptDTO);

        // 输出分页信息
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        UserPageVO userPageVO = new UserPageVO();
        userPageVO.setUserList(userList);
        userPageVO.setPageNum(pageInfo.getPageNum());
        userPageVO.setPageSize(pageInfo.getPageSize());
        userPageVO.setTotalCount(pageInfo.getTotal());
        userPageVO.setTotalPage(pageInfo.getPages());
        return Response.success(userPageVO);
    }

    @PostMapping("/getUser")
    public Response<UserVO> getUser() {
        Integer userId = TokenUtil.getUserId();
        UserVO userVO = userService.getUserById(userId);
        return Response.success(userVO);
    }


    /**
     * 更新用户信息：
     * 1、修改用户昵称
     * 2、禁言
     * 3、拉黑
     */
    @PostMapping("/updateUserInfo")
    public Response<String> updateUserInfo(@RequestBody UserOptDTO userOptDTO) {
        // 后台对用户进行操作则不会为空
        if (userOptDTO.getId() == null) {
            Integer userId = TokenUtil.getUserId();
            userOptDTO.setId(userId);
        }
        userService.updateUser(userOptDTO);
        String resMsg = ResMsgEnum.getMsg(userOptDTO.getUpdateType());
        if (userOptDTO.getUpdateType().equals(ResMsgEnum.UPDATE_WHITE_BLACK.getCode())) {
            resMsg = userOptDTO.getIsBlack() == 0 ? StrConstant.WHITE_SUCCESS : StrConstant.BLACK_SUCCESS;
        }
        return Response.successMsg(resMsg);
    }

    @PostMapping("/adminLogin")
    public Response<String> loginByPassword(@RequestBody AdminDTO adminDTO) {
        if (adminDTO.getUserName().equals(userName) && adminDTO.getPassword().equals(password)) {
            String token = JwtUtil.generateToken(0, userName, secret);
            return Response.success(token);
        }
        return Response.fail(StrConstant.PASSWORD_EMAIL_ERROR);
    }
}
