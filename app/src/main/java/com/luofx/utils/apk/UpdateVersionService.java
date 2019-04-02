package com.luofx.utils.apk;//package com.luofx.utils.apk;//package com.coolshow.mybmobtest.luofx.utils.apk;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.view.View;
//import android.widget.RemoteViews;
//
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.HttpHandler;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.luofx.utils.MyPreferenceUtils;
//import com.luofx.utils.UpdateVersionUtil;
//import com.xuanyuan.library.MyLog;
//import com.shangtongyin.serialport.R;
//
//import java.io.File;
//import java.lang.reflect.Method;
//
///**
// * @author wenjie
// * 下载新版本的服务类
// * 需要使用到jar包   'com.jiechic.library:xUtils:2.6.14'
// */
//public class UpdateVersionService extends Service {
//
//
//    private NotificationManager nm;
//    private Notification notification;
//    //标题标识
//    private int titleId = 0;
//    //安装文件
//    private File updateFile;
//
//    private static HttpHandler<File> httpHandler;
//
//    private HttpUtils httpUtils;
//
//    private long initTotal = 0;//文件的总长度
//
//
//    private Context context;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        MyLog.myInfo("开启服务");
//
//        httpUtils = new HttpUtils();
//
//        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notification = new Notification();
//        notification.icon = R.mipmap.icon;
//        notification.tickerText = "开始下载";
//        notification.when = System.currentTimeMillis();
//        notification.contentView = new RemoteViews(getPackageName(), R.layout.notifycation);
//        context = this.getApplicationContext();
//
//
//    }
//
//    String apkName;
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        MyLog.myInfo("开启服务+onStartCommand");
//
//        if (intent != null) {
//            Bundle bundle = intent.getExtras();
//            String url = bundle.getString("downloadUrl");
//            apkName = bundle.getString("apkName");
//            updateFile = new File(UpdateVersionUtil.getDownDir(this) + "/" + apkName);
//
//
//            if (updateFile.exists()) {
//                MyLog.logTest("存在");
//
//            } else {
//                MyLog.logTest("不存在");
//            }
//
/////storage/emulated/0/Download/TraceBack-v1.0.apk
//
//
//            MyPreferenceUtils.setString(UpdateVersionService.this, "apkDownloadurl", url);
//            nm.notify(titleId, notification);
//            downLoadFile(url);
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//
//    public void downLoadFile(String url) {
//        httpHandler = httpUtils.download(url, updateFile.getAbsolutePath(), true, false, new RequestCallBack<File>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<File> response) {
//                // 更改文字
//                notification.contentView.setTextViewText(R.id.msg, "下载完成!点击安装");
////                notification.contentView.setViewVisibility(R.id.btnStartStop, View.GONE);
////                notification.contentView.setViewVisibility(R.id.btnCancel,View.GONE);
//                // 发送消息
//                nm.notify(0, notification);
//                stopSelf();
//                //收起通知栏
//                collapseStatusBar(UpdateVersionService.this);
//                //自动安装新版本
//
//                //TODO
//                if (Build.VERSION.SDK_INT >= 26) {
//                    boolean b = getPackageManager().canRequestPackageInstalls();
//                    if (b) {
////                        mMainPresenter.installApk();
//                        MyLog.logTest("ceshi ");
//                    } else {
//                        //请求安装未知应用来源的权限
//                        MyLog.logTest("不行 ");
//
////                        ActivityCompat.requestPermissions(con, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 222);
//                    }
//
//                }
//
//                Intent installIntent = MyApkUtils.getInstallIntent(context, updateFile);
//                startActivity(installIntent);
//
//
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                //网络连接错误
//                if (error.getExceptionCode() == 0) {
//                    // 更改文字
//                    notification.contentView.setTextViewText(R.id.msg, "网络异常！请检查网络设置！");
//                } else if (error.getExceptionCode() == 416) {//文件已经下载完毕
//                    // 更改文字
//                    notification.contentView.setTextViewText(R.id.msg, apkName);
//                    // 更改文字
//                    notification.contentView.setTextViewText(R.id.bartext, "检测到新版本已经下载完成，点击即安装!");
//                    // 隐藏进度条
//                    notification.contentView.setViewVisibility(R.id.progressBar1, View.GONE);
//
//                    Intent intent = MyApkUtils.getInstallIntent(context, updateFile);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(UpdateVersionService.this, 0, intent, 0);
//                    notification.flags = Notification.FLAG_AUTO_CANCEL;//点击通知栏之后 消失
//                    notification.contentIntent = pendingIntent;//启动指定意图
//                }
//                // 发送消息
//                stopSelf();
//                nm.notify(0, notification);
//            }
//
//            @Override
//            public void onLoading(long total, long current, boolean isUploading) {
//                if (initTotal == 0) {//说明第一次开始下载
//                    initTotal = total;
//                }
//
//                if (initTotal != total) {//说明下载过程中暂停过，文件的总长度出现问题  就把初始的文件的长度赋值给他重新计算已经下载的比例
//                    total = initTotal;
//                }
//
//                long l = current * 100 / total;
//                notification.contentView.setTextViewText(R.id.msg, "正在下载：" + apkName);
//                // 更改文字
//                notification.contentView.setTextViewText(R.id.bartext, l + "%");
//                // 更改进度条
//                notification.contentView.setProgressBar(R.id.progressBar1, 100, (int) l, false);
//                // 发送消息
//                nm.notify(0, notification);
//
////              Intent intent = new Intent();
////				intent.setAction("cancel");
////				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
////				notification.contentView.setOnClickPendingIntent(R.id.btnStartStop, pendingIntent);
//
//            }
//
//            @Override
//            public void onStart() {
//                notification.contentView.setTextViewText(R.id.msg, "开始下载：" + apkName);
//                nm.notify(titleId, notification);
//            }
//
//        });
//    }
//
//
//    /**
//     * 收起通知栏
//     *
//     * @param context
//     */
//    public static void collapseStatusBar(Context context) {
//        try {
//            Object statusBarManager = context.getSystemService("statusbar");
//            Method collapse;
//            if (Build.VERSION.SDK_INT <= 16) {
//                collapse = statusBarManager.getClass().getMethod("collapse");
//            } else {
//                collapse = statusBarManager.getClass().getMethod("collapsePanels");
//            }
//            collapse.invoke(statusBarManager);
//        } catch (Exception localException) {
//            localException.printStackTrace();
//        }
//    }
//
//    public static HttpHandler<File> getHandler() {
//        return httpHandler;
//    }
//
//
//    @Override
//    public void onDestroy() {
//        //下载完成时，清楚该通知，自动安装
//        nm.cancel(titleId);
////        System.out.println("UpdateVersionService----onDestroy");
////		try {
////			GdmsaecApplication.db.deleteAll(VersionInfo.class);
////		} catch (DbException e) {
////			e.printStackTrace();
////		}
//        super.onDestroy();
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//}
