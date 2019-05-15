package com.axecom.smartweight.my.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.LogActivity;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.dao.UserInfoDao;
import com.axecom.smartweight.my.helper.HttpHelper;
import com.luofx.listener.VolleyListener;
import com.luofx.utils.net.NetWorkJudge;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.mvp.view.MyBaseCommonACActivity;

import org.json.JSONObject;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    10:18
 * 邮件：424533553@qq.com
 * 说明：使用于5.1.1 版本不需要使用 权限检查
 */
public class HomeActivity extends MyBaseCommonACActivity implements View.OnClickListener, VolleyListener, IConstants {

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sysApplication = (SysApplication) getApplication();
        context = this;
        if (sysApplication.getTidType() >= 0) {
            //1. 判定数据是否更新     // 2.是否有标定数据
            userInfoDao = new UserInfoDao(context);
//            initHandler();
            setInitView();
            isUserInfoAble = isCanLogin();
            startLogin();
        } else {
            MyToast.showError(context, "该秤类型无法识别，请联系管理员");
        }
    }

    /**
     * 初始化控件
     */
    public void setInitView() {
        findViewById(R.id.ivLog).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
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

        if (NetWorkJudge.isNetworkAvailable(context)) {
            //获取用户信息
            HttpHelper.getmInstants(sysApplication).getUserInfoEx(this, FLAG_GET_USER_INFO);
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
            String kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, null);
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
            if (NetWorkJudge.isNetworkAvailable(context)) {
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
        if (isUserInfoAble && !isFirstInit && isBDDataAble) {
            if (sysApplication.getTidType() == 2) {
                jumpActivity(MainActivity8.class, true);
            } else if (sysApplication.getTidType() == 3) {
                jumpActivity(MainActivityAXE.class, true);
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
                Intent intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        MyToast.toastShort(context, "网络请求失败");
        switch (flag) {
            case FLAG_GET_USER_INFO://获取用户信息失败
                isUserInfoAble = false;
                break;
            case HANDLER_IMAGE_FINISH:
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
                case HANDLER_IMAGE_FINISH:
                    break;
                default:
                    break;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}

