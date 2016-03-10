package com.github.trace.task;

import com.github.trace.service.DailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 每日00:01存储日报,08:00发送日报邮件
 * Created by hanmz on 2015/12/23.
 */
@Component
public class DailyTask {
  private Logger LOG = LoggerFactory.getLogger(DailyTask.class);
  @Autowired
  private DailyService dailyService;

  @Scheduled(cron = "0 0 8 * * ?")
  void scheduleSendMail() {
    try {
      dailyService.sendMail(null, null);
    } catch (Exception e) {
      LOG.error("send email occur a error {}", e);
    }
  }

  @Scheduled(cron = "0 0 1 * * ?")
  void scheduleStorageDaily() {
    try {
      dailyService.storageDaily();
    } catch (Exception e) {
      LOG.error(e.getMessage());
    }
  }
}
