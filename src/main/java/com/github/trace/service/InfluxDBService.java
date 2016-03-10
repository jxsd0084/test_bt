package com.github.trace.service;

import com.github.autoconf.ConfigFactory;
import com.github.autoconf.api.IConfig;
import com.github.jedis.support.JedisCmd;
import com.github.trace.intern.DateUtil;
import com.github.trace.intern.InfluxCacheClient;
import com.github.trace.intern.InnerUtil;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.Reflection;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 访问InfluxDB服务
 * Created by lirui on 2015/09/09 下午6:01.
 */
abstract class InfluxDBService {
  @Resource(name = "redisCache")
  protected JedisCmd jedis;
  private Logger log = LoggerFactory.getLogger(InfluxDBService.class);
  private Pattern validTimePattern = Pattern.compile("\\d+(s|m|h|d|w|m)");
  private InfluxDB influxDB;
  private String serverUrl;
  @Autowired
  private MarkService markService;

  protected abstract String getConfigName();

  @PostConstruct
  void init() {
    ConfigFactory.getInstance().getConfig(getConfigName(), this::loadConfig);
  }

  private void loadConfig(IConfig config) {
    serverUrl = config.get("servers");
    String username = config.get("username");
    String password = config.get("password");
    InfluxDB delegate = InfluxDBFactory.connect("http://" + serverUrl, username, password);
    delegate.setConnectTimeout(5, TimeUnit.SECONDS);
    delegate.setReadTimeout(5, TimeUnit.MINUTES);
    influxDB = Reflection.newProxy(InfluxDB.class, new InfluxCacheClient(delegate, jedis));
  }

  public String getServerUrl() {
    return serverUrl;
  }

  public List<String> getTags(String measure, String key, String dbName) {
    String command = "SHOW TAG VALUES FROM " + measure + " WITH KEY = " + key;
    Query q = new Query(command, dbName);
    QueryResult result = influxDB.query(q);
    List<List<Object>> tagsList = result.getResults().get(0).getSeries().get(0).getValues();
    List<String> tags = Lists.newArrayList();
    if (tagsList != null) {
      for (List<Object> database : tagsList) {
        tags.add(database.get(0).toString());
      }
    }
    return tags;
  }

  protected List<Map> query(Query q, String name) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null) {
      return ImmutableList.of();
    }
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        String tag = j.getTags().get(name);
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          row.put("name", tag);
          row.put("desc", markService.comment(tag));
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("failNum", String.format("%.0f", (Double) row.get("fail")));
          row.put("cost", divide("cost", "total", row));
          row.put("slow", percent("slow", "total", row));
          row.put("fail", percent("fail", "total", row));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryWebs(Query q) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        Map<String, String> tags = j.getTags();
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          if (null != tags && tags.size() > 0) {
            for (Map.Entry<String, String> entry : tags.entrySet()) {
              row.put(entry.getKey(), entry.getValue());
            }
          }
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("avgCost", divide("cost", "total", row));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryCaller(Query q) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        Map<String, String> tags = j.getTags();
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          if (null != tags && tags.size() > 0) {
            for (Map.Entry<String, String> entry : tags.entrySet()) {
              row.put(entry.getKey(), entry.getValue());
            }
          }
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("cost", divide("cost", "total", row));
          row.put("slowPercent", percent("slow", "total", row));
          row.put("failPercent", percent("fail", "total", row));
          row.put("slow", String.format("%.0f", (Double) row.get("slow")));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }


  protected List<Map> queryCaller(Query q, String name) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        String tag = j.getTags().get(name);
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          row.put("name", tag);
          row.put("desc", markService.comment(tag));
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("cost", divide("cost", "total", row));
          row.put("slowPercent", percent("slow", "total", row));
          row.put("failPercent", percent("fail", "total", row));
          row.put("slow", String.format("%.0f", (Double) row.get("slow")));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryCallerAndFailNum(Query q, String name) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        String tag = j.getTags().get(name);
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          row.put("name", tag);
          row.put("desc", markService.comment(tag));
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("cost", divide("cost", "total", row));
          row.put("failNum", String.format("%.0f", (Double) row.get("fail")));
          row.put("slow", percent("slow", "total", row));
          row.put("fail", percent("fail", "total", row));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryServiceByModule(Query q) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        Map<String, String> tags = j.getTags();
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          if (null != tags && tags.size() > 0) {
            for (Map.Entry<String, String> entry : tags.entrySet()) {
              row.put(entry.getKey(), entry.getValue());
            }
          }
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryCallerByModule(Query q, String module, String name) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        String tag = j.getTags().get(name);
        String mtag = j.getTags().get(module);
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          row.put("name", tag);
          row.put("module", mtag);
          row.put("desc", markService.comment(tag));
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("cost", divide("cost", "total", row));
          row.put("slowPercent", percent("slow", "total", row));
          row.put("failPercent", percent("fail", "total", row));
          row.put("slow", String.format("%.0f", (Double) row.get("slow")));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryDomloads(Query q) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();

    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        Map<String, String> tags = j.getTags();

        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          if (null != tags && tags.size() > 0) {
            for (Map.Entry<String, String> entry : tags.entrySet()) {
              row.put(entry.getKey(), entry.getValue());
            }
          }
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("avgCost", divide("cost", "total", row));
          row.put("thead", divide("thead", "total", row));
          row.put("tload", divide("tload", "total", row));
          row.put("domready", divide("domready", "total", row));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }


  protected List<Map> queryPages(Query q) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();

    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        Map<String, String> tags = j.getTags();

        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          for (Map.Entry<String, String> entry : tags.entrySet()) {
            row.put(entry.getKey(), entry.getValue());
          }
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("failNum", String.format("%.0f", (Double) row.get("fail")));
          row.put("cost", divide("cost", "total", row));
          row.put("spider", percent("spider", "total", row));
          row.put("fail", percent("fail", "total", row));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryUris(Query q) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();

    if (results == null) {
      return ImmutableList.of();
    }
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("cost", divide("cost", "total", row));
          row.put("spider", percent("spider", "total", row));
          row.put("failNum", String.format("%.0f", (Double) row.get("fail")));
          row.put("fail", percent("fail", "total", row));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          row.put("pv20x", String.format("%.0f", (Double) row.get("pv20x")));
          row.put("pv30x", String.format("%.0f", (Double) row.get("pv30x")));
          row.put("pv40x", String.format("%.0f", (Double) row.get("pv40x")));
          row.put("pv50x", String.format("%.0f", (Double) row.get("pv50x")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryTable(String command, String table) {
    Query q = new Query(command, table);
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null) {
      return ImmutableList.of();
    }
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        if (result.getError() != null) {
          log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        }
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("cost", divide("cost", "total", row));
          row.put("slow", percent("slow", "total", row));
          row.put("failNum", String.format("%.0f", (Double) row.get("fail")));
          row.put("fail", percent("fail", "total", row));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryUrisByMachine(Query q) {
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();

    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        Map<String, String> tags = j.getTags();

        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          for (Map.Entry<String, String> entry : tags.entrySet()) {
            row.put(entry.getKey(), entry.getValue());
          }
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("cost", divide("cost", "total", row));
          row.put("spider", percent("spider", "total", row));
          row.put("failNum", String.format("%.0f", (Double) row.get("fail")));
          row.put("fail", percent("fail", "total", row));
          row.put("total", String.format("%.0f", (Double) row.get("total")));
          row.put("pv20x", String.format("%.0f", (Double) row.get("pv20x")));
          row.put("pv30x", String.format("%.0f", (Double) row.get("pv30x")));
          row.put("pv40x", String.format("%.0f", (Double) row.get("pv40x")));
          row.put("pv50x", String.format("%.0f", (Double) row.get("pv50x")));
          parseTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected QueryResult query(Query q) {
    return influxDB.query(q);
  }

  protected List<Map> queryLog(Query q, long minutes) {
    QueryResult result = query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null)
      return ImmutableList.of();
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        return ImmutableList.of();
      }
      Map<String, Map<String, Object>> map = new HashMap<>();
      for (QueryResult.Series j : series) {
        String name = j.getName();
        String appTag = j.getTags().get("app");
        if (appTag == null || appTag.equals(""))
          continue;
        String ipTag = j.getTags().get("ip");
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        String key = appTag + "/" + ipTag;
        if (!map.containsKey(key)) {
          Map<String, Object> newMap = Maps.newHashMap();
          map.put(key, newMap);
        }
        Map<String, Object> row = map.get(key);
        for (List<Object> k : values) {
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            if (names.get(idx).equals("time"))
              continue;
            if (name.equalsIgnoreCase("logback.error")) {
              row.put(names.get(idx) + "Error", convert(k.get(idx)));
            } else if (name.equalsIgnoreCase("logback.warn")) {
              row.put(names.get(idx) + "Warn", convert(k.get(idx)));
            }
          }
        }
      }
      for (Map.Entry<String, Map<String, Object>> m : map.entrySet()) {
        Map<String, Object> row = m.getValue();
        String[] keys = m.getKey().split("/");
        row.put("name", keys[0]);
        row.put("ip", keys[1]);
        Object minCountError = row.get("minCountError");
        Object maxCountError = row.get("maxCountError");
        String errorNum =
          (minCountError != null && maxCountError != null) ? minus("minCountError", "maxCountError", row) : "0";
        row.put("errorNum", errorNum);
        String errorRate = (minCountError != null && maxCountError != null) ?
          String.format("%.2f", ((Double) maxCountError - (Double) minCountError) / minutes) :
          "0";
        row.put("errorRate", errorRate);

        Object minCountWarn = row.get("minCountWarn");
        Object maxCountWarn = row.get("maxCountWarn");
        String warnNum =
          (minCountWarn != null && maxCountWarn != null) ? minus("minCountWarn", "maxCountWarn", row) : "0";
        row.put("warnNum", warnNum);

        String warnRate = (minCountWarn != null && maxCountWarn != null) ?
          String.format("%.2f", ((Double) maxCountWarn - (Double) minCountWarn) / minutes) :
          "0";
        row.put("warnRate", warnRate);
        ret.add(row);
      }

    }
    return ret;
  }

  protected List<Map> queryLogHistory(String command, String table) {
    Query q = new Query(command, table);
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null) {
      return ImmutableList.of();
    }
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        if (result.getError() != null) {
          log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        }
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("count", row.get("count"));
          row.put("m1_rate", String.format("%.2f", (Double) row.get("m1_rate")));
          row.put("m5_rate", String.format("%.2f", (Double) row.get("m5_rate")));
          row.put("m15_rate", String.format("%.2f", (Double) row.get("m15_rate")));
          parseLogTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }

  protected List<Map> queryLogMessages(String command, String table) {
    Query q = new Query(command, table);
    QueryResult result = influxDB.query(q);
    List<QueryResult.Result> results = result.getResults();
    if (results == null) {
      return ImmutableList.of();
    }
    List<Map> ret = Lists.newArrayList();
    for (QueryResult.Result i : results) {
      List<QueryResult.Series> series = i.getSeries();
      if (series == null) {
        if (result.getError() != null) {
          log.warn("reason:\t{},\t{}", result.getError(), q.getCommand());
        }
        return ImmutableList.of();
      }
      for (QueryResult.Series j : series) {
        List<String> names = j.getColumns();
        List<List<Object>> values = j.getValues();
        if (values == null) {
          continue;
        }
        for (List<Object> k : values) {
          Map<String, Object> row = Maps.newHashMap();
          for (int idx = 0, len = names.size(); idx < len; idx++) {
            row.put(names.get(idx), convert(k.get(idx)));
          }
          row.put("value", row.get("value"));
          parseLogTime(row);
          ret.add(row);
        }
      }
    }
    return ret;
  }


  // 将Integer或BigDecimal类型转换成 Double 类型
  private Object convert(Object o) {
    if (o instanceof Integer) {
      return ((Integer) o).doubleValue();
    }
    if (o instanceof Long) {
      return ((Long) o).doubleValue();
    }
    if (o instanceof BigDecimal) {
      return ((BigDecimal) o).doubleValue();
    }
    return o;
  }

  private String minus(String n1, String n2, Map m) {
    return String.format("%.1f", get(m, n2) - get(m, n1));
  }

  private String divide(String n1, String n2, Map m) {
    return String.format("%.2f", get(m, n1) / get(m, n2));
  }

  private String percent(String n1, String n2, Map m) {
    return String.format("%.2f", get(m, n1) * 100 / get(m, n2));
  }

  private Double get(Map m, String k) {
    Object obj = m.get(k);
    if (obj == null) {
      return 0.0;
    }
    if (obj instanceof Double) {
      return (Double) obj;
    } else if (obj instanceof Integer) {
      return ((Integer) obj).doubleValue();
    } else if (obj instanceof Long) {
      return ((Long) obj).doubleValue();
    } else if (obj instanceof Float) {
      return ((Float) obj).doubleValue();
    }
    throw new IllegalArgumentException(k + " type is not Number, real is:" + obj.getClass());
  }

  protected String checkTime(String last) {
    if (!validTimePattern.matcher(last).matches()) {
      throw new IllegalArgumentException("invalidTime: " + last);
    }
    return last;
  }

  private void parseTime(Map<String, Object> row) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    try {
      long step = InnerUtil.CHINA_OFFSET;
      long ms = sdf.parse((String) row.get("time")).getTime() + step;
      row.put("time", DateUtil.formatYmdHis(ms).substring(0, 16));
      row.put("stamp", ms + step); // 创建highcharts图表需要
    } catch (ParseException ignored) {
    }
  }

  private void parseLogTime(Map<String, Object> row) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    try {
      long step = InnerUtil.CHINA_OFFSET;
      long ms = sdf.parse((String) row.get("time")).getTime() + step;
      row.put("time", DateUtil.formatYmdHis(ms).substring(0, 16));
      row.put("stamp", ms + step); // 创建highcharts图表需要
    } catch (ParseException ignored) {
    }
  }

  protected String range(String day, String last) {
    if (!Strings.isNullOrEmpty(day)) {
      day = InnerUtil.format(day);
      String fmt = "time >= '%s' and time < '%s'";
      long offset = InnerUtil.CHINA_OFFSET;
      long midnight = DateUtil.getMidnight(InnerUtil.parse(day, System.currentTimeMillis())).getTimeInMillis() - offset;
      return String.format(fmt, DateUtil.formatYmdHis(midnight), DateUtil.formatYmdHis(midnight + InnerUtil.ONE_DAY));
    }
    return "time > now() - " + last;
  }

  protected String timeRange(String start, String end) {
    StringBuilder sb = new StringBuilder(40);
    sb.append("time >=");
    if (start.contains("now")) {
      sb.append(start).append(" and time<");
    } else {
      sb.append('\'')
        .append(DateUtil.formatYmdHis(Timestamp.valueOf(start).getTime() - InnerUtil.CHINA_OFFSET))
        .append('\'')
        .append(" and time<");
    }
    if (end.contains("now")) {
      sb.append(end);
    } else {
      sb.append('\'')
        .append(DateUtil.formatYmdHis(Timestamp.valueOf(end).getTime() - InnerUtil.CHINA_OFFSET))
        .append('\'');
    }
    return sb.toString();
  }

  protected long timeInterval(String start, String end) {
    long interval = Long.MIN_VALUE;
    if (end.contains("now")) {
      String time = start.split("-")[1];
      if (time.contains("h")) {
        interval = Long.valueOf(time.substring(0, time.length() - 1).trim()) * 60;
      } else if (time.contains("m")) {
        interval = Long.valueOf(time.substring(0, time.length() - 1).trim());
      } else if (time.contains("d")) {
        interval = Long.valueOf(time.substring(0, time.length() - 1).trim()) * 60 * 24;
      }
    } else {
      interval = (Timestamp.valueOf(end).getTime() - Timestamp.valueOf(start).getTime()) / 60000;
    }
    return interval;
  }
}
