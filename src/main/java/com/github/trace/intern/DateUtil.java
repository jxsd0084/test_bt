package com.github.trace.intern;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 之所以构造这么一个类，是因为有些时候只需要简单的功能，不希望引入joda-time的外部包。<br/>
 * 但是对于复杂的功能，还是推荐用joda-time的包，它的功能非常强大。
 *
 * @author colinli
 * @since 13-6-10 上午11:06
 */
public class DateUtil {
  private static final TimeZone chinaZone = TimeZone.getTimeZone("GMT+08:00");
  private static final Locale chinaLocale = Locale.CHINA;

  private DateUtil() {
  }

  /**
   * 构造时间的显示，带上日期信息，如 2013-06-11
   *
   * @param milliseconds 时间
   * @return 字符串表示
   */
  public static String formatYmd(long milliseconds) {
    return formatYmd(milliseconds, "-");
  }

  /**
   * 构造时间的显示，带上日期信息，如 2013-06-11
   *
   * @param milliseconds 时间
   * @return 字符串表示
   */
  public static String formatYmd(long milliseconds, String separator) {
    if (separator == null)
      separator = "";
    Calendar ca = Calendar.getInstance(chinaZone, chinaLocale);
    ca.setTimeInMillis(milliseconds);
    StringBuilder sbd = new StringBuilder();
    sbd.append(ca.get(Calendar.YEAR)).append(separator);
    int month = 1 + ca.get(Calendar.MONTH);
    if (month < 10) {
      sbd.append('0');
    }
    sbd.append(month).append(separator);
    int day = ca.get(Calendar.DAY_OF_MONTH);
    if (day < 10) {
      sbd.append('0');
    }
    sbd.append(day);
    return sbd.toString();
  }

  /**
   * 构造时间的显示，带上时分秒的信息，如 2013-06-11 03:14:25
   *
   * @param milliseconds 时间
   * @return 字符串表示
   */
  public static String formatYmdHis(long milliseconds) {
    Calendar ca = Calendar.getInstance(chinaZone, chinaLocale);
    ca.setTimeInMillis(milliseconds);
    StringBuilder sbd = new StringBuilder();
    sbd.append(ca.get(Calendar.YEAR)).append('-');
    int month = 1 + ca.get(Calendar.MONTH);
    if (month < 10) {
      sbd.append('0');
    }
    sbd.append(month).append('-');
    int day = ca.get(Calendar.DAY_OF_MONTH);
    if (day < 10) {
      sbd.append('0');
    }
    sbd.append(day).append(' ');
    int hour = ca.get(Calendar.HOUR_OF_DAY);
    if (hour < 10) {
      sbd.append('0');
    }
    sbd.append(hour).append(':');
    int minute = ca.get(Calendar.MINUTE);
    if (minute < 10) {
      sbd.append('0');
    }
    sbd.append(minute).append(':');
    int second = ca.get(Calendar.SECOND);
    if (second < 10) {
      sbd.append('0');
    }
    sbd.append(second);
    return sbd.toString();
  }

  /**
   * 构造时间的显示，带上时分秒和毫秒的信息，如 2013-06-11 03.14.25.678
   *
   * @param milliseconds 时间
   * @return 字符串表示
   */
  public static String formatYmdHisS(long milliseconds) {
    Calendar ca = Calendar.getInstance(chinaZone, chinaLocale);
    ca.setTimeInMillis(milliseconds);
    StringBuilder sbd = new StringBuilder();
    sbd.append(ca.get(Calendar.YEAR)).append('-');
    int month = 1 + ca.get(Calendar.MONTH);
    if (month < 10) {
      sbd.append('0');
    }
    sbd.append(month).append('-');
    int day = ca.get(Calendar.DAY_OF_MONTH);
    if (day < 10) {
      sbd.append('0');
    }
    sbd.append(day).append(' ');
    int hour = ca.get(Calendar.HOUR_OF_DAY);
    if (hour < 10) {
      sbd.append('0');
    }
    sbd.append(hour).append(':');
    int minute = ca.get(Calendar.MINUTE);
    if (minute < 10) {
      sbd.append('0');
    }
    sbd.append(minute).append(':');
    int second = ca.get(Calendar.SECOND);
    if (second < 10) {
      sbd.append('0');
    }
    sbd.append(second);
    int ms = ca.get(Calendar.MILLISECOND);
    if (ms > 0) {
      sbd.append('.').append(ms);
    }
    return sbd.toString();
  }

  /**
   * 格式化两个时间差，比如计算耗时信息
   *
   * @param diff_millis 时间差
   * @return 格式化字符串
   */
  public static String diffHisS(long diff_millis) {
    long ms = 0, sec = 0, min = 0, hour = 0, day = 0;
    ms = diff_millis % 1000;
    //seconds
    diff_millis = (diff_millis - ms) / 1000;
    if (diff_millis > 0) {
      sec = diff_millis % 60;
      //minutes
      diff_millis = (diff_millis - sec) / 60;
      if (diff_millis > 0) {
        min = diff_millis % 60;
        if (diff_millis > 0) {
          //hours
          diff_millis = (diff_millis - min) / 60;
          if (diff_millis > 0) {
            hour = diff_millis % 24;
            //day
            day = (diff_millis - hour) / 24;
          }
        }
      }
    }
    StringBuilder sbd = new StringBuilder();
    if (day > 0) {
      sbd.append(day).append("天 ");
    }
    if (hour < 10) {
      sbd.append('0');
    }
    sbd.append(hour).append(':');
    if (min < 10) {
      sbd.append('0');
    }
    sbd.append(min).append(':');
    if (sec < 10) {
      sbd.append('0');
    }
    sbd.append(sec);
    if (ms > 0) {
      sbd.append('.').append(ms);
    }

    return sbd.toString();
  }

  public static String diffHisS(long start_time, long end_time) {
    return diffHisS(end_time - start_time);
  }

  public static String diffHisS(Date start_date, Date end_date) {
    return diffHisS(end_date.getTime() - start_date.getTime());
  }

  /**
   * 把时间换算到固定的分钟，比如nMinutes=1的话，就只保留到分钟级，其他数值设置0。
   * 2013-08-21 13:32:23.89 换成 2013-08-21 13:32:00 <br/>
   * 如果不是nMinutes>1，则保留到当前的分割点。比如nMinutes=5，表示按5分钟划分，
   * 2013-08-21 13:32:23.89 换成 2013-08-21 13:30:00 <br/>
   *
   * @param millis   毫秒数
   * @param nMinutes 多少分钟打一个分割点
   * @return 当前分割点的时间
   */
  public static Date trimMinute(long millis, int nMinutes) {
    Calendar ca = Calendar.getInstance(chinaZone, chinaLocale);
    ca.setTimeInMillis(millis);
    ca.set(Calendar.SECOND, 0);
    ca.set(Calendar.MILLISECOND, 0);
    if (nMinutes != 1) {
      int minute = ca.get(Calendar.MINUTE);
      minute = (minute / nMinutes) * nMinutes;
      ca.set(Calendar.MINUTE, minute);
    }
    return ca.getTime();
  }


  /**
   * 获取一个时间前后多少分钟的时间，会抹去秒和毫秒的数值
   *
   * @param millis   时间戳
   * @param nMinutes 多少分钟
   * @return 修改后的时间
   */
  public static Date addMinutes(long millis, int nMinutes) {
    Calendar ca = Calendar.getInstance(chinaZone, chinaLocale);
    ca.setTimeInMillis(millis);
    ca.set(Calendar.SECOND, 0);
    ca.set(Calendar.MILLISECOND, 0);
    ca.add(Calendar.MINUTE, nMinutes);
    return ca.getTime();
  }

  public static String formatYmd(Date date) {
    return formatYmd(date.getTime());
  }

  public static String formatYmd(Date date, String separator) {
    return formatYmd(date.getTime(), separator);
  }

  public static String formatYmdHis(Date date) {
    return formatYmdHis(date.getTime());
  }

  public static String formatYmdHisS(Date date) {
    return formatYmdHisS(date.getTime());
  }

  public static Date trimMinute(Date date, int nMinutes) {
    return trimMinute(date.getTime(), nMinutes);
  }

  public static Date addMinutes(Date date, int nMinutes) {
    return addMinutes(date.getTime(), nMinutes);
  }

  public static Calendar getCalendar(long millis) {
    Calendar ca = Calendar.getInstance(chinaZone, chinaLocale);
    ca.setTimeInMillis(millis);
    return ca;
  }

  public static Calendar getCalendar() {
    return Calendar.getInstance(chinaZone, chinaLocale);
  }

  public static Calendar getCalendar(Date date) {
    Calendar ca = Calendar.getInstance(chinaZone, chinaLocale);
    ca.setTime(date);
    return ca;
  }

  public static Calendar getMidnight(long millis) {
    Calendar ca = getCalendar(millis);
    ca.set(Calendar.HOUR_OF_DAY, 0);
    ca.set(Calendar.MINUTE, 0);
    ca.set(Calendar.SECOND, 0);
    ca.set(Calendar.MILLISECOND, 0);
    return ca;
  }

  public static Calendar getMidnight(Date date) {
    return getMidnight(date.getTime());
  }
}

