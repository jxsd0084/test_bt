package com.github.trace.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.autoconf.ConfigFactory;
import com.github.trace.entity.KafkaMessageAndOffset;
import com.github.trace.intern.KafkaUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.consumer.SimpleConsumer;

import static com.github.trace.intern.KafkaPropertyConstant.BOOSTRAP_SERVERS;
import static com.github.trace.intern.KafkaPropertyConstant.CONSUMER_BUFFER_SIZE;
import static com.github.trace.intern.KafkaPropertyConstant.CONSUMER_BUFFER_SIZE_DEFAULT;
import static com.github.trace.intern.KafkaPropertyConstant.CONSUMER_CONNECT_TIMEOUT;
import static com.github.trace.intern.KafkaPropertyConstant.CONSUMER_CONNECT_TIMEOUT_DEFAULT;
import static com.github.trace.intern.KafkaPropertyConstant.FETCH_SIZE;
import static com.github.trace.intern.KafkaPropertyConstant.FETCH_SIZE_DEFAULT;
import static com.github.trace.intern.KafkaPropertyConstant.SERVER_PORT;
import static com.github.trace.intern.KafkaPropertyConstant.SERVER_PORT_DEFAULT;

/**
 * 根据topic获取kafka中最新N条数据
 * Created by wzk on 16/3/17.
 */
@Service
public class KafkaService {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

  private static List<String> brokers = Lists.newArrayList();
  private static int port;
  private static int connectTimeout;
  private static int bufferSize;
  private static int fetchSize;

  @PostConstruct
  void init() {
    ConfigFactory.getInstance().getConfig("kafka-simple-consumer", config -> {
      Preconditions.checkNotNull(config.get(BOOSTRAP_SERVERS));

      String servers = config.get(BOOSTRAP_SERVERS);
      brokers = Splitter.on(",").splitToList(servers);
      port = config.getInt(SERVER_PORT, SERVER_PORT_DEFAULT);
      connectTimeout = config.getInt(CONSUMER_CONNECT_TIMEOUT, CONSUMER_CONNECT_TIMEOUT_DEFAULT);
      bufferSize = config.getInt(CONSUMER_BUFFER_SIZE, CONSUMER_BUFFER_SIZE_DEFAULT);
      fetchSize = config.getInt(FETCH_SIZE, FETCH_SIZE_DEFAULT);
    });
  }

  public Set<String> getMessages(String topic, String type, int count) {
    Set<KafkaMessageAndOffset> fetchedData = Sets.newHashSet();

    TreeMap<Integer, PartitionMetadata> metaDatas = KafkaUtil.findLeader(brokers, port, topic);

    for (Map.Entry<Integer, PartitionMetadata> entry : metaDatas.entrySet()) {
      int partition = entry.getKey();
      String leadBroker = entry.getValue().leader().host();
      String clientName = "Client_" + topic + "_" + partition;

      SimpleConsumer consumer = new SimpleConsumer(leadBroker, port,
                                                   connectTimeout, bufferSize, clientName);
      long lastOffset = KafkaUtil.getLastOffset(consumer, topic, partition,
                                            kafka.api.OffsetRequest.LatestTime(), clientName);
      if (lastOffset > 0) {
        fetchedData.addAll(KafkaUtil.fetchData(consumer, topic, partition,
                                               clientName, lastOffset - count, fetchSize));
      }
      consumer.close();
    }

    Set<String> results = Sets.newHashSet();
    for (KafkaMessageAndOffset messageAndOffset : fetchedData) {
      if (results.size() > count) {
        break;
      }
      results.add(messageAndOffset.getMessage());
    }
    return parse(results, type);
  }

  private Set<String> parse(Set<String> set, String type) {
    Set<String> results = Sets.newHashSet();
    if (set == null || set.isEmpty() || Strings.isNullOrEmpty(type)) {
      return results;
    }

    if (StringUtils.equals(type, "nginx")) {
      set.forEach(log -> {
        String json = parseNginx(log);
        if (StringUtils.isNotEmpty(json)) {
          results.add(json);
        }
      });
    } else {
      return set;
    }
    return results;
  }

  private String parseNginx(String log) {
    if (StringUtils.isEmpty(log)) {
      return StringUtils.EMPTY;
    }
    String params = StringUtils.substringBetween(log, ".gif?", " HTTP/1");
    if (StringUtils.isNotEmpty(params)) {
      return parseToJson(params);
    }
    return StringUtils.EMPTY;
  }

  private String parseToJson(String params) {
    Map<String, String> map = Maps.newHashMap();
    List<String> paramList = Splitter.on("&").omitEmptyStrings().splitToList(params);
    paramList.forEach(param -> {
      List<String> kv = Splitter.on("=").omitEmptyStrings().splitToList(param);
      if (kv.size() == 2) {
        map.put(kv.get(0), kv.get(1));
      }
    });
    return JSON.toJSONString(map);
  }

}
