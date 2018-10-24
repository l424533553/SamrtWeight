package com.luofx.entity;

/**
 * 说明：版本更新实体类
 * 作者：User_luo on 2018/6/12 16:44
 * 邮箱：424533553@qq.com
 */
public class VersionInfo {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String versionName;//版本名
    private int   versionCode;//版本号
    /**
     * apkName命名有一定的规则  WTR02_+versionName+ .apk
     * 每版的apk 名字都应该不一样
     */
    private String apkName;
    private String versionDesc;//版本描述信息内容
    private String downloadUrl;//新版本的下载路径
    private String versionSize;//版本大小


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVersionSize() {
        return versionSize;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public void setVersionSize(String versionSize) {
        this.versionSize = versionSize;
    }
    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public int getVersionCode() {
        return versionCode;
    }
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
    public String getVersionDesc() {
        return versionDesc;
    }
    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

}