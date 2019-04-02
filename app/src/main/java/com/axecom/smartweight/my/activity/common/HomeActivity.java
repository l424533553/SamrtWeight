package com.axecom.smartweight.my.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.LogActivity;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.AllGoods;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.GoodsType;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.dao.AllGoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsTypeDao;
import com.axecom.smartweight.my.entity.dao.HotGoodsDao;
import com.axecom.smartweight.my.entity.dao.TraceNoDao;
import com.axecom.smartweight.my.entity.dao.UserInfoDao;
import com.axecom.smartweight.my.entity.netresult.TraceNoBean;
import com.axecom.smartweight.my.helper.HttpHelper;
import com.luofx.listener.VolleyListener;
import com.luofx.utils.MyPreferenceUtils;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.mvp.view.MyBaseCommonActivity;
import com.xuanyuan.xinyu.MyToast;
import com.luofx.utils.net.NetWorkJudge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    10:18
 * 邮件：424533553@qq.com
 * 说明：使用于5.1.1 版本不需要使用 权限检查
 */
public class HomeActivity extends MyBaseCommonActivity implements View.OnClickListener, VolleyListener, IConstants {

    private CheckedTextView savePwdCtv;
    private CheckedTextView autoLogin;
    private SysApplication sysApplication;
    private HotGoodsDao hotGoodsDao;
    private GoodsTypeDao goodsTypeDao;
    private AllGoodsDao allGoodsDao;
    private UserInfoDao userInfoDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sysApplication = (SysApplication) getApplication();
        context = this;
        if (sysApplication.getTidType() >= 0) {
            if (!isInit) {
                userInfoDao = new UserInfoDao(context);
                hotGoodsDao = new HotGoodsDao(context);
                goodsTypeDao = new GoodsTypeDao(context);
                allGoodsDao = new AllGoodsDao(context);
                initHandler();
                setInitView();
                startLogin();
                isInit = true;
            }
        } else {
            MyToast.showError(context, "该秤类型无法识别，请联系管理员");
        }
        //注销注册
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销注册
        EventBus.getDefault().unregister(this);
    }

    boolean isInit = false;// 是否初始化过

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Handler handler;
    private int requestCount = 3;

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
                                HttpHelper.getmInstants(sysApplication).initGoodsEx(HomeActivity.this, tid, 2);
                                HttpHelper.getmInstants(sysApplication).initGoodsType(HomeActivity.this, 3);
                                HttpHelper.getmInstants(sysApplication).initAllGoods(HomeActivity.this, 4);
                                requestCount = 3;
                            }
                        });
                        break;
                    case NOTIFY_SUCCESS:
                        if (successFlag == requestCount)
                            jumpActivity(false);
                        break;
                    case NOTIFY_JUMP:
                        jumpActivity(false);
                        break;
                }
                return false;
            }
        });
    }

    /****************************************************************************************/

    public void setInitView() {
        findViewById(R.id.ivLog).setOnClickListener(this);
        Button confirmBtn = findViewById(R.id.home_confirm_btn);

        TextView cardNumberTv = findViewById(R.id.home_card_number_tv);
        TextView pwdTv = findViewById(R.id.home_pwd_tv);
        TextView loginTv = findViewById(R.id.home_login_tv);
        savePwdCtv = findViewById(R.id.home_save_pwd_ctv);
        autoLogin = findViewById(R.id.home_login_auto);
        autoLogin.setOnClickListener(this);
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

    /**
     * 1.有网
     * 2.无网  UserInfo！=null 直接跳转
     */
    private void startLogin() {
        List<UserInfo> userInfos = userInfoDao.queryByColumnName("id", 1);
        if (userInfos != null && userInfos.size() >= 1) {
            sysApplication.setUserInfo(userInfos.get(0));
            jumpActivity(false);
        } else {
            if (NetWorkJudge.isNetworkAvailable(context)) {
                //获取用户信息
                HttpHelper.getmInstants(sysApplication).getUserInfoEx(this, 1);
            } else {
                MyToast.showError(context, "请设置网络初始化用户信息");
            }
        }
    }

    private String kValue; //k 值
    private String sbAd;  // 标定Ad
    private String sbZeroAd;//标定零位Ad

    /**
     * 检查各项参数，参数获取到了，才能进行跳转
     * 1.标定K值和标定0位K值
     */
    private void jumpActivity(boolean isOverJump) {
        if (isOverJump) {
            jumpActivity(MainActivity.class, true);
            return;
        }
        // 商通秤不用传递ad值
        if (sysApplication.getTidType() > 0) {
            // 查验 是否 有K值
            kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, null);
            sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
            sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
            if (kValue != null && sbAd != null && sbZeroAd != null) {
                jumpActivity(MainActivity.class, true);
            } else {
                //准备进行K值获取
                if (sysApplication.isOpenWeight()) {
                    sysApplication.getMyBaseWeighter().getKValue();
                }
            }

            //TODO 待删除
            sysApplication.getMyBaseWeighter().getKValue();

        } else {
            jumpActivity(MainActivity.class, true);
        }
    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseBusEvent event) {
        switch (event.getEventType()) {
            case BaseBusEvent.NOTIFY_WEIGHT_KVALUE:
                //称重的K值 改变了
                String data = (String) event.getOther();
                String[] array = data.split(" ");
                if (array.length >= 3) {
                    if (array[0].length() == 7 && array[1].length() == 7 && array[2].length() == 9) {
                        sbAd = array[0];
                        sbZeroAd = array[1];
                        kValue = array[2];
                        MyPreferenceUtils.getSp(context).edit().putString(VALUE_K_WEIGHT,kValue).apply();
                        MyPreferenceUtils.getSp(context).edit().putString(VALUE_SB_AD,sbAd).apply();
                        MyPreferenceUtils.getSp(context).edit().putString(VALUE_SB_ZERO_AD,sbZeroAd).apply();
                        MyLog.blue("进行K值得获取，并取得了数据");
                        // 将标定数据传给计量院
                        jumpActivity(true);
                    }else {
                        //准备进行K值获取
                        if (sysApplication.isOpenWeight()) {
                            sysApplication.getMyBaseWeighter().getKValue();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
//        SoftKeyborad.Builder builder = new SoftKeyborad.Builder(HomeActivity.this);
        switch (v.getId()) {
            case R.id.home_confirm_btn:
                startLogin();
                break;
            case R.id.home_save_pwd_ctv:
                savePwdCtv.setChecked(!savePwdCtv.isChecked());
                break;
            case R.id.ivLog:
                //进入日志界面
                Intent intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                break;
            case R.id.home_login_auto:
                autoLogin.setChecked(!autoLogin.isChecked());
//                SPUtils.put(this, AUTO_LOGIN, autoLogin.isChecked());
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        MyToast.toastShort(context, "网络请求失败");
        switch (flag) {
            case 1:
                break;
            case 2:
                jumpActivity(false);
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
                                        hotGoodsDao.insert(goodsList);
                                    }
                                }
                                successFlag++;
                                handler.sendEmptyMessage(NOTIFY_SUCCESS);
                            }
                        });
                    }
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
//                                int flag2 = traceNoDao.insert(goodsList);
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
