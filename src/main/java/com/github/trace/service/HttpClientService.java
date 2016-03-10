package com.github.trace.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.Mark;
import com.github.trace.intern.InnerUtil;
import com.google.common.collect.ImmutableList;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by hanmz on 2016/1/13.
 */
@Service
public class HttpClientService {
  private CloseableHttpClient httpClient;
  private Logger LOG = LoggerFactory.getLogger(HttpClientService.class);

  @PostConstruct
  void init() {
    httpClient = HttpClients.createDefault();
  }

  public String saveMark(String url, String json) {
    CloseableHttpResponse response = null;
    try {
      HttpPost post = new HttpPost(url);
      post.setHeader("Content-Type", "application/json");
      post.setHeader("Accept-Charset", "UTF-8");
      post.setHeader("charset", "UTF-8");
      post.setEntity(new StringEntity(json, "UTF-8"));
      response = httpClient.execute(post);
      if (response.getStatusLine().getStatusCode() == 200) {
        HttpEntity entity = response.getEntity();
        JSONObject jsonObject = parseJson(entity.getContent());
        int code = jsonObject.getInteger("code");
        if (code == 200) {
          return "OK";
        }
        return "code:" + code + " " + jsonObject.getString("message");
      } else {
        return "occur a error : code is " + response.getStatusLine().getStatusCode();
      }
    } catch (Exception e) {
      LOG.error("modify mark occur an exception ", e);
      return e.getMessage();
    } finally {
      InnerUtil.closeQuietly(response);
    }
  }

  public List<Mark> getAllMark(String url) {
    CloseableHttpResponse response = null;
    try {
      HttpGet get = new HttpGet(url);
      get.setHeader("Content-Type", "application/json");
      get.setHeader("Accept-Charset", "UTF-8");
      get.setHeader("charset", "UTF-8");
      response = httpClient.execute(get);
      StatusLine statusLine = response.getStatusLine();
      if (statusLine.getStatusCode() == 200) {
        HttpEntity entity = response.getEntity();
        JSONObject jsonObject = parseJson(entity.getContent());
        int code = jsonObject.getInteger("code");
        if (code == 200) {
          return JSON.parseArray(jsonObject.getString("items"), Mark.class);
        } else {
          LOG.info("get all mark return code:{} message:{}", code, jsonObject.getString("message"));
        }
      } else {
        LOG.info("http get request occur a error : code is " + statusLine.getStatusCode());
      }
    } catch (Exception e) {
      LOG.error("get all mark occur an exception ", e);
    } finally {
      InnerUtil.closeQuietly(response);
    }
    return ImmutableList.of();
  }

  private JSONObject parseJson(InputStream inputStream) throws IOException {
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
    BufferedReader reader = new BufferedReader(inputStreamReader);
    String s;
    StringBuilder result = new StringBuilder(1024);
    while (((s = reader.readLine()) != null)) {
      result.append(s);
    }
    InnerUtil.closeQuietly(reader);
    return JSON.parseObject(result.toString());
  }
}
