package com.github.trace.utils;

import com.github.trace.entity.DatabaseInfo;
import com.github.trace.entity.TableField;
import com.mysql.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlong on 2016/3/29.
 */
public class DataBaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger( DataBaseHelper.class );

    private static String CONN_DRIVER = "";
    private static String CONN_URL = "";
    private static String CONN_PORT = "";
    private static String CONN_USERNAME = "";
    private static String CONN_PASSWORD = "";
    private static String CONN_DB_NAME = "";

    /**
     * 获取 数据库连接
     * @param databaseInfo
     * @return
     */
    private static Connection getConnection(DatabaseInfo databaseInfo){
        Connection conn = null;
        try{
            CONN_DRIVER     = databaseInfo.getDbDriver();
            CONN_PORT       = databaseInfo.getDbPort().toString();
            CONN_USERNAME   = databaseInfo.getDbUsername();
            CONN_PASSWORD   = databaseInfo.getDbPassword();
            CONN_DB_NAME    = databaseInfo.getDbName();
            CONN_URL        = databaseInfo.getDbUrl() + ":" + CONN_PORT + "/" + CONN_DB_NAME;

            Class.forName(CONN_DRIVER);
            conn = (Connection) DriverManager.getConnection(CONN_URL, CONN_USERNAME, CONN_PASSWORD);
        } catch (Exception e){
            LOGGER.error("get connection failed !", e);
        }
        return conn;
    }

    /**
     * 关闭 数据库连接
     * @param conn
     */
    private static void closeConnection(Connection conn){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failed !", e);
            }
        }
    }

    /**
     * 测试 数据库连接
     * @return
     */
    public static int testConnection(DatabaseInfo databaseInfo){
        int res = 0;
        Connection conn =  getConnection(databaseInfo);
        if (conn != null) {
            res = 1;
            closeConnection(conn);
        }
        return res;
    }

    /**
     * 获取 目标数据中所有的表
     * @param databaseInfo
     * @return
     */
    public static List getDatabaseTables(DatabaseInfo databaseInfo) {
        List<String> list = new ArrayList<String>();
        Connection conn = getConnection(databaseInfo);
        if (conn != null) {
            try {
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getTables(null, null, "%", null);
                while (rs.next()){
                    list.add(rs.getString("TABLE_NAME"));
                }
            } catch (SQLException e) {
                LOGGER.error("get metaData failed !", e);
            }
        }
        return list;
    }

    /**
     * 获取 目标表中的所有字段
     * @param databaseInfo
     * @param tableName
     */
    public static List<TableField> getTableFields(DatabaseInfo databaseInfo, String tableName) {
        Connection conn = getConnection(databaseInfo);
        List<TableField> list = new ArrayList<TableField>();
        if (conn != null) {
            try {
                DatabaseMetaData dmd = conn.getMetaData();
                ResultSet rs = dmd.getColumns( null, null, tableName, "%");
                while (rs.next()){
                    TableField tableField = new TableField();
                    tableField.setColumnName(rs.getString(4));
                    tableField.setColumnType(rs.getString(6));

                    list.add(tableField);
                }
            } catch (SQLException e) {
                LOGGER.error("get table fields failed !", e);
            }
        }
        return list;
    }
}
