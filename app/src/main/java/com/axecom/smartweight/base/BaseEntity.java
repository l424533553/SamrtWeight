package com.axecom.smartweight.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-11-29.
 */

public class BaseEntity<T> implements Serializable{
    private static final int SUCCESS_CODE = 0;
    private int ret;
    private String msg;
    private T data;
    private int code;

    public boolean isSuccess(){
        return getCode() == SUCCESS_CODE;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    private int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
