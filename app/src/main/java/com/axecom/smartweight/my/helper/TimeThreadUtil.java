package com.axecom.smartweight.my.helper;

import android.os.Handler;
import android.os.Message;

/**
 * author: luofaxin
 * date： 2018/11/5 0005.
 * email:424533553@qq.com
 * describe:
 */
public class TimeThreadUtil extends Thread {


    private static final int CURRENTDATETIME = 1;
    private static TimeThreadUtil timeThreadUtil;
    private OnGetCurrentDateTimeListener listener;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CURRENTDATETIME:
                    if (listener != null) {
                        listener.onGetDateTime();
                    }
                    break;
            }
            return false;
        }
    });

    public TimeThreadUtil(OnGetCurrentDateTimeListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        do {
            try {
                Thread.sleep(1000);
                Message msg = new Message();
                msg.what = CURRENTDATETIME;
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    public interface OnGetCurrentDateTimeListener {
        void onGetDateTime();
    }
}
