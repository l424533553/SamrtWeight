package com.axecom.smartweight.activity.main.model;

/**
 * 作者：罗发新
 * 时间：2019/8/9 0009    星期五
 * 邮件：424533553@qq.com
 * 说明：称重参数 Bean
 */
public class WeightFieldBean {

    protected int zeroAd;//0位ad
    protected int currentAd;// 当前Ad
    protected float currentWeight;// 当前重量,以kg为单位
    protected float frontWeight;// 前面的重量,以kg为单位
    // 当前时间
    private long currentTime;
    // 稳定起始时间
    private long frontTime;

    //单位kg  皮重
    protected float tareWeight;
    //    0：正常，1：作弊
    private int cheatSign;//作弊标志
    //    0：正，1：负
    protected int isNegative;//是否负数
    //    0：正常，1：超载
    private int isOver;//是否超重
    //    0：非零，1：零位
    private int isZero;//是否零位
    //    0：非去皮，1：去皮
    private int isPeeled;//是否去皮
    //    0：不稳定，1：稳定
    protected int isStable;//是否稳定

    public WeightFieldBean() {
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getFrontTime() {
        return frontTime;
    }

    public void setFrontTime(long frontTime) {
        this.frontTime = frontTime;
    }

    public float getFrontWeight() {
        return frontWeight;
    }

    public void setFrontWeight(float frontWeight) {
        this.frontWeight = frontWeight;
    }

    public int getZeroAd() {
        return zeroAd;
    }

    public void setZeroAd(int zeroAd) {
        this.zeroAd = zeroAd;
    }

    public int getCurrentAd() {
        return currentAd;
    }

    public void setCurrentAd(int currentAd) {
        this.currentAd = currentAd;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
    }

    public float getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(float tareWeight) {
        this.tareWeight = tareWeight;
    }

    public int getCheatSign() {
        return cheatSign;
    }

    public void setCheatSign(int cheatSign) {
        this.cheatSign = cheatSign;
    }

    /**
     * @return 是否   true:是负数
     */
    public boolean isNegative() {
        return isNegative != 0;
    }

    public void setIsNegative(int isNegative) {
        this.isNegative = isNegative;
    }

    public int getIsOver() {
        return isOver;
    }

    public void setIsOver(int isOver) {
        this.isOver = isOver;
    }

    public int getIsZero() {
        return isZero;
    }

    public void setIsZero(int isZero) {
        this.isZero = isZero;
    }

    public int getIsPeeled() {
        return isPeeled;
    }

    public void setIsPeeled(int isPeeled) {
        this.isPeeled = isPeeled;
    }

    public int getIsStable() {
        return isStable;
    }

    public void setIsStable(int isStable) {
        this.isStable = isStable;
    }
}
