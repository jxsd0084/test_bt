package com.github.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 封装的kafka消息
 * Created by wzk on 16/3/18.
 */
@Data
@AllArgsConstructor
public class KafkaMessageAndOffset {

  long offset;
  String message;

}
