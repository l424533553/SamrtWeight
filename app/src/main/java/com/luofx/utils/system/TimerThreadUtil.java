package com.luofx.utils.system;

import android.os.Handler;
import android.os.Message;

/**
 * author: luofaxin
 * date： 2018/11/5 0005.
 * email:424533553@qq.com
 * describe:时间 定时  类
 * 使用步骤：一、开启线程    二：在需要使用的地方进行接口回调
 */
public class TimerThreadUtil extends Thread {

    private static final int CURRENTDATETIME = 1;
    private final OnGetCurrentDateTimeListener listener;

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == CURRENTDATETIME) {
                if (listener != null) {
                    listener.onGetDateTime();
                }
            }
            return false;
        }
    });

    public TimerThreadUtil(OnGetCurrentDateTimeListener listener) {
        this.listener = listener;
    }

    boolean isRunning = true;

    public void closeThread() {
        isRunning = false;
    }

    @Override
    public void run() {
        super.run();
        while (isRunning) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.what = CURRENTDATETIME;
            mHandler.sendMessage(msg);
        }


//        do {
//
//        } while (true);
//

    }

    public interface OnGetCurrentDateTimeListener {
        void onGetDateTime();
    }
}
