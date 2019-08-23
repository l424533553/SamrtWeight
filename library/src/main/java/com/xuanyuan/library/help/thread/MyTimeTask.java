package com.xuanyuan.library.help.thread;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.ALARM_SERVICE;

/**
 * author: luofaxin
 * date： 2018/10/25 0025.
 * email:424533553@qq.com
 * describe:
 */
public class MyTimeTask {
    private Timer timer;
    private final TimerTask task;
    private final long time;
    private final long delayTime;

    public MyTimeTask(long delayTime, long time, TimerTask task) {
        this.task = task;
        this.time = time;
        this.delayTime = delayTime;
        if (timer == null) {
            timer = new Timer();
        }
    }

    public void start() {
        timer.schedule(task, delayTime, time);//每隔time时间段就执行一次
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            if (task != null) {
                task.cancel();  //将原任务从队列中移除
            }
        }
    }

    /**
     * @param startTime 定时任务的开始时间
     */
    private void timingTask(Context context, long startTime, Class clazz) {
        AlarmManager aManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, clazz);
        intent.setAction("111111");
        // 创建PendingIntent对象
        PendingIntent pi = PendingIntent.getActivity(
                context, 0, intent, 0);
        // 设置AlarmManager将在Calendar对应的时间启动指定组件
        aManager.set(AlarmManager.RTC_WAKEUP, startTime, pi);
    }

}
