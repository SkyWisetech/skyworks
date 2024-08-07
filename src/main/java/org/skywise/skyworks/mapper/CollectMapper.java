package org.skywise.skyworks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.skywise.skyworks.model.Collect;

import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/12 星期五 14:07
 * @Description:
 */
@Mapper
public interface CollectMapper extends BaseMapper<Collect> {
    void insertBatch(List<Collect> list);
}
