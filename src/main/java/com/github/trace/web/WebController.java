package com.github.trace.web;

import com.github.trace.intern.DateUtil;
import com.github.trace.intern.InnerUtil;
import com.github.trace.service.InfluxUriService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.net.UrlEscapers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * WEB监控逻辑
 * Created by wangyuebin on 15/11/10.
 */
@Controller
public class WebController {

  @Autowired
  private InfluxUriService influxUriService;

  @RequestMapping("/web")
  public String webList(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, Model model) {

      model.addAttribute("start", start);
      model.addAttribute("end", end);
      model.addAttribute("last", last);
      List<Map> webList = ImmutableList.of();
      List<Map> browserList = ImmutableList.of();
      try {
        webList = influxUriService.recentWeb(start, end);
        browserList = influxUriService.recentBrowser(start, end);
      } catch (Exception e) {
        model.addAttribute("message", "查询InfluxDB找不到信息");
      }
      model.addAttribute("data", InnerUtil.toJson7(webList));
      model.addAttribute("browser", InnerUtil.toJson71(browserList));
      return "web/web_list";
  }

  @RequestMapping("/actionid_list")
  public String actionIdList(@RequestParam(defaultValue = "") String browser, @RequestParam(defaultValue = "1h") String last, @RequestParam(required = true) String actionid, @RequestParam(required = false) String day, Model model) {

    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parse(day, now);
    model.addAttribute("last", last);
    model.addAttribute("actionid", actionid);
    model.addAttribute("times", ImmutableList.of("1h", "6h", "12h", "1d", "3d", "7d"));
    List<Map> actionIdList = ImmutableList.of();
    List<String> browserList = ImmutableList.of();
    try {
        browserList = influxUriService.getTags("web", "browser", "oss_web");
      if (browser.equals("") && browserList.size() > 0) {
        browser = browserList.get(0);
      }
      if (actionid.equals("domload")) {
        actionIdList = influxUriService.recentActionIdByDomload(last, day, actionid, browser);
        model.addAttribute("data", InnerUtil.toJson9(actionIdList));
      } else {
        actionIdList = influxUriService.recentActionId(last, day, actionid, browser);
        model.addAttribute("data", InnerUtil.toJson8(actionIdList));
      }
    } catch (Exception e) {
      model.addAttribute("message", "查询InfluxDB找不到信息");
    }
    if (!actionIdList.isEmpty()) {
      if (actionid.equals("domload")) {
        model.addAttribute("highcharts", InnerUtil.toDomloadCharts(actionIdList));
      } else {
        model.addAttribute("highcharts", InnerUtil.toActionIdCharts(actionIdList));
      }
      model.addAttribute("highchartsTitle", actionid);
    }
    model.addAttribute("browserList", browserList);
    model.addAttribute("browser", browser);
    model.addAttribute("now", DateUtil.formatYmd(now));
    if (!Strings.isNullOrEmpty(day)) {
      model.addAttribute("day", InnerUtil.format(day));
    }
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(day, last, actionid, browser, null));
    return "web/actionid_list";
  }

  @RequestMapping("/actionid_cmp")
  public String actionIdCompare(@RequestParam String browser, @RequestParam String actionid, @RequestParam String day, @RequestParam String cmp, @RequestParam(defaultValue = "1d") String last, Model model) {
    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parse(day, now);
    long cmpStamp = InnerUtil.parse(cmp, now);
    long offset = dayStamp - cmpStamp;
    List<Map> s1 = influxUriService.recentActionIdByDomload(last, day, actionid, browser);
    List<Map> s2 = influxUriService.recentActionIdByDomload(last, cmp, actionid, browser);
    model.addAttribute("nowCharts", InnerUtil.toDomloadCharts(s1));
    model.addAttribute("lastCharts", InnerUtil.toDomloadCharts(s2));
    for (Map m : s2) {
      Long ms = (Long) m.get("stamp");
      m.put("stamp", ms + offset);
    }

    model.addAttribute("actionid", actionid);
    model.addAttribute("browser", browser);
    model.addAttribute("now", DateUtil.formatYmd(now));
    model.addAttribute("cmp", cmp);
    model.addAttribute("day", InnerUtil.format(day));
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(day, last, actionid, browser, cmp));
    model.addAttribute("last", last);
    return "web/actionid_cmp";
  }

  @RequestMapping("/browser_list")
  public String browserList(@RequestParam(defaultValue = "") String actionid, @RequestParam(defaultValue = "1h") String last, @RequestParam(required = true) String browser, @RequestParam(required = false) String day, Model model) {
    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parse(day, now);
    model.addAttribute("last", last);
    model.addAttribute("browser", browser);
    model.addAttribute("times", ImmutableList.of("1h", "6h", "12h", "1d", "3d", "7d"));
    List<Map> browserList = ImmutableList.of();
    List<String> actionList = ImmutableList.of();
    try {
      actionList = influxUriService.getTags("web", "actionid", "oss_web");
      if (actionid.equals("") && actionList.size() > 0) {
        actionid = actionList.get(0);
      }
      browserList = influxUriService.recentBrowser(last, day, browser, actionid);
    } catch (Exception e) {
      model.addAttribute("message", "查询InfluxDB找不到信息");
    }
    if (!browserList.isEmpty()) {
      model.addAttribute("highcharts", InnerUtil.toDomloadCharts(browserList));
      model.addAttribute("highchartsTitle", browser);
    }
    model.addAttribute("data", InnerUtil.toJson9(browserList));
    model.addAttribute("now", DateUtil.formatYmd(now));
    if (!Strings.isNullOrEmpty(day)) {
      model.addAttribute("day", InnerUtil.format(day));
    }
    model.addAttribute("actionid", actionid);
    model.addAttribute("actionList", actionList);
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(day, last, actionid, browser, null));
    return "web/browser_list";
  }

  @RequestMapping("/browser_cmp")
  public String browserCompare(@RequestParam String actionid, @RequestParam String browser, @RequestParam String day, @RequestParam String cmp, @RequestParam(defaultValue = "1d") String last, Model model) {
    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parse(day, now);
    long cmpStamp = InnerUtil.parse(cmp, now);
    long offset = dayStamp - cmpStamp;
    List<Map> s1 = influxUriService.recentBrowser(last, day, browser, actionid);
    List<Map> s2 = influxUriService.recentBrowser(last, cmp, browser, actionid);
    model.addAttribute("nowCharts", InnerUtil.toDomloadCharts(s1));
    model.addAttribute("lastCharts", InnerUtil.toDomloadCharts(s2));
    for (Map m : s2) {
      Long ms = (Long) m.get("stamp");
      m.put("stamp", ms + offset);
    }

    model.addAttribute("browser", browser);
    model.addAttribute("actionid", actionid);
    model.addAttribute("now", DateUtil.formatYmd(now));
    model.addAttribute("cmp", cmp);
    model.addAttribute("day", InnerUtil.format(day));
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("urlPrefix", urlPrefix(day, last, actionid, browser, cmp));
    model.addAttribute("last", last);
    return "web/browser_cmp";
  }

  private String urlPrefix(String day, String last, String actionId, String browser, String cmp) {
    StringBuilder sbd = new StringBuilder(64);
    sbd.append("&last=").append(last);
    if (!Strings.isNullOrEmpty(actionId)) {
      sbd.append("&actionid=").append(UrlEscapers.urlFragmentEscaper().escape(actionId));
    }
    if (!Strings.isNullOrEmpty(browser)) {
      sbd.append("&browser=").append(UrlEscapers.urlFragmentEscaper().escape(browser));
    }
    if (!Strings.isNullOrEmpty(day)) {
      sbd.append("&day=").append(InnerUtil.format(day));
    }
    if (!Strings.isNullOrEmpty(cmp)) {
      sbd.append("&cmp=").append(UrlEscapers.urlFragmentEscaper().escape(cmp));
    }
    return sbd.toString();
  }
}
