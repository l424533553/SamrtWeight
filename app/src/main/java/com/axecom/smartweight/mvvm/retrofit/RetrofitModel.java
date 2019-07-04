package com.axecom.smartweight.mvvm.retrofit;

import com.alibaba.fastjson.JSONObject;
import com.axecom.smartweight.entity.project.UserInfo;
import com.google.gson.JsonParseException;
import com.axecom.smartweight.mvvm.entity.FamousInfo;
import com.axecom.smartweight.mvvm.entity.FamousInfoReq;
import com.axecom.smartweight.mvvm.entity.ResultRtInfo;


import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.HttpException;

public class RetrofitModel {
    private static RetrofitModel retrofitModel;
    private IRetrofitAPI retrofitAPI;

    public RetrofitModel() {
        retrofitAPI = RetrofitHttpUtils.getRetrofitAPI(IRetrofitAPI.class);
    }

    public static RetrofitModel getInstance() {
        if (retrofitModel == null) {
            retrofitModel = new RetrofitModel();
        }
        return retrofitModel;
    }

    public Call<JSONObject> getResultInfo(String desdata) {
        return getInstance().retrofitAPI.getResultInfo(desdata);
    }

    public static Observable<ResultRtInfo<UserInfo>> getUserInfo(String desdata) {
        return getInstance().retrofitAPI.getUserInfo(desdata);
    }

    public static Observable<ResponseBody> downloadFile() {
        Observable<ResponseBody> observable = getInstance().retrofitAPI.downloadFile();
        //通过Observable发起请求
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            observable.subscribeOn(Schedulers.io()) //指定网络请求在io后台线程中进行
                    .observeOn(Schedulers.io()) //指定doOnNext的操作在io后台线程进行
                    .doOnNext(new Consumer<ResponseBody>() {
                        //doOnNext里的方法执行完毕，observer里的onNext、onError等方法才会执行。
                        @Override
                        public void accept(ResponseBody body) {
                            //下载文件，保存到本地
                            //通过body.byteStream()可以得到输入流，然后就是常规的IO读写保存了。

                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread()) //指定observer回调在UI主线程中进行
                    .subscribe(getObserver()); //发起请求，请求的结果先回调到doOnNext进行处理，再回调到observer中
        }

        return observable;
    }


    public Call<FamousInfo> queryLookUp(FamousInfoReq famousInfoReq) {
        return getInstance().retrofitAPI.getFamousResult(famousInfoReq.apiKey, famousInfoReq.keyword, famousInfoReq.page, famousInfoReq.rows);
    }

    private static Observer getObserver() {
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable throwable) {
                if (throwable instanceof Exception) {
                    handleThrowable(throwable);
                } else {
                    onError(new HttpThrowable(HttpThrowable.UNKNOWN, "未知错误", throwable));
                }
            }

            @Override
            public void onComplete() {

            }
        };
        return observer;
    }

    /**
     * Rejava +Retrofit  中的onError方法，该方法的参数为Throwable，
     * 并没能反馈更直接清楚的异常信息给我们，所以有必要对Throwable异常进行处理转换。
     */
    private static HttpThrowable handleThrowable(Throwable throwable) {
        if (throwable instanceof HttpException) {
            return new HttpThrowable(HttpThrowable.HTTP_ERROR, "网络(协议)异常", throwable);
        } else if (throwable instanceof JsonParseException || throwable instanceof JSONException || throwable instanceof ParseException) {
            return new HttpThrowable(HttpThrowable.PARSE_ERROR, "数据解析异常", throwable);
        } else if (throwable instanceof UnknownHostException) {
            return new HttpThrowable(HttpThrowable.NO_NET_ERROR, "网络连接失败，请稍后重试", throwable);
        } else if (throwable instanceof SocketTimeoutException) {
            return new HttpThrowable(HttpThrowable.TIME_OUT_ERROR, "连接超时", throwable);
        } else if (throwable instanceof ConnectException) {
            return new HttpThrowable(HttpThrowable.CONNECT_ERROR, "连接异常", throwable);
        } else if (throwable instanceof javax.net.ssl.SSLHandshakeException) {
            return new HttpThrowable(HttpThrowable.SSL_ERROR, "证书验证失败", throwable);
        } else {
            return new HttpThrowable(HttpThrowable.UNKNOWN, throwable.getMessage(), throwable);
        }
    }


    /**
     * RxJava中提供了许多操作符，takeUntil操作符。
     * ObservableA.takeUntil(ObservableB) 的作用是：
     * 监视ObservableB，当它发射内容时，则停止ObservableA的发射并将其终止。
     * <p>
     * 常利用takeUntil这一特性，让ObservableA负责网络请求，
     * 让ObservableB负责在页面销毁时发射事件，从而终止ObservableA（网络请求）
     */
    private void testTakeUntil() {
        //下面的Observable.interval( x, TimeUnit.MILLISECONDS) 表示
        // 每隔x毫秒发射一个long类型数字，数字从0开始，每次递增1
        // 执行后  observableA，打印了 0，1  。因为800ms后B打断了A
        Observable<Long> observableA = Observable.interval(300, TimeUnit.MILLISECONDS);
        Observable<Long> observableB = Observable.interval(800, TimeUnit.MILLISECONDS);
        observableA.takeUntil(observableB).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


}
