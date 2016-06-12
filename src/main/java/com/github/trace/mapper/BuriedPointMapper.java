package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.BuriedPoint;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BuriedPointMapper extends ICrudMapper< BuriedPoint > {

	@Select( "SELECT * FROM buried_point WHERE parent_id=#{parent_id} and child_id=#{child_id}" )
	List< BuriedPoint > findByBizIds( @Param( "parent_id" ) int parent_id, @Param( "child_id" ) int child_id );

	@Select( "SELECT bp.id,bp.bp_name,bp.bp_value,bp.bp_value_desc,bp.parent_id,bp.parent_name,bp.child_id,bp.child_name,bp.is_checked,bp.regex FROM buried_point as bp join navigation_item as nt on bp.parent_id = nt.parent_id and bp.child_id = nt.child_id WHERE nt.topic=#{topic}" )
	List< BuriedPoint > findByTopic( @Param( "topic" ) String topic );

	@Update( "Update buried_point set bp_name=#{bp_name},bp_value=#{bp_value},regex=#{regex},bp_value_desc=#{bp_value_desc},is_checked=#{is_checked} where id=#{id}" )
	boolean updateBuriedPoint( @Param( "bp_name" ) String bp_name, @Param( "bp_value" ) String bp_value, @Param( "regex" ) String regex, @Param( "bp_value_desc" ) String bp_value_desc, @Param( "is_checked" ) boolean is_checked, @Param( "id" ) int id );

	@Delete( "DELETE from buried_point where id=#{id}" )
	int deleteBuriedPoint( @Param( "id" ) int id );

}