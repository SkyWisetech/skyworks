package org.skywise.skyworks.common.VO;

import lombok.Data;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/23 星期二 15:30
 * @Description:
 */
@Data
public class CommentPageVO extends PageVO{
    List<CommentVO> commentList;
}
