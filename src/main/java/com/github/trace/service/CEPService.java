package com.github.trace.service;

import com.github.trace.entity.BuriedPoint;
import com.github.trace.mapper.BuriedPointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发送邮件
 * Created by hanmz on 2015/12/22.
 */
@Service
public class CEPService {

  @Autowired
  private BuriedPointMapper buriedPointMapper;

  /**
   * 获取所有的埋点列表
   * @return 埋点列表
     */
  public List<BuriedPoint> getConfiguration() {
    return buriedPointMapper.findAll();
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
   * @param parent_id 父业务ID
   * @param child_id  子业务ID
   * @return  1-删除成功  0-删除失败
     */
  public int deleteBuriedPoint(int parent_id, int child_id){
    return buriedPointMapper.deleteByBizIds(parent_id, child_id);
  }

  /**
   * 插入新埋点
   * @param buriedPoint
   * @return
     */
  public int insertBuriedPoint(BuriedPoint buriedPoint){
    return buriedPointMapper.insert(buriedPoint);
  }

}
