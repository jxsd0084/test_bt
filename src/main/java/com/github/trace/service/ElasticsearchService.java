package com.github.trace.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import com.github.trace.utils.ElasticSearchHelper;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 处理es相关
 * Created by wzk on 16/4/5.
 */
@Service
public class ElasticsearchService {

  private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchService.class);
  private static final String INDEX = "datapt-buriedtool";

  public List<Map<String, Object>> search(String topic, String keyword, String timeParamName, long from, long to) {
    SearchRequestBuilder builder = build(topic, keyword, timeParamName, from, to);
    SearchResponse response = ElasticSearchHelper.search(builder);

    List<Map<String, Object>> results = Lists.newArrayList();

    if (response != null && response.getHits() != null) {
      SearchHit[] hits = response.getHits().getHits();
      for (SearchHit hit : hits) {
        results.add(hit.getSource());
      }
    }
    LOG.info("Got {} hits for keyword [{}] in topic [{}]", results.size(), keyword, topic);
    return results;
  }

  /**
   * 必须同时满足keyword和时间范围
   * @param topic    topic, 即es中的type
   * @param keyword  关键词
   * @param param    使用的时间字段
   * @param from     起始时间戳
   * @param to       截止时间戳
   * @return  {@link SearchRequestBuilder}
   */
  private SearchRequestBuilder build(String topic, String keyword,
                                     String param, long from, long to) {
    SearchRequestBuilder builder = ElasticSearchHelper.newBuilder(INDEX);
    if (!Strings.isNullOrEmpty(topic)) {
      builder.setTypes(topic);
    }

    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    boolQuery.must(QueryBuilders.queryStringQuery(keyword));

    if (!Strings.isNullOrEmpty(param) && from > 0 && to > from) {
      RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(param);
      rangeQuery.gte(from).lte(to);
      boolQuery.must(rangeQuery);
    }

    builder.setQuery(boolQuery);
    return builder;
  }

}
