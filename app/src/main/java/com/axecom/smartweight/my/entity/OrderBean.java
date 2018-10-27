package com.axecom.smartweight.my.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * author: luofaxin
 * date： 2018/10/10 0010.
 * email:424533553@qq.com
 * describe:
 */
@DatabaseTable(tableName = "orderbean")
public class OrderBean {

    //部门编号
    @DatabaseField(generatedId = true)
    private  int id;
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
    @DatabaseField
    private String unit = "kg";
    @DatabaseField
    private String x0;
    @DatabaseField
    private String x1;
    @DatabaseField
    private String x2;
    @DatabaseField
    private String k;
    @DatabaseField
    private String xcur;
    @DatabaseField
    private String traceno; // 批次號

    public String getTraceno() {
        return traceno;
    }

    public void setTraceno(String traceno) {
        this.traceno = traceno;
    }

    @DatabaseField(canBeNull = false, foreign = true)
    private OrderInfo orderInfo;

    public OrderBean(String name, String price, String weight, String subTotal) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.money = subTotal;
    }

    public OrderBean() {
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
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
