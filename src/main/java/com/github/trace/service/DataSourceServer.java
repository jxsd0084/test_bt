package com.github.trace.service;

import com.github.trace.entity.DatabaseBiz;
import com.github.trace.entity.DatabaseInfo;
import com.github.trace.mapper.DataBaseBizMapper;
import com.github.trace.mapper.DatabaseInfoMapper;
import com.github.trace.utils.DataBaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by chenlong on 2016/3/28.
 */
@Service
public class DataSourceServer {

    private static final Logger LOG = LoggerFactory.getLogger( DataSourceServer.class );

    @Autowired
    private DataBaseBizMapper                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              dataBaseBizMapper;
    @Autowired
    private DatabaseInfoMapper dataBaseInfoMapper;

    /**
     * 查询 数据源 导航项
     */
    public List<DatabaseBiz> getDataBaseBizList(){
        return dataBaseBizMapper.findAll();
    }

    /**
     * 根据Id查询 数据源列表
     */
    public List<DatabaseInfo> getDataBaseInfoListById(int id){
        return dataBaseInfoMapper.findDatabaseInfoListById(id);
    }

    /**
     * 根据Id查询 数据源
     * @param id
     */
    public DatabaseInfo getDataBaseInfoById(int id) {
        return dataBaseInfoMapper.findById(id);
    }

    /**
     * 根据Id更新 数据源
     * @param databaseInfo
     * @return
     */
    public int updateDataBaseInfo(DatabaseInfo databaseInfo) {
        return dataBaseInfoMapper.update(databaseInfo);
    }

    /**
     * 根据Id删除 数据源
     * @param id
     * @return
     */
    public int deleteDataBaseInfoById(int id) {
        return dataBaseInfoMapper.deleteById(id);
    }

    /**
     * 插入
     * @param databaseInfo
     */
    public int addDatabaseInfo(DatabaseInfo databaseInfo) {
        return dataBaseInfoMapper.insert(databaseInfo);
    }

    /**
     * 测试 数据库连接
     * @param databaseInfo
     * @return
     */
    public int testJdbcConnection(DatabaseInfo databaseInfo) {
        return DataBaseHelper.testConnection(databaseInfo);
    }

    /**
     * 获取 目标数据库中所有的表
     * @param databaseInfo
     * @return
     */
    public List getDatabaseTables(DatabaseInfo databaseInfo) {
        return DataBaseHelper.getDatabaseTables(databaseInfo);
    }

    /**
     * 获取 目标表中所有字段
     * @param databaseInfo
     * @param tableName
     */
    public List<String> getTableFields(DatabaseInfo databaseInfo, String tableName) {
        return DataBaseHelper.getTableFields(databaseInfo, tableName);
    }

}








































