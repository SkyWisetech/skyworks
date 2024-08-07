package org.skywise.skyworks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.skywise.skyworks.model.DailyData;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 10:33
 * @Description: 每日数据汇总
 */
@Mapper
public interface DailyDataMapper extends BaseMapper<DailyData> {
}
