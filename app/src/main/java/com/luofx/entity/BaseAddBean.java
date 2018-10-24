package com.luofx.entity;

/**
 * 说明：
 * 作者：User_luo on 2018/7/27 10:29
 * 邮箱：424533553@qq.com
 */
public class BaseAddBean {
    private String addTime;
    private String baseNo;
    private String baseName;
    private String baseArea;
    private String dataUnit="亩";
    private String latitude;
    private String longitude;
    private String baseRemark;




    public BaseAddBean(String addTime, String baseNo, String baseName, String baseArea, String latitude, String longitude, String baseRemark) {
        this.addTime = addTime;
        this.baseNo = baseNo;
        this.baseName = baseName;
        this.baseArea = baseArea;
        this.latitude = latitude;
        this.longitude = longitude;
        this.baseRemark = baseRemark;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getBaseNo() {
        return baseNo;
    }

    public void setBaseNo(String baseNo) {
        this.baseNo = baseNo;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseArea() {
        return baseArea;
    }

    public void setBaseArea(String baseArea) {
        this.baseArea = baseArea;
    }

    public String getDataUnit() {
        return dataUnit;
    }

    public void setDataUnit(String dataUnit) {
        this.dataUnit = dataUnit;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getBaseRemark() {
        return baseRemark;
    }

    public void setBaseRemark(String baseRemark) {
        this.baseRemark = baseRemark;
    }
}
