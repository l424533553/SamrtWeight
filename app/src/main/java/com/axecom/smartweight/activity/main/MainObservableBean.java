package com.axecom.smartweight.activity.main;


import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;

import com.xuanyuan.library.MyPreferenceUtils;

/**
 * 作者：罗发新
 * 时间：2019/6/5 0005    星期三
 * 邮件：424533553@qq.com
 * 说明：主界面的 实体bean
 */
public class MainObservableBean {

    private ObservableField<String> netWeight = new ObservableField<>("0.000");
    private ObservableField<String> carryWewight = new ObservableField<>("0.000");
    private ObservableField<String> price = new ObservableField<>("0.00");
    private ObservableField<String> hintPrice = new ObservableField<>("0.00");
    private ObservableField<String> goodName = new ObservableField<>();
    private ObservableField<String> grandMoney = new ObservableField<>("0.00");

    private ObservableField<String> totalWeight = new ObservableField<>("0.000");
    private ObservableField<String> totalPrice = new ObservableField<>("0.00");

    private ObservableFloat priceLarge = new ObservableFloat(0.3f);
    private ObservableFloat priceMiddle = new ObservableFloat(0.2f);
    private ObservableFloat priceSmall = new ObservableFloat(0.1f);
    //置零的 重量
    private ObservableFloat zeroWeight = new ObservableFloat(0.000f);

    private ObservableInt backgroundRes = new ObservableInt();

    /**
     * 版本号
     */
    private ObservableField<String> version = new ObservableField<>();

    public ObservableField<String> getNetWeight() {
        return netWeight;
    }

    public ObservableFloat getZeroWeight() {
        return zeroWeight;
    }

    public void setZeroWeight(ObservableFloat zeroWeight) {
        this.zeroWeight = zeroWeight;
    }

    public void setNetWeight(ObservableField<String> netWeight) {
        this.netWeight = netWeight;
    }

    public ObservableField<String> getVersion() {
        return version;
    }

    public void setVersion(ObservableField<String> version) {
        this.version = version;
    }

    public ObservableField<String> getCarryWewight() {
        return carryWewight;
    }

    public void setCarryWewight(ObservableField<String> carryWewight) {
        this.carryWewight = carryWewight;
    }

    public ObservableInt getBackgroundRes() {
        return backgroundRes;
    }

    public void setBackgroundRes(ObservableInt backgroundRes) {
        this.backgroundRes = backgroundRes;
    }

    public ObservableField<String> getGrandMoney() {
        return grandMoney;
    }

    public String getGrandMoneyString() {
        String result = grandMoney.get();
        if (result == null) {
            return "0.00";
        } else {
            return result;
        }
    }

    public void setGrandMoney(ObservableField<String> grandMoney) {
        this.grandMoney = grandMoney;
    }

    public ObservableField<String> getGoodName() {
        return goodName;
    }

    public void setGoodName(ObservableField<String> goodName) {
        this.goodName = goodName;
    }

    public ObservableField<String> getPrice() {
        return price;
    }

    public void setPrice(ObservableField<String> price) {
        this.price = price;
    }

    public ObservableField<String> getHintPrice() {
        return hintPrice;
    }

    public void setHintPrice(ObservableField<String> hintPrice) {
        this.hintPrice = hintPrice;
    }

    public ObservableField<String> getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(ObservableField<String> totalWeight) {
        this.totalWeight = totalWeight;
    }

    public ObservableField<String> getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(ObservableField<String> totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ObservableFloat getPriceLarge() {
        return priceLarge;
    }

    public void setPriceLarge(ObservableFloat priceLarge) {
        this.priceLarge = priceLarge;
    }

    public ObservableFloat getPriceMiddle() {
        return priceMiddle;
    }

    public void setPriceMiddle(ObservableFloat priceMiddle) {
        this.priceMiddle = priceMiddle;
    }

    public ObservableFloat getPriceSmall() {
        return priceSmall;
    }

    public void setPriceSmall(ObservableFloat priceSmall) {
        this.priceSmall = priceSmall;
    }
}
