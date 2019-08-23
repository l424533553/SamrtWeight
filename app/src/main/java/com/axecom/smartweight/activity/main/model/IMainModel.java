package com.axecom.smartweight.activity.main.model;

import com.axecom.smartweight.entity.project.HotGood;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/6/10 0010    星期一
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface IMainModel {
    List<HotGood> getHotGoods();

    //    更改对应流水号的订单状态 是否上传成功   0（待支付）———> 1(支付完成)
    void updateOrderBillcode(String billcode);

    void insertOrderInfo(OrderInfo orderInfo);

    boolean updateOrInsert(OrderInfo orderInfo);

    List<OrderInfo> queryByState();

    int insertOrderBean(List<OrderBean> orderBeans);

}
