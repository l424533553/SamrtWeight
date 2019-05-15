package com.axecom.smartweight.net;


import com.axecom.smartweight.base.BaseEntity;
import com.axecom.smartweight.bean.CalibrationBean;
import com.axecom.smartweight.bean.FastLoginInfo;
import com.axecom.smartweight.bean.LocalSettingsBean;
import com.axecom.smartweight.bean.LoginData;
import com.axecom.smartweight.bean.LoginInfo;
import com.axecom.smartweight.bean.OrderListResultBean;
import com.axecom.smartweight.bean.PayNoticeBean;
import com.axecom.smartweight.bean.ReportResultBean;
import com.axecom.smartweight.bean.SaveGoodsReqBean;
import com.axecom.smartweight.bean.SubOrderBean;
import com.axecom.smartweight.bean.SubOrderReqBean;
import com.axecom.smartweight.bean.UnusualOrdersBean;
import com.axecom.smartweight.bean.VersionBean;
import com.axecom.smartweight.bean.WeightBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-11-27.
 */

public interface RequestInterface {
    @GET("test.php")
    Observable<String> test();

    /**
     * 获取设备Id
     */
    @POST("getScalesIdByMac")
    Observable<BaseEntity<WeightBean>> getScalesIdByMac(@Query("mac") String mac);

    /**
     * 用户登录
     */
    @POST("clientLogin")
    Observable<BaseEntity<LoginData>> clientLogin(@Query("scales_id") String scalesId, @Query("serial_number") String serialNumber, @Query("password") String password);

    /**
     * 司磅员登录
     */
    @POST("staffMemberLogin")
    Observable<BaseEntity<LoginData>> staffMemberLogin(@Query("scales_id") String scalesId, @Query("serial_number") String serialNumber, @Query("password") String password);


    /**
     * 快捷登录
     */
    @POST("fastLogin")
    Observable<BaseEntity<FastLoginInfo>> fastLogin(@Query("scales_id") String scalesId, @Query("client_id") String client_id);

    /**
     * 标定记录
     */
    @POST("storeCalibrationRecord")
    Observable<BaseEntity> storeCalibrationRecord(@Body CalibrationBean calibrationBean);

    /**
     * 上报服务器智能秤在线

     */
    @POST("isOnline")
    Observable<BaseEntity> isOnline(@Query("token") String token, @Query("scales_id") String scalesId);

    /**
     * 测试连接服务器
     */
    @GET("testConnection")
    Observable<BaseEntity> testConnection();

//    /**
//     * 获取商品
//     *
//     * @param token
//     * @param mac
//     * @return
//     */
//    @POST("getScalesCategoryGoods")
//    Observable<BaseEntity<ScalesCategoryGoods>> getScalesCategoryGoods(@Query("token") String token, @Query("mac") String mac);


    /**
     * 提交订单
     */
    @POST("submitOrder")
    Observable<BaseEntity<SubOrderBean>> submitOrder(@Body SubOrderReqBean subOrderReqBean);

    /**
     * 获取系统设置数据
     */
    @POST("getSettingData")
    Observable<BaseEntity> getSettingData(@Query("token") String token, @Query("mac") String mac);

    /**
     * 日报表与月报表
     */
    @POST("getReportsList")
    Observable<BaseEntity<ReportResultBean>> getReportsList(@Query("token") String token, @Query("mac") String mac, @Query("dateVal") String dateVal, @Query("typeVal") String typeVal, @Query("page") String page, @Query("pageNum") String pageNum);

    /**
     * 数据汇总 / 销售明细
     */
    @POST("getOrderList")
    Observable<BaseEntity<OrderListResultBean>> getOrderList(@Query("token") String token, @Query("mac") String mac, @Query("dateVal") String dateVal, @Query("page") String page, @Query("pageNum") String pageNum);

    /**
     * 获取本机设置数据
     */
    @POST("getScalesSettingData")
    Observable<BaseEntity<LocalSettingsBean>> getScalesSettingData(@Query("token") String token, @Query("mac") String mac);

    /**
     * 获取异常和作废订单订单数据
     */
    @POST("getOrders")
    Observable<BaseEntity<UnusualOrdersBean>> getOrders(@Query("token") String token, @Query("mac") String mac, @Query("page") String page, @Query("pageNum") String pageNum, @Query("typeVal") String typeVal);

    /**
     * 商品管理 保存设置
     */
    @POST("storeGoodsData")
    Observable<BaseEntity> storeGoodsData(@Body SaveGoodsReqBean goodsReqBean);

    /**
     * 订单作废操作
     */
    @POST("invalidOrders")
    Observable<BaseEntity> invalidOrders(@Query("token") String token, @Query("mac") String mac, @Query("orderNo") String orderNo);

    /**
     * 获取基本信息
     * 地址：/api/getLoginInfo
     * 类型：POST
     * 状态码：200
     * 简介：获取当前登录信息，离线情况下默认为“暂无”
     */
    @POST("getLoginInfo")
    Observable<BaseEntity<LoginInfo>> getLoginInfo(@Query("token") String token, @Query("mac") String mac);

    /**
     * 获取最新版本
     * 地址：/api/getVersion
     * 类型：GET
     * 状态码：200
     */
    @GET("getVersion")
    Observable<BaseEntity<VersionBean>> getVersion(@Query("id") String id);

    /**
     * 获取订单支付情况
     * 地址：/api/getPayNotice
     * 类型：POST
     * 状态码：200
     */
    @POST("getPayNotice")
    Observable<BaseEntity<PayNoticeBean>> getPayNotice(@Query("order_no") String order_no);

    /**
     * 批量提交订单
     * 地址：/api/submitOrders
     * 类型：POST
     * 状态码：200
     */
    @POST("submitOrders")
    Observable<BaseEntity> submitOrders(@Body List<SubOrderReqBean> list);
}
