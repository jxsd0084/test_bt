package com.github.trace.service;

import com.github.trace.entity.DatabaseBiz;
import com.github.trace.entity.DatabaseInfo;
import com.github.trace.mapper.DataBaseBizMapper;
import com.github.trace.mapper.DatabaseInfoMapper;
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
    private DataBaseBizMapper dataBaseBizMapper;
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
     * 插入
     * @param databaseInfo
     */
    public int addDatabaseInfo(DatabaseInfo databaseInfo) {
        return dataBaseInfoMapper.insert(databaseInfo);
    }

}








































