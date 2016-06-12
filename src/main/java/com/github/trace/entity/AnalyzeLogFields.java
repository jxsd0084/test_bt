package com.github.trace.entity;

/**
 * Created by wangjiezhao on 2016/4/13.
 */
public class AnalyzeLogFields {

	private String level_tag;
	private String tag_group;
	private String d_level_tag;
	private String bp_value_desc;
	private String bp_name;
	private String bp_value;
	private int    is_checked;
	private String regex;
	private String tag;
	private String field_name;
	private String field_desc;
	private String field_type;
	private String field_regex;
	private String nav_name;
	private String topic;


	public String getNav_name() {

		return nav_name;
	}

	public String getTopic() {

		return topic;
	}

	public String getBp_name() {

		return bp_name;
	}

	public String getBp_value_desc() {

		return bp_value_desc;
	}

	public String getBp_value() {

		return bp_value;
	}

	public String getD_level_tag() {

		return d_level_tag;
	}

	public String getField_desc() {

		return field_desc;
	}

	public String getField_name() {

		return field_name;
	}

	public String getField_regex() {

		return field_regex;
	}

	public String getField_type() {

		return field_type;
	}

	public int getIs_checked() {

		return is_checked;
	}

	public String getLevel_tag() {

		return level_tag;
	}

	public String getRegex() {

		return regex;
	}

	public String getTag() {

		return tag;
	}

	public String getTag_group() {

		return tag_group;
	}
}
