package com.chengshare.gateway;

import lombok.Data;

import java.time.LocalTime;

/**
 * LocalTime时间格式
 * org.springframework.format.datetime.standard.DateTimeFormatterRegistrar#getFallbackFormatter(org.springframework.format.datetime.standard.DateTimeFormatterRegistrar.Type)
 * DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
 * @Author fate7
 * @Date 2020/4/16 10:13 上午
 **/
@Data
public class TimeBetweenConfig {

    private LocalTime start;

    private LocalTime end;
}
