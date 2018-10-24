//package com.axecom.smartweight.manager;
//
//import android.text.TextUtils;
//
//import com.axecom.smartweight.bean.HotKeyBean;
//import com.axecom.smartweight.bean.OrderGoods;
//import com.axecom.smartweight.bean.OrderLocal;
//
//import java.util.List;
//
///**
// * Created by Administrator on 2018/10/13.
// */
//
//public class RecordManage {
//    public static void record(
//            boolean record,
//            String companyName,
//            String bitmap, String orderNo,
//            String seller,
//            int sellerid, int tid,
//            int marketId, String payId,
//            String operator, String price,
//            String stallNumber, String time,
//            List<HotKeyBean> seledtedGoodsList) {
//        if(!record)return;
//
//        OrderLocal orderLocal = new OrderLocal();
//        orderLocal.companyName = companyName;
//        orderLocal.qrCode = bitmap;
//        orderLocal.orderNumber = orderNo;
//        orderLocal.seller = seller;
//        orderLocal.sellerid = sellerid;
//        orderLocal.tid = tid;
//        orderLocal.marketId = marketId;
//        orderLocal.payId = payId;
//        orderLocal.operator = operator;
//        orderLocal.stallNumber = stallNumber;
//        orderLocal.totalAmount = parseDouble(price);
//        orderLocal.orderTime = time;
//
//        for (HotKeyBean bean :
//                seledtedGoodsList) {
//            OrderGoods goods = new OrderGoods();
//            goods.orderNumber = orderNo;
//            goods.name = bean.name;
//            goods.weight = parseFloat(bean.weight);
//            goods.price = parseFloat(bean.price);
//            goods.amount =parseDouble(bean.grandTotal);
//        }
//    }
//
//    private static float parseFloat(String s) {
//        if (TextUtils.isEmpty(s)) return 0;
//        return Float.parseFloat(s);
//    }
//
//
//    private static double parseDouble(String s) {
//        if (TextUtils.isEmpty(s)) {
//            s = "0";
//        }
//        return Double.parseDouble(s);
//    }
//
//}
