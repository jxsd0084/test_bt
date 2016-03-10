package com.github.trace.intern;

import com.google.common.base.Strings;

/**
 * 设定时间戳
 * Created by hanmz on 2016/1/21.
 */
public class TimePickerUtil {
  public static String setLast(String last, String start, String end) {
    if ("other".equals(last)) {
      return start + " to " + end;
    }
    return last;
  }

  public static String setDayStart(String day) {
    if (Strings.isNullOrEmpty(day)) {
      return DateUtil.formatYmd(System.currentTimeMillis()) + " 00:00:00";
    }
    return day + " 00:00:00";
  }

  public static String setDayEnd(String day) {
    if (Strings.isNullOrEmpty(day)) {
      return DateUtil.formatYmd(System.currentTimeMillis()) + " 23:59:59";
    }
    return day + " 23:59:59";
  }

  public static String setCmpStart(String day) {
    if (Strings.isNullOrEmpty(day)) {
      return DateUtil.formatYmd(System.currentTimeMillis() - InnerUtil.ONE_DAY) + " 00:00:00";
    }
    return day + " 00:00:00";
  }

  public static String setCmpEnd(String day) {
    if (Strings.isNullOrEmpty(day)) {
      return DateUtil.formatYmd(System.currentTimeMillis() - InnerUtil.ONE_DAY) + " 23:59:59";
    }
    return day + " 23:59:59";
  }

  public static String setCmp(String cmp) {
    if (Strings.isNullOrEmpty(cmp)) {
      return DateUtil.formatYmd(System.currentTimeMillis() - InnerUtil.ONE_DAY);
    }
    return cmp;
  }
}
