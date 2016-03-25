package com.github.trace.task;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * 定时拉取校验测试
 * Created by wzk on 16/3/24.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ValidateSchedulerTest {

  @Autowired
  ValidateScheduler scheduler;

  @Test
  public void testScheduler() throws Exception {
    scheduler.scheduler();
  }

}