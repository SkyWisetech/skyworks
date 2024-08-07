package org.skywise.skyworks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.skywise.skyworks.model.Upvote;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/9 星期二 16:46
 * @Description:
 */
@Mapper
public interface UpvoteMapper extends BaseMapper<Upvote> {
    void insertBatch(List<Upvote> upvoteList);
}
