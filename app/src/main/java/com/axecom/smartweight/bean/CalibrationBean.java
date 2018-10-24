package com.axecom.smartweight.bean;

public class CalibrationBean {

    private String adminToken;
    private String max_ange;
    private String calibration_value;
    private String dividing_value;
    private String standard_weighing;
    private String scales_id;


    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String token) {
        this.adminToken = token;
    }

    public String getMax_ange() {
        return max_ange;
    }

    public void setMax_ange(String max_ange) {
        this.max_ange = max_ange;
    }

    public String getCalibration_value() {
        return calibration_value;
    }

    public void setCalibration_value(String calibration_value) {
        this.calibration_value = calibration_value;
    }

    public String getDividing_value() {
        return dividing_value;
    }

    public void setDividing_value(String dividing_value) {
        this.dividing_value = dividing_value;
    }

    public String getStandard_weighing() {
        return standard_weighing;
    }

    public void setStandard_weighing(String standard_weighing) {
        this.standard_weighing = standard_weighing;
    }

    public String getScales_id() {
        return scales_id;
    }

    public void setScales_id(String scales_id) {
        this.scales_id = scales_id;
    }
}
