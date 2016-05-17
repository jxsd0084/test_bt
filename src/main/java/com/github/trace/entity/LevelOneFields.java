package com.github.trace.entity;

public class LevelOneFields {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column level_one_fields.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column level_one_fields.level1_field_tag
     *
     * @mbggenerated
     */
    private String level1FieldTag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column level_one_fields.level1_field_name
     *
     * @mbggenerated
     */
    private String level1FieldName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column level_one_fields.level1_field_desc
     *
     * @mbggenerated
     */
    private String level1FieldDesc;


    // 自定义属性
    private Integer m99FieldsCount;
    //导航栏ID
    private Integer navigationId;

    private String creator;

    private String modifier;

    private int status = 1;

    public Integer getNavigationId() {
        return navigationId;
    }

    public void setNavigationId(Integer navigationId) {
        this.navigationId = navigationId;
    }

    public Integer getM99FieldsCount() {
        return m99FieldsCount;
    }

    public void setM99FieldsCount(Integer m99FieldsCount) {
        this.m99FieldsCount = m99FieldsCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column level_one_fields.id
     *
     * @return the value of level_one_fields.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column level_one_fields.id
     *
     * @param id the value for level_one_fields.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column level_one_fields.level1_field_tag
     *
     * @return the value of level_one_fields.level1_field_tag
     *
     * @mbggenerated
     */
    public String getLevel1FieldTag() {
        return level1FieldTag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column level_one_fields.level1_field_tag
     *
     * @param level1FieldTag the value for level_one_fields.level1_field_tag
     *
     * @mbggenerated
     */
    public void setLevel1FieldTag(String level1FieldTag) {
        this.level1FieldTag = level1FieldTag == null ? null : level1FieldTag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column level_one_fields.level1_field_name
     *
     * @return the value of level_one_fields.level1_field_name
     *
     * @mbggenerated
     */
    public String getLevel1FieldName() {
        return level1FieldName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column level_one_fields.level1_field_name
     *
     * @param level1FieldName the value for level_one_fields.level1_field_name
     *
     * @mbggenerated
     */
    public void setLevel1FieldName(String level1FieldName) {
        this.level1FieldName = level1FieldName == null ? null : level1FieldName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column level_one_fields.level1_field_desc
     *
     * @return the value of level_one_fields.level1_field_desc
     *
     * @mbggenerated
     */
    public String getLevel1FieldDesc() {
        return level1FieldDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column level_one_fields.level1_field_desc
     *
     * @param level1FieldDesc the value for level_one_fields.level1_field_desc
     *
     * @mbggenerated
     */
    public void setLevel1FieldDesc(String level1FieldDesc) {
        this.level1FieldDesc = level1FieldDesc == null ? null : level1FieldDesc.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LevelOneFields{" +
                "id=" + id +
                ", level1FieldTag='" + level1FieldTag + '\'' +
                ", level1FieldName='" + level1FieldName + '\'' +
                ", level1FieldDesc='" + level1FieldDesc + '\'' +
                ", m99FieldsCount=" + m99FieldsCount +
                ", navigationId=" + navigationId +
                ", creator='" + creator + '\'' +
                ", modifier='" + modifier + '\'' +
                ", status=" + status +
                '}';
    }
}
