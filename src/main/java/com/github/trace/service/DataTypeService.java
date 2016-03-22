package com.github.trace.service;

import com.github.trace.entity.LevelOneFields;
import com.github.trace.entity.LevelTwoFields;
import com.github.trace.mapper.LevelOneFieldsMapper;
import com.github.trace.mapper.LevelTwoFieldsMapper;
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
  @Autowired
  private LevelTwoFieldsMapper levelTwoFieldMapper;

   /**
   * 获取所有的 一级字段 列表
   * @return 一级字段列表
     */
  public List<LevelOneFields> getLevelOneFieldList() {
    return levelOneFieldMapper.findAll();
  }

  /**
   * 根据一级字段ID查询 二级字段
   * @param id 二级字段ID
   * @return 二级字段
     */
  public LevelOneFields getLevelOneFieldById(int id) {
    return levelOneFieldMapper.findById(id);
  }

  /***
   * 根据一级字段ID获取 二级字段 列表
   * @param id 一级字段ID
     */
  public List<LevelTwoFields> getLevelTwoFieldList(int id) {
    return levelTwoFieldMapper.findLevelTwoFieldsListById(id);
  }

  /**
   * 根据二级字段ID查询 二级字段
   * @param id 二级字段ID
   * @return 二级字段
     */
  public LevelTwoFields getLevelTwoFieldById(int id) {
    return levelTwoFieldMapper.findById(id);
  }

  /**
   * 插入一级新字段
   * @param levelOneFields
   * @return
     */
  public int addLevelOneFields(LevelOneFields levelOneFields) {
    return levelOneFieldMapper.insert(levelOneFields);
  }

  /**
   * 插入二级新字段
   * @param levelTwoFields
   * @return
     */
  public int addLevelTwoFields(LevelTwoFields levelTwoFields) {
    return levelTwoFieldMapper.insert(levelTwoFields);
  }


  /**
   * 更新一级字段
   * @param levelOneFields
   * @return
     */
  public int updateLevelOne(LevelOneFields levelOneFields) {
    return levelOneFieldMapper.update(levelOneFields);
  }

  /**
   * 更新一级字段
   * @param levelTwoFields
   * @return
     */
  public int updateLevelTwo(LevelTwoFields levelTwoFields) {
    return levelTwoFieldMapper.update(levelTwoFields);
  }

  /**
   * 级联更新二级字段
   */
  public int updateLevelTwoByCascade(LevelOneFields levelOneFields) {
    int res = updateLevelOne(levelOneFields);
    if (res == 1) {
      return updateLevelTwoByL1Obj(levelOneFields.getId(), levelOneFields.getLevel1FieldTag(), levelOneFields.getLevel1FieldName());
    }
    return 0;
  }

  private int updateLevelTwoByL1Obj(int id, String l1_tag, String l1_name) {
    int res = 0;
    List<LevelTwoFields> list = getLevelTwoFieldList(id);
    for (int i = 0; i < list.size(); i++) {
      LevelTwoFields fields = list.get(i);
      fields.setLevel1FieldName(l1_tag);
      fields.setLevel1FieldName(l1_name);
      res = updateLevelTwo(fields);
    }
    return res;
  }

}
