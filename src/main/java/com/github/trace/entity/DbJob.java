package com.github.trace.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weilei on 2016/4/11.
 */
public class DbJob {
    private String souName;
    private String souUser;
    private String souType;
    private String souUrl;
    private String souDriver;
    private String souPassword;
    private String souIns;
    private String souTable;
    private List<SouTableField> fields;
    private String incrementField;

    private String tarUrl;
    private String tarName;
    private String tarType;
    private String tarPath;

    public void addField(SouTableField field){
        if(fields==null)
            fields = new ArrayList<SouTableField>();
        fields.add(field);
    }

    public String getIncrementField() {
        return incrementField;
    }

    public void setIncrementField(String incrementField) {
        this.incrementField = incrementField;
    }

    public String getSouIns() {
        return souIns;
    }

    public void setSouIns(String souIns) {
        this.souIns = souIns;
    }

    public String getSouUrl() {
        return souUrl;
    }

    public void setSouUrl(String souUrl) {
        this.souUrl = souUrl;
    }

    public String getSouName() {
        return souName;
    }

    public void setSouName(String souName) {
        this.souName = souName;
    }

    public String getSouUser() {
        return souUser;
    }

    public void setSouUser(String souUser) {
        this.souUser = souUser;
    }

    public String getSouType() {
        return souType;
    }

    public void setSouType(String souType) {
        this.souType = souType;
    }

    public String getSouDriver() {
        return souDriver;
    }

    public void setSouDriver(String souDriver) {
        this.souDriver = souDriver;
    }

    public String getSouPassword() {
        return souPassword;
    }

    public void setSouPassword(String souPassword) {
        this.souPassword = souPassword;
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

}
