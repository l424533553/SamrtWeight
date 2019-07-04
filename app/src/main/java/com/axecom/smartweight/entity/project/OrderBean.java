package com.axecom.smartweight.entity.project;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * author: luofaxin
 * date： 2018/10/10 0010.
 * email:424533553@qq.com
 * describe:
 */
@DatabaseTable(tableName = "orderbean")
public class OrderBean implements Cloneable {

    //部门编号
    @JSONField(serialize = false)
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String itemno;// 商品编号
    @DatabaseField
    private String money;
    @DatabaseField
    private String name;
    @DatabaseField
    private String price;
    @DatabaseField
    private String weight;
    @JSONField(serialize = false)
    @DatabaseField
    private String unit = "kg";

    @DatabaseField
    private String x0 = "0";//置零值Ad
    @DatabaseField
    private String x1 = "0";//标定0位ad
    @DatabaseField
    private String x2 = "0";//皮重
    @DatabaseField
    private String k = "0";
    @DatabaseField
    private String weight0="0";  //标定ad值
    @DatabaseField
    private String xcur="0";//当前商品的ad
    @DatabaseField
    private String traceno; // 批次號
    @DatabaseField
    private String time; // 创建时间

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTraceno() {
        return traceno;
    }

    public void setTraceno(String traceno) {
        this.traceno = traceno;
    }

    private long time1;
    private long time2;

    @JSONField(serialize = false)
    @DatabaseField(canBeNull = false, foreign = true)
    private OrderInfo orderInfo;

    @Override
    public OrderBean clone() {
        OrderBean stu = null;
        try {
            stu = (OrderBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }

    public OrderBean(String name, String price, String weight, String subTotal) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.money = subTotal;
    }

    public OrderBean() {
    }

    public String getWeight0() {
        return weight0;
    }

    public void setWeight0(String weight0) {
        this.weight0 = weight0;
    }

    public long getTime1() {
        return time1;
    }

    public void setTime1(long time1) {
        this.time1 = time1;
    }

    public long getTime2() {
        return time2;
    }

    public void setTime2(long time2) {
        this.time2 = time2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getX0() {
        return x0;
    }

    public void setX0(String x0) {
        this.x0 = x0;
    }

    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getX2() {
        return x2;
    }

    public void setX2(String x2) {
        this.x2 = x2;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getXcur() {
        return xcur;
    }

    public void setXcur(String xcur) {
        this.xcur = xcur;
    }


    public String getMoney() {
        if (TextUtils.isEmpty(money)) {
            return "0.00";
        }
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        if (TextUtils.isEmpty(weight)) {
            return "0";
        }
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
