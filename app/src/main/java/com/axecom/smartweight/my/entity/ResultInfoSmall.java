package com.axecom.smartweight.my.entity;

/**
 * 作者：罗发新
 * 时间：2019/4/18 0018    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public class ResultInfoSmall {
    /**
     * url : https://data.axebao.com/smartsz/upload/qrcode/20190418051639.png
     * code : 0
     */

    //网络地址
    private String url;
    // 0: 请求成功    1：请求失败
    private int code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
