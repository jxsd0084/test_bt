package com.github.trace.entity;

/**
 * 封装的kafka消息
 * Created by wzk on 16/3/18.
 */
public class KafkaMessageAndOffset {

  long offset;
  String message;

  public KafkaMessageAndOffset(long offset, String message) {
    this.offset = offset;
    this.message = message;
  }

  public long getOffset() {
    return offset;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
