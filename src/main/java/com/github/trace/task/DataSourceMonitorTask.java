package com.github.trace.task;

import com.github.trace.service.Navigation0Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * Created by wanghl on 2016/5/20.
 */
@Component
@Scope( "prototype" )
public class DataSourceMonitorTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger( DataSourceMonitorTask.class );

	private final static String NIGHT_INTERVAL   = "night-interval";    // 晚上抽检间隔
	private static final String MORNING_INTERVAL = "morning-interval";  // 白天抽检间隔

	@Autowired
	private Navigation0Service navigation0Service;

	@Override
	public void run() {

		LOGGER.info( "数据源监控begin" );

		Calendar now  = Calendar.getInstance();
		int      hour = now.get( Calendar.HOUR_OF_DAY );

		if ( 7 <= hour && hour <= 20 ) {
			navigation0Service.checkDataSource( MORNING_INTERVAL ); // 白天

		} else {
			navigation0Service.checkDataSource( NIGHT_INTERVAL );   // 晚上

		}

		LOGGER.info( "数据源监控end" + hour );
	}

}
