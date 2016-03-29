package com.github.trace.utils;

import com.github.trace.entity.DatabaseInfo;
import com.mysql.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;

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
    public static int testConnection(DatabaseInfo databaseInfo){
        int res = 0;
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
            if (conn != null) {
                res = 1;
            }
        } catch (Exception e){
            LOGGER.error("get connection failed !", e);
        } finally {
            closeConnection(conn);
        }
        return res;
    }

    /**
     * 关闭 数据库连接
     * @param conn
     */
    public static void closeConnection(Connection conn){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failed !", e);
            }
        }
    }

}
