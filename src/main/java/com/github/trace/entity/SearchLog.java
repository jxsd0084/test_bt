package com.github.trace.entity;

/**
 * Created by chenlong on 2016/4/8.
 */
public class SearchLog {

	private String topic;
	private String keyWord;
	private String highLightKey;
	private String tag;
	private long   startTime;
	private long   endTime;
	private int    pageStart;
	private int    pageSize;

	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getHighLightKey() {
		return highLightKey;
	}

	public void setHighLightKey(String highLightKey) {
		this.highLightKey = highLightKey;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
