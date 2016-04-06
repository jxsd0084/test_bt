package com.github.trace.entity;

import java.util.Date;

public class JobConfig {
    private Integer id;
    private Integer bizId;
    private String bizName;
    private String name;
    private String dbSourceId;
    private String dbSourceName;
    private String selectData;
    private String memo;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBizId() {
        return bizId;
    }

    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public String getName() {
        return name;
    }

    public String getMemo() {
        return memo;
    }

    public String getDbSourceId() {
        return dbSourceId;
    }

    public void setDbSourceId(String dbSourceId) {
        this.dbSourceId = dbSourceId;
    }

    public String getDbSourceName() {
        return dbSourceName;
    }

    public void setDbSourceName(String dbSourceName) {
        this.dbSourceName = dbSourceName;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectData() {
        return selectData;
    }

    public void setSelectData(String selectData) {
        this.selectData = selectData;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}