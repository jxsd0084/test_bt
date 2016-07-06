package com.github.trace.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * KafkaServiceTest
 * Created by wzk on 16/3/23.
 */
//@Ignore
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( "classpath:applicationContext.xml" )
@Slf4j
public class KafkaServiceTest {

	private static final Logger log = LoggerFactory.getLogger( KafkaServiceTest.class );

	@Autowired
	private KafkaService service;

	@Test
	public void testGetMessages() throws Exception {

		service.getMessages( "nginx.reverse", 10 );
	}

	@Test
	public void testGetLastMessageTimestamp() throws Exception {

		long ret = service.getLastMessageTimestamp( "dcx.WebRequest" );
//    long ret = service.getLastMessageTimestamp("nginx.reverse");
		log.info( "ret: {}", ret );
	}

	@Test
	public void testGetLastMessageTimestampWithIp() throws Exception {

		Map< String, Long > map = service.getLastMessageTimestampWithIp( "sfsfsfsf" );
		log.info( "==========================" );
		log.info( "result: {}", map );
		log.info( "==========================" );
	}

	@Test
	public void testGetOffsetSum() throws Exception {

		long offset = service.getOffsetSum( "dcx.MonitorRequest" );
		log.info( "Sum of offset: {}", offset );
	}

}
