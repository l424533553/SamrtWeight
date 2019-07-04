package com.axecom.smartweight.listener;

import com.alibaba.fastjson.JSONObject;

import retrofit2.Response;

public interface DataCallBack {

    void onResponse(retrofit2.Call<com.alibaba.fastjson.JSONObject> call, Response<JSONObject> response);

    void onFailure(retrofit2.Call<com.alibaba.fastjson.JSONObject> call, Throwable t);

}