package com.github.trace.task;

import com.google.common.collect.Sets;

import com.alibaba.fastjson.JSONObject;
import com.github.autoconf.ConfigFactory;
import com.github.trace.service.CEPService;
import com.github.trace.service.KafkaService;
import com.github.trace.utils.ElasticSearchHelper;

import org.apache.commons.lang.StringUtils;
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
      String type = "json";
      Set<String> sampleLogs = kafkaService.getMessages(topic, type, sampleCount);
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
    saveToEs(ES_INDEX, topic, toEs);
  }

  private void saveToEs(String index, String type, Set<String> logs) {
    Set<String> convertedLogs = handle(logs);
    ElasticSearchHelper.bulk(index, type, convertedLogs);
  }

  private Set<String> handle(Set<String> logs) {
    Set<String> results = Sets.newHashSet();

    logs.forEach(log -> {
      JSONObject json = JSONObject.parseObject(log);
      JSONObject object = new JSONObject();
      json.entrySet().forEach(entry -> {
        String keyConverted = convertFieldName(entry.getKey());
        String value = entry.getValue().toString();
        object.put(keyConverted, value);
      });
      results.add(object.toJSONString());
    });
    return results;
  }

  private String convertFieldName(String key) {
    if (StringUtils.equals(key, "_time")) {
      return "stamp";
    }
    if (StringUtils.contains(key, '.')) {
      return StringUtils.replaceChars(key, '.', '_');
    }
    return key;
  }

}
