package org.skywise.skyworks.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @Author: LIGHT
 * @Date: 2024/7/10 星期三 16:05
 * @Description:
 */
public class LocalDateTimeUtil {


    public static Date LocalDateTimeToDate(LocalDateTime localDateTime) {
        // 将此日期时间与时区相结合以创建 ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        // 本地时间线LocalDateTime到即时时间线Instant时间戳
        Instant instant = zonedDateTime.toInstant();
        // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
        return Date.from(instant);
    }
}
