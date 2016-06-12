package com.github.trace.entity;


/**
 * Created by wanghl on 2016/3/31.
 */
public class NavigationItem0 {

	private int id;

	private String name;
	//0：非叶子节点;1：叶子节点
	private int    itemType;

	private String topic;

	private String manager;

	//关联本表中的id
	private int parentId = 0;

	private String manageId;

	public String getManager() {

		return manager;
	}

	public void setManager( String manager ) {

		this.manager = manager;
	}

	public String getTopic() {

		return topic;
	}

	public void setTopic( String topic ) {

		this.topic = topic;
	}

	public int getItemType() {

		return itemType;
	}

	public void setItemType( int itemType ) {

		this.itemType = itemType;
	}

	public String getName() {

		return name;
	}

	public void setName( String name ) {

		this.name = name;
	}

	public int getParentId() {

		return parentId;
	}

	public void setParentId( int parentId ) {

		this.parentId = parentId;
	}

	public int getId() {

		return id;
	}

	public void setId( int id ) {

		this.id = id;
	}

	public String getManageId() {

		return manageId;
	}

	public void setManageId( String manageId ) {

		this.manageId = manageId;
	}

	@Override
	public String toString() {

		return "NavigationItem0{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       ", itemType=" + itemType +
		       ", topic='" + topic + '\'' +
		       ", manager='" + manager + '\'' +
		       ", parentId=" + parentId +
		       ", manageId='" + manageId + '\'' +
		       '}';
	}
}
