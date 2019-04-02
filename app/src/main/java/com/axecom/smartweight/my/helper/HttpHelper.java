package com.axecom.smartweight.my.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.dao.OrderInfoDao;
import com.luofx.listener.OkHttpListener;
import com.luofx.listener.VolleyListener;
import com.luofx.listener.VolleyStringListener;
import com.luofx.utils.net.NetWorkJudge;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * author: luofaxin
 * date： 2018/9/10 0010.
 * email:424533553@qq.com
 * describe:
 */
public class HttpHelper implements IConstants {
    //    private VolleyListener listener;
    private SysApplication application;

    private static HttpHelper mInstants;

    public static HttpHelper getmInstants(SysApplication application) {
        if (mInstants == null) {
            mInstants = new HttpHelper(application);
        }
        return mInstants;
    }

    private HttpHelper(SysApplication application) {
        this.application = application;
    }

    public void setApplication(SysApplication application) {
        this.application = application;
    }

    /**
     * @param marketid     市场编号
     * @param terid        秤id
     * @param flag         设备状态   0正常/1异常
     * @param requestIndex 请求索引    返回数据  data =0 正常  。data=1 禁用
     */
    public void upState(int marketid, VolleyListener listener, int terid, int flag, int requestIndex) {
        if (marketid < 0 || terid < 0) {
            return;
        }
        String url = BASE_IP_ST + "/api/smartsz/addatatus?marketid=" + marketid + "&terid=" + terid + "&flag=" + flag;
        application.volleyGet(url, listener, requestIndex);
    }

    /**
     * 加密3Dex
     */
    //通过
    public void upStateEx(int marketid, VolleyListener listener, int terid, int flag, int requestIndex) {
        if (marketid < 0 || terid < 0) {
            return;
        }
        String data = "{\"marketid\":\"" + marketid + "\",\"terid\":\"" + terid + "\",\"flag\":\"" + flag + "\"}";
        String desdata = application.getDesBCBHelper().encode(data);
        if (desdata == null) {
            return;
        }
        String url = BASE_IP_ST + "/api/smartsz/addatatusex?desdata=" + desdata;
        application.volleyGet(url, listener, requestIndex);
    }

//    {"marketid":"11","terid":"103","flag":"0"}

//    void upAdMessage(int marketid, VolleyListener listener, int requestIndex) {
//        if (marketid < 0) {
//            return;
//        }
////        String url = BASE_IP_ST + "/api/smartsz/addatatus?marketid=" + marketid + "&terid=" + terid + "&flag=" + flag;
//        String url = BASE_IP_ST + "/api/smartsz/getvbroadcasbymarketid?marketid=" + marketid;
//        application.volleyGet(url, listener, requestIndex);
//    }

    //通过
    void upAdMessageEx(int marketid, VolleyListener listener, int requestIndex) {
        if (marketid < 0) {
            return;
        }
        String data = "{\"marketid\":\"" + marketid + "\"}";
        String desdata = application.getDesBCBHelper().encode(data);
//        String url = BASE_IP_ST + "/api/smartsz/addatatus?marketid=" + marketid + "&terid=" + terid + "&flag=" + flag;
        String url = BASE_IP_ST + "/api/smartsz/getvbroadcasbymarketid?desdata=" + desdata;
        application.volleyGet(url, listener, requestIndex);
    }

    /**
     * 上傳交易单条信息
     */
//    public void commitDD(OrderInfo orderInfo, OkHttpListener okHttpListener) {
//        String StringOld = JSON.toJSONString(orderInfo);
//        String stringNew = StringOld.replace("orderItem", "items");
//        String url = BASE_IP_ST + "/api/smart/commitszex?";
//        application.okHttpPost(url, stringNew, okHttpListener);
//    }

    /**
     * 上傳交易单条信息
     */
    //通过
    public void commitDDEx(OrderInfo orderInfo, OkHttpListener okHttpListener) {
        if (NetWorkJudge.isNetworkAvailable(application.getContext())) {
            String StringOld = JSON.toJSONString(orderInfo);
            String stringNew = StringOld.replace("orderItem", "items");
            String desdata = application.getDesBCBHelper().encode(stringNew);
            String data = "{\"desdata\":\"" + desdata + "\"}";
            String url = BASE_IP_ST + "/api/smart/commitszex?";
            application.okHttpPost(url, data, okHttpListener);
        }
    }


//    {"billcode":"AX14504012019180451","billstatus":"0","marketid":11,"" +
//            "orderItem":[{"itemno":"3541","money":"28.84","name":"椰子","price":"56",
//            "time1":0,"time2":0,"weight":"0.515","x0":"0","x1":"0","x2":"0"}],
//
//        "seller":"内测03","sellerid":1152,"settlemethod":0,"terid":145,"time":"2019-04-01 18:04:51"}


//    {"billcode":"AN14504012019180321","billstatus":"0","marketid":11,"orderItem":[{"itemno":"3556","k":"0.0383055","money":"26.78","name":"黄豆","price":"52","time1":1554112999853,"time2":0,"weight":"0.515","weight0":"0250000","x0":"2106979","x1":"2107365","x2":"0","xcur":"2120471"}],"seller":"内测03","sellerid":1152,"settlemethod":0,"terid":145,"time":"2019-04-01 18:03:21"}

//    {"billcode":"AX10303062019151936","billstatus":"0","marketid":11,
//            "items":[ {"itemno":"3610","money":"0.2","name":"中袋","price":"0.2","weight":"1","x0":"2141288","x1":"2141289","x2":"2541289"},
//        {"itemno":"3610","money":"0.1","name":"小袋","price":"0.1","weight":"1","x0":"2141288","x1":"2141289","x2":"2541289"},
//        {"itemno":"3610","money":"0.3","name":"大袋","price":"0.3","weight":"1","x0":"2141288","x1":"2141289","x2":"2541289"},
//        {"itemno":"2173","money":"2.08","name":"鸭肠","price":"26","weight":"0.080","x0":"2141288","x1":"2141289","x2":"2541289"}]
//        ,"seller":"胡启城","sellerid":1126,"settlemethod":0,"terid":103,"time":"2019-03-06 15:19:36"}

    /**
     * 订单信息
     *
     * @param orderInfo 订单信息
     * @param flag      表示
     */
    public void commitDD(OrderInfo orderInfo, VolleyStringListener volleyStringListener, int flag) {
//        String tt=JSON.toJSONString(orderInfo);
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
        application.volleyPostString(url, map, volleyStringListener, flag);
    }

    /**
     * 提交批量信息
     *
     * @param orderInfosJson 订单信息
     * @param flag           表示
     */
    public void commitDDs(String orderInfosJson, VolleyStringListener volleyStringListener, int flag) {
        String url = BASE_IP_ST + "/api/smart/commitszexlist";

        Map<String, String> map = new HashMap<>();
        map.put("data", orderInfosJson);
        application.volleyPostString(url, map, volleyStringListener, flag);
    }


    /**
     * 订单支付成功询问
     */
//    public void askOrder(String mchid, String orderno, VolleyListener volleyListener, int flag) {
//        String url = BASE_IP_ST + "/api/pay/check?mchid=" + mchid + "&orderno=" + orderno;
//        application.volleyGet(url, volleyListener, flag);
//    }

    /**
     * 订单支付成功询问
     */
    //通过
    public void askOrderEx(String mchid, String orderno, VolleyListener volleyListener, int flag) {
        String data = "{\"mchid\":\"" + mchid + "\",\"orderno\":\"" + orderno + "\"}";
        String desdata = application.getDesBCBHelper().encode(data);
        String url = BASE_IP_ST + "/api/pay/check?desdata=" + desdata;
        application.volleyGet(url, volleyListener, flag);
    }
//    a11659433bd66e26ea0bf474929ff23529009bf63ac8acea509e722da4713835fe4a9cd4d1a9a955e8c85a8a8feb9248abfa30c03fcad59a64f7a4077520993a
//    {"mchid":"169540002436","orderno":"AX10301162019112837"}
//    http://119.23.43.64/api/pay/check?desdata=a11659433bd66e26ea0bf474929ff23529009bf63ac8acea509e722da4713835fe4a9cd4d1a9a955e8c85a8a8feb9248abfa30c03fcad59a64f7a4077520993a
//    http://119.23.43.64/api/pay/check?desdata=a11659433bd66e26ea0bf474929ff23529009bf63ac8acea509e722da4713835fe4a9cd4d1a9a955e8c85a8a8feb9248743d6c8bbdb3cdda7bc0012ea368b701

    /**
     * 通过mac 获得用户信息
     *
     * @param flag 请求浮标
     */
//    public void getUserInfo(VolleyListener volleyListener, int flag) {
//        String url = BASE_IP_ST + "/api/smart/getinfobymac?mac=" + getMac();
//        application.volleyGet(url, volleyListener, flag);
//    }
    //通过
    public void getUserInfoEx(VolleyListener volleyListener, int flag) {
        String data = "{\"mac\":\"" + getMac() + "\"}";
//        JSON.toJSONString()
        String desdata = application.getDesBCBHelper().encode(data);

        String url = BASE_IP_ST + "/api/smart/getinfobymac?desdata=" + desdata;
        application.volleyGet(url, volleyListener, flag);
    }

//    {"mac":"10:d0:7a:6e:f6:c3"}
//    95036e23953a5187e1ae2f29819781c11a54dd33ec40217a97d94f486831945d
//    http://119.23.43.64/api/smart/getinfobymac?desdata=95036e23953a5187e1ae2f29819781c11a54dd33ec40217a97d94f486831945d

    /**
     * 传输数据，传输离线订单内容
     */
   /* public void updateData(final OrderInfoDao orderInfoDao) {
        application.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                final List<OrderInfo> orderInfos1 = orderInfoDao.queryByState();
                if (orderInfos1 != null && orderInfos1.size() > 0) {
                    String StringOld = JSON.toJSONString(orderInfos1);
                    String stringNew = StringOld.replace("orderBeans", "items");
                    //1.创建OkHttpClient对象
                    final OkHttpClient okHttpClient = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, stringNew);
                    //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
                    okhttp3.Request request = new okhttp3.Request.Builder().url(BASE_IP_ST + "/api/smart/commitszexlist").method("POST", body).build();
                    //3.创建一个call对象,参数就是Request请求对象
                    Call call = okHttpClient.newCall(request);
                    //4.同步调用会阻塞主线程,这边在子线程进行
                    okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                            MyLog.blue(e.getMessage());
                        }

                        // response  返回结果
                        @Override
                        public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                            // 注：该回调是子线程，非主线程
//                                Log.i("wxy", "callback thread id is " + Thread.currentThread().getId());
                            ResultInfo resultInfo = com.alibaba.fastjson.JSON.parseObject(response.body().string(), ResultInfo.class);
                            if (resultInfo.getStatus() == 0) {
                                List<String> data = com.alibaba.fastjson.JSON.parseArray(resultInfo.getData(), String.class);
                                if (data != null && data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        for (int j = 0; j < orderInfos1.size(); j++) {
                                            if (data.get(i).equals(orderInfos1.get(j).getBillcode())) {
                                                orderInfos1.get(j).setState(1);
                                                orderInfoDao.updateOrInsert(orderInfos1.get(j));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }*/
//    {"desdata":"ce231b70b673cf9123e8b142bf7d3ee643d62fab4df43fa3deec1556ad20f1cd1b8b3e3e404f4b4ce46436f49beeccaf5071e35639885f561778009d3d446e166d997df7c52c3839fbe468c721569ddfebdb50a47e5de385ae9fff50e450c35f4afb4915cab93570239d3b89ed7fbebc2af77f8cab06a155d8e265c6cba3d9b5ee5c12116e90eb2f966a5dfb1e45ba2fce3c4f5cf223e245614dda852ac1988185406327743055c2c0b83b3928d8bd4e4d1f4a64b5cca56f734f7b99a0689fc98abac31b4ae2eea0f12ec78a635b9cee6cb1c16884e2ab8da3f6d8f34c2d59e8b814e4952a42a4bcc48f769c3c9e0bfe54136f88e26fbd5bd49749bf7ae4c5cb72c6ec5669cf382c03a196a7c40e2268428695bc0faf45b76157ee85f3fa5bf7a2f39080e040d6d0628a71845caf94bec6b182972c744ebc1e09fb5e60271166c0ea12c97a4ce80713da3b25c6d940c7e3ff8e93ab17863732e12623ed8f2a0a7536967c9e830c34cad839e7fea44b16f7a45e2699ecf2b7f2e4f670b1ef11bf154b9040370a338da4e5b7a21a9e0b87a58a71cbd52e306bf998bc2806ca427232984b3b84c2a3e383b26e95a937e1214a09bf484dc18164c9a819ef5fd4e0a940e69e5f17e02f29e2fd360901300f84b75dc39856709f174316af3bc3362de5d143b2a2fc956b18ad50f1dfb61cc8178f8e6d9bf51c9503c0bfd856640415662f294ae600d79a688a2dced06b4585e6b2bdadd1e281ddf7e7cadacfdaa6e96a71b466dfca07d2792d8bc1674ade37679dd6cd9cbc97914dbad1221afa3a4fae2b618519b11ff48d9fe4162decfd86c30a34508406ad780777c1f015a127b99f27613cedc2b20c13c12e848935de51924b4e580e6253e9eee040f944d7ee225fa36a789cbf3c46db54d29b759a3af238213e2d2adbbb3cc1"}
//   [{"billcode":"AX10301162019113644","billstatus":"0","marketid":11,"items":[{"itemno":"2173","money":"6.98","name":"鸭肠","price":"45","weight":"0.155"}],"seller":"胡启城","sellerid":1126,"settlemethod":0,"terid":103,"time":"2019-01-16 11:36:44"},{"billcode":"AX10301162019113656","billstatus":"0","marketid":11,"items":[{"itemno":"2397","money":"69.45","name":"鸡胗","price":"448","weight":"0.155"},{"itemno":"2398","money":"8.99","name":"水鸭","price":"58","weight":"0.155"},{"itemno":"2400","money":"86.49","name":"鸡肾","price":"558","weight":"0.155"}],"seller":"胡启城","sellerid":1126,"settlemethod":0,"terid":103,"time":"2019-01-16 11:36:56"}]
//    ce231b70b673cf9123e8b142bf7d3ee643d62fab4df43fa3deec1556ad20f1cd1b8b3e3e404f4b4ce46436f49beeccaf5071e35639885f561778009d3d446e166d997df7c52c3839fbe468c721569ddfebdb50a47e5de385ae9fff50e450c35f4afb4915cab93570239d3b89ed7fbebc2af77f8cab06a155d8e265c6cba3d9b5ee5c12116e90eb2f966a5dfb1e45ba2fce3c4f5cf223e245614dda852ac1988185406327743055c2c0b83b3928d8bd4e4d1f4a64b5cca56f734f7b99a0689fc98abac31b4ae2eea0f12ec78a635b9cee6cb1c16884e2ab8da3f6d8f34c2d59e8b814e4952a42a4bcc48f769c3c9e0bfe54136f88e26fbd5bd49749bf7ae4c5cb72c6ec5669cf382c03a196a7c40e2268428695bc0faf45b76157ee85f3fa5bf7a2f39080e040d6d0628a71845caf94bec6b182972c744ebc1e09fb5e60271166c0ea12c97a4ce80713da3b25c6d940c7e3ff8e93ab17863732e12623ed8f2a0a7536967c9e830c34cad839e7fea44b16f7a45e2699ecf2b7f2e4f670b1ef11bf154b9040370a338da4e5b7a21a9e0b87a58a71cbd52e306bf998bc2806ca427232984b3b84c2a3e383b26e95a937e1214a09bf484dc18164c9a819ef5fd4e0a940e69e5f17e02f29e2fd360901300f84b75dc39856709f174316af3bc3362de5d143b2a2fc956b18ad50f1dfb61cc8178f8e6d9bf51c9503c0bfd856640415662f294ae600d79a688a2dced06b4585e6b2bdadd1e281ddf7e7cadacfdaa6e96a71b466dfca07d2792d8bc1674ade37679dd6cd9cbc97914dbad1221afa3a4fae2b618519b11ff48d9fe4162decfd86c30a34508406ad780777c1f015a127b99f27613cedc2b20c13c12e848935de51924b4e580e6253e9eee040f944d7ee225fa36a789cbf3c46db54d29b759a3af238213e2d2adbbb3cc1
    //TODO  接口待检测
    public void updateDataEx(final OrderInfoDao orderInfoDao) {
        application.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<OrderInfo> orderInfos1 = orderInfoDao.queryByState();
                if (orderInfos1 != null && orderInfos1.size() > 0) {
                    String StringOld = JSON.toJSONString(orderInfos1);
                    String stringNew = StringOld.replace("orderBeans", "items");
                    String desdata = application.getDesBCBHelper().encode(stringNew);
                    String data = "{\"desdata\":\"" + desdata + "\"}";

                    //1.创建OkHttpClient对象
                    final OkHttpClient okHttpClient = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, data);
                    //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
                    okhttp3.Request request = new okhttp3.Request.Builder().url(BASE_IP_ST + "/api/smart/commitszexlist").method("POST", body).build();
                    //3.创建一个call对象,参数就是Request请求对象
                    Call call = okHttpClient.newCall(request);
                    //4.同步调用会阻塞主线程,这边在子线程进行
                    okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
//                            MyLog.blue(e.getMessage());
                        }

                        // 返回结果
                        @Override
                        public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response)   {
                            // 注：该回调是子线程，非主线程
//                                Log.i("wxy", "callback thread id is " + Thread.currentThread().getId());
                            try {
                                ResultInfo resultInfo;
                                if (response.body() != null) {
                                    resultInfo = JSON.parseObject(response.body().string(), ResultInfo.class);
                                }else {
                                    return;
                                }
                                if (resultInfo.getStatus() == 0) {
                                    List<String> data = JSON.parseArray(resultInfo.getData(), String.class);
                                    if (data != null && data.size() > 0) {
                                        for (int i = 0; i < data.size(); i++) {
                                            for (int j = 0; j < orderInfos1.size(); j++) {
                                                if (data.get(i).equals(orderInfos1.get(j).getBillcode())) {//TODO 测试
                                                    orderInfos1.get(j).setState(1);
                                                    orderInfoDao.updateOrInsert(orderInfos1.get(j));
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
            }
        });
    }

    /**
     * 請求广告证照图片
     */
//    public void httpQuestImage(VolleyStringListener listener, int shellerid, int flag) {
//        String url = BASE_IP_ST + "/api/smartsz/getadinfo?companyid=";
//        String path = url + shellerid;
//        application.volleyStringGet(path, listener, flag);
//    }

    /**
     * 請求广告证照图片
     */
    //通过
    public void httpQuestImageEx(VolleyStringListener listener, int shellerid, int flag) {
//        http://119.23.43.64/api/smartsz/getadinfo?desdata=5186f38001c5e0a01837a08ddd9a74d2b77680a7fafed075
//        int shellerid=1135;
        String data = "{\"companyid\":\"" + shellerid + "\"}";
        String desdata = application.getDesBCBHelper().encode(data);
        String url = BASE_IP_ST + "/api/smartsz/getadinfo?desdata=" + desdata;
        application.volleyStringGet(url, listener, flag);
    }
//    {"companyid":"1135"}
//5186f38001c5e0a01837a08ddd9a74d2b77680a7fafed075
//    http://119.23.43.64/api/smartsz/getadinfo?desdata=5186f38001c5e0a01837a08ddd9a74d2b77680a7fafed075

    @SuppressLint("HardwareIds")
    public String getMac() {
        WifiManager wm = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
        String mac = "";
        if (wm != null) {
            WifiInfo wifiInfo = wm.getConnectionInfo();
            if (wifiInfo != null) {
                mac = wifiInfo.getMacAddress();
            }
        }
        if (application.getTestMode() > 0) {
            return mac;
        } else {
            return "10:d0:7a:6e:f6:c3";
//        return "10:d0:7a:6e:9c:e7";
        }

    }

    /**
     * 获取产品类别 接口
     */
    public void initGoodsType(VolleyListener listener, int flag) {
        String url = BASE_IP_ST + "/api/smartsz/getproducttype";
        application.volleyGet(url, listener, flag);
    }

//    public void initGoods(VolleyListener listener, int tid, int flag) {
//        String url = BASE_IP_ST + "/api/smartsz/getquick?terid=" + tid;
//        application.volleyGet(url, listener, flag);
//    }

    //通过
    public void initGoodsEx(VolleyListener listener, int tid, int flag) {
        String data = "{\"terid\":\"" + tid + "\"}";
        String desdata = application.getDesBCBHelper().encode(data);
        String url = BASE_IP_ST + "/api/smartsz/getquick?desdata=" + desdata;
        application.volleyGet(url, listener, flag);
    }

    /**
     * 在线检测
     */
    public void onLineTest(VolleyListener listener, int flag) {
        String url = BASE_IP_ST + "/api/smartsz/onlinecheck";
        application.volleyGet(url, listener, flag);
    }

//    /**
//     * 获取批次号
//     */
//    public void getTraceNo(VolleyListener listener, int sellerid, int flag) {
//        String url = BASE_IP_ST + "/api/smartsz/gettracenolist?shid=" + sellerid;
//        application.volleyGet(url, listener, flag);
//    }

    /**
     * 获取批次号
     */
    //通过
    public void getTraceNoEx(VolleyListener listener, int sellerid, int flag) {
        String data = "{\"shid\":\"" + sellerid + "\"}";
        String desdata = application.getDesBCBHelper().encode(data);
        String url = BASE_IP_ST + "/api/smartsz/gettracenolist?desdata=" + desdata;
        application.volleyGet(url, listener, flag);
    }

    /**
     * 获取所有的商品信息,无参数
     */
    public void initAllGoods(VolleyListener listener, int flag) {
        String url = BASE_IP_ST + "/api/smartsz/getproducts";
        application.volleyGet(url, listener, flag);
    }

    /**
     * 计量院 接口接入  登陆
     */
    public void onFpmsLogin(VolleyListener listener, String data, int flag) {
        String url = "http://fpms.chinaap.com/admin/trade?executor=http&appCode=FPMSWS&data=" + data;
        application.volleyGet(url, listener, flag);
    }
}

//http://119.23.43.64/api/pay/check?mchid=169540002436&orderno=AX10312242018152227
