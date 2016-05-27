package com.github.trace.service;

import com.github.autoconf.ConfigFactory;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import com.github.autoconf.admin.ConfigAdminClient;
import com.github.autoconf.admin.api.IConfigAdmin;
import com.github.autoconf.helper.ConfigHelper;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.mapper.NavigationItem0Mapper;
import com.github.trace.utils.OkHttpUtil;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Navigation0Service
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
  private final static String LOG_ERROR = "数据异常（30分钟内无数据访问)";
  private static HierarchicalINIConfiguration iniConfiguration = new HierarchicalINIConfiguration();

  private static final String CONFIG_TOKEN = "E5C7079581FC27BE39CE191A1A252C20";
  private static final String KAFKA2ES_TOPIC_CONFIG = "buriedtool-kafka2es-topic";

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
    int ret = navigationItem0Mapper.insert(navigationItem0);
    updateConfig();
    return ret;
  }

  /**
   * 修改导航条目
   */
  public int modify(NavigationItem0 navigationItem0) {
    int ret =  navigationItem0Mapper.update(navigationItem0);
    updateConfig();
    return ret;
  }

  /**
   * 删除导航条目
   */
  public int remove(int id) {
    int ret = navigationItem0Mapper.deleteById(id);
    updateConfig();
    return ret;
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

  public void checkDataSource(String timeKey){
    boolean result = false;
    String message = "";
    if(iniConfiguration.getInt("allMonitorStatus",0)==0){
      LOGGER.info("topic监控总开关为关闭状态");
      return;
    }
    List<NavigationItem0> navigationItem0List = navigationItem0Mapper.findByType(1);
    if(navigationItem0List!=null){
      long nowTime = System.currentTimeMillis();
      for(NavigationItem0 navigationItem0:navigationItem0List){
         Map<String,Long> mapData = kafkaService.getLastMessageTimestampWithIp(navigationItem0.getTopic());
         long intervalTime = getIntervalTime(navigationItem0.getTopic(),timeKey);
         if(intervalTime==0){
           LOGGER.info("topic："+navigationItem0.getTopic()+"监控开关为关闭状态");
           continue;
         }
         if(mapData.isEmpty()) {
           message = ENVIRONMENT + "环境-" + navigationItem0.getName() + ":" + TOPIC_ERROR;
           sendWarnMessage(navigationItem0.getName(), navigationItem0.getManager(), navigationItem0.getManager(), message);
         }else{
           for(String key:mapData.keySet()) {
               long lastTimestamp = mapData.get(key);
               if(key==null){
                 key = "无效机器IP";
               }
               long timeInterval = nowTime - lastTimestamp;
               if (timeInterval >= intervalTime) {
                 message = ENVIRONMENT + "环境-" + navigationItem0.getName() + ":数据异常（"+intervalTime/60000+"分钟内无数据访问)(" + key + ")";
                 sendWarnMessage(navigationItem0.getName(), navigationItem0.getManager(), navigationItem0.getManageId(), message);
               }

           }
         }
      }
    }
  }

  private long getIntervalTime(String topic,String key){
    long intervalTime = 0;
    long defaultValue = iniConfiguration.getLong(key+".default",30);
    SubnodeConfiguration sectionConfig = iniConfiguration.getSection(topic);
    if(sectionConfig!=null){
      if(sectionConfig.getInt("monitorStatus",1)!=0) {
        intervalTime = sectionConfig.getLong(key, defaultValue);
      }
    }else{
      intervalTime = defaultValue;
    }
    return intervalTime*60000;
  }


  public void sendWarnMessage(String title,String names,String ids,String content){
    String status = "告警失败";
    String url = ConfigHelper.getApplicationConfig().get("qinxin.message.url");
    HttpPost httpPost = new HttpPost(url);
    List<NameValuePair> formParams = new ArrayList<NameValuePair>();
    formParams.add(new BasicNameValuePair("title", title));
    formParams.add(new BasicNameValuePair("content", content));
    formParams.add(new BasicNameValuePair("ids", ids));
    formParams.add(new BasicNameValuePair("names", names));
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
  @PostConstruct
  public void initTopicConfig(){
    ConfigFactory.getInstance().getConfig("buried-topic-monitor").addListener(config ->{
      byte[] content = config.getContent();
      InputStream inputStream = new ByteArrayInputStream(content);
      try {
        iniConfiguration.clear();
        iniConfiguration.load(inputStream);
        iniConfiguration.setThrowExceptionOnMissing(false);
      } catch (Exception e) {
        LOGGER.error("读取主题读取远程配置文件失败");
      }
    });
  }

  public void updateConfig() {
    try {
      List<NavigationItem0> list = queryAll();
      Set<String> topics = Sets.newLinkedHashSet();

      String blacklistStr = ConfigFactory.getInstance().getConfig("buriedtool-scheduler").get("es-blacklist", "");
      List<String> blacklist = Splitter.on(",").splitToList(blacklistStr);

      list.stream()
          .filter(i -> i != null)
          .map(NavigationItem0::getTopic)
          .filter(s -> StringUtils.contains(s, "."))
          .forEach(topics::add);

      StringBuilder configContent = new StringBuilder();
      topics.stream()
          .sorted(String::compareTo)
          .forEach(s -> configContent.append(blacklist.contains(s) ? "#[" : "[").append(s).append("]\n"));

      IConfigAdmin configAdmin = new ConfigAdminClient();
      String profile = ConfigHelper.getProcessInfo().getProfile();
      configAdmin.save(CONFIG_TOKEN, profile, KAFKA2ES_TOPIC_CONFIG, configContent.toString());
    } catch (Exception e) {
      LOGGER.error("Cannot update config for {}", KAFKA2ES_TOPIC_CONFIG, e);
    }
  }

}
