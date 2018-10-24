package com.axecom.smartweight.bean;

public class VersionBean {
    public String version;
    public String downloadurl;
    public String description;

    public VersionBean(String version, String downloadurl, String description) {
        this.version = version;
        this.downloadurl = downloadurl;
        this.description = description;
    }
}
