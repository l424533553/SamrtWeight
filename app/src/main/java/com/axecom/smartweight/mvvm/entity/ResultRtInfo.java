package com.axecom.smartweight.mvvm.entity;

/**
 * author: luofaxin
 * date： 2018/9/26 0026.
 * email:424533553@qq.com
 * describe:
 */
public class ResultRtInfo<T> {

    /**
     * status : 0
     * msg : ok
     * data : {"marketid":1,"marketname":"黄田市场","companyno":"B070","tid":101,"seller":"郭金龙","sellerid":127,"key":null,"mchid":null}
     */

    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
