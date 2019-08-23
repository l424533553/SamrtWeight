package com.axecom.smartweight.activity.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.activity.common.what.WhatActivity;
import com.axecom.smartweight.activity.setting.ErrorLogActivity;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.PowerConsumptionRankingsBatteryView;
import com.xuanyuan.library.base2.BuglyUPHelper;
import com.xuanyuan.library.help.ActivityController;
import com.xuanyuan.library.utils.FileUtils;
import com.xuanyuan.library.utils.MyTextUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.Beta;

import static com.axecom.smartweight.config.IEventBus.WEIGHT_ELECTRIC;
import static com.axecom.smartweight.config.IIntent.INTENT_EXTRA_TYPE;
import static com.axecom.smartweight.config.IIntent.INTENT_UP_DATA_TEMP_ACTIVITY;

public class SystemSettingsActivity extends Activity implements IConstants, View.OnClickListener {
    //    private CheckedTextView notClearPriceCtv, saveWeightCtv, autoObtainCtv, cashEttlementCtv;
//    private CheckedTextView icCardSettlementCtv, stopPrintCtv, noPatchSettlementCtv;
//    private CheckedTextView autoPrevCtv, stopCashCtv, stopAlipayCtv, stopweichatpayCtv;
    private EditText tvWebIP;
    private Context context;
    private SysApplication sysApplication;
    private TextView tvHardwareInfo;

    /**
     * 电池控件
     */
    private PowerConsumptionRankingsBatteryView pcrBattery;
    /**
     * 电池状态 ，电池数据，电池占比
     */
    private TextView pcrBtState, pcrBtData, pcrBtPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.system_settings_activity);
        sysApplication = (SysApplication) getApplication();
        context = this;
        initView();
        initBattery();
        EventBus.getDefault().register(this);//解除事件总线问题
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除事件总线问题
    }

    /**
     * 是否打开充电标志
     */
    private boolean isOpen;

    private void initBattery() {
        pcrBattery = findViewById(R.id.pcrBattery);

        pcrBtState = findViewById(R.id.pcrBtState);
        pcrBtData = findViewById(R.id.pcrBtData);
        pcrBtPercent = findViewById(R.id.pcrBtPercent);

        if (sysApplication.getTidType() <= 0) {
            pcrBattery.setVisibility(View.GONE);
        } else {
            pcrBattery.setVisibility(View.VISIBLE);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
//                mHandler.sendEmptyMessage(0);
                    if (isOpen) {
                        pcrBattery.chargeElectric(8);
                    }
                }
            }, 0, 200);
        }
    }

    public void initView() {
        String baseWebIP = MyPreferenceUtils.getSp(context).getString(BASE_WEB_IP, BASE_IP_ST);
        tvWebIP = findViewById(R.id.tvWebIP);
        tvWebIP.setHint(baseWebIP);
        tvWebIP.setText(baseWebIP);
        tvHardwareInfo = findViewById(R.id.tvHardwareInfo);


        findViewById(R.id.btnSaveIP).setOnClickListener(this);
        findViewById(R.id.btnCleanLog).setOnClickListener(this);
        findViewById(R.id.btnHardwareInfo).setOnClickListener(this);
        findViewById(R.id.btnStartBD).setOnClickListener(this);
        findViewById(R.id.btnTempUpDate).setOnClickListener(this);


        findViewById(R.id.btnDeleteAdImage).setOnClickListener(this);
        findViewById(R.id.btnApiTest).setOnClickListener(this);
        findViewById(R.id.btnRemoveSign).setOnClickListener(this);
        findViewById(R.id.btnCleanPatch).setOnClickListener(this);


//        Button cashRoundingCtv = findViewById(R.id.btnLookLog);
//        stopCashCtv = findViewById(R.id.system_settings_stop_cash_ctv);
//        stopAlipayCtv = findViewById(R.id.system_settings_stop_alipay_ctv);
//        stopweichatpayCtv = findViewById(R.id.system_settings_stop_weichatpay_ctv);
//
//        notClearPriceCtv.setOnClickListener(this);
//        saveWeightCtv.setOnClickListener(this);
//        autoObtainCtv.setOnClickListener(this);
//        cashEttlementCtv.setOnClickListener(this);
//        stopPrintCtv.setOnClickListener(this);
//        noPatchSettlementCtv.setOnClickListener(this);
//        autoPrevCtv.setOnClickListener(this);
//        cashRoundingCtv.setOnClickListener(this);
//        stopCashCtv.setOnClickListener(this);
//        stopAlipayCtv.setOnClickListener(this);
//        stopweichatpayCtv.setOnClickListener(this);
//        icCardSettlementCtv.setOnClickListener(this);


        initSwitch();
    }


    /**
     * 初始化开关设置
     */
    private void initSwitch() {
        Switch stPrintQR = findViewById(R.id.stPrintQR);
        boolean isNoQR = MyPreferenceUtils.getBoolean(context, IS_NO_PRINT_QR, false);
        stPrintQR.setChecked(!isNoQR);
//        stPrintQR.set
        stPrintQR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyPreferenceUtils.getSp(context).edit().putBoolean(IS_NO_PRINT_QR, !isChecked).apply();
                sysApplication.getPrint().isNoPrinterQR = !isChecked;
            }
        });
    }

    public void doBack(View view) {
        onBackPressed();
    }

    private String kValue;

    //定义处理接收的方法
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(BaseBusEvent event) {
        if (WEIGHT_ELECTRIC.equals(event.getEventType())) {
            String[] array = event.getOther().toString().split(" ");
            if (array.length >= 3 && array[1].length() == 4 && array[2].length() == 2) {
                String electricData = array[1];

                int electric = (int) (((Float.valueOf(electricData) / 100) - PowerConsumptionRankingsBatteryView.MinEleve) * PowerConsumptionRankingsBatteryView.ConstantEleve);
                if (electric < 0) {
                    electric = 0;
                } else if (electric > 100) {
                    electric = 100;
                }

                int electricState = array[2].charAt(0) - 48;
                if (electricState == 0) {//无充电
                    isOpen = false;
                    pcrBattery.setLevelHeight(electric);
                    pcrBtState.setText("未充电");
                    if (electric <= 20) {
                        MyToast.showError(context, "电量不足,请及时接通电源！");
                    }
                } else if (electricState == 1) {
                    isOpen = true;
                    pcrBtState.setText("充电中");
                }
                pcrBtData.setText(electricData);
                pcrBtPercent.setText(electric + "");
            }
        }
    }


    /**
     * 是否得到了Ad值
     */
    private boolean isGetAd;

    private boolean isSendDataFpsm;

    /**
     * 上下文。使用上下文
     */
    DecimalFormat decimalFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveIP://保存IP
                String baseWebIP = tvWebIP.getText().toString();
                tvWebIP.setHint(baseWebIP);
                boolean isWebSave = MyPreferenceUtils.getSp(context).edit().putString(BASE_WEB_IP, baseWebIP).commit();
                if (isWebSave) {
                    MyToast.toastShort(SystemSettingsActivity.this, "IP保存成功");
                } else {
                    MyToast.toastShort(SystemSettingsActivity.this, "IP保存失败");
                }
                break;
//            case R.id.system_settings_not_clear_price_ctv:
//                notClearPriceCtv.setChecked(!notClearPriceCtv.isChecked());
//                break;
            case R.id.btnApiTest://API调试测试
                Intent intent2 = new Intent(SystemSettingsActivity.this, ApiTestActivity.class);
                startActivity(intent2);
                break;
            case R.id.btnTempUpDate://API调试测试
                Intent intent3 = new Intent(SystemSettingsActivity.this, WhatActivity.class);
                intent3.putExtra(INTENT_EXTRA_TYPE, INTENT_UP_DATA_TEMP_ACTIVITY);
                startActivity(intent3);
                break;
            case R.id.btnCleanPatch:
                BuglyUPHelper buglyUPHelper = new BuglyUPHelper(this);
                buglyUPHelper.cleanTinkerPatch();
                break;
//            case R.id.system_settings_save_weight_ctv:
//                saveWeightCtv.setChecked(!saveWeightCtv.isChecked());
//                break;
//            case R.id.system_settings_auto_obtain_ctv:
//                autoObtainCtv.setChecked(!autoObtainCtv.isChecked());
//                break;
//            case R.id.system_settings_cash_ettlement_ctv:
//                cashEttlementCtv.setChecked(!cashEttlementCtv.isChecked());
//                break;
//            case R.id.system_settings_ic_card_settlement_ctv:
//                icCardSettlementCtv.setChecked(!icCardSettlementCtv.isChecked());
//                break;
//            case R.id.system_settings_stop_print_ctv:
//                stopPrintCtv.setChecked(!stopPrintCtv.isChecked());
//                break;
//            case R.id.system_settings_no_patch_settlement_ctv:
//                noPatchSettlementCtv.setChecked(!noPatchSettlementCtv.isChecked());
//                break;
//            case R.id.system_settings_auto_prev_ctv:
//                autoPrevCtv.setChecked(!autoPrevCtv.isChecked());
//                break;
            case R.id.btnLookLog:// 查看错误日志信息
                Intent intent = new Intent(SystemSettingsActivity.this, ErrorLogActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCleanLog:// 查看错误日志信息
                //清空缓存目录
                String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/log/error" + ".txt";
                MyTextUtils.clearInfoForFile(fileName);
                break;
//            case R.id.system_settings_stop_cash_ctv:
//                stopCashCtv.setChecked(!stopCashCtv.isChecked());
//                break;
//            case R.id.system_settings_stop_alipay_ctv:
//                stopAlipayCtv.setChecked(!stopAlipayCtv.isChecked());
//                break;
//            case R.id.system_settings_stop_weichatpay_ctv:
//                stopweichatpayCtv.setChecked(!stopweichatpayCtv.isChecked());
//                break;


//            case R.id.btnZeroSetting://置零准备
//                sysApplication.getMyBaseWeighter().sendZer();
//                btnCalibration.setBackgroundResource(R.drawable.selector_btn_epay);
//                btnCalibration.setClickable(true);
//                btnCalibration.setEnabled(true);
//
//                break;
            case R.id.btnRemoveSign://移除拆机标识
                sysApplication.getMyBaseWeighter().sendRemoveSign();
                MyToast.toastShort(context, "设定成功");
                break;

            case R.id.btnStartBD:// 准备标定秤
                Intent intent1 = new Intent(context, ScaleBDACActivity.class);
                startActivity(intent1);
                break;
//            case R.id.btnCalibration://10kg标定
//                isSendDataFpsm = false;
//                sysApplication.getMyBaseWeighter().sendCalibrationData(isCalibration10kg);
//                btnCalibration.setBackgroundResource(R.color.light_green);
//                btnCalibration.setClickable(false);
//                btnCalibration.setEnabled(false);
//                kValue = null;
//                break;
            case R.id.btnDeleteAdImage:// 删除广告图片
                String dir = FileUtils.getDownloadDir(this, FileUtils.DOWNLOAD_DIR);
                boolean isDeleteSuccess = FileUtils.deleteDir(dir);
                if (isDeleteSuccess) {
                    MyToast.toastShort(context, "删除成功");
                } else {
                    MyToast.toastShort(context, "删除失败");
                }
                break;
            case R.id.btnHardwareInfo:// 显示硬件信息
                checkPhoneType();
                break;
            case R.id.btnGetK://  获取K值信息
                sysApplication.getMyBaseWeighter().getKValue();
                break;
            case R.id.btnsbSend://  获取K值信息
                sysApplication.getMyBaseWeighter().getKValue();
                String sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
                String sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
                String kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, VALUE_K_DEFAULT);
//                if (sbAd != null && sbZeroAd != null) {
//                    MyLog.blue("传输的 标定Ad= " + sbAd + "   传输的标定0位Ad=" + sbZeroAd);
//                    sendCalibrationInfo(isCalibration10kg, sbZeroAd, sbAd, kValue);
//                }
                break;
            default:
                break;
        }
    }


    /**
     * 检查手机类型
     */
    public void checkPhoneType() {
        // 是否是ｒｏｍ为２．３以上的三星非Ｇｏｏｇｌｅ定制手机
        String manufacturer = Build.MANUFACTURER;
        int sdkVersion = Build.VERSION.SDK_INT;
        String model = Build.MODEL;
        String displayStr = Build.DISPLAY;
        String brand = Build.BRAND;

        String phoneInfo = "Product: " + android.os.Build.PRODUCT;
        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
        phoneInfo += ", TAGS: " + android.os.Build.TAGS;
        phoneInfo += ", VERSION_CODES.BASE: "
                + android.os.Build.VERSION_CODES.BASE;
        phoneInfo += ", MODEL: " + android.os.Build.MODEL;
        phoneInfo += ", SDK: " + Build.VERSION.SDK_INT;
        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
        phoneInfo += ", BRAND: " + android.os.Build.BRAND;
        phoneInfo += ", BOARD: " + android.os.Build.BOARD;
        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
        phoneInfo += ", ID: " + android.os.Build.ID;
        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
        phoneInfo += ", USER: " + android.os.Build.USER;

        MyLog.bluelog("硬件信息" + phoneInfo);
        tvHardwareInfo.setText(phoneInfo);
    }

    // TODO 硬件信息


}