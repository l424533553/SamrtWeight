package com.axecom.smartweight.bean;

import com.axecom.smartweight.base.BaseEntity;

import java.util.List;

public class UnusualOrdersBean extends BaseEntity {
    public String total;
    public List<Order> list;


    public class Order{
        public String id;
        public String order_no;
        public String create_time;
        public String client_name;
        public String buyer_name;
        public String total_amount;
        public String total_weight;
        public String payment_type;
        public String status;
    }
}
