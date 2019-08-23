//package com.axecom.smartweight.activity.main.view;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.databinding.DataBindingUtil;
//import android.hardware.display.DisplayManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.text.InputFilter;
//import android.text.Selection;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Display;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.android.volley.VolleyError;
//import com.axecom.smartweight.R;
//import com.axecom.smartweight.activity.SecondScreen;
//import com.axecom.smartweight.activity.common.LockActivity;
//import com.axecom.smartweight.activity.common.SecondPresentation;
//import com.axecom.smartweight.activity.common.SettingsActivity;
//import com.axecom.smartweight.activity.common.mvvm.home.HomeActivity;
//import com.axecom.smartweight.activity.main.MainObservableBean;
//import com.axecom.smartweight.activity.main.viewmodel.MainVM;
//import com.axecom.smartweight.activity.setting.SystemLoginActivity;
//import com.axecom.smartweight.adapter.BackgroundData;
//import com.axecom.smartweight.adapter.DigitalAdapter;
//import com.axecom.smartweight.adapter.GoodMenuAdapter;
//import com.axecom.smartweight.adapter.OrderAdapter;
//import com.axecom.smartweight.config.IEventBus;
//import com.axecom.smartweight.databinding.ActivityMainBinding;
//import com.axecom.smartweight.entity.dao.HotGoodsDao;
//import com.axecom.smartweight.entity.dao.OrderBeanDao;
//import com.axecom.smartweight.entity.dao.OrderInfoDao;
//import com.axecom.smartweight.entity.dao.UserInfoDao;
//import com.axecom.smartweight.entity.netresult.OrderResultBean;
//import com.axecom.smartweight.entity.netresult.ResultInfo;
//import com.axecom.smartweight.entity.project.AdResultBean;
//import com.axecom.smartweight.entity.project.HotGood;
//import com.axecom.smartweight.entity.project.OrderBean;
//import com.axecom.smartweight.entity.project.OrderInfo;
//import com.axecom.smartweight.entity.system.BaseBusEvent;
//import com.axecom.smartweight.helper.HttpHelper;
//import com.axecom.smartweight.mvp.MainPresenter;
//import com.axecom.smartweight.mvvm.view.IAllView;
//import com.axecom.smartweight.rzl.utils.ApkUtils;
//import com.axecom.smartweight.rzl.utils.DownloadDialog;
//import com.axecom.smartweight.rzl.utils.Version;
//import com.axecom.smartweight.service.CustomAlertDialog;
//import com.axecom.smartweight.service.HeartBeatServcice;
//import com.jakewharton.rxbinding2.widget.RxTextView;
//import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
//import com.xuanyuan.library.MyLog;
//import com.xuanyuan.library.MyPreferenceUtils;
//import com.xuanyuan.library.MyToast;
//import com.xuanyuan.library.PowerConsumptionRankingsBatteryView;
//import com.xuanyuan.library.base2.BaseBuglyApplication;
//import com.xuanyuan.library.help.CashierInputFilter2;
//import com.xuanyuan.library.listener.VolleyListener;
//import com.xuanyuan.library.listener.VolleyStringListener;
//import com.xuanyuan.library.utils.MyDateUtils;
//import com.xuanyuan.library.utils.net.MyNetWorkUtils;
//
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//
//import static com.axecom.smartweight.mvp.MainPresenter.NOTIFY_GOODS_DATA_CHANGE;
//import static com.xuanyuan.library.config.IConfig.STABLE_TIME;
//
//public class MainActivityST extends MainBaseACActivity implements IAllView, IEventBus, VolleyListener, VolleyStringListener, View.OnClickListener, View.OnLongClickListener {
//
//    private OrderAdapter orderAdapter;
//    private GoodMenuAdapter goodMenuAdapter;
//    private List<OrderBean> orderBeans;
//    private HotGood selectedHotGood;
//    public SecondPresentation banner;
//
//    /************************************************************************************/
//    private DownloadDialog downloadDialog;
//
//    /**
//     * VM
//     * 初始化 第一界面
//     */
//    private void initViewFirst() {
//        binding.date.setText(MyDateUtils.getYYMMDD(System.currentTimeMillis()));//显示年月日
//
//        findViewById(R.id.main_settings_btn).setOnLongClickListener(this);
//        findViewById(R.id.btnClearn).setOnLongClickListener(this);
//        findViewById(R.id.tvTid).setOnClickListener(this);
//        binding.etPrice.requestFocus();
//        InputFilter[] filters = {new CashierInputFilter2()};
//        binding.etPrice.setFilters(filters); //设置
//        disableShowInput(binding.etPrice);
//
//        RxTextView.textChangeEvents(binding.mainGrandtotalTv)
//                .debounce(800, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<TextViewTextChangeEvent>() {
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
//                        autoSendOrder();
//                    }
//                });
//
//        try {
//            PackageInfo packageInfo = getApplicationContext().getPackageManager()
//                    .getPackageInfo(this.getPackageName(), 0);
//            String localVersion = packageInfo.versionName + "." + packageInfo.versionCode;
//            mainBean.getVersion().set(localVersion);
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //TODO delete20190724
//    //是否已经发送了订单
////    private boolean isSendOrder = false;
//
//    private UserInfoDao userInfoDao;
//
//    //记录自动上传数据的时间，两条自动上传的数据间隔不可太短
//    private long orderSendTime;
//
//    /**
//     * OK 非正常订单,自动上传的订单。
//     *
//     * @param orderBean 详细订单  AN非正常。
//     */
//    public synchronized void sendOrder2AxeWeb(final OrderBean orderBean) {
//
//        long currentLongTime = System.currentTimeMillis();
//        if (currentLongTime - orderSendTime < 2500) {
//            return;
//        }
//        orderSendTime = currentLongTime;
//
//        final OrderInfo orderInfo = new OrderInfo();
//        List<OrderBean> orderlist = new ArrayList<>();
//        Date date = new Date();
//        String currentTime = MyDateUtils.getYY_TO_ss(date);
//        orderBean.setTime(currentTime);
//        orderlist.add(orderBean);
//        orderInfo.setOrderItem(orderlist);
//        String billcode = "AN" + mainVM.userInfo.getTid() + MyDateUtils.getSampleNo(date);
//
//        String hourTime = MyDateUtils.getHH(date);
//        String dayTime = MyDateUtils.getDD(date);
//        orderInfo.setBillstatus("0");//待支付
//
//        orderInfo.setSeller(mainVM.userInfo.getSeller());
//        orderInfo.setSellerid(mainVM.userInfo.getSellerid());
//        orderInfo.setMarketName(mainVM.userInfo.getMarketname());
//        orderInfo.setTerid(mainVM.userInfo.getTid());
//        orderInfo.setTotalamount(mainBean.getGrandMoney().get());
//        orderInfo.setTotalweight(mainBean.getNetWeight().get());
//
//        orderInfo.setMarketid(mainVM.userInfo.getMarketid());
//        orderInfo.setTime(currentTime);
//        orderInfo.setTimestamp(Timestamp.valueOf(currentTime));
//        orderInfo.setHour(Integer.valueOf(hourTime));
//        orderInfo.setDay(Integer.valueOf(dayTime));
//        orderInfo.setSettlemethod(0);
//        orderInfo.setBillcode(billcode);
//        orderInfo.setStallNo(sysApplication.getUserInfo().getCompanyno());
//
////        Log.i("ddddddddd", System.currentTimeMillis() + "=执行>>>>>>>>autoSendOrder" + orderBean.toString());
//
//        presenter.send2AxeWebOrderInfo(orderInfo);
//        presenter.send2FpmsWebOrderInfo(orderInfo);
//    }
//
//    /**
//     * VM
//     * OK 设置软件  背景色
//     */
//    private void setBackground() {
//        int index = MyPreferenceUtils.getSp(context).getInt("BACKGROUND_INDEX", 0);
//        binding.llKey.setBackgroundResource(BackgroundData.getData().get(index));
//        binding.llorder.setBackgroundResource(BackgroundData.getData().get(index));
//        binding.gvGoodMenu.setBackgroundResource(BackgroundData.getData().get(index));
//    }
//
//    //检查版本更新
//    private void checkVersion(int marketId) {
//        downloadDialog = new DownloadDialog(context);
//        ApkUtils.checkRemoteVersion(marketId, sysApplication, handler);
//    }
//
//    /**
//     * 上下文。使用上下文。
//     */
//    private DecimalFormat weightFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//
//    /**
//     * 控制器
//     */
//    private MainPresenter presenter;
//    private MainVM mainVM;
//    private ActivityMainBinding binding;
//    private MainObservableBean mainBean;
//
//    /**
//     * VM
//     * 初始化 塑料袋 价格问题
//     */
//    private void initBaseData() {
//        mainBean.getPriceLarge().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_LARGE, 0.3f));
//        mainBean.getPriceMiddle().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_MIDDLE, 0.2f));
//        mainBean.getPriceSmall().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_SMALL, 0.1f));
//    }
//
//    /**
//     * 使用
//     *
//     * @param savedInstanceState 0000J5034789
//     */
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//
//        presenter = new MainPresenter(this);
//        mainVM = new MainVM(this);
//
//        mainBean = new MainObservableBean();
//        binding.setUserInfo(sysApplication.getUserInfo());
//        binding.setMainBean(mainBean);
//        binding.setOnClickListener(this);
//
//        //1.用户信息可以使用
//        mainVM.userInfo = presenter.detectionUserInfo();
//
//        if (mainVM.userInfo != null) {
//            initViewFirst();
//            initHandler();
//            initAdapterView();
//            //初始化背景色
//            setBackground();
//            //初始化信息
//            mainVM.initHotGoods();
//            initBaseData();
//            initHeartBeat();
//        } else {
//            jumpActivity(HomeActivity.class, true);
//        }
//    }
//
//    /**
//     * 是否是第一次打开初始化
//     */
//    private boolean isInit = false;//是否初始化
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!isInit) {
//            checkVersion(mainVM.userInfo.getMarketid());//检查版本更新
////          HttpHelper.getmInstants(sysApplication).onCheckVersion(this, userInfo.getMarketid(), 777);
//            initSecondScreen();
//            askOrderState();
//            orderInfoDao = new OrderInfoDao(context);
//            orderBeanDao = new OrderBeanDao();
//            judegIsLock();
//            isInit = true;
//            presenter.send2FpmsSignIn(false);
//        }
//    }
//
//    /**
//     * 是否锁定
//     */
//    private void judegIsLock() {
////        boolean isLock = MyPreferenceUtils.getSp(context).getBoolean(LOCK_STATE, false);
//        boolean isCheatFlag = MyPreferenceUtils.getSp(context).getBoolean(INTENT_CHEATFLAG, false);
//        boolean lockState = MyPreferenceUtils.getSp(context).getBoolean(LOCK_STATE, false);
//
//        if (isCheatFlag || lockState) {
//            Intent intent = new Intent(this, LockActivity.class);
//            startActivity(intent);
//        }
//    }
//
////    @Override
////    public void sendHanderEmptyMessage(int what) {
////        if (NOTIFY_GOODS_DATA_CHANGE == what) {
////            handler.sendEmptyMessage(NOTIFY_GOODS_DATA_CHANGE);
////        }
////    }
//
//    private OrderInfoDao orderInfoDao;
//    private OrderBeanDao orderBeanDao;
//    boolean loopAskOrdering = true;
//
//    /**
//     * 轮询 询问订单信息
//     */
//    private void askOrderState() {
//        new Thread(() -> {
//            while (loopAskOrdering) {
//                try {
//                    if (sysApplication.getUserInfo().isConfigureEpayParams()) {//有配置才进行询问请求
//                        if (askInfos.size() > 0) {
//                            handler.sendEmptyMessage(NOTIFY_MARQUEE);
//                            for (int i = askInfos.size() - 1; i >= 0; i--) {
//                                OrderInfo orderInfo = askInfos.get(i);
//                                long timingTime = orderInfo.getTimingTime();
//                                if (timingTime <= 0) {
//                                    orderInfo.setTimingTime(System.currentTimeMillis());
//                                } else {
//                                    if (System.currentTimeMillis() - timingTime >= PAYMENT_TIMEOUT) {//等待支付超过180000（3分钟）
//                                        askInfos.remove(i);
//                                        continue;
//                                    }
//                                }
//                                HttpHelper.getmInstants(sysApplication).askOrderEx(mainVM.userInfo.getMchid(), askInfos.get(i).getBillcode(), MainActivityST.this, 4);
//                                Thread.sleep(100);
//                            }
//                        }
//                    }
//                    Thread.sleep(3000);
//                } catch (InterruptedException | IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 控制副屏
//     */
//    private void initSecondScreen() {
//        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
//        assert displayManager != null;
//        Display[] presentationDisplays = displayManager.getDisplays();
//        if (presentationDisplays.length > 1) {
//            banner = new SecondPresentation(this.getApplicationContext(), presentationDisplays[1]);
//            Objects.requireNonNull(banner.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
////            banner.show();
//        }
//    }
//
//    private HotGoodsDao hotGoodsDao;
//    private float oldFloat;
//    private boolean isEdSelect;
//
//    /**
//     * VM
//     * 获取称重重量信息
//     */
//    private void initHandler() {
//        handler = new Handler(msg -> {
//            int what = msg.what;
//            switch (what) {
//                case NOTIFY_GOODS_DATA_CHANGE:
//                    goodMenuAdapter.setDatas(presenter.getHotGoodList());
//                    break;
//                case NOTIFY_EPAY_SUCCESS:
//                    MyLog.blue("支付测试   微信支付");
//                    String message = msg.obj.toString();
//                    showLoading("0", message, 1500);
//                    clearnContext();
//                    break;
//                case NOTIFY_CLEAR:
//                    clearnContext();
//                    break;
//                case NOTIFY_AD_PICTURE_FORMAT:
//                    MyToast.showError(MainActivityST.this, "后台图片格式有误");
//                    break;
//                case 10012:
//                    runOnUiThread(() -> {
//                        if (msg.arg2 > 0) {
//                            downloadDialog.setProgress(msg.arg1, msg.arg2);//arg1:已下载字节数,arg2:总字节数
//                        }
//                    });
//                    break;
//                case 10013:
//                    final Version v = (Version) msg.obj;
//                    runOnUiThread(() -> {
//                        downloadDialog.setVersion(v.version);//版本
//                        downloadDialog.setDate(v.date);//更新日期
//                        downloadDialog.setMarketId(String.valueOf(v.getMarketId()));//市场编号
//                        downloadDialog.setDescription(v.description);//更新描述
//                        downloadDialog.setApkPath(v.apkPath);//apk路径
//                        downloadDialog.show();
//                    });
//                    break;
//                case 10014:
//                    runOnUiThread(() -> {
//                        downloadDialog.canForceQuit();
//                        Toast.makeText(MainActivityST.this, "更新App失败,请联系运营人员", Toast.LENGTH_LONG).show();
//                    });
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        });
//    }
//
//    private void dealWeight(float weightFloat, int isNegativeFlag) {
//        boolean isNegative;
//        isNegative = isNegativeFlag != 0;
//        try {
//            if (weightFloat == 0.000f) {
//                binding.tvWeightTop.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
//                binding.tvKg.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
//            } else {
//                binding.tvWeightTop.setTextColor(context.getResources().getColor(R.color.white));
//                binding.tvKg.setTextColor(context.getResources().getColor(R.color.white));
//            }
//            if (weightFloat <= 0) {
//                if (oldFloat > weightFloat) {
//                    binding.etPrice.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
//                    Selection.selectAll(binding.etPrice.getText());
//                    isEdSelect = true;
//                } else {
//                    if (!isEdSelect) {
//                        binding.etPrice.setTextColor(context.getResources().getColor(R.color.black));
//                    }
//                }
//            }
//            String weight2 = weightFormat.format(weightFloat * 2);//format 返回的是字符串
//            if (isNegative) {
//                mainBean.getNetWeight().set("-" + weightFormat.format(weightFloat));
//                mainBean.getCarryWewight().set("-" + weight2);
//            } else {
//                mainBean.getNetWeight().set(weightFormat.format(weightFloat));
//                mainBean.getCarryWewight().set(weight2);
//            }
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 清理内容
//     */
//    private void clearnContext() {
//        orderBeans.clear();
//        orderAdapter.notifyDataSetChanged();
//        mainBean.getTotalWeight().set(getResources().getString(R.string.default_weight));
//        mainBean.getTotalPrice().set(getResources().getString(R.string.default_price));
//        mainBean.getHintPrice().set(mainBean.getPrice().get());
//
//        binding.etPrice.setText("");
//
////        binding.etPrice.setHint();
//    }
//
//    /**
//     * 数字按键操作
//     */
//    private DigitalAdapter digitalAdapter;
////    // 随机Ad值
////    private int randomAd;
////    // 上传的Ad值
////    private int upZeroAd;
//
//    /**
//     * 按键操作
//     */
//    @Override
//    public void pressDigital(int position) {
//        if (selectedHotGood == null) {
//            return;
//        }
//
////        //价格按键被点击过，即价格变换了
////        long currentTime = System.currentTimeMillis();
////        orderBeanTemp.setTime1(currentTime);
//
//        if (isEdSelect) {
//            binding.etPrice.getText().clear();
//            isEdSelect = false;
//        }
//
//
//        String text = digitalAdapter.getItem(position);
//        String price = binding.etPrice.getText().toString();
//        if ("删除".equals(text)) {
//            if (price.length() > 0) {
//                String priceNew = price.substring(0, price.length() - 1);
//                binding.etPrice.setText(priceNew);
//                mainBean.getHintPrice().set(binding.etPrice.getText().toString());
//            }
//        } else {
//            if (".".equals(text)) {
//                if (!price.contains(".")) {
//                    String priceNew = price + text;
//                    binding.etPrice.setText(priceNew);
//                    mainBean.getHintPrice().set(binding.etPrice.getText().toString());
//                }
//            } else {
//                if ("0".equals(price)) {
//                    price = "";
//                }
//                String priceNew = price + text;
//                binding.etPrice.setText(priceNew);
//                mainBean.getHintPrice().set(binding.etPrice.getText().toString());
//            }
//        }
//        dealGrandMoney();
//
//        Log.i("ddddddddd", System.currentTimeMillis() + "=执行pressDigital");
//    }
//
//    /**
//     * VM
//     * 初始化控件
//     */
//    public void initAdapterView() {
//        //初始化结算列表
//        orderBeans = new ArrayList<>();
//        orderAdapter = new OrderAdapter(this, orderBeans);
//        binding.lvCommoditys.setAdapter(orderAdapter);
//        binding.lvCommoditys.setOnItemLongClickListener((parent, view, position, id) -> {
//            orderBeans.remove(position);
//            orderAdapter.notifyDataSetChanged();
//            calculatePrice();
//            return true;
//        });
//
//        goodMenuAdapter = new GoodMenuAdapter(this);
//        binding.gvGoodMenu.setAdapter(goodMenuAdapter);
//        binding.gvGoodMenu.setOnItemClickListener((parent, view, position, id) -> selectOrderBean(position));
//
//        digitalAdapter = new DigitalAdapter(this);
//        binding.gvDigital.setAdapter(digitalAdapter);
//        binding.gvDigital.setOnItemClickListener((parent, view, position, id) -> pressDigital(position));
//    }
//
//    /**
//     * 进行菜单选择操作
//     *
//     * @param position 菜单索引
//     */
//    @Override
//    public void selectOrderBean(int position) {
//        selectedHotGood = goodMenuAdapter.getItem(position);
//        mainBean.getGoodName().set(selectedHotGood.getName());
//        mainBean.getPrice().set("0");
//        binding.etPrice.setText("");
//        mainBean.getHintPrice().set(selectedHotGood.getPrice());
//
//
//        BigDecimal bigPrice = new BigDecimal(selectedHotGood.getPrice());
//        BigDecimal bigWeight = new BigDecimal(mainBean.getNetWeight().get());
//        bigPrice = bigPrice.multiply(bigWeight).setScale(2, BigDecimal.ROUND_HALF_UP);
//        mainBean.getGrandMoney().set(bigPrice.toPlainString());
////        binding.mainGrandtotalTv.setText(mainBean.getGrandMoneyString());
//
//        goodMenuAdapter.setCheckedAtPosition(position);
//        goodMenuAdapter.notifyDataSetChanged();
//
//        isFromPressMenu = true;
////        autoSendOrder(orderBeanTemp);
//
//        Log.i("ddddddddd", System.currentTimeMillis() + "=执行selectOrderBean");
//    }
//
//    //是否来自菜单的点击
//    private boolean isFromPressMenu;
//
//    /**
//     * 初始化心跳
//     */
//    private void initHeartBeat() {
//        if (mainVM.userInfo.getTid() > 0 && mainVM.userInfo.getMarketid() > 0) {
//            Intent intent = new Intent(MainActivityST.this, HeartBeatServcice.class);
//            intent.putExtra("marketid", mainVM.userInfo.getMarketid());
//            intent.putExtra("terid", mainVM.userInfo.getTid());
//            startService(intent);
//        }
//    }
//
//    /**
//     * 选定商品后，如有重量改变，则 价格控件 自动设置改变
//     * 设置 价格
//     */
//    @SuppressLint("DefaultLocale")
//    public void dealGrandMoney() {
//        if ("0".equalsIgnoreCase(mainBean.getHintPrice().get()) && "0".equalsIgnoreCase(mainBean.getPrice().get())) {
//            return;
//        }
//
//        String price = TextUtils.isEmpty(binding.etPrice.getText().toString()) ? binding.etPrice.getHint().toString() : binding.etPrice.getText().toString();
//        if (TextUtils.isEmpty(price)) {
//            price = "0.00";
//        }
//        if (TextUtils.isEmpty(mainBean.getNetWeight().get())) {
//            mainBean.getNetWeight().set("0.0");
//        }
//        BigDecimal bigPrice = new BigDecimal(price);
//        BigDecimal bigWeight = new BigDecimal(mainBean.getNetWeight().get());
//        bigPrice = bigPrice.multiply(bigWeight).setScale(2, BigDecimal.ROUND_HALF_UP);
//        mainBean.getGrandMoney().set(bigPrice.toPlainString());
//        binding.mainGrandtotalTv.setText(mainBean.getGrandMoneyString());
//    }
//
//    private String oldMoney;
//
//    /**
//     * 自动发送订单，上传规则 大于5毛  ，大于50g才上传数据
//     */
//    private void autoSendOrder(OrderBean orderBeanTemp) {
//        Log.i("ddddddddd", System.currentTimeMillis() + "=进入香山 autoSendOrder");
//
//        orderBeanTemp.setWeight(mainBean.getNetWeight().get());
//        orderBeanTemp.setIsStable(weightBean.getIsStable());
//
//        if (orderBeanTemp.getTime2() - orderBeanTemp.getTime1() < STABLE_TIME) {
//            return;
//        }
//
//        String price = TextUtils.isEmpty(binding.etPrice.getText().toString()) ? binding.etPrice.getHint().toString() : binding.etPrice.getText().toString();
//        if (TextUtils.isEmpty(price)) {
//            return;
//        }
//        float priceF = Float.valueOf(price);
//        if (priceF < 1) {//金额太小,将不自动上传数据
//            return;
//        }
//
//        //菜单列表点击 复位
//        if (isFromPressMenu) {
//            isFromPressMenu = false;
//            return;
//        }
//
//        if (!MyNetWorkUtils.isConnected(context)) {
//            return;
//        }
//
//        sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
//        sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
//        kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, VALUE_K_DEFAULT);
//
//        OrderBean orderBean = orderBeanTemp.clone();
//        orderBean.setPrice(price);
//        orderBean.setWeight(orderBeanTemp.getWeight());
//
//        orderBean.setX0(String.valueOf(weightBean.getZeroAd()));
//        orderBean.setX1(sbZeroAd + "");
//        orderBean.setX2(String.valueOf(weightBean.getTareWeight()));
//        orderBean.setWeight0(sbAd + "");
//        orderBean.setXcur(String.valueOf(weightBean.getCurrentAd()));
//        orderBean.setK(kValue + "");
//
//
//        orderBean.setMoney(mainBean.getGrandMoney().get());
//        orderBean.setTraceno(selectedHotGood.getBatchCode());
//        orderBean.setItemno(String.valueOf(selectedHotGood.getCid()));
//        orderBean.setName(mainBean.getGoodName().get());
//        //TODO delete20190724
////      isSendOrder = true;//锁定不让上传订单
//
//        if (Float.valueOf(orderBean.getWeight()) <= 0.1) {
//            return;
//        }
//        //上传非正常订单
//        if (TextUtils.isEmpty(orderBean.getName())) {
//            return;
//        }
//        if ("0.00".equals(orderBean.getMoney())) {
//            return;
//        }
//        if ("0.00".equals(orderBean.getPrice()) || null == orderBean.getPrice()) {
//            return;
//        }
//
//        if (!orderBean.getMoney().equals(oldMoney)) {
//            oldMoney = orderBean.getMoney();
//        } else {
//            return;
//        }
//        sendOrder2AxeWeb(orderBean);
//    }
//
//    /**
//     * 进行LiveDataBus事件处理
//     *
//     * @param type 事件类型
//     */
//    @Override
//    protected void onLiveEvent(String type) {
//        if (EVENT_NET_WORK_AVAILABLE.equals(type)) {
//            if (MyNetWorkUtils.isNetworkAvailable(context)) {// 0 用流量  ，1  wifi
//                if (orderInfoDao == null) {
//                    orderInfoDao = new OrderInfoDao(context.getApplicationContext());
//                }
//                HttpHelper.getmInstants(sysApplication).updateDataEx(orderInfoDao);
//            }
//        } else if (NOTIFY_USERINFO.equals(type)) {
//            mainVM.setUserInfo(sysApplication.getUserInfo());
//            binding.setUserInfo(sysApplication.getUserInfo());
//        } else if (LIVE_EVENT_NOTIFY_HOT_GOOD.equals(type)) {
//            goodMenuAdapter.setDatas(mainVM.getHotGoods());
//        } else if (BACKGROUND_CHANGE.equals(type)) {
//            setBackground();
//        } else if (SHOPPING_BAG_PRICE_CHANGE.equals(type)) {
//            initBaseData();
//        } else if (LOCK_STATE.equals(type)) {
//            Intent intent = new Intent(context, LockActivity.class);
//            startActivity(intent);
//        }
//    }
//
////    //最新的称重时间
////    private long newWeightTime;
////    //初始稳定时间
////    private long oldWeightTime;
//
//    /**
//     * @param event 事件
//     */
//    //定义处理接收的方法
//    @SuppressLint("SetTextI18n")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(BaseBusEvent event) {
//        String[] array;
//        float weightFloat;
//        switch (event.getEventType()) {
//            case MARKET_NOTICE:
//                AdResultBean.DataBean dataBean = (AdResultBean.DataBean) event.getOther();
//                CustomAlertDialog alertDialog = new CustomAlertDialog(context, dataBean);
//                alertDialog.show();
//                break;
//            case NOTIFY_AD_SECOND:// 副屏刷新广告图
//                if (!BaseBuglyApplication.DEBUG_MODE) {
//                    SecondScreen secondScreen = sysApplication.getBanner();
//                    if (secondScreen != null) {
//                        sysApplication.getBanner().initData();
//                    }
//                }
//                break;
//            case WEIGHT_ST://称重数据变化
//                String weight = (String) event.getOther();
//                weightFloat = Float.parseFloat(weight);
//                oldFloat = weightBean.getCurrentWeight();
//                dealWeight(weightFloat, 0);
//
//                dealGrandMoney();
//                //TODO  delete 20190730
//                autoSendOrder();
//                /*   自动上传非正常订单   End *******************************************************/
//                break;
//            case WEIGHT_ELECTRIC:
//                array = event.getOther().toString().split(" ");
//                if (array.length >= 3 && array[1].length() == 4 && array[2].length() == 2) {
//                    float eleData = Float.valueOf(array[1]);
//                    if (eleData < 1130) {
//                        eleData = 1130f;
//                    }
//                    int precent = (int) ((eleData - PowerConsumptionRankingsBatteryView.MinEleve) * PowerConsumptionRankingsBatteryView.ConstantEleve);
////                    int precent = electric / 100;
//                    int electricState = array[2].charAt(0) - 48;
//                    if (electricState == 0) {//无充电
//                        if (precent <= 20) {
//                            binding.ivImage.setImageResource(R.mipmap.ele1);
//                            MyToast.showError(context, "电量不足,请及时接通电源！");
//                        } else if (precent <= 50) {
//                            binding.ivImage.setImageResource(R.mipmap.ele2);
//                        } else if (precent <= 80) {
//                            binding.ivImage.setImageResource(R.mipmap.ele3);
//                        } else {
//                            binding.ivImage.setImageResource(R.mipmap.ele4);
//                        }
//                    } else if (electricState == 1) {//充电状态
//                        binding.ivImage.setImageResource(R.mipmap.ele0);
//                    }
//                    if (precent < 0) {
//                        precent = 0;
//                    } else if (precent >= 100) {
//                        precent = 100;
//                    }
//                    binding.tvElePrecent.setText(precent + "%");
//                }
//                break;
//            case NOTIFY_HOT_GOOD_CHANGE:
//                //初始化信息
//                mainVM.initHotGoods();
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 检验合格
//     */
//    @Override
//    protected void onDestroy() {
//        sysApplication.onDestory();
//        loopAskOrdering = false;
//        if (banner != null) {
//            banner.dismiss();
//            banner = null;
//        }
//        super.onDestroy();
//    }
//
//    /**
//     * 设置功能 待定算法
//     *
//     * @param requestCode 请求码
//     * @param resultCode  响应码
//     * @param data        参数
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == RC_SYS_SET) {
//                Intent intent = new Intent(this, SettingsActivity.class);
//                intent.putExtra(STRING_TYPE, 1);
//                startActivityForResult(intent, 1111);
//            } else if (requestCode == 1111) {// 数据改变了
//                boolean isDataChange = data.getBooleanExtra("isDataChange", false); //将计算的值回传回去
//                if (isDataChange) {// 设置改变了
//                    if (userInfoDao == null) {
//                        userInfoDao = new UserInfoDao();
//                    }
//                    mainVM.userInfo = userInfoDao.queryById(1);
//                    sysApplication.setUserInfo(mainVM.userInfo);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.main_cash_btn:
//                if (isCanAdd) {
//                    AddDirectlyOrder();
//                }
//                payCashDirect(0);
//                break;
//            case R.id.main_scan_pay:
//                if (MyNetWorkUtils.isNetWorkError(context)) {
//                    MyToast.showError(this, "使用第三方支付需要连接网络！");
//                } else {
//                    if (isCanAdd) {
//                        AddDirectlyOrder();
//                    }
//                    payCashDirect(1);
//                }
//                break;
//            case R.id.main_settings_btn:
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivityForResult(intent, 1111);
//                break;
//            case R.id.tvTid:
//                Intent intent2 = new Intent(this, SettingsActivity.class);
//                startActivityForResult(intent2, 1111);
//                break;
//            case R.id.btnSendZer:
//                if (oldFloat <= 1.2f) {
//                    sysApplication.getMyBaseWeighter().sendZer();
//                }
//                break;
//            case R.id.btnClearn:
//                clear();
//                break;
//            case R.id.btnAdd://累计
//                if (orderCanNext()) {
//                    accumulative();
//                    binding.etPrice.getText().clear();
//                }
//                break;
//            case R.id.rlBagLarge:
//                accumulativeBags(3);
//                break;
//            case R.id.rlBagMiddle:
//                accumulativeBags(2);
//                break;
//            case R.id.rlBagSmall:
//                accumulativeBags(1);
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 直接添加商品
//     */
//    private void AddDirectlyOrder() {
//        if (selectedHotGood == null) {
//            return;
//        }
//        if (TextUtils.isEmpty(binding.etPrice.getText()) && TextUtils.isEmpty(binding.etPrice.getHint())) {
//            return;
//        }
//        if (Float.parseFloat(mainBean.getGrandMoneyString()) <= 0) {
//            return;
//        }
//
//        OrderBean orderBean2 = new OrderBean();
//        String price = TextUtils.isEmpty(binding.etPrice.getText().toString()) ? binding.etPrice.getHint().toString() : binding.etPrice.getText().toString();
//        if (TextUtils.isEmpty(price)) {
//            price = "0.00";
//        }
//        orderBean2.setPrice(price);
//        selectedHotGood.setPrice(price);
//        if (hotGoodsDao == null) {
//            hotGoodsDao = new HotGoodsDao();
//        }
//        hotGoodsDao.update(selectedHotGood);
//        sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
//        sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
//        kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, VALUE_K_DEFAULT);
//
//        mainBean.getTotalWeight().set(mainBean.getNetWeight().get());
//        orderBean2.setWeight(mainBean.getNetWeight().get());
//
//
//        orderBean2.setX0(String.valueOf(weightBean.getZeroAd()));
//        orderBean2.setX1(sbZeroAd + "");
//        orderBean2.setX2(String.valueOf(weightBean.getTareWeight()));
//        orderBean2.setWeight0(sbAd + "");
//        orderBean2.setXcur(String.valueOf(weightBean.getCurrentAd()));
//        orderBean2.setK(kValue + "");
//
//
//        mainBean.getTotalPrice().set(mainBean.getGrandMoney().get());
//        orderBean2.setMoney(mainBean.getGrandMoney().get());
//        orderBean2.setTraceno(selectedHotGood.getBatchCode());
//
//        orderBean2.setItemno(String.valueOf(selectedHotGood.getCid()));
//        orderBean2.setName(mainBean.getGoodName().get());
//        if (!TextUtils.isEmpty(mainBean.getGoodName().get())) {
//            orderBeans.add(orderBean2);
//        }
//    }
//
//    /**
//     * VM
//     * 是否可以进行下一步操作
//     */
//    private boolean orderCanNext() {
//        if (selectedHotGood == null) {
//            return false;
//        }
//        if (TextUtils.isEmpty(binding.etPrice.getText()) && TextUtils.isEmpty(binding.etPrice.getHint())) {
//            return false;
//        }
//        if (Float.parseFloat(mainBean.getGrandMoneyString()) <= 0.05) {
//            return false;
//        }
//        if (TextUtils.isEmpty(mainBean.getGoodName().get())) {
//            return false;
//        }
//        return !TextUtils.isEmpty(mainBean.getNetWeight().get()) && !"0.000".equals(mainBean.getNetWeight().get());
//    }
//
//    /**
//     * 累计 菜品价格
//     */
//    private void accumulative() {
//        if (!orderCanNext()) {
//            return;
//        }
//
//        OrderBean orderBean = new OrderBean();
//        String price = TextUtils.isEmpty(binding.etPrice.getText().toString()) ? binding.etPrice.getHint().toString() : binding.etPrice.getText().toString();
//        if (TextUtils.isEmpty(price)) {
//            price = "0.00";
//        }
//        orderBean.setPrice(price);
//        selectedHotGood.setPrice(price);
//        if (hotGoodsDao == null) {
//            hotGoodsDao = new HotGoodsDao();
//        }
//        hotGoodsDao.update(selectedHotGood);
//
//        orderBean.setWeight(mainBean.getNetWeight().get());
//        orderBean.setMoney(mainBean.getGrandMoney().get());
//
//        sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
//        sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
//        kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, VALUE_K_DEFAULT);
//
//        orderBean.setX0(String.valueOf(weightBean.getZeroAd()));
//        orderBean.setX1(sbZeroAd + "");
//        orderBean.setX2(String.valueOf(weightBean.getTareWeight()));
//        orderBean.setWeight0(sbAd + "");
//        orderBean.setXcur(String.valueOf(weightBean.getCurrentAd()));
//        orderBean.setK(kValue + "");
//
//
//        orderBean.setItemno(String.valueOf(selectedHotGood.getCid()));
//        orderBean.setName(mainBean.getGoodName().get());
//        orderBean.setTraceno(selectedHotGood.getBatchCode());
//
//        isCanAdd = false;
//        orderBeans.add(orderBean);
//        calculatePrice();
//        orderAdapter.notifyDataSetChanged();
//        banner.setOrderBean(orderBeans, mainBean.getTotalPrice().get());
//    }
//
//    /**
//     * 计算购物袋
//     *
//     * @param bagType 1 小 ，2 中 ， 3 大
//     */
//    @SuppressLint("DefaultLocale")
//    private void accumulativeBags(int bagType) {
//        float price = 0;
//        String bagName = "小袋";
//        switch (bagType) {
//            case 1:
//                price = mainBean.getPriceSmall().get();
//                bagName = "小袋";
//                break;
//            case 2:
//                price = mainBean.getPriceMiddle().get();
//                bagName = "中袋";
//                break;
//            case 3:
//                price = mainBean.getPriceLarge().get();
//                bagName = "大袋";
//                break;
//        }
//        boolean isExist = false;
//        for (int i = 0; i < orderBeans.size(); i++) {
//            OrderBean orderBean = orderBeans.get(i);
//            if (orderBean.getName().equalsIgnoreCase(bagName)) {
//                isExist = true;
//                orderBean.setWeight(String.valueOf(Integer.valueOf(orderBean.getWeight()) + 1));
//                orderBean.setMoney(String.format("%.2f", Float.valueOf(orderBean.getMoney()) + price));
//            }
//        }
//
//        if (!isExist) {
//            OrderBean orderBean = new OrderBean();
//            orderBean.setPrice(String.valueOf(price));
//            orderBean.setWeight("1");
//            orderBean.setMoney(String.valueOf(price));
//
//            sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
//            sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
//            kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, VALUE_K_DEFAULT);
//
//            orderBean.setX0("0");
//            orderBean.setX1("0");
//            orderBean.setX2("0");
//            orderBean.setWeight0("0");
//            orderBean.setXcur("0");
//            orderBean.setK("0");
//            //
//
//            orderBean.setItemno(DEFAULT_BAG_ITEM_NO);
//            orderBean.setName(bagName);
//            orderBean.setTraceno(null);
//            orderBeans.add(0, orderBean);
//        }
//        calculatePrice();
//        orderAdapter.notifyDataSetChanged();
//        banner.setOrderBean(orderBeans, mainBean.getTotalPrice().get());
//    }
//
//    /**
//     * 数据 默认地址
//     */
//    @SuppressLint("DefaultLocale")
//    public void calculatePrice() {
//        float weightTotalF = 0.0000f;
//        float priceTotal = 0;
//        for (int i = 0; i < orderBeans.size(); i++) {
//            OrderBean goods = orderBeans.get(i);
//            if (!TextUtils.isEmpty(goods.getPrice())) {
//                if (!DEFAULT_BAG_ITEM_NO.equalsIgnoreCase(goods.getItemno())) {
//                    weightTotalF += Float.parseFloat(goods.getWeight());
//                }
//                priceTotal += Float.parseFloat(goods.getMoney());
//            }
//        }
//        mainBean.getTotalWeight().set(String.format("%.3f", weightTotalF));
//        mainBean.getTotalPrice().set(String.format("%.2f", priceTotal));
//    }
//
//    /**
//     * VM
//     * 清除按钮功能
//     */
//    public void clear() {
//        mainBean.getHintPrice().set("0.00");
//        mainBean.getPrice().set("");
//        mainBean.getGrandMoney().set("0.00");
//        mainBean.getTotalPrice().set("0.00");
//        mainBean.getTotalWeight().set("0.000");
//        orderBeans.clear();
//        mainBean.getGoodName().set("");
//        orderAdapter.notifyDataSetChanged();
//        selectedHotGood = null;
//        goodMenuAdapter.cleanCheckedPosition();
//        goodMenuAdapter.notifyDataSetChanged();
//    }
//
//    /**
//     * 支付结算
//     *
//     * @param payType 0:現金支付   1：扫码支付
//     */
//    @SuppressLint("DefaultLocale")
//    public void payCashDirect(int payType) {
//        if (orderBeans.size() == 0) {
//            return;
//        }
//        final OrderInfo orderInfo = new OrderInfo();//订单信息
//        Date date = new Date();
//        String currentTime = MyDateUtils.getYY_TO_ss(date);
//        final List<OrderBean> orderlist = new ArrayList<>(orderBeans);
//        //设置时间
//        for (OrderBean orderBean : orderlist) {
//            orderBean.setTime(currentTime);
//        }
//
//        orderInfo.setOrderItem(orderlist);
//        String billcode = "AX" + mainVM.userInfo.getTid() + MyDateUtils.getSampleNo(date);
//
//        String hourTime = MyDateUtils.getHH(date);
//        String dayTime = MyDateUtils.getDD(date);
//
//        if (payType == 0) {
//            orderInfo.setBillstatus("0");
//        } else {
//            orderInfo.setBillstatus("1");
//        }
//
//        orderInfo.setSeller(mainVM.userInfo.getSeller());
//        orderInfo.setSellerid(mainVM.userInfo.getSellerid());
//        orderInfo.setMarketName(mainVM.userInfo.getMarketname());
//        orderInfo.setTerid(mainVM.userInfo.getTid());
//
//        float totalMoney = 0;
//        float totalWeight = 0;
//        for (int i = 0; i < orderlist.size(); i++) {
//            OrderBean orderBean = orderlist.get(i);
//            totalMoney += Float.valueOf(orderBean.getMoney());
//            if (!DEFAULT_BAG_ITEM_NO.equalsIgnoreCase(orderBean.getItemno())) {
//                totalWeight += Float.valueOf(orderBean.getWeight());
//            }
//        }
//        orderInfo.setTotalamount(String.format("%.2f", totalMoney));
//        orderInfo.setTotalweight(String.format("%.3f", totalWeight));
//
//        orderInfo.setMarketid(mainVM.userInfo.getMarketid());
//        orderInfo.setTime(currentTime);
//        orderInfo.setTimestamp(Timestamp.valueOf(currentTime));
////        orderInfo.setOrderTime(MyDateUtils.getmm_ss());
//        orderInfo.setHour(Integer.valueOf(hourTime));
//        orderInfo.setDay(Integer.valueOf(dayTime));
//        orderInfo.setSettlemethod(payType);
//        orderInfo.setBillcode(billcode);
//        orderInfo.setStallNo(sysApplication.getUserInfo().getCompanyno());
//
//        String message = "支付金额：" + String.format("%.2f", totalMoney) + "元";
//        int delayTimes = 1500;
//        showLoading("0", message, delayTimes);
//        handler.sendEmptyMessage(NOTIFY_CLEAR);
//        sysApplication.getPrint().printOrder(sysApplication.getSingleThread(), orderInfo);
//        isCanAdd = true;
//
//        for (int i = 0; i < orderlist.size(); i++) {
//            OrderBean goods = orderlist.get(i);
//            goods.setOrderInfo(orderInfo);
//        }
//        orderInfoDao.insert(orderInfo);
//        orderBeanDao.insert(orderlist);
//
//        //结算订单
//        presenter.send2AxeWebOrderInfo(orderInfo);
//        //将AX订单上传至计量院
//        presenter.send2FpmsWebOrderInfo(orderInfo);
//
//        if (payType == 1) {
//            clearnContext();
//        }
//        askInfos.add(orderInfo);
//        banner.notifyData(orderInfo);
//    }
//
//    /**
//     * 订单集合列表
//     */
//    private final List<OrderInfo> askInfos = new ArrayList<>();
//
//    /**
//     * 连接错误，如果网络失败
//     *
//     * @param volleyError 错误信息
//     * @param flag        请求表示索引
//     */
//    @Override
//    public void onResponse(VolleyError volleyError, int flag) {
//        switch (flag) {
//            case 3:
//                MyLog.myInfo("错误" + volleyError.getMessage());
//                break;
//            case 4:
//                MyLog.myInfo("错误" + volleyError.getMessage());
//                break;
//            case 5:
//                MyLog.myInfo("错误" + volleyError.getMessage());
//                break;
//            case 777:
//                MyLog.myInfo("更新错误" + volleyError.getMessage());
//                break;
//        }
//    }
//
//    //    http://fpms.chinaap.com/admin/trade?executor=http&appCode=FPMSWS&data=1338EC0CF5D288B7F5694100704A1978E7E2506C8D7EE17E94FD
//    //    4BFA7C6AD12EC00EA1F620E91FBD40A3ACB407A2E0D048E0A976CEC4AB9A5656A17BB56A2755007BC3F4E143AD90A4DF50EAFDBF1A13
//    @Override
//    public void onResponse(final JSONObject jsonObject, final int flag) {
//        switch (flag) {
//            case 3:
//                MyLog.myInfo("成功" + jsonObject.toString());
//                break;
//            case 777:
//                MyLog.myInfo("成功" + jsonObject.toString());
//                break;
//            case 4://询问支付状态
//                sysApplication.getThreadPool().execute(() -> {
//                    ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
//                    if (resultInfo != null) {
//                        if (resultInfo.getStatus() == 0) {//支付成功
//                            String orderNo = resultInfo.getData();
//                            for (OrderInfo orderInfo : askInfos) {
//                                if (orderInfo.getBillcode().equals(orderNo)) {
//                                    askInfos.remove(orderInfo);
//                                    Message message = new Message();
//                                    String msg = "支付金额：" + orderInfo.getTotalamount() + "元";
//                                    message.what = NOTIFY_EPAY_SUCCESS;// 订单支付成功
//                                    message.obj = msg;
//                                    handler.sendMessage(message);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                });
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onStringResponse(VolleyError volleyError, int flag) {
//        MyLog.myInfo("错误" + volleyError);
//        switch (flag) {
//            case 3:
//                MyLog.myInfo("失败" + volleyError.toString());
//                break;
//            case 5:
//                MyLog.myInfo("错误" + volleyError.getMessage());
//                break;
//            default:
//                break;
//        }
//    }
//
//    //  返回支付状态
//    @Override
//    public void onStringResponse(String response, int flag) {
//        try {
//            if (flag == 3) {
//                OrderResultBean resultInfo = JSON.parseObject(response, OrderResultBean.class);
//                if (resultInfo != null && resultInfo.getStatus() == 0) {
//                    String billcode = resultInfo.getData().getBillcode();//交易号
//                    if (!TextUtils.isEmpty(billcode)) {
//                        orderInfoDao.update(billcode);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * VM
//     * 主页面点击返回键
//     */
//    @Override
//    public void onBackPressed() {
//        if (!mainVM.finishAll()) {
//            MyToast.toastShort(this, "再次点击退出！");
//        }
//    }
//
//    /**
//     * VM,点击长按按钮数据
//     */
//    @Override
//    public boolean onLongClick(View v) {
//        //进入系统设置，密码验证界面
//        if (v.getId() == R.id.main_settings_btn) {
//            jumpActivity(SystemLoginActivity.class, false);
//        }
//        return false;
//    }
//
//
//}