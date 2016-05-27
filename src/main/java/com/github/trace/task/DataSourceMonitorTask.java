package com.github.trace.task;

import com.github.autoconf.helper.ConfigHelper;
import com.github.trace.service.Navigation0Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by wanghl on 2016/5/20.
 */
@Component
@Scope("prototype")
public class DataSourceMonitorTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceMonitorTask.class);

    private final static String NIGHTINTERVAL = "night.interval";

    private static final String MORNINGINTERVAL = "morning.interval";

    @Autowired
    private Navigation0Service navigation0Service;

    @Override
    public void run() {
        LOGGER.info("数据源监控begin");
        Date date = new Date();
        int hour = date.getHours();
        if(7<hour&&hour<20){
            navigation0Service.checkDataSource(MORNINGINTERVAL);
        }else{
            navigation0Service.checkDataSource(NIGHTINTERVAL);
        }
        LOGGER.info("数据源监控end");
    }
}
