package com.axecom.smartweight.activity.main.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.activity.common.LockActivity;
import com.axecom.smartweight.activity.common.SecondPresentation;
import com.axecom.smartweight.activity.main.MainObservableBean;
import com.axecom.smartweight.activity.main.model.WeightFieldBean;
import com.axecom.smartweight.activity.main.viewmodel.MainVM;
import com.axecom.smartweight.adapter.GoodMenuAdapter;
import com.axecom.smartweight.adapter.OrderAdapter;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.entity.project.HotGood;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.helper.printer.AdapterFactory;
import com.axecom.smartweight.mvp.IMainView;
import com.axecom.smartweight.mvp.MainPresenter;
import com.axecom.smartweight.service.HeartBeatServcice;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.help.ActivityController;
import com.xuanyuan.library.mvp.view.MyBaseCommonACActivity;
import com.xuanyuan.library.utils.MyDateUtils;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.xuanyuan.library.config.IConfig.STABLE_MIN_TIME;
import static com.xuanyuan.library.config.IConfig.STABLE_TIME;

/**
 * author: luofaxin
 * date： 2018/10/17 0017.
 * email:424533553@qq.com
 * describe:
 */
public abstract class MainBaseACActivity extends MyBaseCommonACActivity implements IConstants, IMainView {
    protected SysApplication sysApplication;
    protected Handler handler;
//    protected HttpHelper helper;

    protected float priceLarge;
    protected float priceMiddle;
    protected float priceSmall;

    protected WeightFieldBean weightBean = new WeightFieldBean();
    protected MainObservableBean mainBean = new MainObservableBean();
    //初始化结算列表   商品购物单
    protected List<OrderBean> orderBeans = new ArrayList<>();

    /**
     * 订单集合列表 ,用于记录电子支付
     */
    protected final List<OrderInfo> askInfos = new ArrayList<>();

    protected AdapterFactory adapterFactory;

    protected OrderAdapter orderAdapter;
    protected GoodMenuAdapter goodMenuAdapter;
    //main 控制器ViewModel
    protected MainVM mainVM;

    //k 值
    protected String kValue;
    //标定Ad
    protected String sbAd;
    //标定零位Ad
    protected String sbZeroAd;

    protected boolean isCanAdd = true;//是否直接点击现金 进入累计菜单

    /**
     * 解析安鑫宝秤  称重数据
     */
    protected void analyticWeightDataAXE(byte[] weightArr) {
        if (weightArr != null) {
            weightBean.setCurrentAd(((weightArr[6] & 0xFF) << 24) | ((weightArr[5] & 0xFF) << 16) | ((weightArr[4] & 0xFF) << 8) | (weightArr[3] & 0xFF));
            weightBean.setZeroAd(((weightArr[10] & 0xFF) << 24) | ((weightArr[9] & 0xFF) << 16) | ((weightArr[8] & 0xFF) << 8) | (weightArr[7] & 0xFF));
            weightBean.setCurrentWeight(((weightArr[14] & 0xFF) << 24) | ((weightArr[13] & 0xFF) << 16) | ((weightArr[12] & 0xFF) << 8) | (weightArr[11] & 0xFF) / 1000);
            weightBean.setTareWeight(((weightArr[18] & 0xFF) << 24) | ((weightArr[17] & 0xFF) << 16) | ((weightArr[16] & 0xFF) << 8) | (weightArr[15] & 0xFF) / 1000);
            weightBean.setCheatSign((weightArr[19] >> 5) & 1);
            weightBean.setIsNegative((weightArr[19] >> 4) & 1);
            weightBean.setIsOver((weightArr[19] >> 3) & 1);
            weightBean.setIsZero((weightArr[19] >> 2) & 1);
            weightBean.setIsPeeled((weightArr[19] >> 1) & 1);
            weightBean.setIsStable((weightArr[19]) & 1);
        }
    }

    /**
     * 控制器
     */
    protected MainPresenter presenter;
    protected SecondPresentation banner;
    protected HotGood selectedHotGood;
    protected UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        sysApplication = (SysApplication) getApplication();
        userInfo = sysApplication.getUserInfo();
        adapterFactory = sysApplication.getAdapterFactory();
        presenter = new MainPresenter(this);
        initSecondScreen();
        if (userInfo != null) {
            initHeartBeat();
        }

        presenter.send2FpmsSignIn(false);
        //注销注册
        EventBus.getDefault().register(this);

    }

    /**
     * 控制副屏， 主要显示 购物列表清单界面
     */
    private void initSecondScreen() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        assert displayManager != null;
        Display[] presentationDisplays = displayManager.getDisplays();
        if (presentationDisplays.length > 1) {
            banner = new SecondPresentation(this.getApplicationContext(), presentationDisplays[1]);
            Objects.requireNonNull(banner.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            banner.show();
        }
    }

    /**
     * 开始了 作弊锁
     */
    protected void startCheatLock() {
        boolean isCheatSign = MyPreferenceUtils.getSp(context).getBoolean(INTENT_CHEATFLAG, false);
        if (!isCheatSign) {
            MyPreferenceUtils.getSp(context).edit().putBoolean(INTENT_CHEATFLAG, true).apply();
            Intent intent = new Intent(context, LockActivity.class);
            startActivity(intent);
        }
    }

    protected abstract void setBackground();

    /**
     * 监控金额输入的自动上传
     */
    protected void listenerAmountInput(TextView listenerTextView) {

        long timeOut = 800;
        if (sysApplication.getTidType() == 0) {
            timeOut = 1000;
        }
        RxTextView.textChangeEvents(listenerTextView)
                .debounce(timeOut, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TextViewTextChangeEvent>() {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        autoSendOrder();
                    }
                });

    }

    //记录自动上传数据的时间，两条自动上传的数据间隔不可太短
    private long orderSendTime;
    /**
     * 上下文。使用上下文。
     */
    protected DecimalFormat weightFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
    protected DecimalFormat priceFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.

//    //是否来自菜单的点击
//    protected boolean isFromPressMenu;

    /**
     * 自动发送订单，上传规则 大于5毛  ，大于50g才上传数据
     */
    protected void autoSendOrder() {
        if (!MyNetWorkUtils.isConnected(context)) {
            return;
        }

        //通过了初级检查
        int tidType = sysApplication.getTidType();
        switch (tidType) {
            case 0:
                if (weightBean.getCurrentTime() - weightBean.getFrontTime() < STABLE_TIME) {
                    return;
                }
                break;
            case 1:
                if (weightBean.getIsStable() != 1) {
                    return;
                }
                if (weightBean.getCurrentTime() - weightBean.getFrontTime() < STABLE_MIN_TIME) {
                    return;
                }
                break;
            default:
                break;
        }

        if (verifySendDataIllegal(true)) {
            return;
        }


        synchronized (object) {
            if (mainBean.getFrontDealName().equals(mainBean.getGoodName().get())) {
                if (mainBean.getFrontDealPrice() == mainBean.getReallyPrice()) {
                    // 两次上传重量太小了
                    if (Math.abs(weightBean.getCurrentWeight() - mainBean.getFrontDealWeight()) < 0.015) {
                        return;
                    }
                }
            }

            mainBean.setFrontDealName(mainBean.getGoodName().get());
            mainBean.setFrontDealPrice(mainBean.getReallyPrice());
            mainBean.setFrontDealWeight(weightBean.getCurrentWeight());


            // 3500ms内不可上传两条数据
            long currentLongTime = System.currentTimeMillis();
            if (currentLongTime - orderSendTime < 4500) {
                return;
            }
            orderSendTime = currentLongTime;

            OrderBean orderBean = createOrderBean(currentLongTime);
            List<OrderBean> orderlist = new ArrayList<>();
            orderlist.add(orderBean);
            UserInfo userInfo = presenter.detectionUserInfo();
            if (userInfo != null) {
                sendOrder2AxeAndFmps(orderlist, userInfo, true, 0);
            }
        }
    }

    /**
     * VM
     * 初始化 塑料袋 价格问题
     */
    protected void initBaseData() {
        mainBean.getPriceLarge().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_LARGE, 0.3f));
        mainBean.getPriceMiddle().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_MIDDLE, 0.2f));
        mainBean.getPriceSmall().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_SMALL, 0.1f));
        mainBean.getZeroWeight().set(MyPreferenceUtils.getSp(context).getFloat(ZERO_WEIGHT, 0.000f));
    }

    /**
     * 点击 了购物袋按钮， 计算完之后记得刷新相关控件
     *
     * @param bagType 1 小 ，2 中 ， 3 大
     */
    protected void btnClickBags(int bagType) {
        float price = 0;
        String bagName = "小袋";
        switch (bagType) {
            case 1:
                price = mainBean.getPriceSmall().get();
                bagName = "小袋";
                break;
            case 2:
                price = mainBean.getPriceMiddle().get();
                bagName = "中袋";
                break;
            case 3:
                price = mainBean.getPriceLarge().get();
                bagName = "大袋";
                break;
        }
        boolean isExist = false;
        for (int i = 0; i < orderBeans.size(); i++) {
            OrderBean orderBean = orderBeans.get(i);
            if (orderBean.getName().equalsIgnoreCase(bagName)) {
                isExist = true;
                orderBean.setWeight(String.valueOf(Integer.valueOf(orderBean.getWeight()) + 1));
                orderBean.setMoney(priceFormat.format(Float.valueOf(orderBean.getMoney()) + price));
            }
        }

        if (!isExist) {
            OrderBean orderBean = new OrderBean();
            orderBean.setPrice(String.valueOf(price));
            orderBean.setWeight("1");
            orderBean.setMoney(String.valueOf(price));

            orderBean.setItemno(DEFAULT_BAG_ITEM_NO);
            orderBean.setName(bagName);
            orderBean.setTraceno(null);
            orderBeans.add(0, orderBean);
        }
        notifyOrderInfo();

    }

    /**
     * VM
     * 是否锁定，判定是否需要锁定
     */
    protected void judegIsLock() {
        boolean isCheatFlag = MyPreferenceUtils.getSp(context).getBoolean(INTENT_CHEATFLAG, false);
        boolean lockState = MyPreferenceUtils.getSp(context).getBoolean(LOCK_STATE, false);
        if (isCheatFlag || lockState) {
            jumpActivity(LockActivity.class, false);
        }
    }

    /**
     * VM
     * 清除按钮  功能, 清除了所有
     */
    public void clear() {
        mainBean.getHintPrice().set("0.00");
        mainBean.getPrice().set("");
        mainBean.getGrandMoney().set("0.00");
        mainBean.getTotalPrice().set("0.00");
        mainBean.getTotalWeight().set("0.000");
        orderBeans.clear();
        mainBean.getGoodName().set("");

        orderAdapter.notifyDataSetChanged();
        selectedHotGood = null;
        goodMenuAdapter.cleanCheckedPosition();
        goodMenuAdapter.notifyDataSetChanged();
    }

    /**
     * 总重和总计 有变动，或者选中的商品有变动
     * 此时需要刷新相关列表和控件
     */
    protected void notifyOrderInfo() {
        calculatePrice();
        orderAdapter.notifyDataSetChanged();
        banner.setOrderBean(orderBeans, mainBean.getTotalPrice().get());
    }

    /**
     * 计算总价格和 总重量   将要显示出来
     */
    protected void calculatePrice() {
        float weightTotalF = 0.000f;
        float priceTotal = 0.00f;
        for (int i = 0; i < orderBeans.size(); i++) {
            OrderBean goods = orderBeans.get(i);
            if (!TextUtils.isEmpty(goods.getPrice())) {
                if (!DEFAULT_BAG_ITEM_NO.equalsIgnoreCase(goods.getItemno())) {
                    weightTotalF += Float.parseFloat(goods.getWeight());
                }
                priceTotal += Float.parseFloat(goods.getMoney());
            }
        }
        mainBean.getTotalWeight().set(weightFormat.format(weightTotalF));
        mainBean.getTotalPrice().set(priceFormat.format(priceTotal));
    }

    private final static Object object = new Object();

    /**
     * 上传订单信息
     *
     * @param orderlist  上传商品的列表
     * @param userInfo   商户信息
     * @param isAutoSend true 自动上传 AN订单  ，false 点击的主动订单  ，AX订单
     * @param payType    0:現金支付   1：扫码支付
     */
    protected synchronized OrderInfo sendOrder2AxeAndFmps(List<OrderBean> orderlist, UserInfo userInfo, boolean isAutoSend, int payType) {
        String billcode;
        Date date = new Date();
        if (isAutoSend) {
            billcode = "AN" + userInfo.getTid() + MyDateUtils.getSampleNo(date);
        } else {
            billcode = "AX" + userInfo.getTid() + MyDateUtils.getSampleNo(date);
        }
        if (billcode.equals(mainBean.getFrontOrderNo())) {
            return null;
        }
        mainBean.setFrontOrderNo(billcode);

        float totalMoney = 0;
        float totalWeight = 0;
        for (int i = orderlist.size() - 1; i >= 0; i--) {
            OrderBean orderBean = orderlist.get(i);
            //垃圾袋不加入统计
            if (!DEFAULT_BAG_ITEM_NO.equalsIgnoreCase(orderBean.getItemno())) {
                totalWeight += Float.valueOf(orderBean.getWeight());
                totalMoney += Float.valueOf(orderBean.getMoney());
            } else {
                orderlist.remove(i);
            }
        }
        if (orderlist.size() == 0) {
            return null;
        }

        OrderInfo orderInfo = new OrderInfo();
        String currentTime = MyDateUtils.getYY_TO_ss(date);
        orderInfo.setOrderItem(orderlist);

        String hourTime = MyDateUtils.getHH(date);
        String dayTime = MyDateUtils.getDD(date);
        //设置支付状态 ，0 支付完成  ，1待支付
        if (payType == 0) {
            orderInfo.setBillstatus("0");
        } else {
            orderInfo.setBillstatus("1");
        }

        orderInfo.setSeller(userInfo.getSeller());
        orderInfo.setSellerid(userInfo.getSellerid());
        orderInfo.setMarketName(userInfo.getMarketname());
        orderInfo.setTerid(userInfo.getTid());

        orderInfo.setTotalamount(priceFormat.format(totalMoney));
        orderInfo.setTotalweight(weightFormat.format(totalWeight));
        orderInfo.setMarketid(userInfo.getMarketid());
        orderInfo.setTime(currentTime);
        orderInfo.setTimestamp(Timestamp.valueOf(currentTime));
        orderInfo.setHour(Integer.valueOf(hourTime));
        orderInfo.setDay(Integer.valueOf(dayTime));
        orderInfo.setSettlemethod(payType);
        orderInfo.setBillcode(billcode);
        orderInfo.setStallNo(userInfo.getCompanyno());

        presenter.send2AxeWebOrderInfo(orderInfo);
        presenter.send2FpmsWebOrderInfo(orderInfo);
        return orderInfo;
    }

    /**
     * @return 创建一个 OrderBean
     */
    protected OrderBean createOrderBean(long currentLongTime) {
        OrderBean bean = new OrderBean();
        String sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
        String sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
        String kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, VALUE_K_DEFAULT);

        bean.setPrice(String.valueOf(mainBean.getReallyPrice()));
        bean.setWeight(String.valueOf(weightBean.getCurrentWeight()));

        bean.setX0(String.valueOf(weightBean.getZeroAd()));
        bean.setX1(sbZeroAd);
        bean.setX2(String.valueOf(weightBean.getTareWeight()));
        bean.setWeight0(sbAd);
        bean.setXcur(String.valueOf(weightBean.getCurrentAd()));
        bean.setK(kValue);

        bean.setMoney(mainBean.getGrandMoney().get());
        bean.setTraceno(mainBean.getBatchCode());
        bean.setItemno(String.valueOf(mainBean.getCid()));
        bean.setName(mainBean.getGoodName().get());

        String currentTime = MyDateUtils.getYY_TO_ss(currentLongTime);
        bean.setTime(currentTime);
        return bean;
    }

    /**
     * 选定商品后，如有重量改变，则 价格控件 自动设置改变
     * 设置 价格
     */
    protected void dealGrandMoney() {
//        if ("0".equalsIgnoreCase(mainBean.getHintPrice().get()) && "0".equalsIgnoreCase(mainBean.getPrice().get())) {
//            return;
//        }
//
//        String price = TextUtils.isEmpty(binding.etPrice.getText().toString()) ? binding.etPrice.getHint().toString() : binding.etPrice.getText().toString();
//        if (TextUtils.isEmpty(price)) {
//            price = "0.00";
//        }
//
//        if (TextUtils.isEmpty(mainBean.getNetWeight().get())) {
//            mainBean.getNetWeight().set("0.0");
//        }

        BigDecimal bigPrice = new BigDecimal(mainBean.getReallyPrice());
        BigDecimal bigWeight = new BigDecimal(weightBean.getCurrentWeight());
        bigPrice = bigPrice.multiply(bigWeight).setScale(2, BigDecimal.ROUND_HALF_UP);
        mainBean.getGrandMoney().set(bigPrice.toPlainString());
//      binding.mainGrandtotalTv.setText(mainBean.getGrandMoneyString());
    }

    /**
     * 验证数据是否非法    true:非法   第一步普通验证
     */
    protected boolean verifySendDataIllegal(boolean isNotBtnAdd) {
        //  验证重量
        if (isNotBtnAdd) {
            if (weightBean.getCurrentWeight() <= 0.1f) {
                if (weightBean.getCurrentWeight() <= 0.0f) {
                    mainBean.setFrontDealName("");
                }
                return true;
            }

            //验证价格price;
            float price = mainBean.getReallyPrice();
            if (price < 1) {
                return true;
            }
        }

        if (weightBean.isNegative()) {
            return true;
        }

        //上传非正常订单
        if (TextUtils.isEmpty(mainBean.getGoodName().get())) {
            return true;
        }

//        if (!MyNetWorkUtils.isConnected(context)) {
//            return true;
//        }

        return "0.00".equals(mainBean.getGrandMoneyString());
    }

    protected boolean loopAskOrdering = true;


    /**
     * 注销内容
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (HeartBeatIntent != null) {
            stopService(HeartBeatIntent);
        }
        if (banner != null) {
            banner.dismiss();
            banner = null;
        }
        sysApplication.onDestory();
        loopAskOrdering = false;

        EventBus.getDefault().unregister(this);//解除事件总线问题
    }

    @Override
    public SysApplication getMyAppliaction() {
        return sysApplication;
    }

    // 心跳 Intent
    private Intent HeartBeatIntent;

    /**
     * 初始化心跳
     */
    private void initHeartBeat() {
        if (userInfo.getTid() > 0 && userInfo.getMarketid() > 0) {
            HeartBeatIntent = new Intent(context, HeartBeatServcice.class);
            HeartBeatIntent.putExtra("marketid", userInfo.getMarketid());
            HeartBeatIntent.putExtra("terid", userInfo.getTid());
            startService(HeartBeatIntent);
        }
    }

    public void disableShowInput(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setSelection(editText.length());
                dealGrandMoney();
            }
        });
    }


    private SweetAlertDialog mSweetAlertDialog;

    public void showLoading(String titleText, int type) {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    /**
     * 顯示进度框
     */
    public void showLoading() {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading(String titleText, String confirmText) {
        if (mSweetAlertDialog != null) {
            if (mSweetAlertDialog.isShowing()) {
                mSweetAlertDialog.dismiss();
            }
            mSweetAlertDialog = null;
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setConfirmText(confirmText);

        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading(String title, String confirmText, long times) {
        String titleText;
        if ("0".equals(title)) {
            titleText = "支付成功";
        } else {
            titleText = "待支付";
        }
        showLoading(titleText, confirmText);
        SysApplication.getHandler().postDelayed(this::closeLoading, times);
    }

    public void showLoading(String titleText) {
        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);

        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void closeLoading() {
        if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {
            mSweetAlertDialog.dismissWithAnimation();
        }
    }

    /**
     * 跳转Activity的方法,传入我们需要的参数即可
     */
    public void startDDMActivity(Class activity, boolean isAinmain) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }

}
