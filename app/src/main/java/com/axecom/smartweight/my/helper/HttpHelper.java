package com.axecom.smartweight.my.helper;

import com.alibaba.fastjson.JSON;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.luofx.listener.VolleyListener;
import com.luofx.listener.VolleyStringListener;
import com.shangtongyin.tools.serialport.IConstants_ST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shangtongyin.tools.serialport.IConstants_ST.BASE_IP_ST;

/**
 * author: luofaxin
 * date： 2018/9/10 0010.
 * email:424533553@qq.com
 * describe:
 */
public class HttpHelper implements IConstants_ST {
    private VolleyListener listener;
    SysApplication application;

    public void setApplication(SysApplication application) {
        this.application = application;
    }

    public void setListener(VolleyListener listener) {
        this.listener = listener;
    }

    public HttpHelper(VolleyListener listener, SysApplication application) {
        this.listener = listener;
        this.application = application;
    }

    /**
     * @param marketid     市场编号
     * @param terid        秤id
     * @param flag         设备状态   0正常/1异常
     * @param requestIndex 请求索引
     */
    public void upState(int marketid, int terid, int flag, int requestIndex) {
        String url = BASE_IP_ST + "/api/smartsz/addatatus?marketid=" + marketid + "&terid=" + terid + "&flag=" + flag;
        application.volleyGet(url, listener, requestIndex);
    }


    /**
     * 订单信息
     *
     * @param orderInfo 订单信息
     * @param flag      表示
     */
    public void commitDD(OrderInfo orderInfo, VolleyStringListener volleyStringListener, int flag) {
        String url = BASE_IP_ST + "/api/smart/commitszex?";


        Map<String, String> map = new HashMap<>();
        map.put("marketid", String.valueOf(orderInfo.getMarketid()));
        map.put("billcode", orderInfo.getBillcode());
        map.put("billstatus", orderInfo.getBillstatus());
        map.put("seller", orderInfo.getSeller());
        map.put("totalamount", orderInfo.getTotalamount());
        map.put("totalweight", orderInfo.getTotalweight());

        map.put("sellerid", String.valueOf(orderInfo.getSellerid()));
        map.put("settlemethod", String.valueOf(orderInfo.getSettlemethod()));
        map.put("terid", String.valueOf(orderInfo.getTerid()));
        map.put("time", orderInfo.getTime());

        List<OrderBean> items = orderInfo.getOrderItem();
        if (items != null) {
            String jsonItem = JSON.toJSONString(items);
            map.put("items", jsonItem);
        }
        application.volleyPost(url, map, volleyStringListener, flag);
    }


    public void askOrder(String mchid, String orderno, VolleyListener volleyListener, int flag) {
        String url = BASE_IP_ST + "/api/pay/check?mchid=" + mchid + "&orderno=" + orderno;
        application.volleyGet(url, volleyListener, flag);
    }


    /**
     * 根据mac地址获得
     */
    private void getUserInfo(int flag) {
        String url = BASE_IP_ST + "/api/smart/getinfobymac?";
        application.volleyGet(url, listener, flag);
    }

    /**
     * 通过mac 获得用户信息
     *
     * @param mac  机器的mac地址
     * @param flag 请求浮标
     */
    public void getUserInfo(VolleyListener volleyListener, String mac, int flag) {
        String url = BASE_IP_ST + "/api/smart/getinfobymac?mac=" + mac;
        application.volleyGet(url, volleyListener, flag);
    }
}
