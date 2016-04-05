package com.github.trace.service;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.LevelOneFields;
import com.github.trace.entity.LevelTwoFields;
import com.github.trace.entity.M99Fields;
import com.github.trace.mapper.LevelOneFieldsMapper;
import com.github.trace.mapper.LevelTwoFieldsMapper;
import com.github.trace.mapper.M99FieldsMapper;
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
  @Autowired
  private M99FieldsMapper      m99FieldsMapper;

  /**
   * 根据M1字段获取所有M99扩展字段的数量
   * @param m1Name
   * @return
     */
  public int getM99FieldsCount(String m1Name){
    return m99FieldsMapper.getM99FieldsCountByM1Name(m1Name);
  }

  /**
   * 根据M1字段获取所有M99扩展字段的数量
   * @param m1Name
   * @return
     */
  public List<M99Fields> getM99Fields(String m1Name){
    return m99FieldsMapper.getM99FieldsByM1Name(m1Name);
  }

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

  private int updateLevelTwoByL1Obj(int l1_id, String l1_tag, String l1_name) {
    int res = 0;
    List<LevelTwoFields> list = getLevelTwoFieldList(l1_id);
    if(list.size() == 0){ // 有一级字段,没有二级字段
      res = 1;
      return res;
    }
    for (int i = 0; i < list.size(); i++) {
      LevelTwoFields fields = list.get(i);
      fields.setLevel1FieldTag(l1_tag);
      fields.setLevel1FieldName(l1_name);
      res = updateLevelTwo(fields);
    }
    return res;
  }

  public JSONArray getLevelOneFieldList2() {
    List<LevelOneFields> list = getLevelOneFieldList();
    JSONArray jsonArray1 = new JSONArray();
    for (LevelOneFields levelOneFields : list ) {
        JSONArray jsonArray2 = new JSONArray();
        String tagName = levelOneFields.getLevel1FieldTag();
        jsonArray2.add(levelOneFields.getId());
        jsonArray2.add(levelOneFields.getLevel1FieldName());
        jsonArray2.add(levelOneFields.getLevel1FieldDesc());
        jsonArray2.add(tagName);
        int m99Count = getM99FieldsCount(tagName);  // M99的扩展字段
        jsonArray2.add(m99Count);
        jsonArray1.add(jsonArray2);
    }
    return jsonArray1;
  }

}
