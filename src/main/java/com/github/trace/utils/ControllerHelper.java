package com.github.trace.utils;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.DatabaseBiz;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.service.CEPService;
import com.google.common.collect.ImmutableMap;

import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by chenlong on 2016/3/17.
 */
public class ControllerHelper {

	// 此处为防止页面刷新之后, 左边导航条的数据丢失
	public static List setLeftNavigationTree( Model model, CEPService cepService, String navFlag ) {

		List< DatabaseBiz > databaseBizList = cepService.getDataBaseBizList();
//        List<NavigationItem> navigationItemList = cepService.getConfiguration();
		List< NavigationItem0 > navigationItemList = cepService.getRootItem();
		model.addAttribute( "navigationItemList", navigationItemList );
		model.addAttribute( "databaseBizList", databaseBizList );
		model.addAttribute( "flag", navFlag );
		return navigationItemList;
	}

	/**
	 * 处理响应信息
	 *
	 * @param res
	 * @return
	 */
	public static Map returnResponseVal( int res, String msg ) {

		String result;
		if ( res > 0 ) {
			result = "数据" + msg + "成功!";
			return ImmutableMap.of( "code", 200, "info", result );
		} else {
			result = "数据" + msg + "失败！";
			return ImmutableMap.of( "code", -1, "info", result );
		}
	}

	/**
	 * 处理响应信息
	 *
	 * @param res
	 * @return
	 */
	public static Map returnResponseValue( int res, String msg ) {

		String result;
		if ( res > 0 ) {
			result = "数据操作成功!";
			return ImmutableMap.of( "code", 200, "info", result );
		} else {
			result = "数据操作失败！失败原因：" + msg;
			return ImmutableMap.of( "code", -1, "info", result );
		}
	}

	/**
	 * 验证数据重复性
	 *
	 * @param res
	 * @return
	 */
	public static Map returnResponseMsg( int res ) {

		if ( res == 0 ) {
			return ImmutableMap.of( "code", 200, "info", "该命名可以使用" );
		} else {
			return ImmutableMap.of( "code", -1, "info", "该命名已存在，请重新输入" );
		}
	}

	/**
	 * 转换为JSONArray
	 *
	 * @param list
	 * @return
	 */
	public static JSONArray convertToJSON( List list ) {

		JSONArray outerJSON = new JSONArray();
		for ( int i = 0; i < list.size(); i++ ) {
			JSONArray innerJSON = ReflectionUtil.convertToJSON( list.get( i ) );
			outerJSON.add( innerJSON );
		}
		return outerJSON;
	}

}
