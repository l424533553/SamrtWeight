package com.axecom.smartweight.bean;

import com.axecom.smartweight.base.BaseEntity;

import java.util.List;

public class OrderListResultBean<T> extends BaseEntity {
    public int total;
    public String total_amount;

    public List<list> list;

    public class list{
        public String order_no;
        public String goods_name;
        public String goods_weight;
        public String goods_price;
        public String goods_number;
        public String price_number;
        public String total_amount;
        public String times;
        public String payment_type;
    }
}
