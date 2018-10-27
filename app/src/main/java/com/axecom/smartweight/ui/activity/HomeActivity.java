package com.axecom.smartweight.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.manager.AccountManager;
import com.axecom.smartweight.my.LogActivity;
import com.axecom.smartweight.my.entity.AllGoods;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.GoodsType;
import com.axecom.smartweight.my.entity.LogBean;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.dao.AllGoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsTypeDao;
import com.axecom.smartweight.my.entity.dao.TraceNoDao;
import com.axecom.smartweight.my.entity.dao.UserInfoDao;
import com.axecom.smartweight.my.entity.netresult.TraceNoBean;
import com.axecom.smartweight.my.net.NetHelper;
import com.axecom.smartweight.ui.view.CustomDialog;
import com.axecom.smartweight.ui.view.SoftKeyborad;
import com.axecom.smartweight.utils.ButtonUtils;
import com.axecom.smartweight.utils.CommonUtils;
import com.axecom.smartweight.utils.SPUtils;
import com.luofx.listener.VolleyListener;
import com.luofx.utils.DateUtils;
import com.luofx.utils.PreferenceUtils;
import com.luofx.utils.common.MyToast;
import com.luofx.utils.log.MyLog;
import com.luofx.utils.net.NetWorkJudge;
import com.shangtongyin.tools.serialport.IConstants_ST;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018-5-8.
 */
public class HomeActivity extends Activity implements View.OnClickListener, VolleyListener, IConstants_ST {

    private static final String AUTO_LOGIN = "auto_login";
    private TextView cardNumberTv;
    private TextView pwdTv;


    private CheckedTextView savePwdCtv;


    private CheckedTextView autoLogin;

    private Button confirmBtn;

    SysApplication sysApplication;

    private GoodsDao goodsDao;
    private GoodsTypeDao goodsTypeDao;
    private AllGoodsDao allGoodsDao;
    private UserInfoDao userInfoDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sysApplication = (SysApplication) getApplication();

        context = this;

        userInfoDao = new UserInfoDao(context);
        goodsDao = new GoodsDao(context);
        goodsTypeDao = new GoodsTypeDao(context);
        allGoodsDao = new AllGoodsDao(context);

        initHandler();
        setInitView();
        startLogin();
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
                    case NOTIFY_SUCCESS:
                        if (successFlag == 3)
                            getTraceNo(sysApplication.getUserInfo());
                        break;
                    case NOTIFY_JUMP:
                        jumpActivity();
                        break;
                }
                return false;
            }
        });

    }

    /****************************************************************************************/


    public void setInitView() {

        findViewById(R.id.ivLog).setOnClickListener(this);
        confirmBtn = findViewById(R.id.home_confirm_btn);

        cardNumberTv = findViewById(R.id.home_card_number_tv);
        pwdTv = findViewById(R.id.home_pwd_tv);
        TextView loginTv = findViewById(R.id.home_login_tv);
        savePwdCtv = findViewById(R.id.home_save_pwd_ctv);
        savePwdCtv.setChecked(AccountManager.getInstance().getRememberPwdState());
        autoLogin = findViewById(R.id.home_login_auto);
        autoLogin.setOnClickListener(this);
        boolean autoLoin = (boolean) SPUtils.get(this, AUTO_LOGIN, false);
        autoLogin.setChecked(autoLoin);
        pwdTv.setOnClickListener(this);
        loginTv.setOnClickListener(this);

        cardNumberTv.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        savePwdCtv.setOnClickListener(this);
    }


    /**
     * 上下文对象
     */
    private Context context;


    NetHelper netHelper;

    private void startLogin() {
        UserInfo userInfo = userInfoDao.queryById(1);
        SysApplication application = (SysApplication) getApplication();
        if (userInfo == null) {
            //进行 信息获取
            netHelper = new NetHelper(application, this);
            netHelper.getUserInfo(netHelper.getIMEI(context), 1);
        } else {
            sysApplication.setUserInfo(userInfo);
            getTraceNo(userInfo);


//            jumpActivity();
        }
    }

    private void jumpActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void getTraceNo(UserInfo userInfo) {
        if (NetWorkJudge.isNetworkAvailable(context)) {
            //TODO
//            String url = BASE_IP_ST + "/api/smartsz/gettracenolist?shid=1136";
            String url = BASE_IP_ST + "/api/smartsz/gettracenolist?shid=" + userInfo.getSellerid();
            sysApplication.volleyGet(url, this, 7);
        }
    }

    @Override
    public void onClick(View v) {
//        SoftKeyborad.Builder builder = new SoftKeyborad.Builder(HomeActivity.this);
        switch (v.getId()) {
            case R.id.home_confirm_btn:
                startLogin();
                break;
//            case R.id.home_card_number_tv:
//                if (!ButtonUtils.isFastDoubleClick(R.id.home_card_number_tv)) {
//                    builder.create(new SoftKeyborad.OnConfirmedListener() {
//                        @Override
//                        public void onConfirmed(String result) {
//                            cardNumberTv.setText(result);
//                            if (AccountManager.getInstance().getPwdBySerialNumber(result) != null) {
//                                pwdTv.setText(AccountManager.getInstance().getPwdBySerialNumber(result));
//                                savePwdCtv.setChecked(true);
//                            } else {
//                                savePwdCtv.setChecked(false);
//                            }
//                        }
//                    }).show();
//                }
//                break;
//            case R.id.home_pwd_tv:
//                if (!ButtonUtils.isFastDoubleClick(R.id.home_pwd_tv)) {
//                    builder.create(new SoftKeyborad.OnConfirmedListener() {
//                        @Override
//                        public void onConfirmed(String result) {
//                            pwdTv.setText(result);
//                        }
//                    }).show();
//                }
//                break;
            case R.id.home_save_pwd_ctv:
                savePwdCtv.setChecked(!savePwdCtv.isChecked());
                AccountManager.getInstance().saveRememberPwdState(savePwdCtv.isChecked());
                break;

            case R.id.ivLog:
                //进入日志界面
                Intent intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                break;
            case R.id.home_login_auto:
                autoLogin.setChecked(!autoLogin.isChecked());
                SPUtils.put(this, AUTO_LOGIN, autoLogin.isChecked());
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        MyToast.toastShort(context, "网络请求失败");

        switch (flag) {
            case 1:
                break;
            case 2:
                jumpActivity();
                MyToast.toastShort(context, "初始化数据不完全");
                break;
            case 7:
                handler.sendEmptyMessage(NOTIFY_JUMP);
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
                                sysApplication.setUserInfo(userInfo);//保存信息

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
                                    successFlag++;
                                    handler.sendEmptyMessage(NOTIFY_SUCCESS);
                                }
                            }
                        });
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
                            successFlag++;
                            handler.sendEmptyMessage(NOTIFY_SUCCESS);
                        }
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
                            successFlag++;
                            handler.sendEmptyMessage(NOTIFY_SUCCESS);
                        }
                    }
                    break;
                case 7:
                    if (resultInfo != null) {
                        if (resultInfo.getStatus() == 0) {
                            List<TraceNoBean> goodsList = JSON.parseArray(resultInfo.getData(), TraceNoBean.class);
                            if (goodsList != null && goodsList.size() > 0) {

                                TraceNoDao traceNoDao = new TraceNoDao(context);
                                traceNoDao.deleteTableData();
                              int flag2=  traceNoDao.insert(goodsList);
                            }
                        }
                    }
                    handler.sendEmptyMessage(NOTIFY_JUMP);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private int successFlag = 0;


}
