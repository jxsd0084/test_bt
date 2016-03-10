package com.github.trace.service;

import com.github.autoconf.ConfigFactory;
import com.github.autoconf.api.IChangeListener;
import com.github.autoconf.api.IConfig;
import com.github.trace.TraceContext;
import com.github.trace.annotation.RpcTrace;
import com.github.trace.bean.AccessBean;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 搜索ElasticSearch
 * Created by lirui on 2015/08/28 下午5:52.
 */
@Service
@RpcTrace
public class ElasticSearch {
  private static final Logger LOG = LoggerFactory.getLogger(ElasticSearch.class);
  private Client client = null;
  private String serverUrl = null;

  @PostConstruct
  void init() {
    ConfigFactory.getInstance().getConfig("elasticsearch", new IChangeListener() {
      @Override
      public void changed(IConfig config) {
        loadConfig(config);
      }
    });
  }

  private void loadConfig(IConfig config) {
    String cluster = config.get("cluster.name");
    Settings settings = Settings.settingsBuilder().put("cluster.name", cluster).build();
    TransportClient c = TransportClient.builder().settings(settings).build();
    serverUrl = config.get("servers");
    LOG.info("{}.servers={}", cluster, serverUrl);
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

  @PreDestroy
  void destroy() {
    if (client != null)
      client.close();
  }

  public String getServerUrl() {
    return serverUrl;
  }

  public List<AccessBean> findRecentAccess(String range) {
    QueryBuilder excludeAjax = QueryBuilders.boolQuery()
                                            .must(QueryBuilders.termQuery("rpcId", "0"))
                                            .must(QueryBuilders.rangeQuery("stamp")
                                                               .timeZone("Asia/Chongqing")
                                                               .from("now-" + range)
                                                               .to("now"));
    SearchRequestBuilder srb = client.prepareSearch("trace")
                                     .setTypes("access")
                                     .setQuery(excludeAjax)
                                     .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                                     .addSort("stamp", SortOrder.DESC)
                                     .setTerminateAfter(1000)
                                     .setFrom(0)
                                     .setSize(500);
    SearchResponse res = srb.execute().actionGet(10, TimeUnit.SECONDS);
    List<AccessBean> items = Lists.newArrayList();
    for (SearchHit i : res.getHits()) {
      Map<String, Object> s = i.getSource();
      if (s != null) {
        items.add(parseAccess(s));
      }
    }
    Collections.sort(items, (o1, o2) -> (int) (o1.getStamp() - o2.getStamp()));
    return items;
  }


  public List<AccessBean> searchTraceId(String traceId) {
    SearchResponse res = client.prepareSearch("trace")
                               .setTypes("access")
                               .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                               .setFrom(0)
                               .setSize(1000)
                               .setQuery(QueryBuilders.termQuery("traceId", traceId))
                               .execute()
                               .actionGet(10, TimeUnit.SECONDS);
    List<AccessBean> items = Lists.newArrayList();
    for (SearchHit i : res.getHits()) {
      Map<String, Object> s = i.getSource();
      if (s != null) {
        items.add(parseAccess(s));
      }
    }
    Collections.sort(items, (o1, o2) -> (int) (o1.getStamp() - o2.getStamp()));
    return items;
  }

  public List<AccessBean> history(QueryBuilder q) {
    SearchResponse res = client.prepareSearch("trace")
                               .setTypes("access")
                               .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                               .setFrom(0)
                               .setSize(1000)
                               .setQuery(q)
                               .execute()
                               .actionGet(10, TimeUnit.SECONDS);
    List<AccessBean> items = Lists.newArrayList();
    for (SearchHit i : res.getHits()) {
      Map<String, Object> s = i.getSource();
      if (s != null) {
        items.add(parseAccess(s));
      }
    }
    Collections.sort(items, (o1, o2) -> (int) (o1.getStamp() - o2.getStamp()));
    return items;
  }

  public List<TraceContext> searchRpc(String traceId) {
    SearchResponse res = client.prepareSearch("trace")
                               .setTypes("rpc")
                               .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                               .setFrom(0)
                               .setSize(5000)
                               .setQuery(QueryBuilders.termQuery("traceId", traceId))
                               .execute()
                               .actionGet(10, TimeUnit.SECONDS);
    List<TraceContext> items = Lists.newArrayList();
    for (SearchHit i : res.getHits()) {
      Map<String, Object> s = i.getSource();
      if (s != null) {
        items.add(parseTrace(s));
      }
    }
    Collections.sort(items, (o1, o2) -> o1.getRpcId().compareTo(o2.getRpcId()));
    return items;
  }

  private AccessBean parseAccess(Map<String, Object> s) {
    AccessBean e = new AccessBean();
    e.setStamp(getLong(s, "stamp"));
    e.setTraceId(getStr(s, "traceId"));
    e.setRpcId(getStr(s, "rpcId"));
    e.setExtra(getStr(s, "extra"));
    e.setUrl(getStr(s, "url"));
    e.setClientIp(getStr(s, "clientIp"));
    e.setServerIp(getStr(s, "serverIp"));
    e.setCookie(getStr(s, "cookie"));
    e.setCost(getInt(s, "cost"));
    e.setReferer(getStr(s, "referer"));
    e.setCode(getInt(s, "code"));
    e.setSize(getInt(s, "size"));
    e.setUid(getStr(s, "uid"));
    e.setUserAgent(getStr(s, "userAgent"));
    return e;
  }

  private TraceContext parseTrace(Map<String, Object> s) {
    TraceContext e = new TraceContext();
    e.setStamp(getLong(s, "stamp"));
    e.setClientIp(getStr(s, "clientIp"));
    e.setClientName(getStr(s, "clientName"));
    e.setTraceId(getStr(s, "traceId"));
    e.setServerName(getStr(s, "serverName"));
    e.setCost(getInt(s, "cost"));
    e.setUrl(getStr(s, "url"));
    e.setParameter(getStr(s, "parameter"));
    e.setIface(getStr(s, "iface"));
    e.setMethod(getStr(s, "method"));
    e.setReason(getStr(s, "reason"));
    e.setExtra(getStr(s, "extra"));
    e.setUid(getStr(s, "uid"));
    e.setRpcId(getStr(s, "rpcId"));
    e.setSpider(getBool(s, "spider"));
    e.setFail(getBool(s, "fail"));
    return e;
  }

  private String getStr(Map<String, ?> m, String key) {
    return (String) m.get(key);
  }

  private int getInt(Map<String, ?> m, String key) {
    return m.containsKey(key) ? (Integer) m.get(key) : -1;
  }

  private long getLong(Map<String, ?> m, String key) {
    return m.containsKey(key) ? (Long) m.get(key) : -1L;
  }

  private boolean getBool(Map<String, ?> m, String key) {
    return m.containsKey(key) ? (Boolean) m.get(key) : false;
  }
}
