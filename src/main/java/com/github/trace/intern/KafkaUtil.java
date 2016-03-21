package com.github.trace.intern;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;

import com.github.trace.entity.KafkaMessageAndOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

/**
 * Kafka相关工具类
 * Created by wzk on 16/3/17.
 */
public class KafkaUtil {

  private static final Logger LOG = LoggerFactory.getLogger(KafkaUtil.class);

  private KafkaUtil() {
  }

  public static TreeMap<Integer, PartitionMetadata> findLeader(List<String> a_seedBrokers, int a_port, String a_topic) {
    TreeMap<Integer, PartitionMetadata> map = new TreeMap<>();
    for (String seed : a_seedBrokers) {
      SimpleConsumer consumer = null;
      try {
        consumer = new SimpleConsumer(seed, a_port, 100000, 64 * 1024, "leaderLookup_" + System.currentTimeMillis());
        List<String> topics = Collections.singletonList(a_topic);
        TopicMetadataRequest req = new TopicMetadataRequest(topics);
        kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

        List<TopicMetadata> metaData = resp.topicsMetadata();
        for (TopicMetadata item : metaData) {
          for (PartitionMetadata part : item.partitionsMetadata()) {
            map.put(part.partitionId(), part);
          }
        }
      } catch (Exception e) {
        LOG.error("Error communicating with Broker [{}] to find Leader for [{}]", seed, a_topic, e);
      } finally {
        if (consumer != null) {
          consumer.close();
        }
      }
    }
    return map;
  }

  public static long getLastOffset(SimpleConsumer consumer, String topic,
                                   int partition, long whichTime, String clientName) {
    TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
    Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<>();
    requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
    kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(
        requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
    OffsetResponse response = consumer.getOffsetsBefore(request);

    if (response.hasError()) {
      LOG.error("Error fetching offset for topic [{}], partition [{}]. Reason: {}",
                topic, partition, response.errorCode(topic, partition));
      return 0L;
    }
    long[] offsets = response.offsets(topic, partition);
    return offsets[0];
  }


  public static Set<KafkaMessageAndOffset> fetchData(SimpleConsumer consumer, String topic, int partition,
                                                     String clientName, long offsetFrom, int fetchSize) {
    Set<KafkaMessageAndOffset> results = Sets.newHashSet();

    kafka.api.FetchRequest fetchRequest = new FetchRequestBuilder().clientId(clientName)
                                            .addFetch(topic, partition, offsetFrom, fetchSize).build();
    kafka.javaapi.FetchResponse response = consumer.fetch(fetchRequest);

    if (response.hasError()) {
      LOG.error("Error fetching data for topic [{}], partition [{}]. Reason: {}",
                topic, partition, response.errorCode(topic, partition));
      return results;
    }

    ByteBufferMessageSet messageAndOffsetSet = response.messageSet(topic, partition);
    for (MessageAndOffset messageAndOffset : messageAndOffsetSet) {
      long offset = messageAndOffset.offset();
      ByteBuffer payload = messageAndOffset.message().payload();
      messageAndOffset.message().attributes();
      byte[] bytes = new byte[payload.limit()];
      payload.get(bytes);
      String message = new String(bytes, Charsets.UTF_8);
      results.add(new KafkaMessageAndOffset(offset, message));
    }

    return results;
  }

}
