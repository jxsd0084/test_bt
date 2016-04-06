package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.M99Fields;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface M99FieldsMapper extends ICrudMapper<M99Fields> {

    @Select("SELECT * FROM m99_fields WHERE m1_name=#{m1Name}")
    List<M99Fields> getM99FieldsByM1Name(@Param("m1Name") String m1Name);

    @Select("SELECT count(*) FROM m99_fields WHERE m1_name=#{m1Name}")
    int getM99FieldsCountByM1Name(@Param("m1Name") String m1Name);

}