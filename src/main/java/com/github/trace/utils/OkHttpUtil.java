package com.github.trace.utils;

/**
 * OkHttpUtil Created by wzk on 16/5/6.
 */

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkHttpUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpUtil.class);
  private static final String CHARSET_NAME = "UTF-8";
  private static final OkHttpClient mOkHttpClient = new OkHttpClient();

  static {
    mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
    mOkHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
  }

  /**
   * 不会开启异步线程。
   * @param request
   * @return
   * @throws IOException
   */
  public static Response execute(Request request) throws IOException {
    return mOkHttpClient.newCall(request).execute();
  }

  /**
   * 开启异步线程访问网络
   * @param request
   * @param responseCallback
   */
  public static void enqueue(Request request, Callback responseCallback) {
    mOkHttpClient.newCall(request).enqueue(responseCallback);
  }

  /**
   * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
   * @param request
   */
  public static void enqueue(Request request) {
    mOkHttpClient.newCall(request).enqueue(new Callback() {
      @Override
      public void onResponse(Response arg0) throws IOException {

      }
      @Override
      public void onFailure(Request arg0, IOException arg1) {

      }
    });
  }

  public static String getStringFromServer(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();
    Response response = execute(request);
    if (response.isSuccessful()) {
      return response.body().string();
    } else {
      throw new IOException("Unexpected code " + response);
    }
  }

  /**
   * 这里使用了HttpClinet的API。只是为了方便
   * @param params
   * @return
   */
  public static String formatParams(List<BasicNameValuePair> params) {
    return URLEncodedUtils.format(params, CHARSET_NAME);
  }

  /**
   * 为HttpGet 的 url 方便的添加1个name value 参数。
   * @param url
   * @param name
   * @param value
   * @return
   */
  public static String attachHttpGetParam(String url, String name, String value) {
    return url + "?" + name + "=" + value;
  }

  /**
   * 为HttpGet 的 url 方便的添加多个name value 参数。
   * @param url
   * @param params
   * @return
   */
  public static String attachHttpGetParams(String url, List<BasicNameValuePair> params) {
    return url + "?" + formatParams(params);
  }

  public static HttpEntity postData(HttpPost httpPost,List<NameValuePair> formParams) {
    HttpClient httpclient = new DefaultHttpClient();
    HttpEntity entity = null;
    UrlEncodedFormEntity uefEntity=null;
    try {
      uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
      httpPost.setEntity(uefEntity);
      HttpResponse response = httpclient.execute(httpPost);
      entity = response.getEntity();
    } catch (Exception e) {
      LOGGER.error("访问出错：" + e);
    }
    return entity;
  }

  public static HttpEntity getData(String url) {
    HttpClient httpclient = new DefaultHttpClient();
    HttpEntity entity = null;
    HttpGet request = new HttpGet();
    try {
      request.setURI(new URI(url));
      HttpResponse response = httpclient.execute(request);
      entity = response.getEntity();
    } catch (Exception e) {
      LOGGER.error("访问出错：" + e);
    }
    return entity;
  }
}
