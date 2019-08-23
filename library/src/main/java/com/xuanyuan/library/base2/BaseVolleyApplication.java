package com.xuanyuan.library.base2;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xuanyuan.library.help.CharsetJsonRequest;
import com.xuanyuan.library.listener.OkHttpListener;
import com.xuanyuan.library.listener.VolleyListener;
import com.xuanyuan.library.listener.VolleyStringListener;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * 说明：
 * 作者：User_luo on 2018/7/24 13:52
 * 邮箱：424533553@qq.com
 * 需要导入Volley.jar 或者  远程依赖
 */
@SuppressLint("Registered")
public class BaseVolleyApplication extends BaseBuglyApplication {
    /**
     * 先创建一个请求队列，因为这个队列是全局的，所以在Application中声明这个队列
     */
    RequestQueue queues;

    private RequestQueue getQueues() {
        return queues;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    /**
     * Volley Get 请求方式
     *
     * @param url      网址
     * @param listener 监听请求
     * @param flag     旗标
     */
    public void volleyGet(String url, final VolleyListener listener, final int flag) {
        if (MyNetWorkUtils.isNetworkAvailable(getApplicationContext())) {
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
//            request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        if (MyNetWorkUtils.isNetworkAvailable(this)) {
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    listener.onStringResponse(response, flag);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onStringResponse(error, flag);
                }
            });
            // 设置Volley超时重试策略
//            request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        if (MyNetWorkUtils.isNetworkAvailable(this)) {
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
//            request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getQueues().add(request);
        }
    }

    public void volleyPost(String url, JSONObject jsonRequest, final VolleyListener volleyListener, final int flag) {
        if (MyNetWorkUtils.isNetworkAvailable(this)) {
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
            request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getQueues().add(request);
        }
    }

    public void volleyPostString(String url, final Map<String, String> map, final VolleyStringListener volleyListener, final int flag) {
        if (MyNetWorkUtils.isNetworkAvailable(this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    volleyListener.onStringResponse(response, flag);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyListener.onStringResponse(error, flag);
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
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        call.enqueue(new okhttp3.Callback() {
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
