package com.github.trace.service;

import com.alibaba.fastjson.JSONObject;
import com.github.autoconf.ConfigFactory;
import com.github.autoconf.api.IConfig;
import com.github.trace.entity.Mark;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 存取备注信息
 * Created by hanmz on 2016/1/14.
 */
@Service
public class MarkService {
  private Map<String, String> comments = Maps.newHashMap();
  private String modifyUrl;
  private String getAllUrl;
  @Autowired
  private OkHttpService okHttpService;

  @PostConstruct
  void init() {
    ConfigFactory.getInstance().getConfig("oss-mark", this::loadConfig);
    refresh(1);
  }

  private void loadConfig(IConfig config) {
    this.modifyUrl = config.get("modify");
    this.getAllUrl = config.get("get-all");
  }

  public void refresh(long stamp) {
    List<Mark> marks = okHttpService.getAllMark(getAllUrl + "&stamp=" + stamp);
    if (marks != null && !marks.isEmpty()) {
      Map<String, String> latest = Maps.newHashMap();
      for (Mark i : marks) {
        latest.put(i.getService().trim(), i.getDescription().trim());
      }
      comments = latest;
    }
  }

  public String comment(String s) {
    s = s.trim();
    return comments.containsKey(s) ? comments.get(s) : "";
  }

  public String save(List<Mark> marks) {
    String message = okHttpService.saveMark(modifyUrl, JSONObject.toJSONString(marks));
    if ("OK".equals(message)) {
      for (Mark i : marks) {
        comments.put(i.getService().trim(), i.getDescription());
      }
    }
    return message;
  }
}
