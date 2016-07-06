package com.fxiaoke.tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by weilei on 2016/4/8.
 */
public class Test {

	public static void main( String[] args ) {

		JSONObject source = new JSONObject();
		source.put( "dbType", "MySQL" );
		source.put( "dbUrl", "jdbc:mysql://172.31.102.105" );
		source.put( "dbDriver", "com.mysql.jdbc.Driver" );
		source.put( "dbPort", "3306" );
		source.put( "dbName", "sso" );
		source.put( "dbUser", "root" );
		source.put( "dbPassword", "root123" );

		JSONArray  selectedData = new JSONArray();
		JSONObject tableData    = new JSONObject();
		tableData.put( "tableName", "comment" );
		JSONArray  selectField = new JSONArray();
		JSONObject fieldData   = new JSONObject();
		fieldData.put( "fieldName", "key" );
		fieldData.put( "des", "1" );
		fieldData.put( "par", "0" );
		selectField.add( fieldData );
		fieldData = new JSONObject();
		fieldData.put( "fieldName", "val" );
		fieldData.put( "des", "1" );
		fieldData.put( "par", "0" );
		selectField.add( fieldData );
		tableData.put( "fields", selectField );
		selectedData.add( tableData );

		tableData = new JSONObject();
		tableData.put( "tableName", "daily" );
		selectField = new JSONArray();
		fieldData = new JSONObject();
		fieldData.put( "fieldName", "day" );
		fieldData.put( "des", "0" );
		fieldData.put( "par", "1" );
		selectField.add( fieldData );
		fieldData = new JSONObject();
		fieldData.put( "fieldName", "data" );
		fieldData.put( "des", "1" );
		fieldData.put( "par", "1" );
		selectField.add( fieldData );
		tableData.put( "fields", selectField );
		selectedData.add( tableData );

		source.put( "selectedData", selectedData );

		//////////////////////////////////////////////////////////////////////////

		JSONObject target = new JSONObject();
		target.put( "tarType", "HDFS" );
		target.put( "tarUrl", "172.31.107.11" );
		target.put( "tarPath", "/facishare-data" );
		target.put( "tarBeginTime", "2016-04-05 19:04:30" );
		target.put( "tarName", "sso" );

		//////////////////////////////////////////////////////////////////////////

		JSONObject job = new JSONObject();
		job.put( "source", source );
		job.put( "target", target );
		job.put( "beginTime", "2016-04-05 19:04:30" );
		job.put( "exportName", "ssoexp" );

		//System.out.println(job.toJSONString());

		Pattern pattern2 = Pattern.compile( "(\\d{2}|\\d{4})(?:\\-)?([0]{1}\\d{1}|[1]{1}[0-2]{1})(?:\\-)?([0-2]{1}\\d{1}|[3]{1}[0-1]{1})(?:\\s)?([0-1]{1}\\d{1}|[2]{1}[0-3]{1})(?::)?([0-5]{1}\\d{1})(?::)?([0-5]{1}\\d{1})" );
		Matcher matcher2 = pattern2.matcher( "2016-04-08 19:08:13" );
		boolean b        = matcher2.matches();
//         System.out.println(b);

		String x = "[{'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':9,'M98':'2016-01-28 20:51:32','M99.M1':'activite','realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}, {'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':9,'M98':1453985500196,'M99.M1':'unactivite','realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}, {'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':2,'M98':1453985500196,'M99.avg':0,'M99.M3':0,'M99.M2':0,'M99.failed':0,'M99.M1':0,'realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}, {'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':2,'M98':1453984760374,'M99.avg':0,'M99.M3':0,'M99.M2':0,'M99.failed':0,'M99.M1':0,'realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}, {'M4':'iPhone OS','M7':'100146','M2':'15820776627','M5':'8.4','M8':'iPhone6,2','M3':'ecf557a77013dafc53b6fd574a80fd7b','M6':'5.1','M1':'fs','M96':'WIFI','M97':9,'M98':1453984760374,'M99.M1':'unactivite','realip':'10.20.0.3','_ip':'172.31.103.120','_time':'2016-04-08 19:33:09.588'}]";
		x = x.replace( "}", "}<br>" );
//         System.out.println(x);
		x = "M99_M6 : java.lang.NoSuchMethodError: No direct method (Landroid/app/Activity;)V in class Lcom/facishare/fs/js/JSServerHandler; or its super classes (declaration of 'com.facishare.fs.js.JSServerHandler' appears in /data/app/com.facishare.fs-1/base.apk) at";
		System.out.println( x.length() );
	}
}
