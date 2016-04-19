package com.github.trace.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by weilei on 2016/4/18.
 */
@Service
public class JobSchedueService {
    @Autowired
    private JobServer jobServer;
    @Autowired
    private DataSourceServer dataSourceServer;

    public List<DbJob> getDbJobList(int jobInfoId){
        List<DbJob> dbJobs = new ArrayList<DbJob>();
        JobInfo jobInfo = jobServer.getJobInfoById(jobInfoId);
        int jobSouId = jobInfo.getSouId();
        int jobTarId = jobInfo.getTarId();
        JobSource js = jobServer.getJobSouById(jobSouId);
        JobTarget jt = jobServer.getJobTarById(jobTarId);
        int dbSourceId = Integer.valueOf(js.getDbSourceId());
        DatabaseInfo dbInfo = dataSourceServer.getDataBaseInfoById(dbSourceId);
        String selectData = js.getSelectData()==null?"":js.getSelectData();
        JSONObject jsonObj =  JSONObject.parseObject(selectData);
        if(jsonObj!=null){
            for(Map.Entry<String,Object> entry:jsonObj.entrySet()){
                DbJob job = new DbJob();
                initSou(job,js,dbInfo);
                initTar(job,jt);

                String key = entry.getKey();
                Object val = entry.getValue();
                job.setSouTable(key);
                job.setStartTime(new Date(jobInfo.getStartTime()));
                job.setEndTime(new Date(jobInfo.getEndTime()));
                String expType = jobInfo.getExpType();
                if(expType!=null){
                    if("0".equals(expType))
                        job.setExpType(true);
                    else
                        job.setExpType(false);
                }

                initField(job,val);
                dbJobs.add(job);
            }
        }
        return dbJobs;
    }

    private  void initSou(DbJob job,JobSource js,DatabaseInfo dbInfo){
        job.setSouName(dbInfo.getName());
        job.setSouUser(dbInfo.getDbUsername());
        job.setSouPort(dbInfo.getDbPort());
        job.setSouPassword(dbInfo.getDbPassword());
        job.setSouDriver(dbInfo.getDbDriver());
        job.setSouUrl(dbInfo.getDbUrl());
        job.setSouType(dbInfo.getDbType());
        job.setSouIns(dbInfo.getDbIns());
    }

    private  void initTar(DbJob job,JobTarget jt){
        job.setTarName(jt.getName());
        job.setTarUrl(jt.getIp());
        job.setTarPort(jt.getPort());
        job.setTarType(jt.getType());
        job.setTarPath(jt.getPath());
    }

    private void initField(DbJob job ,Object val){
        if(val!=null){
            JSONArray jsonArr = (JSONArray)val;
            String priKeys="";
            for(Object field:jsonArr){
                if(field!=null){
                    SouTableField tf = new SouTableField();
                    JSONObject fieldJson = (JSONObject)field;
                    Integer exp = (Integer)(fieldJson.get("exp"));
                    if(exp==1){
                        String type = (String)(fieldJson.get("type"))==null?SouTableField.DB_STRING:
                                (String)(fieldJson.get("type"));
                        if(type.indexOf("CHAR")>=0|| type .indexOf("BLOB")>=0|| type.indexOf("TEXT")>=0)
                            type =  SouTableField.DB_STRING;
                        else if(type.indexOf("INT")>=0)
                            type =  SouTableField.DB_INT;
                        else if(type.indexOf("DOUBLE")>=0)
                            type =  SouTableField.DB_DOUBLE;
                        tf.setType(type);

                        String name = (String)(fieldJson.get("name"));
                        tf.setName(name);
                        Integer pri = (Integer)(fieldJson.get("pri"));
                        if(pri!=null&&pri==1){
                            priKeys+=name+",";
                        }
                        tf.setDes(String.valueOf((Integer)(fieldJson.get("des"))));
                        tf.setPar(String.valueOf((Integer)(fieldJson.get("par"))));

                        Integer tfd = (Integer)(fieldJson.get("tfd"));
                        if(tfd!=null&&tfd==1)
                            job.setDateField(name);

                        job.addField(tf);
                    }

                }
            }
            if(priKeys.endsWith(","))
                priKeys = priKeys.substring(0,priKeys.length()-1);
            job.setPrimaryKey(priKeys);
        }

    }
}
