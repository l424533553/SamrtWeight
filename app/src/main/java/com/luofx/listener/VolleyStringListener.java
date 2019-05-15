package com.luofx.listener;
//package com.luofx.listener;

import com.android.volley.VolleyError;

public interface VolleyStringListener {

    /**
     * @param volleyError 错误信息
     */
    void onStringResponse(VolleyError volleyError, int flag);

    /**
     * 登陸請求成功
     *
     * @param response json  String对象
     */
    void onStringResponse(String response, int flag);
}
