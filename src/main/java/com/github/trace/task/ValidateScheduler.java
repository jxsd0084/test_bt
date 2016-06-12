package com.github.trace.task;

import com.alibaba.fastjson.JSONArray;
import com.github.autoconf.ConfigFactory;
import com.github.trace.entity.NavigationItem0;
import com.github.trace.service.AnalyzeLogService;
import com.github.trace.service.CEPService;
import com.github.trace.service.KafkaService;
import com.github.trace.utils.ElasticSearchHelper;
import com.github.trace.utils.JsonLogHandler;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * 定时从kafka拉取日志进行校验, 校验失败入ES
 * Created by wzk on 16/3/23.
 */
@Component
class ValidateScheduler {

	private static final long           SAMPLE_RATE    = 60000L;
	private static       int            sampleCount    = 10;
	private static       boolean        scheduleSwitch = true;
	private static final String         ES_INDEX       = "datapt-validate";
	private static       List< String > alterTimes     = Lists.newArrayList();

	@Autowired
	private KafkaService kafkaService;
	@Autowired
	private CEPService   cepService;

	@Autowired
	private AnalyzeLogService analyzeLogService;

	// private AnalyzeLog analyzeLog = new AnalyzeLog();

	@PostConstruct
	public void init() {

		ConfigFactory.getInstance().getConfig( "buriedtool-scheduler" ).addListener( config -> {
			scheduleSwitch = config.getBool( "scheduleSwitch", true );
			sampleCount = config.getInt( "sampleCount", 10 );
			String alarmTime = config.get( "alarmTime", "10:00,15:00" );
			alterTimes = Splitter.on( ',' ).omitEmptyStrings().splitToList( alarmTime );
		} );
	}

	@Scheduled( fixedDelay = SAMPLE_RATE )
	void scheduler() {

		if ( !scheduleSwitch ) {
			return;
		}
		boolean                 sendMonitor = sendMonitor();
		List< NavigationItem0 > navs        = cepService.getRootItem();
		for ( NavigationItem0 nav : navs ) {
			if ( nav.getItemType() == 0 ) {
				continue;
			}
			String        topic      = nav.getTopic();
			String        name       = nav.getName();
			Set< String > sampleLogs = kafkaService.getMessages( topic, sampleCount );
			batchValidate( name, topic, sampleLogs, sendMonitor );
		}
	}

	public void sendMonitor( String name ) {

		NavigationItem0 item = cepService.getNavigationItem0ByName( name );
		if ( item == null ) {
			return;
		}
		String        topic      = item.getTopic();
		Set< String > sampleLogs = kafkaService.getMessages( topic, sampleCount );
		batchValidate( name, topic, sampleLogs, true );
	}

	@Async
	private void batchValidate( String name, String topic, Set< String > set, boolean sendMonitor ) {

		Set< String > toEs      = analyzeLogService.filterToES( name, JSONArray.toJSONString( set ), sendMonitor );
		Set< String > converted = JsonLogHandler.batchConvert( toEs );
		ElasticSearchHelper.bulk( ES_INDEX, topic, converted );
	}

	private boolean sendMonitor() {

		String hourMinute = getHourMinute();
		return alterTimes.contains( hourMinute );
	}

	private String getHourMinute() {

		StringBuilder sb   = new StringBuilder( 5 );
		Calendar      c    = Calendar.getInstance();
		int           hour = c.get( Calendar.HOUR_OF_DAY );
		if ( hour < 10 ) {
			sb.append( '0' );
		}
		sb.append( hour ).append( ':' );
		int minute = c.get( Calendar.MINUTE );
		if ( minute < 10 ) {
			sb.append( '0' );
		}
		sb.append( minute );
		return sb.toString();
	}

}
