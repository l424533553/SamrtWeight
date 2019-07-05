package com.axecom.smartweight.activity.common.mvvm.home;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.activity.common.DataFlushActivity;
import com.axecom.smartweight.activity.common.ScaleBDACActivity;
import com.axecom.smartweight.activity.main.view.MainActivity;
import com.axecom.smartweight.activity.main.view.MainActivity8;
import com.axecom.smartweight.activity.main.view.MainActivityAXE;
import com.axecom.smartweight.activity.main.view.MainActivitySX;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.mvvm.view.IAllView;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.entity.netresult.ResultInfo;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.entity.dao.UserInfoDao;
import com.axecom.smartweight.helper.HttpHelper;
import com.axecom.smartweight.mvvm.entity.ResultRtInfo;
import com.axecom.smartweight.mvvm.entity.RetrofitCallback;
import com.axecom.smartweight.mvvm.retrofit.HttpRtHelper;
import com.xuanyuan.library.help.ActivityController;
import com.xuanyuan.library.utils.system.SystemInfoUtils;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.listener.VolleyListener;
import com.xuanyuan.library.mvp.view.MyBaseCommonACActivity;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;

import org.json.JSONObject;

import java.util.List;

import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_ERR_BY_NET;
import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_NOT_GET;
import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_OK_BY_NET;
import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_VALIDATE_FINISH;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    10:18
 * 邮件：424533553@qq.com
 * 说明：使用于5.1.1 版本不需要使用 权限检查
 */
public class HomeActivity extends MyBaseCommonACActivity implements IAllView, View.OnClickListener, RetrofitCallback, VolleyListener, IConstants {

    private SysApplication sysApplication;
    private UserInfoDao userInfoDao;
    private boolean isUserInfoAble;
    // 是否第一次登陆
    private boolean isFirstInit;
    //标定数据是否可用
    private boolean isBDDataAble;

    // 数据使用量，是用功能
    // 使用功能开发。使用功能

    @Override
    protected void onLiveEvent(String type) {
        switch (type) {
            case EVENT_GET_USERINFO_ERR_BY_NET:
                isUserInfoAble = false;
                break;
            case EVENT_GET_USERINFO_OK_BY_NET:
                viewModel.validateUserInfo();
                break;
            case EVENT_GET_USERINFO_VALIDATE_FINISH:
                isUserInfoAble = true;
                startLogin();
                break;
            case EVENT_GET_USERINFO_NOT_GET:
                MyToast.toastLong(context, "未获取到秤的配置信息");
                break;
        }
    }

    private HomeActivityVM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sysApplication = (SysApplication) getApplication();
        context = this;
        if (sysApplication.getTidType() >= 0) {
            viewModel = new HomeActivityVM(this);

            //1. 判定数据是否更新     // 2.是否有标定数据
            userInfoDao = new UserInfoDao();
//            initHandler();
            setInitView();
            isUserInfoAble = isCanLogin();
            startLogin();
        } else {
            MyToast.showError(context, "该秤类型无法识别，请联系管理员");
        }
    }

    private Button btnLogin;
    /**
     * 初始化控件
     */
    public void setInitView() {
        findViewById(R.id.ivLog).setOnClickListener(this);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        TextView loginTv = findViewById(R.id.home_login_tv);
        loginTv.setOnClickListener(this);
    }

    /**
     * 1.有网  验证用户信息
     * 2.无网  UserInfo！=null 直接跳转
     *
     * @return false UserInfo 不可用，不能直接使用 :   true,无网情况下可以 UserInfo 有效
     */
    private boolean isCanLogin() {
        List<UserInfo> userInfos = userInfoDao.queryByColumnName("id", 1);
        // 用户信息是否有效
        boolean userInfoClickAble = false;
        if (userInfos != null && userInfos.size() >= 1) {
            sysApplication.setUserInfo(userInfos.get(0));
            userInfoClickAble = true;
        }

        if (MyNetWorkUtils.isNetworkAvailable(context)) {
            //获取用户信息
            if (Build.VERSION.SDK_INT >= 21) {
                HttpRtHelper.getmInstants().getUserInfoExByRetrofit(SystemInfoUtils.getMac(context), HomeActivity.this, FLAG_GET_USER_INFO);
            } else {
                viewModel.getUserInfo();
            }


        } else { // 无网
            if (userInfoClickAble) {
                return true;
            } else {
                MyToast.showError(context, "请设置网络初始化用户信息");
            }
        }
        return false;
    }

    /**
     * 商通 15寸秤要检测 标定数据
     *
     * @return 标定数据是否可以
     */
    private boolean isBDData() {
        if (sysApplication.getTidType() == 1) {
            // 查验 是否 有K值
            String kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, VALUE_K_DEFAULT);
            //标定零位Ad
            String sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
//            if (sysApplication.getTidType() == 1) {
//                // 标定Ad ,相对固定的 数据
//                String sbAd = "2629157";
//            }

            if (kValue != null && sbZeroAd != null) {
                if (HttpHelper.getmInstants(sysApplication).validateUserInfo(sysApplication.getUserInfo(), sysApplication.getTidType())) {
                    return true;
                } else {
                    MyToast.showError(context, "用户信息不全");
                }
            } else {
                Intent intent = new Intent(context, ScaleBDACActivity.class);
                intent.putExtra(ACTIVITY_JUMP_TYPE, 1);
                startActivityForResult(intent, CODE_JUMP2_SCALEBD);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 开始 登陆
     */
    private void startLogin() {
        if (isUserInfoAble) {
            isFirstInit = MyPreferenceUtils.getBoolean(context, SP_IS_FIRST_INIT, true);
            if (MyNetWorkUtils.isNetworkAvailable(context)) {
                Intent intent = new Intent(context, DataFlushActivity.class);
                if (isFirstInit) {//是否第一次安装
                    intent.putExtra(INTENT_AUTO_UPDATE, 0);
                    startActivityForResult(intent, CODE_JUMP2_DATAFLUSH);
                } else {
//                    intent.putExtra(INTENT_AUTO_UPDATE, 2);
                    isBDDataAble = isBDData();
                    goMainActivity();
                }
            } else {
                if (isFirstInit) {
                    MyToast.showError(context, "请设置网络初始化用户信息");
                } else {
                    isBDDataAble = isBDData();
                    goMainActivity();
                }
            }
        }
    }

    /**
     * 根据秤的不同进入 MainActivity
     */
    private void goMainActivity() {
        //TODO
        if (isUserInfoAble && !isFirstInit && isBDDataAble) {
            if (sysApplication.getTidType() == 2) {
                jumpActivity(MainActivity8.class, true);
            } else if (sysApplication.getTidType() == 3) {
                jumpActivity(MainActivityAXE.class, true);
            } else if (sysApplication.getTidType() == 4) {
                jumpActivity(MainActivitySX.class, true);
            } else {
                jumpActivity(MainActivity.class, true);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CODE_JUMP2_DATAFLUSH == requestCode) {
            if (RESULT_OK == resultCode) {//数据更新成功
                isFirstInit = false;
                // 标定数据检测 合格了
                isBDDataAble = isBDData();
                if (isBDDataAble) {
                    goMainActivity();
                }
            }
        } else if (CODE_JUMP2_SCALEBD == requestCode) {
            if (RESULT_OK == resultCode) {
                isBDDataAble = true;
                goMainActivity();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                isUserInfoAble = isCanLogin();
                startLogin();
                break;
            case R.id.ivLog:
                //进入日志界面
//                Intent intent = new Intent(this, LogActivity.class);
//                startActivity(intent);
                this.finish();
                System.exit(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        MyToast.toastShort(context, "网络请求失败");
        //获取用户信息失败
        if (flag == FLAG_GET_USER_INFO) {
            isUserInfoAble = false;
        }
    }

    @Override
    public void onNext(ResultRtInfo resultInfo, int flag) {
        if (FLAG_GET_USER_INFO == flag) {
            if (resultInfo != null && resultInfo.getStatus() == 0) {
                UserInfo userInfo = (UserInfo) resultInfo.getData();
//                UserInfo userInfo = JSON.parseObject(resultInfo.getData().toString(), UserInfo.class);
                if (userInfo != null && HttpHelper.getmInstants(sysApplication).validateUserInfo(userInfo, sysApplication.getTidType())) {//用户信息合格
                    userInfo.setId(1);
                    userInfoDao.updateOrInsert(userInfo);

                    // 比对两次的信息是否一样
                    UserInfo appInfo = sysApplication.getUserInfo();
                    if (appInfo != null) {
                        if (appInfo.getSellerid() != userInfo.getSellerid()) {
                            MyPreferenceUtils.getSp(context).edit().putBoolean(SP_IS_FIRST_INIT, true).apply();
                        }
                    }
                    sysApplication.setUserInfo(userInfo);//保存信息
                    isUserInfoAble = true;
                    startLogin();
                }
            } else {
                MyToast.toastLong(context, "未获取到秤的配置信息");
            }
        }
    }

    @Override
    public void onError(Throwable e, int flag) {
        MyToast.toastShort(context, "网络请求失败");
        //获取用户信息失败
        if (flag == FLAG_GET_USER_INFO) {
            isUserInfoAble = false;
        }
    }

    @Override
    public void onComplete(int flag) {

    }

    /**
     * @param jsonObject json对象
     * @param flag       网络
     */
    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        try {
            final ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
            switch (flag) {
                case FLAG_GET_USER_INFO:
                    if (resultInfo != null && resultInfo.getStatus() == 0) {
                        UserInfo userInfo = JSON.parseObject(resultInfo.getData(), UserInfo.class);
                        if (userInfo != null && HttpHelper.getmInstants(sysApplication).validateUserInfo(userInfo, sysApplication.getTidType())) {//用户信息合格
                            userInfo.setId(1);
                            userInfoDao.updateOrInsert(userInfo);

                            // 比对两次的信息是否一样
                            UserInfo appInfo = sysApplication.getUserInfo();
                            if (appInfo != null) {
                                if (appInfo.getSellerid() != userInfo.getSellerid()) {
                                    MyPreferenceUtils.getSp(context).edit().putBoolean(SP_IS_FIRST_INIT, true).apply();
                                }
                            }

                            sysApplication.setUserInfo(userInfo);//保存信息
                            isUserInfoAble = true;
                            startLogin();
                        }
                    } else {
                        MyToast.toastLong(context, "未获取到秤的配置信息");
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //按键监听：
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("RRR", "按键==" + keyCode);
        switch (keyCode) {
            case 22://向右
            case 20://向下
            case 19://向上
            case 21://向左
                btnLogin.setFocusable(true);
                btnLogin.requestFocus();
                btnLogin.setFocusableInTouchMode(true);
                btnLogin.requestFocusFromTouch();
                btnLogin.setHovered(true);
                btnLogin.setPressed(true);
//                btnLogin.setFocusableInTouchMode();
                break;
            case 131://退出键
                onBackPressed();

                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (!finishAll()) {
            MyToast.toastShort(this, "再次点击退出！");
        }
    }






}

