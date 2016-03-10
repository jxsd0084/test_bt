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
 * 被叫服务
 * Created by wangyuebin on 15/12/3.
 */
@Controller
public class CalledController {
  @Autowired
  private InfluxRpcService influxRpcService;

  @RequestMapping("/called")
  public String callerList(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, Model model) {
    List<Map> caller = influxRpcService.recentCalled(start, end);
    model.addAttribute("data", InnerUtil.toJsonService(caller));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    return "called/called_list";
  }

  @RequestMapping("/called_detail")
  public String callerDetail(@RequestParam(defaultValue = "最近1小时") String last, @RequestParam(defaultValue = "now() - 1h") String start, @RequestParam(defaultValue = "now()") String end, @RequestParam(required = true) String name, Model model) {
    List<Map> detail = influxRpcService.recentModuleByServer(start, end, name);
    model.addAttribute("data", InnerUtil.toJsonService(detail));
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("last", TimePickerUtil.setLast(last, start, end));
    model.addAttribute("name", name);
    return "called/called_detail";
  }
}
