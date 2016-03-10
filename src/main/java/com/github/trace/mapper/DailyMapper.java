package com.github.trace.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.github.trace.entity.Daily;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 存取日报
 * Created by hanmz on 2015/12/21.
 */
public interface DailyMapper extends ICrudMapper<Daily> {
  @Select("SELECT data FROM daily WHERE day=#{day}")
  String findByDay(@Param("day") String day);

  @Insert("INSERT INTO `daily` (`day`,`data`) VALUES(#{day},#{data})")
  void insertDaily(@Param("day") String day, @Param("data") String data);
}
