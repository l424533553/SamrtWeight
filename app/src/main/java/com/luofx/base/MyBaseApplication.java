package com.luofx.base;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.luofx.utils.net.NetWorkJudge;
import com.luofx.listener.VolleyListener;
import com.luofx.listener.VolleyStringListener;
import com.luofx.utils.CharsetJsonRequest;
import com.luofx.utils.log.ErrorLog;
import com.luofx.utils.log.MyLog;

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
public class MyBaseApplication extends Application implements Thread.UncaughtExceptionHandler {

    String TAG = "网络不可用，请检查网络";

    /**
     * 先创建一个请求队列，因为这个队列是全局的，所以在Application中声明这个队列
     */
    private RequestQueue queues;
    private Context context;
    //  线程池  记得要关闭
    protected ExecutorService threadPool;

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
    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ErrorLog.errorLog(Thread.currentThread().getName() + "");
                ErrorLog.errorLog("Thread Id :" + t.getId());
                ErrorLog.errorLog("Thread Ex :" + e.toString());
                Looper.loop();
            }
        }).start();

        SystemClock.sleep(3000);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
