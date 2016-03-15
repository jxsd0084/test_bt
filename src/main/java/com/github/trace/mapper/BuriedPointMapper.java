package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.BuriedPoint;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BuriedPointMapper extends ICrudMapper<BuriedPoint> {

  @Select("SELECT * FROM buried_point WHERE parent_id=#{parent_id} and child_id=#{child_id}")
  List<BuriedPoint> findByBizIds(@Param("parent_id") int parent_id, @Param("child_id") int child_id);

  @Delete("DELETE FROM buried_point WHERE parent_id=#{parent_id} and child_id=#{child_id}")
  int deleteByBizIds(@Param("parent_id") int parent_id, @Param("child_id") int child_id);

}