package com.axecom.smartweight.net;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.axecom.smartweight.base.BaseEntity;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017-11-28.
 */

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {

    private Context context;

    public BaseObserver(Context context) {
        this.context = context;
    }

    public BaseObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();
    }

    @Override
    public void onNext(BaseEntity<T> baseEntity) {
        onRequestEnd();
        if (baseEntity.isSuccess()) {
            try {
                onSuccess(baseEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                onCodeError(baseEntity);
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

    protected void onCodeError(BaseEntity<T> t) throws Exception{

    };

    protected abstract void onSuccess(BaseEntity<T> t) throws Exception;

    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;


    protected void onRequestStart() {

    }

    protected void onRequestEnd() {

    }
}
