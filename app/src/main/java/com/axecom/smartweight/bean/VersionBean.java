package com.axecom.smartweight.bean;

public class VersionBean {
    public final String version;
    public final String downloadurl;
    public final String description;

    public VersionBean(String version, String downloadurl, String description) {
        this.version = version;
        this.downloadurl = downloadurl;
        this.description = description;
    }
}
