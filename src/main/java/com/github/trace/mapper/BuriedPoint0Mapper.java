package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.BuriedPoint;
import com.github.trace.entity.BuriedPoint0;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wanghl on 2016/4/1.
 */
public interface BuriedPoint0Mapper extends ICrudMapper<BuriedPoint0>{

    @Select("SELECT * FROM buried_point0 WHERE navigation_id=#{navigation_id}")
    List<BuriedPoint> findByBizIds(@Param("navigation_id") int navigationId);

    @Select("SELECT bp.id,bp.bp_name,bp.bp_value,bp.bp_value_desc,bp.is_checked,bp.regex FROM buried_point0 as bp join navigation_item0 as nt on bp.navigation_id = nt.id  WHERE nt.topic=#{topic}")
    List<BuriedPoint0> findByTopic(@Param("topic") String topic);

}
