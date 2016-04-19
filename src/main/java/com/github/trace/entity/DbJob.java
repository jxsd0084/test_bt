package com.github.trace.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by weilei on 2016/4/11.
 */
public class DbJob {
    /**
     * souName:数据库名称
     */
    private String souName;
    /**
     * souType:数据库类型，MySql，SQLServer,MongoDB,
     */
    private String souType;
    /**
     * souUrl:链接地址
     */
    private String souUrl;

    /**
     * souPort:端口号
     */
    private Integer souPort;
    /**
     * souDriver:驱动名称
     */
    private String souDriver;
    /**
     * souUser:用户名
     */
    private String souUser;
    /**
     * souPassword:密码
     */
    private String souPassword;
    /**
     * souIns:数据库实例（sqlserver使用）
     */
    private String souIns;
    /**
     * souTable:要导出的表
     */
    private String souTable;
    /**
     * fields:要导出的表字段属性
     */
    private List<SouTableField> fields;
    /**
     * incrementField:自增长字段
     */
    private String incrementField;
    /**
     * primaryKey:主键
     */
    private String primaryKey;
    /**
     * expType:导出类型：false：全量导入，true：增量导入
     */
    private boolean expType;
    /**
     * startTime:增量导入时的开始起始时间
     */
    private Date startTime;
    /**
     * endTime:增量导入时的截止时间
     */
    private Date endTime;

    private String dateField;

    /**
     * tarUrl:目标地址
     */
    private String tarUrl;
    /**
     * tarUrl:目标端口号
     */
    private int tarPort;
    /**
     * tarName:目标名称
     */
    private String tarName;
    /**
     * tarType:目标类型：ES或者HDFS
     */
    private String tarType;
    /**
     * tarPath:主题或路径
     */
    private String tarPath;

    public void addField(SouTableField field){
        if(fields==null)
            fields = new ArrayList<SouTableField>();
        fields.add(field);
    }

    public String getSouName() {
        return souName;
    }

    public void setSouName(String souName) {
        this.souName = souName;
    }

    public String getSouType() {
        return souType;
    }

    public void setSouType(String souType) {
        this.souType = souType;
    }

    public String getSouUrl() {
        return souUrl;
    }

    public void setSouUrl(String souUrl) {
        this.souUrl = souUrl;
    }

    public String getSouDriver() {
        return souDriver;
    }

    public void setSouDriver(String souDriver) {
        this.souDriver = souDriver;
    }

    public String getSouUser() {
        return souUser;
    }

    public void setSouUser(String souUser) {
        this.souUser = souUser;
    }

    public String getSouPassword() {
        return souPassword;
    }

    public void setSouPassword(String souPassword) {
        this.souPassword = souPassword;
    }

    public String getSouIns() {
        return souIns;
    }

    public void setSouIns(String souIns) {
        this.souIns = souIns;
    }

    public String getSouTable() {
        return souTable;
    }

    public void setSouTable(String souTable) {
        this.souTable = souTable;
    }

    public List<SouTableField> getFields() {
        return fields;
    }

    public void setFields(List<SouTableField> fields) {
        this.fields = fields;
    }

    public String getIncrementField() {
        return incrementField;
    }

    public void setIncrementField(String incrementField) {
        this.incrementField = incrementField;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isExpType() {
        return expType;
    }

    public void setExpType(boolean expType) {
        this.expType = expType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTarUrl() {
        return tarUrl;
    }

    public void setTarUrl(String tarUrl) {
        this.tarUrl = tarUrl;
    }

    public String getTarName() {
        return tarName;
    }

    public void setTarName(String tarName) {
        this.tarName = tarName;
    }

    public String getDateField() {
        return dateField;
    }

    public void setDateField(String dateField) {
        this.dateField = dateField;
    }

    public int getTarPort() {

        return tarPort;
    }

    public void setTarPort(int tarPort) {
        this.tarPort = tarPort;
    }

    public String getTarType() {
        return tarType;
    }

    public void setTarType(String tarType) {
        this.tarType = tarType;
    }

    public String getTarPath() {
        return tarPath;
    }

    public void setTarPath(String tarPath) {
        this.tarPath = tarPath;
    }

    public Integer getSouPort() {
        return souPort;
    }

    public void setSouPort(Integer souPort) {
        this.souPort = souPort;
    }
}
