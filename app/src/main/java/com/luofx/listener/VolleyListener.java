package com.luofx.listener;//package com.luofx.listener;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyListener {

    /**
     *
     * @param volleyError  错误信息

     */
    void onResponse(VolleyError volleyError, int flag);

    /**
     * 登陸請求成功
     * @param jsonObject json对象
     */
    void onResponse(JSONObject jsonObject, int flag);
}
