package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.M99Fields;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface M99FieldsMapper extends ICrudMapper<M99Fields> {

    @Select("SELECT * FROM m99_fields WHERE level_two_id=#{m1Id}")
    List<M99Fields> getM99FieldsByM1Id(@Param("m1Id") int m1Id);

    @Select("SELECT count(*) FROM m99_fields WHERE m1_name=#{m1Name}")
    int getM99FieldsCountByM1Name(@Param("m1Name") String m1Name);

    @Update("UPDATE m99_fields SET m1_name=#{m1Name} WHERE m1_id=#{id}")
    int updateM99FieldsByM1Name(@Param("m1Name") String m1Name, @Param("id") int id);

}