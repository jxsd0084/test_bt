package com.github.trace.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * KafkaServiceTest
 * Created by wzk on 16/3/23.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@Slf4j
public class KafkaServiceTest {

  @Autowired
  private KafkaService service;

  @Test
  public void testGetMessages() throws Exception {
    service.getMessages("nginx.reverse", 10);
  }

  @Test
  public void testGetLastMessageTimestamp() throws Exception {
    long ret = service.getLastMessageTimestamp("dcx.WebRequest");
//    long ret = service.getLastMessageTimestamp("nginx.reverse");
    log.info(ret + "");
  }
}