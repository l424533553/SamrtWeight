package com.xuanyuan.library.apk_update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 说明：获取app相关信息的工具类
 * 1. VersionCode  只可能是整数 ;  VersionName  是String，但一般设计为1.1这样Float 样的数据
 * 作者：User_luo on 2018/6/12 16:44
 * 邮箱：424533553@qq.com
 */
public class MyApkUtils {
    private static final String TAG = MyApkUtils.class.getSimpleName();

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 当前程序的版本号
     */
    public static int getVersionCode(Context context) {
        int version;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            version = 0;
        }
        return version;
    }

    /**
     * 得到安装的intent
     */
    public static Intent getInstallIntent(Context context, File apkFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        Uri uri;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities

//            File file=new File(apkFile.getAbsolutePath());
//            if(file.exists()){
//                MyLog.logTest("存在");
//
//            }else {
//                MyLog.logTest("不存在");
//            }

            uri = FileProvider.getUriForFile(context, "com.dtncpzs.traceback.fileprovider", apkFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {
            uri = Uri.fromFile(new File(apkFile.getAbsolutePath()));
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    public void readyInstall() {

    }

//    /**
//     * 安装 程序
//     * @param context
//     * @param filePath
//     */
//    public static void installUseAS(Context context, String filePath) {
//        File apkFile = new File(filePath);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //判断是否是AndroidN以及更高的版本
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(context, "com.dtncpzs.traceback.fileprovider", apkFile);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            Uri uri = Uri.fromFile(apkFile);
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        context.startActivity(intent);
//    }


    /***
     * 安装 apk 程序
     * SDK >=24以后 不允许直接暴露uri  ，26以后安装程序 需要 手动开 允许安装未知 应用
     * @param authority  "com.collect.user_luo.mycollect.fileprovider"  需要在配置文件中有对应的配置
     * @param fileOrPath 传apk 保存的文件 或文件地址
     */
    public static void installApp(Context context, @NonNull String authority, @NonNull Object fileOrPath) {
        File file = null;
        if (fileOrPath instanceof File) {
            file = (File) fileOrPath;
        } else if (fileOrPath instanceof String) {
            file = new File((String) fileOrPath);
        }

        if (file == null) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, authority, file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    public static String getState() {
        return Environment.getExternalStorageState();
    }

    /**
     * SD卡是否可用
     *
     * @return 只有当SD卡已经安装并且准备好了才返回true
     */
    public static boolean isAvailable() {
        return getState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡的根目录
     *
     * @return null：不存在SD卡
     */
    public static File getRootDirectory() {
        return isAvailable() ? Environment.getExternalStorageDirectory() : null;
    }

}
