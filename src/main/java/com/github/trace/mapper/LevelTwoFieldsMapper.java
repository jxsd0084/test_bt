package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.LevelTwoFields;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LevelTwoFieldsMapper extends ICrudMapper<LevelTwoFields> {

    @Select("SELECT * FROM level_two_fields WHERE level1_field_id=#{id} and status=1")
    List<LevelTwoFields> findLevelTwoFieldsListById(@Param("id") int id);

    @Select("SELECT count(*) FROM level_two_fields WHERE level1_field_id = #{level1_field_id} AND level1_field_tag = #{level1_field_tag}")
    int queryBylevelOneIdAndTag(@Param("level1_field_id") int level1_field_id,@Param("level1_field_tag") String level1_field_tag);

    @Select("SELECT count(*) FROM level_two_fields WHERE level1_field_tag = #{level1_field_tag}")
    int queryByTag(@Param("level1_field_tag") String level1_field_tag);

    @Select("SELECT L2.id,L2.level1_field_id,L2.level1_field_name,L2.level1_field_tag,L2.level2_field_desc,L2.level2_field_name from level_one_fields as L1 INNER JOIN level_two_fields as L2 ON L1.id = L2.level1_field_id WHERE L1.navigation_id= #{navigation_id}")
    List<LevelTwoFields> queryAll(@Param("navigation_id") int navigation_id);

    @Select("SELECT * FROM level_two_fields WHERE level1_field_id = #{level1_field_id} and status=1")
    List<LevelTwoFields> queryBylevelOneId(@Param("level1_field_id") int level1_field_id);

}
