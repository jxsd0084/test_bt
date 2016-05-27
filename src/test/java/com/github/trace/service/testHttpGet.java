package com.github.trace.service;

import com.github.autoconf.helper.ConfigHelper;
import com.github.trace.utils.OkHttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghl on 2016/5/19.
 */
public class testHttpGet {
    @Test
    public void testGet() throws IOException, URISyntaxException {
        String url = "http://cmdb.foneshare.cn/index.php?r=user/json-header-user-id&q=whl";
        HttpClient client = new DefaultHttpClient();
        // 实例化HTTP方法
        HttpGet request = new HttpGet();
        request.setURI(new URI(url));
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity,"UTF-8"));

    }

    @Test
    public void testPost() throws IOException, URISyntaxException {
        String url = ConfigHelper.getApplicationConfig().get("qinxin.message.url");
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
//        formParams.add(new BasicNameValuePair("title", "测试"));
//        formParams.add(new BasicNameValuePair("content", "这是一个测试"));
//        formParams.add(new BasicNameValuePair("ids", "-1"));
//        formParams.add(new BasicNameValuePair("names", "-1"));
//        formParams.add(new BasicNameValuePair("id", "201"));

        HttpEntity entity = OkHttpUtil.postData(httpPost,formParams);
        String result = null;
        try {
            if(entity!=null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
           System.out.println("获取entity数据出错："+e);
        }
       System.out.println("返回结果:"+result);

    }

    public void test(){

    }
}
