//package com.axecom.smartweight.my.service;
//
//import android.annotation.TargetApi;
//import android.app.ActivityManager;
//import android.app.job.JobInfo;
//import android.app.job.JobParameters;
//import android.app.job.JobScheduler;
//import android.app.job.JobService;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//
//import java.util.List;
//
///**
// * author: luofaxin
// * date： 2018/11/7 0007.
// * email:424533553@qq.com
// * describe:  打开 该服务 需要在5.0及以上 才能打开
// *  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
// *  //版本必须大于5.0
// *  startService(new Intent(getApplicationContext(), JobWakeUpService.class));
// *  }
// */
//
//@TargetApi(Build.VERSION_CODES.LOLLIPOP)
//public class JobWakeUpService extends JobService {
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        //开启轮寻
//        int JobWakeUpId = 1;
//        JobInfo.Builder mJobBulider = new JobInfo.Builder(
//                JobWakeUpId, new ComponentName(this, JobWakeUpService.class));
//        //设置轮寻时间
//        mJobBulider.setPeriodic(2000);
//        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        if (mJobScheduler != null) {
//            mJobScheduler.schedule(mJobBulider.build());
//        }
//        return START_STICKY;
//    }
//
//    @Override
//    public boolean onStartJob(JobParameters params) {
//        //开启定时任务 定时轮寻 判断应用Service是否被杀死
//        //如果被杀死则重启Service
//        boolean messageServiceAlive = serviceAlive(CarouselService.class.getName());
//        if (!messageServiceAlive) {// 如果图片服务死掉了，则重开服务
//            startService(new Intent(this, CarouselService.class));
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters params) {
//        return false;
//    }
//
//    /**
//     * 判断某个服务是否正在运行的方法
//     *
//     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
//     * @return true代表正在运行，false代表服务没有正在运行
//     */
//    private boolean serviceAlive(String serviceName) {
//        boolean isWork = false;
//        ActivityManager myAM = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        if (myAM != null) {
//            List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
//            if (myList.size() <= 0) {
//                return false;
//            }
//            for (int i = 0; i < myList.size(); i++) {
//                String mName = myList.get(i).service.getClassName();
//                if (mName.equals(serviceName)) {
//                    isWork = true;
//                    break;
//                }
//            }
//        }
//        return isWork;
//    }
//
//}
