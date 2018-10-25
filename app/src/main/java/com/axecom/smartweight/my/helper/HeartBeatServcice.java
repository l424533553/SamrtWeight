package com.axecom.smartweight.my.helper;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.axecom.smartweight.base.SysApplication;
import com.luofx.listener.VolleyListener;
import com.luofx.utils.log.MyLog;

import org.json.JSONObject;

/**
 * author: luofaxin
 * date： 2018/9/11 0011
 * email:424533553@qq.com
 * describe:
 */
public class HeartBeatServcice extends Service implements VolleyListener {
    private boolean isLooper = true; //是否需要循环
    /**
     * 定义我们自己写的Binder对象
     */
    private HttpHelper httpHelper;
    private int marketid;   // 市场编号
    private int terid;     // 秤/编号
    private int NOTIFY = 555;

    public void setMarketid(int marketid) {
        this.marketid = marketid;
    }

    public void setTerid(int terid) {
        this.terid = terid;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    private Handler handler;

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == NOTIFY) {
                    httpHelper.upState(marketid, terid, 0, 1);  // scalesId 秤的编号     1 正常    1 请求索引
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SysApplication application = (SysApplication) this.getApplication();
        
        initHandler();
        httpHelper = new HttpHelper(this, application);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isLooper) {
                    try {
                        Thread.sleep(120000);//2分钟
//                        MyLog.bluelog("心跳信息1234567");
                        handler.sendEmptyMessage(NOTIFY);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        isLooper = false;
        super.onDestroy();

    }

    @Override
    public void unbindService(ServiceConnection conn) {
        isLooper = false;
        super.unbindService(conn);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isLooper = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        MyLog.myInfo("错误" + volleyError.getMessage());

    }

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        MyLog.myInfo("成功" + jsonObject.toString());
    }

    /**
     * 自定义的内部类,继承Binder
     * 在该内部类里,我们写对服务进行操作的方法
     * 通过该对象实现对服务的操作,并从服务中获取数据
     */
    public class MyBinder extends Binder {
        public HeartBeatServcice getService() {
            return HeartBeatServcice.this;
        }
    }

    //
}
