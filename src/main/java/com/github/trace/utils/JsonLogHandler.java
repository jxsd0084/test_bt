package com.github.trace.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.alibaba.fastjson.JSONObject;
import com.github.autoconf.ConfigFactory;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * json格式日志处理
 * Created by wzk on 16/4/6.
 */
public class JsonLogHandler {

	private static final Logger LOG = LoggerFactory.getLogger( JsonLogHandler.class );

	private static final String                DATEFORMAT          = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final DateTimeFormatter     DATE_TIME_FORMATTER = DateTimeFormat.forPattern( DATEFORMAT );
	private static       Map< String, String > keyMap              = Maps.newLinkedHashMap();

	static {
		ConfigFactory.getInstance().getConfig( "buriedtool-es-keymap" ).addListener( config -> {
			keyMap = config.getAll();
		} );
	}

	private JsonLogHandler() {

	}

	public static Set< String > batchConvert( Set< String > logs ) {

		Set< String > results = Sets.newHashSet();
		if ( logs != null ) {
			logs.forEach( log -> results.add( convert( log ) ) );
		}
		return results;
	}

	public static String convert( String log ) {

		JSONObject json   = JSONObject.parseObject( log );
		JSONObject object = new JSONObject();
		json.entrySet().forEach( entry -> {
			String keyConverted = convertFieldName( entry.getKey() );
			String value        = entry.getValue().toString();

			if ( StringUtils.equals( keyConverted, "stamp" ) ) {
				long stamp = parseTimeStamp( value );
				object.put( keyConverted, String.valueOf( stamp ) );
			} else {
				object.put( keyConverted, value );
			}
		} );
		return object.toJSONString();
	}

	public static String convertFieldName( String originKey ) {

		for ( Map.Entry< String, String > entry : keyMap.entrySet() ) {
			String oKey = entry.getKey();
			String nKey = entry.getValue();
			if ( StringUtils.equals( oKey, originKey ) ) {
				return nKey;
			}
			if ( StringUtils.contains( originKey, '.' ) ) {
				return StringUtils.replaceChars( originKey, '.', '_' );
			}
		}
		return originKey;
	}

	public static long getStampFromLog( String log ) {

		JSONObject json = JSONObject.parseObject( log );
		String     time = json.getString( "_time" );
		return parseTimeStamp( time );
	}

	public static String getIpFromLog( String log ) {

		JSONObject json = JSONObject.parseObject( log );
		return json.getString( "_ip" );
	}

	private static long parseTimeStamp( String value ) {

		DateTime jodaTime = DateTime.now();
		try {
			jodaTime = DateTime.parse( value, DATE_TIME_FORMATTER );
		} catch ( Exception e ) {
			LOG.error( "Cannot parse date {}, use current date instead", value, e );
		}
		return jodaTime.getMillis();
	}

}
