package com.luofx.listener;//package com.luofx.listener;

import android.support.annotation.NonNull;

import java.io.IOException;

public interface OkHttpListener {

    /**
     * 请求失败
     */
    void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e);

    /**
     * @param response 返回结果
     */
    void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response);
    // 注：该回调是子线程，非主线程
//                ResultInfo resultInfo = com.alibaba.fastjson.JSON.parseObject(response.body().string(), ResultInfo.class);


}
