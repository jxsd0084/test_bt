package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.M99Fields;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface M99FieldsMapper extends ICrudMapper< M99Fields > {

	@Select( "SELECT * FROM m99_fields WHERE level_two_id=#{m2Id} and status=1" )
	List< M99Fields > getM99FieldsByM1Id( @Param( "m2Id" ) int m2Id );

	@Select( "SELECT count(*) FROM m99_fields WHERE m1_name=#{m1Name}" )
	int getM99FieldsCountByM1Name( @Param( "m1Name" ) String m1Name );

	@Update( "UPDATE m99_fields SET m1_name=#{m1Name} WHERE level_one_id=#{m1Id}" )
	int updateM99FieldsByM1Id( @Param( "m1Name" ) String m1Name, @Param( "m1Id" ) int m1Id );

	@Select( "SELECT count(*) FROM m99_fields WHERE level_two_id = #{level_two_id} AND field_name = #{field_name}" )
	int queryByLevelTwoIdAndTag( @Param( "level_two_id" ) int level_two_id, @Param( "field_name" ) String field_name );

	@Select( "SELECT * FROM m99_fields WHERE level_two_id = #{level_two_id} and status=1" )
	List< M99Fields > queryByLevelTwoId( @Param( "level_two_id" ) int level_two_id );

}
