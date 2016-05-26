package com.github.trace.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Navigation0ServiceTest
 * Created by wzk on 16/5/26.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Navigation0ServiceTest {

  @Autowired
  private Navigation0Service navigation0Service;

  @Test
  public void updateConfig() throws Exception {
    navigation0Service.updateConfig();
    Thread.sleep(10000L);
  }
}