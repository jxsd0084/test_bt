package com.github.trace.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.Mark;
import com.google.common.collect.ImmutableList;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 从运维调用接口获取信息
 * Created by hanmz on 2016/1/13.
 */
@Service
public class OkHttpService {
  private final OkHttpClient CLIENT = new OkHttpClient();
  private final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
  private Logger LOG = LoggerFactory.getLogger(OkHttpService.class);

  /**
   * 获取全部备注信息
   */
  public List<Mark> getAllMark(String url) {
    Request request = new Request.Builder().url(url).build();
    try {
      Response response = CLIENT.newCall(request).execute();
      if (response.isSuccessful()) {
        JSONObject jsonObject = JSON.parseObject(response.body().string());
        int code = jsonObject.getInteger("code");
        if (code == 200) {
          return JSON.parseArray(jsonObject.getString("items"), Mark.class);
        } else {
          LOG.info("get all mark return code:{} message:{}", code, jsonObject.getString("message"));
        }
      } else {
        LOG.info("http get request occur a error : code is " + response.code());
      }
    } catch (IOException e) {
      LOG.error("get all mark occur an exception ", e);
    }
    return ImmutableList.of();
  }

  /**
   * 修改备注信息
   */
  public String saveMark(String url, String json) {
    RequestBody body = RequestBody.create(JSON_TYPE, json);
    Request request = new Request.Builder().url(url).post(body).build();
    try {
      Response response = CLIENT.newCall(request).execute();
      if (response.isSuccessful()) {
        JSONObject jsonObject = JSON.parseObject(response.body().string());
        int code = jsonObject.getInteger("code");
        if (code == 200) {
          return "OK";
        }
        return "code:" + code + " " + jsonObject.getString("message");
      } else {
        return "occur a error : code is " + response.code();
      }
    } catch (IOException e) {
      LOG.error("modify mark occur an exception ", e);
      return e.getMessage();
    }
  }
}
