package com.axecom.smartweight.rzl.utils;
/*版本控制的实体类*/
public class Version {
    public float version;//服务器端的新版本号
    public String description;//更新的内容描述
    public String date;//更新日期
    public int marketId;//市场编号
    public String apkPath;//apk路径
    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
}
