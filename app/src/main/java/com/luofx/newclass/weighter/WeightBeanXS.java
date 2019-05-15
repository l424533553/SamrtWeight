package com.luofx.newclass.weighter;


import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 称重数据类 Bean
 */
public class WeightBeanXS implements Serializable {
    private int CurrentAD;//当前ad
    private int zeroAd;//零位Ad
    private int sbZeroAd;//标定零位Ad
    private int sbAd;//标定Ad
    private String kValue;// K值
    private int currentWeight;//净重
    private int tareWeight;//皮重
    private int dataFlag;// 标志位

    public WeightBeanXS() {
    }

    public int getSbZeroAd() {
        return sbZeroAd;
    }

    public void setSbZeroAd(int sbZeroAd) {
        this.sbZeroAd = sbZeroAd;
    }

    public int getSbAd() {
        return sbAd;
    }

    public void setSbAd(int sbAd) {
        this.sbAd = sbAd;
    }

    public String getkValue() {
        return kValue;
    }

    public void setkValue(String kValue) {
        this.kValue = kValue;
    }

    public int getCurrentAD() {
        return CurrentAD;
    }

    public void setCurrentAD(int currentAD) {
        CurrentAD = currentAD;
    }

    public int getZeroAd() {
        return zeroAd;
    }

    public void setZeroAd(int zeroAd) {
        this.zeroAd = zeroAd;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public int getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(int tareWeight) {
        this.tareWeight = tareWeight;
    }

    public int getDataFlag() {
        return dataFlag;
    }

    public void setDataFlag(int dataFlag) {
        this.dataFlag = dataFlag;
    }


    @NonNull
    @Override
    public String toString() {
        return "WeightBeanXS{" +
                "CurrentAD=" + CurrentAD +
                ", zeroAd=" + zeroAd +
                ", currentWeight=" + currentWeight +
                ", tareWeight=" + tareWeight +
                ", dataFlag=" + dataFlag +
                '}';
    }
}
