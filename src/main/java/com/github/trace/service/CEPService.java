package com.github.trace.service;

import com.github.trace.entity.BuriedPoint;
import com.github.trace.entity.NavigationItem;
import com.github.trace.mapper.BuriedPointMapper;
import com.github.trace.mapper.NavigationItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
   * 获取所有的导航项列表
   * @return 埋点列表
     */
//  @Cacheable(value="navigationItemCache")
  public List<NavigationItem> getConfiguration() {
    return navigationItemMapper.findAll();
  }

  /**
   * 根据id查询具体的导航项
   * @param id
   * @return
     */
  public NavigationItem getConfigById(int id){
    return navigationItemMapper.findById(id);
  }

  /**
   * 更新导航项
   * @param navigationItem
   * @return
     */
  public int updateConfig(NavigationItem navigationItem){
    return navigationItemMapper.update(navigationItem);
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

}
