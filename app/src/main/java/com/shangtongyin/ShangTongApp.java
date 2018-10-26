package com.shangtongyin;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.luofx.base.MyBaseApplication;
import com.luofx.utils.PreferenceUtils;
import com.shangtongyin.tools.serialport.EpsPrint;
import com.shangtongyin.tools.serialport.USB_Print;

/**
 * Fun：
 * Author：Linus_Xie
 * Date：2018/8/2 14:55
 */
public class ShangTongApp extends MyBaseApplication {




    private EpsPrint epsPrint;

    public EpsPrint getEpsPrint() {
        return epsPrint;
    }


    public ShangTongApp() {
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



    @Override
    public void onCreate() {
        super.onCreate();

        epsPrint = new EpsPrint();
        epsPrint.open();
//        registerBroadcast();
        USB_Print.initPrinter(this);
        SharedPreferences sharedPreferences = PreferenceUtils.getSp(this);

    }

    /**
     * 最终关闭串口
     */
    public void onDestory() {
        if (epsPrint != null) {
            epsPrint.closeSerialPort();
        }
    }

    protected int marketid;

    protected String marketname;

    protected String companyno;

    protected int tid;

    protected String seller;

    protected int sellerid;

    protected String key;

    protected String mchid;

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

    public void setKey(String key) {
        this.key = key;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }
}
