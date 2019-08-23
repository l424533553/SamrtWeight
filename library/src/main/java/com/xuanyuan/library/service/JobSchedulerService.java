package com.xuanyuan.library.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.xuanyuan.library.MyLog;

import java.util.concurrent.TimeUnit;

/**
 * 作者：罗发新
 * 时间：2019/4/25 0025    星期四
 * 邮件：424533553@qq.com
 * 说明： JobService实际运行在主线程中，不能做耗时操作
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    private int count;

    @Override
    public boolean onStartJob(JobParameters params) {
        MyLog.log("onStartJob:" + params.getJobId());
        Toast.makeText(this, "start job:" + params.getJobId(), Toast.LENGTH_SHORT).show();
//        jobFinished(params, false);//任务执行完后记得调用jobFinsih通知系统释放相关资源
        MyLog.log("onStartCount==:" + count);
        count++;
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        MyLog.log("onStopJob:" + params.getJobId());
        MyLog.log("onStopCount==:" + count);
        return false;
    }

    /**
     * 测试开启 JobService 的使用使用案例
     * 可以在Activity中直接调用 进行测试
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onMainTest(Context context) {
        int mJobId = 1;
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(context, JobSchedulerService.class);
        //通过JobInfo.Builder来设定触发服务的约束条件，最少设定一个条件
        JobInfo.Builder builder = new JobInfo.Builder(++mJobId, componentName);
        //0:默认，不需要网络连接   1：任何网络连接   2：不计量收费的网络连接
        //3：需要不漫游的网络连接  4：需要蜂窝网络连接
        int requiresNetState = 0;
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
//            builder.setRequiresDeviceIdle();//是否要求设备为idle状态, 即空闲状态下，基本不可能
        builder.setRequiresCharging(false);//是否要设备为充电状态
        // 设置时间线性
        builder.setBackoffCriteria(TimeUnit.MILLISECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//从24开始定时机制不一样
//        设置JobService执行的最小延时时间，不可和setPeriodic()方法共存
            builder.setMinimumLatency(5 * 1000);
//        设置JobService执行的最晚时间，不可和setPeriodic()方法共存
            builder.setOverrideDeadline(2 * 1000);
        } else {
            builder.setPeriodic(25 * 1000);
            builder.setPersisted(true);
        }
        scheduler.schedule(builder.build());
        MyLog.log("schedule job:" + mJobId);
        //TODO 下面屏蔽的是重点
//        //停止指定JobId的工作服务
//        scheduler.cancel(JOB_ID);
//        //停止全部的工作服务
//        scheduler.cancelAll();
    }
}
