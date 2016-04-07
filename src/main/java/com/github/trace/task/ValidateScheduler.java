package com.github.trace.task;

import com.google.common.collect.Sets;

import com.github.autoconf.ConfigFactory;
import com.github.trace.service.CEPService;
import com.github.trace.service.KafkaService;
import com.github.trace.utils.ElasticSearchHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

import javax.annotation.PostConstruct;

/**
 * 定时从kafka拉取日志进行校验, 校验失败入ES
 * Created by wzk on 16/3/23.
 */
@Component
class ValidateScheduler {
  private static final long SAMPLE_RATE = 60000L;
  private static int sampleCount = 10;
  private static boolean scheduleSwitch = true;
  private static final String ES_INDEX = "datapt-validate";

  @Autowired
  private KafkaService kafkaService;
  @Autowired
  private CEPService cepService;

  @PostConstruct
  public void init() {
    ConfigFactory.getInstance().getConfig("buriedtool-scheduler").addListener(config -> {
      scheduleSwitch = config.getBool("scheduleSwitch", true);
      sampleCount = config.getInt("sampleCount", 10);
    });
  }

  @Scheduled(fixedDelay = SAMPLE_RATE)
  void scheduler() {
    if (!scheduleSwitch) {
      return;
    }
    Set<String> topics = cepService.getAllTopics();
    topics.forEach(topic -> {
      Set<String> sampleLogs = kafkaService.getMessages(topic, sampleCount);
      batchValidate(topic, sampleLogs);
    });
  }

  @Async
  private void batchValidate(String topic, Set<String> set) {
    Set<String> toEs = Sets.newHashSet();
    set.forEach(log -> {
      boolean valid = cepService.compareByTopic(topic, "[" + log + "]");
      if (!valid) {
        toEs.add(log);
      }
    });
    ElasticSearchHelper.bulk(ES_INDEX, topic, toEs);
  }

}
