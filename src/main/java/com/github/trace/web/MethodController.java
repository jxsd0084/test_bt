package com.github.trace.web;

import com.github.trace.intern.DateUtil;
import com.github.trace.intern.InnerUtil;
import com.github.trace.intern.TimePickerUtil;
import com.github.trace.service.InfluxOssService;
import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.net.UrlEscapers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 服务接口调用监控
 * Created by lirui on 2015/09/09 下午8:55.
 */
@Controller
@RequestMapping("/if/")
public class MethodController {
  @Autowired
  private InfluxOssService influxOssService;

  @RequestMapping("list")
  public String serviceList(@RequestParam String name,
                            @RequestParam(defaultValue = "最近1小时") String last,
                            @RequestParam(defaultValue = "now() - 1h") String start,
                            @RequestParam(defaultValue = "now()") String end,
                            @RequestParam(required = false) String day,
                            @RequestParam(required = false) String caller,
                            @RequestParam(required = false) String method,
                            Model model) {
    String where = whereClause(caller, method);
    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parse(day, now);
    List<Map> recent = influxOssService.recentMethod(start, end, name, where);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("data", InnerUtil.toJson(recent));
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("name", name);
    model.addAttribute("caller", caller);
    model.addAttribute("method", method);
    model.addAttribute("now", DateUtil.formatYmd(now));
    model.addAttribute("day", InnerUtil.format(day));
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(day, last, start, end, name, caller, null, null));
    return "iface/iface_list";
  }

  @RequestMapping("/api")
  @ResponseBody
  public String api(@RequestParam String name,
                    @RequestParam(defaultValue = "now() - 1h") String start,
                    @RequestParam(defaultValue = "now()") String end,
                    @RequestParam(required = false) String caller,
                    @RequestParam(required = false) String method) {
    String where = whereClause(caller, method);
    List<Map> items = influxOssService.recentMethod(start, end, name, where);
    return "{\"data\":" + InnerUtil.toJson(items) + '}';
  }

  @RequestMapping("caller")
  @ResponseBody
  public String caller(@RequestParam String name,
                       @RequestParam String method,
                       @RequestParam(defaultValue = "now() - 1h") String start,
                       @RequestParam(defaultValue = "now()") String end,
                       @RequestParam(required = false) String caller) {
    String where = whereClause(caller, method);
    List<Map> callerItems = influxOssService.recentServiceCaller(start, end, name, where);
    return "{\"data\":" + InnerUtil.toJson(callerItems) + '}';
  }

  @RequestMapping("detail")
  @SuppressWarnings("unchecked")
  public String detail(@RequestParam String name,
                       @RequestParam String method,
                       @RequestParam(defaultValue = "最近1小时") String last,
                       @RequestParam(defaultValue = "now() - 1h") String start,
                       @RequestParam(defaultValue = "now()") String end,
                       @RequestParam(required = false) String day,
                       @RequestParam(required = false) String caller,
                       Model model) {
    String where = whereClause(caller, method);
    List<Map> detailItems = influxOssService.recentService(start, end, name, where);
    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parse(day, now);
    model.addAttribute("detailData", InnerUtil.toJson2(detailItems));
    if (detailItems.size() > 0) {
      model.addAttribute("highcharts", InnerUtil.toCharts(detailItems));
      model.addAttribute("highchartsTitle", method);
    }
    model.addAttribute("name", name);
    model.addAttribute("method", method);
    model.addAttribute("caller", caller);
    model.addAttribute("now", DateUtil.formatYmd(now));
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(day, last, start, end, name, caller, method, null));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    return "iface/iface_detail";
  }

  @RequestMapping("compare")
  @SuppressWarnings("unchecked")
  public String compare(@RequestParam String name,
                        @RequestParam String method,
                        @RequestParam String day,
                        @RequestParam String cmp,
                        @RequestParam(required = false) String last,
                        @RequestParam(required = false) String caller,
                        Model model) {
    String where = whereClause(caller, method);

    String dayStart = TimePickerUtil.setDayStart(day);
    String dayEnd = TimePickerUtil.setDayEnd(day);
    String cmpStart = TimePickerUtil.setDayStart(cmp);
    String cmpEnd = TimePickerUtil.setDayEnd(cmp);

    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parseYmdHis(dayEnd, now);
    long cmpStamp = InnerUtil.parseYmdHis(cmpEnd, now);
    long offset = dayStamp - cmpStamp;
    cmp = TimePickerUtil.setCmp(cmp);

    List<Map> s1 = influxOssService.recentService(dayStart, dayEnd, name, where);
    List<Map> s2 = influxOssService.recentService(cmpStart, cmpEnd, name, where);
    for (Map m : s2) {
      Long ms = (Long) m.get("stamp");
      m.put("stamp", ms + offset);
    }
    model.addAttribute("nowCharts", InnerUtil.toCharts(s1));
    model.addAttribute("lastCharts", InnerUtil.toCharts(s2));
    model.addAttribute("name", name);
    model.addAttribute("caller", caller);
    model.addAttribute("method", method);
    model.addAttribute("now", DateUtil.formatYmd(now));
    model.addAttribute("cmp", cmp);
    model.addAttribute("day", InnerUtil.format(day));
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(day, last, null, null, name, caller, method, cmp));
    return "iface/iface_compare";
  }

  private String urlPrefix(String day,
                           String last,
                           String start,
                           String end,
                           String name,
                           String caller,
                           String method,
                           String cmp) {
    StringBuilder sbd = new StringBuilder(64);
    sbd.append("last=").append(last);
    sbd.append("&name=").append(UrlEscapers.urlFragmentEscaper().escape(name));
    if (!Strings.isNullOrEmpty(start)) {
      sbd.append("&start=").append(start);
    }
    if (!Strings.isNullOrEmpty(end)) {
      sbd.append("&end=").append(InnerUtil.format(end));
    }
    if (!Strings.isNullOrEmpty(day)) {
      sbd.append("&day=").append(InnerUtil.format(day));
    }
    if (!Strings.isNullOrEmpty(caller)) {
      sbd.append("&caller=").append(UrlEscapers.urlFragmentEscaper().escape(caller));
    }
    if (!Strings.isNullOrEmpty(method)) {
      sbd.append("&method=").append(UrlEscapers.urlFragmentEscaper().escape(method));
    }
    if (!Strings.isNullOrEmpty(cmp)) {
      sbd.append("&cmp=").append(UrlEscapers.urlFragmentEscaper().escape(cmp));
    }
    return sbd.toString();
  }

  private String whereClause(String caller, String method) {
    CharMatcher m = CharMatcher.anyOf("'\";");
    StringBuilder sbd = new StringBuilder(64);
    if (!Strings.isNullOrEmpty(caller)) {
      sbd.append(" and caller=\'").append(m.removeFrom(caller)).append('\'');
    }
    if (!Strings.isNullOrEmpty(method)) {
      sbd.append(" and method=\'").append(m.removeFrom(method)).append('\'');
    }
    return sbd.toString();
  }
}
