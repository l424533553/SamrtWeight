package com.axecom.smartweight.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-12-1.
 */

public class PhoneBean<T> implements Serializable{
    private int resultcode;
    private String reason;

    private T result;

    public boolean isSuccess(){
        return resultcode == 200;
    }

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
