package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.JobSource;
import com.github.trace.entity.JobTarget;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JobTargetMapper extends ICrudMapper<JobTarget>{

    @Select("SELECT * FROM job_target WHERE biz_id=#{id}")
    List<JobTarget> findJobListByBizId(@Param("id") int id);

}
