package com.luofx.listener;
//package com.luofx.listener;

import com.android.volley.VolleyError;

public interface VolleyStringListener {

    /**
     * @param volleyError 错误信息
     */
    void onResponseError(VolleyError volleyError, int flag);

    /**
     * 登陸請求成功
     *
     * @param response json  String对象
     */
    void onResponse(String response, int flag);
}
