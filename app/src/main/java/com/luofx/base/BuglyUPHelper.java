package com.luofx.base;

import android.content.Context;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.xuanyuan.xinyu.MyToast;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;


public class BuglyUPHelper {
    private static final String TAG = "OnUILifecycle123456";//升级的 生命周期
    private static final boolean DEBUG_MODE = false;
     static final boolean COMMON_DEBUG_MODE = false;//普通的调试模式，不需要Tinker功能
    private  static final String APP_ID = "01cd5b631b";

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void initBugly(Context context) {
        Bugly.init(context, APP_ID, DEBUG_MODE);
        this.context = context;
    }

    /***** Bugly高级设置 ,带渠道的设置*****/
//    BuglyStrategy strategy = new BuglyStrategy();
//    // 设置app渠道号
//    strategy.setAppChannel(APP_CHANNEL);
    public void initBugly(Context context, BuglyStrategy strategy) {
        Bugly.init(context, APP_ID, DEBUG_MODE, strategy);
        this.context = context;
    }

    /**
     * 手动检查更新（用于设置页面中检测更新按钮的点击事件）
     * UP SDk的核心 API之一
     */
    public void checkUpgrade() {
        Beta.checkUpgrade();
    }

    /**
     * @param isManual  用户手动点击检查true，非用户点击操作请传false
     * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
     *                  UP SDk的核心 API之一
     */
    public void checkUpgrade(boolean isManual, boolean isSilence) {
        Beta.checkUpgrade(isManual, isSilence);
    }

    /**
     * 获取本地已有升级策略（非实时，可用于界面红点展示）
     *
     * @return 获取升级信息 ，如果信息不为空
     * StringBuilder info = new StringBuilder();
     * info.append("id: ").append(upgradeInfo.id).append("\n");
     * info.append("标题: ").append(upgradeInfo.title).append("\n");
     * info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
     * info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
     * info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
     * info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
     * info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
     * info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
     * info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
     * info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
     * info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
     * info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
     * info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType);
     * info.append("图片地址：").append(upgradeInfo.imageUrl);
     */
    public static synchronized UpgradeInfo getUpgradeInfo() {
        return Beta.getUpgradeInfo();
    }


    /**
     * 限制策略，可进行性能优化
     */
    public void setStrictMode() {
        if (DEBUG_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .permitAll()
                    .detectNetwork()   // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
                    .penaltyLog()   //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects() //探测SQLite数据库操作
                    .penaltyLog()  //打印logcat
                    .penaltyDeath()
                    .detectAll()
                    .build());
        }
    }


    /**
     * 设置升级的 配置参数 ,需要在初始Init之后,否则可能无效
     */
    public void initUpdateConfig() {
        /**** Beta高级设置  升级配置*****/
        /**
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用  Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 2 * 1000;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
//        Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = R.mipmap.upload;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = R.mipmap.no_upload;

        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
//        Beta.canShowUpgradeActs.add(MainActivity.class);

        /**
         * 如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。
         */
        Beta.enableNotification = true;
        /**
         * 如果你想在Wifi网络下自动下载，可以将这个接口设置为true，默认值为false。
         */
        Beta.autoDownloadOnWifi = true;
        /**
         * 如果你使用我们默认弹窗是会显示apk信息的，如果你不想显示可以将这个接口设置为false。
         */
        Beta.canShowApkInfo = true;

        /**
         * 升级SDK默认是开启热更新能力的，如果你不需要使用热更新，可以将这个接口设置为false。
         */
        Beta.enableHotfix = true;
    }

    /**
     * 设置弹框的生命周期监听
     * 注意》》 设定在 Bugly.init() 前面否则不奏效
     * 自定义更新 弹框  ， 第二种方式 是自定义 Activity
     */
    public void setUILifecycleListener() {
        /**
         *  设置自定义升级对话框UI布局
         *  注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
         *  标题：beta_title，如：android:tag="beta_title"
         *  升级信息：beta_upgrade_info  如： android:tag="beta_upgrade_info"
         *  更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"
         *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
         *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         *  详见layout/upgrade_dialog.xml
         */
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;

        /**
         * 设置自定义tip弹窗UI布局  顶部下载的布局
         * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
         *  标题：beta_title，如：android:tag="beta_title"
         *  提示信息：beta_tip_message 如： android:tag="beta_tip_message"
         *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
         *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         *  详见layout/tips_dialog.xml
         */
        Beta.tipsDialogLayoutId = R.layout.tips_dialog;

        /**
         *  如果想监听升级对话框的生命周期事件，可以通过设置OnUILifecycleListener接口
         *  回调参数解释：
         *  context - 当前弹窗上下文对象
         *  view - 升级对话框的根布局视图，可通过这个对象查找指定view控件
         *  upgradeInfo - 升级信息
         */
        //不知道为什么 调用不起作用
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onCreate");
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(final Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onResume");
                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:

                // 通过id方式获取控件，并更改imageview图片
                ImageView imageView = view.findViewById(R.id.imageview);
                imageView.setImageResource(R.drawable.logo);

                // 通过tag方式获取控件，并更改布局内容
                TextView textView = view.findViewWithTag("textview");
                textView.setText("升级更新");

                // 更多的操作：比如设置控件的点击事件
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyToast.toastShort(context, "点击了图片控件");
                    }
                });
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStop");
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onDestory");
            }
        };
    }

}
