package com.github.trace.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.github.trace.utils.ElasticSearchHelper;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
  private static final String LOG_STATISTIC_INDEX = "datapt-logstatistic";


  /**
   * 必须同时满足keyword和时间范围
   * @param topic        topic, 即es中的type
   * @param keyword      关键词
   * @param timeParam    使用的时间字段
   * @param timeFrom     起始时间戳
   * @param timeTo       截止时间戳
   * @param from         item起始
   * @param size         item条数
   * @return  结果的list
   */
  public List<Map<String, Object>> search(String topic, String keyword, String timeParam,
                                          long timeFrom, long timeTo, int from, int size) {
    List<Map<String, Object>> results = Lists.newArrayList();
    SearchResponse response;
    try {
      SearchRequestBuilder builder = build(topic, keyword, timeParam, timeFrom, timeTo, from, size);
      response = ElasticSearchHelper.search(builder);
    } catch (Exception e) {
      LOG.error("Cannot search for keyword {}", keyword, e);
      return results;
    }
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
   * 必须同时满足keyword和时间范围, 默认返回10条
   * @param topic        topic, 即es中的type
   * @param keyword      关键词
   * @param timeParam    使用的时间字段
   * @param timeFrom     起始时间戳
   * @param timeTo       截止时间戳
   */
  public List<Map<String, Object>> search(String topic, String keyword, String timeParam,
                                          long timeFrom, long timeTo) {
    return search(topic, keyword, timeParam, timeFrom, timeTo, 0, 10);
  }

  public List<Map<String, Object>> aggregation(String nav, String key, long timeFrom, long timeTo) {
    Preconditions.checkNotNull(key, "Key should not be empty!");

    List<Map<String, Object>> aggList = Lists.newLinkedList();

    // 聚合前进行查询, 先过滤出需要的内容
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    // 时间范围
    RangeQueryBuilder timeRangeBuilder = QueryBuilders.rangeQuery("time").lte(timeTo).gte(timeFrom);
    boolQuery.filter(timeRangeBuilder);
    // 必须匹配nav和key
    if (! Strings.isNullOrEmpty(nav)) {
      boolQuery.must(QueryBuilders.termQuery("nav", nav));
    }
    boolQuery.must(QueryBuilders.termQuery("key", key));

    SumBuilder totalCountAgg = AggregationBuilders.sum("totalCount").field("totalCount");
    SumBuilder successCountAgg = AggregationBuilders.sum("successCount").field("successCount");
    SumBuilder failCountAgg = AggregationBuilders.sum("failCount").field("failCount");

    TermsBuilder aggBuilder = AggregationBuilders
        .terms("stat")
        .field("value")
        .size(0)
        .order(Terms.Order.aggregation("totalCount", false))
        .subAggregation(totalCountAgg)
        .subAggregation(successCountAgg)
        .subAggregation(failCountAgg);

    SearchRequestBuilder builder = ElasticSearchHelper.newBuilder(LOG_STATISTIC_INDEX);
    SearchResponse response;
    try {
      response = builder.setQuery(boolQuery)
          .addAggregation(aggBuilder)
          .get();
    } catch (Exception e) {
      LOG.error("Cannot get aggregations for nav {} and key {}", nav, key, e);
      return aggList;
    }
    if (response != null) {
      Terms agg = response.getAggregations().get("stat");
      for (Terms.Bucket bucket : agg.getBuckets()) {
        Map<String, Object> fields = Maps.newHashMap();
        String aggKey = bucket.getKeyAsString();
        fields.put("value", aggKey);
        getBucketSubAggregationValue(bucket, fields);

        aggList.add(fields);
      }
    }

    return aggList;
  }

  private void getBucketSubAggregationValue(Terms.Bucket bucket, Map<String, Object> fields) {
    Preconditions.checkNotNull(bucket, "Bucket should not be null.");
    List<Aggregation> aggregations = bucket.getAggregations().asList();

    if (aggregations == null || aggregations.isEmpty()) {
      return;
    }
    aggregations.stream()
        .filter(agg -> agg instanceof InternalSum)
        .forEach(agg -> {
          String key = agg.getName();
          double value = ((InternalSum) agg).getValue();
          fields.put(key, value);
        });
  }

  /**
   * 必须同时满足keyword和时间范围
   * @param topic        topic, 即es中的type
   * @param keyword      关键词
   * @param timeParam    使用的时间字段
   * @param timeFrom     起始时间戳
   * @param timeTo       截止时间戳
   * @param from         item起始
   * @param size         item条数
   * @return  {@link SearchRequestBuilder}
   */
  private SearchRequestBuilder build(String topic, String keyword, String timeParam, long timeFrom,
                                     long timeTo, int from, int size) {
    SearchRequestBuilder builder = ElasticSearchHelper.newBuilder(INDEX);
    if (!Strings.isNullOrEmpty(topic)) {
      builder.setTypes(topic);
    }

    builder.addSort(timeParam, SortOrder.DESC);

    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    boolQuery.must(QueryBuilders.queryStringQuery(keyword));

    if (!Strings.isNullOrEmpty(timeParam) && timeFrom > 0 && timeTo >= timeFrom) {
      RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(timeParam);
      rangeQuery.gte(timeFrom).lte(timeTo);
      boolQuery.must(rangeQuery);
    }

    builder.setFrom(from);
    builder.setSize(size);

    builder.setQuery(boolQuery);
    return builder;
  }

}
