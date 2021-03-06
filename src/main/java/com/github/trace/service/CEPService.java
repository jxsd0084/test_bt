package com.github.trace.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.trace.entity.*;
import com.github.trace.mapper.BuriedPoint0Mapper;
import com.github.trace.mapper.BuriedPointMapper;
import com.github.trace.mapper.DatabaseBizMapper;
import com.github.trace.mapper.NavigationItemMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CEP 后台埋点配置管理
 * Created by wujing on 2016/03/11.
 */
@Service
public class CEPService {

	private static final Logger LOG = LoggerFactory.getLogger( CEPService.class );

	@Autowired
	private BuriedPointMapper  buriedPointMapper;
	@Autowired
	private BuriedPoint0Mapper buriedPoint0Mapper;

	@Autowired
	private NavigationItemMapper navigationItemMapper;

	@Autowired
	private Navigation0Service navigation0Service;

	@Autowired
	private DatabaseBizMapper dataBaseBizMapper;

	@Autowired
	private KafkaService kafkaService;

	/**
	 * 查询 数据源 导航项
	 */
	public List< DatabaseBiz > getDataBaseBizList() {

		return dataBaseBizMapper.findAll();
	}

	/**
	 * 获取所有的导航项列表
	 *
	 * @return 埋点列表
	 */
//  @Cacheable(value="navigationItemCache")
	public List< NavigationItem > getConfiguration() {

		return navigationItemMapper.findAll();
	}

	public List< NavigationItem0 > getRootItem() {

		return navigation0Service.queryAll();
	}

	public NavigationItem0 getNavigationItem0ByName( String name ) {

		return navigation0Service.queryByName( name );
	}

	/**
	 * 根据id查询具体的导航项
	 *
	 * @param id
	 * @return
	 */
	public NavigationItem getConfigById( int id ) {

		return navigationItemMapper.findById( id );
	}

	/**
	 * 更新导航项
	 *
	 * @param navigationItem
	 * @return
	 */
	public int updateConfig( NavigationItem navigationItem ) {

		return navigationItemMapper.update( navigationItem );
	}

	/**
	 * 获取埋点列表
	 *
	 * @param parent_id 父业务ID
	 * @param child_id  子业务ID
	 * @return 埋点列表
	 */
	public List< BuriedPoint > getBuriedPointList( int parent_id, int child_id ) {

		return buriedPointMapper.findByBizIds( parent_id, child_id );
	}

	/**
	 * 获取埋点列表
	 *
	 * @return 埋点列表
	 */
	public List< BuriedPoint0 > getBuriedPoint0List( int navigationId ) {

		return buriedPoint0Mapper.findByBizIds( navigationId );
	}

	/**
	 * 删除埋点
	 *
	 * @param id 业务ID
	 * @return 1-删除成功  0-删除失败
	 */
	public int deleteBuriedPoint( int id ) {

		return buriedPointMapper.deleteBuriedPoint( id );
	}

	/**
	 * 删除埋点2
	 *
	 * @param id 业务ID
	 * @return 1-删除成功  0-删除失败
	 */
	public int deleteById( int id ) {

		return buriedPoint0Mapper.deleteById( id );
	}


	/**
	 * 插入新埋点
	 *
	 * @param buriedPoint
	 * @return
	 */
	public int addBuriedPoint( BuriedPoint buriedPoint ) {

		return buriedPointMapper.insert( buriedPoint );
	}

	/**
	 * 插入新埋点
	 *
	 * @param buriedPoint
	 * @return
	 */
	public int addBuriedPoint0( BuriedPoint0 buriedPoint ) {

		return buriedPoint0Mapper.insert( buriedPoint );
	}

	public BuriedPoint getBuriedPoint( int id ) {

		return buriedPointMapper.findById( id );
	}

	public BuriedPoint0 getBuriedPoint0( int id ) {

		return buriedPoint0Mapper.findById( id );
	}


//    /** 修改埋点信息
//     *
//      * @param bp_name
//     * @param bp_value
//     * @param regex
//     * @param bp_value_desc
//     * @param is_checked
//     * @param id
//     * @return
//     */
//  public boolean modifyBuriedPoint(String bp_name,String bp_value,String regex,String bp_value_desc,boolean is_checked ,int id) {
//    return buriedPointMapper.updateBuriedPoint(bp_name,bp_value,regex,bp_value_desc,is_checked,id);
//  }

	public int modifyBuriedPoint( BuriedPoint buriedPoint ) {

		return buriedPointMapper.update( buriedPoint );
	}

	public int modifyBuriedPoint0( BuriedPoint0 buriedPoint ) {

		return buriedPoint0Mapper.update( buriedPoint );
	}


	public Set< String > getServerLog( String str1, int st2 ) {
		//return kafkaService.getMessages("nginx.reverse","type",1);
		return kafkaService.getMessages( str1, st2 );
	}

	public boolean compareByTopic( String topic, String json ) {

		boolean             result       = true;
		List< BuriedPoint > buriedPoints = buriedPointMapper.findByTopic( topic );
		JSONObject          jsonObject   = new JSONObject();

		for ( BuriedPoint br : buriedPoints ) {
			jsonObject.put( br.getBpName(), br.getBpValueDesc() + "," + br.getBpValue() + "," + br.getRegex() );
		}

		JSONArray jsonArray = JSON.parseArray( json );
		JSONArray ja1       = new JSONArray();
		LinkedHashMap< String, String > jsonMap1 = JSON.parseObject( jsonObject.toString(),
				new TypeReference< LinkedHashMap< String, String > >() {
				} );
		for ( Map.Entry< String, String > entry1 : jsonMap1.entrySet() ) {
			JSONArray ja2 = new JSONArray();
			ja2.add( entry1.getKey() + "" );
			ja2.add( entry1.getValue().split( "," )[ 0 ] + "" );
			for ( int i = 0; i < jsonArray.size(); i++ ) {
				LinkedHashMap< String, String > jsonMap2 = JSON.parseObject( jsonArray.get( i ).toString(),
						new TypeReference< LinkedHashMap< String, String > >() {
						} );

				if ( jsonMap2.containsKey( entry1.getKey() ) ) {
					String patternString  = "";
					String patternString2 = "";

					String rule = entry1.getValue().split( "," )[ 1 ];
					if ( StringUtils.equals( rule, "文本" ) ) {
						patternString = ".*";
					}
					if ( StringUtils.equals( rule, "数字" ) ) {
						patternString = "^[0-9]*$";
					}
					if ( StringUtils.equals( rule, "日期" ) ) {
						patternString = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";
					}

					Pattern pattern = Pattern.compile( patternString );
					Matcher matcher = pattern.matcher( jsonMap2.get( entry1.getKey() ) );
					result = matcher.matches();

					if ( entry1.getValue().split( "," ).length >= 3 ) {
						try {
							patternString2 = URLDecoder.decode( entry1.getValue().split( "," )[ 2 ].toString(), "UTF-8" );
						} catch ( UnsupportedEncodingException e ) {
							LOG.error( "Cannot decode {} to UTF8", entry1.getValue().split( "," )[ 2 ].toString(), e );
						}
						Pattern pattern2 = Pattern.compile( patternString2 );
						Matcher matcher2 = pattern2.matcher( jsonMap2.get( entry1.getKey() ) );
						result = matcher2.matches();
					}
					ja2.add( jsonMap2.get( entry1.getKey() ) );
					ja2.add( result );
				} else {
					ja2.add( "" );
					ja2.add( false );
					result = false;
				}
				if ( !result ) {
					return result;
				}
			}
			ja1.add( ja2 );
		}
		return result;
	}

}
