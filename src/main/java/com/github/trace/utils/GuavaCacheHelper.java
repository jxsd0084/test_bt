package com.github.trace.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jetbrick.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghl on 2016/4/28.
 */
@Component
public class GuavaCacheHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCacheHelper.class);
    private static Cache<String,Object> cache = CacheBuilder.newBuilder().concurrencyLevel(5).expireAfterWrite(3600, TimeUnit.SECONDS).maximumSize(100).build();

    public static void put(String key,Object value){
        cache.put(key,value);
    }

    public static Object getVersion(String key){
        try {
            return cache.get(key, new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                    return null;
                }
            });
        } catch (Exception e) {
            LOGGER.error("从guava缓存中读取数据出现异常： " + e);
            return null;
        }
    }

    public static List<Map<String,Object>> sort(List<Map<String,Object>> list){
        Map<String,Map<String,Object>> tempMap = new HashMap<>();
        List<String> strList = new ArrayList<>();
        for(Map<String,Object> map:list){
            String key = map.get("value").toString();
            if(StringUtils.isNotEmpty(key)) {
                strList.add(key);
                tempMap.put(key, map);
            }
        }
        Collections.sort(strList);
        list.clear();
        for(String key:strList){
            list.add(tempMap.get(key));
        }
        return list;
    }

    public static boolean isNum(String str) {

        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
