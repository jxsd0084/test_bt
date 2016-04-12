package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.LevelOneFields;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LevelOneFieldsMapper extends ICrudMapper<LevelOneFields> {

    @Select("SELECT * FROM level_one_fields WHERE navigation_id=#{navigation_id}")
    List<LevelOneFields> queryByNavId(@Param("navigation_id") int navigation_id);
}
