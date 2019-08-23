package com.xuanyuan.library.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.xuanyuan.library.MyToast;


/**
 * 作者：罗发新
 * 时间：2019/4/25 0025    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public abstract class PermissionAppCompatActivity extends BaseAppCompatActivity  {

    /**
     * 是否已经权限授权
     */
//    private boolean isAllGranted;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    /**
     * 权限访问请求CODE
     */
    private static final int MY_PERMISSION_SET_REQUEST_CODE = 10010;
    private static final int REQUEST_CODE_APP_INSTALL = 10020;
    private static final int REQUEST_CODE_APP_OVERLAY = 10030;//悬浮框权限询问


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startAskPermissions();
    }

    /**
     * 开始询问权限
     */
    private void startAskPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();

            //根据需要确定   23 SDK 及6.0以后 想使用悬浮框，需要检查是否授权，检查的权限方式不一般
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_APP_OVERLAY);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0后  每个应用是否关联安装更新apk,需要手动控制
            boolean hasInstallPermission = isHasInstallPermissionWithO(this);
            if (!hasInstallPermission) {   // 如果没有未知应用安装权限,则需要手动开启
                MyToast.toastShort(context, "因您的设备安全限制，请手动允许安装未知应用");
                startInstallPermissionSettingActivity(this);
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }


    /**
     * 开启设置安装未知来源应用权限界面  8.0 以后需要请求未知安装
     *
     * @param context 上下文对象
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
    }


    private void checkPermissions() {
        String[] arr = new String[]{

        };

        /*
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(arr);
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (!isAllGranted) {
            /*
             * 第 2 步: 请求权限
             */
            // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
            ActivityCompat.requestPermissions(this, arr, MY_PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
//            this.isAllGranted = isAllGranted;
            if (!isAllGranted) {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }

    }

    /**
     * 打开 APP 的详情设置
     */

    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

//                startActivityForResult(intent,MY_PERMISSION_SET_REQUEST_CODE);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_PERMISSION_SET_REQUEST_CODE) {
            checkPermissions();
        }

        if (requestCode == REQUEST_CODE_APP_OVERLAY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {// 悬浮权限已经申请
                    startAskPermissions();

//                    WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
//                    params.format = PixelFormat.RGBA_8888;
//                    windowManager.addView(op, params);
                } else {
                    Toast.makeText(this, "ACTION_MANAGE_OVERLAY_PERMISSION权限已被拒绝", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
