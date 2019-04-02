package com.axecom.smartweight.mvp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.activity.common.HomeActivity;
import com.axecom.smartweight.my.activity.common.MainActivity;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.dao.HotGoodsDao;
import com.axecom.smartweight.my.entity.dao.OrderInfoDao;
import com.axecom.smartweight.my.entity.dao.TraceNoDao;
import com.axecom.smartweight.my.entity.netresult.OrderResultBean;
import com.axecom.smartweight.my.entity.netresult.TraceNoBean;
import com.axecom.smartweight.my.helper.HttpHelper;
import com.axecom.smartweight.utils.AESHelper;
import com.luofx.listener.OkHttpListener;
import com.luofx.listener.VolleyListener;
import com.luofx.utils.DateUtils;
import com.luofx.utils.MyPreferenceUtils;
import com.luofx.utils.net.NetWorkJudge;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.mvp.MyBasePresenter;
import com.xuanyuan.xinyu.MyToast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.axecom.smartweight.my.config.IConstants.FPMS_AUTHENCODE;
import static com.axecom.smartweight.my.config.IConstants.FPMS_DATAKEY;
import static com.axecom.smartweight.my.config.IConstants.MAIN_KEY;
import static com.axecom.smartweight.my.config.IConstants.VOLLEY_FLAG_AXE_TRACRE_NO;
import static com.axecom.smartweight.my.config.IConstants.VOLLEY_FLAG_FPMS_SUBMIT;

/**
 * MainActivity 的控制类
 */
public class MainPresenter extends MyBasePresenter implements IMainContract.IPresenter, VolleyListener, OkHttpListener {
    private IMainContract.IView view;
    private IMainContract.IModel model;
    private SysApplication myApplication;
    private Context context;
    private OrderInfoDao orderInfoDao;
    private TraceNoDao traceNoDao;//批次号Dao
    private HotGoodsDao hotGoodsDao;
    private List<Goods> goodsList;

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public MainPresenter(IMainContract.IView view) {
        this.view = view;
        model = new MainModel();
        myApplication = view.getMyAppliaction();
        context = view.getMyContext();
    }

    private void initGoodsList() {
        myApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (hotGoodsDao == null) {
                    hotGoodsDao = new HotGoodsDao(context);
                }
                goodsList = hotGoodsDao.queryAll();

                if (goodsList == null) {
                    return;
                }
                if (traceNoDao == null) {
                    traceNoDao = new TraceNoDao(context);
                }
                List<TraceNoBean> traceNoBeans = traceNoDao.queryAll();
                if (traceNoBeans != null && traceNoBeans.size() > 0) {
                    for (int i = 0; i < traceNoBeans.size(); i++) {
                        for (int j = 0; j < goodsList.size(); j++) {
                            if (traceNoBeans.get(i).getTypeid() == goodsList.get(j).getTypeid()) {
                                goodsList.get(j).setBatchCode(traceNoBeans.get(i).getTraceno());
                            }
                        }
                    }
                }
                view.sendHanderEmptyMessage(NOTIFY_GOODS_DATA_CHANGE);
            }
        });
    }

    /**
     * 进行菜单信息及对应批次号 的更新
     */
    public static final int NOTIFY_GOODS_DATA_CHANGE = 9777;

    @Override
    public void getData(int dataType) {
        switch (dataType) {
            case NOTIFY_GOODS_DATA_CHANGE:
                initGoodsList();
                break;

        }

    }

    /**
     * MainActivity  OnCreate第一步 检测用户信息，如用户信息不全则 返回欢迎首页面
     */
    @Override
    public UserInfo detectionUserInfo() {
        UserInfo userInfo = myApplication.getUserInfo();
        if (userInfo == null) {
            MyToast.toastLong(view.getMyContext(), "未读取到用户基本信息!关闭软件");
            view.jumpActivity(HomeActivity.class, true);
            return userInfo;
        } else {
            MyPreferenceUtils.getSp(view.getMyContext()).edit().putInt("shellerid", userInfo.getSellerid()).apply();
            return userInfo;
        }
    }

    @Override
    public void send2AxeWebOrderInfo(final OrderInfo orderInfo) {

                HttpHelper.getmInstants(myApplication).commitDDEx(orderInfo, MainPresenter.this);

    }

    @Override
    public void requestWebTraceNo(int sellerid) {
        HttpHelper.getmInstants(myApplication).getTraceNoEx(this, sellerid, VOLLEY_FLAG_AXE_TRACRE_NO);
    }


    /**
     * @param orderInfo 订单信息发送给计量院
     */
    @Override
    public void send2FpmsWebOrderInfo(final OrderInfo orderInfo) {
        if (!NetWorkJudge.isNetworkAvailable(myApplication.getContext())) {
            return;
        }
        if (myApplication.getTidType() < 1) {//非香山秤，无ad值无法给计量院
            return;
        }
        final String authenCode = MyPreferenceUtils.getSp(context).getString(FPMS_AUTHENCODE, null);
        final String dataKey = MyPreferenceUtils.getSp(context).getString(FPMS_DATAKEY, null);
        if (authenCode == null || dataKey == null) {
            return;
        }
        myApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String cmdECB = AESHelper.encryptDESedeECB("submitWeightInfo", dataKey);
                for (OrderBean orderbean : orderInfo.getOrderItem()) {
                    String sb = "service=deviceService&cmd=" + cmdECB + "&authenCode=" + authenCode +
                            "&appCode=FPMSWS" +
                            "&deviceNo=123456789012" +
                            "&deviceModel=ACS-30-101" +
                            "&factoryName=广东香山衡器集团股份有限公司" +
                            "&productionDate=2019-01-13" + "&macAddr=" +
                            HttpHelper.getmInstants(myApplication).getMac() +
                            "&orderNo=" + orderInfo.getBillcode() +
                            "&goodsCode=" + orderbean.getItemno() +
                            "&goodsName=" + orderbean.getName() +
                            "&price=" + orderbean.getPrice() +
                            "&weight=" + orderbean.getWeight() +
                            "&amounts=" + orderbean.getMoney() +
                            "&initAd=" + orderbean.getX0() +
                            "&zeroAd=" + orderbean.getX1() +
                            "&weightAd=" + orderbean.getX2() +
                            "&orderTime=" + orderInfo.getTime() +
                            "&stallCode=" +
                            "&businessEntity=" +
                            "&creditCode=";
                    String data = AESHelper.encryptDESedeECB(sb, MAIN_KEY);
                    HttpHelper.getmInstants(myApplication).onFpmsLogin(MainPresenter.this, data, VOLLEY_FLAG_FPMS_SUBMIT);
                }
            }
        });
    }


    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        MyLog.blue("自动上传的稳定订单失败");

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
                        orderInfoDao = new OrderInfoDao(context);
                    }
                    orderInfoDao.update(billcode);
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
                break;
            case 4:

                break;
            case 5:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
        }
    }

    @Override
    public void onResponse(final JSONObject jsonObject, int flag) {
        switch (flag) {
            case VOLLEY_FLAG_FPMS_SUBMIT:
                MyLog.myInfo("上传订单信息至计量院成功");
                break;
            case VOLLEY_FLAG_AXE_TRACRE_NO://批次号有更新
                myApplication.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                        if (resultInfo != null) {
                            if (resultInfo.getStatus() == 0) {
                                List<TraceNoBean> goodsList = JSON.parseArray(resultInfo.getData(), TraceNoBean.class);
                                if (goodsList != null && goodsList.size() > 0) {
                                    TraceNoDao traceNoDao = new TraceNoDao(context);
                                    traceNoDao.deleteTableData();
                                    traceNoDao.insert(goodsList);
                                    BaseBusEvent event = new BaseBusEvent();
                                    event.setEventType(BaseBusEvent.NOTIFY_HOT_GOOD_CHANGE);
                                    EventBus.getDefault().post(event);
                                }
                            }
                        }
                    }
                });
                break;
            case 5:
                break;
        }
    }
}
