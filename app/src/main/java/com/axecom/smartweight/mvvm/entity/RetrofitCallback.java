package com.axecom.smartweight.mvvm.entity;

public interface RetrofitCallback{

    void onNext(ResultRtInfo resultInfo, int flag);

    void onError(Throwable e, int flag);

    void onComplete(int flag);

}