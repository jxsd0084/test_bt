package com.github.trace.service;

import com.github.autoconf.helper.ConfigHelper;
import com.github.trace.entity.NavigationItem;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.mapper.NavigationItem0Mapper;

import com.github.trace.utils.OkHttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghl on 2016/3/31.
 */
@Service
public class Navigation0Service {
  private final static Logger LOGGER = LoggerFactory.getLogger(Navigation0Service.class);
  @Autowired
  private NavigationItem0Mapper navigationItem0Mapper;
  @Autowired
  private KafkaService kafkaService;
  private final static String ENVIRONMENT =  ConfigHelper.getApplicationConfig().get("process.profile");
  private final static String TOPIC_ERROR = "kafka消费主题无效或数据无日志";

  private final static String LOG_ERROR = "数据异常（长时间无数据访问)";

  /**
   * 获取所有节点
   */
  public List<NavigationItem0> queryAll() {
    return navigationItem0Mapper.findAll();
  }

  /**
   * 创建导航条目
   */
  public int insert(NavigationItem0 navigationItem0) {
    return navigationItem0Mapper.insert(navigationItem0);
  }

  /**
   * 修改导航条目
   */
  public int modify(NavigationItem0 navigationItem0) {
    return navigationItem0Mapper.update(navigationItem0);
  }

  /**
   * 删除导航条目
   */
  public int remove(int id) {
    return navigationItem0Mapper.deleteById(id);
  }

  /**
   * 查询单条导航栏项目
   */
  public NavigationItem0 queryById(int id) {
    return navigationItem0Mapper.findById(id);
  }

  /**
   * 查询单条导航栏项目
   */
  public List<NavigationItem0> queryByParentId(int parentId) {
    return navigationItem0Mapper.findByParentId(parentId);
  }

  /**
   * 根据name查询导航栏项目
   * @param name
   * @return
   */
  public NavigationItem0 queryByName(String name) {
    return navigationItem0Mapper.findByName(name);
  }

  public String getUserInfo(String username) {
    String url = ConfigHelper.getApplicationConfig().get("qinxin.user.url");
    url = url + "?r=user/json-header-user-id&q="+username;

    HttpEntity entity = OkHttpUtil.getData(url);
    String result = null;
    try {
      if(entity!=null) {
        result = EntityUtils.toString(entity, "UTF-8");
      }
    } catch (Exception e) {
      LOGGER.error("获取entity数据出错："+e);
    }

    return result;
  }

  public void checkDataSource(long compareTime){
    boolean result = false;
    String message = "";
    List<NavigationItem0> navigationItem0List = navigationItem0Mapper.findByType(1);
    if(navigationItem0List!=null){
      long nowTime = System.currentTimeMillis();
      for(NavigationItem0 navigationItem0:navigationItem0List){
         long lastTime = kafkaService.getLastMessageTimestamp(navigationItem0.getTopic());
        LOGGER.debug("测试数据，" + navigationItem0.getName() +"," +navigationItem0.getTopic()+"," +lastTime);
         if(lastTime==0) {
            message = ENVIRONMENT +"环境-" + navigationItem0.getName() + ":" +LOG_ERROR;
            sendWarnMessage(navigationItem0.getName(),navigationItem0.getManager(),navigationItem0.getManager(),message);
         }else {
             long timeInterval = nowTime - lastTime;
             if (timeInterval >= compareTime) {
                message = ENVIRONMENT +"环境-" + navigationItem0.getName() +":" + LOG_ERROR;
                sendWarnMessage(navigationItem0.getName(),navigationItem0.getManager(),navigationItem0.getManageId(),message);
             }
         }
      }
    }
  }

  public void sendWarnMessage(String title,String names,String ids,String content){
    String status = "告警失败";
    String url = ConfigHelper.getApplicationConfig().get("qinxin.message.url");
    HttpPost httpPost = new HttpPost(url);
    List<NameValuePair> formParams = new ArrayList<NameValuePair>();
    formParams.add(new BasicNameValuePair("title", title));
    formParams.add(new BasicNameValuePair("content", content));
    formParams.add(new BasicNameValuePair("ids", "3719;"+ids));
    formParams.add(new BasicNameValuePair("names", "武靖;"+names));
    formParams.add(new BasicNameValuePair("id", "201"));

    HttpEntity entity = OkHttpUtil.postData(httpPost,formParams);
    String result = "";
    try {
      if(entity!=null) {
        result = EntityUtils.toString(entity, "UTF-8");
      }
    } catch (Exception e) {
      LOGGER.error("获取entity数据出错："+e);
    }
    if(result.contains("ok")){
       status = "告警成功";
    }
  }

}
