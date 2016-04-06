package com.github.trace.service;

import com.github.trace.entity.JobConfig;
import com.github.trace.mapper.JobConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by chenlong on 2016/3/28.
 */
@Service
public class JobServer {

    private static final Logger LOG = LoggerFactory.getLogger( JobServer.class );

    @Autowired
    private JobConfigMapper jobConfigMapper;

    /**
     * 根据bizId查询 作业列表
     */
    public List<JobConfig> getJobListByBizId(int id){
        return jobConfigMapper.findJobListByBizId(id);
    }

    /**
     * 根据Id查询 作业
     * @param id
     */
    public JobConfig getJobById(int id) {
        return jobConfigMapper.findById(id);
    }

    /**
     * 根据Id更新 作业
     * @param jobConfig
     * @return
     */
    public int updateJob(JobConfig jobConfig) {
        return jobConfigMapper.update(jobConfig);
    }

    /**
     * 根据Id删除 作业
     * @param id
     * @return
     */
    public int deleteJobById(int id) {
        return jobConfigMapper.deleteById(id);
    }

    /**
     * 插入
     * @param jobConfig
     */
    public int addJob(JobConfig jobConfig) {
        return jobConfigMapper.insert(jobConfig);
    }

}








































