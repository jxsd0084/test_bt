package com.github.trace.utils;

import com.github.trace.service.ElasticsearchService;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * ElasticSearchHelper Test
 * Created by wzk on 16/4/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ElasticSearchHelperTest {

  private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchHelperTest.class);

  @Autowired
  ElasticsearchService esService;

  @Test
  public void testSearch() throws Exception {

    SearchResponse response = esService.search("dcx.MonitorRequest", "867389026965532", "M98", 1459853700000L, 1459853760000L);

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