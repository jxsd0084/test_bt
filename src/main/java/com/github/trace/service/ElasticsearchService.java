package com.github.trace.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.utils.ElasticSearchHelper;
import com.github.trace.utils.OkHttpUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jetbrick.util.StringUtils;

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

  /**
   * 使用sql查询es
   * @param os   iPhone OS  or  Android
   * @param appVersion
   * @param from
   * @param to
   * @return List<Map<String, Object>>
   */
  public List<Map<String, Object>> searchBySqlForMonitorRequest(String os, String appVersion, String item,
                                                                long from, long to) {
    return searchBySqlForMonitorRequest(os, appVersion, item, from, to, -1);
  }

  /**
   * 使用sql查询es
   * @param os   iPhone OS  or  Android
   * @param appVersion
   * @param from
   * @param to
   * @param limit
   * @return List<Map<String, Object>>
   */
  public List<Map<String, Object>> searchBySqlForMonitorRequest(String os, String appVersion, String item,
                                                                long from, long to, int limit) {
    String sql = sqlBuilderForMonitorRequest(os, appVersion, item, from, to, limit);
    return sqlSearch(sql);
  }

  public List<Map<String, Object>> searchBySqlForOthers(String topic, String item,
                                                        long from, long to) {
    return searchBySqlForOthers(topic, item, from, to, -1);
  }

  public List<Map<String, Object>> searchBySqlForOthers(String topic, String item,
                                                        long from, long to, int limit) {
    String sql = sqlBuilderForOthers(topic, item, from, to, limit);
    return sqlSearch(sql);
  }

  public Response searchBySql(String sql) throws IOException {
    String baseUrl = ElasticSearchHelper.getSqlUrl();
    String fullUrl = baseUrl + "?sql=" + StringUtils.trim(sql);

    Request request = new Request.Builder().url(fullUrl).build();
    return OkHttpUtil.execute(request);
  }

  private List<Map<String, Object>> sqlSearch(String sql) {
    List<Map<String, Object>> list = Lists.newLinkedList();
    try {
      Response response = searchBySql(sql);
      if (!response.isSuccessful()) {
        return list;
      }
      String ss = response.body().string();
      JSONObject object = JSON.parseObject(ss);
      JSONObject aggs = object.getJSONObject("aggregations");
      Set<String> aggKeySet = aggs.keySet();
      for (String aggKey : aggKeySet) {
        JSONObject agg = aggs.getJSONObject(aggKey);
        JSONArray buckets = agg.getJSONArray("buckets");
        for (int i = 0; i < buckets.size(); i++) {
          JSONObject b = buckets.getJSONObject(i);
          String value = b.getString("key");
          Map<String, Object> map = Maps.newLinkedHashMap();
          int totalCount = b.getInteger("doc_count");
          map.put("value", value);
          map.put("totalCount", totalCount);
          map.put("successCount", totalCount);
          map.put("failCount", 0);
          list.add(map);
        }
      }
    } catch (Exception e) {
      LOG.error("Cannot search by sql from elasticsearch", e);
    }
    return list;
  }

  private String sqlBuilderForMonitorRequest(String os, String appVersion, String item,
                                             long from, long to, int limit) {
    StringBuilder sql = new StringBuilder();
    sql.append(" SELECT ").append(item).append(", count(*) as count1 from datapt-buriedtool ")
        .append(" where _type = 'dcx.MonitorRequest' ");
    sql.append(" and M98 >= ").append(from).append(" and M98 <= ").append(to).append(" ");
    if (!Strings.isNullOrEmpty(os)) {
      sql.append(" and M4 = '").append(os.trim()).append("' ");
    }
    if (!Strings.isNullOrEmpty(appVersion)) {
      sql.append(" and M6 = '").append(appVersion.trim()).append("' ");
    }
    sql.append(" group by ").append(item).append(" order by count1 desc ");
    sql.append(" limit ").append(limit > 0 ? limit : 10000);
    LOG.debug(sql.toString());
    return sql.toString();
  }

  private String sqlBuilderForOthers(String topic, String item, long from, long to, int limit) {
    StringBuilder sql1 = new StringBuilder();
    sql1.append(" SELECT ").append(item).append(", count(*) as count1 from datapt-buriedtool ")
        .append(" where _type = '").append(topic).append("' ")
        .append(" and stamp >= ").append(from).append(" and stamp <= ").append(to).append(" ")
        .append(" group by ").append(item).append(" order by count1 desc ")
        .append(" limit ").append(limit > 0 ? limit : 10000);
    LOG.debug(sql1.toString());
    return sql1.toString();
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
