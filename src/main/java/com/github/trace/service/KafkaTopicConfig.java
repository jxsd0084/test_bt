package com.github.trace.service;

import com.google.common.collect.Maps;

import com.github.autoconf.api.IConfig;
//import com.github.trace.exceptions.IniConfigException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * Topic管理
 * Created by wzk on 16/4/6.
 */
public class KafkaTopicConfig {
  private static final Logger LOG = LoggerFactory.getLogger(KafkaTopicConfig.class);

  public static final String EMPTY = "";

  public static final String THREAD_SIZE_CONFIG = "threadize";
  public static final String PARSE_TYPE_CONFIG = "parseType";
  public static final String WRITERS_CONFIG = "writers";
  public static final int THREAD_SIZE_DEFAULT = 1;
  public static final String PARSE_TYPE_DEFAULT = "json";
  public static final String WRITERS_DEFAULT = "hdfs";

  private static HierarchicalINIConfiguration iniConfiguration = new HierarchicalINIConfiguration();

  private KafkaTopicConfig() {
  }

  public static void initTopicConfig(IConfig config) {
    LOG.info("Reload kafka2hdfs-topic config... ");
    byte[] content = config.getContent();
    InputStream inputStream = new ByteArrayInputStream(content);
    try {
      iniConfiguration.clear();
      iniConfiguration.load(inputStream);
      iniConfiguration.setThrowExceptionOnMissing(false);
    } catch (ConfigurationException e) {
//      throw new IniConfigException("Cannot load ini config " + config.getName(), e);
    }
  }

  public static Map<String, Integer> getTopicCountMap() {
    Map<String, Integer> topicCountMap = Maps.newHashMap();
    Set<String> topics = iniConfiguration.getSections();
    for (String topic : topics) {
      int threadSize = getThreadSize(topic);
      topicCountMap.put(topic, threadSize);
    }
    return topicCountMap;
  }

  public static Set<String> getTopics() {
    return iniConfiguration.getSections();
  }

  public static SubnodeConfiguration getTopicConfig(String topic) {
    return iniConfiguration.getSection(topic);
  }

  public static String get(String topic, String key, String defaultValue) {
    SubnodeConfiguration sectionConfig = getTopicConfig(topic);
    return sectionConfig == null ? defaultValue : sectionConfig.getString(key, defaultValue);
  }

  public static int getInt(String topic, String key, int defaultValue) {
    SubnodeConfiguration sectionConfig = getTopicConfig(topic);
    return sectionConfig == null ? defaultValue : sectionConfig.getInt(key, defaultValue);
  }

  public static String get(String topic, String key) {
    return get(topic, key, EMPTY);
  }

  public static int getThreadSize(String topic) {
    return getInt(topic, THREAD_SIZE_CONFIG, THREAD_SIZE_DEFAULT);
  }

  public static String getParseType(String topic) {
    return get(topic, PARSE_TYPE_CONFIG, PARSE_TYPE_DEFAULT);
  }

  public static String getWriters(String topic) {
    return get(topic, WRITERS_CONFIG, WRITERS_DEFAULT);
  }

}
