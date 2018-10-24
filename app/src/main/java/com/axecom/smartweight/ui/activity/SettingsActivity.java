package com.axecom.smartweight.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BaseDialog;
import com.axecom.smartweight.base.BusEvent;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.bean.SettingsBean;
import com.axecom.smartweight.manager.AccountManager;
import com.axecom.smartweight.my.entity.AllGoods;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.GoodsType;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.dao.AllGoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsTypeDao;
import com.axecom.smartweight.my.entity.dao.UserInfoDao;
import com.axecom.smartweight.my.net.NetHelper;
import com.axecom.smartweight.my.rzl.utils.ApkUtils;
import com.axecom.smartweight.ui.activity.datasummary.SummaryActivity;
import com.axecom.smartweight.ui.activity.setting.GoodsSettingActivity;
import com.axecom.smartweight.utils.SPUtils;
import com.luofx.listener.VolleyListener;
import com.luofx.utils.PreferenceUtils;
import com.luofx.utils.common.MyToast;
import com.shangtongyin.tools.serialport.IConstants_ST;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-5-16.
 */

public class SettingsActivity extends Activity implements VolleyListener, IConstants_ST {

    public final String ACTION_NET_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public final String IS_RE_BOOT = "is_re_boot";

    private final int POSITION_PATCH = 1;
    private final int POSITION_REPORTS = 2;
    private final int POSITION_SERVER = 3;
    private final int POSITION_INVALID = 4;
    private final int POSITION_ABNORMAL = 5;

    private final int POSITION_COMMODITY = 6;
    private final int POSITION_UPDATE = 7;
    private final int POSITION_RE_CONNECTING = 8;
    private final int POSITION_WIFI = 9;
    private final int POSITION_LOCAL = 10;

    private final int POSITION_WEIGHT = 11;
    private final int POSITION_RE_BOOT = 12;
    private final int POSITION_BD = 13;
    private final int POSITION_SYSTEM = 14;


    private GridView settingsGV;

    private WifiManager wifiManager;


    private Context context;
    private SysApplication sysApplication;
    private GoodsDao goodsDao;
    private GoodsTypeDao goodsTypeDao;
    private AllGoodsDao allGoodsDao;
    private UserInfoDao userInfoDao;
    private NetHelper netHelper;
    private BaseDialog baseDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        sysApplication = (SysApplication) getApplication();

        context = this;
        userInfoDao = new UserInfoDao(context);
        goodsDao = new GoodsDao(context);
        goodsTypeDao = new GoodsTypeDao(context);
        allGoodsDao = new AllGoodsDao(context);
        baseDialog = new BaseDialog(context);

        netHelper = new NetHelper(sysApplication, SettingsActivity.this);

        initHandler();
        setInitView();
        initView();
    }

    private Handler handler;

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case NOTIFY_INITDAT:
                        final int tid = msg.arg1;
                        sysApplication.getThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                netHelper.initGoods(tid);
                                netHelper.initGoodsType();
                                netHelper.initAllGoods();
                            }
                        });

                        break;
                    case NOTIFY_SUCCESS:// 更新数据成功
                        if (successFlag == 3)
                            baseDialog.closeLoading();
                        break;
                }
                return false;
            }
        });

    }


    public void setInitView() {
        settingsGV = findViewById(R.id.settings_grid_view);

        //当前版本
        TextView tvSystemVersion = findViewById(R.id.tvDataUpdate_SystemVersion);
        tvSystemVersion.setText("当前版本号:" + ApkUtils.getVersionName(this));

        context = this;


    }


    private SettingsAdapter settingsAdapter;


    public void initView() {
        List<SettingsBean> settngsList = new ArrayList<>();

        SettingsBean settingsBean1 = new SettingsBean(R.drawable.switching_setting, "补打上笔", POSITION_PATCH);
        settngsList.add(settingsBean1);
        SettingsBean settingsBean2 = new SettingsBean(R.drawable.patch_setting, "数据汇总", POSITION_REPORTS);
        settngsList.add(settingsBean2);
        SettingsBean settingsBean3 = new SettingsBean(R.drawable.server_setting, "服务器测试", POSITION_SERVER);
        settngsList.add(settingsBean3);
        SettingsBean settingsBean4 = new SettingsBean(R.drawable.invalid, "异常订单", POSITION_INVALID);
        settngsList.add(settingsBean4);

        SettingsBean settingsBean5 = new SettingsBean(R.drawable.bd_setting, "订单作废", POSITION_BD);
        settngsList.add(settingsBean5);
        SettingsBean settingsBean6 = new SettingsBean(R.drawable.commodity_setting, "商品设置", POSITION_COMMODITY);
        settngsList.add(settingsBean6);
        SettingsBean settingsBean7 = new SettingsBean(R.drawable.update_setting, "数据更新", POSITION_UPDATE);
        settngsList.add(settingsBean7);

        SettingsBean settingsBean8 = new SettingsBean(R.drawable.re_connecting, "一键重连", POSITION_RE_CONNECTING);
        settngsList.add(settingsBean8);
        SettingsBean settingsBean9 = new SettingsBean(R.drawable.wifi_setting, "WIFI设置", POSITION_WIFI);
        settngsList.add(settingsBean9);
        SettingsBean settingsBean10 = new SettingsBean(R.drawable.local_setting, "本机设置", POSITION_LOCAL);
        settngsList.add(settingsBean10);
        SettingsBean settingsBean11 = new SettingsBean(R.drawable.weight_setting, "标定管理", POSITION_WEIGHT);
        settngsList.add(settingsBean11);
        SettingsBean settingsBean12 = new SettingsBean(R.drawable.re_boot, "重启", POSITION_RE_BOOT);
        settngsList.add(settingsBean12);
        SettingsBean settingsBean13 = new SettingsBean(R.drawable.system_setting, "系统设置", POSITION_SYSTEM);
        settngsList.add(settingsBean13);


        int userType = AccountManager.getInstance().getUserType();
        if (userType == 1 || userType == 2) {
            settngsList.remove(15);
            settngsList.remove(14);
            settngsList.remove(13);
        }
        settingsAdapter = new SettingsAdapter(this, settngsList);
        settingsGV.setAdapter(settingsAdapter);
        settingsGV.setOnItemClickListener(settingsOnItemClickListener);


    }


    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        MyToast.toastShort(context, "网络请求失败");

        switch (flag) {
            case 1:
                break;
            case 2:

                MyToast.toastShort(context, "初始化数据不完全");
                break;

            default:
                break;
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        try {
            final ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
            switch (flag) {
                case 1:
                    if (resultInfo != null) {
                        if (resultInfo.getStatus() == 0) {
                            UserInfo userInfo = JSON.parseObject(resultInfo.getData(), UserInfo.class);
                            if (userInfo != null) {
                                userInfo.setId(1);
                                boolean isSuccess = userInfoDao.updateOrInsert(userInfo);

                                SharedPreferences sp = PreferenceUtils.getSp(context);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt(MARKET_ID, userInfo.getMarketid());
                                editor.putString(MARKET_NAME, userInfo.getMarketname());
                                editor.putInt(TID, userInfo.getTid());
                                editor.putInt(SELLER_ID, userInfo.getSellerid());
                                editor.putString(SELLER, userInfo.getSeller());
                                editor.putString(KEY, userInfo.getKey());
                                editor.putString(MCHID, userInfo.getMchid());
                                editor.apply();
                                Message message = handler.obtainMessage();
                                message.arg1 = userInfo.getTid();
                                message.what = NOTIFY_INITDAT;
                                handler.sendMessage(message);
                            }
                        } else {
                            MyToast.toastLong(context, "未获取到秤的配置信息");
                        }

                    } else {
                        MyToast.toastLong(context, "未获取到秤的配置信息");
                    }

                    break;
                case 2:
                    if (resultInfo != null) {
                        sysApplication.getThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (resultInfo.getStatus() == 0) {
                                    List<Goods> goodsList = JSON.parseArray(resultInfo.getData(), Goods.class);
                                    if (goodsList != null && goodsList.size() > 0) {
                                        goodsDao.insert(goodsList);
                                    }

                                }
                            }
                        });
                        successFlag++;
                        handler.sendEmptyMessage(NOTIFY_SUCCESS);

                    }


//                jumpActivity();
                    break;
                case 3:
                    if (resultInfo != null) {
                        if (resultInfo.getStatus() == 0) {
                            List<GoodsType> goodsList = JSON.parseArray(resultInfo.getData(), GoodsType.class);
                            if (goodsList != null && goodsList.size() > 0) {

                                goodsTypeDao.insert(goodsList);
                            }

                        }
                        successFlag++;
                        handler.sendEmptyMessage(NOTIFY_SUCCESS);
                    }


//                jumpActivity();
                    break;
                case 4:
                    if (resultInfo != null) {
                        if (resultInfo.getStatus() == 0) {
                            List<AllGoods> goodsList = JSON.parseArray(resultInfo.getData(), AllGoods.class);
                            if (goodsList != null && goodsList.size() > 0) {

                                allGoodsDao.insert(goodsList);
                            }

                        }
                        successFlag++;
                        handler.sendEmptyMessage(NOTIFY_SUCCESS);
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private int successFlag = 0;

    AdapterView.OnItemClickListener settingsOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int flag = settingsAdapter.getItem(position).getFlag();
            switch (flag) {
                case POSITION_PATCH:
//                    EventBus.getDefault().post(new BusEvent(BusEvent.POSITION_PATCH, SPUtils.getString(SettingsActivity.this, "print_orderno", ""), SPUtils.getString(SettingsActivity.this, "print_payid", "")));
//                    finish();
                    break;
                case POSITION_REPORTS://数据汇总
                    startDDMActivity(SummaryActivity.class, false);
                    break;
                case POSITION_INVALID:
                    startDDMActivity(OrderInvalidActivity.class, false);
                    break;
                case POSITION_ABNORMAL:
                    startDDMActivity(AbnormalOrderActivity.class, false);
                    break;
                case POSITION_SERVER:
                    startDDMActivity(ServerTestActivity.class, false);
                    break;
                case POSITION_BD:
                    startDDMActivity(CalibrationActivity.class, false);
                    break;
                case POSITION_WIFI:
                    startDDMActivity(WifiSettingsActivity.class, false);
                    break;
                case POSITION_COMMODITY:// 商品设置
                    startDDMActivity(GoodsSettingActivity.class, false);
                    break;
                case POSITION_LOCAL:
                    startDDMActivity(LocalSettingsActivity.class, false);
                    break;
                case POSITION_SYSTEM:
                    startDDMActivity(SystemSettingsActivity2.class, false);
                    break;
                case POSITION_RE_BOOT:
                    EventBus.getDefault().post(new BusEvent(BusEvent.GO_HOME_PAGE, true));
                    Intent intent = new Intent();
                    intent.setClass(SettingsActivity.this, HomeActivity.class);
                    intent.putExtra(IS_RE_BOOT, true);
                    startActivity(intent);
                    break;
                case POSITION_WEIGHT:
                    finish();
                    break;
//                case POSITION_SWITCH:
//                    showLoading("切换成功");
//                    boolean switchSimpleOrComplex = (boolean) SPUtils.get(SettingsActivity.this, KET_SWITCH_SIMPLE_OR_COMPLEX, false);
//                    SPUtils.put(SettingsActivity.this, KET_SWITCH_SIMPLE_OR_COMPLEX, !switchSimpleOrComplex);
//                    break;
                case POSITION_RE_CONNECTING:
                    baseDialog.showLoading();
                    String wifiSSID = SPUtils.getString(SettingsActivity.this, WifiSettingsActivity.KEY_SSID_WIFI_SAVED, "");
                    if (!TextUtils.isEmpty(wifiSSID)) {
                        WifiConfiguration mWifiConfiguration;
                        WifiConfiguration tempConfig = IsExsits(wifiSSID);
                        if (tempConfig != null) {
                            mWifiConfiguration = tempConfig;
                            boolean b = wifiManager.enableNetwork(mWifiConfiguration.networkId, true);
                            if (b) {
//                                showLoading("连接成功");
                                baseDialog.closeLoading();
                            }
                        }
                    }
                    break;
                case POSITION_UPDATE:
                    baseDialog.showLoading();

                    netHelper.getUserInfo(netHelper.getIMEI(context), 1);


//                    SystemSettingManager.updateData(SettingsActivity.this);
//                    updateScalesId();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            baseDialog.closeLoading();
                        }
                    }, 2000);
                    break;
//                case POSITION_BLUETOOTH:
//                    BTHelperDialog.Builder builder = new BTHelperDialog.Builder(SettingsActivity.this);
//                    builder.create(new BTHelperDialog.OnBtnClickListener() {
//
//                        @Override
//                        public void onConfirmed(BtHelperClient.STATUS mCurrStatus, String deviceAddress) {
//                            if (mCurrStatus == BtHelperClient.STATUS.CONNECTED) {
////                                showLoading("连接成功");
//                                SPUtils.putString(SettingsActivity.this, BTHelperDialog.KEY_BT_ADDRESS, deviceAddress);
//                                EventBus.getDefault().post(new BusEvent(BusEvent.BLUETOOTH_CONNECTED, true));
//                            }
//                        }
//
//                        @Override
//                        public void onCanceled(String result) {
//
//                        }
//                    }).show();
//                    break;
            }
        }
    };

//    private void updateScalesId() {
//        RetrofitFactory.getInstance().API()
//                .getScalesIdByMac(MacManager.getInstace(this).getMac())
//                .compose(this.<BaseEntity<WeightBean>>setThread())
//                .subscribe(new Observer<BaseEntity<WeightBean>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(final BaseEntity<WeightBean> baseEntity) {
//                        if (baseEntity.isSuccess()) {
//                            AccountManager.getInstance().saveScalesId(baseEntity.getData().getId() + "");
//                        } else {
//                            showLoading(baseEntity.getMsg());
//                        }
//                    }
//
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        closeLoading();
//                    }
//                });
//    }

//    private BroadcastReceiver netWorkReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (ACTION_NET_CHANGE.equals(intent.getAction())) {
//                closeLoading();
//                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo networkInfo = null;
//                if (connectivityManager != null) {
//                    networkInfo = connectivityManager.getActiveNetworkInfo();
//                }
//                if (networkInfo != null && networkInfo.isAvailable()) {
//                    Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    };

    public WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 跳转Activity的方法,传入我们需要的参数即可
     */
    public <T> void startDDMActivity(Class<T> activity, boolean isAinmain) {
        Intent intent = new Intent(SettingsActivity.this, activity);
        startActivity(intent);
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }

    class SettingsAdapter extends BaseAdapter {

        private Context context;
        private List<SettingsBean> settingList;

        public SettingsAdapter(Context context, List<SettingsBean> settingList) {
            this.context = context;
            this.settingList = settingList;
        }

        @Override
        public int getCount() {
            return settingList.size();
        }

        @Override
        public SettingsBean getItem(int position) {
            return settingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.settings_item, null);
                holder = new ViewHolder();
                holder.iconIv = convertView.findViewById(R.id.settings_item_icon_iv);
                holder.titleTv = convertView.findViewById(R.id.settings_item_title_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SettingsBean item = settingList.get(position);
            holder.iconIv.setImageDrawable(SettingsActivity.this.getResources().getDrawable(item.getIcon()));
            holder.titleTv.setText(item.getTitle());
            return convertView;
        }

        class ViewHolder {
            ImageView iconIv;
            TextView titleTv;
        }
    }
}
