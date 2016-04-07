package com.github.trace.utils;

import com.google.common.collect.Sets;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * json格式日志处理
 * Created by wzk on 16/4/6.
 */
public class JsonLogHandler {
  private static final Logger LOG = LoggerFactory.getLogger(JsonLogHandler.class);
  private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATEFORMAT);

  private JsonLogHandler() {
  }

  public static Set<String> batchConvert(Set<String> logs) {
    Set<String> results = Sets.newHashSet();
    logs.forEach(log -> results.add(convert(log)));
    return results;
  }

  public static String convert(String log) {
    JSONObject json = JSONObject.parseObject(log);
    JSONObject object = new JSONObject();
    json.entrySet().forEach(entry -> {
      String keyConverted = convertFieldName(entry.getKey());
      String value = entry.getValue().toString();

      if (StringUtils.equals(keyConverted, "stamp")) {
        DateTime jodaTime = DateTime.now();
        try {
          jodaTime = DateTime.parse(value, DATE_TIME_FORMATTER);
        } catch (Exception e) {
          LOG.error("Cannot parse date {}, use current date instead", value, e);
        }
        long stamp = jodaTime.getMillis();
        object.put(keyConverted, String.valueOf(stamp));
      } else {
        object.put(keyConverted, value);
      }
    });
    return object.toJSONString();
  }

  private static String convertFieldName(String key) {
    if (StringUtils.equals(key, "_time")) {
      return "stamp";
    }
    if (StringUtils.contains(key, '.')) {
      return StringUtils.replaceChars(key, '.', '_');
    }
    return key;
  }

}
