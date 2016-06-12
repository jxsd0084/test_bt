package com.github.trace.task;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * LogStatCounter
 * Created by wzk on 16/4/20.
 */
class LogStatCounter {

	private String nav;
	private String key;
	private String value;
	private String desc;
	private long   time;

	private AtomicInteger totalCount   = new AtomicInteger( 0 );
	private AtomicInteger successCount = new AtomicInteger( 0 );
	private AtomicInteger failCount    = new AtomicInteger( 0 );

	LogStatCounter( String nav, String key, String value, String desc, long time ) {

		this.nav = nav;
		this.key = key;
		this.value = value;
		this.desc = desc;
		this.time = time;
	}

	void report( boolean status ) {

		totalCount.incrementAndGet();
		if ( status ) {
			successCount.incrementAndGet();
		} else {
			failCount.incrementAndGet();
		}
	}

	String toJson() {

		JSONObject object = new JSONObject();
		object.put( "nav", nav );
		object.put( "key", key );
		object.put( "value", value );
		object.put( "desc", desc );
		object.put( "time", time );
		object.put( "totalCount", totalCount.get() );
		object.put( "successCount", successCount.get() );
		object.put( "failCount", failCount.get() );
		return object.toJSONString();
	}

	@Override
	public String toString() {

		return "LogStatCounter{" +
		       "nav='" + nav + '\'' +
		       ", key='" + key + '\'' +
		       ", value='" + value + '\'' +
		       ", desc='" + desc + '\'' +
		       ", time=" + time +
		       ", totalCount=" + totalCount +
		       ", successCount=" + successCount +
		       ", failCount=" + failCount +
		       '}';
	}
}
