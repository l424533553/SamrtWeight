package com.axecom.smartweight.my.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

/**
 * author: luofaxin
 * date： 2018/10/17 0017.
 * email:424533553@qq.com
 * describe:
 */

@DatabaseTable(tableName = "hotGoods")
public class HotGood {
    /**
     * type : 猪肉
     * typeid : 149
     * name : 前瘦肉
     * cid : 1690
     * price : 0
     */

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String type;
    @DatabaseField
    private int typeid;
    @DatabaseField
    private String name;
    @DatabaseField
    private int cid;
    @DatabaseField
    private String price="0.00";
    @DatabaseField
    private String batchCode; // 批次號

    public HotGood() {
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotGood hotGood = (HotGood) o;
        return typeid == hotGood.typeid &&
                cid == hotGood.cid &&
                Objects.equals(type, hotGood.type) &&
                Objects.equals(name, hotGood.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, typeid, name, cid);
    }
}

