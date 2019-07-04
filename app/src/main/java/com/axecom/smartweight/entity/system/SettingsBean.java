package com.axecom.smartweight.entity.system;

/**
 * Created by Administrator on 2018-5-16.
 */

public class SettingsBean {
    private int id;

    private int icon;
    private String  title;
    private int  flag;
    private  int color;

    public SettingsBean(int icon, String title, int flag) {
        this.icon = icon;
        this.title = title;
        this.flag = flag;
    }

    public SettingsBean(int icon, String title, int flag, int color) {
        this.icon = icon;
        this.title = title;
        this.flag = flag;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


}
