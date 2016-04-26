package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.LevelOneFields;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LevelOneFieldsMapper extends ICrudMapper<LevelOneFields> {

    @Select("SELECT * FROM level_one_fields WHERE navigation_id=#{navigation_id}")
    List<LevelOneFields> queryByNavId(@Param("navigation_id") int navigation_id);
    @Select("SELECT count(*) FROM level_one_fields WHERE navigation_id = #{navigation_id} AND level1_field_tag = #{level1_field_tag}")
    int queryByNavIdAndTag(@Param("navigation_id") int navigation_id,@Param("level1_field_tag") String level1_field_tag);
}
