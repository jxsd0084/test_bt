package com.github.trace.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.trace.entity.BuriedPoint;
import com.github.trace.entity.NavigationItem;
import com.github.trace.mapper.BuriedPointMapper;
import com.github.trace.mapper.NavigationItemMapper;
import com.github.trace.utils.ControllerHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CEP 后台埋点配置管理
 * Created by wujing on 2016/03/11.
 */
@Service
public class CEPService {

  @Autowired
  private BuriedPointMapper buriedPointMapper;

  @Autowired
  private NavigationItemMapper navigationItemMapper;

  @Autowired
  private KafkaService kafkaService;

  /**
   * 获取所有的埋点列表
   * @return 埋点列表
     */
//  @Cacheable(value="navigationItemCache")
  public List<NavigationItem> getConfiguration() {
    return navigationItemMapper.findAll();
  }

  /**
   * 获取埋点列表
   * @param parent_id 父业务ID
   * @param child_id  子业务ID
   * @return 埋点列表
     */
  public List<BuriedPoint> getBuriedPointList(int parent_id, int child_id) {
    return buriedPointMapper.findByBizIds(parent_id, child_id);
  }

  /**
   * 删除埋点
   * @param id 业务ID
   * @return  1-删除成功  0-删除失败
     */
  public int deleteBuriedPoint(int id) {
    return buriedPointMapper.deleteBuriedPoint(id);
  }

  /**
   * 插入新埋点
   * @param buriedPoint
   * @return
   *
  */
  public int addBuriedPoint(BuriedPoint buriedPoint){
    return buriedPointMapper.insert(buriedPoint);
  }

  public BuriedPoint getBuriedPoint(int id) {
    return buriedPointMapper.findById(id);
  }


  public boolean modifyBuriedPoint(String bp_name,String bp_value,String regex,String bp_value_desc,boolean is_checked ,int id) {
    return buriedPointMapper.updateBuriedPoint(bp_name,bp_value,regex,bp_value_desc,is_checked,id);
  }


  public Set<String> getServerLog(String str1,int st2) {
    //return kafkaService.getMessages("nginx.reverse","type",1);
    return kafkaService.getMessages(str1,"type",st2);
  }

  public String compareByTopic(String topic,
                        String json) {


    List<BuriedPoint>  buriedPoints = buriedPointMapper.findByTopic(topic);
    //buriedPointMapper.findByTopic(topic);


    JSONObject jsonObject = new JSONObject();

    for (BuriedPoint br :buriedPoints) {
      jsonObject.put(br.getBpName(), br.getBpValueDesc() + "," + br.getBpValue() + "," + br.getRegex());
    }



    JSONArray jsonArray  = JSON.parseArray(json);


    JSONArray ja1 = new JSONArray();

    LinkedHashMap<String, String> jsonMap1 = JSON.parseObject(jsonObject.toString(), new TypeReference<LinkedHashMap<String, String>>() {});


    for (Map.Entry<String, String> entry1 : jsonMap1.entrySet()) {
      JSONArray ja2 = new JSONArray();
      ja2.add(entry1.getKey()+"");
      ja2.add(entry1.getValue().split(",")[0]+"");
      for (int i = 0; i < jsonArray.size(); i++) {

        LinkedHashMap<String, String> jsonMap2 = JSON.parseObject(jsonArray.get(i).toString(), new TypeReference<LinkedHashMap<String, String>>() {});

        if(jsonMap2.containsKey(entry1.getKey())){

          String patternString = "";
          String patternString2 = "";

          if(entry1.getValue().split(",")[1].equals("文本")){
            patternString = ".*";
          }

          if(entry1.getValue().split(",")[1].equals("数字")){
            patternString = "^[0-9]*$";
          }

          if(entry1.getValue().split(",")[1].equals("日期")){
            patternString = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";
          }

          Pattern pattern = Pattern.compile(patternString);
          Matcher matcher = pattern.matcher(jsonMap2.get(entry1.getKey()));
          boolean b= matcher.matches();


          if(entry1.getValue().split(",").length>=3){

            try {
              patternString2 = URLDecoder.decode(entry1.getValue().split(",")[2].toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
            }

            Pattern pattern2 = null;
            pattern2 = Pattern.compile( patternString2);

            Matcher matcher2 = pattern2.matcher(jsonMap2.get(entry1.getKey()));
            b= matcher2.matches();
          }

          ja2.add(jsonMap2.get(entry1.getKey()));
          ja2.add(b);


        }else{

          ja2.add("");
          ja2.add(false);

        }

      }
      ja1.add(ja2);

    }


    System.out.println("BuriedPointList"+ja1.toJSONString());

    return ja1.toJSONString();
  }


}
