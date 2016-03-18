package com.github.trace.service;

import com.github.trace.entity.LevelOneFields;
import com.github.trace.mapper.LevelOneFieldsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenlong on 2016/3/17.
 */
@Service
public class DataTypeService {

  @Autowired
  private LevelOneFieldsMapper levelOneFieldMapper;

   /**
   * 获取所有的 一级字段 列表
   * @return 一级字段列表
     */
  public List<LevelOneFields> getLevelOneFieldList() {
    return levelOneFieldMapper.findAll();
  }

}
