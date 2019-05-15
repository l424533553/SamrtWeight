package com.axecom.smartweight.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.activity.common.HomeActivity;
import com.axecom.smartweight.my.config.DataConfig;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.axecom.smartweight.my.entity.HotGood;
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
import com.luofx.utils.net.NetWorkJudge;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.mvp.MyBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.axecom.smartweight.my.config.IEventBus.NOTIFY_HOT_GOOD_CHANGE;

/**
 * MainActivity 的控制类
 */
public class MainPresenter extends MyBasePresenter implements IMainContract.IPresenter, VolleyListener, OkHttpListener, IConstants {
    private final IMainView view;
    private final SysApplication myApplication;
    private final Context context;
    private OrderInfoDao orderInfoDao;
    private TraceNoDao traceNoDao;//批次号Dao
    private HotGoodsDao hotGoodsDao;
    private List<HotGood> hotGoodList;

    public List<HotGood> getHotGoodList() {
        return hotGoodList;
    }

    public MainPresenter(IMainView view) {
        this.view = view;
        IMainContract.IModel model = new MainModel();
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
                hotGoodList = hotGoodsDao.queryAll();

                if (hotGoodList == null) {
                    return;
                }
                if (traceNoDao == null) {
                    traceNoDao = new TraceNoDao(context);
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
        if (dataType == NOTIFY_GOODS_DATA_CHANGE) {
            initGoodsList();
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
            return null;
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
        HttpHelper.getmInstants(myApplication).getTraceNoEx(this, sellerid, IConstants.VOLLEY_FLAG_AXE_TRACRE_NO);
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
        final String sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);

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
                            "&deviceNo=" + myApplication.getUserInfo().getSno() +
                            "&deviceModel=ACS-30" +
                            "&factoryName=广东香山衡器集团股份有限公司" +
                            "&productionDate=2019-01-13" + "&macAddr=" +
                            HttpHelper.getmInstants(myApplication).getMac() +
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
                    String data = AESHelper.encryptDESedeECB(sb, MAIN_KEY);
                    MyLog.e("9999999999", "上传订单" + orderInfo.getTotalamount());
                    HttpHelper.getmInstants(myApplication).onFpmsLogin(MainPresenter.this, data, VOLLEY_FLAG_FPMS_SUBMIT);
                }
            }
        });
    }

//    service=deviceService&cmd=8F661895AF18F57BA35E669B952B442425623B054CBDEB70&authenCode=fpms_vender_axb&appCode=FPMSWS&deviceNo=0000J5034784&deviceModel=ACS-30&factoryName=广东香山衡器集团股份有限公司&productionDate=2019-01-13&macAddr=28:ed:e0:ac:c5:a5&orderNo=AN14504102019104854&goodsCode=3573&goodsName=鸭蛋&price=56&weight=0.275&amounts=15.40&initAd=2106818&zeroAd=2106820&weightAd=0&orderTime=2019-04-10 10:48:54&stallCode=&businessEntity=&creditCode=
//            1338EC0CF5D288B78F824E1CC26BEA0E18983A581F949C23B622304CA3AFA43F56912F06C6283CB0F79DF1A927005F5113866E3CF5020AFA213E79DE01760B6C2AD394FB85AACD4AF918BB018C96EC37C48BAED9E751D2BF6CFD66B8207B1CA05FE5509DF4635FBA78DBF596648E79F3A23DCF2C6CB7F18141FD57D9BF6958CB353FBB5B0C9B03C924C04ABA465A62EB1075DFE8D198C4E69067DCAC814700F37B5AA9CEFF2F504F802ED2F1C587E61B4CD5D1EE4C0482BD742F3BAB8483BFD8130DC96F614D812C41453B64515253C3E407447058A60622EE2818DA44FEDB4E07D7A88C32FB86D543422074CC182F3934C4A63D7366C7EC7B6E49314C4F1C1B25DCBD6366BC1F8C1FF23C6CA28B8FEE661251030176E2D3820B15AFB385DA1C20A5C0E430D81F4807F002072B4FEDE69A152168BDEE23D7CFF8FB23D4F0D74706B19041DD6DCFC2E3C8256C3CDDA71A608A979FA32481343B9F2305192FE759FCBB8F29F9E6B9FE18EABD338161C63EA9D6904F327839D10D615FD56F573EE394E9CDFCC1E893C71931EE7D7F3836F09C910751036B28E1F29B7AA069C9F544CEEA7E38FC52D6B3E2F300D7DE28B18A06681176E86F63694CD22D7B1A7C9C55EB1E2299DD737F8F53B2755E96008209C8CEFB18BB74B0D2
//    https://fpms.chinaap.com/admin/trade?executor=http&appCode=FPMSWS&data=1338EC0CF5D288B78F824E1CC26BEA0E18983A581F949C23B622304CA3AFA43F56912F06C6283CB0F79DF1A927005F5113866E3CF5020AFA213E79DE01760B6C2AD394FB85AACD4AF918BB018C96EC37C48BAED9E751D2BF6CFD66B8207B1CA05FE5509DF4635FBA78DBF596648E79F3A23DCF2C6CB7F18141FD57D9BF6958CB353FBB5B0C9B03C924C04ABA465A62EB1075DFE8D198C4E69067DCAC814700F37B5AA9CEFF2F504F802ED2F1C587E61B4CD5D1EE4C0482BD742F3BAB8483BFD8130DC96F614D812C41453B64515253C3E407447058A60622EE2818DA44FEDB4E07D7A88C32FB86D543422074CC182F3934C4A63D7366C7EC7B6E49314C4F1C1B25DCBD6366BC1F8C1FF23C6CA28B8FEE661251030176E2D3820B15AFB385DA1C20A5C0E430D81F4807F002072B4FEDE69A152168BDEE23D7CFF8FB23D4F0D74706B19041DD6DCFC2E3C8256C3CDDA71A608A979FA32481343B9F2305192FE759FCBB8F29F9E6B9FE18EABD338161C63EA9D6904F327839D10D615FD56F573EE394E9CDFCC1E893C71931EE7D7F3836F09C910751036B28E1F29B7AA069C9F544CEEA7E38FC52D6B3E2F300D7DE28B18A06681176E86F63694CD22D7B1A7C9C55EB1E2299DD737F8F53B2755E96008209C8CEFB18BB74B0D2


//    service=deviceService&cmd=F28AD2ED32736A0C5C9E717314552D98D7656179E9EB3244&authenCode=fpms_vender_axb&appCode=FPMSWS&deviceNo=201910100145&deviceModel=ACS-30&factoryName=广东香山衡器集团股份有限公司&productionDate=2019-01-13&macAddr=28:ed:e0:ac:c5:a5&orderNo=AN14504092019101111&goodsCode=3542&goodsName=马蹄&price=59&weight=0.250&amounts=14.75&initAd=2106808&zeroAd=2106808&weightAd=0&orderTime=2019-04-09 10:11:11&stallCode=&businessEntity=&creditCode=

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
                                    event.setEventType(NOTIFY_HOT_GOOD_CHANGE);
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
}
