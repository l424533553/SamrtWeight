package com.axecom.smartweight.my.helper;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.activity.common.LockActivity;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.AdResultBean;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.utils.AESHelper;
import com.luofx.listener.VolleyListener;
import com.luofx.utils.MyPreferenceUtils;
import com.xuanyuan.library.MyLog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * author: luofaxin
 * date： 2018/9/11 0011
 * email:424533553@qq.com
 * describe:
 */
public class HeartBeatServcice extends IntentService implements VolleyListener, IConstants {
    private boolean isLooper = true; //是否需要循环
    /**
     * 定义我们自己写的Binder对象
     */
//    private HttpHelper httpHelper;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public HeartBeatServcice(String name) {
        super(name);
        MyLog.mylog("构造方法  HeartBeatServcice( name)");
    }

    public HeartBeatServcice() {
        super("HeartBeatServcice");
        MyLog.mylog("构造方法  HeartBeatServcice()");
    }

    private int marketid;// 市场id

    @Override
    protected void onHandleIntent(Intent intent) {
        marketid = intent.getIntExtra("marketid", -1);
        int terid = intent.getIntExtra("terid", -1);
        if (marketid <= 0 || terid <= 0) {
            return;
        }

        while (isLooper) {
            try {
//              MyLog.mylog(" 执行 心跳 命令");
                HttpHelper.getmInstants(application).upStateEx(marketid, HeartBeatServcice.this, terid, 0, 1);  // scale
                Thread.sleep(UPDATE_STATE_TIME);//2分钟
                sendStatus2Fpms(0, HeartBeatServcice.this);
//              sendCheck2Fpms(HeartBeatServcice.this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送状态信息到计量院
     *
     * @param userStatus 0正常，1异常
     */
    private void sendStatus2Fpms(int userStatus, VolleyListener listener) {
        String authenCode = MyPreferenceUtils.getSp(getApplicationContext()).getString(FPMS_AUTHENCODE, null);
        String dataKey = MyPreferenceUtils.getSp(getApplicationContext()).getString(FPMS_DATAKEY, null);
        if (authenCode == null || dataKey == null) {
            return;
        }
        String cmdECB = AESHelper.encryptDESedeECB("updateStatus", dataKey);

        StringBuilder sb = new StringBuilder();
        sb.append("service=deviceService&cmd=" + cmdECB)
                .append("&authenCode=" + authenCode)
                .append("&appCode=FPMSWS")
                .append("&deviceNo=123456789012")
                .append("&deviceModel=xs-weight")
                .append("&factoryName=xs-weight")
                .append("&productionDate=2018-11-28")
                .append("&macAddr=" + HttpHelper.getmInstants(application).getMac())
                .append("&stallCode=")
                .append("&businessEntity=")
                .append("&creditCode=")
                .append("&userStatus=" + userStatus);

        String data = AESHelper.encryptDESedeECB(sb.toString(), MAIN_KEY);
        HttpHelper.getmInstants(application).onFpmsLogin(listener, data, VOLLEY_FLAG_FPMS_STATE);
    }

    /**
     * @param listener 上传标定数据
     */
    private void sendCheck2Fpms(VolleyListener listener) {
        String authenCode = MyPreferenceUtils.getSp(getApplicationContext()).getString(FPMS_AUTHENCODE, null);
        String dataKey = MyPreferenceUtils.getSp(getApplicationContext()).getString(FPMS_DATAKEY, null);
        if (authenCode == null || dataKey == null) {
            return;
        }
        String cmdECB = AESHelper.encryptDESedeECB("deviceCheck", dataKey);

        StringBuilder sb = new StringBuilder();
        sb.append("service=deviceService&cmd=" + cmdECB)
                .append("&authenCode=" + authenCode)
                .append("&appCode=FPMSWS")
                .append("&deviceNo=123456789012")
                .append("&macAddr=" + HttpHelper.getmInstants(application).getMac())

                .append("&weight=" + 15)//校准点的重量（kg）,整数
                .append("&initAd=" + 2341289)//校准后的原始零位值
                .append("&initAd=" + 2641289);//校准点的AD值

        String data = AESHelper.encryptDESedeECB(sb.toString(), MAIN_KEY);
        HttpHelper.getmInstants(application).onFpmsLogin(listener, data, VOLLEY_FLAG_FPMS_CHECK);
    }

    private SysApplication application;
    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        application = (SysApplication) this.getApplication();
        MyLog.mylog("构造方法  onCreate() ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.mylog("构造方法  onStartCommand() ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isLooper = false;
        super.onDestroy();
        //注销注册

    }

    @Override
    public void unbindService(ServiceConnection conn) {
        isLooper = false;
        super.unbindService(conn);
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        MyLog.myInfo("错误" + volleyError.getMessage());
    }

    private boolean isDisable;// 是否消失

    /**
     * @param jsonObject json对象
     * @param flag       请求索引标识
     */
    @Override
    public void onResponse(final JSONObject jsonObject, final int flag) {

        application.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                switch (flag) {
                    case 1:
                        MyLog.myInfo("成功" + jsonObject.toString());
                        ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                        if (resultInfo.getStatus() == 0) {
                            if ("1".equals(resultInfo.getData())) {//禁用
                                if (!isDisable) {
                                    Intent intent = new Intent(context, LockActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                isDisable = true;
                            } else {
                                // 用事件总线的方式取代BroadCast
                                BaseBusEvent event = new BaseBusEvent();
                                event.setEventType(BaseBusEvent.ACTION_UNLOCK_SOFT);
                                EventBus.getDefault().post(event);
                                isDisable = false;
                            }
                            //获取通告信息
                            MyLog.mylog(" 执行 通告 命令");
                            HttpHelper.getmInstants(application).upAdMessageEx(marketid, HeartBeatServcice.this, 2);  //通知 scalesId 秤的编号
                        }
                        break;
                    case 2:// 获得消息
                        AdResultBean adResultBean = JSON.parseObject(jsonObject.toString(), AdResultBean.class);
                        if (adResultBean != null && adResultBean.getStatus() == 0) {
                            AdResultBean.DataBean dataBean = adResultBean.getData();
                            if (dataBean != null) {
                                int saveId = MyPreferenceUtils.getSp(context).getInt("AdId", -1);
                                int id = dataBean.getId();
                                if (saveId != id) {// 遇上一条不一样，发终端通知
                                    MyPreferenceUtils.getSp(context).edit().putInt("AdId", id).apply();
//                                    Intent intent = new Intent();
//                                    intent.setAction(NOTIFY_MESSAGE_CHANGE);
//                                    intent.putExtra("data", dataBean);
//                                    sendBroadcast(intent);

                                    //取代 发广播的方式
                                    BaseBusEvent event = new BaseBusEvent();
                                    event.setEventType(BaseBusEvent.MARKET_NOTICE);
                                    event.setOther(dataBean);
                                    EventBus.getDefault().post(event);
                                }
                            }
                        }
                        break;
                }
            }
        });
    }
}
