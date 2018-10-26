//package com.axecom.smartweight.bean;
//
//import com.axecom.smartweight.base.AppDatabase;
//import com.raizlabs.android.dbflow.annotation.Column;
//import com.raizlabs.android.dbflow.annotation.OneToMany;
//import com.raizlabs.android.dbflow.annotation.PrimaryKey;
//import com.raizlabs.android.dbflow.annotation.Table;
//import com.raizlabs.android.dbflow.sql.language.Select;
//import com.raizlabs.android.dbflow.structure.BaseModel;
//
//import java.io.Serializable;
//
///**
// * Created by Administrator on 2018/7/22.
// */
//
//@Table(database = AppDatabase.class)
//public class Order extends BaseModel implements Serializable{
//    @PrimaryKey(autoincrement = true)//ID自增
//    public long id;
//
//    @Column
//    public String token;
//    @Column
//    public String mac;
//    @Column
//    public String total_amount;
//    @Column
//    public String total_weight;
//    @Column
//    public String payment_id;
//    @Column
//    public String create_time;
//    @Column
//    public String total_number;
//    @Column
//    public String pricing_model;
//
//
//    @Column
//    public String goods_id;
//    @Column
//    public String goods_name;
//    @Column
//    public String goods_price;
//    @Column
//    public String goods_number;
//    @Column
//    public String goods_weight;
//    @Column
//    public String amount;
//}
