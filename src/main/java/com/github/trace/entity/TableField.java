package com.github.trace.entity;

/**
 * Created by chenlong on 2016/3/30.
 */
public class TableField {

    private int id;
    private String columnName;
    private String columnType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

}
