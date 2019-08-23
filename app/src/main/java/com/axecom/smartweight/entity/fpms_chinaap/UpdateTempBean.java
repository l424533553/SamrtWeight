package com.axecom.smartweight.entity.fpms_chinaap;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/7/12 0012    星期五
 * 邮件：424533553@qq.com
 * 说明：上传统计的临时变量
 */
public class UpdateTempBean {
    /**
     * 上传订单号
     */
    private String OrderNo;
    /**
     * 商品名
     */
    private List<String> orderNames;
    /**
     * 0 未上传成功
     */
    private int state;

    public UpdateTempBean() {
        orderNames=new ArrayList<>();
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public List<String> getOrderNames() {
        return orderNames;
    }

    public void setOrderNames(List<String> orderNames) {
        this.orderNames = orderNames;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
