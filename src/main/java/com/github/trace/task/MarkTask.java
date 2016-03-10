package com.github.trace.task;

import com.github.trace.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每五分钟刷新一次备注缓存信息
 * Created by hanmz on 2015/12/23.
 */
@Component
public class MarkTask {
  @Autowired
  private MarkService markService;

  @Scheduled(cron = "0 0/5 * * * ?")
  void scheduleRefresh() {
    markService.refresh(System.currentTimeMillis() / 1000 - 300);
  }
}
