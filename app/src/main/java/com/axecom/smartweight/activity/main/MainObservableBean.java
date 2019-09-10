package com.axecom.smartweight.activity.main;


import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.text.TextUtils;

/**
 * 作者：罗发新
 * 时间：2019/6/5 0005    星期三
 * 邮件：424533553@qq.com
 * 说明：主界面的 实体bean
 */
public class MainObservableBean {

    private ObservableField<String> netWeight = new ObservableField<>("0.000");
    private ObservableField<String> carryWewight = new ObservableField<>("0.000");
    // 真实的价格
    private ObservableField<String> price = new ObservableField<>("0.00");
    // 隐藏的价格
    private ObservableField<String> hintPrice = new ObservableField<>("0.00");
    private ObservableField<String> goodName = new ObservableField<>();
    private ObservableField<String> grandMoney = new ObservableField<>("0.00");

    private ObservableField<String> totalWeight = new ObservableField<>("0.000");
    private ObservableField<String> totalPrice = new ObservableField<>("0.00");

    private ObservableFloat priceLarge = new ObservableFloat(0.3f);
    private ObservableFloat priceMiddle = new ObservableFloat(0.2f);
    private ObservableFloat priceSmall = new ObservableFloat(0.1f);
    //置零的 重量 ，主要用于SX秤 中使用
    private ObservableFloat zeroWeight = new ObservableFloat(0.000f);
    private ObservableInt backgroundRes = new ObservableInt();

    /**
     * etPrice控件被选中了
     */
    private boolean isEtPriceSelect;
    private String batchCode;
    // 产品id
    private String cid;

    // 前面成交的商品名
    private String frontDealName;
    //前面成交了的订单号
    private String frontOrderNo;
    // 前面成交价格
    private float frontDealPrice;
    private float frontDealWeight;

    public boolean isEtPriceSelect() {
        return isEtPriceSelect;
    }

    public void setEtPriceSelect(boolean etPriceSelect) {
        isEtPriceSelect = etPriceSelect;
    }

    public String getFrontDealName() {
        if (frontDealName == null) {
            return "";
        }
        return frontDealName;
    }

    public String getFrontOrderNo() {
        return frontOrderNo;
    }

    public void setFrontOrderNo(String frontOrderNo) {
        this.frontOrderNo = frontOrderNo;
    }

    public float getFrontDealPrice() {
        return frontDealPrice;
    }

    public void setFrontDealPrice(float frontDealPrice) {
        this.frontDealPrice = frontDealPrice;
    }

    public void setFrontDealName(String frontDealName) {
        this.frontDealName = frontDealName;
    }

    public float getFrontDealWeight() {
        return frontDealWeight;
    }

    public void setFrontDealWeight(float frontDealWeight) {
        this.frontDealWeight = frontDealWeight;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

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
        if (TextUtils.isEmpty(result)) {
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

    /**
     * @return 验证价格字段是否可用
     */
    private boolean verifyPriceAble(String price) {
        if (TextUtils.isEmpty(price)) {
            return false;
        }
        if (price != null) {
            return Float.valueOf(price) > 0;
        }
        return false;
    }

    //获取真实的价格
    public float getReallyPrice() {
        String priceString = null;
        if (!verifyPriceAble(getPrice().get())) {
            if (verifyPriceAble(getHintPrice().get())) {
                priceString = getHintPrice().get();
            }
        } else {
            priceString = getPrice().get();
        }

        if (priceString != null) {
            return Float.valueOf(priceString);
        }
        return 0.00f;
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
