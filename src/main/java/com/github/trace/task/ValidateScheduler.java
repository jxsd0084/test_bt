package com.github.trace.task;

import com.google.common.collect.ImmutableList;

import com.alibaba.fastjson.JSON;
import com.github.trace.service.CEPService;
import com.github.trace.service.KafkaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 定时从kafka拉取日志进行校验, 校验失败入ES
 * Created by wzk on 16/3/23.
 */
@Component
public class ValidateScheduler {

  private static List<String> topics = ImmutableList.of("dcx.MonitorRequest", "dcx.WebRequest");
  private static final long SAMPLE_RATE = 60000L;
  private static final int SAMPLE_COUNT = 10;

  @Autowired
  private KafkaService kafkaService;
  @Autowired
  private CEPService cepService;

  @Scheduled(fixedRate = SAMPLE_RATE)
  public void scheduler() {
    topics.forEach(topic -> {
      String type = "json";
      Set<String> sampleLogs = kafkaService.getMessages(topic, type, 1);
      batchValidate(topic, sampleLogs);
    });
  }

  @Async
  private void batchValidate(String topic, Set<String> set) {
    set.forEach(log -> {
      boolean ret = cepService.compareByTopic(topic, "[" + log + "]");
      System.out.println(ret);
    });
  }

  public void saveToEs(String index, String type, String log) {

  }

}
