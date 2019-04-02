package com.axecom.smartweight.carouselservice.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "AdImageInfo")
public class AdImageInfo {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String netPath;
    @DatabaseField
    private String localPath;
    @DatabaseField
    private String title;
    @DatabaseField
    private int type;
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AdImageInfo(String netPath, String title) {
        this.netPath = netPath;
        this.title = title;
    }

    public AdImageInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AdImageInfo{" +
                "id=" + id +
                ", netPath='" + netPath + '\'' +
                ", localPath='" + localPath + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}
