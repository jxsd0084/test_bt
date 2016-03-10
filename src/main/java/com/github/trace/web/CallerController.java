package com.github.trace.web;

import com.github.trace.intern.InnerUtil;
import com.github.trace.intern.TimePickerUtil;
import com.github.trace.service.InfluxRpcService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 主叫服务
 * Created by wangyuebin on 15/12/3.
 */
@Controller
public class CallerController {
  @Autowired
  private InfluxRpcService influxRpcService;

  @RequestMapping("/callerlist")
  public String callerList(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, Model model) {
    List<Map> caller = influxRpcService.recentCaller(start, end);
    model.addAttribute("data", InnerUtil.toJsonService(caller));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("times", Lists.newArrayList("10m", "30m", "1h", "6h", "12h", "1d", "2d", "3d", "5d"));
    return "caller/caller_list";
  }

  @RequestMapping("/caller_detail")
  public String callerDetail(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, @RequestParam(required = true) String name, Model model) {
    List<Map> detail = influxRpcService.recentModuleByCaller(start, end, name);
    model.addAttribute("data", InnerUtil.toJsonService(detail));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("name", name);
    model.addAttribute("times", Lists.newArrayList("10m", "30m", "1h", "6h", "12h", "1d", "2d", "3d", "5d"));
    return "caller/caller_detail";
  }
}
