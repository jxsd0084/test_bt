package com.github.trace.service;

import com.github.trace.annotation.RpcTrace;

import org.influxdb.dto.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangwj on 2016/1/28.
 */
@Service
@RpcTrace
public class InfluxMetricService extends InfluxDBService{

  @Override
  protected String getConfigName() {
    return "influxdb-metrics";
  }

  public List<Map> listServiceLog(String start, String end){
    String fmt =
        "select min(count) as minCount,max(count) as maxCount from \"logback.error\",\"logback.warn\" where %s group by app,ip";
    String command = String.format(fmt, timeRange(start, end));
    Query q = new Query(command, "oss_metrics");
    return queryLog(q,timeInterval(start,end));
  }

  public List<Map> recentLog(String start, String end, String name, String where) {
    String fmt = "select * " +
                 "from \""+name+"\" where %s %s fill(none)";
    String command = String.format(fmt, timeRange(start, end), where);
    return queryLogHistory(command, "oss_metrics");
  }

  public List<Map> recentLogMessage(String start, String end, String where) {
    String fmt = "select * " +
                 "from \"logback.exception\" where %s %s fill(none)";
    String command = String.format(fmt, timeRange(start, end), where);
    return queryLogMessages(command, "oss_metrics");
  }

}
