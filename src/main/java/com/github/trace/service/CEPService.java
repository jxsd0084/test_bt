package com.github.trace.service;

import com.github.trace.entity.BuriedPoint;
import com.github.trace.entity.NavigationItem;
import com.github.trace.mapper.BuriedPointMapper;
import com.github.trace.mapper.NavigationItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

  /**
   * 获取所有的埋点列表
   * @return 埋点列表
     */
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
     */
  public int addBuriedPoint(BuriedPoint buriedPoint){
    return buriedPointMapper.insert(buriedPoint);
  }

  public BuriedPoint getBuriedPoint(int id) {
    return buriedPointMapper.findById(id);
  }


  public boolean modifyBuriedPoint(String bp_name,String bp_value,String bp_value_desc,boolean is_checked ,int id) {
    return buriedPointMapper.updateBuriedPoint(bp_name,bp_value,bp_value_desc,is_checked,id);
  }

}
