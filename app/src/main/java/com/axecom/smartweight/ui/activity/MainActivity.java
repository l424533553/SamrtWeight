package com.axecom.smartweight.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
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
import com.axecom.smartweight.manager.ActivityController;
import com.axecom.smartweight.my.MyEPSPrinter;
import com.axecom.smartweight.my.adapter.BackgroundData;
import com.axecom.smartweight.my.adapter.DigitalAdapter;
import com.axecom.smartweight.my.adapter.OrderAdapter;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.dao.GoodsDao;
import com.axecom.smartweight.my.entity.dao.OrderBeanDao;
import com.axecom.smartweight.my.entity.dao.OrderInfoDao;
import com.axecom.smartweight.my.entity.dao.TraceNoDao;
import com.axecom.smartweight.my.entity.dao.UserInfoDao;
import com.axecom.smartweight.my.entity.netresult.OrderResultBean;
import com.axecom.smartweight.my.entity.netresult.TraceNoBean;
import com.axecom.smartweight.my.helper.DateTimeUtil;
import com.axecom.smartweight.my.helper.HeartBeatServcice;
import com.axecom.smartweight.my.helper.HttpHelper;
import com.axecom.smartweight.my.helper.NetUtil;
import com.axecom.smartweight.my.helper.TimeThreadUtil;
import com.axecom.smartweight.my.rzl.utils.ApkUtils;
import com.axecom.smartweight.my.rzl.utils.DownloadDialog;
import com.axecom.smartweight.my.rzl.utils.Version;
import com.axecom.smartweight.ui.adapter.GoodMenuAdapter;
import com.luofx.help.CashierInputFilter;
import com.luofx.listener.VolleyListener;
import com.luofx.listener.VolleyStringListener;
import com.luofx.utils.DateUtils;
import com.luofx.utils.ToastUtils;
import com.luofx.utils.common.MyToast;
import com.luofx.utils.log.MyLog;
import com.luofx.utils.net.NetWorkJudge;
import com.shangtongyin.tools.serialport.WeightHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.axecom.smartweight.utils.CommonUtils.parseFloat;

public class MainActivity extends MainBaseActivity implements VolleyListener, VolleyStringListener,
        View.OnClickListener, View.OnLongClickListener, TimeThreadUtil.OnGetCurrentDateTimeListener {

    private GridView gvGoodMenu;
    private GridView digitalGridView;
    private ListView commoditysListView;
    private OrderAdapter orderAdapter;
    private GoodMenuAdapter goodMenuAdapter;

    private EditText etPrice;
    private TextView tvGoodsName;
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
    boolean switchSimpleOrComplex;

    /************************************************************************************/
    private DownloadDialog downloadDialog;

    // 市场名    售卖者   秤号
    private TextView tvmarketName, tvSeller, tvTid;
    private LinearLayout llKey, llorder;

    private void initViewFirst() {
        llorder = findViewById(R.id.llorder);
        llKey = findViewById(R.id.llKey);
        tvSeller = findViewById(R.id.tvSeller);
        tvmarketName = findViewById(R.id.tvmarketName);
        tvTid = findViewById(R.id.tvTid);
        tvNumber = findViewById(R.id.tvNumber);

        digitalGridView = findViewById(R.id.main_digital_keys_grid_view);
        commoditysListView = findViewById(R.id.main_commoditys_list_view);

//      countEt = findViewById(R.id.main_count_et);
        tvGoodsName = findViewById(R.id.tvGoodsName);
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
        initVersion();
    }


    /**
     * 初始化 版本
     */
    private void initVersion() {
        TextView tvVersion = findViewById(R.id.tvVersion);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getApplicationContext().getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0);
            String localVersion = packageInfo.versionName;
            tvVersion.setText(localVersion);//版本號
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setBackground() {
        int index = sharedPreferences.getInt("BACKGROUND_INDEX", 0);
        llKey.setBackgroundResource(BackgroundData.getData().get(index));
        llorder.setBackgroundResource(BackgroundData.getData().get(index));
        gvGoodMenu.setBackgroundResource(BackgroundData.getData().get(index));
    }

    //检查版本更新
    private void checkVersion(int marketId) {
        downloadDialog = new DownloadDialog(context);
        ApkUtils.checkRemoteVersion(marketId, sysApplication, handler);
    }

    private void initData() {
        //初始化 键盘 设置
        initDigital();
        weighUtils = new WeightHelper(handler);
        weighUtils.open();
        initHeartBeat();
    }

    private UserInfoDao userInfoDao;
    private UserInfo userInfo;

    private void initUserInfo() {
        userInfo = sysApplication.getUserInfo();
        if (userInfo == null) {
            MyToast.toastLong(context, "未读取到用户基本信息!");
            ActivityController.finishAll();
        }
        int MODE = Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;
        SharedPreferences share = context.getSharedPreferences("share", MODE);
//        int shellerid = share.getInt("shellerid", 0);
        share.edit().putInt("shellerid", userInfo.getSellerid()).apply();
    }

    /**
     * 上下文
     */
    private Context context;
    DecimalFormat decimalFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.

    private SharedPreferences sharedPreferences;

    private TextView date, time, week;
    private DateTimeUtil dateTimeUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityController.addActivity(this);
        helper = new HttpHelper(this, sysApplication);
        context = this;
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        initUserInfo();

        initViewFirst();
        initHandler();
        initView();
        initNetReceiver();
        setBackground();
        initData();

        getGoods(false);

        checkVersion(userInfo.getMarketid());//检查版本更新

        Intent intent = new Intent();
        intent.setAction("com.axecom.iweight.carouselservice.datachange");
        sendBroadcast(intent);

        initSecondScreen();
        askOrderState();
        initDate();


        orderInfoDao = new OrderInfoDao(context);
        orderBeanDao = new OrderBeanDao(context);
        getTraceNo(userInfo);
        judegIsLock();
    }

    private void initDate() {
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
//        week = findViewById(R.id.week);
        dateTimeUtil = DateTimeUtil.getInstance();
        new TimeThreadUtil(this).start();
    }

    @Override
    public void onGetDateTime() {
        time.setText(dateTimeUtil.getCurrentTime());//显示时间
        date.setText(dateTimeUtil.getCurrentDate());//显示年月日
//        week.setText(dateTimeUtil.getCurrentWeekDay(0));//显示星期几
    }


    private void getTraceNo(UserInfo userInfo) {
        if (NetWorkJudge.isNetworkAvailable(context)) {
            String url = BASE_IP_ST + "/api/smartsz/gettracenolist?shid=" + userInfo.getSellerid();
            sysApplication.volleyGet(url, this, 7);
        }
    }

    /**
     * 是否锁定
     */
    private void judegIsLock() {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean isLock = sp.getBoolean(LOCK_STATE, false);
        if (isLock) {
            Intent intent = new Intent(this, LockActivity.class);
            startActivity(intent);
        }
    }

    //广播
    private NetBroadcastReceiver recevier;
    private IntentFilter intentFilter;

    private void initNetReceiver() {
        recevier = new NetBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.axecom.smartweight.ui.activity.setting.background.change");
    }

    /**
     * @param isUpdateTrace true 只是刷新批次号
     */
    private void getGoods(boolean isUpdateTrace) {
        if (!isUpdateTrace) {
            initUserInfo();
            tvmarketName.setText(userInfo.getMarketname());
            tvSeller.setText(userInfo.getSeller());
            tvTid.setText(userInfo.getTid() + "");
            tvNumber.setText(userInfo.getCompanyno());
        }

        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (goodsDao == null) {
                    goodsDao = new GoodsDao(context);
                }
                goodsList = goodsDao.queryAll();
                handler.sendEmptyMessage(NOTIFY_DATA_CHANGE);
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
                    handler.sendEmptyMessage(NOTIFY_TRACENO);
                }
            }
        });
    }

    private TraceNoDao traceNoDao;
    private OrderInfoDao orderInfoDao;
    private OrderBeanDao orderBeanDao;
    //    private CustomScrollBar customScrollBar;
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
                        if (askInfos.size() > 0) {
                            handler.sendEmptyMessage(NOTIFY_MARQUEE);
                            for (int i = 0; i < askInfos.size(); i++) {
                                helper.askOrder(userInfo.getMchid(), askInfos.get(i).getBillcode(), MainActivity.this, 4);
                                Thread.sleep(100);
                            }
                        }
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
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
//        LogUtils.d("------------: " + presentationDisplays.length + "  --- " + presentationDisplays[1].getName());
        if (presentationDisplays.length > 1) {
            banner = new SecondPresentation(this.getApplicationContext(), presentationDisplays[1]);
//            banner.setData(askInfos);
            Objects.requireNonNull(banner.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            banner.show();
        }
    }

    private GoodsDao goodsDao;
    private List<Goods> goodsList;

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
                    case NOTIFY_WEIGHT:
                        String weight = (String) msg.obj;
                        try {
                            float weightFloat = Float.parseFloat(weight);
                            if (weightFloat <= 0) {
                                if (oldFloat > weightFloat) {
                                    Selection.selectAll(etPrice.getText());
                                    isEdSelect = true;
                                }

//                              etPrice.getText().clear();
                            }
                            oldFloat = weightFloat;
                            String weight2 = decimalFormat.format(weightFloat * 2);//format 返回的是字符串
                            weightTv.setText(weight);
                            weightTopTv.setText(weight);
                            tvCatty.setText(weight2);
                            setGrandTotalTxt();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        break;
                    case NOTIFY_MARQUEE:

                        break;
                    case NOTIFY_TRACENO:
                        goodMenuAdapter.notifyDataSetChanged();
                    case NOTIFY_DATA_CHANGE:
                        goodMenuAdapter.setDatas(goodsList);
                        break;
                    case NOTIFY_EPAY_SUCCESS:
                        String message = msg.obj.toString();
                        showLoading("0", message, 1500);
                        clearnContext(0);

                        break;
                    case NOTIFY_CLEAR:
                        clearnContext(0);
                        break;
                    case NOTIFY_TRACE_STATE:
                        getGoods(true);
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
                tvTotalPrice.setText("0.00");
                tvTotalWeight.setText("0.00");
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
                float count = 0;

                if (switchSimpleOrComplex) {
                    tvgrandTotal.setText(String.format("%.2f", parseFloat(selectedGoods.getPrice()) * count));
                } else {
                    if (weightTopTv.getText().toString().indexOf('.') == -1 || weightTopTv.getText().length() - (weightTopTv.getText().toString().indexOf(".") + 1) <= 1) {
                        tvgrandTotal.setText(String.format("%.2f", parseFloat(selectedGoods.getPrice()) * parseFloat(weightTopTv.getText().toString()) / 1000));
                    } else {
                        tvgrandTotal.setText(String.format("%.2f", parseFloat(selectedGoods.getPrice()) * parseFloat(weightTopTv.getText().toString())));
                    }
                }
                goodMenuAdapter.setCheckedAtPosition(position);
                goodMenuAdapter.notifyDataSetChanged();
            }
        });
        switchSimpleOrComplex = false;
    }

    //商通的称重  工具类
    private WeightHelper weighUtils;
    private HeartBeatServcice.MyBinder myBinder;
    private HeartBeatServcice heartBeatServcice;
    private ServiceConnection mConnection;

    /**
     * 初始化心跳
     */
    private void initHeartBeat() {
        if (userInfo.getTid() > 0 && userInfo.getMarketid() > 0) {
            mConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    myBinder = (HeartBeatServcice.MyBinder) service;
                    heartBeatServcice = myBinder.getService();
                    heartBeatServcice.setMarketid(userInfo.getMarketid());
                    heartBeatServcice.setTerid(userInfo.getTid());
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.d("MainActivity", "onServiceDisconnected");
                }
            };
            Intent serviceIntent = new Intent(this, HeartBeatServcice.class);
            bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
        } else {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    /**
     * 设置价格
     */
    @SuppressLint("DefaultLocale")
    public void setGrandTotalTxt() {
        try {
            float count = 0;

            if (switchSimpleOrComplex) {
                if (!TextUtils.isEmpty(etPrice.getText())) {
                    tvgrandTotal.setText(String.format("%.2f", parseFloat(etPrice.getText().toString()) * count));
                } else if (!TextUtils.isEmpty(etPrice.getHint())) {
                    tvgrandTotal.setText(String.format("%.2f", parseFloat(etPrice.getHint().toString()) * count));
                }

            } else {
                if (!TextUtils.isEmpty(etPrice.getText())) {
                    if (weightTopTv.getText().toString().indexOf('.') == -1 || weightTopTv.getText().length() - (weightTopTv.getText().toString().indexOf(".") + 1) <= 1) {
                        tvgrandTotal.setText(String.format("%.2f", parseFloat(etPrice.getText().toString()) * parseFloat(weightTopTv.getText().toString()) / 1000));
                    } else {
                        tvgrandTotal.setText(String.format("%.2f", parseFloat(etPrice.getText().toString()) * parseFloat(weightTopTv.getText().toString())));
                    }
                } else if (!TextUtils.isEmpty(etPrice.getHint())) {
                    if (weightTopTv.getText().toString().indexOf('.') == -1 || weightTopTv.getText().length() - (weightTopTv.getText().toString().indexOf(".") + 1) <= 1) {
                        tvgrandTotal.setText(String.format("%.2f", parseFloat(etPrice.getHint().toString()) * parseFloat(weightTopTv.getText().toString()) / 1000));
                    } else {
                        tvgrandTotal.setText(String.format("%.2f", parseFloat(etPrice.getHint().toString()) * parseFloat(weightTopTv.getText().toString())));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
        if (weighUtils != null) {
            weighUtils.closeSerialPort();
        }
        isAskOrdering = false;
        if (banner != null) {
            if (banner.isShowing()) {
                banner.dismiss();
            }
            banner = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2222) {
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
                    getGoods(false);// 需要更新 状态
                }

                if (isLocalChange) {
                    setBackground();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_cash_btn:
                if (orderBeans.size() == 0) {
                    if (selectedGoods == null) {
                        return;
                    }
                    if (TextUtils.isEmpty(etPrice.getText()) && TextUtils.isEmpty(etPrice.getHint())) {
                        return;
                    }
                    if (parseFloat(tvgrandTotal.getText().toString()) <= 0) {
                        return;
                    }

                    OrderBean orderBean = new OrderBean();
                    String price = TextUtils.isEmpty(etPrice.getText().toString()) ? etPrice.getHint().toString() : etPrice.getText().toString();
                    orderBean.setPrice(price);
                    selectedGoods.setPrice(price);
                    goodsDao.update(selectedGoods);


                    String weight = weightTopTv.getText().toString();
                    tvTotalWeight.setText(weight);
                    orderBean.setWeight(weight);
                    orderBean.setTraceno(selectedGoods.getBatchCode());

                    String grandTotal = tvgrandTotal.getText().toString();
                    tvTotalPrice.setText(grandTotal);
                    orderBean.setMoney(grandTotal);

                    orderBean.setItemno(String.valueOf(selectedGoods.getCid()));
                    orderBean.setName(tvGoodsName.getText().toString());

                    if (!TextUtils.isEmpty(tvGoodsName.getText().toString())) {
                        orderBeans.add(orderBean);
                    }
//                    orderAdapter.notifyDataSetChanged();
//                    calculatePrice();
                }
                payCashDirect(0);

                break;
            case R.id.main_scan_pay:
                if (!NetWorkJudge.checkedNetWork(context)) {
                    ToastUtils.showToast(this, "使用第三方支付需要连接网络！");
                    break;
                }

                if (orderBeans.size() == 0) {
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
                    String price2 = TextUtils.isEmpty(etPrice.getText().toString()) ? etPrice.getHint().toString() : etPrice.getText().toString();
                    orderBean2.setPrice(price2);
                    selectedGoods.setPrice(price2);
                    goodsDao.update(selectedGoods);

                    String weight2 = weightTopTv.getText().toString();
                    tvTotalWeight.setText(weight2);
                    orderBean2.setWeight(weight2);

                    String grandTotal2 = tvgrandTotal.getText().toString();
                    tvTotalPrice.setText(grandTotal2);
                    orderBean2.setMoney(grandTotal2);
                    orderBean2.setTraceno(selectedGoods.getBatchCode());
                    orderBean2.setItemno(String.valueOf(selectedGoods.getCid()));
                    orderBean2.setName(tvGoodsName.getText().toString());
                    orderBeans.add(orderBean2);
//                    orderAdapter.notifyDataSetChanged();
//                    calculatePrice();
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
                accumulative(false);
                clearnContext(1);
                break;
        }
    }


//    // 为了获取结果
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
//        // operation succeeded. 默认值是-1
//        if (resultCode == RESULT_OK) {
////            if (requestCode == REQUESTCODE) {
////                int three = data.getIntExtra("three", 0);
////                //设置结果显示框的显示数值
////                result.setText(String.valueOf(three));
////            }
//        }
//    }


    private void appendCurrentGood() {
        if (orderBeans.size() == 0)
            accumulative(true);
    }

    /**
     * 累计 菜品价格
     */
    private void accumulative(boolean clean) {
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
        orderBean.setPrice(price);
        selectedGoods.setPrice(price);
        goodsDao.update(selectedGoods);

        String weight = weightTopTv.getText().toString();
        orderBean.setWeight(weight);

        String grandTotal = tvgrandTotal.getText().toString();
        orderBean.setMoney(grandTotal);

        orderBean.setItemno(String.valueOf(selectedGoods.getCid()));
        orderBean.setName(tvGoodsName.getText().toString());
        orderBean.setTraceno(selectedGoods.getBatchCode());

        orderBeans.add(orderBean);
        calculatePrice();
        orderAdapter.notifyDataSetChanged();
        banner.setOrderBean(orderBeans, tvTotalPrice.getText().toString());

    }


    @SuppressLint("DefaultLocale")
    public void calculatePrice() {
        float weightTotalF = 0.0000f;
        float priceTotal = 0;
        for (int i = 0; i < orderBeans.size(); i++) {
            OrderBean goods = orderBeans.get(i);
            if (!TextUtils.isEmpty(goods.getPrice())) {
                weightTotalF += parseFloat(goods.getWeight());
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
            if (oldFloat <= 5.0f) {
                weighUtils.resetBalance();
            }
        }
        if (type == 1) {
            weightTopTv.setText("0.000");
            tvCatty.setText("0.000");
            weightTv.setText("");
            etPrice.setHint("0");
            etPrice.setText("");
            tvTotalWeight.setText("0");
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

    @Override
    protected void onResume() {
        super.onResume();
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        registerReceiver(recevier, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recevier != null) {
            unregisterReceiver(recevier);
        }

    }

    private class NetBroadcastReceiver extends BroadcastReceiver {
        private OrderInfoDao orderInfoDao;

        @Override
        public void onReceive(Context context, Intent intent) {
            //如果相等的话就说明网络状态发生了变化
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                int netWorkState = NetUtil.getNetWorkState(context);
                if (netWorkState >= 0) {// 0 用流量  ，1  wifi
                    // TODO 开启上传模式
                    updateData(context);
                }
                MyLog.myInfo("网络转态" + netWorkState);
            }

            if (intent.getAction().equals("com.axecom.smartweight.ui.activity.setting.background.change")) {
                setBackground();
            }
        }

        /**
         * 传输数据，传输 离线订单内容
         *
         * @param context 内容
         */
        private void updateData(final Context context) {
            sysApplication.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (orderInfoDao == null) {
                        orderInfoDao = new OrderInfoDao(context);
                    }
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
                            public void onFailure(okhttp3.Call call, IOException e) {
                                MyLog.blue(e.getMessage());
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                                // 注：该回调是子线程，非主线程
//                                Log.i("wxy", "callback thread id is " + Thread.currentThread().getId());
//                                Log.i("wxy", response.body().string());
                                ResultInfo resultInfo = com.alibaba.fastjson.JSON.parseObject(response.body().string(), ResultInfo.class);
                                if (resultInfo.getStatus() == 0) {
                                    List<String> data = com.alibaba.fastjson.JSON.parseArray(resultInfo.getData(), String.class);
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
                            }
                        });
                    }
                }
            });
        }
    }

    private void okHttp3(String json) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        okhttp3.Request request = new okhttp3.Request.Builder().url(BASE_IP_ST + "/api/smart/commitszexlist").method("POST", body).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.同步调用会阻塞主线程,这边在子线程进行
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                MyLog.blue(e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                // 注：该回调是子线程，非主线程
                Log.i("wxy", "callback thread id is " + Thread.currentThread().getId());
                Log.i("wxy", response.body().string());
            }
        });


    }


    private boolean isOrderSuccess;

    /**
     * @param payType 0:現金   1：微信支付
     */
    public void payCashDirect(int payType) {
        if (orderBeans.size() == 0) {
            return;
        }
//        final List<OrderBean> data = new ArrayList<>(orderBeans);
        final OrderInfo orderInfo = new OrderInfo();
        final List<OrderBean> orderlist = new ArrayList<>(orderBeans);
        orderInfo.setOrderItem(orderlist);
        String billcode = "AX" + DateUtils.getSampleNo() + userInfo.getTid();
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
        orderInfo.setTotalamount(tvTotalPrice.getText().toString());
        orderInfo.setTotalweight(tvTotalWeight.getText().toString());

        orderInfo.setMarketid(userInfo.getMarketid());
        orderInfo.setTime(currentTime);
        orderInfo.setTimestamp(Timestamp.valueOf(currentTime));
        orderInfo.setHour(Integer.valueOf(hourTime));
        orderInfo.setDay(Integer.valueOf(dayTime));
        orderInfo.setSettlemethod(payType);
        orderInfo.setBillcode(billcode);
        orderInfo.setStallNo(tvNumber.getText().toString());

        String message = "支付金额：" + tvTotalPrice.getText().toString() + "元";
        int delayTimes = 1500;
        showLoading("0", message, delayTimes);
        handler.sendEmptyMessage(NOTIFY_CLEAR);

        // 打印数据
        if (epsPrinter == null) {
            epsPrinter = new MyEPSPrinter(sysApplication.getEpsPrint());
        }
        epsPrinter.printOrder(sysApplication.getSingleThread(), orderInfo);

        //保存交易数据
        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < orderlist.size(); i++) {
                    OrderBean goods = orderlist.get(i);
                    goods.setOrderInfo(orderInfo);
                }
                orderInfoDao.insert(orderInfo);
                orderBeanDao.insert(orderlist);
                if (NetWorkJudge.isNetworkAvailable(context)) {
                    helper.commitDD(orderInfo, MainActivity.this, 3);
                }
            }
        });


        clearnContext(0);
//        askInfos.add(orderInfo);
        banner.notifyData(orderInfo);
        isOrderSuccess = true;

//        if (payType == 1) {
////         String baseUrl  = "http://pay.axebao.com/payInterface_gzzh/pay?key=" + key + "&mchid=" + mchid;
//            //TODO
//            banner.addData(orderInfo);
////            askInfos.add(orderInfo);
////            banner.notifyData(askInfos);
//        }


    }

    private MyEPSPrinter epsPrinter;
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
        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                switch (flag) {
                    case 3:
                        MyLog.myInfo("成功" + jsonObject.toString());
                        break;
                    case 4:
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
                                        message.what = NOTIFY_EPAY_SUCCESS;
                                        message.obj = msg;
                                        handler.sendMessage(message);
                                        break;
                                    }
                                }
                            }
                        }
                        MyLog.myInfo("成功" + jsonObject.toString());
                        break;
                    case 5:

                        break;
                    case 7:
                        if (resultInfo != null) {
                            if (resultInfo.getStatus() == 0) {
                                List<TraceNoBean> goodsList = JSON.parseArray(resultInfo.getData(), TraceNoBean.class);
                                if (goodsList != null && goodsList.size() > 0) {
                                    TraceNoDao traceNoDao = new TraceNoDao(context);
                                    traceNoDao.deleteTableData();
                                    int flag2 = traceNoDao.insert(goodsList);
                                    //TODO  通知更新批次號
                                    handler.sendEmptyMessage(NOTIFY_TRACE_STATE);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onResponseError(VolleyError volleyError, int flag) {
        MyLog.myInfo("错误" + volleyError);
        switch (flag) {
            case 3:
                MyLog.myInfo("失败" + volleyError.toString());
                break;
            case 5:
                //TODO   555666 计划修改状态
                MyLog.myInfo("错误" + volleyError.getMessage());
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int pressCount = 0;

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
            case R.id.btnClearn:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("是否删除待支付订单")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                askInfos.clear();
                                handler.sendEmptyMessage(NOTIFY_MARQUEE);
//                                banner.notifyData(askInfos);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
                break;
            case R.id.main_settings_btn:
                Intent intent2 = new Intent(context, SystemLoginActivity.class);
                startActivityForResult(intent2, 2222);
                break;
        }
        return false;
    }

}



