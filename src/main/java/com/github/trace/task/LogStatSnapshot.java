package com.github.trace.task;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

/**
 * LogStatSnapshot
 * Created by wzk on 16/4/20.
 */
class LogStatSnapshot {

	private final ConcurrentMap< String, LogStatCounter > statCounterMap = Maps.newConcurrentMap();

	private static final char SEPARATOR = 0x03; // 不可见字符

	/**
	 * 计数
	 *
	 * @param nav
	 * @param key
	 * @param value
	 * @param desc
	 * @param status
	 * @param stamp
	 */
	public void add( String nav, String key, String value, String desc, boolean status, long stamp ) {

		String counterKey = Joiner.on( SEPARATOR ).join( nav, key, value );

		LogStatCounter counter = statCounterMap.get( counterKey );

		if ( counter == null ) {

			counter = new LogStatCounter( nav, key, value, desc, stamp );

			LogStatCounter real = statCounterMap.putIfAbsent( counterKey, counter );

			if ( real != null ) {

				counter = null;
			}
		}

		if ( counter != null ) {

			counter.report( status ); // 进行统计
		}
	}

	ConcurrentMap< String, LogStatCounter > getStatCounterMap() {

		return statCounterMap;
	}

}
