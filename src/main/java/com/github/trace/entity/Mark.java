package com.github.trace.entity;

import java.io.Serializable;

/**
 * 备注信息
 * Created by hanmz on 2015/12/21.
 */
public class Mark implements Serializable{
  private String service;
  private String info;
  private String owner;

  public Mark() {
  }

  public Mark(String service, String info, String owner) {
    this.service = service;
    this.info = info;
    this.owner = owner;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getDescription() {
    return owner == null || owner.trim().isEmpty() ? info : (info + "-[" + owner + "]");
  }
}
