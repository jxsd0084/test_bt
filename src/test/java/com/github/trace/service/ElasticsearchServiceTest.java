package com.github.trace.service;

import com.squareup.okhttp.Response;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * ElasticsearchServiceTest
 * Created by wzk on 16/4/7.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ElasticsearchServiceTest {
  private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchServiceTest.class);

  @Autowired
  private ElasticsearchService esService;

  @Test
  public void testSearch() throws Exception {
    List<Map<String, Object>> results = esService.search("dcx.MonitorRequest", "*", "stamp",
                     System.currentTimeMillis(), System.currentTimeMillis() - 24 * 3600 * 1000L);

    for (Map<String, Object> source : results) {
      for (Map.Entry<String, Object> entry : source.entrySet()) {
        LOG.info(entry.getKey() + " : " + entry.getValue());
      }
      LOG.info("\n");
    }
  }

  @Test
  public void testSearchWithSize() throws Exception {
    List<Map<String, Object>> results = esService.search("dcx.MonitorRequest", "*", "stamp",
           System.currentTimeMillis(), System.currentTimeMillis() - 24 * 3600 * 1000L, 1, 2);

    for (Map<String, Object> source : results) {
      for (Map.Entry<String, Object> entry : source.entrySet()) {
        LOG.info(entry.getKey() + " : " + entry.getValue());
      }
      LOG.info("\n");
    }
  }

  @Test
  public void testAggregation() throws Exception {
    List<Map<String, Object>> list = esService.aggregation("iOS", "M99.M1", 0, System.currentTimeMillis());
    LOG.info(list.toString());
  }

  @Test
  public void testSearchBySql() throws Exception {
    String sql = "SELECT M6, count(*) from datapt-buriedtool group by M6 limit 10";
    Response response = esService.searchBySql(sql);
    String ss = response.body().string();
    LOG.info(ss);
  }

  @Test
  public void testSearchBySql2() throws Exception {
    List<Map<String, Object>> list = esService.searchBySql("iPhone OS", "5.3", 1462498860172L, 1462499860172L);
    LOG.info(list.toString());
  }
}