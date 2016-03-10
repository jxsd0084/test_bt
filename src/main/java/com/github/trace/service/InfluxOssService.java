package com.github.trace.service;

import com.github.trace.annotation.RpcTrace;
import com.github.trace.interceptor.IntervalInterceptor;
import org.influxdb.dto.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 从oss-service库中获取服务层统计
 * Created by lirui on 2015-12-10 22:59.
 */
@Service
@RpcTrace
public class InfluxOssService extends InfluxDBService {
  @Override
  protected String getConfigName() {
    return "influxdb-service";
  }

  // 日报服务
  public List<Map> dailyService(String last, String day) {
    last = checkTime(last);
    String fmt = "select sum(total) as total, " +
      "sum(sumCost) as cost, " +
      "sum(slow) as slow, " +
      "sum(fail) as fail " +
      "from service where %s" +
      "group by \"module\" fill(none)";
    String command = String.format(fmt, range(day, last));
    Query q = new Query(command, "oss_service_all");
    return query(q, "module");
  }

  public List<Map> recentService(String start, String end, String name, String where) {
    String fmt = "select sum(total) as total, " +
      "sum(sumCost) as cost, " +
      "sum(slow) as slow, " +
      "sum(fail) as fail " +
      "from \"service.%s\" where %s %s %s fill(none)";
    String command = String.format(fmt, name, timeRange(start, end), where, IntervalInterceptor.getTimeInterval());
    return queryTable(command, "oss_service");
  }

  public List<Map> recentMethod(String start, String end) {
    String fmt =
      "select sum(total) as total,sum(fail) as fail,sum(sumCost) as cost,sum(slow) as slow from service where %s group by \"module\", method fill(none)";
    String command = String.format(fmt, timeRange(start, end));
    Query q = new Query(command, "oss_service_all");
    return queryCallerByModule(q, "module", "method");
  }

  public List<Map> recentMethod(String start, String end, String name, String where) {
    String fmt = "select sum(total) as total, " +
      "sum(sumCost) as cost, " +
      "sum(slow) as slow, " +
      "sum(fail) as fail " +
      "from \"service.%s\" where %s %s " +
      "group by \"method\" fill(none)";
    String command = String.format(fmt, name, timeRange(start, end), where);
    Query q = new Query(command, "oss_service");
    return query(q, "method");
  }

  public List<Map> recentServiceCaller(String start, String end, String name, String where) {
    String fmt = "select sum(total) as total, " +
      "sum(sumCost) as cost, " +
      "sum(slow) as slow, " +
      "sum(fail) as fail " +
      "from \"service.%s\" where %s %s " +
      "group by \"caller\" fill(none)";
    String command = String.format(fmt, name, timeRange(start, end), where);
    Query q = new Query(command, "oss_service");
    return query(q, "caller");
  }
}
