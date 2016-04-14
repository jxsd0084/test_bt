package com.github.trace.service;

import com.github.trace.entity.JobSource;
import com.github.trace.mapper.JobSourceMapper;
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
    private JobSourceMapper jobSourceMapper;

    /**
     * 根据bizId查询 作业列表
     */
    public List<JobSource> getJobListByBizId(int id){
        return jobSourceMapper.findJobListByBizId(id);
    }

    /**
     * 根据Id查询 作业
     * @param id
     */
    public JobSource getJobById(int id) {
        return jobSourceMapper.findById(id);
    }

    /**
     * 根据Id更新 作业
     * @param jobSource
     * @return
     */
    public int updateJob(JobSource jobSource) {
        return jobSourceMapper.update(jobSource);
    }

    /**
     * 根据Id删除 作业
     * @param id
     * @return
     */
    public int deleteJobById(int id) {
        return jobSourceMapper.deleteById(id);
    }

    /**
     * 插入
     * @param jobSource
     */
    public int addJob(JobSource jobSource) {
        return jobSourceMapper.insert(jobSource);
    }

}








































