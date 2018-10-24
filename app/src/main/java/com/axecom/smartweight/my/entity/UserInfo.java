package com.axecom.smartweight.my.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * author: luofaxin
 * date： 2018/9/26 0026.
 * email:424533553@qq.com
 * describe:
 */
@DatabaseTable(tableName = "userinfo")
public class UserInfo {
    /**
     * marketid : 1
     * marketname : 黄田市场
     * companyno : B070
     * tid : 101
     * seller : 郭金龙
     * sellerid : 127
     * key : null
     * mchid : null
     */
    //部门编号
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int marketid;
    @DatabaseField
    private String marketname;
    @DatabaseField
    private String companyno;
    @DatabaseField
    private int tid;
    @DatabaseField
    private String seller;
    @DatabaseField
    private int sellerid;
    @DatabaseField
    private String key;
    @DatabaseField
    private String mchid;

    public UserInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public int getMarketid() {
        return marketid;
    }

    public void setMarketid(int marketid) {
        this.marketid = marketid;
    }

    public String getMarketname() {
        return marketname;
    }

    public void setMarketname(String marketname) {
        this.marketname = marketname;
    }

    public String getCompanyno() {
        return companyno;
    }

    public void setCompanyno(String companyno) {
        this.companyno = companyno;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
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

    public String getKey() {
        return key;
    }

    public String getMchid() {
        return mchid;
    }
}
