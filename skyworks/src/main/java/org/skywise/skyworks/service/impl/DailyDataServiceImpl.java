package org.skywise.skyworks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.exception.ServiceException;
import org.skywise.skyworks.mapper.DailyDataMapper;
import org.skywise.skyworks.model.DailyData;
import org.skywise.skyworks.service.IDailyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 10:33
 * @Description:
 */
@Service
public class DailyDataServiceImpl implements IDailyDataService {
    @Autowired
    private DailyDataMapper dailyDataMapper;

    @Override
    public List<DailyData> listDailyData(LocalDate date, Integer numberOfDays) {
        QueryWrapper<DailyData> queryWrapper = new QueryWrapper<>();
        LocalDate fromDate = date.minusDays(numberOfDays.longValue());
        queryWrapper.gt(DailyData.DAILY_DATE_COLUMN, fromDate);
        return dailyDataMapper.selectList(queryWrapper);
    }

    @Override
    public void insertDailyData(DailyData dailyData) {
        int result = dailyDataMapper.insert(dailyData);
        if (result != 1) {
            throw new ServiceException(StrConstant.INSERT_DAILY_DATA_FAIL);
        }
    }
}
