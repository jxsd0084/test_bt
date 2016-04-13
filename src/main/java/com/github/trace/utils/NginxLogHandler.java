package com.github.trace.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.alibaba.fastjson.JSON;
import com.github.autoconf.ConfigFactory;

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
  private static Map<String, String> keyMap = Maps.newLinkedHashMap();
  static {
    ConfigFactory.getInstance().getConfig("buriedtool-es-keymap").addListener(config -> {
      keyMap = config.getAll();
    });
  }

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
        String key = convertKey(kv.get(0));
        String value = kv.get(1);
        map.put(key, value);
      }
    });
    return JSON.toJSONString(map);
  }

  private static String convertKey(String originKey) {
    for (Map.Entry<String, String> entry : keyMap.entrySet()) {
      String oKey = entry.getKey();
      String nKey = entry.getValue();
      if (StringUtils.equals(oKey, originKey)) {
        return nKey;
      }
    }
    return originKey;
  }

}
