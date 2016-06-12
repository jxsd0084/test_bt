package com.github.trace.service;

import com.github.autoconf.ConfigFactory;
import com.github.trace.entity.KafkaMessageAndOffset;
import com.github.trace.intern.KafkaUtil;
import com.github.trace.utils.JsonLogHandler;
import com.github.trace.utils.NginxLogHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.trace.intern.KafkaPropertyConstant.*;

/**
 * 根据topic获取kafka中最新N条数据
 * Created by wzk on 16/3/17.
 */
@Service
public class KafkaService {

	private static final Logger LOGGER = LoggerFactory.getLogger( KafkaService.class );

	private static List< String > brokers = Lists.newArrayList();

	private static int port;
	private static int connectTimeout;
	private static int bufferSize;
	private static int fetchSize;

	@PostConstruct
	void init() {

		ConfigFactory.getInstance().getConfig( "kafka-simple-consumer", config -> {
			Preconditions.checkNotNull( config.get( BOOSTRAP_SERVERS ) );

			String servers = config.get( BOOSTRAP_SERVERS );
			brokers = Splitter.on( "," ).splitToList( servers );
			port = config.getInt( SERVER_PORT, SERVER_PORT_DEFAULT );
			connectTimeout = config.getInt( CONSUMER_CONNECT_TIMEOUT, CONSUMER_CONNECT_TIMEOUT_DEFAULT );
			bufferSize = config.getInt( CONSUMER_BUFFER_SIZE, CONSUMER_BUFFER_SIZE_DEFAULT );
			fetchSize = config.getInt( FETCH_SIZE, FETCH_SIZE_DEFAULT );
		} );
	}

	public long getOffsetSum( String topic ) {

		Set< KafkaMessageAndOffset > fetchedData = fetchData( topic, 1 );
		if ( fetchedData == null || fetchedData.isEmpty() ) {
			return 0L;
		}
		return fetchedData.stream().mapToLong( KafkaMessageAndOffset:: getOffset ).sum();
	}

	public Map< String, Long > getLastMessageTimestampWithIp( String topic ) {

		Map< String, Long >          ret         = Maps.newHashMap();
		Set< KafkaMessageAndOffset > fetchedData = fetchData( topic, 1000 );
		if ( fetchedData == null || fetchedData.isEmpty() ) {
			return ret;
		}

		boolean isNginx = StringUtils.startsWith( topic, "nginx" );

		for ( KafkaMessageAndOffset k : fetchedData ) {
			String message = k.getMessage();
			String ip;
			long   stamp;

			if ( isNginx ) {
				ip = NginxLogHandler.getIpFromNginxLog( message );
				stamp = NginxLogHandler.getStampFromNginxLog( message );
			} else {
				ip = JsonLogHandler.getIpFromLog( message );
				stamp = JsonLogHandler.getStampFromLog( message );
			}

			if ( ret.containsKey( ip ) ) {
				long lastStamp = ret.get( ip );
				if ( stamp > lastStamp ) {
					ret.put( ip, stamp );
				}
			} else {
				ret.put( ip, stamp );
			}
		}

		return ret;
	}

	public long getLastMessageTimestamp( String topic ) {

		Set< KafkaMessageAndOffset > fetchedData = fetchData( topic, 1 );
		if ( fetchedData == null || fetchedData.isEmpty() ) {
			return 0L;
		}

		boolean isNginx = StringUtils.startsWith( topic, "nginx" );

		List< Long > list = fetchedData.stream().map( k -> {
			String message = k.getMessage();
			return isNginx ? NginxLogHandler.getStampFromNginxLog( message )
					: JsonLogHandler.getStampFromLog( message );
		} ).collect( Collectors.toList() );
		LOGGER.info( list.toString() );
		Collections.sort( list, ( t1, t2 ) -> Objects.equals( t1, t2 ) ? 0 : ( t1 < t2 ? 1 : -1 ) );

		return list.get( 0 );
	}

	public Set< String > getMessages( String topic, int count ) {

		Set< KafkaMessageAndOffset > fetchedData = fetchData( topic, count );

		Set< String > results = Sets.newHashSet();
		for ( KafkaMessageAndOffset messageAndOffset : fetchedData ) {
			if ( results.size() > count ) {
				break;
			}
			results.add( messageAndOffset.getMessage() );
		}
		return parse( results, topic );
	}

	private Set< KafkaMessageAndOffset > fetchData( String topic, int count ) {

		Set< KafkaMessageAndOffset > fetchedData = Sets.newHashSet();

		TreeMap< Integer, PartitionMetadata > metaDatas = KafkaUtil.findLeader( brokers, port, topic );
		for ( Map.Entry< Integer, PartitionMetadata > entry : metaDatas.entrySet() ) {
			int    partition  = entry.getKey();
			String leadBroker = entry.getValue().leader().host();
			String clientName = "Client_" + topic + "_" + partition;

			SimpleConsumer consumer = new SimpleConsumer( leadBroker, port,
					connectTimeout, bufferSize, clientName );
			long lastOffset = KafkaUtil.getLastOffset( consumer, topic, partition,
					kafka.api.OffsetRequest.LatestTime(), clientName );
			if ( lastOffset > 0 ) {
				fetchedData.addAll( KafkaUtil.fetchData( consumer, topic, partition,
						clientName, lastOffset - count, fetchSize ) );
			}
			consumer.close();
		}
		return fetchedData;
	}

	private Set< String > parse( Set< String > logs, String topic ) {

		Set< String > results = Sets.newHashSet();
		if ( logs == null || logs.isEmpty() || Strings.isNullOrEmpty( topic ) ) {
			return results;
		}

		if ( StringUtils.startsWith( topic, "nginx" ) ) {
			results = NginxLogHandler.batchParse( logs );
		} else {
			results = logs;
		}
		return results;
	}

}
