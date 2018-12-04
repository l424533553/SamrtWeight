package com.luofx.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.luofx.listener.VolleyListener;
import com.luofx.listener.VolleyStringListener;
import com.luofx.utils.CharsetJsonRequest;
import com.luofx.utils.log.MyLog;
import com.luofx.utils.net.NetWorkJudge;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明：
 * 作者：User_luo on 2018/7/24 13:52
 * 邮箱：424533553@qq.com
 * 需要导入Volley.jar 或者  远程依赖
 */
public class MyBaseApplication extends MultiDexApplication {

    String TAG = "网络不可用，请检查网络";

    /**
     * 先创建一个请求队列，因为这个队列是全局的，所以在Application中声明这个队列
     */
    private RequestQueue queues;
    private Context context;
    //  线程池  记得要关闭
    protected ExecutorService threadPool;
    protected ExecutorService singleThread;

    public ExecutorService getSingleThread() {
        return singleThread;
    }

    //    ThreadPoolExecutor
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public RequestQueue getQueues() {
        return queues;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        queues = Volley.newRequestQueue(getApplicationContext());
        threadPool = Executors.newFixedThreadPool(4);
        singleThread = Executors.newSingleThreadExecutor();
//        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //关闭线程池
        threadPool.shutdown();
    }

    /**
     * Volley Get 请求方式
     *
     * @param url      网址
     * @param listener 监听请求
     * @param flag     旗标
     */
    public void volleyGet(String url, final VolleyListener listener, final int flag) {
        if (NetWorkJudge.isNetworkAvailable(this)) {
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
            getQueues().add(request);
        } else {
//            Toast.makeText(context, TAG, Toast.LENGTH_SHORT).show();
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
            getQueues().add(request);
        } else {
            Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Volley Post请求方式
     *
     * @param url            网络地址
     * @param map            post请求参数
     * @param volleyListener 监听接口
     */
    public void volleyPost1(String url, final Map<String, String> map, final VolleyListener volleyListener, final int flag) {
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

            MyLog.logTest(request.toString());
            getQueues().add(request);

        } else {
//            Toast.makeText(context, TAG, Toast.LENGTH_SHORT).show();
        }
    }


    public void volleyPost2(String url, JSONObject jsonRequest, final VolleyListener volleyListener, final int flag) {
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

            MyLog.logTest(request.toString());
            getQueues().add(request);

        } else {
//            Toast.makeText(context, TAG, Toast.LENGTH_SHORT).show();
        }
    }


    public void volleyPost(String url, final Map<String, String> map, final VolleyStringListener volleyListener, final int flag) {
        if (NetWorkJudge.isNetworkAvailable(this)) {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    volleyListener.onResponse(response, flag);

//                    MyLog.logTest(response);
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

//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> headers = new HashMap<>();
////                    headers.put("Charset", "UTF-8");
//                    headers.put("Content-Type", "application/json");
////                    headers.put("Accept-Encoding", "gzip,deflate");
//                    return headers;
//                }
            };


//            CharsetJsonRequest request = new CharsetJsonRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    volleyListener.onResponse(jsonObject, flag);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyListener.onResponse(volleyError, flag);
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    return map;
//                }
//            };

            getQueues().add(stringRequest);

        } else {
//            Toast.makeText(context, TAG, Toast.LENGTH_SHORT).show();
        }
    }

    public void volleyPostString(String url, final Map<String, String> map, final VolleyStringListener volleyListener, final int flag) {
        if (NetWorkJudge.isNetworkAvailable(this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    volleyListener.onResponse(response, flag);

//                    MyLog.logTest(response);
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
            };


//            CharsetJsonRequest request = new CharsetJsonRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    volleyListener.onResponse(jsonObject, flag);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyListener.onResponse(volleyError, flag);
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    return map;
//                }
//            };

            getQueues().add(stringRequest);

        } else {
//            Toast.makeText(context, TAG, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取到 未捕获的异常
     *
     * @param t 线程
     * @param e 异常
     */
//    @Override
//    public void uncaughtException(final Thread t, final Throwable e) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                ErrorLog.errorLog(Thread.currentThread().getName() + "");
//                ErrorLog.errorLog("Thread Id :" + t.getId());
//                ErrorLog.errorLog("Thread Ex :" + e.toString());
//                Looper.loop();
//            }
//        }).start();
//
//        SystemClock.sleep(3000);
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }


}


//{"status":0,"msg":"ok",
//        "data":{"licence":"ups\/uploads\/file\/20181114\/B018_\u526f\u672c.jpg;",
//        "ad":"ups\/uploads\/file\/20181120\/default3.jpg" +
//        ";ups\/uploads\/file\/20181120\/default2.jpg;" +
//        "ups\/uploads\/file\/20181120\/default1.jpg;","photo":"assets\/files\/20181122102525919.jpg",
//        "companyno":"A066","introduce":"\u852c\u83dc\u6863","adcontent":"\u6d4b\u8bd5 \u76ae\u4e00\u4e0b",
//        "companyname":"\u80e1\u542f\u57ce","linkphone":"15818546414","companyid":"1126","status":"0",
//        "baseurl":"https:\/\/data.axebao.com\/smartsz\/"}}
//
//
//        https://data.axebao.com/smartsz/assets/files/20181122102525919.jpg