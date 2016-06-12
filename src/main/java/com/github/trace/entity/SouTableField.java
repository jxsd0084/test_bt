package com.github.trace.entity;

/**
 * Created by weilei on 2016/4/11.
 */
public class SouTableField {

	public static final String DB_INT      = "int";
	public static final String DB_DOUBLE   = "double";
	public static final String DB_LONG     = "long";
	public static final String DB_STRING   = "string";
	public static final String DB_DATETIME = "dateTime";
	/**
	 * name ：表字段字段名称
	 */
	private String name;
	/**
	 * des ：是否脱敏
	 */
	private String des;
	/**
	 * par : 是否分区
	 */
	private String par;
	/**
	 * type ：字段类型
	 */
	private String type;

	public SouTableField() {

	}

	public SouTableField( String name, String des, String par ) {

		this.name = name;
		this.des = des;
		this.par = par;
	}

	public SouTableField( String name, String des, String par, String type ) {

		this.name = name;
		this.des = des;
		this.par = par;
		this.type = type;
	}

	public String getType() {

		return type;
	}

	public void setType( String type ) {

		this.type = type;
	}

	public String getName() {

		return name;
	}

	public void setName( String name ) {

		this.name = name;
	}

	public String getDes() {

		return des;
	}

	public void setDes( String des ) {

		this.des = des;
	}

	public String getPar() {

		return par;
	}

	public void setPar( String par ) {

		this.par = par;
	}
}
