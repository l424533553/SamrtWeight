package com.xuanyuan.library.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 作者：罗发新
 * 时间：2018/12/5 0005    17:14
 * 邮件：424533553@qq.com
 * 说明：
 */

@DatabaseTable(tableName = "Deviceinfo")
public class Deviceinfo {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String release = "4.1.2";  //系统版本	RELEASE	获取系统版本字符串。如4.1.2 或2.2 或2.3等	4.4.4
    @DatabaseField
    private String sdk = "19";   //系统版本值 SDK 系统的API级别 一般使用下面大的SDK_INT 来查看 19
    @DatabaseField
    private String brand = "华为";   //品牌 BRAND 获取设备品牌 Huawei
    @DatabaseField
    private String model = "荣耀9";  // 型号 MODEL

    @DatabaseField
    private String networkoperatorname = "中国联通";   // 网络类型名 getNetworkOperatorName 返回移动网络运营商的名字(SPN)中国联通
    @DatabaseField
    private int networktype = 3;   //网络类型 getNetworkType 3
    @DatabaseField
    private int phonetype = 1;    //手机类型 getPhoneType 手机类型 1
    @DatabaseField
    private String mac = "53:ff:a4:53:65:te";        //mac地址 Address
     @DatabaseField
    private String radioVersion = "64676";        //固件版本



    public Deviceinfo(String release, String sdk, String brand, String model, String networkoperatorname, int networktype, int phonetype, String mac, String radioVersion) {
        this.release = release;
        this.sdk = sdk;
        this.brand = brand;
        this.model = model;
        this.networkoperatorname = networkoperatorname;
        this.networktype = networktype;
        this.phonetype = phonetype;
        this.mac = mac;
        this.radioVersion = radioVersion;
    }

    /**
     * 设备信息
     */
    public Deviceinfo() {


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNetworkoperatorname() {
        return networkoperatorname;
    }

    public void setNetworkoperatorname(String networkoperatorname) {
        this.networkoperatorname = networkoperatorname;
    }

    public int getNetworktype() {
        return networktype;
    }

    public void setNetworktype(int networktype) {
        this.networktype = networktype;
    }

    public int getPhonetype() {
        return phonetype;
    }

    public void setPhonetype(int phonetype) {
        this.phonetype = phonetype;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


}
