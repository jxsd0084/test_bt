package com.github.trace.task;

import com.alibaba.fastjson.JSONArray;
import com.github.autoconf.ConfigFactory;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.service.AnalyzeLogService;
import com.github.trace.service.CEPService;
import com.github.trace.utils.ElasticSearchHelper;
import com.github.trace.utils.JsonLogHandler;
import com.github.trace.utils.NginxLogHandler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * LogStatCollector
 * Created by wzk on 16/4/20.
 */
@Service
class LogStatCollector {

	private static final Logger LOG = LoggerFactory.getLogger( LogStatCollector.class );

	private static final String INDEX = "datapt-logstatistic";
	private static final String TYPE  = "stat";

	private Map< String, String >              navTopicMap = Maps.newHashMap();
	private AtomicReference< LogStatSnapshot > current     = new AtomicReference<>( new LogStatSnapshot() );

	private long    counterStamp = 0L;
	private boolean statSwitch   = true;
	private long    statInterval = 5 * 60 * 1000L;

	@Autowired
	private AnalyzeLogService analyzeLogService;
	@Autowired
	private CEPService        cepService;

	@PostConstruct
	void init() {

		ConfigFactory.getInstance().getConfig( "buriedtool-scheduler", config -> {
			statSwitch = config.getBool( "stat-switch", true );
			statInterval = config.getLong( "stat-interval", 60 * 1000L );
			LOG.info( "Statistic interval was changed to {}", statInterval );
		} );
	}

	void report( String topic, String content ) {

		if ( !statSwitch ) {
			return;
		}
		String navName = getNavName( topic, content );
		if ( Strings.isNullOrEmpty( navName ) ) {
			return;
		}
		String json;
		if ( StringUtils.startsWith( topic, "nginx" ) ) {
			json = NginxLogHandler.parseNginx( content );
		} else {
			json = content;
		}
		JSONArray result = null;
		try {
			result = analyzeLogService.formatLog( navName, "[" + json + "]" );
		} catch ( Exception e ) {
			LOG.error( "Cannot get analyzed result. ", e );
		}
		if ( result == null || result.isEmpty() ) {
			return;
		}
		long stamp = getStamp( topic, content );
		if ( counterStamp == 0L ) {
			counterStamp = stamp;
		} else if ( stamp - counterStamp > statInterval ) {
			collect();
			counterStamp = stamp;
		}

		for ( int i = 0; i < result.size(); i++ ) {
			JSONArray field = result.getJSONArray( i );
			try {
				JSONArray inner  = field.getJSONArray( 2 );
				String    key    = field.getString( 0 );
				String    desc   = field.getString( 1 );
				String    value  = inner.getString( 1 );
				boolean   status = inner.getBoolean( 0 );
				current.get().add( navName, key, value, desc, status, stamp );
			} catch ( Exception e ) {
				LOG.error( "Cannot parse analyzed result, {}", field, e );
			}
		}
	}

	private void collect() {

		LogStatSnapshot                         snapshot   = getAndReset();
		ConcurrentMap< String, LogStatCounter > counterMap = snapshot.getStatCounterMap();
		if ( counterMap.isEmpty() ) {
			return;
		}

		Set< String > items = Sets.newLinkedHashSet();
		for ( Map.Entry< String, LogStatCounter > entry : counterMap.entrySet() ) {
			LogStatCounter counter = entry.getValue();
			items.add( counter.toJson() );
		}
		ElasticSearchHelper.bulk( INDEX, TYPE, items );
	}

	private LogStatSnapshot getAndReset() {

		return current.getAndSet( new LogStatSnapshot() );
	}

	// 根据log得到stamp
	private long getStamp( String topic, String content ) {

		long stamp;
		if ( StringUtils.startsWith( topic, "nginx" ) ) {
			stamp = NginxLogHandler.getStampFromNginxLog( content );
		} else {
			stamp = JsonLogHandler.getStampFromLog( content );
		}
		return stamp;
	}

	private String getNavName( String topic, String content ) {

		if ( StringUtils.equals( topic, "dcx.MonitorRequest" ) ) {
			if ( StringUtils.contains( content.toLowerCase(), "iphone" ) ) {
				return "iOS";
			} else {
				return "Android";
			}
		}
		if ( navTopicMap.isEmpty() ) {
			List< NavigationItem0 > navigationItems = cepService.getRootItem();
			for ( NavigationItem0 nav : navigationItems ) {
				navTopicMap.put( nav.getTopic(), nav.getName() );
			}
		}
		return navTopicMap.get( topic );
	}

}
