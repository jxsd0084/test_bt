package com.github.trace.web;

import com.github.trace.entity.Mark;
import com.github.trace.service.MarkService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class MarkController {
  @Autowired
  private MarkService markService;

  @RequestMapping(value = "/edit/mark", method = RequestMethod.POST)
  @ResponseBody
  public Map editDesc(@RequestBody List<Mark> markList) {
    String message = markService.save(markList);
    if ("OK".equals(message)) {
      return ImmutableMap.of("code", 200, "info", message);
    }
    return ImmutableMap.of("code", -1, "info", message);
  }
}
