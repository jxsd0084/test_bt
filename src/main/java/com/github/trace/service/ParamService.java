package com.github.trace.service;

import com.github.trace.entity.BpJob;
import com.github.trace.mapper.BpJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenlong on 2016/4/1.
 */

@Service
public class ParamService {

  @Autowired
  private BpJobMapper bpJobMapper;

  public List<BpJob> getBpJobList() {
    return bpJobMapper.findAll();
  }

}
