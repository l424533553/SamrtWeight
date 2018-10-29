package com.luofx.utils.thread;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author: luofaxin
 * date： 2018/10/25 0025.
 * email:424533553@qq.com
 * describe:
 */
public class MyTimeTask {
    private Timer timer;
    private TimerTask task;
    private long time;
    private long delayTime;

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
}
