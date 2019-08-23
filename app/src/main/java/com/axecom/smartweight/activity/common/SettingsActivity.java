package com.axecom.smartweight.activity.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.activity.common.mvvm.home.HomeActivity;
import com.axecom.smartweight.activity.datasummary.SummaryActivity;
import com.axecom.smartweight.activity.setting.GoodsSettingActivity;
import com.axecom.smartweight.activity.setting.HelpActivity;
import com.axecom.smartweight.activity.setting.LocalSettingActivity;
import com.axecom.smartweight.activity.setting.ServerTestActivity;
import com.axecom.smartweight.activity.setting.VideoActivity;
import com.axecom.smartweight.adapter.SettingsAdapter;
import com.axecom.smartweight.base.BaseDialog;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.entity.dao.OrderInfoDao;
import com.axecom.smartweight.entity.dao.UserInfoDao;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.entity.system.SettingsBean;
import com.axecom.smartweight.helper.HttpHelper;
import com.axecom.smartweight.helper.printer.MyBasePrinter;
import com.axecom.smartweight.mvvm.entity.ResultRtInfo;
import com.axecom.smartweight.mvvm.retrofit.RetrofitCallback;
import com.axecom.smartweight.mvvm.retrofit.HttpRtHelper;
import com.axecom.smartweight.rzl.utils.ApkUtils;
import com.bumptech.glide.Glide;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.base2.BuglyUPHelper;
import com.xuanyuan.library.help.ActivityController;
import com.xuanyuan.library.utils.LiveBus;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;
import com.xuanyuan.library.utils.system.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;

import static com.axecom.smartweight.config.IEventBus.NOTIFY_SMALLL_ROUTINE;
import static com.axecom.smartweight.config.IEventBus.NOTIFY_USERINFO;
import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;
import static com.xuanyuan.library.config.IConfig.PATCH_TIME;


/**
 * Created by Administrator on 2018-5-16.
 */
public class SettingsActivity extends AppCompatActivity implements IConstants, RetrofitCallback {
    public final String IS_RE_BOOT = "is_re_boot";
    private GridView settingsGV;
    private Context context;
    private SysApplication sysApplication;
    private UserInfoDao userInfoDao;
    private BaseDialog baseDialog;
    private int type;

    /**
     * 小程序二维码
     */
    private ImageView ivSmallRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActivityController.addActivity(this);
        sysApplication = (SysApplication) getApplication();
        context = this;
        type = getIntent().getIntExtra(STRING_TYPE, 0);
        userInfoDao = new UserInfoDao();
        baseDialog = new BaseDialog(context);

        setInitView();
        initView();
        LiveBus.observe(EVENT_BUS_COMMON, String.class, this, s -> {
            if (NOTIFY_SMALLL_ROUTINE.equals(s)) {
                updateSmallRoutine();
            }
        });
    }

//    //定义处理接收的方法
//    @SuppressLint("SetTextI18n")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(BaseBusEvent event) {
//        // 更新小程序二位码
//        if (NOTIFY_SMALLL_ROUTINE.equals(event.getEventType())) {
//            updateSmallRoutine();
//        }
//    }

    /**
     * 更新小程序图片
     */
    private void updateSmallRoutine() {
        String url2 = MyPreferenceUtils.getSp(context).getString(SMALLROUTINE_URL, null);
        if (url2 == null) {
            ivSmallRoutine.setVisibility(View.GONE);
        } else {
            ivSmallRoutine.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext()).load(url2).into(ivSmallRoutine);
        }
    }

    // 数据功能
    @SuppressLint("SetTextI18n")
    public void setInitView() {
        settingsGV = findViewById(R.id.settings_grid_view);
        //当前版本
        TextView tvSystemVersion = findViewById(R.id.tvDataUpdate_SystemVersion);
        tvSystemVersion.setText(ApkUtils.getVersionName(this) + "." + BuglyUPHelper.APP_CHANNEL + PATCH_TIME);
        ivSmallRoutine = findViewById(R.id.ivImageView123);
        updateSmallRoutine();
    }

    //设置菜单中的适配器
    private SettingsAdapter settingsAdapter;

    public void initView() {
        List<SettingsBean> settngsList = new ArrayList<>();
        SettingsBean settingsBean1 = new SettingsBean(R.mipmap.printer, "补打上笔", POSITION_PATCH, R.color.color_settings1);
        settngsList.add(settingsBean1);
        SettingsBean settingsBean2 = new SettingsBean(R.mipmap.settings2, "交易统计", POSITION_REPORTS, R.color.color_settings2);
        settngsList.add(settingsBean2);
        SettingsBean settingsBean6 = new SettingsBean(R.mipmap.settings3, "商品设置", POSITION_COMMODITY, R.color.color_settings3);
        settngsList.add(settingsBean6);
        SettingsBean settingsBean7 = new SettingsBean(R.mipmap.settings4, "数据更新", POSITION_UPDATE, R.color.color_settings4);
        settngsList.add(settingsBean7);

      /*  SettingsBean settingsBean16 = new SettingsBean(R.drawable.settings5, "热键更新", POSITION_HOT, R.color.color_settings1);
        settngsList.add(settingsBean16);*/
        SettingsBean settingsBean3 = new SettingsBean(R.mipmap.settings6, "服务器测试", POSITION_SERVER, R.color.color_settings5);
        settngsList.add(settingsBean3);

//      SettingsBean settingsBean8 = new SettingsBean(R.drawable.re_connecting, "一键重连", POSITION_RE_CONNECTING);
//      settngsList.add(settingsBean8);

        SettingsBean settingsBean9 = new SettingsBean(R.mipmap.settings7, "WIFI设置", POSITION_WIFI, R.color.color_settings7);
        settngsList.add(settingsBean9);
//      SettingsBean settingsBean10 = new SettingsBean(R.drawable.local_setting, "本机设置", POSITION_LOCAL);
//      settngsList.add(settingsBean10);

        SettingsBean settingsBean15 = new SettingsBean(R.mipmap.settings8, "本机设置", POSITION_LOCAL, R.color.color_settings8);
        settngsList.add(settingsBean15);

//        SettingsBean settingsBean4 = new SettingsBean(R.drawable.settings13, "异常订单", POSITION_INVALID, R.color.color_settings13);
//        settngsList.add(settingsBean4);

        if (type == 1) {
            SettingsBean settingsBean13 = new SettingsBean(R.mipmap.settings11, "系统设置", POSITION_SYSTEM, R.color.color_settings11);
            settngsList.add(settingsBean13);

//          SettingsBean settingsBean5 = new SettingsBean(R.drawable.settings14, "订单作废", POSITION_BD, R.color.color_settings14);
//          settngsList.add(settingsBean5);
//            SettingsBean settingsBean11 = new SettingsBean(R.drawable.settings12, "标定管理", POSITION_WEIGHT, R.color.color_settings12);
//            settngsList.add(settingsBean11);
        }
        SettingsBean settingsBean18 = new SettingsBean(R.mipmap.settings18, "清除数据", POSITION_DATA_DELETE, R.color.color_settings18);
        settngsList.add(settingsBean18);

        SettingsBean settingsBean12 = new SettingsBean(R.mipmap.settings10, "重启", POSITION_RE_BOOT, R.color.color_settings10);
        settngsList.add(settingsBean12);

        SettingsBean settingsBean17 = new SettingsBean(R.mipmap.settings17, "帮助", POSITION_HELP, R.color.color_settings17);
        settngsList.add(settingsBean17);
        SettingsBean settingsBean20 = new SettingsBean(R.mipmap.settings20, "操作视频", POSITION_VIDEO, R.color.color_settings20);
        settngsList.add(settingsBean20);

        SettingsBean settingsBean14 = new SettingsBean(R.mipmap.settings9, "返回", POSITION_BACK, R.color.color_settings9);
        settngsList.add(settingsBean14);
        SettingsBean settingsBean19 = new SettingsBean(R.mipmap.settings19, "关闭软件", POSITION_RE_EXIT, R.color.color_settings19);
        settngsList.add(settingsBean19);


        settingsAdapter = new SettingsAdapter(this, settngsList);
        settingsGV.setAdapter(settingsAdapter);
        settingsGV.setOnItemClickListener(settingsOnItemClickListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    /**
     * 进入数据更新Activity中
     */
    private void gotoDataFlush(UserInfo userInfo) {
        //判断商户名和 shellerid是否改变
        UserInfo appInfo = sysApplication.getUserInfo();
        if (MyNetWorkUtils.isNetworkAvailable(context)) {
            Intent intent = new Intent(context, DataFlushActivity.class);
            if (appInfo.getSellerid() == userInfo.getSellerid()) {
                intent.putExtra(INTENT_AUTO_UPDATE, 1);
            } else {
                MyPreferenceUtils.getSp(context).edit().putBoolean(SP_IS_FIRST_INIT, true).apply();
                intent.putExtra(INTENT_AUTO_UPDATE, 0);
            }
            startActivityForResult(intent, CODE_JUMP2_DATAFLUSH);
        } else {
            MyToast.showError(context, "请设置网络初始化用户信息");
        }
    }

    final AdapterView.OnItemClickListener settingsOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int flag = settingsAdapter.getItem(position).getFlag();
            Intent intent;
            switch (flag) {
                case POSITION_PATCH://补打上一笔
                    OrderInfoDao orderInfoDao = new OrderInfoDao();
                    List<OrderInfo> infoList = orderInfoDao.queryByDay(false, 1);
                    if (infoList.size() == 0) {
                        MyToast.toastShort(context, "暂无交易数据");
                    } else {
                        OrderInfo orderInfo = infoList.get(0);
//                        MyPrinter myPrinter = new MyPrinter(sysApplication.getPrint());
                        MyBasePrinter printer=sysApplication.getPrint();
                        if (printer!= null&&printer.isOpen()) {
                                sysApplication.getPrint().printOrder(sysApplication.getSingleThread(), orderInfo);
                                return;
                        }
                        MyToast.showError(context, "打印机异常或者接线异常");
                    }
                    break;
                case POSITION_REPORTS://数据汇总
                    startDDMActivity(SummaryActivity.class, false);
                    break;
                case POSITION_SERVER:
                    startDDMActivity(ServerTestActivity.class, false);
                    break;
                case POSITION_BD:
//                    startDDMActivity(CalibrationActivity.class, false);
                    break;
                case POSITION_HELP:
                    startDDMActivity(HelpActivity.class, false);
                    break;
                case POSITION_VIDEO:
                    startDDMActivity(VideoActivity.class, false);
                    break;
                case POSITION_WIFI:// wifi 管理
//                  startDDMActivity(WifiSettingsActivity.class, false);
                    intent = new Intent();
                    intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                    startActivity(intent);
                    break;
                case POSITION_COMMODITY:// 商品设置
                    startDDMActivity(GoodsSettingActivity.class, false);
                    break;
                case POSITION_LOCAL://本机操作 ，当地操作
                    Intent intent2 = new Intent(SettingsActivity.this, LocalSettingActivity.class);
                    startActivity(intent2);
                    break;
                case POSITION_SYSTEM:// 系统设置
                    startDDMActivity(SystemSettingsActivity.class, true);
                    break;
                case POSITION_RE_BOOT://重启
                    intent = new Intent();
                    intent.setClass(SettingsActivity.this.getApplicationContext(), HomeActivity.class);
                    intent.putExtra(IS_RE_BOOT, true);
                    ActivityController.finishAll(false);
                    startActivity(intent);
                    break;
                case POSITION_RE_EXIT:// 标定管理
                    ActivityController.finishAll(true);
                    break;
                case POSITION_WEIGHT:// 标定管理
                    break;
//                case POSITION_HOT:
//                    baseDialog.showLoading();
//                    UserInfo userInfo = sysApplication.getUserInfo();
//                    if (userInfo != null) {
//                        int tid = userInfo.getTid();
//                        HttpHelper.getmInstants(sysApplication).initHotGoodEx(SettingsActivity.this, tid, 8);//热键更新
//                    }
//                    break;
                case POSITION_BACK://返回
                    onBackPressed();
                    break;
                case POSITION_DATA_DELETE://清除数据
                    //TODO  待确定 要删除哪些数据
//                    Beta.checkUpgrade();//检查版本号
//                    loadUpgradeInfo();

                    break;
                case POSITION_RE_CONNECTING:
//                    baseDialog.showLoading();
//                    String wifiSSID = SPUtils.getString(SettingsActivity.this, WifiSettingsActivity.KEY_SSID_WIFI_SAVED, "");
//                    if (!TextUtils.isEmpty(wifiSSID)) {
//                        WifiConfiguration mWifiConfiguration;
//                        WifiConfiguration tempConfig = IsExsits(wifiSSID);
//                        if (tempConfig != null) {
//                            mWifiConfiguration = tempConfig;
//                            boolean b = wifiManager.enableNetwork(mWifiConfiguration.networkId, true);
//                            if (b) {
////                                showLoading("连接成功");
//                                baseDialog.closeLoading();
//                            }
//                        }
//                    }
                    break;
                case POSITION_UPDATE:// 数据更新
                    if (MyNetWorkUtils.isNetworkAvailable(getApplicationContext())) {
                        baseDialog.showLoading("数据更新", "用户信息更新");
                        HttpRtHelper.getmInstants().getUserInfoExByRetrofit(SystemInfoUtils.getMac(context), SettingsActivity.this, FLAG_GET_USER_INFO);
//                      HttpHelper.getmInstants(sysApplication).getUserInfoEx(SettingsActivity.this, FLAG_GET_USER_INFO);
                    } else {
                        MyToast.showError(context, "网络异常，请检查网络设置");
                    }
                    break;
            }
        }
    };

    /**
     * 跳转Activity的方法,传入我们需要的参数即可
     * 是否需要 开启动画
     */
    public <T> void startDDMActivity(Class<T> activity, boolean isAinmain) {
        Intent intent = new Intent(SettingsActivity.this, activity);
        startActivity(intent);
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }

    @Override
    public void onNext(ResultRtInfo resultInfo, int flag) {
        if (flag == FLAG_GET_USER_INFO) {
            boolean isSuccess = false;
            baseDialog.closeLoading();
            if (resultInfo != null && resultInfo.getStatus() == 0) {
                UserInfo userInfo = (UserInfo) resultInfo.getData();
                if (userInfo != null && HttpHelper.getmInstants(sysApplication).validateUserInfo(userInfo, sysApplication.getTidType())) {
                    //数据更新成功了
                    userInfo.setId(1);
                    userInfoDao.updateOrInsert(userInfo);
                    gotoDataFlush(userInfo);
                    // 必须放在下面
                    sysApplication.setUserInfo(userInfo);
                    LiveBus.post(EVENT_BUS_COMMON, String.class, NOTIFY_USERINFO);
                    isSuccess = true;
                }
            }
            if (!isSuccess) {
                MyToast.toastLong(context, "未获取到秤的配置信息");
            }
        }
    }

    @Override
    public void onError(Throwable e, int flag) {
        if (FLAG_GET_USER_INFO == flag) {
            baseDialog.closeLoading();
            MyToast.showError(context, "数据更新失败");
        }
    }

    /**
     * @param flag 旗标参数，设定数据。
     */
    @Override
    public void onComplete(int flag) {

    }
}
