package com.github.trace.web;

import com.github.trace.intern.DateUtil;
import com.github.trace.intern.InnerUtil;
import com.github.trace.intern.TimePickerUtil;
import com.github.trace.service.InfluxOssService;
import com.github.trace.service.InfluxRpcService;
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
 * 服务监控
 * Created by lirui on 2015/09/09 下午8:55.
 */
@Controller
public class OssController {
  @Autowired
  private InfluxOssService influxOssService;
  @Autowired
  private InfluxRpcService influxRpcService;

  @RequestMapping("/method")
  public String callerList(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, Model model) {
    List<Map> items = influxOssService.recentMethod(start, end);
    model.addAttribute("data", InnerUtil.toJsonModule(items));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    return "method/method_list";
  }

  @RequestMapping("/service")
  public String serviceList(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, Model model) {
    List<Map> items = influxRpcService.recentModule(start, end);
    model.addAttribute("data", InnerUtil.toJson(items));
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "oss/service_list";
  }

  @RequestMapping("/caller")
  @ResponseBody
  public String caller(@RequestParam String name, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, @RequestParam(required = false) String caller, @RequestParam(required = false) String server) {
    String where = whereClause(caller, server);
    List<Map> callerItems = influxRpcService.recentRpcCaller(start, end, name, where);
    return "{\"data\":" + InnerUtil.toJson(callerItems) + '}';
  }

  @RequestMapping("/server")
  @ResponseBody
  public String server(@RequestParam String name, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, @RequestParam(required = false) String caller, @RequestParam(required = false) String server) {
    String where = whereClause(caller, server);
    List<Map> serverItems = influxRpcService.recentRpcServer(start, end, name, where);
    return "{\"data\":" + InnerUtil.toJson(serverItems) + '}';
  }

  @RequestMapping("/detail")
  @SuppressWarnings("unchecked")
  public String detail(@RequestParam String name, @RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, @RequestParam(required = false) String caller, @RequestParam(required = false) String server, Model model) {
    String where = whereClause(caller, server);
    List<Map> detailItems = influxRpcService.recentRpc(start, end, name, where);
    long now = System.currentTimeMillis();
    model.addAttribute("detailData", InnerUtil.toJson2(detailItems));
    if (detailItems.size() > 0) {
      model.addAttribute("highcharts", InnerUtil.toCharts(detailItems));
      model.addAttribute("highchartsTitle", name);
    }
    model.addAttribute("name", name);
    model.addAttribute("caller", caller);
    model.addAttribute("server", server);
    model.addAttribute("lastDay", DateUtil.formatYmd(now - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(now - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(null, start, end, last, name, caller, server, null));
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "oss/service_detail";
  }

  @RequestMapping("/compare")
  @SuppressWarnings("unchecked")
  public String compare(@RequestParam String name, @RequestParam(defaultValue = "最近1小时") String last, @RequestParam(required = false) String day, @RequestParam(required = false) String cmp, @RequestParam(required = false) String caller, @RequestParam(required = false) String server, Model model) {
    String where = whereClause(caller, server);

    String dayStart = TimePickerUtil.setDayStart(day);
    String dayEnd = TimePickerUtil.setDayEnd(day);
    String cmpStart = TimePickerUtil.setCmpStart(cmp);
    String cmpEnd = TimePickerUtil.setCmpEnd(cmp);
    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parseYmdHis(dayEnd, now);
    long cmpStamp = InnerUtil.parseYmdHis(cmpEnd, now);
    long offset = dayStamp - cmpStamp;
    cmp = TimePickerUtil.setCmp(cmp);

    List<Map> s1 = influxRpcService.recentRpc(dayStart, dayEnd, name, where);
    List<Map> s2 = influxRpcService.recentRpc(cmpStart, cmpEnd, name, where);
    for (Map m : s2) {
      Long ms = (Long) m.get("stamp");
      m.put("stamp", ms + offset);
    }
    model.addAttribute("nowCharts", InnerUtil.toCharts(s1));
    model.addAttribute("lastCharts", InnerUtil.toCharts(s2));
    model.addAttribute("name", name);
    model.addAttribute("caller", caller);
    model.addAttribute("server", server);
    model.addAttribute("now", DateUtil.formatYmd(now));
    model.addAttribute("cmp", cmp);
    model.addAttribute("day", InnerUtil.format(day));
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(day, null, null, last, name, caller, server, cmp));
    return "oss/service_compare";
  }

  private String urlPrefix(String day, String start, String end, String last, String name, String caller, String server, String cmp) {
    StringBuilder sbd = new StringBuilder(64);
    sbd.append("last=").append(last);
    sbd.append("&name=").append(UrlEscapers.urlFragmentEscaper().escape(name));
    if (!Strings.isNullOrEmpty(day)) {
      sbd.append("&day=").append(InnerUtil.format(day));
    }
    if (!Strings.isNullOrEmpty(start)) {
      sbd.append("&start=").append(start);
    }
    if (!Strings.isNullOrEmpty(end)) {
      sbd.append("&end=").append(end);
    }
    if (!Strings.isNullOrEmpty(caller)) {
      sbd.append("&caller=").append(UrlEscapers.urlFragmentEscaper().escape(caller));
    }
    if (!Strings.isNullOrEmpty(server)) {
      sbd.append("&server=").append(UrlEscapers.urlFragmentEscaper().escape(server));
    }
    if (!Strings.isNullOrEmpty(cmp)) {
      sbd.append("&cmp=").append(UrlEscapers.urlFragmentEscaper().escape(cmp));
    }
    return sbd.toString();
  }

  private String whereClause(String caller, String server) {
    CharMatcher m = CharMatcher.anyOf("'\";");
    StringBuilder sbd = new StringBuilder(64);
    if (!Strings.isNullOrEmpty(caller)) {
      sbd.append(" and caller=\'").append(m.removeFrom(caller)).append('\'');
    }
    if (!Strings.isNullOrEmpty(server)) {
      sbd.append(" and \"server\"=\'").append(m.removeFrom(server)).append('\'');
    }
    return sbd.toString();
  }
}
