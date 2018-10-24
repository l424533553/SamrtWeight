package com.axecom.smartweight.bean;

import java.io.Serializable;
import java.util.List;

public class SubOrderReqBean implements Serializable{

    private String token;
    private String mac;
    private String total_amount;
    private String total_weight;
    private String payment_id;
    private String create_time;
    private String pricing_model;
    private List<Goods> goods;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    private String order_no;

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List goods) {
        this.goods = goods;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(String total_weight) {
        this.total_weight = total_weight;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPricing_model() {
        return pricing_model;
    }

    public void setPricing_model(String pricing_model) {
        this.pricing_model = pricing_model;
    }

    public static class Goods implements Serializable{
        private String goods_id;
        private String goods_name;
        private String goods_price;
        private String goods_number;
        private String goods_weight;
        private String amount;
        private String batch_code;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(String goods_number) {
            this.goods_number = goods_number;
        }

        public String getGoods_amount() {
            return amount;
        }

        public void setGoods_amount(String goods_amount) {
            this.amount = goods_amount;
        }

        public String getGoods_weight() {
            return goods_weight;
        }

        public void setGoods_weight(String goods_weight) {
            this.goods_weight = goods_weight;
        }

        public String getBatch_code() {
            return batch_code;
        }

        public void setBatch_code(String batch_code) {
            this.batch_code = batch_code;
        }
    }
}
