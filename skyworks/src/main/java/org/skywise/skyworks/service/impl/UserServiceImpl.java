package org.skywise.skyworks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.skywise.skyworks.common.DTO.CommentOptDTO;
import org.skywise.skyworks.common.DTO.UserDTO;
import org.skywise.skyworks.common.DTO.UserOptDTO;
import org.skywise.skyworks.common.VO.UserVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.enums.DeleteEnum;
import org.skywise.skyworks.common.enums.ParentEnum;
import org.skywise.skyworks.common.exception.ServiceException;
import org.skywise.skyworks.mapper.UserMapper;
import org.skywise.skyworks.model.Comment;
import org.skywise.skyworks.model.User;
import org.skywise.skyworks.service.ICommentService;
import org.skywise.skyworks.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 11:38
 * @Description:
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private Cache<Integer, Set<Integer>> userIdToCollectCache;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ICommentService commentService;

    @Override
    public Long getUserCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Comment.IS_DELETE, DeleteEnum.NOT_DELETE.getCode());
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public UserVO getUserById(Integer userId) {
        User user = userMapper.selectById(userId);
        UserVO userVO = new UserVO();
        try {
            BeanUtils.copyProperties(userVO, user);
            // 收藏数
            Set<Integer> collectSet = userIdToCollectCache.get(userId, k -> new HashSet<>());
            userVO.setCollectNum(collectSet.size());

            // 评论数
            CommentOptDTO commentOptDTO = new CommentOptDTO();
            commentOptDTO.setParentId(ParentEnum.IS_PARENT.getCode());
            commentOptDTO.setUserId(userId);
            Long commentNum = commentService.getCommentCount(commentOptDTO);
            userVO.setCommentNum(commentNum.intValue());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        return userVO;
    }

    @Override
    public List<User> listUsers(UserOptDTO userOptDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        // 邮箱、昵称查询
        queryWrapper.eq(StringUtils.hasLength(userOptDTO.getEmail()), User::getEmail, userOptDTO.getEmail())
                .eq(StringUtils.hasLength(userOptDTO.getPassword()), User::getPassword, userOptDTO.getPassword())
                .like(StringUtils.hasLength(userOptDTO.getNickName()), User::getNickName, userOptDTO.getNickName());

        // 日期查询
        if (userOptDTO.getStartDate() != null && userOptDTO.getEndDate() != null) {
            LocalDate endDate = userOptDTO.getEndDate().plusDays(1L);
            queryWrapper.gt(User::getCreateTime, userOptDTO.getStartDate())
                    .lt(User::getCreateTime, endDate);
        }
        PageHelper.startPage(userOptDTO.getPageNum(), userOptDTO.getPageSize());
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public void insertUser(UserDTO userDTO) {
        // 如果存在，则不能注册
        UserOptDTO userOptDTO = new UserOptDTO();
        userOptDTO.setEmail(userDTO.getEmail());
        List<User> userList = this.listUsers(userOptDTO);
        if (!userList.isEmpty()) {
            throw new ServiceException(StrConstant.REGISTER_FAIL);
        }

        User user = new User();
        try {
            BeanUtils.copyProperties(user, userDTO);
        } catch (Exception e) {
            throw new ServiceException(StrConstant.COPY_PROPERTY_FAIL);
        }
        int result = userMapper.insert(user);
        if (result != 1) {
            throw new ServiceException(StrConstant.REGISTER_FAIL_UNKNOWN);
        }
    }

    @Override
    public void updateUser(UserOptDTO userOptDTO) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        // 查询条件
        updateWrapper.eq(userOptDTO.getId() != null, User::getId, userOptDTO.getId());
        updateWrapper.eq(userOptDTO.getEmail() != null, User::getEmail, userOptDTO.getEmail());

        // 更新时间
        updateWrapper.set(User::getUpdateTime, LocalDateTime.now());
        // 个人简介
        updateWrapper.set(StringUtils.hasLength(userOptDTO.getRemark()), User::getRemark, userOptDTO.getRemark());
        // 用户昵称
        updateWrapper.set(StringUtils.hasLength(userOptDTO.getNickName()), User::getNickName, userOptDTO.getNickName());
        // 是否拉黑
        updateWrapper.set(userOptDTO.getIsBlack() != null, User::getIsBlack, userOptDTO.getIsBlack());
        // 是否禁言
        if (userOptDTO.getForbidCommentDay() != null) {
            LocalDate endDate = LocalDate.now().plusDays(userOptDTO.getForbidCommentDay());
            updateWrapper.set(User::getForbidCommentDate, endDate);
        }
        // 重置密码
        updateWrapper.set(StringUtils.hasLength(userOptDTO.getPassword()), User::getPassword, userOptDTO.getPassword());
        int result = userMapper.update(null, updateWrapper);
        if (result != 1) {
            throw new ServiceException(StrConstant.UPDATE_USER_FAIL);
        }
    }

}
