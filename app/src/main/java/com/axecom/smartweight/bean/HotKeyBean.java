//package com.axecom.smartweight.bean;
//
//import com.axecom.smartweight.base.AppDatabase;
//import com.raizlabs.android.dbflow.annotation.Column;
//import com.raizlabs.android.dbflow.annotation.PrimaryKey;
//import com.raizlabs.android.dbflow.annotation.Table;
//import com.raizlabs.android.dbflow.structure.BaseModel;
//
//import java.io.Serializable;
//
///**
// * Created by Administrator on 2018/8/6.
// */
//
//@Table(database = AppDatabase.class)
//public class HotKeyBean extends BaseModel implements Serializable {
//    @PrimaryKey(autoincrement = true)//ID自增
//    public long hotKeyid;
//
//    @Column
//    public int id;
//    @Column
//    public String name;
//    @Column
//    public int cid;
//    @Column
//    public int traceable_code;
//    @Column
//    public String price;
//    @Column
//    public int is_default;
//    @Column
//    public String weight;
//    @Column
//    public String batch_code;
//    @Column
//    public String grandTotal;
//
//    public String count;
//
//    public long getHotKeyid() {
//        return hotKeyid;
//    }
//
//    public void setHotKeyid(long hotKeyid) {
//        this.hotKeyid = hotKeyid;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getCid() {
//        return cid;
//    }
//
//    public void setCid(int cid) {
//        this.cid = cid;
//    }
//
//    public int getTraceable_code() {
//        return traceable_code;
//    }
//
//    public void setTraceable_code(int traceable_code) {
//        this.traceable_code = traceable_code;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public int getIs_default() {
//        return is_default;
//    }
//
//    public void setIs_default(int is_default) {
//        this.is_default = is_default;
//    }
//
//    public String getWeight() {
//        return weight;
//    }
//
//    public void setWeight(String weight) {
//        this.weight = weight;
//    }
//
//    public String getGrandTotal() {
//        return grandTotal;
//    }
//
//    public void setGrandTotal(String grandTotal) {
//        this.grandTotal = grandTotal;
//    }
//
//    public String getCount() {
//        return count;
//    }
//
//    public void setCount(String count) {
//        this.count = count;
//    }
//
//    public String getBatch_code() {
//        return batch_code;
//    }
//
//    public void setBatch_code(String batch_code) {
//        this.batch_code = batch_code;
//    }
//}
