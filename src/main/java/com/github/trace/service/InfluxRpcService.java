package com.github.trace.service;

import com.github.trace.annotation.RpcTrace;
import com.github.trace.interceptor.IntervalInterceptor;
import org.influxdb.dto.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 从oss-rpc库中获取rpc调用统计
 * Created by lirui on 2015-12-10 22:59.
 */
@Service
@RpcTrace
public class InfluxRpcService extends InfluxDBService {
  @Override
  protected String getConfigName() {
    return "influxdb-rpc";
  }

  public List<Map> recentRpc(String start, String end, String name, String where) {
    String fmt = "select sum(total) as total, " +
      "sum(sumCost) as cost, " +
      "sum(slow) as slow, " +
      "sum(fail) as fail " +
            "from \"rpc.%s\" where %s %s %s fill(none)";
    String command = String.format(fmt, name, timeRange(start, end), where, IntervalInterceptor.getTimeInterval());
    return queryTable(command, "oss_rpc");
  }

  public List<Map> recentCaller(String start, String end) {
    String fmt =
      "select sum(total) as total,sum(fail) as fail,sum(sumCost) as cost,sum(slow) as slow from rpc where %s group by caller fill(none)";
    String command = String.format(fmt, timeRange(start, end));
    Query q = new Query(command, "oss_rpc_all");
    return queryCaller(q, "caller");
  }

  public List<Map> recentModuleByCaller(String start, String end, String caller) {
    String fmt =
      "select sum(total) as total,sum(fail) as fail,sum(sumCost) as cost,sum(slow) as slow from rpc where %s and caller = '%s' group by module fill(none)";
    String command = String.format(fmt, timeRange(start, end), caller);
    Query q = new Query(command, "oss_rpc_all");
    return queryCaller(q, "module");
  }

  public List<Map> recentCalled(String start, String end) {
    String fmt =
      "select sum(total) as total,sum(fail) as fail,sum(sumCost) as cost,sum(slow) as slow from rpc where %s group by \"server\" fill(none)";
    String command = String.format(fmt, timeRange(start, end));
    Query q = new Query(command, "oss_rpc_all");
    return queryCaller(q, "server");
  }

  public List<Map> recentModule(String start, String end) {
    String fmt =
      "select sum(total) as total,sum(fail) as fail,sum(sumCost) as cost,sum(slow) as slow from rpc where %s group by \"module\" fill(none)";
    String command = String.format(fmt, timeRange(start, end));
    Query q = new Query(command, "oss_rpc_all");
    return queryCallerAndFailNum(q, "module");
  }

  public List<Map> recentModuleByServer(String start, String end, String server) {
    String fmt =
      "select sum(total) as total,sum(fail) as fail,sum(sumCost) as cost,sum(slow) as slow from rpc where %s and \"server\" = '%s' group by module fill(none)";
    String command = String.format(fmt, range(start, end), server);
    Query q = new Query(command, "oss_rpc_all");
    return queryCaller(q, "module");
  }

  public List<Map> recentRpcCaller(String start, String end, String name, String where) {
    String fmt = "select sum(total) as total, " +
      "sum(sumCost) as cost, " +
      "sum(slow) as slow, " +
      "sum(fail) as fail " +
      "from \"rpc.%s\" where %s %s " +
      "group by caller fill(none)";
    String command = String.format(fmt, name, timeRange(start, end), where);
    Query q = new Query(command, "oss_rpc");
    return query(q, "caller");
  }

  public List<Map> recentRpcServer(String start, String end, String name, String where) {
    String fmt = "select sum(total) as total, " +
      "sum(sumCost) as cost, " +
      "sum(slow) as slow, " +
      "sum(fail) as fail " +
      "from \"rpc.%s\" where %s %s " +
      "group by \"server\" fill(none)";
    String command = String.format(fmt, name, timeRange(start, end), where);
    Query q = new Query(command, "oss_rpc");
    return query(q, "server");
  }
}
