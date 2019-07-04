//package com.axecom.smartweight.base;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class OrderInfo {
//
//    private String Service;
//    private String cmd;
//    //    用户标识号 字符	接口方提供
//    private String authenCode = "rerafdg";
//    //    应用渠道  字符	由平台管理人员提供
//    private String appCode = "EVFFDV";
//    //
////    器具出厂编号  	设备的唯一标识，12位纯数字
//    private String deviceNo = "3543rewrew";
//    //    器具型号规格
//    private String deviceModel = "V30";
//    //    器具厂家
//    private String factoryName = "香山";
//    //    出厂日期  字符	yy-MM-dd, 如：2018-10-12
//    private String productionDate = "2018-01-01";
//
//    //    MAC地址  字符	智能秤mac地址
//    private String macAddr = "65:fd:ad:34:53:45";
//
//    //    交易时间 字符	yy-MM-dd   HH：mm：ss
//    private String orderTime = "2019-03-18";
//    //    商户档位号 字符	不超过25位（传空值即可）
//    private String stallCode = "AXE1001";
//    //    经营主体 	字符	（传空值即可）
//    private String businessEntity = "蔬菜";
//    //    社会信用代码 字符	（传空值即可）
//    private String creditCode = "4244423345";
//    private List<OrderBean> list = new ArrayList<>();
//
//    public List<OrderBean> getList() {
//        return list;
//    }
//
//    public void setList(List<OrderBean> list) {
//        this.list = list;
//    }
//
//    public String getAuthenCode() {
//        return authenCode;
//    }
//
//    public void setAuthenCode(String authenCode) {
//        this.authenCode = authenCode;
//    }
//
//    public String getAppCode() {
//        return appCode;
//    }
//
//    public String getCmd() {
//        return cmd;
//    }
//
//    public void setCmd(String cmd) {
//        this.cmd = cmd;
//    }
//
//    public String getService() {
//        return Service;
//    }
//
//    public void setService(String service) {
//        Service = service;
//    }
//
//    public void setAppCode(String appCode) {
//        this.appCode = appCode;
//    }
//
//    public String getDeviceNo() {
//        return deviceNo;
//    }
//
//    public void setDeviceNo(String deviceNo) {
//        this.deviceNo = deviceNo;
//    }
//
//    public String getDeviceModel() {
//        return deviceModel;
//    }
//
//    public void setDeviceModel(String deviceModel) {
//        this.deviceModel = deviceModel;
//    }
//
//    public String getFactoryName() {
//        return factoryName;
//    }
//
//    public void setFactoryName(String factoryName) {
//        this.factoryName = factoryName;
//    }
//
//    public String getProductionDate() {
//        return productionDate;
//    }
//
//    public void setProductionDate(String productionDate) {
//        this.productionDate = productionDate;
//    }
//
//    public String getMacAddr() {
//        return macAddr;
//    }
//
//    public void setMacAddr(String macAddr) {
//        this.macAddr = macAddr;
//    }
//
//    public String getOrderTime() {
//        return orderTime;
//    }
//
//    public void setOrderTime(String orderTime) {
//        this.orderTime = orderTime;
//    }
//
//    public String getStallCode() {
//        return stallCode;
//    }
//
//    public void setStallCode(String stallCode) {
//        this.stallCode = stallCode;
//    }
//
//    public String getBusinessEntity() {
//        return businessEntity;
//    }
//
//    public void setBusinessEntity(String businessEntity) {
//        this.businessEntity = businessEntity;
//    }
//
//    public String getCreditCode() {
//        return creditCode;
//    }
//
//    public void setCreditCode(String creditCode) {
//        this.creditCode = creditCode;
//    }
//}
