package com.xuanyuan.library.apk_update.download;

import android.support.annotation.NonNull;

import com.xuanyuan.library.retrofit.DownloadCallBack;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Description: 带进度 下载  拦截器
 * Created by jia on 2017/11/30.
 * 人之所以能，是相信能
 *
 * 观察，修改以及可能短路的请求输出和响应请求的回来。
 * 通常情况下拦截器用来添加，移除或者转换请求或者回应的头部信息
 */
public class JsDownloadInterceptor implements Interceptor {

    private final DownloadCallBack downloadListener;

    public JsDownloadInterceptor(DownloadCallBack downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new JsResponseBody(response.body(), downloadListener)).build();
    }
}
