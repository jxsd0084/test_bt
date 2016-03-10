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
public class InfluxUriService extends InfluxDBService {
    @Override
    protected String getConfigName() {
        return "influxdb-uri";
    }

    public List<Map> recentPage(String start, String end) {
        String fmt =
                "select sum(totalPv) as total,sum(failPv) as fail,sum(spiderPv) as spider,sum(totalCost) as cost from page where %s group by \"app\",\"uri\" fill(none)";
        String command = String.format(fmt, timeRange(start, end));
        Query q = new Query(command, "oss_uri_all");
        return queryPages(q);
    }

    @SuppressWarnings("unchecked")
    public List<Map> recentPageByUri(String start, String end, String uri) {
        String fmt = "select sum(totalPv) as total,sum(failPv) as fail,sum(spiderPv) as spider," +
                "sum(totalCost) as cost,sum(pv20x) as pv20x, sum(pv30x) as pv30x, sum(pv40x) as pv40x,sum(pv50x) as pv50x " +
                "from \"page.%s\" where %s group by \"app\" fill(none)";
        String command = String.format(fmt, uri, timeRange(start, end));
        Query q = new Query(command, "oss_uri");
        List<Map> items = queryPages(q);
        items.forEach(i -> i.put("uri", uri));
        return items;
    }

    public List<Map> recentWeb(String start, String end) {
        String fmt =
                "select sum(pv) as total, sum(cost) as cost from web where %s group by \"actionid\" fill(none)";
        String command = String.format(fmt, timeRange(start, end));
        Query q = new Query(command, "oss_web");
        return queryWebs(q);
    }

    public List<Map> recentBrowser(String start, String end) {
        String fmt =
                "select sum(pv) as total, sum(cost) as cost from web where  %s group by \"browser\" fill(none)";
        String command = String.format(fmt, timeRange(start, end));
        Query q = new Query(command, "oss_web");
        return queryWebs(q);
    }

    public List<Map> recentActionId(String last, String day, String actionid, String browser) {
        last = checkTime(last);
        String fmt =
                "select sum(pv) as total, sum(cost) as cost from web where %s and actionid = '%s' and browser = '%s' %s fill(none)";
        String command = String.format(fmt, range(day, last), actionid, browser, IntervalInterceptor.getTimeInterval());
        Query q = new Query(command, "oss_web");
        return queryWebs(q);
    }

    public List<Map> recentActionIdByDomload(String last, String day, String actionid, String browser) {
        last = checkTime(last);
        String fmt =
                "select sum(pv) as total, sum(cost) as cost, sum(thead) as thead, sum(tload) as tload, sum(domready) as domready  from web where %s and actionid = '%s' and browser = '%s' %s fill(none)";
        String command = String.format(fmt, range(day, last), actionid, browser, IntervalInterceptor.getTimeInterval());
        Query q = new Query(command, "oss_web");
        return queryDomloads(q);
    }

    public List<Map> recentBrowser(String last, String day, String browser, String actionid) {
        last = checkTime(last);
        String fmt =
                "select sum(pv) as total, sum(cost) as cost, sum(thead) as thead, sum(tload) as tload, sum(domready) as domready  from web where %s and browser = '%s' and actionid = '%s' %s fill(none)";
        String command = String.format(fmt, range(day, last), browser, actionid, IntervalInterceptor.getTimeInterval());
        Query q = new Query(command, "oss_web");
        return queryDomloads(q);
    }

    public List<Map> recentUri(String start, String end, String uri, String app) {
        String fmt = "select sum(totalPv) as total,sum(failPv) as fail,sum(spiderPv) as spider," +
                "sum(totalCost) as cost,sum(pv20x) as pv20x, sum(pv30x) as pv30x, sum(pv40x) as pv40x,sum(pv50x) as pv50x " +
                "from \"page.%s\" where %s and app = '%s' %s fill(none)";
        String command = String.format(fmt, uri, timeRange(start, end), app, IntervalInterceptor.getTimeInterval());
        Query q = new Query(command, "oss_uri");
        return queryUris(q);
    }

    public List<Map> recentUri(String start, String end, String uri, String server, String app) {
        String fmt = "select sum(totalPv) as total,sum(failPv) as fail,sum(spiderPv) as spider," +
                "sum(totalCost) as cost,sum(pv20x) as pv20x, sum(pv30x) as pv30x, sum(pv40x) as pv40x,sum(pv50x) as pv50x " +
                "from \"page.%s\" where %s and \"server\" = '%s' and app = '%s' %s fill(none)";
        String command = String.format(fmt, uri, timeRange(start, end), server, app, IntervalInterceptor.getTimeInterval());
        Query q = new Query(command, "oss_uri");
        return queryUris(q);
    }

    public List<Map> recentUriByMachine(String start, String end, String uri, String app) {
        String fmt = "select sum(totalPv) as total,sum(failPv) as fail,sum(spiderPv) as spider," +
                "sum(totalCost) as cost,sum(pv20x) as pv20x, sum(pv30x) as pv30x, sum(pv40x) as pv40x,sum(pv50x) as pv50x " +
                "from \"page.%s\" where %s and app = '%s' group by \"server\" fill(none)";
        String command = String.format(fmt, uri, timeRange(start, end), app);
        Query q = new Query(command, "oss_uri");
        return queryUrisByMachine(q);
    }
}
