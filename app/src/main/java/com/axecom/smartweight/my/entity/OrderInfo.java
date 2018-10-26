package com.axecom.smartweight.my.entity;

import android.text.TextUtils;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;
import java.util.List;

/**
 * author: luofaxin
 * date： 2018/9/11 0011.
 * email:424533553@qq.com
 * describe:
 */
@DatabaseTable(tableName = "orderinfo")
public class OrderInfo implements Cloneable {
    /**
     * billcode : AX1234
     * billstatus : 成功
     * seller : 梁日成
     * sellerid : 1025
     * settlemethod : 现金支付
     * terid : 91
     * time : 2018-08-28 08:56:52
     * items : [{"itemno":"1847","money":"149.30","name":"小草鱼","price":"15.00","unit":"kg","weight":"9.955","x0":"0.1","x1":"0.2","x2":"0.3","k":"0.4","xcur":"0.5"},{"itemno":"1847","money":"149.30","name":"小草鱼","price":"15.00","unit":"kg","weight":"9.955","x0":"0.1","x1":"0.2","x2":"0.3","k":"0.4","xcur":"0.5"}]
     */

    //部门编号
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String billcode;
    @DatabaseField
    private String billstatus;
    @DatabaseField
    private String seller;
    @DatabaseField
    private int sellerid;
    @DatabaseField
    private int settlemethod;
    @DatabaseField
    private int terid;//称号
    @DatabaseField
    private int marketid;
    @DatabaseField
    private String time;
    @DatabaseField
    private int hour; //小時
    @DatabaseField
    private int day; //小時
    @DatabaseField
    private String totalamount;  // 总金额
    @DatabaseField
    private String totalweight;  // 总重量
    @DatabaseField
    private String stallNo;// 摊位号
    @DatabaseField
    private Timestamp timestamp;  //时间戳

    @DatabaseField
    private int state;  // 订单状态
    @DatabaseField
    private String marketName;

    /**
     * 这里需要注意的是：属性类型只能是ForeignCollection<T>或者Collection<T>
     * 如果需要懒加载（延迟加载）可以在@ForeignCollectionField加上参数eager=false
     * 这个属性也就说明一个部门对应着多个用户
     */
    @ForeignCollectionField
    private ForeignCollection<OrderBean> orderBeans;
    private List<OrderBean> orderItem;

    public List<OrderBean> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderBean> orderItem) {
        this.orderItem = orderItem;
    }

    public OrderInfo() {
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStallNo() {
        if (TextUtils.isEmpty(stallNo)) {
            return " ";
        }
        return stallNo;
    }

    public void setStallNo(String stallNo) {
        this.stallNo = stallNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSettlemethod() {
        return settlemethod;
    }

    public ForeignCollection<OrderBean> getOrderBeans() {
        return orderBeans;
    }

    public void setOrderBeans(ForeignCollection<OrderBean> orderBeans) {
        this.orderBeans = orderBeans;
    }

    public void setSettlemethod(int settlemethod) {
        this.settlemethod = settlemethod;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getTotalweight() {
        return totalweight;
    }

    public void setTotalweight(String totalweight) {
        this.totalweight = totalweight;
    }

    public String getBillcode() {
        return billcode;
    }

    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }

    public String getBillstatus() {
        return billstatus;
    }

    public void setBillstatus(String billstatus) {
        this.billstatus = billstatus;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getSellerid() {
        return sellerid;
    }

    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }

    public int getTerid() {
        return terid;
    }

    public void setTerid(int terid) {
        this.terid = terid;
    }

    public int getMarketid() {
        return marketid;
    }

    public void setMarketid(int marketid) {
        this.marketid = marketid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
