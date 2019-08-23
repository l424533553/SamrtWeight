package com.axecom.smartweight.mvp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.activity.common.mvvm.home.HomeActivity;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.config.DataConfig;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.entity.dao.HotGoodsDao;
import com.axecom.smartweight.entity.dao.OrderInfoDao;
import com.axecom.smartweight.entity.dao.TraceNoDao;
import com.axecom.smartweight.entity.fpms_chinaap.FpsResultInfo;
import com.axecom.smartweight.entity.fpms_chinaap.LoginFpmsInfo;
import com.axecom.smartweight.entity.fpms_chinaap.UpdateTempBean;
import com.axecom.smartweight.entity.netresult.OrderResultBean;
import com.axecom.smartweight.entity.netresult.ResultInfo;
import com.axecom.smartweight.entity.netresult.TraceNoBean;
import com.axecom.smartweight.entity.project.FpmOrderBean;
import com.axecom.smartweight.entity.project.HotGood;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.axecom.smartweight.helper.HttpHelper;
import com.axecom.smartweight.utils.security.AESUtils;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.listener.OkHttpListener;
import com.xuanyuan.library.listener.VolleyListener;
import com.xuanyuan.library.listener.VolleyStringListener;
import com.xuanyuan.library.mvp.MyBasePresenter;
import com.xuanyuan.library.utils.system.SystemInfoUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.axecom.smartweight.config.IEventBus.NOTIFY_HOT_GOOD_CHANGE;

/**
 * MainActivity 的控制类
 */
public class MainPresenter extends MyBasePresenter implements IMainContract.IPresenter, VolleyStringListener, VolleyListener, OkHttpListener, IConstants {
    private final IMainView view;
    private final SysApplication myApplication;
    private final Context context;
    private OrderInfoDao orderInfoDao;
    private HotGoodsDao hotGoodsDao;

    private List<HotGood> hotGoodList;

    public List<HotGood> getHotGoodList() {
        return hotGoodList;
    }

    public void setHotGoodList(List<HotGood> hotGoodList) {
        this.hotGoodList = hotGoodList;
    }

    public MainPresenter(IMainView view) {
        this.view = view;
        IMainContract.IModel model = new MainModel();
        myApplication = view.getMyAppliaction();
        context = view.getMyContext();
    }

    /**
     * @param selectedHotGood 更新选中商品的价格
     */
    public void updateHotGoodPrice(HotGood selectedHotGood, float price) {
        if (selectedHotGood == null) {
            return;
        }
        selectedHotGood.setPrice(String.valueOf(price));
        if (hotGoodsDao == null) {
            hotGoodsDao = new HotGoodsDao();
        }
        hotGoodsDao.update(selectedHotGood);
    }

 /*   private void initGoodsList() {
        myApplication.getThreadPool().execute(() -> {
            if (hotGoodsDao == null) {
                hotGoodsDao = new HotGoodsDao();
            }
            hotGoodList = hotGoodsDao.queryAll();

            if (hotGoodList == null) {
                return;
            }
            if (traceNoDao == null) {
                traceNoDao = new TraceNoDao(context);
            }
            view.sendHanderEmptyMessage(NOTIFY_GOODS_DATA_CHANGE);
        });
    }*/

    /**
     * 进行菜单信息及对应批次号 的更新
     */
    public static final int NOTIFY_GOODS_DATA_CHANGE = 9777;

//    @Override
//    public void getData(int dataType) {
//        if (dataType == NOTIFY_GOODS_DATA_CHANGE) {
//            initGoodsList();
//        }
//    }

    /**
     * MainActivity  OnCreate第一步 检测用户信息，如用户信息不全则 返回欢迎首页面
     */
    @Override
    public UserInfo detectionUserInfo() {
        UserInfo userInfo = myApplication.getUserInfo();
        if (userInfo == null) {
            MyToast.toastLong(view.getMyContext(), "未读取到用户基本信息!关闭软件");
            view.jumpActivity(HomeActivity.class, true);
            return null;
        } else {
            MyPreferenceUtils.getSp(view.getMyContext()).edit().putInt("shellerid", userInfo.getSellerid()).apply();
            return userInfo;
        }
    }

    @Override
    public void send2AxeWebOrderInfo(final OrderInfo orderInfo) {
        if (Build.VERSION.SDK_INT >= 21) {
            HttpHelper.getmInstants(myApplication).commitDDEx(orderInfo, MainPresenter.this);
        } else {
            HttpHelper.getmInstants(myApplication).commitDDExVolley(orderInfo, MainPresenter.this);
        }

        if (orderInfo != null && orderInfo.getOrderItem() != null) {
            currentNoAxe = orderInfo.getBillcode();
            UpdateTempBean updateTempBean = new UpdateTempBean();
            for (OrderBean bean : orderInfo.getOrderItem()) {
                updateTempBean.getOrderNames().add(bean.getName());
            }
            updateTempBean.setOrderNo(orderInfo.getBillcode());
            myApplication.getUpdateBeanAxeList().add(updateTempBean);
        }
    }

    @Override
    public void requestWebTraceNo(int sellerid) {
        HttpHelper.getmInstants(myApplication).getTraceNoEx(this, sellerid, IConstants.VOLLEY_FLAG_AXE_TRACRE_NO);
    }

    //记录当前订单账号 Fpms
    private String currentNoFpms;
    //记录当前订单账号 Axe ，
    private String currentNoAxe;
    private long timeLong;//记录返回点击时间

    /**
     * 当 返回键 按下
     */
    public boolean ableUpLoad() {
        // 2s 内
        if (System.currentTimeMillis() - timeLong > 1500L) {
            timeLong = System.currentTimeMillis();
            return true;
//            Toast.makeText(this, "再按一次返回退出！", Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
    }

    /**
     * @param orderInfo 订单信息发送给计量院  ,使用方便
     */
    @Override
    public void send2FpmsWebOrderInfo(final OrderInfo orderInfo) {

        if (!ableUpLoad()) {
            return;
        }

        if (myApplication.getTidType() < 1) {//非香山秤，无ad值无法给计量院
            return;
        }
        final String authenCode = MyPreferenceUtils.getSp(context).getString(FPMS_AUTHENCODE, null);
        final String dataKey = MyPreferenceUtils.getSp(context).getString(FPMS_DATAKEY, null);
        final String sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);

        if (authenCode == null || dataKey == null) {
            return;
        }
        myApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                int orderCount = orderInfo.getOrderItem().size();
                currentNoFpms = orderInfo.getBillcode();
                if (orderCount == 1) {
                    sendOneOrder();
                } else {
                    sendMoreOrder();
                }
            }

            /**
             * 上传 多条订单信息一次。
             */
            private void sendMoreOrder() {
                int canUpLoadCount = 0;
                ArrayList<FpmOrderBean> list = new ArrayList<>();
                UpdateTempBean updateTempBean = new UpdateTempBean();
                updateTempBean.setOrderNo(orderInfo.getBillcode());

                boolean isANOrder = false;
                if (orderInfo.getBillcode().startsWith("AN")) {
                    isANOrder = true;
                }

                for (OrderBean orderbean : orderInfo.getOrderItem()) {
                    if ("0".equals(sbZeroAd) || "0".equals(orderbean.getX0()) || "0".equals(orderbean.getXcur())) {
                        continue;
                    }
                    canUpLoadCount++;
                    FpmOrderBean orderBean = new FpmOrderBean();
                    orderBean.setDeviceNo(myApplication.getUserInfo().getSno());
                    orderBean.setDeviceModel("ACS-30");
                    orderBean.setFactoryName("广东香山衡器集团股份有限公司");
                    orderBean.setProductionDate("2019-01-13");
                    orderBean.setMacAddr(SystemInfoUtils.getMac(myApplication));
                    orderBean.setOrderNo(orderInfo.getBillcode());
                    orderBean.setGoodsCode(orderbean.getItemno());
                    orderBean.setGoodsName(orderbean.getName());
                    orderBean.setPrice(orderbean.getPrice());
                    orderBean.setWeight(orderbean.getWeight());
                    orderBean.setAmounts(orderbean.getMoney());

                    orderBean.setInitAd(Integer.valueOf(sbZeroAd));
                    orderBean.setZeroAd(Integer.valueOf(orderbean.getX0()));
                    orderBean.setWeightAd(Integer.valueOf(orderbean.getXcur()));
                    orderBean.setOrderTime(orderInfo.getTime());
                    orderBean.setStallCode("");
                    orderBean.setBusinessEntity("");
                    orderBean.setCreditCode("");
                    list.add(orderBean);
                    updateTempBean.getOrderNames().add(orderbean.getName());
                    if (isANOrder) {
                        break;
                    }
                }
                myApplication.getUpdateBeanFpmsList().add(updateTempBean);

                if (canUpLoadCount > 0) {
                    String infoItems = JSON.toJSONString(list);
                    String cmdECB = AESUtils.encryptDESedeECB("submitBatchWeightInfo", dataKey);
                    String sb = "service=deviceService&cmd=" + cmdECB + "&authenCode=" + authenCode +
                            "&appCode=FPMSWS&infoItems=" + infoItems;
                    sendFpsData = AESUtils.encryptDESedeECB(sb, MAIN_KEY);
                    HttpHelper.getmInstants(myApplication).onFpmsLogin(MainPresenter.this, sendFpsData, VOLLEY_FLAG_FPMS_SUBMIT);
                }
            }

            /**
             * 上传单条信息
             */
            private void sendOneOrder() {
                String cmdECB = AESUtils.encryptDESedeECB("submitWeightInfo", dataKey);
                if (orderInfo.getOrderItem().size() > 0) {
                    OrderBean orderbean = orderInfo.getOrderItem().get(0);
                    if ("0".equals(sbZeroAd) || "0".equals(orderbean.getX0()) || "0".equals(orderbean.getXcur())) {
                        return;
                    }

                    String sb = "service=deviceService&cmd=" + cmdECB + "&authenCode=" + authenCode +
                            "&appCode=FPMSWS" +
                            "&deviceNo=" + myApplication.getUserInfo().getSno() +
                            "&deviceModel=ACS-30" +
                            "&factoryName=广东香山衡器集团股份有限公司" +
                            "&productionDate=2019-01-13" + "&macAddr=" +
                            SystemInfoUtils.getMac(myApplication) +
                            "&orderNo=" + orderInfo.getBillcode() +
                            "&goodsCode=" + orderbean.getItemno() +
                            "&goodsName=" + orderbean.getName() +
                            "&price=" + orderbean.getPrice() +
                            "&weight=" + orderbean.getWeight() +
                            "&amounts=" + orderbean.getMoney() +
                            "&initAd=" + sbZeroAd +
                            "&zeroAd=" + orderbean.getX0() +
                            "&weightAd=" + orderbean.getXcur() +
                            "&orderTime=" + orderInfo.getTime() +
                            "&stallCode=" +
                            "&businessEntity=" +
                            "&creditCode=";
                    sendFpsData = AESUtils.encryptDESedeECB(sb, MAIN_KEY);
                    HttpHelper.getmInstants(myApplication).onFpmsLogin(MainPresenter.this, sendFpsData, VOLLEY_FLAG_FPMS_SUBMIT);

                    UpdateTempBean updateTempBean = new UpdateTempBean();
                    updateTempBean.setOrderNo(orderInfo.getBillcode());
                    updateTempBean.getOrderNames().add(orderbean.getName());
                    myApplication.getUpdateBeanFpmsList().add(updateTempBean);
                }
            }
        });
    }

    /**
     * 计量院签到获取DataKey
     *
     * @param isReset 是否是datakey失效后重新发送获取的  true:此时可能要重新发送交易数据
     */
    public void send2FpmsSignIn(final boolean isReset) {
        if (myApplication.getTidType() != 1) {//暂时非XS15 不用签到
            return;
        }

        //进行计量院登记信息记录
        myApplication.getThreadPool().execute(() -> {
            // 待加密数据源
            String src = "service=sign&cmd=login&authenCode=fpms_vender_axb&password=h79OpV3MtCfiZHcu";
            String mainKey = "F7AD4703F4520AFDB0216339";
            String encryptString = AESUtils.encryptDESedeECB(src, mainKey);
            if (encryptString == null) {
                return;
            }

            if (isReset) {
                HttpHelper.getmInstants(myApplication).onFpmsLogin(MainPresenter.this, encryptString, VOLLEY_FLAG_FPMSLOGIN_RESET);
            } else {
                HttpHelper.getmInstants(myApplication).onFpmsLogin(MainPresenter.this, encryptString, VOLLEY_FLAG_FPMSLOGIN);
            }
        });
    }

    private String sendFpsData;

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        MyLog.blue("自动上传的稳定订单失败");
        int fpsCountERR = MyPreferenceUtils.getSp(myApplication).getInt(AXE_COUNT_ERR, 0) + 1;
        MyPreferenceUtils.getSp(myApplication).edit().putInt(AXE_COUNT_ERR, fpsCountERR).apply();
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return;
        }
        try {
            OrderResultBean resultInfo = JSON.parseObject(responseBody.string(), OrderResultBean.class);
            if (resultInfo != null && resultInfo.getStatus() == 0) {
                String billcode = resultInfo.getData().getBillcode();//交易号
                if (!TextUtils.isEmpty(billcode)) {
                    if (orderInfoDao == null) {
                        orderInfoDao = new OrderInfoDao();
                    }
                    orderInfoDao.update(billcode);
                    int fpsCountERR = MyPreferenceUtils.getSp(myApplication).getInt(AXE_COUNT_OK, 0) + 1;
                    MyPreferenceUtils.getSp(myApplication).edit().putInt(AXE_COUNT_OK, fpsCountERR).apply();

                    for (UpdateTempBean bean : myApplication.getUpdateBeanAxeList()) {
                        if (billcode.equals(bean.getOrderNo())) {
                            bean.setState(1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        switch (flag) {
            case VOLLEY_FLAG_FPMS_SUBMIT:
                MyLog.myInfo("订单信息上传计量院失败");
                int fpsCountERR = MyPreferenceUtils.getSp(myApplication).getInt(FPS_COUNT_ERR, 0) + 1;
                MyPreferenceUtils.getSp(myApplication).edit().putInt(FPS_COUNT_ERR, fpsCountERR).apply();

//                String TAG = this.getClass().getName();
//                //volley 有专门处理error的库，下面就是调用了其中的一些，可以方便调试的时候查找到错误
//                Log.d(TAG, "Volley returned error________________:" + volleyError);
//                Class klass = volleyError.getClass();
//                if (klass == com.android.volley.AuthFailureError.class) {
//
//                    Toast.makeText(context, "未授权，请重新登录", Toast.LENGTH_LONG).show();
//                } else if (klass == com.android.volley.NetworkError.class) {
//                    Log.d(TAG, "NetworkError");
//                    Toast.makeText(context, "网络连接错误，请重新登录", Toast.LENGTH_LONG).show();
//                } else if (klass == com.android.volley.NoConnectionError.class) {
//                    Log.d(TAG, "NoConnectionError");
//                } else if (klass == com.android.volley.ServerError.class) {
//                    Log.d(TAG, "ServerError");
//                    Toast.makeText(context, "服务器未知错误，请重新登录", Toast.LENGTH_LONG).show();
//                } else if (klass == com.android.volley.TimeoutError.class) {
//                    Log.d(TAG, "TimeoutError");
//                    Toast.makeText(context, "连接超时，请重新登录", Toast.LENGTH_LONG).show();
//                } else if (klass == com.android.volley.ParseError.class) {
//                    Log.d(TAG, "ParseError");
//                } else if (klass == com.android.volley.VolleyError.class) {
//                    Log.d(TAG, "General error");
//                }
//                Toast.makeText(context, "登录失败", Toast.LENGTH_LONG).show();

                break;
            case 4:
                break;
            case VOLLEY_FLAG_FPMSLOGIN://计量院 登陆接口
            case VOLLEY_FLAG_FPMSLOGIN_RESET://计量院 登陆接口
                MyLog.blue("计量院上传数据失败");
                break;
            case 5:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
        }
    }

    /**
     * 获得到了FPS的 登陆签到信息
     *
     * @return true：活到到了签到信息
     */
    private boolean analyticFpsLoginInfo(JSONObject jsonObject) {
        LoginFpmsInfo loginFpmsInfo = JSON.parseObject(jsonObject.toString(), LoginFpmsInfo.class);
        if (loginFpmsInfo != null) {
            LoginFpmsInfo.ResultBean resultBean = loginFpmsInfo.getResult();
            if (resultBean != null) {
                if (resultBean.isSuccess()) {//正确获取数据
                    // 保存参数
                    MyPreferenceUtils.getSp(context).edit()
                            .putString(FPMS_LOGINTIME, loginFpmsInfo.getLogintime())
                            .putString(FPMS_EXPIRETIME, loginFpmsInfo.getExpiretime())
                            .putString(FPMS_THIRDKEY, loginFpmsInfo.getThirdKey())
                            .putString(FPMS_AUTHENCODE, loginFpmsInfo.getAuthenCode())
                            .putString(FPMS_DATAKEY, loginFpmsInfo.getDatakey())
                            .apply();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onResponse(final JSONObject jsonObject, int flag) {
        switch (flag) {
            case VOLLEY_FLAG_FPMS_SUBMIT:
                FpsResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), FpsResultInfo.class);
                if (resultInfo != null) {
                    FpsResultInfo.ResultBean resultBean = resultInfo.getResult();
                    if (resultBean != null) {
                        if (resultBean.isSuccess()) {
                            int fpsCountOK = MyPreferenceUtils.getSp(myApplication).getInt(FPS_COUNT_OK, 0) + 1;
                            MyPreferenceUtils.getSp(myApplication).edit().putInt(FPS_COUNT_OK, fpsCountOK).apply();
                            MyLog.myInfo("上传订单信息至计量院成功" + jsonObject.toString());

                            if (currentNoFpms != null) {
                                for (UpdateTempBean bean : myApplication.getUpdateBeanFpmsList()) {
                                    if (currentNoFpms.equals(bean.getOrderNo())) {
                                        bean.setState(1);
                                        break;
                                    }
                                }
                            }
                        } else {
                            if ("OT-DATAKEY".equals(resultBean.getCode())) {
                                send2FpmsSignIn(true);
                            }
                        }
                    }
                }
                break;
            case VOLLEY_FLAG_FPMSLOGIN://计量院 登陆接口
                analyticFpsLoginInfo(jsonObject);
                break;
            case VOLLEY_FLAG_FPMSLOGIN_RESET://计量院 登陆接口
                if (analyticFpsLoginInfo(jsonObject)) {
                    //重新获取到了dataKey
                    HttpHelper.getmInstants(myApplication).onFpmsLogin(MainPresenter.this, sendFpsData, VOLLEY_FLAG_FPMS_SUBMIT);
                }
                break;
            case VOLLEY_FLAG_AXE_TRACRE_NO://批次号有更新
                myApplication.getThreadPool().execute(() -> {
                    ResultInfo resultInfo1 = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                    if (resultInfo1 != null) {
                        if (resultInfo1.getStatus() == 0) {
                            List<TraceNoBean> goodsList = JSON.parseArray(resultInfo1.getData(), TraceNoBean.class);
                            if (goodsList != null && goodsList.size() > 0) {
                                TraceNoDao traceNoDao = new TraceNoDao(context);
                                traceNoDao.deleteTableData();
                                traceNoDao.insert(goodsList);
                                BaseBusEvent event = new BaseBusEvent();
                                event.setEventType(NOTIFY_HOT_GOOD_CHANGE);
                                EventBus.getDefault().post(event);
                            }
                        }
                    }
                });
                break;
            case 5:
                break;
        }
    }

    /**
     * 按键操作
     */
    public void keystrokeOperation(String key) {
        String value = DataConfig.getKey(key.replace("#", ""));
        if (value == null) {
            return;
        }
        if (value.startsWith("menu")) {
            int position = Integer.parseInt(value.replace("menu", ""));
            view.selectOrderBean(position);
        } else if (value.startsWith("num")) {
            int position = Integer.parseInt(value.replace("num", ""));
            view.pressDigital(position);
        } else if (value.startsWith("func")) {
            switch (value) {
                case FUNC_TARE:// 去皮操作
                    view.getMyAppliaction().getMyBaseWeighter().resetBalance();
                    break;
                case FUNC_ZERO://置零操作
                    view.getMyAppliaction().getMyBaseWeighter().sendZer();
                    break;
                case FUNC_SET:// 设置操作
                    // 设置操作
                    break;
                case FUNC_BACK:// 返回操作
                    break;
                case FUNC_CARSH:// 现金操作
                    break;
                case FUNC_QR:// 扫码操作
                    break;
                case FUNC_ADD: // 累积操作
                    break;
                case FUNC_CLEAR:// 清除操作

                    break;
            }
        }
    }

    @Override
    public void onStringResponse(VolleyError volleyError, int flag) {
        MyLog.blue("自动上传的稳定订单失败");
        int fpsCountERR = MyPreferenceUtils.getSp(myApplication).getInt(AXE_COUNT_ERR, 0) + 1;
        MyPreferenceUtils.getSp(myApplication).edit().putInt(AXE_COUNT_ERR, fpsCountERR).apply();
    }

    @Override
    public void onStringResponse(String response, int flag) {
        try {
            OrderResultBean resultInfo = JSON.parseObject(response, OrderResultBean.class);
            if (resultInfo != null && resultInfo.getStatus() == 0) {
                String billcode = resultInfo.getData().getBillcode();//交易号
                if (!TextUtils.isEmpty(billcode)) {
                    if (orderInfoDao == null) {
                        orderInfoDao = new OrderInfoDao();
                    }
                    orderInfoDao.update(billcode);
                    int fpsCountERR = MyPreferenceUtils.getSp(myApplication).getInt(AXE_COUNT_OK, 0) + 1;
                    MyPreferenceUtils.getSp(myApplication).edit().putInt(AXE_COUNT_OK, fpsCountERR).apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


/*
[{"amounts":"5.13","businessEntity":"","creditCode":"",
        "deviceModel":"ACS-30","deviceNo":"0000J5034784","factoryName":"广东香山衡器集团股份有限公司",
        "goodsCode":"3561","goodsName":"清凉补","initAd":2104944,"macAddr":"28:ed:e0:ac:c5:a5",
        "orderNo":"AX14507092019144840","orderTime":"2019-07-09 14:48:40","price":"25",
        "productionDate":"2019-01-13","stallCode":"","weight":"0.205",
        "weightAd":2116672,"zeroAd":2111320},

        {"amounts":"3.08","businessEntity":"","creditCode":"","deviceModel":"ACS-30","deviceNo":"0000J5034784",
        "factoryName":"广东香山衡器集团股份有限公司","goodsCode":"3573",
        "goodsName":"鸭蛋","initAd":2104944,"macAddr":"28:ed:e0:ac:c5:a5","orderNo":"AX14507092019144840",
        "orderTime":"2019-07-09 14:48:40","price":"15","productionDate":"2019-01-13","stallCode":"",
        "weight":"0.205","weightAd":2116672,"zeroAd":2111320}]*/
