package com.github.trace.entity;

public class DatabaseBiz {

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column database_biz.id
	 *
	 * @mbggenerated
	 */
	private Integer id;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column database_biz.name
	 *
	 * @mbggenerated
	 */
	private String name;

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column database_biz.id
	 *
	 * @return the value of database_biz.id
	 * @mbggenerated
	 */
	public Integer getId() {

		return id;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column database_biz.id
	 *
	 * @param id the value for database_biz.id
	 * @mbggenerated
	 */
	public void setId( Integer id ) {

		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column database_biz.name
	 *
	 * @return the value of database_biz.name
	 * @mbggenerated
	 */
	public String getName() {

		return name;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column database_biz.name
	 *
	 * @param name the value for database_biz.name
	 * @mbggenerated
	 */
	public void setName( String name ) {

		this.name = name == null ? null : name.trim();
	}
}