package com.github.trace.task;

import com.github.trace.service.ElasticsearchService;
import com.github.trace.utils.GuavaCacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by wanghl on 2016/5/13.
 */
@Component
@Scope("prototype")
public class VersionSyncTask implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionSyncTask.class);
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long beginTime = now - 3*(24 * 3600 * 1000L);
        LOGGER.info("安卓、苹果应用版本号同步任务开始");
        queryAndroidVersion(now,beginTime);
        queryIOSVersion(now,beginTime);
        LOGGER.info("安卓、苹果应用版本号同步任务结束");
    }

    private void queryAndroidVersion(long now,long yesterday){
        Map<String,Object> map = new HashMap<>();
        List<Map<String, Object>> appVersionList = null;
        List<Map<String, Object>> innerVersionList = null;

        appVersionList = elasticsearchService.searchBySqlForMonitorRequest("Android", null, null, "M6", yesterday, now);
        innerVersionList = elasticsearchService.searchBySqlForMonitorRequest("Android", null, null, "M7", yesterday, now);
        map.put("appVersionList",GuavaCacheHelper.sort(appVersionList));
        map.put("innerVersionList",GuavaCacheHelper.sort(innerVersionList));
        GuavaCacheHelper.put("AndroidVersion",map);
    }

    private void queryIOSVersion(long now,long yesterday){
        Map<String,Object> map = new HashMap<>();
        List<Map<String, Object>> appVersionList = null;
        List<Map<String, Object>> innerVersionList = null;
        appVersionList = elasticsearchService.searchBySqlForMonitorRequest("iPhone OS", null, null, "M6", yesterday, now);
        innerVersionList = elasticsearchService.searchBySqlForMonitorRequest("iPhone OS", null, null, "M7", yesterday, now);
        map.put("appVersionList",GuavaCacheHelper.sort(appVersionList));
        map.put("innerVersionList",GuavaCacheHelper.sort(innerVersionList));
        GuavaCacheHelper.put("IosVersion",map);
    }


}
