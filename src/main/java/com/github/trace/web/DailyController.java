package com.github.trace.web;

import com.github.trace.intern.DateUtil;
import com.github.trace.intern.InnerUtil;
import com.github.trace.mapper.DailyMapper;
import com.github.trace.service.DailyService;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 日报
 * Created by hanmz on 2015/12/23
 */
@Controller
@RequestMapping("/daily")
public class DailyController {
  private Logger LOG = LoggerFactory.getLogger(DailyController.class);
  @Autowired
  private DailyMapper dailyMapper;
  @Autowired
  private DailyService dailyService;

  @RequestMapping("")
  public String dailyList(@RequestParam(required = false) String day, Model model) {
    if (day == null) {
      day = DateUtil.formatYmd(System.currentTimeMillis() - InnerUtil.ONE_DAY);
    }
    String data = dailyMapper.findByDay(day);
    long now = System.currentTimeMillis() - InnerUtil.ONE_DAY;
    long dayStamp = InnerUtil.parse(day, now);

    if (!Strings.isNullOrEmpty(day)) {
      model.addAttribute("day", InnerUtil.format(day));
    }
    model.addAttribute("data", data);
    model.addAttribute("now", DateUtil.formatYmd(now));
    model.addAttribute("lastDay", DateUtil.formatYmd(dayStamp - InnerUtil.ONE_DAY));
    model.addAttribute("lastWeek", DateUtil.formatYmd(dayStamp - 7 * InnerUtil.ONE_DAY));
    return "daily/daily_list";
  }

  @RequestMapping("mail")
  public String mailForm() {
    return "daily/mail_form";
  }

  @RequestMapping(value = "sendmail", method = RequestMethod.POST)
  public String sendMail(@RequestParam String day, @RequestParam String receiver, RedirectAttributes r) {
    try {
      dailyService.sendMail(day, receiver);
      r.addFlashAttribute("message", "发送邮件成功");
    } catch (Exception e) {
      r.addFlashAttribute("message", "发送失败,原因:" + e);
      r.addFlashAttribute("day", day);
      r.addFlashAttribute("receiver", receiver);
      LOG.error("cannot send {}", day, e);
    }
    return "redirect:/daily/mail";
  }
}
