package com.github.trace.entity;

public class BuriedPoint {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.bp_name
     *
     * @mbggenerated
     */
    private String bpName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.bp_value
     *
     * @mbggenerated
     */
    private String bpValue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.bp_value_desc
     *
     * @mbggenerated
     */
    private String bpValueDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.parent_id
     *
     * @mbggenerated
     */
    private Integer parentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.parent_name
     *
     * @mbggenerated
     */
    private String parentName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.child_id
     *
     * @mbggenerated
     */
    private Integer childId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.child_name
     *
     * @mbggenerated
     */
    private String childName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.is_checked
     *
     * @mbggenerated
     */
    private Boolean isChecked;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column buried_point.regex
     *
     * @mbggenerated
     */
    private String regex;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.id
     *
     * @return the value of buried_point.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.id
     *
     * @param id the value for buried_point.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.bp_name
     *
     * @return the value of buried_point.bp_name
     *
     * @mbggenerated
     */
    public String getBpName() {
        return bpName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.bp_name
     *
     * @param bpName the value for buried_point.bp_name
     *
     * @mbggenerated
     */
    public void setBpName(String bpName) {
        this.bpName = bpName == null ? null : bpName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.bp_value
     *
     * @return the value of buried_point.bp_value
     *
     * @mbggenerated
     */
    public String getBpValue() {
        return bpValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.bp_value
     *
     * @param bpValue the value for buried_point.bp_value
     *
     * @mbggenerated
     */
    public void setBpValue(String bpValue) {
        this.bpValue = bpValue == null ? null : bpValue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.bp_value_desc
     *
     * @return the value of buried_point.bp_value_desc
     *
     * @mbggenerated
     */
    public String getBpValueDesc() {
        return bpValueDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.bp_value_desc
     *
     * @param bpValueDesc the value for buried_point.bp_value_desc
     *
     * @mbggenerated
     */
    public void setBpValueDesc(String bpValueDesc) {
        this.bpValueDesc = bpValueDesc == null ? null : bpValueDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.parent_id
     *
     * @return the value of buried_point.parent_id
     *
     * @mbggenerated
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.parent_id
     *
     * @param parentId the value for buried_point.parent_id
     *
     * @mbggenerated
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.parent_name
     *
     * @return the value of buried_point.parent_name
     *
     * @mbggenerated
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.parent_name
     *
     * @param parentName the value for buried_point.parent_name
     *
     * @mbggenerated
     */
    public void setParentName(String parentName) {
        this.parentName = parentName == null ? null : parentName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.child_id
     *
     * @return the value of buried_point.child_id
     *
     * @mbggenerated
     */
    public Integer getChildId() {
        return childId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.child_id
     *
     * @param childId the value for buried_point.child_id
     *
     * @mbggenerated
     */
    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.child_name
     *
     * @return the value of buried_point.child_name
     *
     * @mbggenerated
     */
    public String getChildName() {
        return childName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.child_name
     *
     * @param childName the value for buried_point.child_name
     *
     * @mbggenerated
     */
    public void setChildName(String childName) {
        this.childName = childName == null ? null : childName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.is_checked
     *
     * @return the value of buried_point.is_checked
     *
     * @mbggenerated
     */
    public Boolean getIsChecked() {
        return isChecked;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.is_checked
     *
     * @param isChecked the value for buried_point.is_checked
     *
     * @mbggenerated
     */
    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column buried_point.regex
     *
     * @return the value of buried_point.regex
     *
     * @mbggenerated
     */
    public String getRegex() {
        return regex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column buried_point.regex
     *
     * @param regex the value for buried_point.regex
     *
     * @mbggenerated
     */
    public void setRegex(String regex) {
        this.regex = regex == null ? null : regex.trim();
    }
}