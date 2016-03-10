package com.github.trace.web;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import com.github.trace.TraceContext;
import com.github.trace.bean.AccessBean;
import com.github.trace.intern.EChartUtil;
import com.github.trace.service.ElasticSearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 获取trace信息并展示
 */
@Controller
public class TraceController {
  private static final Logger LOG = LoggerFactory.getLogger(TraceController.class);
  @Autowired
  private ElasticSearch elasticSearch;

  @RequestMapping("/trace")
  public String links(@RequestParam(required = false) String traceId, Model model) {
    if (!Strings.isNullOrEmpty(traceId)) {
      model.addAttribute("traceId", traceId);
      List<AccessBean> access = ImmutableList.of();
      try {
        access = elasticSearch.searchTraceId(traceId);
      } catch (Exception e) {
        model.addAttribute("message", "在ElasticSearch中找不到TraceID为 " + traceId + " 的记录");
        LOG.error("cannot find {}", traceId, e);
      }
      model.addAttribute("access", access);
      List<TraceContext> rpc = null;
      try {
        rpc = elasticSearch.searchRpc(traceId);
      } catch (Exception e) {
        model.addAttribute("message", "在ElasticSearch中找不到TraceID为" + traceId + "对应的RPC纪录");
        LOG.error("cannot searchRpc({})", traceId, e);
      }
      model.addAttribute("rpc", rpc);
    }
    return "trace/trace";
  }

  @RequestMapping("/history")
  public String home(@RequestParam(defaultValue = "1h") String range, Model model) {
    List<AccessBean> accesses;
    try {
      accesses = elasticSearch.findRecentAccess(range);
    } catch (Throwable e) {
      model.addAttribute("message", "在Last " + range + "时间内找不到对应的纪录");
      LOG.error("cannot findRecent('{}')", range, e);
      accesses = ImmutableList.of();
    }
    model.addAttribute("accesses", accesses);
    model.addAttribute("range", range);
    model.addAttribute("times", ImmutableList.of("1h", "6h", "12h", "1d", "3d", "7d"));
    return "trace/history";
  }

  @RequestMapping("/topochart")
  public String rpcTopo(@RequestParam(required = false) String traceId, Model model){
    if (!Strings.isNullOrEmpty(traceId)) {
      model.addAttribute("traceId", traceId);
      List<AccessBean> access = ImmutableList.of();
      try {
        access = elasticSearch.searchTraceId(traceId);
      } catch (Exception e) {
        model.addAttribute("message", "在ElasticSearch中找不到TraceID为 " + traceId + " 的记录");
        LOG.error("cannot find {}", traceId, e);
      }
      model.addAttribute("access", access);
      List<TraceContext> rpc = null;
      try {
        rpc = elasticSearch.searchRpc(traceId);
      } catch (Exception e) {
        model.addAttribute("message", "在ElasticSearch中找不到TraceID为" + traceId + "对应的RPC纪录");
        LOG.error("cannot searchRpc({})", traceId, e);
      }
      model.addAttribute("treemapdata", EChartUtil.toTreeMapData(rpc));
    }
    return "trace/topochart";
  }
}
