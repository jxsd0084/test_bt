package com.github.trace.entity;

import com.github.mybatis.entity.IdEntity;

/**
 * 保存日报数据
 * Created by hanmz on 2015/12/23.
 */
public class Daily extends IdEntity {
  private String day;
  private String data;

  public Daily() {
  }

  public Daily(String day, String data) {
    this.day = day;
    this.data = data;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "Daily{" +
      "day='" + day + '\'' +
      ", data='" + data + '\'' +
      '}';
  }
}
