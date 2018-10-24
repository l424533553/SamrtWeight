package com.axecom.smartweight.bean;

/**
 * Created by Administrator on 2018-5-16.
 */

public class SettingsBean {
    private int id;

    private int icon;
    private String  title;
    private int  flag;

    public SettingsBean(int icon, String title, int flag) {
        this.icon = icon;
        this.title = title;
        this.flag = flag;
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
