package org.skywise.skyworks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.skywise.skyworks.model.User;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 11:49
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
