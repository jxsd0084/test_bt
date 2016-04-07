package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.JobConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JobConfigMapper extends ICrudMapper<JobConfig>{

    @Select("SELECT * FROM job_config WHERE biz_id=#{id}")
    List<JobConfig> findJobListByBizId(@Param("id") int id);

}
