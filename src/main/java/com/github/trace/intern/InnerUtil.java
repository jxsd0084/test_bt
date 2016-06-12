package com.github.trace.intern;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;

import com.github.trace.entity.Log;

import java.io.Closeable;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InnerUtil {

	public static final int  ONE_HOUR     = 60000 * 60;
	public static final long ONE_DAY      = 24 * 3600 * 1000;
	public static final long CHINA_OFFSET = 8 * ONE_HOUR;

	public static final Escaper JS_ESCAPE = Escapers.builder()
			.addEscape( '\'', "\\'" )
			.addEscape( '\"', "\\\"" )
			.addEscape( '\\', "\\\\" )
			.addEscape( '/', "\\/" )
			.build();

	private InnerUtil() {

	}

	/**
	 * 提取公共代码，方便在测试环境进行开发
	 *
	 * @return 某个时间点
	 */
	public static long getStamp() {

		return System.currentTimeMillis();
	}

	/**
	 * 获取日期描述的当天零点的时刻
	 *
	 * @param day        日期描述串
	 * @param defaultVal 默认值
	 * @return 解析的时间
	 */
	public static long parse( String day, long defaultVal ) {

		if ( day == null || day.length() == 0 ) {
			return defaultVal;
		}
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		try {
			return sdf.parse( day ).getTime();
		} catch ( ParseException ignored ) {
			return defaultVal;
		}
	}

	/**
	 * 获取日期描述的当天零点的时刻
	 *
	 * @param day        日期描述串
	 * @param defaultVal 默认值
	 * @return 解析的时间
	 */
	public static long parseYmdHis( String day, long defaultVal ) {

		if ( day == null || day.length() == 0 ) {
			return defaultVal;
		}
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		try {
			return sdf.parse( day ).getTime();
		} catch ( ParseException ignored ) {
			return defaultVal;
		}
	}

	/**
	 * 把字符日期值变换为yyyy-MM-dd格式
	 *
	 * @param day 字符描述
	 * @return 如果day为空则返回当天日期标识
	 */
	public static String format( String day ) {

		if ( Strings.isNullOrEmpty( day ) ) {
			return DateUtil.formatYmd( System.currentTimeMillis() );
		} else {
			switch ( day ) {
				case "today":
					return DateUtil.formatYmd( System.currentTimeMillis() );
				case "yesterday":
					return DateUtil.formatYmd( System.currentTimeMillis() - ONE_DAY );
				case "lastWeek":
					return DateUtil.formatYmd( System.currentTimeMillis() - 7 * ONE_DAY );
			}
		}
		return day;
	}

	public static boolean isEmpty( Collection< ? > objects ) {

		return objects == null || objects.size() == 0;
	}

	public static void closeQuietly( Closeable obj ) {

		if ( obj != null ) {
			try {
				obj.close();
			} catch ( IOException ignored ) {
			}
		}
	}

	public static Map< String, String > toCharts( List< Map > items ) {

		StringBuilder total = new StringBuilder( 1024 );
		StringBuilder cost  = new StringBuilder( 1024 );
		StringBuilder fail  = new StringBuilder( 1024 );
		StringBuilder slow  = new StringBuilder( 1024 );
		for ( Map i : items ) {
			Long stamp = ( Long ) i.get( "stamp" );
			total.append( '[' ).append( stamp ).append( ',' ).append( i.get( "total" ) ).append( "]," );
			cost.append( '[' ).append( stamp ).append( ',' ).append( i.get( "cost" ) ).append( "]," );
			fail.append( '[' ).append( stamp ).append( ',' ).append( i.get( "fail" ) ).append( "]," );
			slow.append( '[' ).append( stamp ).append( ',' ).append( i.get( "slow" ) ).append( "]," );
		}
		HashMap< String, String > m = Maps.newHashMap();
		m.put( "total", trim( total ) );
		m.put( "cost", trim( cost ) );
		m.put( "fail", trim( fail ) );
		m.put( "slow", trim( slow ) );
		return m;
	}

	public static Map< String, String > toChartsByCaller( List< Map > items ) {

		StringBuilder total       = new StringBuilder( 1024 );
		StringBuilder cost        = new StringBuilder( 1024 );
		StringBuilder slowPercent = new StringBuilder( 1024 );
		StringBuilder failPercent = new StringBuilder( 1024 );
		for ( Map i : items ) {
			Long stamp = ( Long ) i.get( "stamp" );
			total.append( '[' ).append( stamp ).append( ',' ).append( i.get( "total" ) ).append( "]," );
			cost.append( '[' ).append( stamp ).append( ',' ).append( i.get( "cost" ) ).append( "]," );
			slowPercent.append( '[' ).append( stamp ).append( ',' ).append( i.get( "slowPercent" ) ).append( "]," );
			failPercent.append( '[' ).append( stamp ).append( ',' ).append( i.get( "failPercent" ) ).append( "]," );
		}
		HashMap< String, String > m = Maps.newHashMap();
		m.put( "total", trim( total ) );
		m.put( "cost", trim( cost ) );
		m.put( "slowPercent", trim( slowPercent ) );
		m.put( "failPercent", trim( failPercent ) );
		return m;
	}


	public static Map< String, String > toActionIdCharts( List< Map > items ) {

		StringBuilder total   = new StringBuilder( 1024 );
		StringBuilder cost    = new StringBuilder( 1024 );
		StringBuilder avgCost = new StringBuilder( 1024 );
		for ( Map i : items ) {
			Long stamp = ( Long ) i.get( "stamp" );
			total.append( '[' ).append( stamp ).append( ',' ).append( i.get( "total" ) ).append( "]," );
			cost.append( '[' ).append( stamp ).append( ',' ).append( i.get( "cost" ) ).append( "]," );
			avgCost.append( '[' ).append( stamp ).append( ',' ).append( i.get( "avgCost" ) ).append( "]," );
		}
		HashMap< String, String > m = Maps.newHashMap();
		m.put( "total", trim( total ) );
		m.put( "cost", trim( cost ) );
		m.put( "avgCost", trim( avgCost ) );
		return m;
	}

	public static Map< String, String > toDomloadCharts( List< Map > items ) {

		StringBuilder total    = new StringBuilder( 1024 );
		StringBuilder cost     = new StringBuilder( 1024 );
		StringBuilder avgCost  = new StringBuilder( 1024 );
		StringBuilder thead    = new StringBuilder( 1024 );
		StringBuilder tload    = new StringBuilder( 1024 );
		StringBuilder domready = new StringBuilder( 1024 );
		for ( Map i : items ) {
			Long stamp = ( Long ) i.get( "stamp" );
			total.append( '[' ).append( stamp ).append( ',' ).append( i.get( "total" ) ).append( "]," );
			cost.append( '[' ).append( stamp ).append( ',' ).append( i.get( "cost" ) ).append( "]," );
			avgCost.append( '[' ).append( stamp ).append( ',' ).append( i.get( "avgCost" ) ).append( "]," );
			thead.append( '[' ).append( stamp ).append( ',' ).append( i.get( "thead" ) ).append( "]," );
			tload.append( '[' ).append( stamp ).append( ',' ).append( i.get( "tload" ) ).append( "]," );
			domready.append( '[' ).append( stamp ).append( ',' ).append( i.get( "domready" ) ).append( "]," );
		}
		HashMap< String, String > m = Maps.newHashMap();
		m.put( "total", trim( total ) );
		m.put( "cost", trim( cost ) );
		m.put( "avgCost", trim( avgCost ) );
		m.put( "thead", trim( thead ) );
		m.put( "tload", trim( tload ) );
		m.put( "domready", trim( domready ) );
		return m;
	}

	public static String trim( StringBuilder s ) {

		if ( s.length() > 0 ) {
			return s.substring( 0, s.length() - 1 );
		}
		return "";
	}

	public static String toJson( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "name" ) ) ).append( '"' );
			sbd.append( ',' ).append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "desc" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "failNum" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "slow" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}


	public static Map< String, String > toLogCharts( List< Map > items ) {

		StringBuilder count = new StringBuilder( 1024 );
		StringBuilder rate  = new StringBuilder( 1024 );
		double        start = 0, end = 0;
		if ( items.get( 0 ) != null ) {
			start = Double.valueOf( items.get( 0 ).get( "count" ).toString() );
		}
		for ( int i = 1; i < items.size(); i++ ) {
			Map m = items.get( i );
			end = Double.valueOf( m.get( "count" ).toString() );
			Long stamp = ( Long ) m.get( "stamp" );
			count.append( '[' ).append( stamp ).append( ',' ).append( end - start ).append( "]," );
			rate.append( '[' ).append( stamp ).append( ',' ).append( m.get( "m1_rate" ) ).append( "]," );
			start = end;
		}
		HashMap< String, String > m = Maps.newHashMap();
		m.put( "rate", trim( rate ) );
		m.put( "count", trim( count ) );
		return m;
	}

	public static String toJsonService( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "name" ) ) ).append( '"' );
			sbd.append( ',' ).append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "desc" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "failPercent" ) );
			sbd.append( ',' ).append( m.get( "slowPercent" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}


	public static String toJsonModule( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "module" ) ) ).append( '"' );
			sbd.append( ',' ).append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "name" ) ) ).append( '"' );
			sbd.append( ',' ).append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "desc" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "failPercent" ) );
			sbd.append( ',' ).append( m.get( "slowPercent" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJsonCallerChart( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "time" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "failPercent" ) );
			sbd.append( ',' ).append( m.get( "slowPercent" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson2( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "time" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "failNum" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "slow" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson3( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "app" ) ) ).append( '"' );
			sbd.append( ',' ).append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "uri" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ',' ).append( m.get( "failNum" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "spider" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson4( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "time" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "pv20x" ) );
			sbd.append( ',' ).append( m.get( "pv30x" ) );
			sbd.append( ',' ).append( m.get( "pv40x" ) );
			sbd.append( ',' ).append( m.get( "pv50x" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ',' ).append( m.get( "failNum" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "spider" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson5( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "server" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "pv20x" ) );
			sbd.append( ',' ).append( m.get( "pv30x" ) );
			sbd.append( ',' ).append( m.get( "pv40x" ) );
			sbd.append( ',' ).append( m.get( "pv50x" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ',' ).append( m.get( "failNum" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "spider" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson6( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "uri" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ',' ).append( m.get( "cost" ) );
			sbd.append( ',' ).append( m.get( "failNum" ) );
			sbd.append( ',' ).append( m.get( "fail" ) );
			sbd.append( ',' ).append( m.get( "spider" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson7( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "actionid" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "avgCost" ) );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson71( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "browser" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "avgCost" ) );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson8( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "time" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "avgCost" ) );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJson9( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "time" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "thead" ) );
			sbd.append( ',' ).append( m.get( "tload" ) );
			sbd.append( ',' ).append( m.get( "domready" ) );
			sbd.append( ',' ).append( m.get( "avgCost" ) );
			sbd.append( ',' ).append( m.get( "total" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJsonLog( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Map m : items ) {
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "name" ) ) ).append( '"' ).append( ',' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "ip" ) ) ).append( '"' );
			sbd.append( ',' ).append( m.get( "errorNum" ) );
			sbd.append( ',' ).append( m.get( "errorRate" ) );
			sbd.append( ',' ).append( m.get( "warnNum" ) );
			sbd.append( ',' ).append( m.get( "warnRate" ) );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String toJsonHistoryLog( List< Map > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		double start = 0, end = 0;
		if ( items.get( 0 ) != null ) {
			start = Double.valueOf( items.get( 0 ).get( "count" ).toString() );
		}
		for ( int i = 1; i < items.size(); i++ ) {
			Map m = items.get( i );
			end = Double.valueOf( m.get( "count" ).toString() );
			sbd.append( '[' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.get( "time" ) ) ).append( '"' );
			sbd.append( ',' ).append( ( long ) ( end - start ) );
			sbd.append( ',' ).append( m.get( "m1_rate" ) );
			sbd.append( ',' ).append( m.get( "m5_rate" ) );
			sbd.append( ',' ).append( m.get( "m15_rate" ) );
			sbd.append( ']' );
			sbd.append( ',' );
			start = end;
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}

	public static String highlightContent( String content ) {

		CharMatcher    charMatcher = CharMatcher.anyOf( "\r\n" );
		List< String > list        = Splitter.on( charMatcher ).splitToList( content );
		StringBuilder  sb          = new StringBuilder();
		for ( String line : list ) {
			if ( line.contains( "Exception" ) ||
			     line.contains( "Caused by" ) ) {
				sb.append( "<span class='highlightrow'>" ).append( line ).append( "</span>" ).append( "<br/>" );
			} else {
				sb.append( line ).append( "<br/>" );
			}
		}
		return sb.toString();
	}

	public static String toJsonLogMessage( List< Log > items ) {

		StringBuilder sbd = new StringBuilder( 4096 );
		sbd.append( '[' );
		for ( Log m : items ) {
			sbd.append( '[' );
			String content = m.getContent();
			content = JS_ESCAPE.escape( content );
			CharMatcher charMatcher = CharMatcher.anyOf( "\r\n" );
			//content = charMatcher.replaceFrom(content,"<br/>");
			content = highlightContent( content );
			String message = JS_ESCAPE.escape( m.getMessage() );
			message = charMatcher.replaceFrom( message, "<br/>" );
			sbd.append( '"' ).append( message ).append( '"' ).append( ',' );
			sbd.append( '"' ).append( content ).append( '"' ).append( ',' );
			sbd.append( '"' ).append( m.getThreadName() ).append( '"' ).append( ',' );
			sbd.append( '"' ).append( m.getClassName() ).append( '"' ).append( ',' );
			sbd.append( '"' ).append( JS_ESCAPE.escape( ( String ) m.getTime() ) ).append( '"' );
			sbd.append( ']' );
			sbd.append( ',' );
		}
		if ( sbd.charAt( sbd.length() - 1 ) == ',' ) {
			sbd.setLength( sbd.length() - 1 );
		}
		sbd.append( ']' );
		return sbd.toString();
	}


}


