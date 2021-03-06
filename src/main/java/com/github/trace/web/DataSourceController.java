package com.github.trace.web;

import com.alibaba.fastjson.JSONArray;
import com.github.trace.entity.DatabaseInfo;
import com.github.trace.service.CEPService;
import com.github.trace.service.DataSourceServer;
import com.github.trace.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * Created by chenlong on 2016/3/25.
 */
@Controller
@RequestMapping( "/ds" )
public class DataSourceController {

	@Autowired
	private CEPService       cepService;
	@Autowired
	private DataSourceServer dataSourceServer;

	@RequestMapping( "/list" )
	public String list( @RequestParam( name = "bizId" ) int bizId,
	                    @RequestParam( name = "bizName" ) String bizName,
	                    Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "ds" );
		return setCommonParam( bizId, bizName, model, "ds/ds_index" );
	}

	@RequestMapping( "/listJob" )
	public String listJob( @RequestParam( name = "bizId" ) int bizId,
	                       @RequestParam( name = "bizName" ) String bizName,
	                       Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "ds" );
		return setCommonParam( bizId, bizName, model, "job/job_index" );
	}

	@RequestMapping( "/create" )
	public String create( @RequestParam( name = "bizId" ) int bizId,
	                      @RequestParam( name = "bizName" ) String bizName,
	                      @RequestParam( name = "tag" ) String tag,
	                      Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "ds" );

		model.addAttribute( "bizId", bizId );
		model.addAttribute( "bizName", bizName );
		model.addAttribute( "tag", tag );
		return "ds/ds_edit";
	}

	@RequestMapping( "/edit" )
	public String edit( @RequestParam( name = "id", required = false ) int id,
	                    @RequestParam( name = "bizId" ) int bizId,
	                    @RequestParam( name = "bizName" ) String bizName,
	                    @RequestParam( name = "tag" ) String tag,
	                    Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "ds" );
		DatabaseInfo dataBaseInfo = dataSourceServer.getDataBaseInfoById( id );

		model.addAttribute( "id", id );
		model.addAttribute( "bizId", bizId );
		model.addAttribute( "bizName", bizName );
		model.addAttribute( "tag", tag );
		model.addAttribute( "obj", dataBaseInfo );
		return "ds/ds_edit";
	}

	@RequestMapping( "/add" )
	@ResponseBody
	public Map add( @RequestParam( name = "bizId" ) int bizId,
	                @RequestParam( name = "bizName" ) String bizName,
	                @RequestParam( name = "dbType" ) String dbType,
	                @RequestParam( name = "name" ) String name,
	                @RequestParam( name = "dbDriver" ) String dbDriver,
	                @RequestParam( name = "dbUrl" ) String dbUrl,
	                @RequestParam( name = "dbPort" ) int dbPort,
	                @RequestParam( name = "dbName" ) String dbName,
	                @RequestParam( name = "dbUsername" ) String dbUsername,
	                @RequestParam( name = "dbPassword" ) String dbPassword,
	                @RequestParam( name = "dbIns" ) String dbIns,
	                Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "ds" );

		DatabaseInfo databaseInfo = new DatabaseInfo();
		databaseInfo.setBizId( bizId );
		databaseInfo.setBizName( bizName );
		databaseInfo.setDbType( dbType );
		databaseInfo.setName( name );
		databaseInfo.setDbDriver( dbDriver );
		databaseInfo.setDbUrl( dbUrl );
		databaseInfo.setDbPort( dbPort );
		databaseInfo.setDbName( dbName );
		databaseInfo.setDbUsername( dbUsername );
		databaseInfo.setDbPassword( dbPassword );
		databaseInfo.setDbIns( dbIns );

		int res = dataSourceServer.addDatabaseInfo( databaseInfo );

		model.addAttribute( "bizId", bizId );
		model.addAttribute( "bizName", bizName );
		return ControllerHelper.returnResponseVal( res, "添加" );
	}

	@RequestMapping( "/modify" )
	@ResponseBody
	public Map modify( @RequestParam( name = "id" ) int id,
	                   @RequestParam( name = "bizId" ) int bizId,
	                   @RequestParam( name = "bizName" ) String bizName,
	                   @RequestParam( name = "dbType" ) String dbType,
	                   @RequestParam( name = "name" ) String name,
	                   @RequestParam( name = "dbDriver" ) String dbDriver,
	                   @RequestParam( name = "dbUrl" ) String dbUrl,
	                   @RequestParam( name = "dbPort" ) int dbPort,
	                   @RequestParam( name = "dbName" ) String dbName,
	                   @RequestParam( name = "dbUsername" ) String dbUsername,
	                   @RequestParam( name = "dbPassword" ) String dbPassword,
	                   @RequestParam( name = "dbIns" ) String dbIns,
	                   Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "ds" );

		DatabaseInfo databaseInfo = new DatabaseInfo();
		databaseInfo.setId( id );
		databaseInfo.setBizId( bizId );
		databaseInfo.setBizName( bizName );
		databaseInfo.setDbType( dbType );
		databaseInfo.setName( name );
		databaseInfo.setDbDriver( dbDriver );
		databaseInfo.setDbUrl( dbUrl );
		databaseInfo.setDbPort( dbPort );
		databaseInfo.setDbName( dbName );
		databaseInfo.setDbUsername( dbUsername );
		databaseInfo.setDbPassword( dbPassword );
		databaseInfo.setDbIns( dbIns );

		int res = dataSourceServer.updateDataBaseInfo( databaseInfo );

		model.addAttribute( "bizId", bizId );
		model.addAttribute( "bizName", bizName );
		return ControllerHelper.returnResponseVal( res, "修改" );
	}

	@RequestMapping( "/test_con" )
	@ResponseBody
	public Map jdbcCon( @RequestParam( name = "id" ) int id,
	                    @RequestParam( name = "bizId" ) int bizId,
	                    @RequestParam( name = "bizName" ) String bizName,
	                    @RequestParam( name = "dbType" ) String dbType,
	                    @RequestParam( name = "name" ) String name,
	                    @RequestParam( name = "dbDriver" ) String dbDriver,
	                    @RequestParam( name = "dbUrl" ) String dbUrl,
	                    @RequestParam( name = "dbPort" ) int dbPort,
	                    @RequestParam( name = "dbName" ) String dbName,
	                    @RequestParam( name = "dbUsername" ) String dbUsername,
	                    @RequestParam( name = "dbPassword" ) String dbPassword,
	                    @RequestParam( name = "dbIns" ) String dbIns,
	                    Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "ds" );

		DatabaseInfo databaseInfo = new DatabaseInfo();
		databaseInfo.setId( id );
		databaseInfo.setBizId( bizId );
		databaseInfo.setBizName( bizName );
		databaseInfo.setDbType( dbType );
		databaseInfo.setName( name );
		databaseInfo.setDbDriver( dbDriver );
		databaseInfo.setDbUrl( dbUrl );
		databaseInfo.setDbPort( dbPort );
		databaseInfo.setDbName( dbName );
		databaseInfo.setDbUsername( dbUsername );
		databaseInfo.setDbPassword( dbPassword );
		databaseInfo.setDbIns( dbIns );

		int res = dataSourceServer.testJdbcConnection( databaseInfo );

		model.addAttribute( "bizId", bizId );
		model.addAttribute( "bizName", bizName );
		return ControllerHelper.returnResponseVal( res, "连接" );
	}

	@RequestMapping( "/delete" )
	public String delete( @RequestParam( name = "id" ) int id,
	                      @RequestParam( name = "bizId" ) int bizId,
	                      @RequestParam( name = "bizName" ) String bizName,
	                      Model model ) {

		ControllerHelper.setLeftNavigationTree( model, cepService, "ds" );
		dataSourceServer.deleteDataBaseInfoById( id );
		return setCommonParam( bizId, bizName, model, "ds/ds_index" );
	}

	private String setCommonParam( int bizId, String bizName, Model model, String retPath ) {

		List< DatabaseInfo > list      = dataSourceServer.getDataBaseInfoListById( bizId );
		JSONArray            jsonArray = ControllerHelper.convertToJSON( list );
		model.addAttribute( "data", jsonArray );
		model.addAttribute( "bizId", bizId );
		model.addAttribute( "bizName", bizName );
		return retPath;
	}

}
