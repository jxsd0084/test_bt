package com.github.trace.task;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * LogStatCounter
 * Created by wzk on 16/4/20.
 */

// 日志计数器对象

class LogStatCounter {

	private String nav;
	private String key;
	private String value;
	private String desc;

	private long time;

	// 线程安全的计数器
	private AtomicInteger totalCount   = new AtomicInteger( 0 ); // 总数量
	private AtomicInteger successCount = new AtomicInteger( 0 ); // 成功数量
	private AtomicInteger failCount    = new AtomicInteger( 0 ); // 失败数量

	LogStatCounter( String nav, String key, String value, String desc, long time ) {

		this.nav = nav;
		this.key = key;
		this.value = value;
		this.desc = desc;
		this.time = time;
	}

	void report( boolean status ) {

		totalCount.incrementAndGet();       // 总数量 +1

		if ( status ) {
			successCount.incrementAndGet(); // 成功 +1

		} else {
			failCount.incrementAndGet();    // 失败 +1

		}

	}

	/**
	 * 转为JSON字符串
	 *
	 * @return
	 */
	String toJson() {

		// alibaba fastjson
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
