package com.github.trace.entity;

import java.util.Date;

public class BpJob {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.biz_id
     *
     * @mbggenerated
     */
    private Integer bizId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.biz_name
     *
     * @mbggenerated
     */
    private String bizName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.table_id
     *
     * @mbggenerated
     */
    private Integer tableId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.table_name
     *
     * @mbggenerated
     */
    private String tableName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.target_id
     *
     * @mbggenerated
     */
    private Integer targetId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.target_name
     *
     * @mbggenerated
     */
    private String targetName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.field_id
     *
     * @mbggenerated
     */
    private Integer fieldId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.execute_time
     *
     * @mbggenerated
     */
    private Date executeTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bp_job.last_update
     *
     * @mbggenerated
     */
    private Date lastUpdate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.id
     *
     * @return the value of bp_job.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.id
     *
     * @param id the value for bp_job.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.biz_id
     *
     * @return the value of bp_job.biz_id
     *
     * @mbggenerated
     */
    public Integer getBizId() {
        return bizId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.biz_id
     *
     * @param bizId the value for bp_job.biz_id
     *
     * @mbggenerated
     */
    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.biz_name
     *
     * @return the value of bp_job.biz_name
     *
     * @mbggenerated
     */
    public String getBizName() {
        return bizName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.biz_name
     *
     * @param bizName the value for bp_job.biz_name
     *
     * @mbggenerated
     */
    public void setBizName(String bizName) {
        this.bizName = bizName == null ? null : bizName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.table_id
     *
     * @return the value of bp_job.table_id
     *
     * @mbggenerated
     */
    public Integer getTableId() {
        return tableId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.table_id
     *
     * @param tableId the value for bp_job.table_id
     *
     * @mbggenerated
     */
    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.table_name
     *
     * @return the value of bp_job.table_name
     *
     * @mbggenerated
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.table_name
     *
     * @param tableName the value for bp_job.table_name
     *
     * @mbggenerated
     */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.target_id
     *
     * @return the value of bp_job.target_id
     *
     * @mbggenerated
     */
    public Integer getTargetId() {
        return targetId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.target_id
     *
     * @param targetId the value for bp_job.target_id
     *
     * @mbggenerated
     */
    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.target_name
     *
     * @return the value of bp_job.target_name
     *
     * @mbggenerated
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.target_name
     *
     * @param targetName the value for bp_job.target_name
     *
     * @mbggenerated
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName == null ? null : targetName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.field_id
     *
     * @return the value of bp_job.field_id
     *
     * @mbggenerated
     */
    public Integer getFieldId() {
        return fieldId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.field_id
     *
     * @param fieldId the value for bp_job.field_id
     *
     * @mbggenerated
     */
    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.execute_time
     *
     * @return the value of bp_job.execute_time
     *
     * @mbggenerated
     */
    public Date getExecuteTime() {
        return executeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.execute_time
     *
     * @param executeTime the value for bp_job.execute_time
     *
     * @mbggenerated
     */
    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bp_job.last_update
     *
     * @return the value of bp_job.last_update
     *
     * @mbggenerated
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bp_job.last_update
     *
     * @param lastUpdate the value for bp_job.last_update
     *
     * @mbggenerated
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}