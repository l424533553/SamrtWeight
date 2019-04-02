package com.axecom.smartweight.fpms_chinaap;

/**
 * 作者：罗发新
 * 时间：2019/2/12 0012    15:00
 * 邮件：424533553@qq.com
 * 说明：
 */
public class OrderFpmsInfo {

    //用户标识号
    private String authenCode;
    //应用渠道
    private String appCode = "FPMSWS";
    //器具出厂编号
    private String deviceNo;
    //器具型号规格
    private String deviceModel;
    // 器具厂家
    private String factoryName;
    // 出厂日期
    private String productionDate;
    // MAC地址
    private String macAddr;
    //交易流水
    private String orderNo;
    //商品编码
    private String goodsCode;
    //商品名称
    private String goodsName;
    // 单价（kg）
    private String price;
    //重量（kg）
    private String weight;
    //交易金额
    private String amounts;
    // 原始零位值
    private String initAd;
    // 除皮零位值
    private String zeroAd;
    //称重值AD
    private String weightAd;
    //交易时间
    private String orderTime;
    //商户档位号
    private String stallCode = "";
    //经营主体
    private String businessEntity = "";
    //社会信用代码
    private String creditCode = "";
}
