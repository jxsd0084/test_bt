package com.github.trace.entity;

/**
 * Created by wanghl on 2016/4/1.
 */
public class BuriedPoint0 {

	private Integer id;

	private String bpName;

	private String bpValue;

	private String bpValueDesc;

	private Integer navigationId;

	private Integer isChecked;

	private String regex;

	public Integer getId() {

		return id;
	}

	public void setId( Integer id ) {

		this.id = id;
	}

	public String getBpName() {

		return bpName;
	}

	public void setBpName( String bpName ) {

		this.bpName = bpName;
	}

	public String getBpValue() {

		return bpValue;
	}

	public void setBpValue( String bpValue ) {

		this.bpValue = bpValue;
	}

	public String getBpValueDesc() {

		return bpValueDesc;
	}

	public void setBpValueDesc( String bpValueDesc ) {

		this.bpValueDesc = bpValueDesc;
	}

	public Integer getNavigationId() {

		return navigationId;
	}

	public void setNavigationId( Integer navigationId ) {

		this.navigationId = navigationId;
	}

	public Integer getIsChecked() {

		return isChecked;
	}

	public void setIsChecked( Integer isChecked ) {

		this.isChecked = isChecked;
	}

	public String getRegex() {

		return regex;
	}

	public void setRegex( String regex ) {

		this.regex = regex;
	}

	@Override
	public String toString() {

		return "BuriedPoint0{" +
		       "id=" + id +
		       ", bpName='" + bpName + '\'' +
		       ", bpValue='" + bpValue + '\'' +
		       ", bpValueDesc='" + bpValueDesc + '\'' +
		       ", navigationId=" + navigationId +
		       ", isChecked=" + isChecked +
		       ", regex='" + regex + '\'' +
		       '}';
	}
}
