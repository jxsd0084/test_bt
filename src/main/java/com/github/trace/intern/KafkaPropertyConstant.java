package com.github.trace.intern;

/**
 * Created by wzk on 16/3/18.
 */
public class KafkaPropertyConstant {

	public static final String GROUP_ID = "buriedtool_kafkaService";

	public static final String BOOSTRAP_SERVERS         = "boostrap.servers";
	public static final String SERVER_PORT              = "port";
	public static final String CONSUMER_CONNECT_TIMEOUT = "connect.timeout.ms";
	public static final String CONSUMER_BUFFER_SIZE     = "buffer.size";
	public static final String FETCH_SIZE               = "fetch.size";

	public static final int SERVER_PORT_DEFAULT              = 9092;
	public static final int CONSUMER_CONNECT_TIMEOUT_DEFAULT = 60000;
	public static final int CONSUMER_BUFFER_SIZE_DEFAULT     = 64 * 1024;
	public static final int FETCH_SIZE_DEFAULT               = 10 * 1024;

}
