package com.axecom.smartweight.my.activity.common;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.carouselservice.entity.AdImageInfo;
import com.axecom.smartweight.carouselservice.entity.AdUserBean;
import com.axecom.smartweight.carouselservice.entity.AdUserDao;
import com.axecom.smartweight.carouselservice.entity.AdUserInfo;
import com.axecom.smartweight.carouselservice.entity.ImageDao;
import com.axecom.smartweight.carouselservice.service.SecondScreen;
import com.axecom.smartweight.fpms_chinaap.LoginFpmsInfo;

import com.axecom.smartweight.mvp.MainPresenter;
import com.axecom.smartweight.my.CustomDialog123;
import com.axecom.smartweight.my.activity.SystemLoginActivity;
import com.axecom.smartweight.my.adapter.BackgroundData;
import com.axecom.smartweight.my.adapter.DigitalAdapter;
import com.axecom.smartweight.my.adapter.OrderAdapter;
import com.axecom.smartweight.my.entity.AdResultBean;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.dao.HotGoodsDao;
import com.axecom.smartweight.my.entity.dao.OrderBeanDao;
import com.axecom.smartweight.my.entity.dao.OrderInfoDao;
import com.axecom.smartweight.my.entity.dao.UserInfoDao;
import com.axecom.smartweight.my.entity.netresult.OrderResultBean;
import com.axecom.smartweight.my.helper.DateTimeUtil;
import com.axecom.smartweight.my.helper.HeartBeatServcice;
import com.axecom.smartweight.my.helper.HttpHelper;
import com.axecom.smartweight.my.helper.NetUtil;
import com.axecom.smartweight.my.rzl.utils.ApkUtils;
import com.axecom.smartweight.my.rzl.utils.DownloadDialog;
import com.axecom.smartweight.my.rzl.utils.Version;
import com.axecom.smartweight.my.service.CustomAlertDialog;
import com.axecom.smartweight.ui.adapter.GoodMenuAdapter;
import com.axecom.smartweight.utils.AESHelper;
import com.luofx.help.CashierInputFilter;
import com.luofx.listener.VolleyListener;
import com.luofx.listener.VolleyStringListener;
import com.luofx.newclass.ActivityController;
import com.luofx.utils.DateUtils;
import com.luofx.utils.MyPreferenceUtils;
import com.luofx.utils.ToastUtils;
import com.xuanyuan.xinyu.MyToast;
import com.luofx.utils.file.FileUtils;
import com.xuanyuan.library.MyLog;
import com.luofx.utils.net.NetWorkJudge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.axecom.smartweight.mvp.MainPresenter.NOTIFY_GOODS_DATA_CHANGE;
import static com.axecom.smartweight.utils.CommonUtils.parseFloat;

public class MainActivity extends MainBaseActivity implements VolleyListener, VolleyStringListener, View.OnClickListener, View.OnLongClickListener {

    private GridView gvGoodMenu;
    private GridView digitalGridView;
    private ListView commoditysListView;
    private OrderAdapter orderAdapter;
    private GoodMenuAdapter goodMenuAdapter;
    private EditText etPrice;
    private TextView tvGoodsName;
    //小计价格，可以设置addTextChangedListener 进行内容的变换更新
    private TextView tvgrandTotal;
    private TextView tvTotalWeight;
    private TextView weightTv;
    private TextView tvTotalPrice;

    //摊位号
    private TextView tvNumber;
    // 公斤   ，市斤
    private TextView weightTopTv, tvCatty;
    private List<OrderBean> orderBeans;
    private Goods selectedGoods;
    public SecondPresentation banner;
//    boolean switchSimpleOrComplex;// 是否是计件模式

    /************************************************************************************/
    private DownloadDialog downloadDialog;

    // 市场名    售卖者   秤号  kg
    private TextView tvmarketName, tvSeller, tvTid, tvKg;
    //订单区和按键区的父控件
    private LinearLayout llKey, llorder;

    private void initViewFirst() {
        llorder = findViewById(R.id.llorder);
        llKey = findViewById(R.id.llKey);
        tvSeller = findViewById(R.id.tvSeller);
        tvmarketName = findViewById(R.id.tvmarketName);
        tvTid = findViewById(R.id.tvTid);
        tvNumber = findViewById(R.id.tvNumber);

        findViewById(R.id.rlBagSmall).setOnClickListener(this);
        findViewById(R.id.rlBagMiddle).setOnClickListener(this);
        findViewById(R.id.rlBagLarge).setOnClickListener(this);

        // 三种袋子的价格
        TextView tvLarge = findViewById(R.id.tvLarge2);
        TextView tvMiddle = findViewById(R.id.tvMiddle2);
        TextView tvSmall = findViewById(R.id.tvSmall2);
        tvLarge.setText(String.valueOf(priceLarge));
        tvMiddle.setText(String.valueOf(priceMiddle));
        tvSmall.setText(String.valueOf(priceSmall));

        digitalGridView = findViewById(R.id.main_digital_keys_grid_view);
        commoditysListView = findViewById(R.id.main_commoditys_list_view);

//      countEt = findViewById(R.id.main_count_et);
        tvGoodsName = findViewById(R.id.tvGoodsName);
        tvKg = findViewById(R.id.tvKg);
        tvgrandTotal = findViewById(R.id.main_grandtotal_tv);
        tvTotalWeight = findViewById(R.id.main_weight_total_tv);
//      weightTotalMsgTv = findViewById(R.id.main_weight_total_msg_tv);
        weightTv = findViewById(R.id.main_weight_tv);

        tvTotalPrice = findViewById(R.id.main_price_total_tv);
        weightTopTv = findViewById(R.id.main_weight_top_tv);
        tvCatty = findViewById(R.id.tvCatty);
//      weightTopMsgTv = findViewById(R.id.main_weight_msg_tv);
        findViewById(R.id.main_cash_btn).setOnClickListener(this);
        findViewById(R.id.main_settings_btn).setOnClickListener(this);
        findViewById(R.id.main_settings_btn).setOnLongClickListener(this);
        findViewById(R.id.main_clear_btn).setOnClickListener(this);
        findViewById(R.id.btnClearn).setOnClickListener(this);
        findViewById(R.id.btnClearn).setOnLongClickListener(this);
        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.main_scan_pay).setOnClickListener(this);
        etPrice = findViewById(R.id.main_commodity_price_et);
        etPrice.requestFocus();
        InputFilter[] filters = {new CashierInputFilter()};
        etPrice.setFilters(filters); //设置
        disableShowInput(etPrice);
//        disableShowInput(countEt);
        weightTopTv.setOnClickListener(this);
        tvmarketName.setText(userInfo.getMarketname());
        tvSeller.setText(userInfo.getSeller());
        tvTid.setText(String.valueOf(userInfo.getTid()));
        tvNumber.setText(userInfo.getCompanyno());
        notifyUserInfoView();
    }


    /**
     * 小计金额的变动
     */
  /*  private void grandMoney() {
        tvgrandTotal.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                MyLog.bluelog("beforeTextChanged: " + tvgrandTotal.getText().toString() + "; Start: " + i + "; 变化前: " + i1 + "; 变化后: " + i2);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                MyLog.bluelog("onTextChanged: " + tvgrandTotal.getText().toString() + "; Start: " + i + "; 变化前: " + i1 + "; 变化后: " + i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String grandTotal = tvgrandTotal.getText().toString();
                float grandTotalMoneyNew = Float.parseFloat(grandTotal);

               *//* if (grandTotalMoneyNew > 0.00) {
                    if (grandTotalMoneyNew >= grandTotalMoneyOld) {//
                        grandTotalMoneyOld = grandTotalMoneyNew;
                        String price = TextUtils.isEmpty(etPrice.getText().toString()) ? etPrice.getHint().toString() : etPrice.getText().toString();
                        orderBeanTemp.setPrice(price);
//                    selectedGoods.setPrice(price);
                        String weight = weightTopTv.getText().toString();
                        orderBeanTemp.setWeight(weight);
                        orderBeanTemp.setMoney(grandTotal);
                        orderBeanTemp.setItemno(String.valueOf(selectedGoods.getCid()));
                        orderBeanTemp.setName(tvGoodsName.getText().toString());
                        orderBeanTemp.setTraceno(selectedGoods.getBatchCode());
                    }
                } else {//传输订单
                    grandTotalMoneyOld = 0.00f;
                    if (priceValid(etPrice)) {
//                        OrderBean orderBean = orderBeanTemp.clone();
//                        sendOrder2AxeWeb(orderBean);
                    }
                }*//*
            }
        });
    }*/

    /**
     * 是否已经发送了订单
     */
    private boolean isSendOrder = false;

    /**
     * OK 非正常订单,自动上传的订单
     *
     * @param orderBean 详细订单  AN非正常
     */
    public void sendOrder2AxeWeb(final OrderBean orderBean) {
        if (!NetWorkJudge.isNetworkAvailable(context)) {
            return;
        }
        if (!ableUpLoad()) {
            return;
        }

        if (Float.valueOf(orderBean.getWeight()) <= 0) {
            return;
        }

        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                OrderInfo orderInfo = new OrderInfo();
                List<OrderBean> orderlist = new ArrayList<>();
                orderlist.add(orderBean);
                orderInfo.setOrderItem(orderlist);
                String billcode = "AN" + userInfo.getTid() + DateUtils.getSampleNo();
                Date date = new Date();
                String currentTime = DateUtils.getYY_TO_ss(date);
                String hourTime = DateUtils.getHH(date);
                String dayTime = DateUtils.getDD(date);
                orderInfo.setBillstatus("0");//待支付

                orderInfo.setSeller(userInfo.getSeller());
                orderInfo.setSellerid(userInfo.getSellerid());
                orderInfo.setMarketName(tvmarketName.getText().toString());
                orderInfo.setTerid(userInfo.getTid());
                orderInfo.setTotalamount(tvTotalPrice.getText().toString());
                orderInfo.setTotalweight(tvTotalWeight.getText().toString());

                orderInfo.setMarketid(userInfo.getMarketid());
                orderInfo.setTime(currentTime);
                orderInfo.setTimestamp(Timestamp.valueOf(currentTime));
                orderInfo.setHour(Integer.valueOf(hourTime));
                orderInfo.setDay(Integer.valueOf(dayTime));
                orderInfo.setSettlemethod(0);
                orderInfo.setBillcode(billcode);
                orderInfo.setStallNo(tvNumber.getText().toString());
                presenter.send2AxeWebOrderInfo(orderInfo);
            }
        });
    }

    /**
     * OK 初始化 版本
     */
    private void initVersion() {
        TextView tvVersion = findViewById(R.id.tvVersion);
        PackageInfo packageInfo;
        try {
            packageInfo = getApplicationContext().getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0);
            String localVersion = packageInfo.versionName;
            tvVersion.setText(localVersion);//版本號
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * OK 设置软件  背景色
     */
    private void setBackground() {
        int index = MyPreferenceUtils.getSp(context).getInt("BACKGROUND_INDEX", 0);
        llKey.setBackgroundResource(BackgroundData.getData().get(index));
        llorder.setBackgroundResource(BackgroundData.getData().get(index));
        gvGoodMenu.setBackgroundResource(BackgroundData.getData().get(index));
    }

    //检查版本更新
    private void checkVersion(int marketId) {
        downloadDialog = new DownloadDialog(context);
        ApkUtils.checkRemoteVersion(marketId, sysApplication, handler);
    }

    private UserInfoDao userInfoDao;
    private UserInfo userInfo;


    //TODO 使用MVP模式后屏蔽了
    private boolean initUserInfo(Context context) {
        if (userInfo == null) {
            MyToast.toastLong(context, "未读取到用户基本信息!关闭软件");
//            ActivityController.finishAll();
            Intent intent = new Intent(context, HomeActivity.class);
            //放在startActivity前后无关，生命周期实质一样
            this.finish();
            startActivity(intent);
            return false;
        } else {
            MyPreferenceUtils.getSp(context).edit().putInt("shellerid", userInfo.getSellerid()).apply();
            return true;
        }
    }

    /**
     * 上下文。使用上下文
     */
    DecimalFormat decimalFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.

    /**
     * 控制器
     */
    private MainPresenter presenter;

    // 使用功能 ，测试开发
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityController.addActivity(this);
        presenter = new MainPresenter(this);

        //1.用户信息可以使用
        userInfo = presenter.detectionUserInfo();
        if (userInfo != null) {
            initViewFirst();
            //初始化版本信息
            initVersion();
//            grandMoney();
            initHandler();
            initView();
            //初始化背景色
            setBackground();
            //注销注册
            EventBus.getDefault().register(this);
            //显示菜单信息 及对应的批次号
            presenter.getData(NOTIFY_GOODS_DATA_CHANGE);

            //获取批次号信息
            presenter.requestWebTraceNo(userInfo.getSellerid());
        }
    }

    //接收广播信息，已注册*
    @Override
    protected void onBroadCastReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            int netWorkState = NetUtil.getNetWorkState(context);
            if (netWorkState >= 0) {// 0 用流量  ，1  wifi
                if (orderInfoDao == null) {
                    orderInfoDao = new OrderInfoDao(getApplication());
                }
                HttpHelper.getmInstants(sysApplication).updateDataEx(orderInfoDao);
            }
        }
    }

    private boolean isInit = false;//是否初始化

    @Override
    protected void onStart() {
        super.onStart();
        if (!isInit) {

            //初始化 键盘 设置
            initDigital();
            initHeartBeat();

            checkVersion(userInfo.getMarketid());//检查版本更新
            initSecondScreen();
            askOrderState();
            initDate();

            orderInfoDao = new OrderInfoDao(context);
            orderBeanDao = new OrderBeanDao(context);

            judegIsLock();
            isInit = true;

            //更新图片
            sysApplication.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    questImage(MainActivity.this);
                }
            });
            //进行计量院登记信息记录
            sysApplication.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    // 待加密数据源
                    String src = "service=sign&cmd=login&authenCode=fpms_vender_axb&password=h79OpV3MtCfiZHcu";
                    String mainKey = "F7AD4703F4520AFDB0216339";
                    String encryptString = AESHelper.encryptDESedeECB(src, mainKey);
                    if (encryptString == null) {
                        return;
                    }
                    HttpHelper.getmInstants(sysApplication).onFpmsLogin(MainActivity.this, encryptString, VOLLEY_FLAG_FPMSLOGIN);
                }
            });
        }


    }

    /**
     * 请求图片更新
     */
    private void questImage(VolleyStringListener listener) {
        UserInfo userInfo = sysApplication.getUserInfo();
        if (userInfo == null) {
            return;
        }
        int shellerid = sysApplication.getUserInfo().getSellerid();
        if (shellerid == 0) {
            return;
        }
        if (shellerid > 0) {
            HttpHelper.getmInstants(sysApplication).httpQuestImageEx(listener, shellerid, 11);
        }
    }


    private void initDate() {
        TextView date = findViewById(R.id.date);
        DateTimeUtil dateTimeUtil = DateTimeUtil.getInstance();
        date.setText(dateTimeUtil.getCurrentDate());//显示年月日
    }


    /**
     * 是否锁定
     */
    private void judegIsLock() {
        boolean isLock = MyPreferenceUtils.getSp(context).getBoolean(LOCK_STATE, false);
        if (isLock) {
            Intent intent = new Intent(this, LockActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void sendHanderEmptyMessage(int what) {
        switch (what) {
            case NOTIFY_GOODS_DATA_CHANGE:
                handler.sendEmptyMessage(NOTIFY_GOODS_DATA_CHANGE);
                break;
        }

    }


    /**
     * 更新用户信息控件
     */
    private void notifyUserInfoView() {
        tvmarketName.setText(userInfo.getMarketname());
        tvSeller.setText(userInfo.getSeller());
        tvTid.setText(String.valueOf(userInfo.getTid()));
        tvNumber.setText(userInfo.getCompanyno());
    }

    private OrderInfoDao orderInfoDao;
    private OrderBeanDao orderBeanDao;
    boolean isAskOrdering = true;

    /**
     * 轮询 询问订单信息
     */
    private void askOrderState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isAskOrdering) {
                    try {
                        if (sysApplication.isConfigureEpayParams()) {//有配置才进行询问请求
                            if (askInfos.size() > 0) {
                                handler.sendEmptyMessage(NOTIFY_MARQUEE);
                                for (int i = askInfos.size() - 1; i >= 0; i--) {
                                    OrderInfo orderInfo = askInfos.get(i);
                                    long timingTime = orderInfo.getTimingTime();
                                    if (timingTime <= 0) {
                                        orderInfo.setTimingTime(System.currentTimeMillis());
                                    } else {
                                        if (System.currentTimeMillis() - timingTime >= PAYMENT_TIMEOUT) {//等待支付超过180000（3分钟）
                                            askInfos.remove(i);
                                            continue;
                                        }
                                    }
                                    HttpHelper.getmInstants(sysApplication).askOrderEx(userInfo.getMchid(), askInfos.get(i).getBillcode(), MainActivity.this, 4);
                                    Thread.sleep(100);
                                }
                            }
                        }
                        Thread.sleep(3000);
                    } catch (InterruptedException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 控制副屏
     */
    private void initSecondScreen() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        assert displayManager != null;
        Display[] presentationDisplays = displayManager.getDisplays();
//        LogWriteUtils.d("------------: " + presentationDisplays.length + "  --- " + presentationDisplays[1].getName());
        if (presentationDisplays.length > 1) {
            banner = new SecondPresentation(this.getApplicationContext(), presentationDisplays[1]);
//            banner.setData(askInfos);
            Objects.requireNonNull(banner.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            banner.show();
        }
    }

    private HotGoodsDao hotGoodsDao;

    private float oldFloat;
    private boolean isEdSelect;

    /**
     * 获取称重重量信息
     */
    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(final Message msg) {
                int what = msg.what;
                switch (what) {

                    case WEIGHT_NOTIFY_XS8:
//                        try {
//                            String data = (String) msg.obj;
//                            String[] array = data.split(" ");
//                            if (array.length >= 10 && array[0].length() == 7 && array[1].length() == 7 && array[2].length() == 7 && array[3].length() == 7) {
//                                MyLog.logTest("Main  获取的称重数据" + array[2]);
//                                currentAd = array[0];
//                                zeroAd = array[1];
//                                currentWeight = array[2];
//                                tareWeight = array[3];
//
//                                cheatSign = array[4];
//                                isNegative = array[5];
//                                isOver = array[6];
//                                isZero = array[7];
//                                isPeeled = array[8];
//                                isStable = array[9];
//
//                                dealWeight(array[2], isNegative);
//                            }
//                            MyLog.logTest("称重数据" + data);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            break;
//                        }
                        break;
                    case WEIGHT_NOTIFY_XS15:
//                        String weightString = (String) msg.obj;
//                        weight = weightString.substring(15, 23).replace(" ", "");
//                        dealWeight(weight, null);
                        break;

                    case NOTIFY_GOODS_DATA_CHANGE:
                        goodMenuAdapter.setDatas(presenter.getGoodsList());
                        break;
                    case NOTIFY_AD_SECOND:// 副屏刷新
                        if (sysApplication.getTestMode() > 0) {
                            SecondScreen secondScreen = sysApplication.getBanner();
                            if (secondScreen != null) {
                                sysApplication.getBanner().initData();
                            }
                        }
                        break;
                    case NOTIFY_EPAY_SUCCESS:
                        MyLog.blue("支付测试   微信支付");
                        String message = msg.obj.toString();
                        showLoading("0", message, 1500);
                        clearnContext(0);
                        break;
                    case NOTIFY_CLEAR:
                        clearnContext(0);
                        break;

                    case NOTIFY_AD_PICTURE_FORMAT:
                        MyToast.showError(MainActivity.this, "后台图片格式有误");
                        break;
                    case 10012:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.arg2 > 0) {
                                    downloadDialog.setProgress(msg.arg1, msg.arg2);//arg1:已下载字节数,arg2:总字节数
                                }
                            }
                        });
                        break;
                    case 10013:
                        final Version v = (Version) msg.obj;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadDialog.setVersion(v.version);//版本
                                downloadDialog.setDate(v.date);//更新日期
                                downloadDialog.setMarketId(String.valueOf(v.getMarketId()));//市场编号
                                downloadDialog.setDescription(v.description);//更新描述
                                downloadDialog.setApkPath(v.apkPath);//apk路径
                                downloadDialog.show();
                            }
                        });

                        break;
                    case 10014:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadDialog.canForceQuit();
                                Toast.makeText(MainActivity.this, "更新App失败,请联系运营人员", Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void dealWeight(int weight, int isNegativeFlag) {
        boolean isNegative;
        if (isNegativeFlag == 0) {
            isNegative = false;
        } else {
            isNegative = true;
        }

        try {
            float weightFloat = (float) weight / 1000;
//            if (oldFloat == weightFloat) {//前后一样就不在刷新控件
//                return;
//            }

            if (weightFloat == 0.000f) {
//                              etPrice.getText().clear();
                weightTopTv.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
                tvKg.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
            } else {
                weightTopTv.setTextColor(context.getResources().getColor(R.color.white));
                tvKg.setTextColor(context.getResources().getColor(R.color.white));
            }

            if (weightFloat <= 0) {
//                if (oldFloat > weightFloat) {
                Selection.selectAll(etPrice.getText());
                isEdSelect = true;
//                }
            }

            String weight2 = decimalFormat.format(weightFloat * 2);//format 返回的是字符串
            if (isNegative) {
                weightTv.setText("-" + decimalFormat.format(weightFloat));
                weightTopTv.setText("-" + decimalFormat.format(weightFloat));
                tvCatty.setText("-" + weight2);
            } else {
                weightTv.setText(decimalFormat.format(weightFloat));
                weightTopTv.setText(decimalFormat.format(weightFloat));
                tvCatty.setText(weight2);
            }
            setGrandTotalTxt();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理内容
     *
     * @param type 0: 清理所有     1:累加清理
     */
    private void clearnContext(int type) {
        switch (type) {
            case 0:
                orderBeans.clear();
                orderAdapter.notifyDataSetChanged();
                tvTotalPrice.setText(getString(R.string.Default_Initial_Price));
                tvTotalWeight.setText(getString(R.string.Default_Initial_Price));
                etPrice.setHint(etPrice.getText().toString());
                etPrice.getText().clear();
//                tvGoodsName.setText("");
                break;
            case 1:
//                etPrice.setHint(etPrice.getText().toString());
                etPrice.setHint(etPrice.getText().toString());
                etPrice.getText().clear();
//                tvGoodsName.setText("");
                break;
        }
    }

    /**
     * 初始化结算列表
     */
    private void initSettlement() {
        orderBeans = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderBeans);
        commoditysListView.setAdapter(orderAdapter);
        commoditysListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                orderBeans.remove(position);
                orderAdapter.notifyDataSetChanged();
                calculatePrice();
                return true;
            }
        });
    }

    /**
     * 初始化键盘
     */
    private void initDigital() {
        final DigitalAdapter digitalAdapter = new DigitalAdapter(this, null);
        digitalGridView.setAdapter(digitalAdapter);
        digitalGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedGoods == null) {
                    return;
                }
                if (isEdSelect) {
                    etPrice.getText().clear();
                    isEdSelect = false;
                }
                String text = digitalAdapter.getItem(position);
                String price = etPrice.getText().toString();
                if ("删除".equals(text)) {
                    if (price.length() > 0) {
                        String priceNew = price.substring(0, price.length() - 1);
                        etPrice.setText(priceNew);
                    }
                } else {
                    if (".".equals(text)) {
                        if (!price.contains(".")) {
                            String priceNew = price + text;
                            etPrice.setText(priceNew);
                        }
                    } else {
                        String priceNew = price + text;
                        etPrice.setText(priceNew);
                    }
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    public void initView() {
        //初始化结算列表
        initSettlement();
        // 初始化菜单按钮
        gvGoodMenu = findViewById(R.id.gvGoodMenu);
        goodMenuAdapter = new GoodMenuAdapter(this);
        gvGoodMenu.setAdapter(goodMenuAdapter);
        gvGoodMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGoods = goodMenuAdapter.getItem(position);
                tvGoodsName.setText(selectedGoods.getName());
                etPrice.setText("");
                etPrice.setHint(selectedGoods.getPrice());

                //切换了商品，计算重量从0开始
                orderBeanTemp.setWeight(null);
                BigDecimal bigPrice = new BigDecimal(selectedGoods.getPrice());
                BigDecimal bigWeight = new BigDecimal(weightTopTv.getText().toString());
                bigPrice = bigPrice.multiply(bigWeight).setScale(2, BigDecimal.ROUND_HALF_UP);
                tvgrandTotal.setText(bigPrice.toPlainString());

                goodMenuAdapter.setCheckedAtPosition(position);
                goodMenuAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化心跳
     */
    private void initHeartBeat() {
        if (userInfo == null) {
            return;
        }
        if (userInfo.getTid() > 0 && userInfo.getMarketid() > 0) {
            Intent intent = new Intent(MainActivity.this, HeartBeatServcice.class);
            intent.putExtra("marketid", userInfo.getMarketid());
            intent.putExtra("terid", userInfo.getTid());
            startService(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    /**
     * 选定商品后，如有重量改变，则 价格控件 自动设置改变
     * 设置 价格
     */
    @SuppressLint("DefaultLocale")
    public void setGrandTotalTxt() {
        if ("0".equalsIgnoreCase(etPrice.getHint().toString()) && "0".equalsIgnoreCase(etPrice.getText().toString())) {
            return;
        }

        String price = TextUtils.isEmpty(etPrice.getText().toString()) ? etPrice.getHint().toString() : etPrice.getText().toString();
        if (TextUtils.isEmpty(price)) {
            price = "0.00";
        }
        BigDecimal bigPrice = new BigDecimal(price);
        BigDecimal bigWeight = new BigDecimal(weightTopTv.getText().toString());
        bigPrice = bigPrice.multiply(bigWeight).setScale(2, BigDecimal.ROUND_HALF_UP);
        tvgrandTotal.setText(bigPrice.toPlainString());
    }

    /**
     * 自动发送订单
     */
    private void autoSendOrder(float weightFloat) {
        if (weightFloat < 0.005) {
            isSendOrder = false;
        }
        /*   自动上传非正常订单   Start *******************************************************/
        if (!isSendOrder) { //可以上传订单
            long currentTime = System.currentTimeMillis();
            float weightFloatOld = Float.parseFloat(orderBeanTemp.getWeight());
            if (Math.abs(weightFloat - weightFloatOld) < 0.010) {//稳定的
                if (currentTime - orderBeanTemp.getTime1() >= 1000) {
                    if (Float.valueOf(orderBeanTemp.getWeight()) > 0) {
                        //上传非正常订单
                        if (!TextUtils.isEmpty(tvGoodsName.getText().toString())) {
                            String price = TextUtils.isEmpty(etPrice.getText().toString()) ? etPrice.getHint().toString() : etPrice.getText().toString();
                            if (TextUtils.isEmpty(price)) {
                                price = "0.00";
                            }
                            float priceF = Float.valueOf(price);
                            if (priceF < 0.0009) {//金额太小,将不自动上传数据
                                return;
                            }
                            orderBeanTemp.setPrice(price);
                            String weight2 = weightTopTv.getText().toString();
                            tvTotalWeight.setText(weight2);
                            orderBeanTemp.setWeight(weight2);
                            orderBeanTemp.setX0(zeroAd + "");
                            orderBeanTemp.setX1(sbZeroAd + "");
                            orderBeanTemp.setX2(tareWeight + "");
                            orderBeanTemp.setWeight0(sbAd + "");
                            orderBeanTemp.setXcur(currentAd + "");
                            orderBeanTemp.setK(kValue + "");

                            String grandTotal2 = tvgrandTotal.getText().toString();
                            tvTotalPrice.setText(grandTotal2);
                            orderBeanTemp.setMoney(grandTotal2);
                            orderBeanTemp.setTraceno(selectedGoods.getBatchCode());
                            orderBeanTemp.setItemno(String.valueOf(selectedGoods.getCid()));
                            orderBeanTemp.setName(tvGoodsName.getText().toString());

                            OrderBean orderBean = orderBeanTemp.clone();
                            sendOrder2AxeWeb(orderBean);
                            isSendOrder = true;//锁定不让上传订单
                            orderBeanTemp.setTime1(currentTime);
                        }
                    }
                }
            } else {
                orderBeanTemp.setTime1(currentTime);
                String weight2 = weightTopTv.getText().toString();
                orderBeanTemp.setWeight(weight2);
            }
        }
    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseBusEvent event) {
        String array[];
        switch (event.getEventType()) {
            case BaseBusEvent.MARKET_NOTICE:
                AdResultBean.DataBean dataBean = (AdResultBean.DataBean) event.getOther();
                CustomAlertDialog alertDialog = new CustomAlertDialog(context, dataBean);
                alertDialog.show();
                break;
            case BaseBusEvent.BACKGROUND_CHANGE://背景色改变
                setBackground();
                break;
            case BaseBusEvent.NOTIFY_WEIGHT_ST://称重数据变化
                String weight = (String) event.getOther();
                try {
                    float weightFloat = Float.parseFloat(weight);
                    if (weightFloat == 0.000f) {
//                              etPrice.getText().clear();
                        weightTopTv.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
                        tvKg.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
                    } else {
                        weightTopTv.setTextColor(context.getResources().getColor(R.color.white));
                        tvKg.setTextColor(context.getResources().getColor(R.color.white));
                    }

                    autoSendOrder(weightFloat);

                    /*   自动上传非正常订单   End *******************************************************/

                    if (oldFloat == weightFloat) {//前后一样就不在刷新控件
                        break;
                    }

                    if (weightFloat <= 0) {
                        if (oldFloat > weightFloat) {
                            Selection.selectAll(etPrice.getText());
                            isEdSelect = true;
                        }
                    }

                    oldFloat = weightFloat;
                    String weight2 = decimalFormat.format(weightFloat * 2);//format 返回的是字符串
                    weightTv.setText(decimalFormat.format(weightFloat));
                    weightTopTv.setText(decimalFormat.format(weightFloat));
                    tvCatty.setText(weight2);
                    setGrandTotalTxt();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case BaseBusEvent.NOTIFY_WEIGHT_SX15:// 重量
                array = event.getOther().toString().split(" ");
                if (array.length >= 10 && array[0].length() == 7 && array[1].length() == 7 && array[2].length() == 7 && array[3].length() == 7) {
                    MyLog.logTest("Main  获取的称重数据" + array[2]);

                    currentAd = Integer.parseInt(array[0]);
                    zeroAd = Integer.parseInt(array[1]);
                    currentWeight = Integer.parseInt(array[2]);
                    tareWeight = Integer.parseInt(array[3]);
                    cheatSign = Integer.parseInt(array[4]);
                    isNegative = Integer.parseInt(array[5]);
                    isOver = Integer.parseInt(array[6]);
                    isZero = Integer.parseInt(array[7]);
                    isPeeled = Integer.parseInt(array[8]);
                    isStable = Integer.parseInt(array[9]);

                    dealWeight(currentWeight, isNegative);
                    float weightFloat = (float) currentWeight / 1000;
                    //自动发送订单
                    autoSendOrder(weightFloat);
                }

                break;
            case BaseBusEvent.NOTIFY_HOT_GOOD_CHANGE:
                presenter.getData(NOTIFY_GOODS_DATA_CHANGE);
                break;
            case BaseBusEvent.NOTIFY_USERINFO://用户信息更改过
                userInfo = sysApplication.getUserInfo();
                notifyUserInfoView();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (sysApplication.getMyBaseWeighter() != null) {
            sysApplication.getMyBaseWeighter().closeSerialPort();
        }
        isAskOrdering = false;
        if (banner != null) {
            if (banner.isShowing()) {
                banner.dismiss();
            }
            banner = null;
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除事件总线问题
    }

    /**
     * 设置功能 待定算法
     *
     * @param requestCode 请求码
     * @param resultCode  响应码
     * @param data        参数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_SYS_SET) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(STRING_TYPE, 1);
                startActivityForResult(intent, 1111);
            } else if (requestCode == 1111) {
                boolean isDataChange = data.getBooleanExtra("isDataChange", false); //将计算的值回传回去
                boolean isLocalChange = data.getBooleanExtra("isLocalChange", false); //将计算的值回传回去
                if (isDataChange) {// 设置改变了
                    if (userInfoDao == null) {
                        userInfoDao = new UserInfoDao(context);
                    }
                    userInfo = userInfoDao.queryById(1);
                    sysApplication.setUserInfo(userInfo);
                    presenter.getData(NOTIFY_GOODS_DATA_CHANGE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_cash_btn:
//                if (orderBeans.size() == 0) {
//                    AddDirectlyOrder();
//                } else if (orderBeans.size() == 1) {
//                    OrderBean orderBean = orderBeans.get(0);
//                    if (DEFAULT_BAG_ITEM_NO.equalsIgnoreCase(orderBean.getItemno())) {
//                        AddDirectlyOrder();
//                    }
//                }
                if (isCanAdd) {
                    AddDirectlyOrder();
                }
                payCashDirect(0);
                break;
            case R.id.main_scan_pay:
                if (!NetWorkJudge.checkedNetWork(context)) {
                    ToastUtils.showToast(this, "使用第三方支付需要连接网络！");
                    break;
                }

//                if (orderBeans.size() == 0) {
//                    AddDirectlyOrder();
//                } else if (orderBeans.size() == 1) {
//                    OrderBean orderBean = orderBeans.get(0);
//                    if (DEFAULT_BAG_ITEM_NO.equalsIgnoreCase(orderBean.getItemno())) {
//                        AddDirectlyOrder();
//                    }
//                }
                if (isCanAdd) {
                    AddDirectlyOrder();
                }

                payCashDirect(1);
                break;
            case R.id.main_settings_btn:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 1111);
                break;
            case R.id.main_clear_btn:
                clear(0, false);
                break;
            case R.id.btnClearn:
                clear(1, false);
                break;
            case R.id.btnAdd:
                accumulative();
                clearnContext(1);
                break;
            case R.id.rlBagLarge:
                accumulativeBags(3);
                break;
            case R.id.rlBagMiddle:
                accumulativeBags(2);
                break;
            case R.id.rlBagSmall:
                accumulativeBags(1);
                break;
            default:
                break;
        }
    }

    /**
     * 直接添加商品
     */
    private void AddDirectlyOrder() {
        if (selectedGoods == null) {
            return;
        }
        if (TextUtils.isEmpty(etPrice.getText()) && TextUtils.isEmpty(etPrice.getHint())) {
            return;
        }
        if (parseFloat(tvgrandTotal.getText().toString()) <= 0) {
            return;
        }

        OrderBean orderBean2 = new OrderBean();
        String price = TextUtils.isEmpty(etPrice.getText().toString()) ? etPrice.getHint().toString() : etPrice.getText().toString();
        if (TextUtils.isEmpty(price)) {
            price = "0.00";
        }
        orderBean2.setPrice(price);
        selectedGoods.setPrice(price);
        if (hotGoodsDao == null) {
            hotGoodsDao = new HotGoodsDao(context);
        }
        hotGoodsDao.update(selectedGoods);

        String weight2 = weightTopTv.getText().toString();
        tvTotalWeight.setText(weight2);
        orderBean2.setWeight(weight2);

        orderBean2.setX0(zeroAd + "");
        orderBean2.setX1(sbZeroAd + "");
        orderBean2.setX2(tareWeight + "");
        orderBean2.setWeight0(sbAd + "");
        orderBean2.setXcur(currentAd + "");
        orderBean2.setK(kValue + "");

        String grandTotal2 = tvgrandTotal.getText().toString();
        tvTotalPrice.setText(grandTotal2);
        orderBean2.setMoney(grandTotal2);
        orderBean2.setTraceno(selectedGoods.getBatchCode());

        orderBean2.setItemno(String.valueOf(selectedGoods.getCid()));
        orderBean2.setName(tvGoodsName.getText().toString());
        if (!TextUtils.isEmpty(tvGoodsName.getText().toString())) {
            orderBeans.add(orderBean2);
        }
    }

    private void appendCurrentGood() {
        if (orderBeans.size() == 0)
            accumulative();
    }


    /**
     * 累计 菜品价格
     */
    private void accumulative() {

        if (selectedGoods == null) {
            return;
        }
        if (TextUtils.isEmpty(etPrice.getText()) && TextUtils.isEmpty(etPrice.getHint())) {
            return;
        }
        if (parseFloat(tvgrandTotal.getText().toString()) <= 0) {
            return;
        }
        if (TextUtils.isEmpty(tvGoodsName.getText().toString())) {
            return;
        }

        OrderBean orderBean = new OrderBean();
        String price = TextUtils.isEmpty(etPrice.getText().toString()) ? etPrice.getHint().toString() : etPrice.getText().toString();
        if (TextUtils.isEmpty(price)) {
            price = "0.00";
        }
        orderBean.setPrice(price);
        selectedGoods.setPrice(price);
        if (hotGoodsDao == null) {
            hotGoodsDao = new HotGoodsDao(context);
        }
        hotGoodsDao.update(selectedGoods);

        String weight = weightTopTv.getText().toString();
        orderBean.setWeight(weight);

        String grandTotal = tvgrandTotal.getText().toString();
        orderBean.setMoney(grandTotal);

        orderBean.setX0(zeroAd + "");
        orderBean.setX1(sbZeroAd + "");
        orderBean.setX2(tareWeight + "");
        orderBean.setWeight0(sbAd + "");
        orderBean.setXcur(currentAd + "");
        orderBean.setK(kValue + "");

        orderBean.setItemno(String.valueOf(selectedGoods.getCid()));
        orderBean.setName(tvGoodsName.getText().toString());
        orderBean.setTraceno(selectedGoods.getBatchCode());

        isCanAdd = false;
        orderBeans.add(orderBean);
        calculatePrice();
        orderAdapter.notifyDataSetChanged();
        banner.setOrderBean(orderBeans, tvTotalPrice.getText().toString());
    }

    /**
     * 计算购物袋
     *
     * @param bagType 1 小 ，2 中 ， 3 大
     */
    @SuppressLint("DefaultLocale")
    private void accumulativeBags(int bagType) {
        float price = 0;
        String bagName = "小袋";
        switch (bagType) {
            case 1:
                price = priceSmall;
                bagName = "小袋";
                break;
            case 2:
                price = priceMiddle;
                bagName = "中袋";
                break;
            case 3:
                price = priceLarge;
                bagName = "大袋";
                break;
        }
        boolean isExist = false;
        for (int i = 0; i < orderBeans.size(); i++) {
            OrderBean orderBean = orderBeans.get(i);
            if (orderBean.getName().equalsIgnoreCase(bagName)) {
                isExist = true;
                orderBean.setWeight(String.valueOf(Integer.valueOf(orderBean.getWeight()) + 1));
                orderBean.setMoney(String.format("%.2f", Float.valueOf(orderBean.getMoney()) + price));
            }
        }

        if (!isExist) {
            OrderBean orderBean = new OrderBean();
            orderBean.setPrice(String.valueOf(price));
            orderBean.setWeight("1");
            orderBean.setMoney(String.valueOf(price));

            orderBean.setX0(zeroAd + "");
            orderBean.setX1(sbZeroAd + "");
            orderBean.setX2(tareWeight + "");
            orderBean.setWeight0(sbAd + "");
            orderBean.setXcur(currentAd + "");
            orderBean.setK(kValue + "");

            orderBean.setItemno(DEFAULT_BAG_ITEM_NO);
            orderBean.setName(bagName);
            orderBean.setTraceno(null);
            orderBeans.add(0, orderBean);
        }
        calculatePrice();
        orderAdapter.notifyDataSetChanged();
        banner.setOrderBean(orderBeans, tvTotalPrice.getText().toString());
    }

    /**
     * 数据 默认地址
     */
    @SuppressLint("DefaultLocale")
    public void calculatePrice() {
        float weightTotalF = 0.0000f;
        float priceTotal = 0;
        for (int i = 0; i < orderBeans.size(); i++) {
            OrderBean goods = orderBeans.get(i);
            if (!TextUtils.isEmpty(goods.getPrice())) {
                if (!DEFAULT_BAG_ITEM_NO.equalsIgnoreCase(goods.getItemno())) {
                    weightTotalF += parseFloat(goods.getWeight());
                }
                priceTotal += parseFloat(goods.getMoney());
            }
        }
        tvTotalWeight.setText(String.format("%.3f", weightTotalF));
        tvTotalPrice.setText(String.format("%.2f", priceTotal));
    }

    @SuppressLint("SetTextI18n")
    public void clear(int type, boolean b) {
        if (type == 0) {
            //置零
            if (oldFloat <= 1.2f) {
                sysApplication.getMyBaseWeighter().resetBalance();
            }
        }
        if (type == 1) {
//            weightTopTv.setText("0.000");
            tvCatty.setText("0.000");
//            weightTv.setText("");
            etPrice.setHint("0");
            etPrice.setText("");
//            tvTotalWeight.setText("0");
            tvgrandTotal.setText("0.00");
            tvTotalPrice.setText("0.00");
            orderBeans.clear();
            tvGoodsName.setText("");

            orderAdapter = new OrderAdapter(this, orderBeans);
            commoditysListView.setAdapter(orderAdapter);
            selectedGoods = null;
            goodMenuAdapter.cleanCheckedPosition();
            goodMenuAdapter.notifyDataSetChanged();
        }
        if (type == 3) {
            selectedGoods = null;
            tvGoodsName.setText("");
            goodMenuAdapter.cleanCheckedPosition();
            goodMenuAdapter.notifyDataSetChanged();
            String hint = "";
            if (!TextUtils.isEmpty(etPrice.getText())) {
                hint = etPrice.getText().toString();
            } else if (!TextUtils.isEmpty(etPrice.getHint())) {
                hint = etPrice.getHint().toString();
            }
            etPrice.setText("");
            etPrice.setHint(hint);
        }
    }


    /**
     * @param payType 0:現金   1：微信支付
     */
    @SuppressLint("DefaultLocale")
    public void payCashDirect(int payType) {
        if (orderBeans.size() == 0) {
            return;
        }
        final OrderInfo orderInfo = new OrderInfo();//订单信息

        final List<OrderBean> orderlist = new ArrayList<>(orderBeans);
        orderInfo.setOrderItem(orderlist);
        String billcode = "AX" + userInfo.getTid() + DateUtils.getSampleNo();
        Date date = new Date();
        String currentTime = DateUtils.getYY_TO_ss(date);
        String hourTime = DateUtils.getHH(date);
        String dayTime = DateUtils.getDD(date);

        if (payType == 0) {
            orderInfo.setBillstatus("0");
        } else {
            orderInfo.setBillstatus("1");
        }

        orderInfo.setSeller(userInfo.getSeller());
        orderInfo.setSellerid(userInfo.getSellerid());
        orderInfo.setMarketName(tvmarketName.getText().toString());
        orderInfo.setTerid(userInfo.getTid());

        float totalMoney = 0;
        float totalWeight = 0;
        for (int i = 0; i < orderlist.size(); i++) {
            OrderBean orderBean = orderlist.get(i);
            totalMoney += Float.valueOf(orderBean.getMoney());
            if (!DEFAULT_BAG_ITEM_NO.equalsIgnoreCase(orderBean.getItemno())) {
                totalWeight += Float.valueOf(orderBean.getWeight());
            }
        }
        orderInfo.setTotalamount(String.format("%.2f", totalMoney));
        orderInfo.setTotalweight(String.format("%.3f", totalWeight));

        orderInfo.setMarketid(userInfo.getMarketid());
        orderInfo.setTime(currentTime);
        orderInfo.setTimestamp(Timestamp.valueOf(currentTime));
//        orderInfo.setOrderTime(DateUtils.getmm_ss());
        orderInfo.setHour(Integer.valueOf(hourTime));
        orderInfo.setDay(Integer.valueOf(dayTime));
        orderInfo.setSettlemethod(payType);
        orderInfo.setBillcode(billcode);
        orderInfo.setStallNo(tvNumber.getText().toString());

        String message = "支付金额：" + String.format("%.2f", totalMoney) + "元";
        int delayTimes = 1500;
        showLoading("0", message, delayTimes);
        handler.sendEmptyMessage(NOTIFY_CLEAR);

        sysApplication.getPrint().printOrder(sysApplication.getSingleThread(), orderInfo);
        isCanAdd = true;


        for (int i = 0; i < orderlist.size(); i++) {
            OrderBean goods = orderlist.get(i);
            goods.setOrderInfo(orderInfo);
        }
        orderInfoDao.insert(orderInfo);
        orderBeanDao.insert(orderlist);

        presenter.send2AxeWebOrderInfo(orderInfo);
        //上传计量院数据
        presenter.send2FpmsWebOrderInfo(orderInfo);

        if (payType == 1) {
            clearnContext(0);
        }
        askInfos.add(orderInfo);
        banner.notifyData(orderInfo);
    }

    private List<OrderInfo> askInfos = new ArrayList<>();

    /**
     * 连接错误
     *
     * @param volleyError 错误信息
     * @param flag        请求表示索引
     */
    @Override
    public void onResponse(VolleyError volleyError, int flag) {

        switch (flag) {
            case 3:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
            case 4:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
            case 5:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
        }
    }

    @Override
    public void onResponse(final JSONObject jsonObject, final int flag) {
        switch (flag) {
            case 3:
                MyLog.myInfo("成功" + jsonObject.toString());
                break;
            case 4://询问支付状态
                sysApplication.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                        if (resultInfo != null) {
                            if (resultInfo.getStatus() == 0) {//支付成功
                                String orderNo = resultInfo.getData();

                                for (OrderInfo orderInfo : askInfos) {
                                    if (orderInfo.getBillcode().equals(orderNo)) {
                                        // TODO
                                        askInfos.remove(orderInfo);
                                        //TODO 电子订单支付成功
                                        Message message = new Message();
                                        String msg = "支付金额：" + orderInfo.getTotalamount() + "元";
                                        message.what = NOTIFY_EPAY_SUCCESS;// 订单支付成功
                                        message.obj = msg;
                                        handler.sendMessage(message);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
                break;

            case VOLLEY_FLAG_FPMSLOGIN://计量院 登陆接口
                LoginFpmsInfo resultInfo = JSON.parseObject(jsonObject.toString(), LoginFpmsInfo.class);
                if (resultInfo != null) {
                    LoginFpmsInfo.ResultBean resultBean = resultInfo.getResult();
                    if (resultBean != null) {
                        if (resultBean.isSuccess()) {//正确获取数据
                            // 保存参数 
                            MyPreferenceUtils.getSp(context).edit()
                                    .putString(FPMS_LOGINTIME, resultInfo.getLogintime())
                                    .putString(FPMS_EXPIRETIME, resultInfo.getExpiretime())
                                    .putString(FPMS_THIRDKEY, resultInfo.getThirdKey())
                                    .putString(FPMS_AUTHENCODE, resultInfo.getAuthenCode())
                                    .putString(FPMS_DATAKEY, resultInfo.getDatakey())
                                    .apply();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseError(VolleyError volleyError, int flag) {
        MyLog.myInfo("错误" + volleyError);
        switch (flag) {
            case 3:
                MyLog.myInfo("失败" + volleyError.toString());
                break;
            case 5:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
            default:
                break;
        }
    }

    //  返回支付状态
    @Override
    public void onResponse(String response, int flag) {
        try {
            switch (flag) {
                case 3:
                    OrderResultBean resultInfo = JSON.parseObject(response, OrderResultBean.class);
                    if (resultInfo != null && resultInfo.getStatus() == 0) {
                        String billstatus = resultInfo.getData().getBillstatus();
                        String billcode = resultInfo.getData().getBillcode();//交易号

                        if (!TextUtils.isEmpty(billcode)) {
                            orderInfoDao.update(billcode);
                        }
                    }
                    break;
                case 11:
                    new ImageDownThread(response).start();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int pressCount = 0;

    private long timeLong;//记录返回点击时间

    /**
     * 当 返回键 按下
     */
    public boolean ableUpLoad() {
        // 2s 内
        if (System.currentTimeMillis() - timeLong > 5000L) {
            timeLong = System.currentTimeMillis();
            return true;
//            Toast.makeText(this, "再按一次返回退出！", Toast.LENGTH_SHORT).show();
        } else {
            return false;
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setIcon(android.R.drawable.ic_dialog_info);
//            builder.setMessage("是否退出应用?");
//            builder.setCancelable(false);
//            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                    System.exit(0);
//                }
//            });
//            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.create().show();
        }
    }

    @Override
    public void onBackPressed() {
        if (pressCount == 0) {
            ToastUtils.showToast(this, "再次点击退出！");
            pressCount++;
            Timer timer = new Timer();//实例化Timer类
            timer.schedule(new TimerTask() {
                public void run() {
                    pressCount = 0;
                    this.cancel();
                }
            }, 2000);//五百毫秒
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
//            case R.id.btnClearn:
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setMessage("是否删除待支付订单")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                askInfos.clear();
//                                handler.sendEmptyMessage(NOTIFY_MARQUEE);
////                                banner.notifyData(askInfos);
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                            }
//                        });
//                builder.create().show();
//                break;
            case R.id.main_settings_btn://进入系统设置，密码验证界面
                Intent intent2 = new Intent(context, SystemLoginActivity.class);
                startActivityForResult(intent2, RC_SYS_SET);
                break;
        }
        return false;
    }


    private Dialog dialog;

    /**
     * @param context  上下文对象
     * @param dataBean 数据
     */
    private void showAdDialog(Context context, AdResultBean.DataBean dataBean) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }
        CustomDialog123.Builder builder = new CustomDialog123.Builder(context);

//          .style(R.style.Dialog123)
        dialog = builder
                .style(R.style.Dialog123)
                .heightDimenRes(R.dimen.yy320)
                .widthDimenRes(R.dimen.xx560)
                .cancelTouchout(false)
                .view(R.layout.dialog_admessage)
                .addViewOnclick(R.id.btnAdSubmit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).build();
        dialog.show();
    }


    //为了下载图片资源，开辟一个新的子线程
    private class ImageDownThread extends Thread {
        private String response;

        ImageDownThread(String response) {
            this.response = response;
        }

        public void run() {

            AdUserInfo netImageInfo = JSON.parseObject(response, AdUserInfo.class);
            if (netImageInfo != null) {
                if (netImageInfo.getStatus() == 0) {
                    AdUserBean adUserBean = netImageInfo.getData();
                    if (adUserBean != null) {
                        String screenImageState = MyPreferenceUtils.getSp(context).getString(IMAGE_STATE, "default");
                        assert screenImageState != null;
                        if (!screenImageState.equals(adUserBean.getStatus())) {
                            downImage(adUserBean, screenImageState);
                        }
                    }
                }
            }
        }

        private void downImage(AdUserBean adUserBean, String screenImageState) {
            String dir = FileUtils.getDownloadDir(context, FileUtils.DOWNLOAD_DIR);
            //下载图片的路径
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                String baseUrl = adUserBean.getBaseurl();
                List<AdImageInfo> imageInfos = new ArrayList<>();
                String prefix = DateUtils.getSampleNo();

                //2018.12.18 取消证照图片的下载
//                String licences = adUserBean.getLicence();
//                boolean issuccess = FileUtils.deleteDir(dir);
//                if (licences != null) {
//                    String[] adArray = licences.split(";");
//                    if (adArray.length > 0) {
//                        for (int i = 0; i < adArray.length; i++) {
//                            String netUrl = baseUrl + adArray[i];
//                            URL url = new URL(netUrl);
//                            //再一次打开
//                            inputStream = url.openStream();
//                            String localPath = dir + prefix + "licence" + i + ".png";
//                            File file = new File(localPath);
//                            fileOutputStream = new FileOutputStream(file);
//                            int hasRead;
//                            while ((hasRead = inputStream.read()) != -1) {
//                                fileOutputStream.write(hasRead);
//                            }
//                            AdImageInfo AdImageInfo = new AdImageInfo();
//                            AdImageInfo.setNetPath(netUrl);
//                            AdImageInfo.setLocalPath(localPath);
//                            AdImageInfo.setType(2);
//                            imageInfos.add(AdImageInfo);
//                        }
//                    }
//                }

                String ads = adUserBean.getAd();
                if (ads != null) {
                    String[] adArray = ads.split(";");
                    if (adArray.length > 0) {
                        int piclength = 0;

                        if (adArray.length > 8) {
                            piclength = 8;
                        } else {
                            piclength = adArray.length;
                        }

                        for (int i = 0; i < piclength; i++) {
                            String netUrl = (baseUrl + adArray[i]).replace(" ", "");
                            URL url = new URL(netUrl);
                            //再一次打开
                            inputStream = url.openStream();
                            String localPath = dir + prefix + "ad" + i + ".png";
                            File file = new File(localPath);
                            fileOutputStream = new FileOutputStream(file);
                            int hasRead;
                            while ((hasRead = inputStream.read()) != -1) {
                                fileOutputStream.write(hasRead);
                            }
                            AdImageInfo AdImageInfo = new AdImageInfo();
                            AdImageInfo.setNetPath(netUrl);
                            AdImageInfo.setLocalPath(localPath);
                            AdImageInfo.setType(1);
                            imageInfos.add(AdImageInfo);
                        }
                    }
                }

                String photo = adUserBean.getPhoto();
                if (photo != null) {
                    String netUrl = (baseUrl + photo).replace(" ", "");
                    URL url = new URL(netUrl);
                    //再一次打开
                    inputStream = url.openStream();
                    String localPath = dir + prefix + "photo.png";
                    File file = new File(localPath);
                    fileOutputStream = new FileOutputStream(file);
                    int hasRead;
                    while ((hasRead = inputStream.read()) != -1) {
                        fileOutputStream.write(hasRead);
                    }
                    AdImageInfo AdImageInfo = new AdImageInfo();
                    AdImageInfo.setNetPath(netUrl);
                    AdImageInfo.setLocalPath(localPath);
                    AdImageInfo.setType(0);
                    imageInfos.add(AdImageInfo);
                }

                ImageDao imageDao = new ImageDao(context);
                AdUserDao adUserDao = new AdUserDao(context);
                imageDao.deleteAll();
                imageDao.inserts(imageInfos);

                adUserDao.deleteAll();
                adUserBean.setId(1);
                adUserDao.insert(adUserBean);
                MyPreferenceUtils.getSp(context).edit().putString(IMAGE_STATE, screenImageState).apply();
                handler.sendEmptyMessage(NOTIFY_AD_SECOND);
            } catch (Exception e) {
                handler.sendEmptyMessage(NOTIFY_AD_PICTURE_FORMAT);
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}