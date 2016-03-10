package com.github.trace.entity;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;

import java.util.Map;

/**
 * 邮件发送数据实体类
 * Created by hanmz on 2015/12/23.
 */
public class Oss {
  public static final Escaper JS_ESCAPE = Escapers.builder()
                                                  .addEscape('\'', "\\'")
                                                  .addEscape('\"', "\\\"")
                                                  .addEscape('\\', "\\\\")
                                                  .addEscape('/', "\\/")
                                                  .build();
  private String name;
  private String desc;
  private long total;
  private long failNum;
  private double fail;
  private double slow;
  private double cost;

  public Oss() {
  }

  public Oss(String name, String desc, long total, long failNum, double fail, double slow, double cost) {
    this.name = name;
    this.desc = desc;
    this.total = total;
    this.failNum = failNum;
    this.fail = fail;
    this.slow = slow;
    this.cost = cost;
  }

  public Oss(Map m) {
    this.name = JS_ESCAPE.escape((String) m.get("name"));
    this.desc = JS_ESCAPE.escape((String) m.get("desc"));
    this.total = Long.parseLong(m.get("total").toString());
    this.failNum = Long.parseLong(m.get("failNum").toString());
    this.fail = Double.parseDouble(m.get("fail").toString());
    this.slow = Double.parseDouble(m.get("slow").toString());
    this.cost = Double.parseDouble(m.get("cost").toString());
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public double getFail() {
    return fail;
  }

  public void setFail(double fail) {
    this.fail = fail;
  }

  public double getSlow() {
    return slow;
  }

  public void setSlow(double slow) {
    this.slow = slow;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getFailNum() {
    return failNum;
  }

  public void setFailNum(long failNum) {
    this.failNum = failNum;
  }
}
