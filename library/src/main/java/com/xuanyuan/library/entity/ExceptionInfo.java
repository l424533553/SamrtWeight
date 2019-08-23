package com.xuanyuan.library.entity;


/**
 * 作者：罗发新
 * 时间：2018/12/5 0005    16:05
 * 邮件：424533553@qq.com
 * 说明：崩溃的 异常收集信息
 */
public class ExceptionInfo {

    private int steelyardtype = 0;//0:商通称,1：香山7.0,2:香山15.6-1:其他型号秤待定
    private Errorinfo errorinfo;
    private Deviceinfo deviceinfo;


    public int getSteelyardtype() {
        return steelyardtype;
    }

    public void setSteelyardtype(int steelyardtype) {
        this.steelyardtype = steelyardtype;
    }

    public Errorinfo getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(Errorinfo errorinfo) {
        this.errorinfo = errorinfo;
    }

    public Deviceinfo getDeviceinfo() {
        return deviceinfo;
    }

    public void setDeviceinfo(Deviceinfo deviceinfo) {
        this.deviceinfo = deviceinfo;
    }


//    private static class Userinfo {
//
//        private int marketid = 11; //int  市场编号
//        private String marketname = "黄田市场";
//        private String companyno = "B070";
//        private int tid = 101;  //秤号
//        private String seller = "郭金龙";
//        private int sellerid = 127;
//
//        public int getMarketid() {
//            return marketid;
//        }
//
//        public void setMarketid(int marketid) {
//            this.marketid = marketid;
//        }
//
//        public String getMarketname() {
//            return marketname;
//        }
//
//        public void setMarketname(String marketname) {
//            this.marketname = marketname;
//        }
//
//        public String getCompanyno() {
//            return companyno;
//        }
//
//        public void setCompanyno(String companyno) {
//            this.companyno = companyno;
//        }
//
//        public int getTid() {
//            return tid;
//        }
//
//        public void setTid(int tid) {
//            this.tid = tid;
//        }
//
//        public String getSeller() {
//            return seller;
//        }
//
//        public void setSeller(String seller) {
//            this.seller = seller;
//        }
//
//        public int getSellerid() {
//            return sellerid;
//        }
//
//        public void setSellerid(int sellerid) {
//            this.sellerid = sellerid;
//        }
//    }





    /* * 方式************/

    /*data: {
steelyardtype:0  int
        userinfo：{
             marketid:11  int  市场编号
              marketname:"黄田市场"  String
              companyno:"B070"  String
           tid:101   int
           seller:"郭金龙"  String
           sellerid:127  int
        }

        errorinfo：{
            classpath:"com.axecom.smartweight.entity.MyBuglyActivity"
            errormessage:" fasf "  String
            errortime:" 2018-01-01"  String
        }

        deviceinfo:{
            release:""  String  //系统版本	RELEASE	获取系统版本字符串。如4.1.2 或2.2 或2.3等	4.4.4
            sdk：""  String 系统版本值 SDK 系统的API级别 一般使用下面大的SDK_INT 来查看 19
            brand:""  String 品牌 BRAND 获取设备品牌 Huawei
            model:""  String 型号 MODEL

            networkoperatorname:"" String   网络类型名 getNetworkOperatorName 返回移动网络运营商的名字(SPN)中国联通
            networktype："" String  网络类型 getNetworkType 3
            phonetype："" 手机类型 getPhoneType 手机类型 1
            mac：""mac地址 getMacAddress

        }
}*/


}
