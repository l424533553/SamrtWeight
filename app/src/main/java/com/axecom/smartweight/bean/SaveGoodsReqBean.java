package com.axecom.smartweight.bean;

import java.io.Serializable;
import java.util.List;

public class SaveGoodsReqBean implements Serializable {
    private String token;
    private String mac;
    private List goods;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public List getGoods() {
        return goods;
    }

    public void setGoods(List goods) {
        this.goods = goods;
    }

    public static class Goods implements Serializable{
        public int id;
        public String name;
        public int cid;
        public int traceable_code;
        public String price;
        public int is_default;
        public String batch_code;
    }
}
