package com.github.trace.web;

import com.github.trace.intern.DateUtil;
import com.github.trace.intern.InnerUtil;
import com.github.trace.intern.TimePickerUtil;
import com.github.trace.service.InfluxUriService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * 页面监控逻辑
 * Created by wangyb on 10/29/2015.
 */
@Controller
public class PageController {
    @Autowired
    private InfluxUriService influxUriService;

    @RequestMapping("/page")
    public String pageList(@RequestParam(defaultValue = "最近1小时") String last,
                           @RequestParam(defaultValue = "now() - 1h") String start,
                           @RequestParam(defaultValue = "now()") String end,
                           @RequestParam(required = false) String uri,
                           Model model) {
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
        model.addAttribute("times", ImmutableList.of("1h", "6h", "12h", "1d", "3d", "7d"));
        List<Map> pageList = ImmutableList.of();
        try {
            if (Strings.isNullOrEmpty(uri)) {
                pageList = influxUriService.recentPage(start, end);
            } else {
                pageList = influxUriService.recentPageByUri(start, end, uri);
            }
        } catch (Exception e) {
            model.addAttribute("message", "查询InfluxDB找不到信息");
        }
        model.addAttribute("data", InnerUtil.toJson3(pageList));
        return "page/page_list";
    }

    @RequestMapping("/uri")
    public String uriList(@RequestParam(defaultValue = "最近1小时") String last,
                          @RequestParam(defaultValue = "now() - 1h") String start,
                          @RequestParam(defaultValue = "now()") String end,
                          @RequestParam String uri,
                          @RequestParam String app,
                          @RequestParam(required = false) String day,
                          Model model) {
        long now = System.currentTimeMillis();
        long dayStamp = InnerUtil.parse(day, now);
        List<Map> uriList = ImmutableList.of();
        try {
            uriList = influxUriService.recentUri(start, end, uri, app);
        } catch (Exception e) {
            model.addAttribute("message", "查询InfluxDB找不到信息");
        }
        model.addAttribute("data", InnerUtil.toJson4(uriList));
        if (!uriList.isEmpty()) {
            model.addAttribute("highcharts", InnerUtil.toCharts(uriList));
            model.addAttribute("highchartsTitle", uri);
        }

        uriList = ImmutableList.of();
        try {
            uriList = influxUriService.recentUriByMachine(start, end, uri, app);
        } catch (Exception e) {
            model.addAttribute("message", "查询InfluxDB找不到信息");
        }
        model.addAttribute("machines", InnerUtil.toJson5(uriList));

        model.addAttribute("now", DateUtil.formatYmd(now));
        if (!Strings.isNullOrEmpty(day)) {
            model.addAttribute("day", InnerUtil.format(day));
        }
        model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
        model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
        model.addAttribute("urlPrefix", urlPrefix(day, last, start, end, uri, null, app));
        model.addAttribute("app", app);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
        model.addAttribute("times", ImmutableList.of("1h", "6h", "12h", "1d", "3d", "7d"));
        model.addAttribute("uri", uri);

        return "page/uri_detail";
    }

    @RequestMapping("/cmp-uri")
    @SuppressWarnings("unchecked")
    public String compareUri(@RequestParam String uri,
                             @RequestParam String app,
                             @RequestParam String day,
                             @RequestParam String cmp,
                             @RequestParam(defaultValue = "最近1小时") String last,
                             Model model) {
        String dayStart = TimePickerUtil.setDayStart(day);
        String dayEnd = TimePickerUtil.setDayEnd(day);
        String cmpStart = TimePickerUtil.setDayStart(cmp);
        String cmpEnd = TimePickerUtil.setDayEnd(cmp);
        long now = System.currentTimeMillis();
        long dayStamp = InnerUtil.parseYmdHis(dayEnd, now);
        long cmpStamp = InnerUtil.parseYmdHis(cmpEnd, now);
        long offset = dayStamp - cmpStamp;
        cmp = TimePickerUtil.setCmp(cmp);

        List<Map> s1 = influxUriService.recentUri(dayStart, dayEnd, uri, day, app);
        List<Map> s2 = influxUriService.recentUri(cmpStart, cmpEnd, uri, cmp, app);
        for (Map m : s2) {
            Long ms = (Long) m.get("stamp");
            m.put("stamp", ms + offset);
        }
        model.addAttribute("nowCharts", InnerUtil.toCharts(s1));
        model.addAttribute("lastCharts", InnerUtil.toCharts(s2));
        model.addAttribute("uri", uri);
        model.addAttribute("now", DateUtil.formatYmd(now));
        model.addAttribute("cmp", cmp);
        model.addAttribute("day", InnerUtil.format(day));
        model.addAttribute("app", app);
        model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
        model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
        model.addAttribute("urlPrefix", urlPrefix(day, last, null, null, uri, cmp, app));
        model.addAttribute("last", last);
        return "page/uri_compare";
    }

    @RequestMapping("cmp-server")
    public String compareByMachine(@RequestParam String uri,
                                   @RequestParam String app,
                                   @RequestParam String day,
                                   @RequestParam(defaultValue = "1d") String last,
                                   Model model) {
        long now = System.currentTimeMillis();
        List<Map> uriList = ImmutableList.of();
        try {
            uriList = influxUriService.recentUriByMachine(last, uri, day, app);
        } catch (Exception e) {
            model.addAttribute("message", "查询InfluxDB找不到信息");
        }
        model.addAttribute("data", InnerUtil.toJson5(uriList));
        model.addAttribute("now", DateUtil.formatYmd(now));
        if (!Strings.isNullOrEmpty(day)) {
            model.addAttribute("day", InnerUtil.format(day));
        }
        model.addAttribute("app", app);
        model.addAttribute("urlPrefix", urlPrefix(day, last, null, null, uri, null, app));
        model.addAttribute("last", last);
        model.addAttribute("times", ImmutableList.of("1h", "6h", "12h", "1d", "3d", "7d"));
        model.addAttribute("uri", uri);

        return "page/machine_list";
    }

    @RequestMapping("server-detail")
    public String serverDetail(@RequestParam(required = false) String day,
                               @RequestParam String server,
                               @RequestParam String uri,
                               @RequestParam String app,
                               @RequestParam(defaultValue = "最近1小时") String last,
                               @RequestParam(defaultValue = "now() - 1h") String start,
                               @RequestParam(defaultValue = "now()") String end,
                               Model model) {
        long now = System.currentTimeMillis();
        List<Map> uriList = ImmutableList.of();
        try {
            uriList = influxUriService.recentUri(start, end, uri, server, app);
        } catch (Exception e) {
            model.addAttribute("message", "查询InfluxDB找不到信息");
        }
        model.addAttribute("data", InnerUtil.toJson4(uriList));
        model.addAttribute("now", DateUtil.formatYmd(now));
        if (!Strings.isNullOrEmpty(day)) {
            model.addAttribute("day", InnerUtil.format(day));
        }

        model.addAttribute("app", app);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
        model.addAttribute("urlPrefix", urlPrefix(day, last, start, end, null, null, app));
        model.addAttribute("times", ImmutableList.of("1h", "6h", "12h", "1d", "3d", "7d"));
        model.addAttribute("server", server);
        model.addAttribute("uri", uri);

        return "page/machine_detail";
    }

    @RequestMapping("uri-server")
    public String uriByServer(@RequestParam String uri,
                              @RequestParam String app,
                              @RequestParam String server,
                              @RequestParam(required = false) String day,
                              @RequestParam(defaultValue = "最近1小时") String last,
                              @RequestParam(defaultValue = "now() - 1h") String start,
                              @RequestParam(defaultValue = "now()") String end,
                              Model model) {
        long now = System.currentTimeMillis();
        long dayStamp = InnerUtil.parse(day, now);
        List<Map> uriList = ImmutableList.of();
        try {
            uriList = influxUriService.recentUri(start, end, uri, server, app);
        } catch (Exception e) {
            model.addAttribute("message", "查询InfluxDB找不到信息");
        }
        model.addAttribute("data", InnerUtil.toJson4(uriList));
        if (uriList.size() > 0) {
            model.addAttribute("highcharts", InnerUtil.toCharts(uriList));
            model.addAttribute("highchartsTitle", uri);
        }

        model.addAttribute("now", DateUtil.formatYmd(now));
        if (!Strings.isNullOrEmpty(day)) {
            model.addAttribute("day", InnerUtil.format(day));
        }
        model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
        model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
        model.addAttribute("urlPrefix", urlPrefix(day, last, start, end, uri, null, app));
        model.addAttribute("server", server);

        model.addAttribute("app", app);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
        model.addAttribute("times", ImmutableList.of("1h", "6h", "12h", "1d", "3d", "7d"));
        model.addAttribute("uri", uri);

        return "page/uri_server_list";
    }

    private String urlPrefix(String day, String last, String start, String end, String name, String cmp, String app) {
        Escaper escaper = UrlEscapers.urlFragmentEscaper();
        StringBuilder sbd = new StringBuilder(64);
        if (!Strings.isNullOrEmpty(last)) {
            sbd.append("last=").append(last);
        }
        if (!Strings.isNullOrEmpty(start)) {
            sbd.append("&start=").append(start);
        }
        if (!Strings.isNullOrEmpty(end)) {
            sbd.append("&end=").append(end);
        }
        if (!Strings.isNullOrEmpty(name)) {
            sbd.append("&uri=").append(escaper.escape(name));
        }
        if (!Strings.isNullOrEmpty(app)) {
            sbd.append("&app=").append(escaper.escape(app));
        }
        if (!Strings.isNullOrEmpty(day)) {
            sbd.append("&day=").append(InnerUtil.format(day));
        }
        if (!Strings.isNullOrEmpty(cmp)) {
            sbd.append("&cmp=").append(escaper.escape(cmp));
        }
        return sbd.toString();
    }
}
