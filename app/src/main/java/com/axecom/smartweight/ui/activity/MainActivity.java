package com.axecom.smartweight.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
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
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.MyEPSPrinter;
import com.axecom.smartweight.my.adapter.DigitalAdapter;
import com.axecom.smartweight.my.adapter.OrderAdapter;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.GoodsType;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.dao.GoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsTypeDao;
import com.axecom.smartweight.my.entity.dao.OrderBeanDao;
import com.axecom.smartweight.my.entity.dao.OrderInfoDao;
import com.axecom.smartweight.my.entity.dao.TraceNoDao;
import com.axecom.smartweight.my.entity.dao.UserInfoDao;
import com.axecom.smartweight.my.entity.netresult.OrderResultBean;
import com.axecom.smartweight.my.entity.netresult.TraceNoBean;
import com.axecom.smartweight.my.entity.netresult.TracenoResultBean;
import com.axecom.smartweight.my.helper.HeartBeatServcice;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.axecom.smartweight.utils.CommonUtils.parseFloat;

public class MainActivity extends MainBaseActivity implements VolleyListener, VolleyStringListener, View.OnClickListener, View.OnLongClickListener {

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

    /**
     * 摊位号
     */
    private TextView tvNumber;
    private TextView weightTopTv;
    private List<OrderBean> orderBeans;
    private Goods selectedGoods;
    public SecondPresentation banner;
    boolean switchSimpleOrComplex;

    /************************************************************************************/
    private DownloadDialog downloadDialog;

    // 市场名    售卖者   秤号
    private TextView tvmarketName, tvSeller, tvTid;

    private void initViewFirst() {
        tvSeller = findViewById(R.id.tvSeller);
        tvmarketName = findViewById(R.id.tvmarketName);
        tvTid = findViewById(R.id.tvTid);
        tvNumber = findViewById(R.id.tvNumber);

        digitalGridView = findViewById(R.id.main_digital_keys_grid_view);
        commoditysListView = findViewById(R.id.main_commoditys_list_view);

//        countEt = findViewById(R.id.main_count_et);
        tvGoodsName = findViewById(R.id.tvGoodsName);
        tvgrandTotal = findViewById(R.id.main_grandtotal_tv);
        tvTotalWeight = findViewById(R.id.main_weight_total_tv);
//        weightTotalMsgTv = findViewById(R.id.main_weight_total_msg_tv);
        weightTv = findViewById(R.id.main_weight_tv);


        tvTotalPrice = findViewById(R.id.main_price_total_tv);
        weightTopTv = findViewById(R.id.main_weight_top_tv);
//        weightTopMsgTv = findViewById(R.id.main_weight_msg_tv);
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
            this.finish();
            System.exit(0);
        }
    }


    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initUserInfo();

        initViewFirst();
        initHandler();
        initView();
        initData();

        getGoods();


        checkVersion(userInfo.getMarketid());//检查版本更新
        initSecondScreen();
        askOrderState();

        orderInfoDao = new OrderInfoDao(context);
        orderBeanDao = new OrderBeanDao(context);

    }

    private void getGoods() {
        initUserInfo();
        tvmarketName.setText(userInfo.getMarketname());
        tvSeller.setText(userInfo.getSeller());
        tvTid.setText(userInfo.getTid() + "");
        tvNumber.setText(userInfo.getCompanyno());

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


//        goodMenuAdapter.setDatas(goodsList);

//        if (NetWorkJudge.isNetworkAvailable(context)) {
//            String url = BASE_IP_ST + "/api/smartsz/gettracenolist?shid=" + sellerid;
//            sysApplication.volleyGet(url, this, 6);
//        }

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
            banner.setData(askInfos);
            SysApplication.bannerActivity = banner;
            Objects.requireNonNull(banner.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            banner.show();
        }
    }

    private GoodsDao goodsDao;
    private List<Goods> goodsList;

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
                        weightTv.setText(weight);
                        weightTopTv.setText(weight);
                        setGrandTotalTxt();
                        break;
                    case NOTIFY_MARQUEE:
                        setmarQueeNotify();
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
                        setmarQueeNotify();
                        break;
                    case NOTIFY_CLEAR:
                        clearnContext(0);
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


    private void setmarQueeNotify() {
//        if (scrollBarBuilder == null) {
//            scrollBarBuilder = new StringBuilder();
//        }
//        scrollBarBuilder.setLength(0);
//
//        for (OrderInfo orderinfo : askInfos) {
//            scrollBarBuilder.append(orderinfo.getTotalamount()).append("元，待支付").append(textEmpty);
//        }

//        customScrollBar.setText(scrollBarBuilder.toString());


        banner.notifyData(askInfos);

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

    // 商通的称重  工具类
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


    @SuppressLint("DefaultLocale")
    public void setGrandTotalTxt() {
        try {
            float count = 0;
//            if (!TextUtils.isEmpty(countEt.getText())) {
//                count = parseFloat(countEt.getText().toString());
//            } else if (!TextUtils.isEmpty(countEt.getHint())) {
//                count = parseFloat(countEt.getHint().toString());
//            }

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

//        sysApplication.getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                List<Goods> goodsList = goodMenuAdapter.getList();
//                if (goodsList != null && goodsList.size() > 0) {
//                    for (Goods goods : goodsList) {
//                        goodsDao.updateOrInsert(goods);
//                    }
//                }
//            }
//        });


        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2222) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 1111);
            } else if (requestCode == 1111) {
                boolean isDataChange = data.getBooleanExtra("isDataChange", false); //将计算的值回传回去
                if (isDataChange) {// 设置改变了
                    //TODO
                    if (userInfoDao == null) {
                        userInfoDao = new UserInfoDao(context);
                    }
                    sysApplication.setUserInfo(userInfo);
                    userInfo = userInfoDao.queryById(1);
                    getGoods();// 需要更新 状态
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

                    String grandTotal = tvgrandTotal.getText().toString();
                    tvTotalPrice.setText(grandTotal);
                    orderBean.setMoney(grandTotal);

                    orderBean.setItemno(String.valueOf(selectedGoods.getCid()));
                    orderBean.setName(tvGoodsName.getText().toString());

                    orderBeans.add(orderBean);
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

                    OrderBean orderBean = new OrderBean();
                    String price = TextUtils.isEmpty(etPrice.getText().toString()) ? etPrice.getHint().toString() : etPrice.getText().toString();
                    orderBean.setPrice(price);
                    selectedGoods.setPrice(price);
                    goodsDao.update(selectedGoods);

                    String weight = weightTopTv.getText().toString();
                    tvTotalWeight.setText(weight);
                    orderBean.setWeight(weight);

                    String grandTotal = tvgrandTotal.getText().toString();
                    tvTotalPrice.setText(grandTotal);
                    orderBean.setMoney(grandTotal);

                    orderBean.setItemno(String.valueOf(selectedGoods.getCid()));
                    orderBean.setName(tvGoodsName.getText().toString());
                    orderBeans.add(orderBean);
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

        orderBeans.add(orderBean);
        calculatePrice();
        orderAdapter.notifyDataSetChanged();

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
            weighUtils.resetBalance();
        }
        if (type == 1) {
            weightTopTv.setText("0.000");
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

   /* public void charge(boolean useCash) {

        if (orderBeans.size() < 1) {
            accumulative(true);
        }
        Intent intent = new Intent();
        SubOrderReqBean subOrderReqBean = new SubOrderReqBean();
        SubOrderReqBean.Goods good;
        List<SubOrderReqBean.Goods> goodsList = new ArrayList<>();
        for (int i = 0; i < orderBeans.size(); i++) {
            good = new SubOrderReqBean.Goods();
            OrderBean HotKeyBean = orderBeans.get(i);
            good.setGoods_id(HotKeyBean.getItemno() + "");
            good.setGoods_name(HotKeyBean.getName());
            good.setGoods_price(HotKeyBean.getPrice());
            good.setGoods_number(countEt.getText().toString());
            good.setGoods_weight(HotKeyBean.getWeight());
            good.setGoods_amount(HotKeyBean.getMoney());
            good.setBatch_code(HotKeyBean.getTraceno());
            goodsList.add(good);
        }
        subOrderReqBean.setToken(AccountManager.getInstance().getToken());
        subOrderReqBean.setMac(MacManager.getInstace(this).getMac());
        subOrderReqBean.setTotal_amount(tvTotalPrice.getText().toString());
        subOrderReqBean.setTotal_weight(tvTotalWeight.getText().toString());
        subOrderReqBean.setCreate_time(getCurrentTime());
        String orderNo = "AX" + getCurrentTime("yyyyMMddHHmmss") + AccountManager.getInstance().getScalesId();
        subOrderReqBean.setOrder_no(orderNo);
        subOrderReqBean.setGoods(goodsList);
        if (switchSimpleOrComplex) {
            subOrderReqBean.setPricing_model("2");
        } else {
            subOrderReqBean.setPricing_model("1");
        }


        intent.setClass(this, UseCashActivity.class);


    }*/


    /**
     * 直接现金支付
     */
    public void payCashDirect(int payType) {
        final List<OrderBean> data = new ArrayList<>(orderBeans);
        final OrderInfo orderInfo = new OrderInfo();
        List<OrderBean> orderlist = new ArrayList<>(data);
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

        if (NetWorkJudge.isNetworkAvailable(context)) {
            helper.commitDD(orderInfo, MainActivity.this, 3);
        } else {
            String message = "支付金额：" + tvTotalPrice.getText().toString() + "元";
            int delayTimes = 1500;
            showLoading("0", message, delayTimes);
            handler.sendEmptyMessage(NOTIFY_CLEAR);
        }
        // 打印数据
        if (epsPrinter == null) {
            epsPrinter = new MyEPSPrinter(sysApplication.getEpsPrint());
        }
        epsPrinter.printOrder(sysApplication.getThreadPool(), orderInfo);

        //保存交易数据
        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < data.size(); i++) {
                    OrderBean goods = orderBeans.get(i);
                    goods.setOrderInfo(orderInfo);
                }
                orderInfoDao.insert(orderInfo);
                orderBeanDao.insert(orderBeans);
            }
        });


        askInfos.add(orderInfo);
        banner.notifyData(askInfos);


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
        }

    }

    @Override
    public void onResponse(final JSONObject jsonObject, final int flag) {
        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                switch (flag) {
                    case 3:
                        MyLog.myInfo("成功" + jsonObject.toString());
                        break;
                    case 4:
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

                    default:
                        break;
//                    case 6:
//                        sysApplication.getThreadPool().execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                TracenoResultBean tracenoResultBean = JSON.parseObject(jsonObject.toString(), TracenoResultBean.class);
//                                if (tracenoResultBean != null) {
//                                    if (tracenoResultBean.getStatus() == 0) {
//                                        if (tracenoResultBean.getData().size() > 0) {
//                                            boolean isUpdate = false;
//                                            for (int i = 0; i < tracenoResultBean.getData().size(); i++) {
//                                                TracenoResultBean.DataBean dataBean = tracenoResultBean.getData().get(i);
//                                                for (int j = 0; j < goodsList.size(); j++) {
//                                                    if (dataBean.getTypeid() == goodsList.get(j).getTypeid()) {
//                                                        goodsList.get(j).setBatchCode(dataBean.getTraceno());
//                                                        isUpdate = true;
//                                                    }
//                                                }
//                                            }
//                                            if (isUpdate) {
//                                                handler.sendEmptyMessage(NOTIFY_TRACENO);
//                                            }
//                                            //更新 数据中的批次号
//
//                                            boolean isUpdateType = false;
//                                            GoodsTypeDao goodsTypeDao = new GoodsTypeDao(context);
//                                            List<GoodsType> goodsTypes = goodsTypeDao.queryAll();
//                                            for (int i = 0; i < tracenoResultBean.getData().size(); i++) {
//                                                TracenoResultBean.DataBean dataBean = tracenoResultBean.getData().get(i);
//                                                for (int j = 0; j < goodsTypes.size(); j++) {
//                                                    if (dataBean.getTypeid() == goodsTypes.get(j).getId()) {
//                                                        goodsTypes.get(j).setTraceno(dataBean.getTraceno());
//                                                        isUpdateType = true;
//                                                    }
//                                                }
//                                            }
//
//                                            if (isUpdateType) {//批次号 有更新
//                                                goodsTypeDao.updates(goodsTypes);
//                                            }
//
//                                        }
//                                    }
//                                }
//
//                            }
//                        });
//                        break;
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
        }
    }

    //  返回支付状态
    @Override
    public void onResponse(String response, int flag) {
        switch (flag) {
            case 3:
                OrderResultBean resultInfo = JSON.parseObject(response, OrderResultBean.class);
                if (resultInfo != null && resultInfo.getStatus() == 0) {
                    String billstatus = resultInfo.getData().getBillstatus();
//                    String title = MyTextUtils.changeCharset(billstatus);
                    String message = "支付金额：" + tvTotalPrice.getText().toString() + "元";
                    int delayTimes = 1500;
                    showLoading(billstatus, message, delayTimes);
                    handler.sendEmptyMessage(NOTIFY_CLEAR);
                }

                break;
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
