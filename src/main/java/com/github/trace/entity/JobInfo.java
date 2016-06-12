package com.github.trace.entity;

import java.util.Date;

public class JobInfo {

	private Integer id;
	private Integer bizId;
	private String  bizName;
	private String  name;
	private int     souId;
	private String  souName;
	private int     tarId;
	private String  tarName;
	private String  expType;
	private String  startTime;
	private String  endTime;
	private String  exeTime;
	private String  memo;
	private Date    createTime;
	private Date    updateTime;

	public Integer getId() {

		return id;
	}

	public void setId( Integer id ) {

		this.id = id;
	}

	public Integer getBizId() {

		return bizId;
	}

	public void setBizId( Integer bizId ) {

		this.bizId = bizId;
	}

	public String getBizName() {

		return bizName;
	}

	public void setBizName( String bizName ) {

		this.bizName = bizName;
	}

	public String getName() {

		return name;
	}

	public void setName( String name ) {

		this.name = name;
	}

	public int getSouId() {

		return souId;
	}

	public void setSouId( int souId ) {

		this.souId = souId;
	}

	public String getSouName() {

		return souName;
	}

	public void setSouName( String souName ) {

		this.souName = souName;
	}

	public int getTarId() {

		return tarId;
	}

	public void setTarId( int tarId ) {

		this.tarId = tarId;
	}

	public String getTarName() {

		return tarName;
	}

	public void setTarName( String tarName ) {

		this.tarName = tarName;
	}

	public String getStartTime() {

		return startTime;
	}

	public void setStartTime( String startTime ) {

		this.startTime = startTime;
	}

	public String getEndTime() {

		return endTime;
	}

	public void setEndTime( String endTime ) {

		this.endTime = endTime;
	}

	public String getMemo() {

		return memo;
	}

	public void setMemo( String memo ) {

		this.memo = memo;
	}

	public Date getCreateTime() {

		return createTime;
	}

	public void setCreateTime( Date createTime ) {

		this.createTime = createTime;
	}

	public Date getUpdateTime() {

		return updateTime;
	}

	public void setUpdateTime( Date updateTime ) {

		this.updateTime = updateTime;
	}

	public String getExpType() {

		return expType;
	}

	public void setExpType( String expType ) {

		this.expType = expType;
	}

	public String getExeTime() {

		return exeTime;
	}

	public void setExeTime( String exeTime ) {

		this.exeTime = exeTime;
	}
}