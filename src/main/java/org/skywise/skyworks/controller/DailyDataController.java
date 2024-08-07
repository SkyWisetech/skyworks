package org.skywise.skyworks.controller;

import org.skywise.skyworks.common.DTO.Response;
import org.skywise.skyworks.model.DailyData;
import org.skywise.skyworks.service.IDailyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author: LIGHT
 * @Date: 2024/7/8 星期一 10:23
 * @Description: 每日数据统计汇总
 */
@RestController
@RequestMapping("/skyworks/dailyData/")
public class DailyDataController {
    @Autowired
    private IDailyDataService dailyDataService;

    @PostMapping("/listDailyData")
    public Response<List<DailyData>> listDailyData() {
        List<DailyData> dailyDataList = dailyDataService.listDailyData(LocalDate.now(), 7);
        return Response.success(dailyDataList);
    }
}
