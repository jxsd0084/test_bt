package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.trace.entity.LevelOneFields;
import com.github.trace.entity.LevelTwoFields;
import com.github.trace.entity.M99Fields;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataTypeService;
import com.github.trace.utils.ControllerHelper;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by chenlong on 2016/3/17.
 */
@Controller
@RequestMapping( "/dataType" )
public class DataTypeController {

	private final static Logger LOGGER = LoggerFactory.getLogger( DataTypeController.class );

	@Autowired
	private CEPService      cepService;
	@Autowired
	private DataTypeService dataTypeService;

	@RequestMapping( "/listM99" )
	public String listM99( @RequestParam( name = "L1_id" ) int l1_id,
	                       @RequestParam( name = "L1_tag" ) String l1_tag,
	                       @RequestParam( name = "L1_name" ) String l1_name,
	                       @RequestParam( name = "L2_id" ) int l2_id,
	                       @RequestParam( name = "navigationId" ) int navigationId,
	                       @RequestParam( name = "navigationName" ) String navigationName,
	                       @RequestParam( name = "L2_tag" ) String l2_tag,
	                       Model model ) {

		JSONArray jsonArray = getM99FieldsList( l2_id );
		ControllerHelper.setLeftNavigationTree( model, cepService, "" );
		model.addAttribute( "data", jsonArray );
		model.addAttribute( "L1_id", l1_id );
		model.addAttribute( "L1_tag", l1_tag );
		model.addAttribute( "L2_id", l2_id );
		model.addAttribute( "L2_tag", l2_tag );
		model.addAttribute( "L1_name", l1_name );
		model.addAttribute( "navigation_id", navigationId );
		model.addAttribute( "navigation_name", navigationName );
//        model.addAttribute("id", id);
		return "data/m99_list";
	}

	@RequestMapping( "/newM99" )
	public String newM99( @RequestParam( name = "L1_id" ) int l1_id,
	                      @RequestParam( name = "L1_tag" ) String l1_tag,
	                      @RequestParam( name = "L1_name" ) String l1_name,
	                      @RequestParam( name = "L2_id" ) int l2_id,
	                      @RequestParam( name = "L2_tag" ) String l2_tag,
	                      @RequestParam( name = "tag" ) String tag,
	                      @RequestParam( name = "navigationId" ) int navigationId,
	                      @RequestParam( name = "navigationName" ) String navigationName,
	                      Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "" ); // 左边导航条

		model.addAttribute( "L1_id", l1_id );
		model.addAttribute( "L1_tag", l1_tag );
		model.addAttribute( "L1_name", l1_name );
		model.addAttribute( "L2_id", l2_id );
		model.addAttribute( "L2_tag", l2_tag );
		model.addAttribute( "tag", tag );
		model.addAttribute( "navigation_id", navigationId );
		model.addAttribute( "navigation_name", navigationName );

		return "data/m99_edit";
	}

	@RequestMapping( "/editM99" )
	public String editM99( @RequestParam( name = "L3_id" ) int l3_id,
	                       @RequestParam( name = "L1_id" ) int l1_id,
	                       @RequestParam( name = "L1_tag" ) String l1_tag,
	                       @RequestParam( name = "L1_name" ) String l1_name,
	                       @RequestParam( name = "L2_id" ) String l2_id,
	                       @RequestParam( name = "L2_tag" ) String l2_tag,
	                       @RequestParam( name = "tag" ) String tag,
	                       @RequestParam( name = "navigationId" ) int navigationId,
	                       @RequestParam( name = "navigationName" ) String navigationName,
	                       Model model ) {

		M99Fields m99 = dataTypeService.getM99FieldsById( l3_id );
		model.addAttribute( "obj", m99 );

		ControllerHelper.setLeftNavigationTree( model, cepService, "" ); // 左边导航条

		model.addAttribute( "id", l3_id );
		model.addAttribute( "L1_id", l1_id );
		model.addAttribute( "L1_tag", l1_tag );
		model.addAttribute( "L1_name", l1_name );
		model.addAttribute( "L2_id", l2_id );
		model.addAttribute( "L2_tag", l2_tag );
		model.addAttribute( "tag", tag );
		model.addAttribute( "navigation_id", navigationId );
		model.addAttribute( "navigation_name", navigationName );
		return "data/m99_edit";
	}

	@RequestMapping( value = "/modifyM99", method = RequestMethod.POST )
	@ResponseBody
	public Map modifyM99( @RequestBody JSONObject requestJson ) {

		M99Fields m99Fields = new M99Fields();
		m99Fields.setId( requestJson.getInteger( "L3_id" ) );
		m99Fields.setLevelOneId( requestJson.getInteger( "L1_id" ) );
		m99Fields.setM1Name( requestJson.getString( "L1_tag" ) );
		m99Fields.setLevelTwoId( requestJson.getInteger( "L2_id" ) );
		m99Fields.setFieldName( requestJson.getString( "F1_name" ) );
		m99Fields.setFieldDesc( requestJson.getString( "F1_desc" ) );
		m99Fields.setFieldType( requestJson.getString( "F1_type" ) );
		m99Fields.setFieldRegex( requestJson.getString( "F1_regx" ) );
		m99Fields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
		int res = dataTypeService.updateM99Fields( m99Fields );

		return ControllerHelper.returnResponseVal( res, "更新" );

	}

	@RequestMapping( value = "/addM99", method = RequestMethod.POST )
	@ResponseBody
	public Map addM99( @RequestBody JSONObject requestJson ) {

		M99Fields m99Fields = new M99Fields();
		m99Fields.setLevelOneId( requestJson.getInteger( "L1_id" ) );
		m99Fields.setM1Name( requestJson.getString( "L1_tag" ) );
		m99Fields.setLevelTwoId( requestJson.getInteger( "L2_id" ) );
		m99Fields.setFieldName( requestJson.getString( "F1_name" ) );
		m99Fields.setFieldDesc( requestJson.getString( "F1_desc" ) );
		m99Fields.setFieldType( requestJson.getString( "F1_type" ) );
		m99Fields.setFieldRegex( requestJson.getString( "F1_regx" ) );
		m99Fields.setCreator( SecurityUtils.getSubject().getPrincipal().toString() );
		m99Fields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
		int res = dataTypeService.addM99Fields( m99Fields );

		return ControllerHelper.returnResponseVal( res, "更新" );

	}

	@RequestMapping( "/deleteM99" )
	@ResponseBody
	public Map deleteM99( @RequestParam( "L3_id" ) int l3_id,
	                      @RequestParam( "L1_id" ) int l1_id,
	                      @RequestParam( "L1_tag" ) String l1_tag,
	                      @RequestParam( "L1_name" ) String l1_name,
	                      @RequestParam( "L2_id" ) int l2_id,
	                      @RequestParam( "L2_tag" ) String l2_tag,
	                      Model model ) {

//        int res = dataTypeService.deleteM99Fields(l3_id);
		M99Fields m99Fields = new M99Fields();
		m99Fields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
		m99Fields.setStatus( 0 );
		m99Fields.setId( l3_id );
		int res = dataTypeService.updateM99Fields( m99Fields );
		model.addAttribute( "L1_id", l1_id );
		model.addAttribute( "L1_tag", l1_tag );
		model.addAttribute( "L1_name", l1_name );
		model.addAttribute( "L2_id", l2_id );
		model.addAttribute( "L2_tag", l2_tag );

		return ControllerHelper.returnResponseVal( res, "删除" );
	}

	@RequestMapping( "/listLevelOne" )
	public String listLevelOneFields( Model model ) {

		JSONArray jsonArray = getLevelOneFieldList( 11 );
		model.addAttribute( "data", jsonArray );
		ControllerHelper.setLeftNavigationTree( model, cepService, "" );
		return "data/data_list";
	}

	@RequestMapping( "/listByNavId" )
	public String listLevelOneByNavId( @RequestParam( name = "navigationId" ) int navigationId,
	                                   @RequestParam( name = "navigationName" ) String navigationName,
	                                   Model model ) {

		JSONArray jsonArray = getLevelOneFieldList( navigationId );
		model.addAttribute( "data", jsonArray );
		model.addAttribute( "navigation_id", navigationId );
		model.addAttribute( "navigation_name", navigationName );
		ControllerHelper.setLeftNavigationTree( model, cepService, "" );
		return "data/data_list";
	}

	@RequestMapping( "/listLevelTwo" )
	public String listLevelTwoFields( @RequestParam( name = "L1_id" ) int l1_id,
	                                  @RequestParam( name = "navigationId" ) int navigationId,
	                                  @RequestParam( name = "navigationName" ) String navigationName,
	                                  @RequestParam( name = "L1_tag" ) String l1_tag,
	                                  @RequestParam( name = "L1_name" ) String l1_name,
	                                  Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "" );

		JSONArray jsonArray = getLevelTwoFieldList( l1_id );

		model.addAttribute( "data", jsonArray );
		model.addAttribute( "L1_id", l1_id );
		model.addAttribute( "L1_tag", l1_tag );
		model.addAttribute( "L1_name", l1_name );
		model.addAttribute( "navigation_id", navigationId );
		model.addAttribute( "navigation_name", navigationName );
		return "data/data_list_2";
	}

	@RequestMapping( "/editLevelOne" )
	public String editLevelOne( @RequestParam( name = "L1_id" ) int l1_id,
	                            @RequestParam( name = "tag" ) String tag,
	                            @RequestParam( name = "navigationId" ) int navigationId,
	                            @RequestParam( name = "navigationName" ) String navigationName,
	                            Model model ) {

		LevelOneFields fieldObj = dataTypeService.getLevelOneFieldById( l1_id );
		model.addAttribute( "obj", fieldObj );

		ControllerHelper.setLeftNavigationTree( model, cepService, "" ); // 左边导航条

		model.addAttribute( "id", l1_id );
		model.addAttribute( "tag", tag );
		model.addAttribute( "navigation_id", navigationId );
		model.addAttribute( "navigation_name", navigationName );
		return "data/data_edit";
	}

	@RequestMapping( "/editLevelTwo" )
	public String editLevelTwo( @RequestParam( name = "id" ) int id,
	                            @RequestParam( name = "tag" ) String tag,
	                            @RequestParam( name = "navigationId" ) int navigationId,
	                            @RequestParam( name = "navigationName" ) String navigationName,
	                            @RequestParam( name = "L1_id" ) String l1_id,
	                            @RequestParam( name = "L1_tag" ) String l1_tag,
	                            @RequestParam( name = "L1_name" ) String l1_name,
	                            Model model ) {

		LevelTwoFields fieldObj = dataTypeService.getLevelTwoFieldById( id );
		model.addAttribute( "obj", fieldObj );

		ControllerHelper.setLeftNavigationTree( model, cepService, "" ); // 左边导航条

		model.addAttribute( "id", id );
		model.addAttribute( "tag", tag );
		model.addAttribute( "L1_id", l1_id );
		model.addAttribute( "L1_tag", l1_tag );
		model.addAttribute( "L1_name", l1_name );
		model.addAttribute( "navigation_id", navigationId );
		model.addAttribute( "navigation_name", navigationName );
		return "data/data_edit_2";
	}

	@RequestMapping( "/new" )
	public String newLevel( @RequestParam( name = "tag" ) String tag,
	                        @RequestParam( name = "lev" ) int lev,
	                        @RequestParam( name = "L1_id", required = false ) String l1_id,
	                        @RequestParam( name = "L1_tag", required = false ) String l1_tag,
	                        @RequestParam( name = "navigationId" ) int navigationId,
	                        @RequestParam( name = "navigationName" ) String navigationName,
	                        @RequestParam( name = "L1_name", required = false ) String l1_name,
	                        Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "" ); // 左边导航条
		model.addAttribute( "tag", tag );
		model.addAttribute( "navigation_id", navigationId );
		model.addAttribute( "navigation_name", navigationName );
		if ( lev == 1 ) {
			return "data/data_edit";
		} else {
			model.addAttribute( "L1_id", l1_id );
			model.addAttribute( "L1_tag", l1_tag );
			model.addAttribute( "L1_name", l1_name );
			return "data/data_edit_2";
		}
	}

	@RequestMapping( value = "/modifyLevelOne", method = RequestMethod.POST )
	@ResponseBody
	public Map modifyLevelOne( @RequestBody JSONObject requestJson ) {

		LevelOneFields levelOneFields = new LevelOneFields();
		levelOneFields.setId( requestJson.getInteger( "id" ) );
		levelOneFields.setLevel1FieldTag( requestJson.getString( "L1_tag" ) );
		levelOneFields.setLevel1FieldName( requestJson.getString( "L1_name" ) );
		levelOneFields.setLevel1FieldDesc( requestJson.getString( "L1_desc" ) );
		levelOneFields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
		//      levelOneFields.setModifier(requestJson.getString("modifier"));

		int res = dataTypeService.updateFieldsByCascade( levelOneFields );

		return ControllerHelper.returnResponseVal( res, "更新" );
	}

	@RequestMapping( value = "/modifyLevelTwo", method = RequestMethod.POST )
	@ResponseBody
	public Map modifyLevelTwo( @RequestBody JSONObject requestJson ) {

		LevelTwoFields levelTwoFields = new LevelTwoFields();
		levelTwoFields.setId( requestJson.getInteger( "L2_id" ) );
		levelTwoFields.setLevel1FieldTag( requestJson.getString( "L1_tag" ) );
		levelTwoFields.setLevel1FieldName( requestJson.getString( "L1_name" ) );
		levelTwoFields.setLevel2FieldName( requestJson.getString( "L2_name" ) );
		levelTwoFields.setLevel2FieldDesc( requestJson.getString( "L2_desc" ) );
		levelTwoFields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
		LOGGER.info( "修改数据" + levelTwoFields.toString() );
		int res = dataTypeService.updateLevelTwo( levelTwoFields );

		return ControllerHelper.returnResponseVal( res, "更新" );

	}

	@RequestMapping( value = "/addLevelOne", method = RequestMethod.POST )
	@ResponseBody
	public Map addLevelOne( @RequestBody JSONObject requestJson ) {

		LevelOneFields levelOneFields = new LevelOneFields();
		levelOneFields.setLevel1FieldTag( requestJson.getString( "L1_tag" ) );
		levelOneFields.setLevel1FieldName( requestJson.getString( "L1_name" ) );
		levelOneFields.setNavigationId( requestJson.getInteger( "navigationId" ) );
		levelOneFields.setLevel1FieldDesc( requestJson.getString( "L1_desc" ) );
		levelOneFields.setCreator( SecurityUtils.getSubject().getPrincipal().toString() );
		levelOneFields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
		int res = dataTypeService.addLevelOneFields( levelOneFields );

		return ControllerHelper.returnResponseVal( res, "添加" );

	}

	@RequestMapping( value = "/addLevelTwo", method = RequestMethod.POST )
	@ResponseBody
	public Map addLevelTwo( @RequestBody JSONObject requestJson ) {

		LevelTwoFields levelTwoFields = new LevelTwoFields();
		levelTwoFields.setLevel1FieldId( requestJson.getInteger( "L1_id" ) );
		levelTwoFields.setLevel1FieldTag( requestJson.getString( "L1_tag" ) );
		levelTwoFields.setLevel1FieldName( requestJson.getString( "L1_name" ) );
		levelTwoFields.setLevel2FieldName( requestJson.getString( "L2_name" ) );
		levelTwoFields.setLevel2FieldDesc( requestJson.getString( "L2_desc" ) );
		levelTwoFields.setCreator( SecurityUtils.getSubject().getPrincipal().toString() );
		levelTwoFields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
		int res = dataTypeService.addLevelTwoFields( levelTwoFields );

		return ControllerHelper.returnResponseVal( res, "添加" );

	}

	@RequestMapping( "validateLevelOne" )
	public
	@ResponseBody
	Map validateLevelOne( @RequestParam( "navigationId" ) int navigationId,
	                      @RequestParam( "levelOneTag" ) String levelOneTag ) {

		int res = dataTypeService.validateLevelOne( navigationId, levelOneTag );
		return ControllerHelper.returnResponseMsg( res );
	}

	@RequestMapping( "validateLevelTwo" )
	public
	@ResponseBody
	Map validateLevelTwo( @RequestParam( "levelOneId" ) int levelOneId,
	                      @RequestParam( "levelTwoTag" ) String levelTwoTag ) {

		int res = dataTypeService.validateLeveTwo( levelTwoTag );
		return ControllerHelper.returnResponseMsg( res );
	}

	@RequestMapping( "validateM99" )
	public
	@ResponseBody
	Map validateM99( @RequestParam( "levelTwoId" ) int levelTwoId,
	                 @RequestParam( "fieldName" ) String fieldName ) {

		int res = dataTypeService.validateM99( levelTwoId, fieldName );
		return ControllerHelper.returnResponseMsg( res );
	}

	@RequestMapping( "deleteLevelOne" )
	public
	@ResponseBody
	Map deleteLevelOne( @RequestParam( "id" ) int id ) {

		int res = 0;
		if ( dataTypeService.queryLevelTwoByLevelOneId( id ) ) {
			LevelOneFields levelOneFields = new LevelOneFields();
			levelOneFields.setId( id );
			levelOneFields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
			levelOneFields.setStatus( 0 );
			res = dataTypeService.updateLevelOne( levelOneFields );
			return ControllerHelper.returnResponseVal( res, "删除" );
		} else {
			return ControllerHelper.returnResponseValue( res, "该事件下含有二级事件，请先删除二级事件" );
		}
	}

	@RequestMapping( "deleteLevelTwo" )
	public
	@ResponseBody
	Map deleteLevelTwo( @RequestParam( "id" ) int id ) {

		int res = 0;
		if ( dataTypeService.queryM99ByLevelTwoId( id ) ) {
			LevelTwoFields levelTwoFields = new LevelTwoFields();
			levelTwoFields.setId( id );
			levelTwoFields.setModifier( SecurityUtils.getSubject().getPrincipal().toString() );
			levelTwoFields.setStatus( 0 );
			res = dataTypeService.updateLevelTwo( levelTwoFields );
			return ControllerHelper.returnResponseVal( res, "删除" );
		} else {
			return ControllerHelper.returnResponseValue( res, "该事件下含有属性事件，请先删除属性事件" );
		}
	}

	/**
	 * 一级字段列表
	 *
	 * @return
	 */
	public JSONArray getLevelOneFieldList( int navigationId ) {

		List< LevelOneFields > list       = dataTypeService.queryLevelOneByNavId( navigationId );
		JSONArray              jsonArray1 = new JSONArray();
		for ( LevelOneFields levelOneFields : list ) {
			JSONArray jsonArray2 = new JSONArray();
			String    tagName    = levelOneFields.getLevel1FieldTag();
			jsonArray2.add( tagName );                                    // 标识 样例:AV
			jsonArray2.add( levelOneFields.getLevel1FieldName() );        // 名称 样例:音视频
			jsonArray2.add( levelOneFields.getLevel1FieldDesc() );        // 描述
			jsonArray2.add( levelOneFields.getModifier() );
			jsonArray2.add( levelOneFields.getId() );                     // 编号 不展示, 以免混淆

			jsonArray1.add( jsonArray2 );
		}
		return jsonArray1;
	}

	/**
	 * 二级字段列表
	 *
	 * @param l1_id
	 * @return
	 */
	private JSONArray getLevelTwoFieldList( int l1_id ) {

		List< LevelTwoFields > list       = dataTypeService.getLevelTwoFieldList( l1_id );
		JSONArray              jsonArray1 = new JSONArray();
		for ( LevelTwoFields levelTwoFields : list ) {
			JSONArray jsonArray2 = new JSONArray();
			jsonArray2.add( levelTwoFields.getLevel1FieldTag() );         // 一级组件标识 样例:AV
			jsonArray2.add( levelTwoFields.getLevel2FieldName() );        // 二级组件名称 样例:音视频
			jsonArray2.add( levelTwoFields.getLevel2FieldDesc() );        // 一级组件名称
			jsonArray2.add( levelTwoFields.getLevel1FieldId() );          // 一级组件Id
			jsonArray2.add( levelTwoFields.getModifier() );
			jsonArray2.add( levelTwoFields.getId() );                     // 编号

			jsonArray1.add( jsonArray2 );
		}
		return jsonArray1;
	}

	/**
	 * M99字段列表
	 *
	 * @param l2_id
	 * @return
	 */
	private JSONArray getM99FieldsList( int l2_id ) {

		List< M99Fields > list       = dataTypeService.getM99FieldsByM1Id( l2_id );
		JSONArray         jsonArray1 = new JSONArray();
		for ( M99Fields m99 : list ) {
			JSONArray jsonArray2 = new JSONArray();

			jsonArray2.add( m99.getFieldName() );                         // 字段名称   样例:M2
			jsonArray2.add( m99.getFieldDesc() );                         // 字段描述   样例:音视频2
			jsonArray2.add( m99.getFieldType() );                         // 字段类型   样例:文本、日期、数字
			jsonArray2.add( m99.getFieldRegex() );                        // 正则表达式
			jsonArray2.add( m99.getLevelOneId() );                        // M1-Id     样例:1
			jsonArray2.add( m99.getModifier() );
			jsonArray2.add( m99.getId() );
			jsonArray1.add( jsonArray2 );
		}
		return jsonArray1;
	}

}



