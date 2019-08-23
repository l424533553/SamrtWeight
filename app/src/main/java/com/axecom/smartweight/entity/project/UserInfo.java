package com.axecom.smartweight.entity.project;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.axecom.smartweight.BR;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.xuanyuan.library.utils.text.StringUtils;

/**
 * author: luofaxin
 * date： 2018/9/26 0026.
 * email:424533553@qq.com
 * describe:
 */
@DatabaseTable(tableName = "userinfo")
public class UserInfo extends BaseObservable {
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
    @DatabaseField(id = true)
    private int id = 1;
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
    // 出厂编号
    @DatabaseField
    private String sno;
    /**
     * 支付类型，可能有不同的合作银行商
     */
    @DatabaseField
    private String paytype;

    // 秤规格
    @DatabaseField
    private String model;
    // 厂家
    @DatabaseField
    private String producer;
    // 出厂时间
    @DatabaseField
    private String outtime;

    public UserInfo() {
    }

    /**
     * @return 是否配置了电子支付的参数，如果没有配置，那么无法使用电子支付
     */
    public boolean isConfigureEpayParams() {
        return !StringUtils.isEmpty(getMchid()) && !StringUtils.isEmpty(getKey());
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
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

    @Bindable
    public String getMarketname() {
        return marketname;
    }

    public void setMarketname(String marketname) {
        this.marketname = marketname;
        notifyPropertyChanged(BR.marketname);
    }

    /**
     * @return 获取了摊位号
     */
    @Bindable
    public String getCompanyno() {
        if (companyno == null) {
            return "";
        }
        return companyno;
    }

    public void setCompanyno(String companyno) {
        this.companyno = companyno;
        notifyPropertyChanged(BR.companyno);
    }


    @Bindable
    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
        notifyPropertyChanged(BR.tid);
    }

    @Bindable
    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
        notifyPropertyChanged(BR.seller);
    }

    @Override
    public void notifyChange() {
        notifyPropertyChanged(BR.seller);
        notifyPropertyChanged(BR.tid);
        notifyPropertyChanged(BR.companyno);
        notifyPropertyChanged(BR.marketname);
        super.notifyChange();
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

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProducer() {
        if (TextUtils.isEmpty(producer)) {
            return "";
        }
        if (producer.contains("香山")) {
            return "xs";
        }
        return "";
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }
}


