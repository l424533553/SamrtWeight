package com.axecom.smartweight.my.rzl.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.android.volley.VolleyError;
import com.luofx.base.MyBaseApplication;
import com.luofx.listener.VolleyListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 说明：从com.luofx.base。apkROUND_UP中复制再改造的
 * by rzl
 */
public class ApkUtils implements VolleyListener {
    private static final String TAG = com.axecom.smartweight.my.rzl.utils.ApkUtils.class.getSimpleName();

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
     * 获取当前版本.
     *
     * @param context 上下文对象
     * @return 返回当前版本
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            return versionName + "." + versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//    public static String getVersionName(Context context) {
//        try {
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
//                    PackageManager.GET_CONFIGURATIONS);
//            int  versionCode = packageInfo.versionCode;
//            String versionName = packageInfo.versionName;
//            return versionName + "." + versionCode;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

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
     * 安装 程序
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void installUseAS(Context context, String filePath) {
        try {
            File apkFile = new File(filePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(com.axecom.smartweight.my.rzl.utils.ApkUtils.getInstance().ctx, "com.axecom.iweight.fileprovider", apkFile);//com.dtncpzs.traceback.fileprovider
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                Uri uri = Uri.fromFile(apkFile);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            }
            context.startActivity(intent);
        } catch (Exception ex) {
            Log.e("rzl", "install apk error:" + ex.getMessage());
        }
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

    //by rzl 2018-10-19
    //检查远程版本更新(每个市场有独立的版本，所以携带marketId参数)
    public static void checkRemoteVersion(int marketId, Context context, Handler handler) {
        if (isWorking) return;
        isWorking = true;
        String url = "https://data.axebao.com/api/smartsz/getvbymarketid?marketid=" + marketId;
        if (context != null) {
            if (context instanceof MyBaseApplication) {
                //把原来的广播侦听取消掉
                if (ApkUtils.getInstance().ctx != null) {
                    if (ApkUtils.getInstance().radio != null) {
                        ApkUtils.getInstance().ctx.unregisterReceiver(ApkUtils.getInstance().radio);
                    }
                }
                //把原来的文件清理掉
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                Log.i("rzl", "direcotry:" + directory.getAbsolutePath());

                File[] files = directory.listFiles();
                if (files != null) {
                    int c = 0;
                    for (File f : files) {
                        if (f.exists()) {
                            if (f.getName().endsWith(".apk")) {
                                c += f.delete() ? 1 : 0;
                            }
                        }
                    }
                    Log.e("rzl", c + " apk files has been cleared");
                }
                ApkUtils.getInstance().ctx = context;//用来获得旧版本信息和显示下载进度
                ApkUtils.getInstance().marketId = marketId;
                ApkUtils.getInstance().handler = handler;
                MyBaseApplication ma = (MyBaseApplication) context;
                ma.volleyGet(url, ApkUtils.getInstance(), 10010);//10010代表检查版本更新
            } else {
                Log.e("rzl", "CheckRemoteVersion error,context type must be MyBaseApplication");
                isWorking = false;
            }
        } else {
            Log.e("rzl", "CheckRemoteVersion error,listenr and context must not be null");
            isWorking = false;
        }
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        Log.e("rzl", "ApkUtil Error,flag:" + flag + ",content:" + volleyError.getMessage());
        isWorking = false;
    }


//    {"status":0,"msg":"ok","data":{"id":47,"name":"APP升级","version":"4.21","vtype":"","filepath":"http:\/\/data.axebao.com\/smartsz\/assets\/files\/20190117134333123.apk","description":"","date":"2019-01-17","marketid":11}}
    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        Log.i("rzl", "ApkUtil response,flag:" + flag + ",content:" + jsonObject.toString());
        if (flag == 10010) {//10010代表得到检查版本更新的反馈
            //{"status":0,"msg":"ok","data":{"id":2,"name":"\u667a\u80fd\u79e4APP\u7248\u672c\u5347\u7ea7","version":"19.2","filepath":"20181017144051204.txt","description":"","date":"2018-10-19","marketid":1}}
            if (jsonObject.has("status") && jsonObject.has("msg") && jsonObject.has("data")) {
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null) {
                        if (data.has("version")) {
                            remoteVersion = data.getString("version");
                        }
                        Log.i("rzl", "remote version:" + remoteVersion);
                        if (data.has("description")) {
                            remoteDescription = data.getString("description");
                        }
                        Log.i("rzl", "remote description:" + remoteDescription);
                        if (data.has("date")) {
                            remoteDate = data.getString("date");
                        }
                        if (data.has("filepath")) {
                            remoteApkPath = data.getString("filepath");
                        }
                        Log.i("rzl", "remote date:" + remoteDate);
                        if (this.ctx != null) {
                            String oldVersion = getVersionName(this.ctx);
                            float _oldVersion, _newVersion;
//                            _oldVersion = Float.parseFloat(oldVersion);
                            _newVersion = Float.parseFloat(remoteVersion);
                            if (remoteVersion.compareTo(oldVersion) > 0) {//开始下载
//                                if (_newVersion > _oldVersion) {//开始下载
                                Log.i("rzl", "begin downloading new apk," + remoteApkPath);
                                Message msg = Message.obtain();
                                msg.what = 10013;
                                Version v = new Version();
                                v.setDate(remoteDate);
                                v.setDescription(remoteDescription);//我不在的时候不要偷吃我的蔬菜和肉肉，因为我会很生气，后果很严重，你们到底听到了没有，记住，千万别偷吃我的东西
                                v.setVersion(_newVersion);
                                v.setMarketId(marketId);
                                v.setApkPath(remoteApkPath);
                                msg.obj = v;
                                handler.sendMessage(msg);//通知UI显示下载对话框
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(remoteApkPath + "?timestamp=" + Math.random()));
                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);//允许手机流量和wifi
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/smartWeight.apk");
                                //request.setDestinationInExternalFilesDir(this.ctx,Environment.getExternalStorageDirectory().toString(),"smartWeight.apk");
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);//在下载过程中通知栏是否会一直显示该下载的Notification
                                //设置通知信息
                                request.setTitle("智能秤");
                                request.setDescription("安鑫宝智能秤-可溯源、防作弊");
                                request.setVisibleInDownloadsUi(true);
                                request.allowScanningByMediaScanner();
                                request.setMimeType("application/vnd.android.package-archive");
                                //得到DownloadManager实例
                                final DownloadManager dm = (DownloadManager) this.ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                                downloadId = dm.enqueue(request);//在广播接收器中匹配该下载id并不停查询该下载任务的状态

                                //下载完成通知消息的操作系统过滤器
                                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                                dco = new DownloadChangeObserver(this.ctx, handler);//下载过程监控
                                radio = new BroadcastReceiver() {
                                    @Override
                                    public void onReceive(Context context, Intent intent) {
                                        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                        if (downloadId == id) {
                                            isWorking = false;
                                            ctx.getContentResolver().unregisterContentObserver(dco);//取消监控下载过程
                                            DownloadManager.Query query = new DownloadManager.Query();
                                            query.setFilterById(id);
                                            Cursor cs = dm.query(query);
                                            if (cs.moveToFirst()) {
                                                    /*int c=cs.getColumnCount();
                                                    for(int i=0;i<c;i++){
                                                        Log.i("rzl",i + ",key:" + cs.getColumnName(i)  + ",value:" + cs.getString(i));
                                                    }*/
                                                String fileName = cs.getString(cs.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                                                int downloadedBytes = cs.getInt(cs.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                                if (downloadedBytes > 0) {
                                                    if (fileName != null) {
                                                        Log.i("rzl", "will install:" + fileName);
                                                        File f = new File(fileName);
                                                        //Log.i("rzl","file exist? " + f.exists());
                                                        installUseAS(ctx, fileName);
                                                    }
                                                } else {
                                                    Log.i("rzl", "下载失败");
                                                    handler.sendEmptyMessage(10014);
                                                }
                                            }
                                            cs.close();
                                        }
                                    }
                                };
                                ctx.registerReceiver(radio, filter);//下载完成的广播
                                //下载过程监控进度
                                this.ctx.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, dco);
                            } else {
                                isWorking = false;
                            }
                        } else {
                            isWorking = false;
                        }
                    } else {
                        isWorking = false;
                    }

                } catch (JSONException ex) {
                    Log.e("rzl", "json array parse error in check apk version");
                    isWorking = false;
                } catch (NumberFormatException ex1) {
                    Log.e("rzl", "parse apk version to float error");
                    isWorking = false;
                }
            } else {
                isWorking = false;
            }
        } else {
            isWorking = false;
        }
    }

    //取消下载更新
    public void cancelDownloading() {
        //停止下载管理器
        if (this.ctx != null) {
            DownloadManager dm = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
            if (dm != null) {
                dm.remove(this.downloadId);
                Log.i("rzl", "cancel downloading apk successful,downloadId:" + this.downloadId);
            }
            //取消URI监控
            if (this.dco != null) {
                this.ctx.getContentResolver().unregisterContentObserver(this.dco);
                this.dco = null;
            }
            //取消下载完成广播
            if (this.radio != null) {
                this.ctx.unregisterReceiver(this.radio);
                this.radio = null;
            }
            this.ctx = null;
            this.downloadId = 0;
            handler = null;
            remoteVersion = "";
            remoteDescription = "";
            remoteDate = "";
            remoteApkPath = "";
            isWorking = false;
        }
    }

    public long getDownloadId() {
        return this.downloadId;
    }

    private static boolean isWorking = false;//是否正在工作-防止连续发起多次检查更新操作
    // private long downloadId;DownloadManager下载完后返回的一个下载id,自带的，每一个下载任务都会返回一个唯一的id，并且会发一条广播
    private Context ctx;//检查版本更新传入，显示进度画面、获得旧版本信息需要用到
    private int marketId;//市场编号
    private long downloadId;//下载任务id-唯一的
    private Handler handler;//用来向UI通知消息
    private DownloadChangeObserver dco;//监控URI内容变化并发出通知的contentObserver对象
    private BroadcastReceiver radio;//下载消息广播
    //  private Activity act;//用来安装apk用的
    private String remoteVersion;//新版本号
    private String remoteDescription;//新版本描述
    private String remoteDate;//新版本发布日期
    private String remoteApkPath;//远程apk路径
    //单例，实现了网络通讯功能
    @SuppressLint("StaticFieldLeak")
    private static com.axecom.smartweight.my.rzl.utils.ApkUtils myself;

    public static com.axecom.smartweight.my.rzl.utils.ApkUtils getInstance() {
        if (myself == null) {
            myself = new com.axecom.smartweight.my.rzl.utils.ApkUtils();
        }
        return myself;
    }
}
