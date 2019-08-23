package com.axecom.smartweight.mvvm.retrofit;

import com.alibaba.fastjson.JSONObject;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.listener.DataCallBack;
import com.axecom.smartweight.mvvm.entity.ResultRtInfo;
import com.axecom.smartweight.utils.security.DesBCBHelper;
import com.xuanyuan.library.MyLog;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: luofaxin
 * date： 2018/9/10 0010.
 * email:424533553@qq.com
 * describe:  关于okhttp 或者retrofit请求的集合
 */
public class HttpRtHelper implements IConstants {

    private static HttpRtHelper mInstants;

    public static HttpRtHelper getmInstants() {
        if (mInstants == null) {
            mInstants = new HttpRtHelper();
        }
        return mInstants;
    }

    private HttpRtHelper() {

    }


    /**
     * 采用了retrofit+Rxjava  模式请求
     *
     * @param callback 回调监听
     * @param flag     请求标识
     */
    public void getUserInfoExByRetrofit(String mac, RetrofitCallback callback, int flag) {
        if (mac == null) {
            return;
        }
        String data = "{\"mac\":\"" + mac + "\"}";
        String desdata = DesBCBHelper.getmInstants().encode(data);

        RetrofitModel.getUserInfo(desdata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subject<ResultRtInfo<UserInfo>>() {
                    @Override
                    public boolean hasObservers() {
                        MyLog.i("retrofit", "hasObservers");
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        MyLog.i("retrofit", "hasThrowable");
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        MyLog.i("retrofit", "hasComplete");
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        MyLog.i("retrofit", "getThrowable");
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super ResultRtInfo<UserInfo>> observer) {
                        MyLog.i("retrofit", "subscribeActual");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        //1
                        MyLog.i("retrofit", "onSubscribe");
                    }

                    @Override
                    public void onNext(ResultRtInfo<UserInfo> resultInfo) {
                        MyLog.i("retrofit", "onNext" + resultInfo.toString());
                        callback.onNext(resultInfo, flag);
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLog.i("retrofit", "onError");
                        callback.onError(e, flag);
                    }

                    @Override
                    public void onComplete() {
                        MyLog.i("retrofit", "onComplete");
                        callback.onComplete(flag);
                    }
                });
    }

    /**
     * 采用了okhttp3 模式请求
     *
     * @param volleyListener 回调监听
     * @param flag           请求标识
     */
    public void getUserInfoExByOkhttp(String mac, DataCallBack volleyListener, int flag) {
        if (mac == null) {
            return;
        }
        String data = "{\"mac\":\"" + mac + "\"}";
        String desdata = DesBCBHelper.getmInstants().encode(data);
//        String url = BASE_IP_ST + "/api/smart/getinfobymac?desdata=" + desdata;
//        application.volleyGet(url, volleyListener, flag);

        //步骤三：对发送的请求进行封装
        retrofit2.Call<JSONObject> call = RetrofitHttpUtils.getRetrofitAPI(IRetrofitAPI.class).getResultInfo(desdata);

        //步骤四:发送网络请求(异步)
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(retrofit2.Call<JSONObject> call, Response<JSONObject> response) {
                volleyListener.onResponse(call, response);
            }

            @Override
            public void onFailure(retrofit2.Call<JSONObject> call, Throwable t) {
                volleyListener.onFailure(call, t);
            }
        });

    }


}
