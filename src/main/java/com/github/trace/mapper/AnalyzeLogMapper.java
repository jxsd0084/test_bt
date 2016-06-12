package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import com.github.trace.entity.AnalyzeLogFields;

import java.util.List;

/**
 * Created by wangjiezhao on 2016/4/13.
 */

public interface AnalyzeLogMapper extends ICrudMapper< AnalyzeLogFields > {

	//@Select("SELECT * FROM level_two_fields WHERE level1_field_id=#{id}")
	@Select( "SELECT level1_field_tag as level_tag,GROUP_CONCAT(id) as tag_group FROM level_two_fields group by level1_field_tag" )
	@Results( {
			@Result( property = "level_tag", column = "level_tag" ),
			@Result( property = "tag_group", column = "tag_group" )
	} )
	//List<LevelTwoFields> setTagGroupInfo(@Param("id") int id);
	List< AnalyzeLogFields > getTagGroupInfo();


	@Select( "select  distinct  a.level1_field_tag as d_level_tag from level_two_fields a join m99_fields b on (a.level1_field_id=b.level_one_id and a.id=level_two_id)" )
	@Results( {
			@Result( property = "d_level_tag", column = "d_level_tag" )
	} )
	List< AnalyzeLogFields > getTagSet();


	@Select( "select a.bp_name,a.bp_value_desc,bp_value,is_checked,regex from buried_point0 a  join navigation_item0 b on(a.navigation_id=b.id and  b.item_type=1 and b.name=#{navName}) order by bp_name" )
	@Results( {
			@Result( property = "bp_name", column = "bp_name" ),
			@Result( property = "bp_value_desc", column = "bp_value_desc" ),
			@Result( property = "bp_value", column = "bp_value" ),
			@Result( property = "is_checked", column = "is_checked" ),
			@Result( property = "regex", column = "regex" )
	} )
	List< AnalyzeLogFields > getBuriedInfoByBusi( @Param( "navName" ) String navName );


	@Select( "select  a.level1_field_tag as tag,b.field_name,b.field_desc,b.field_type,b.field_regex from level_two_fields a left join m99_fields b on (a.level1_field_id=b.level_one_id and a.id=level_two_id)" )
	@Results( {
			@Result( property = "tag", column = "tag" ),
			@Result( property = "field_name", column = "field_name" ),
			@Result( property = "field_desc", column = "field_desc" ),
			@Result( property = "field_type", column = "field_type" ),
			@Result( property = "field_regex", column = "field_regex" ),

	} )
	List< AnalyzeLogFields > getBuriedTwoMap();


	@Select( "select name,topic from navigation_item0 where item_type=1 and name=#{navName}" )
	@Results( {
			@Result( property = "nav_name", column = "nav_name" ),
			@Result( property = "topic", column = "topic" ),
	} )
	List< AnalyzeLogFields > getTopicByNavName( @Param( "navName" ) String navName );

}
