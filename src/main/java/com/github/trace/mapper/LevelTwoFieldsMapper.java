package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.LevelTwoFields;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LevelTwoFieldsMapper extends ICrudMapper<LevelTwoFields> {

    @Select("SELECT * FROM level_two_fields WHERE level1_field_id=#{id}")
    List<LevelTwoFields> findLevelTwoFieldsListById(@Param("id") int id);

    @Select("SELECT count(*) FROM level_two_fields WHERE level1_field_id = #{level1_field_id} AND level1_field_tag = #{level1_field_tag}")
    int queryBylevelOneIdAndTag(@Param("level1_field_id") int level1_field_id,@Param("level1_field_tag") String level1_field_tag);

}
