package com.github.trace.service;

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
//@Ignore
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

}