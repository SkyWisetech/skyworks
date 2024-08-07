package org.skywise.skyworks.common.VO;

import lombok.Data;
import org.skywise.skyworks.model.User;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/24 星期三 13:23
 * @Description:
 */
@Data
public class UserPageVO extends PageVO{
    List<User> userList;
}
