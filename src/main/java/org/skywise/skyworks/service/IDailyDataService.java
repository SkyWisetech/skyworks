package org.skywise.skyworks.service;

import org.skywise.skyworks.model.DailyData;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 10:32
 * @Description:
 */
public interface IDailyDataService {
    List<DailyData> listDailyData(LocalDate date, Integer numberOfDays);

    void insertDailyData(DailyData dailyData);
}
