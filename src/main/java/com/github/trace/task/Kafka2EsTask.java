package com.github.trace.task;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import com.github.autoconf.ConfigFactory;
import com.github.autoconf.api.IChangeListener;
import com.github.autoconf.api.IChangeableConfig;
import com.github.autoconf.api.IConfig;
import com.github.trace.service.KafkaTopicConfig;
import com.github.trace.utils.ElasticSearchHelper;
import com.github.trace.utils.JsonLogHandler;
import com.github.trace.utils.NginxLogHandler;

import org.antlr.v4.runtime.misc.MultiMap;
import org.apache.commons.lang.StringUtils;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * kafka2es
 * Created by wzk on 16/4/6.
 */
@Component
public class Kafka2EsTask {
  private static final Logger LOG = LoggerFactory.getLogger(Kafka2EsTask.class);
  private static final String GROUP_ID = "buriedtool-kafka2es";
  private static final String ES_INDEX = "datapt-buriedtool";
  private ConsumerConnector consumerConnector;
  private ExecutorService service;

  private LinkedHashMultimap<String, String> current = LinkedHashMultimap.create();

  @PostConstruct
  void init() {
    final IChangeableConfig consumerConfig = ConfigFactory.getInstance().getConfig("kafka-consumer");
    final IChangeableConfig topicConfig = ConfigFactory.getInstance().getConfig("buriedtool-kafka2es-topic");
    IChangeListener changeListener = conf -> {
      loadConsumerConfig(consumerConfig);
      loadTopicConfig(topicConfig);
    };
    consumerConfig.addListener(changeListener);
    topicConfig.addListener(changeListener);
  }

  @Scheduled(fixedDelay = 500L)
  void saveToEs() {
    LinkedHashMultimap<String, String> snapshot = current;
    current = LinkedHashMultimap.create();
    for (String topic : snapshot.keySet()) {
      Set<String> set = snapshot.get(topic);
      ElasticSearchHelper.bulk(ES_INDEX, topic, set);
    }
  }

  private void loadConsumerConfig(IConfig config) {
    Preconditions.checkNotNull(config.get("zookeeper.connect"));
    Properties kafkaProperties = new Properties();
    kafkaProperties.putAll(config.getAll());
    kafkaProperties.put("group.id", GROUP_ID);
    ConsumerConfig consumerConfig = new ConsumerConfig(kafkaProperties);
    if (consumerConnector != null) {
      consumerConnector.commitOffsets(true);
      consumerConnector.shutdown();
    }
    consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
  }

  private void loadTopicConfig(IConfig config) {
    KafkaTopicConfig.initTopicConfig(config);
    ExecutorService old = service;
    ThreadFactory daemonThreadFactory =
        new ThreadFactoryBuilder().setNameFormat("kafka2es-%d").setDaemon(true).build();
    service = Executors.newSingleThreadExecutor(daemonThreadFactory);
    service.submit(this::listen);
    if (old != null) {
      try {
        old.awaitTermination(1, TimeUnit.MINUTES);
      } catch (InterruptedException e) {
        LOG.error("Cannot shutdown executors", e);
        if (!old.isShutdown()) {
          old.shutdownNow();
        }
      }
    }
  }

  private void listen() {
    LOG.info("Begin to consume from kafka... ");
    Map<String, Integer> topicCountMap = KafkaTopicConfig.getTopicCountMap();
    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connectToKafka(topicCountMap);
    if (consumerMap == null || consumerMap.isEmpty()) {
      LOG.error("ConsumerMap is empty.");
      return;
    }

    ExecutorService executorService = Executors.newCachedThreadPool();
    for (Map.Entry<String, List<KafkaStream<byte[], byte[]>>> entry : consumerMap.entrySet()) {
      List<KafkaStream<byte[], byte[]>> streams = entry.getValue();
      for (final KafkaStream<byte[], byte[]> stream : streams) {
        executorService.execute(new KafkaConsumerRunnable(stream));
      }
    }
  }

  private Map<String, List<KafkaStream<byte[], byte[]>>> connectToKafka(Map<String, Integer> topicCountMap) {
    if (consumerConnector == null || topicCountMap == null || topicCountMap.isEmpty()) {
      return Maps.newHashMap();
    }
    return consumerConnector.createMessageStreams(topicCountMap);
  }

  private class KafkaConsumerRunnable implements Runnable {
    private KafkaStream<byte[], byte[]> stream;

    KafkaConsumerRunnable(KafkaStream<byte[], byte[]> stream) {
      super();
      this.stream = stream;
    }

    @Override
    public void run() {
      // 逐条处理消息
      for (MessageAndMetadata<byte[], byte[]> message : stream) {
        consume(message);
      }
    }

    private void consume(MessageAndMetadata<byte[], byte[]> message) {
      String topic = null;
      String content = null;
      try {
        topic = message.topic();
        content = new String(message.message(), Charsets.UTF_8);
        current.put(topic, convert(topic, content));
      } catch (Exception e) {
        LOG.error("Cannot consume topic={}, body={}", topic, content, e);
      }
    }

    private String convert(String topic, String content) {
      if (StringUtils.startsWith(topic, "nginx")) {
        return NginxLogHandler.parseNginx(content);
      }
      return JsonLogHandler.convert(content);
    }
  }

  @PreDestroy
  public void destroy() {
    if (consumerConnector != null) {
      consumerConnector.commitOffsets(true);
      consumerConnector.shutdown();
    }
    if (service != null) {
      service.shutdown();
    }
  }
}
