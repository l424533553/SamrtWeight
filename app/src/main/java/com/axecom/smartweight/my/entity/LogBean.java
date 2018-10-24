package com.axecom.smartweight.my.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * author: luofaxin
 * date： 2018/9/27 0027.
 * email:424533553@qq.com
 * describe:
 */
@DatabaseTable(tableName = "logbean")
public class LogBean {

    @DatabaseField(generatedId = true, useGetSet = true)
    private int id;
    @DatabaseField(useGetSet = true)
    private String type;
    @DatabaseField(useGetSet = true)
    private String message;
    @DatabaseField(useGetSet = true)
    private String time;
    @DatabaseField(useGetSet = true)
    private String location;

    public LogBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
