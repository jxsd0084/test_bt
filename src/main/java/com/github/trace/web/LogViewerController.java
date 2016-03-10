package com.github.trace.web;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import com.github.trace.entity.Log;
import com.github.trace.intern.DateUtil;
import com.github.trace.intern.InnerUtil;
import com.github.trace.intern.TimePickerUtil;
import com.github.trace.service.InfluxMetricService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日志管理页
 * Created by jiangwj on 2016/1/28.
 */
@Controller
@RequestMapping("/log")
public class LogViewerController {

  @Autowired
  InfluxMetricService influxMetricService;

  @RequestMapping("/list")
  public String serviceList(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end,Model model) {
    List<Map> items = influxMetricService.listServiceLog(start, end);
    model.addAttribute("data", InnerUtil.toJsonLog(items));
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "log/servicelog_list";
  }

  @RequestMapping("/detail")
  @SuppressWarnings("unchecked")
  public String detail(@RequestParam String name, @RequestParam(defaultValue = "最近1小时") String last,
                       @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end,
                       @RequestParam(required = false) String day,  Model model) {
    String where = "and app='"+name.split("/")[0]+"' and ip='"+name.split("/")[1]+"'";
    List<Map> detailItems = influxMetricService.recentLog(start, end, "logback.error", where);
    long now = System.currentTimeMillis();
    long dayStamp = InnerUtil.parse(day, now);
    model.addAttribute("detailData", InnerUtil.toJsonHistoryLog(detailItems));
    if (detailItems.size() > 0) {
      model.addAttribute("highcharts", InnerUtil.toLogCharts(detailItems));
      model.addAttribute("highchartsTitle", name);
    }
    model.addAttribute("name", name);
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "log/servicelog_detail";
  }


  @RequestMapping("/view")
  @SuppressWarnings("unchecked")
  public String view(@RequestParam String name, @RequestParam(defaultValue = "最近1小时") String last,
                       @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, Model model) {
    String where = "and app='"+name.split("/")[0]+"' and ip='"+name.split("/")[1]+"'";
    List<Map> detailItems = influxMetricService.recentLogMessage(start, end, where);
    List<Log> items = new ArrayList<>();
    int n=0;
    for (Map m : detailItems) {
      String content = m.get("value").toString();
      List<String> itr = Splitter.on("\n\n").splitToList(content);
      for(String s:itr){
        if(s.trim().equals("")) continue;
        String time = s.trim().substring(0,19);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
          format.parse(time);
        } catch (ParseException e) {
          items.get(n-1).setContent(items.get(n-1).getContent()+s);
          continue;
        }
        String item = s.trim().substring(20);
        String []arr2 = item.split("\t");
        Log log = new Log();
        log.setTime(time);
        log.setThreadName(arr2[0]);
        log.setClassName(arr2[1]);
        String []arr3 = arr2[2].split("\n");
        StringBuilder msb =new StringBuilder();
        for(int i=0;i<arr3.length;i++){
          if(arr3[i].contains("Exception")) break;
          msb.append(arr3[i]).append("\n");
        }
        log.setMessage(msb.toString());
        StringBuilder sb = new StringBuilder();
        for(int j=2;j<arr2.length;j++){
          sb.append(arr2[j]).append("\t");
        }
        String stack = sb.toString().substring(msb.toString().length());
        CharMatcher charMatcher = CharMatcher.is('\t');
        stack =  charMatcher.replaceFrom(stack,"&nbsp;&nbsp;&nbsp;&nbsp;");
        log.setContent(stack);
        items.add(log);
        n++;
      }
    }
    model.addAttribute("data", InnerUtil.toJsonLogMessage(items));
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("name", name);
    return "log/servicelog_view";
  }



}
