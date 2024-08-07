package org.skywise.skyworks.service;

import org.skywise.skyworks.common.DTO.UserDTO;
import org.skywise.skyworks.common.DTO.UserOptDTO;
import org.skywise.skyworks.common.VO.UserVO;
import org.skywise.skyworks.model.User;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 11:38
 * @Description:
 */
public interface IUserService {
    Long getUserCount();

    UserVO getUserById(Integer id);

    /**
     * 查询用户：可根据昵称，邮箱，注册日期查询
     * */
    List<User> listUsers(UserOptDTO userOptDTO);

    /**
     * 用户注册
     * */
    void insertUser(UserDTO userDTO);

    /**
     * 用户信息修改，或者用户进行禁言、拉黑操作
     * */
    void updateUser(UserOptDTO userOptDTO);
}
