package com.axecom.smartweight.net;

import android.accounts.NetworkErrorException;
import android.content.Context;


import com.axecom.smartweight.bean.PhoneBean;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017-11-28.
 */

public abstract class PhoneObserver<T> implements Observer<PhoneBean<T>> {

    private Context context;

    public PhoneObserver(Context context) {
        this.context = context;
    }

    public PhoneObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();
    }

    @Override
    public void onNext(PhoneBean<T> phoneBean) {
        onRequestEnd();
        if (phoneBean.isSuccess()) {
            try {
                onSuccess(phoneBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                onCodeError(phoneBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);
            }else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    protected void onCodeError(PhoneBean<T> t) throws Exception{

    };

    protected abstract void onSuccess(PhoneBean<T> t) throws Exception;

    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;


    protected void onRequestStart() {

    }

    protected void onRequestEnd() {

    }
}
