package com.axecom.smartweight.my.activity.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.axecom.smartweight.my.helper.HttpHelper;
import com.axecom.smartweight.ui.activity.setting.ErrorLogActivity;
import com.axecom.smartweight.utils.AESHelper;
import com.luofx.listener.VolleyListener;
import com.luofx.newclass.ActivityController;
import com.luofx.newclass.weighter.XSWeighter15;
import com.luofx.utils.MyPreferenceUtils;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.xinyu.MyToast;
import com.luofx.utils.file.FileUtils;
import com.luofx.utils.text.MyTextUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class SystemSettingsActivity extends Activity implements IConstants, View.OnClickListener, VolleyListener {
    private CheckedTextView notClearPriceCtv, saveWeightCtv, autoObtainCtv, cashEttlementCtv;
    private CheckedTextView icCardSettlementCtv, stopPrintCtv, noPatchSettlementCtv;
    private CheckedTextView autoPrevCtv, stopCashCtv, stopAlipayCtv, stopweichatpayCtv;
    private EditText tvWebIP;
    private Context context;
    private SysApplication sysApplication;
    private Button btnCalibration;//标定按钮
    private Button btnZeroSetting;//置零按钮

    private TextView tvHardwareInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.system_settings_activity);
        sysApplication = (SysApplication) getApplication();
        context = this;
        initView();
        EventBus.getDefault().register(this);//解除事件总线问题
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除事件总线问题
    }

    private boolean isCalibration10kg;//是不是10kg的砝码标定

    public void initView() {
        String baseWebIP = MyPreferenceUtils.getSp(context).getString(BASE_WEB_IP, BASE_IP_ST);
        tvWebIP = findViewById(R.id.tvWebIP);
        tvWebIP.setHint(baseWebIP);
        tvWebIP.setText(baseWebIP);
        tvHardwareInfo = findViewById(R.id.tvHardwareInfo);

        findViewById(R.id.btnSaveIP).setOnClickListener(this);
        findViewById(R.id.btnCleanLog).setOnClickListener(this);
        findViewById(R.id.btnHardwareInfo).setOnClickListener(this);

        //标定数据
        RadioGroup rgWeight = findViewById(R.id.rgWeight);
        RadioButton rbWeight20 = findViewById(R.id.rbWeight20);
        rbWeight20.setChecked(true);
        rgWeight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rbWeight10:
                        isCalibration10kg = true;
                        break;
                    case R.id.rbWeight20:
                        isCalibration10kg = false;
                        break;
                    default:
                        isCalibration10kg = false;
                        break;
                }
            }
        });

        btnZeroSetting = findViewById(R.id.btnZeroSetting);
        btnZeroSetting.setOnClickListener(this);
        btnCalibration = findViewById(R.id.btnCalibration);
        btnCalibration.setOnClickListener(this);
        btnCalibration.setClickable(false);
        btnCalibration.setEnabled(false);

        findViewById(R.id.btnDeleteAdImage).setOnClickListener(this);
        findViewById(R.id.btnApiTest).setOnClickListener(this);
        findViewById(R.id.btnRemoveSign).setOnClickListener(this);

        notClearPriceCtv = findViewById(R.id.system_settings_not_clear_price_ctv);
        saveWeightCtv = findViewById(R.id.system_settings_save_weight_ctv);
        autoObtainCtv = findViewById(R.id.system_settings_auto_obtain_ctv);
        cashEttlementCtv = findViewById(R.id.system_settings_cash_ettlement_ctv);

        icCardSettlementCtv = findViewById(R.id.system_settings_ic_card_settlement_ctv);
        stopPrintCtv = findViewById(R.id.system_settings_stop_print_ctv);
        noPatchSettlementCtv = findViewById(R.id.system_settings_no_patch_settlement_ctv);
        autoPrevCtv = findViewById(R.id.system_settings_auto_prev_ctv);
        Button cashRoundingCtv = findViewById(R.id.btnLookLog);
        stopCashCtv = findViewById(R.id.system_settings_stop_cash_ctv);
        stopAlipayCtv = findViewById(R.id.system_settings_stop_alipay_ctv);
        stopweichatpayCtv = findViewById(R.id.system_settings_stop_weichatpay_ctv);

        notClearPriceCtv.setOnClickListener(this);
        saveWeightCtv.setOnClickListener(this);
        autoObtainCtv.setOnClickListener(this);
        cashEttlementCtv.setOnClickListener(this);
        stopPrintCtv.setOnClickListener(this);
        noPatchSettlementCtv.setOnClickListener(this);
        autoPrevCtv.setOnClickListener(this);
        cashRoundingCtv.setOnClickListener(this);
        stopCashCtv.setOnClickListener(this);
        stopAlipayCtv.setOnClickListener(this);
        stopweichatpayCtv.setOnClickListener(this);
        icCardSettlementCtv.setOnClickListener(this);
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
                sysApplication.getPrint().setNoQR(!isChecked);
            }
        });
    }

    public void doBack(View view) {
        onBackPressed();
    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(BaseBusEvent event) {
        switch (event.getEventType()) {
            case BaseBusEvent.TYPE_GET_K_VALUE:
                try {
                    String data = (String) event.getOther();
                    String array[] = data.split(" ");
                    if (array.length >= 3) {
                        if (array[0].length() == 7 && array[1].length() == 7 && array[2].length() == 9) {
//                            String kValue = array[2];
                            String initAd = array[0];
                            String initAd0 = array[1];
                            if (!isBDFinish) {
                                isBDFinish = true;//标定完成
                                sendCalibrationInfo(isCalibration10kg, initAd0, initAd);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    //是否标定完成
    private boolean isBDFinish;

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
            case R.id.system_settings_not_clear_price_ctv:
                notClearPriceCtv.setChecked(!notClearPriceCtv.isChecked());
                break;
            case R.id.btnApiTest://API调试测试
                Intent intent2 = new Intent(SystemSettingsActivity.this, ApiTestActivity.class);
                startActivity(intent2);

                break;
            case R.id.system_settings_save_weight_ctv:
                saveWeightCtv.setChecked(!saveWeightCtv.isChecked());
                break;
            case R.id.system_settings_auto_obtain_ctv:
                autoObtainCtv.setChecked(!autoObtainCtv.isChecked());
                break;
            case R.id.system_settings_cash_ettlement_ctv:
                cashEttlementCtv.setChecked(!cashEttlementCtv.isChecked());
                break;
            case R.id.system_settings_ic_card_settlement_ctv:
                icCardSettlementCtv.setChecked(!icCardSettlementCtv.isChecked());
                break;
            case R.id.system_settings_stop_print_ctv:
                stopPrintCtv.setChecked(!stopPrintCtv.isChecked());
                break;
            case R.id.system_settings_no_patch_settlement_ctv:
                noPatchSettlementCtv.setChecked(!noPatchSettlementCtv.isChecked());
                break;
            case R.id.system_settings_auto_prev_ctv:
                autoPrevCtv.setChecked(!autoPrevCtv.isChecked());
                break;
            case R.id.btnLookLog:// 查看错误日志信息
                Intent intent = new Intent(SystemSettingsActivity.this, ErrorLogActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCleanLog:// 查看错误日志信息
                //清空缓存目录
                String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/log/error" + ".txt";
                MyTextUtils.clearInfoForFile(fileName);
                break;
            case R.id.system_settings_stop_cash_ctv:
                stopCashCtv.setChecked(!stopCashCtv.isChecked());
                break;
            case R.id.system_settings_stop_alipay_ctv:
                stopAlipayCtv.setChecked(!stopAlipayCtv.isChecked());
                break;
            case R.id.system_settings_stop_weichatpay_ctv:
                stopweichatpayCtv.setChecked(!stopweichatpayCtv.isChecked());
                break;
            case R.id.btnZeroSetting://置零准备
                XSWeighter15 xsWeighter15 = XSWeighter15.getXSWeighter();
                xsWeighter15.sendZer();
                btnZeroSetting.setBackgroundResource(R.color.light_green);
                btnCalibration.setBackgroundResource(R.drawable.selector_btn_epay);
                btnCalibration.setClickable(true);
                btnCalibration.setEnabled(true);
                break;
            case R.id.btnRemoveSign://移除拆机标识
                xsWeighter15 = XSWeighter15.getXSWeighter();
                xsWeighter15.sendRemoveSign();
                MyToast.toastShort(context, "设定成功");
                break;
            case R.id.btnCalibration://10kg标定
                isBDFinish = false;
                xsWeighter15 = XSWeighter15.getXSWeighter();
                xsWeighter15.sendCalibrationData(isCalibration10kg);
                MyToast.toastShort(context, "标定成功");
                btnCalibration.setBackgroundResource(R.color.light_green);

                //发送获取K值  的命令
//                xsWeighter15.getKValue();


//                if (sysApplication.getTidType() == 1) {//XS
////                    final SerialPort serialPort = .getSerialPort();
//                    xsWeighter15 = XSWeighter15.getXSWeighter();
//                    if (xsWeighter15 != null) {
//                        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//                        builder1.setTitle("标定密码");
//                        final EditText et = new EditText(this);
//                        et.setHint("请输入密码");
//                        et.setInputType(InputType.TYPE_CLASS_PHONE);
//                        et.setSingleLine(true);
//                        builder1.setView(et);
//                        builder1.setNegativeButton("取消", null);
//                        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String password = et.getText().toString();
//                                if (password.equals("93716")) {
//                                    xsWeighter15.sendCalibrationData();  //发送标定命令10kg
//                                    MyToast.toastShort(context, "标定成功");
//                                } else {
//                                    MyToast.toastShort(context, "密码错误");
//                                }
//                            }
//                        });
//                        AlertDialog alertDialog = builder1.create();
//                        alertDialog.show();
//                    }
//                }
                break;
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
        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
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

//        String device = Build.DEVICE;
//
//        if (model.startsWith("Lenovo")) {
//            setLenovoLenovo(true);
//        }
//
//        if ("Meizu".equals(brand)) {
//            setMZ(true);
//        }
//
//        if ((manufacturer != null && manufacturer.trim().contains("samsung") && sdkVersion >= 9)
//                && (model == null || (!model.trim().toLowerCase()
//                .contains("google") && !model.trim().toLowerCase()
//                .contains("nexus")))) {
//            setSamsung(true);
//        }
//
//        // 安卓4.0以上rom
//        if (sdkVersion >= 14) {
//            setSdkGreaterThanApi14(true);
//        }
//        if (displayStr != null && displayStr.toLowerCase().contains("miui")) {
//            setMIUI(true);
//        }
//        if (brand.equals("Xiaomi") && model.trim().contains("MI 2")) {
//            setIsXiaomi2S(true);
//        }
//        if (brand.equals("Xiaomi") && model.trim().contains("1S")) {
//            setIsXiaomi2S(true);
//        }
//        if (brand.equals("Xiaomi") && model.trim().contains("MI-")) {
//            setMIUI(true);
//        }

    }

    // TODO 硬件信息


    /**
     * 发送订单信息到计量院后台,获取到 标定后的数据传输给计量院
     */
    private void sendCalibrationInfo(boolean isCalibration10kg, String initAd0, String initAd) {
        if (sysApplication.getTidType() < 1) {//非香山秤
            return;
        }
        int CalibrationWeight;
        if (isCalibration10kg) {
            CalibrationWeight = 10;
        } else {
            CalibrationWeight = 20;
        }
        String authenCode = MyPreferenceUtils.getSp(getApplicationContext()).getString(FPMS_AUTHENCODE, null);
        String dataKey = MyPreferenceUtils.getSp(getApplicationContext()).getString(FPMS_DATAKEY, null);
        if (authenCode == null || dataKey == null) {
            return;
        }

        String cmdECB = AESHelper.encryptDESedeECB("deviceCheck", dataKey);//标定
        String sb = "service=deviceService&cmd=" + cmdECB + "&authenCode=" + authenCode +
                "&appCode=FPMSWS" +
                "&deviceNo=123456789012" +
                "&macAddr=" +
                HttpHelper.getmInstants(sysApplication).getMac() +
                "&weight=" + CalibrationWeight +  //重量
                "&initAd=" + initAd0 +   //标准点0位Ad
                "&initAd=" + initAd;//标准点Ad

        String data = AESHelper.encryptDESedeECB(sb, MAIN_KEY);
        HttpHelper.getmInstants(sysApplication).onFpmsLogin(SystemSettingsActivity.this, data, VOLLEY_FLAG_FPMS_SUBMIT);

    }


    @Override
    public void onResponse(VolleyError volleyError, int flag) {

    }

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {

    }
}


////硬件信息Product: rk3288, CPU_ABI: armeabi-v7a, TAGS: test-keys, VERSION_CODES.BASE: 1, MODEL: rk3288, SDK: 22,
////        VERSION.RELEASE: 5.1.1, DEVICE: rk3288,
////
//           DISPLAY: rk3288-userdebug 5.1.1 LMY49F eng.android-build.20180629.135349 test-keys,
////        BRAND: Android, BOARD: rk30sdk,
//            FINGERPRINT: Android/rk3288/rk3288:5.1.1/LMY49F/android-build06291356:userdebug/test-keys,
////        ID: LMY49F, MANUFACTURER: rockchip,
//// USER: android-build
//                   liaokai
//
////香山
////硬件信息Product: rk3288, CPU_ABI: armeabi-v7a, TAGS: test-keys, VERSION_CODES.BASE: 1, MODEL: rk3288, SDK: 22,
////        VERSION.RELEASE: 5.1.1, DEVICE: rk3288,
//
//        DISPLAY: 4.0.0-180716-OEM,
//                   BRAND: Android, BOARD: rk30sdk,
//                   FINGERPRINT: Android/rk3288/rk3288:5.1.1/LMY49F/liaokai12061456:userdebug/test-keys,
//        ID: LMY49F, MANUFACTURER: rockchip,
//                   USER: