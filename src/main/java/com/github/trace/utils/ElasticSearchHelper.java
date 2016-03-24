package com.github.trace.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

import com.github.autoconf.ConfigFactory;
import com.github.autoconf.api.IConfig;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * ElasticSearch Helper
 * Created by wzk on 16/1/19.
 */
public class ElasticSearchHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchHelper.class);

  private static Client client = null;

  static {
    ConfigFactory.getInstance().getConfig("elasticsearch", ElasticSearchHelper::loadConfig);
  }

  private static void loadConfig(IConfig config) {
    String cluster = config.get("cluster.name");
    Settings settings = Settings.settingsBuilder().put("cluster.name", cluster).build();
    TransportClient c = TransportClient.builder().settings(settings).build();
    String serverUrl = config.get("servers");
    LOG.info("{}.serverUrl={}", cluster, serverUrl);
    for (String i : Splitter.on(',').trimResults().omitEmptyStrings().split(serverUrl)) {
      int pos = i.indexOf(':');
      String ip = i;
      int port = 9300;
      if (pos > 0) {
        ip = i.substring(0, pos);
        port = Integer.parseInt(i.substring(pos + 1));
      }
      try {
        c.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
      } catch (UnknownHostException e) {
        LOG.error("cannot parse address:{}", ip, e);
      }
    }
    if (client != null) {
      client.close();
    }
    client = c;
  }

  public static void bulk(String index, String type, Set<String> items) {
    Preconditions.checkNotNull(index, "elasticsearch index should not be empty");
    Preconditions.checkNotNull(type, "elasticsearch type should not be empty");
    BulkRequestBuilder bulkRequest = client.prepareBulk();
    for (String i : items) {
      bulkRequest.add(client.prepareIndex(index, type).setSource(i));
    }
    if (bulkRequest.numberOfActions() > 0) {
      BulkResponse bulkResponse = bulkRequest.execute().actionGet();
      if (bulkResponse.hasFailures()) {
        LOG.error("cannot execute bulkIndex, reason: {}, items: {}", bulkResponse.buildFailureMessage(), items);
      } else {
        LOG.info("save {} items to ElasticSearch in {}", items.size(), index);
      }
    }
  }

}
