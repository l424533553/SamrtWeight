package com.axecom.smartweight.mvvm.retrofit;

import com.axecom.smartweight.mvvm.entity.ResultRtInfo;

public interface RetrofitCallback{

    void onNext(ResultRtInfo resultInfo, int flag);

    void onError(Throwable e, int flag);

    void onComplete(int flag);

}