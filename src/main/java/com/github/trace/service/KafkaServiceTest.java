package com.github.trace.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wzk on 16/3/18.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class KafkaServiceTest {

  @Autowired
  KafkaService service;

  @Test
  public void testGetMessages() throws Exception {
    service.getMessages("nginx.reverse", "json", 10);
  }
}