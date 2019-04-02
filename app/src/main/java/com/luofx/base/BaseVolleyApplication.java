package com.luofx.base;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.axecom.smartweight.my.config.IConstants;
import com.luofx.listener.OkHttpListener;
import com.luofx.listener.VolleyListener;
import com.luofx.listener.VolleyStringListener;
import com.luofx.utils.CharsetJsonRequest;
import com.luofx.utils.net.NetWorkJudge;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES;

/**
 * 说明：
 * 作者：User_luo on 2018/7/24 13:52
 * 邮箱：424533553@qq.com
 * 需要导入Volley.jar 或者  远程依赖
 */
@SuppressLint("Registered")
public class BaseVolleyApplication extends BaseBuglyApplication implements IConstants {
    /**
     * 先创建一个请求队列，因为这个队列是全局的，所以在Application中声明这个队列
     */
    protected RequestQueue queues;

    public RequestQueue getQueues() {
        return queues;
    }



//    /**
//     * 初始化Bugly
//     */
//    private void initBugly() {
//        String APP_ID = "467fa8da1e";
//
//        /* **** Beta高级设置 *****/
//
//        // 设置是否开启热更新能力，默认为true
//        Beta.enableHotfix = true;
//        // 设置是否自动下载补丁
//        Beta.canAutoDownloadPatch = true;
//        // 设置是否提示用户重启
//        Beta.canNotifyUserRestart = false;
//        // 设置是否自动合成补丁
//        Beta.canAutoPatch = true;
//
//
//        /**
//         * true表示app启动自动初始化升级模块; false不会自动初始化;
//         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
//         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
//         */
//        Beta.autoInit = true;
//
//        /**
//         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
//         */
//        Beta.autoCheckUpgrade = true;
//
////        /**
////         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
////         */
////        Beta.upgradeCheckPeriod = 60 * 1000;
////        /**
////         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
////         */
////        Beta.initDelay = 1 * 1000;
////        /**
////         * 设置通知栏大图标，largeIconId为项目中的图片资源;
////         */
////        Beta.largeIconId = R.drawable.ic_launcher;
////        /**
////         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
////         */
////        Beta.smallIconId = R.drawable.ic_launcher;
////        /**
////         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
////         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
////         */
////        Beta.defaultBannerId = R.drawable.ic_launcher;
////        /**
////         * 设置sd卡的Download为更新资源保存目录;
////         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
////         */
//        Beta.storageDir = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
////        /**
////         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
////         */
////        Beta.showInterruptedStrategy = true;
////        /**
////         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
////         */
////        Beta.canShowUpgradeActs.add(MyBuglyActivity.class);
////
////        /***** Bugly高级设置 *****/
//        BuglyStrategy strategy = new BuglyStrategy();
//        /**
//         * 设置app渠道号
//         */
////        strategy.setAppChannel(APP_CHANNEL);
//
//
//        /**
//         *  全量升级状态回调
//         */
//        Beta.upgradeStateListener = new UpgradeStateListener() {
//            @Override
//            public void onUpgradeFailed(boolean b) {
//
//            }
//
//            @Override
//            public void onUpgradeSuccess(boolean b) {
//
//            }
//
//            @Override
//            public void onUpgradeNoVersion(boolean b) {
//                Toast.makeText(getApplicationContext(), "最新版本", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onUpgrading(boolean b) {
//                Toast.makeText(getApplicationContext(), "onUpgrading", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadCompleted(boolean b) {
//
//            }
//        };
//
//        /**
//         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
//         */
//        Beta.betaPatchListener = new BetaPatchListener() {
//            @Override
//            public void onPatchReceived(String patchFileUrl) {
//                Toast.makeText(getApplicationContext(), patchFileUrl, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadReceived(long savedLength, long totalLength) {
//                Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(),
//                        "%s %d%%",
//                        Beta.strNotificationDownloading,
//                        (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadSuccess(String patchFilePath) {
//                Toast.makeText(getApplicationContext(), patchFilePath, Toast.LENGTH_SHORT).show();
////                Beta.applyDownloadedPatch();
//            }
//
//            @Override
//            public void onDownloadFailure(String msg) {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onApplySuccess(String msg) {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onApplyFailure(String msg) {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPatchRollback() {
//                Toast.makeText(getApplicationContext(), "onPatchRollback", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//
//        /***** 统一初始化Bugly产品，包含Beta *****/
//        Bugly.init(getApplicationContext(), APP_ID, false, strategy);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
//        initBugly();
    }

    /**
     * 获取硬件信息
     */
//    private void getHardwareInfo() {
//        TelephonyManager phone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        @SuppressLint("HardwareIds")
//        String mac = wifi.getConnectionInfo().getMacAddress();   // mac 地址
//        int phonetype = phone.getPhoneType();  //  手机类型
//        String model = Build.MODEL; // ****  手机型号
//        String sdk = String.valueOf(Build.VERSION.SDK_INT); // 系统版本值
//
//        String brand = Build.BRAND;
//        String release = phone.getDeviceSoftwareVersion();// 系统版本 ,
//        int networktype = phone.getNetworkType();   // 网络类型
//        String networkoperatorname = phone.getNetworkOperatorName();   // 网络类型名
//        String radioVersion = Build.getRadioVersion();   // 固件版本
//
//        // 数据
//        Deviceinfo deviceinfo = new Deviceinfo(release, sdk, brand, model, networkoperatorname, networktype, phonetype, mac, radioVersion);
//        DeviceInfoDao deviceInfoDao = new DeviceInfoDao(context);
//        deviceInfoDao.insert(deviceinfo);
//        setDeviceinfo(deviceinfo);
//    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * Volley Get 请求方式
     *
     * @param url      网址
     * @param listener 监听请求
     * @param flag     旗标
     */
    public void volleyGet(String url, final VolleyListener listener, final int flag) {
        if (NetWorkJudge.isNetworkAvailable(getApplicationContext())) {
            CharsetJsonRequest request = new CharsetJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    listener.onResponse(jsonObject, flag);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    listener.onResponse(volleyError, flag);
                }
            });
            // 设置Volley超时重试策略
            request.setRetryPolicy(new DefaultRetryPolicy(5000, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getQueues().add(request);
        }
    }

    /**
     * Volley Get 请求方式
     *
     * @param url      网址
     * @param listener 监听请求
     * @param flag     旗标
     */
    public void volleyStringGet(String url, final VolleyStringListener listener, final int flag) {
        if (NetWorkJudge.isNetworkAvailable(this)) {
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    listener.onResponse(response, flag);

//                    MyLog.logTest(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onResponseError(error, flag);
                }
            });
            // 设置Volley超时重试策略
            request.setRetryPolicy(new DefaultRetryPolicy(5000, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getQueues().add(request);
        }
    }

    /**
     * Volley Post请求方式
     *
     * @param url            网络地址
     * @param map            post请求参数
     * @param volleyListener 监听接口
     */
    public void volleyPost(String url, final Map<String, String> map, final VolleyListener volleyListener, final int flag) {
        if (NetWorkJudge.isNetworkAvailable(this)) {
            CharsetJsonRequest request = new CharsetJsonRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    volleyListener.onResponse(jsonObject, flag);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyListener.onResponse(volleyError, flag);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return map;
                }
            };
            // 设置Volley超时重试策略
            request.setRetryPolicy(new DefaultRetryPolicy(5000, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getQueues().add(request);
        }
    }

    public void volleyPost(String url, JSONObject jsonRequest, final VolleyListener volleyListener, final int flag) {
        if (NetWorkJudge.isNetworkAvailable(this)) {
            CharsetJsonRequest request = new CharsetJsonRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    volleyListener.onResponse(jsonObject, flag);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyListener.onResponse(volleyError, flag);
                }
            });
//            RequestBody body = RequestBody.create(MEDia_MEDIA_TYPE_JSON, json);
            // 设置Volley超时重试策略
            request.setRetryPolicy(new DefaultRetryPolicy(5000, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getQueues().add(request);
        }
    }

    public void volleyPostString(String url, final Map<String, String> map, final VolleyStringListener volleyListener, final int flag) {
        if (NetWorkJudge.isNetworkAvailable(this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    volleyListener.onResponse(response, flag);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyListener.onResponseError(error, flag);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return map;
                }
                //设定 头部 文件
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> headers = new HashMap<>();
////                    headers.put("Charset", "UTF-8");
//                    headers.put("Content-Type", "application/json");
////                    headers.put("Accept-Encoding", "gzip,deflate");
//                    return headers;
//                }
            };

            // 设置Volley超时重试策略  ,  当次数 为零时就是不试 ，当为一时就是 可有两次
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getQueues().add(stringRequest);
        }
    }

    /**
     * 使用okhttp的 方式请求  ，多数用 POST请求
     *
     * @param url 请求地址
     */
    public void okHttpPost(String url, String stringBody, final OkHttpListener okHttpListener) {
        //1.创建OkHttpClient对象
        final OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, stringBody);
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).method("POST", body).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.同步调用会阻塞主线程,这边在子线程进行
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                okHttpListener.onFailure(call, e);
            }

            /**
             * @param response  返回结果
             */
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) {
                // 注：该回调是子线程，非主线程
//                ResultInfo resultInfo = com.alibaba.fastjson.JSON.parseObject(response.body().string(), ResultInfo.class);
                okHttpListener.onResponse(call, response);
            }
        });
    }
}
