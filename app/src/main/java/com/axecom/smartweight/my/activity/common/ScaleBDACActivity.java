package com.axecom.smartweight.my.activity.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.config.IEventBus;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.axecom.smartweight.my.helper.HttpHelper;
import com.axecom.smartweight.utils.AESHelper;
import com.luofx.listener.VolleyListener;
import com.luofx.listener.VolleyStringListener;
import com.luofx.newclass.ActivityController;
import com.luofx.utils.DateUtils;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.mvp.view.MyBaseCommonACActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScaleBDACActivity extends MyBaseCommonACActivity implements IEventBus, IConstants, View.OnClickListener, VolleyListener, VolleyStringListener {

    private Button btnCalibration;//标定按钮

    /**
     * 重量数据  硬件K值  标定零位Ad ，标定ad
     */
    private TextView tvWeight, tvkValue, tvsbZeroAd, tvsbAd;

    /**
     * 计算K值  置零Ad ，当前ad
     */
    private TextView tvkValue2, tvsbZeroAd2, tvsbAd2;
    private Button btnGetK;//获取K值

    private void initView() {
        //置零按钮
        Button btnZeroSetting = findViewById(R.id.btnZeroSetting);
        btnZeroSetting.setOnClickListener(this);
        btnCalibration = findViewById(R.id.btnCalibration);
        btnCalibration.setOnClickListener(this);
        btnCalibration.setClickable(false);
        btnCalibration.setEnabled(false);

        findViewById(R.id.btnGetK).setOnClickListener(this);
        findViewById(R.id.btnsbSend).setOnClickListener(this);

        tvWeight = findViewById(R.id.tvWeight);
        tvkValue = findViewById(R.id.tvkValue);
        tvsbZeroAd = findViewById(R.id.tvsbZeroAd);
        tvsbAd = findViewById(R.id.tvsbAd);

        tvkValue2 = findViewById(R.id.tvkValue2);
        tvsbZeroAd2 = findViewById(R.id.tvsbZeroAd2);
        tvsbAd2 = findViewById(R.id.tvsbAd2);

        notifyKView();

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
    }

    private boolean isCalibration10kg;
    private SysApplication sysApplication;

    /**
     *
     */
    private int jumpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_bd);
        initView();
        jumpType = getIntent().getIntExtra(ACTIVITY_JUMP_TYPE, 0);
        sysApplication = (SysApplication) getApplication();
        EventBus.getDefault().register(this);//解除事件总线问题
        ActivityController.addActivity(this);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                goBackActivity();
                return false;
            }
        });
    }

    private Handler handler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除事件总线问题
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnZeroSetting://置零准备
                sysApplication.getMyBaseWeighter().sendZer();
                btnCalibration.setBackgroundResource(R.drawable.selector_btn_epay);
                btnCalibration.setClickable(true);
                btnCalibration.setEnabled(true);
                btnCalibration.setText("第三步\n标定");

                break;
            case R.id.btnRemoveSign://移除拆机标识
                sysApplication.getMyBaseWeighter().sendRemoveSign();
                MyToast.toastShort(context, "设定成功");
                break;
            case R.id.btnCalibration://10kg标定
                isSendDataFpsm = false;
                btnCalibration.setBackgroundResource(R.color.light_green);
                btnCalibration.setClickable(false);
                btnCalibration.setEnabled(false);
                kValue = null;
                sysApplication.getMyBaseWeighter().sendCalibrationData(isCalibration10kg);

                break;
            case R.id.btnGetK://  获取K值信息
                sysApplication.getMyBaseWeighter().getKValue();
                break;
            case R.id.btnsbSend://  获取K值信息
                sysApplication.getMyBaseWeighter().getKValue();
                String sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
                String sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
                String kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, null);
//                if (sbAd != null && sbZeroAd != null) {
//                    MyLog.blue("传输的 标定Ad= " + sbAd + "   传输的标定0位Ad=" + sbZeroAd);
//                    sendCalibrationInfo(isCalibration10kg, sbZeroAd, sbAd, kValue);
//                }
                break;
            default:
                break;
        }
    }

    private String kValue;
    /**
     * 开始标定
     */
    private boolean isStartBd;

    //定义处理接收的方法
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(BaseBusEvent event) {
        switch (event.getEventType()) {
            case WEIGHT_KVALUE:// 获取K值
                //称重的K值 改变了
                String data = (String) event.getOther();
                String[] array = data.split(" ");
                if (array.length == 4 && array[1].length() == 7 && array[2].length() == 7 && array[3].length() == 10) {
                    MyLog.logTest("Main  获取K值数据" + array[2]);

                    String sbAd = (array[1]);
                    String sbZeroAd = (array[2]);
                    kValue = (array[3]).replace("#", "");
                    MyPreferenceUtils.getSp(context).edit().putString(VALUE_K_WEIGHT, kValue).putString(VALUE_SB_ZERO_AD, sbZeroAd).apply();
                    notifyKView();
                } else {
                    sysApplication.getMyBaseWeighter().getKValue();
                }
                break;
            case WEIGHT_SX15:
                array = event.getOther().toString().split(" ");
                if (array.length >= 6 && array[1].length() == 7
                        && array[2].length() == 7 && array[3].length() == 7
                        && array[4].length() == 7 && array[5].length() == 7) {
                    MyLog.logTest("Main  获取的称重数据" + array[2]);

                    int currentAd = Integer.parseInt(array[1]);
                    int zeroAd = Integer.parseInt(array[2]);
                    int currentWeight = Integer.parseInt(array[3]);
                    int tareWeight = Integer.parseInt(array[4]);

                    int cheatSign = array[5].charAt(0) - 48;
                    int isNegative = array[5].charAt(1) - 48;
                    int isOver = array[5].charAt(2) - 48;
                    int isZero = array[5].charAt(3) - 48;
                    int isPeeled = array[5].charAt(4) - 48;
                    int isStable = array[5].charAt(5) - 48;

                    // 数据 设计
                    tvsbAd2.setText(currentAd + "");
                    tvsbZeroAd2.setText(zeroAd + "");
                    double k = ((float) currentWeight) / (currentAd - zeroAd) / 10000;
                    tvkValue2.setText(k + "");

                    if (isStartBd) {
                        if ((isCalibration10kg && currentWeight == 10000) || (!isCalibration10kg && currentWeight == 20000)) {
                            MyPreferenceUtils.getSp(context).edit().putString(VALUE_SB_AD, String.valueOf(currentAd)).apply();
                            MyPreferenceUtils.getSp(context).edit().putString(VALUE_SB_ZERO_AD, String.valueOf(zeroAd)).apply();

                            if (kValue != null) {//上传数据
                                if (!isSendDataFpsm) {
                                    int value = (currentAd - zeroAd) * 1000 / currentWeight;
                                    MyToast.toastShort(context, "标定成功");
                                    btnCalibration.setText("标定成功");
                                    sendCalibrationInfo(isCalibration10kg, String.valueOf(zeroAd), String.valueOf(currentAd), value);
                                    isSendDataFpsm = true;
                                    isStartBd = false;

                                    handler.sendEmptyMessageDelayed(1011, 600);
                                }
                            }
                            kValue = null;
                        }
                    }
                    dealWeight(currentWeight, isNegative);
                }
                break;
            case WEIGHT_CALIBRATION:// 获取到标定完成
                String eventString = event.getOther().toString();
                array = eventString.split(" "); //  C4 ERR0#
                if (array.length == 2 && array[1].length() == 5) {
                    if (eventString.contains("ERR0")) {
                        isStartBd = true;
                        sysApplication.getMyBaseWeighter().getKValue();
                    } else {
                        MyToast.toastShort(context, "标定失败");
                    }
                }
                break;
            case WEIGHT_ZEROING:// 置零返回值
                String zerolString = event.getOther().toString();
                array = zerolString.split(" "); //  C8 ERR0#
                if (array.length == 2 && array[1].length() == 5) {
                    if (zerolString.contains("ERR0")) {
                        MyToast.toastShort(context, "置零成功");
                        btnCalibration.setBackgroundResource(R.drawable.selector_btn_epay);
                        btnCalibration.setClickable(true);
                        btnCalibration.setEnabled(true);
                    } else {
                        MyToast.toastShort(context, "置零失败");
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * 回到原有的跳转目标  Activity
     */
    private void goBackActivity() {
        if (jumpType == 1) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            ScaleBDACActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBackActivity();
    }

    private void notifyKView() {
        String kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, null);
        String sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
        String sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
        tvkValue.setText(kValue);
        tvsbAd.setText(sbAd);
        tvsbZeroAd.setText(sbZeroAd);
    }

    /**
     * 是否得到了Ad值
     */
    private boolean isGetAd;

    private boolean isSendDataFpsm;

    /**
     * 显示重量
     *
     * @param weight         重量数据
     * @param isNegativeFlag 是否超重的标志
     */
    @SuppressLint("SetTextI18n")
    private void dealWeight(int weight, int isNegativeFlag) {
        boolean isNegative;
        isNegative = isNegativeFlag != 0;
        float weightFloat = (float) weight / 1000;

        if (weightFloat == 0.000f) {
            tvWeight.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
        } else {
            tvWeight.setTextColor(context.getResources().getColor(R.color.black));
        }

        if (isNegative) {
            tvWeight.setText("-" + decimalFormat.format(weightFloat));
        } else {
            tvWeight.setText(decimalFormat.format(weightFloat));
        }
    }

    /**
     * 上下文。使用上下文
     */
    final DecimalFormat decimalFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.

    /**
     * 发送订单信息到计量院后台,获取到 标定后的数据传输给计量院
     */
    private void sendCalibrationInfo(boolean isCalibration10kg, String initAd0, String initAd, int kValue) {
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

        sendDBData2Axe(isCalibration10kg, initAd0, initAd, kValue);

        String cmdECB = AESHelper.encryptDESedeECB("deviceCheck", dataKey);//标定
        String sb = "service=deviceService&cmd=" + cmdECB + "&authenCode=" + authenCode +
                "&appCode=FPMSWS" +
                "&deviceNo=" + sysApplication.getUserInfo().getSno() +
                "&macAddr=" +
                HttpHelper.getmInstants(sysApplication).getMac() +
                "&weight=" + CalibrationWeight + //重量，必须20kg
                "&initAd=" + initAd0 +   //标准点0位Ad
                "&initAd=" + initAd;//标准点Ad

        String data = AESHelper.encryptDESedeECB(sb, MAIN_KEY);
        HttpHelper.getmInstants(sysApplication).onFpmsLogin(ScaleBDACActivity.this, data, VOLLEY_FLAG_FPMS_SUBMIT);
    }


    /**
     * 发送必定数据到安鑫宝后台
     */
    private void sendDBData2Axe(boolean isCalibration10kg, String initAd0, String initAd, int kValue) {
        Map<String, String> map = new HashMap<>();

        map.put("terid", String.valueOf(sysApplication.getUserInfo().getTid()));
        map.put("maxlv", MAX_LEV);
        if (isCalibration10kg) {
            map.put("bd", "10kg");
            map.put("bdvalue", "10kg");
        } else {
            map.put("bd", "20kg");
            map.put("bdvalue", "20kg");
        }
        map.put("fkd", "0.005");
        map.put("ad0", initAd0);
        map.put("ad1", initAd);
        map.put("bdman", sysApplication.getUserInfo().getSeller());
        map.put("bdtime", DateUtils.getYY_TO_ss(new Date()));

        map.put("k", String.valueOf(kValue));
        map.put("note", "-");
        HttpHelper.getmInstants(sysApplication).upTicBD(map, ScaleBDACActivity.this, VOLLEY_FLAG_AXE_BD);
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {

    }

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        switch (flag) {
            case VOLLEY_FLAG_FPMS_SUBMIT:
                MyLog.blue("返回数据" + jsonObject.toString());
                break;
            case VOLLEY_FLAG_AXE_BD:
                MyLog.blue("返回数据标定数据" + jsonObject.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void onStringResponse(VolleyError volleyError, int flag) {
        switch (flag) {
            case VOLLEY_FLAG_FPMS_SUBMIT:

                break;
            case VOLLEY_FLAG_AXE_BD:

                MyLog.blue("返回数据标定数据失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void onStringResponse(String response, int flag) {
        switch (flag) {
            case VOLLEY_FLAG_FPMS_SUBMIT:
                MyLog.blue("返回数据" + response);
                break;
            case VOLLEY_FLAG_AXE_BD:
                MyLog.blue("返回数据标定数据" + response);
                break;
            default:
                break;
        }
    }
}
