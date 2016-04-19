package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.JobInfo;
import com.github.trace.entity.JobTarget;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JobInfoMapper extends ICrudMapper<JobInfo>{

    @Select("SELECT * FROM job_info WHERE biz_id=#{id}")
    List<JobInfo> findJobInfoListByBizId(@Param("id") int id);

}
