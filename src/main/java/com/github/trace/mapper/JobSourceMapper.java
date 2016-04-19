package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.JobSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JobSourceMapper extends ICrudMapper<JobSource>{

    @Select("SELECT * FROM job_source WHERE biz_id=#{id}")
    List<JobSource> findJobListByBizId(@Param("id") int id);

}
