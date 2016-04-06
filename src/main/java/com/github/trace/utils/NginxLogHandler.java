package com.github.trace.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * nginx日志处理
 * Created by wzk on 16/4/6.
 */
public class NginxLogHandler {

  private static final Logger LOG = LoggerFactory.getLogger(NginxLogHandler.class);
  private static final String NGINX_LOG_SPLITTER = "\u0001";
  private static final String DATEFORMAT = "dd/MMM/yyyy:HH:mm:ss Z";
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormat.forPattern(DATEFORMAT).withLocale(Locale.US);

  private NginxLogHandler() {
  }

  public static Set<String> batchParse(Set<String> logs) {
    Set<String> results = Sets.newHashSet();
    logs.forEach(log -> results.add(parseNginx(log)));
    return results;
  }

  public static String parseNginx(String log) {
    if (StringUtils.isEmpty(log)) {
      return StringUtils.EMPTY;
    }

    DateTime jodaTime = DateTime.now();
    try {
      List<String> paramList = Splitter.on(NGINX_LOG_SPLITTER).omitEmptyStrings().splitToList(log);
      if (!paramList.isEmpty()) {
        String nginxTime = paramList.get(2);
        if (StringUtils.contains(nginxTime, " +0800")) {
          jodaTime = DateTime.parse(nginxTime, DATE_TIME_FORMATTER);
        }
      }
    } catch (Exception e) {
      LOG.error("Cannot parse date from \" {} \", use current date instead", log, e);
    }

    long stamp = jodaTime.getMillis();
    String params = StringUtils.substringBetween(log, ".gif?", " HTTP/1");
    if (StringUtils.isNotEmpty(params)) {
      return parseToJson(params, stamp);
    }
    return StringUtils.EMPTY;
  }

  private static String parseToJson(String params, long stamp) {
    Map<String, String> map = Maps.newHashMap();
    map.put("stamp", String.valueOf(stamp));
    List<String> paramList = Splitter.on("&").omitEmptyStrings().splitToList(params);
    paramList.forEach(param -> {
      List<String> kv = Splitter.on("=").omitEmptyStrings().splitToList(param);
      if (kv.size() == 2) {
        map.put(kv.get(0), kv.get(1));
      }
    });
    return JSON.toJSONString(map);
  }

}
