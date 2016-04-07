package com.github.trace.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;

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
  public void search() throws Exception {
    SearchResponse response = esService.search("dcx.MonitorRequest", "ecf557a77013dafc53b6fd574a80fd7b",
                                               "stamp", 1459731600000L, 1459735200000L);

    SearchHit[] hits = response.getHits().getHits();
    for (SearchHit hit : hits) {
      Map<String, Object> source = hit.getSource();
      for (Map.Entry<String, Object> entry : source.entrySet()) {
        LOG.info(entry.getKey() + " : " + entry.getValue());
      }
      LOG.info("\n");
    }
  }
}