package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.NavigationItem0;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wanghl on 2016/3/31.
 */
public interface NavigationItem0Mapper extends ICrudMapper< NavigationItem0 > {

	@Select( "SELECT * FROM navigation_item0 WHERE parent_id=#{parent_id}" )
	List< NavigationItem0 > findByParentId( @Param( "parent_id" ) int parent_id );

	@Select( "SELECT * FROM navigation_item0 WHERE name=#{name}" )
	NavigationItem0 findByName( @Param( "name" ) String name );

	@Select( "SELECT * FROM navigation_item0 WHERE item_type =#{itemType}" )
	List< NavigationItem0 > findByType( @Param( "itemType" ) int itemType );

}
