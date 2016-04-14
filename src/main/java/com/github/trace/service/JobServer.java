package com.github.trace.service;

import com.github.trace.entity.JobSource;
import com.github.trace.entity.JobTarget;
import com.github.trace.mapper.JobSourceMapper;
import com.github.trace.mapper.JobTargetMapper;
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

    @Autowired
    private JobTargetMapper jobTargetMapper;

    /**
     * 根据bizId查询 作业_源列表
     */
    public List<JobSource> getJobSouListByBizId(int id){
        return jobSourceMapper.findJobListByBizId(id);
    }

    /**
     * 根据Id查询 作业_源
     * @param id
     */
    public JobSource getJobSouById(int id) {
        return jobSourceMapper.findById(id);
    }

    /**
     * 根据Id更新 作业_源
     * @param jobSource
     * @return
     */
    public int updateJobSou(JobSource jobSource) {
        return jobSourceMapper.update(jobSource);
    }

    /**
     * 根据Id删除 作业_源
     * @param id
     * @return
     */
    public int deleteJobSouById(int id) {
        return jobSourceMapper.deleteById(id);
    }

    /**
     * 插入 作业_源
     * @param jobSource
     */
    public int addJobSou(JobSource jobSource) {
        return jobSourceMapper.insert(jobSource);
    }


    /**
     * 根据bizId查询 作业_目标列表
     */
    public List<JobTarget> getJobTarListByBizId(int id){
        return jobTargetMapper.findJobListByBizId(id);
    }

    /**
     * 根据Id查询 作业_目标
     * @param id
     */
    public JobTarget getJobTarById(int id) {
        return jobTargetMapper.findById(id);
    }

    /**
     * 根据Id更新 作业_目标
     * @param jobTarget
     * @return
     */
    public int updateJobTar(JobTarget jobTarget) {
        return jobTargetMapper.update(jobTarget);
    }

    /**
     * 根据Id删除 作业_目标
     * @param id
     * @return
     */
    public int deleteJobTarById(int id) {
        return jobTargetMapper.deleteById(id);
    }

    /**
     * 插入 作业_目标
     * @param jobTarget
     */
    public int addJobTar(JobTarget jobTarget) {
        return jobTargetMapper.insert(jobTarget);
    }
}








































