package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.DatabaseInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DatabaseInfoMapper extends ICrudMapper< DatabaseInfo > {

	@Select( "SELECT * FROM database_info WHERE biz_id=#{id}" )
	List< DatabaseInfo > findDatabaseInfoListById( @Param( "id" ) int id );

}
