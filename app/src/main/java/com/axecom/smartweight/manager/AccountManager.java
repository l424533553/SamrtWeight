package com.axecom.smartweight.manager;

import android.content.Context;

import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.conf.Constants;
import com.axecom.smartweight.utils.SPUtils;

/**
 * 账号管理者
 */
public class AccountManager {
    private static final String LAST_SERIALNUMBER = "last_serialnumber";
    private static final String IS_SAVE_PWD = "is_save_pwd";
    private static AccountManager accountManager ;
    private String adminToken; 
    private String mToken;

    private static Context mCtx;
    private int userType;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    private AccountManager(){}

    public static AccountManager getInstance(){
        mCtx = SysApplication.getContext();
        if(accountManager==null){
            accountManager = new AccountManager();
        }
        return accountManager;
    }

    public String getToken() {
        if(mToken!=null){
            return mToken;
        }
        return SPUtils.getString(mCtx, Constants.USER_TOKEN, null);
    }

    public void saveToken(String token){
        SPUtils.putString(mCtx, Constants.USER_TOKEN, token);
    }

    public void saveScalesId(String scalesId){
        SPUtils.putString(mCtx, Constants.USER_SCALES_ID, scalesId);
    }

    public String getScalesId(){
        return SPUtils.getString(mCtx, Constants.USER_SCALES_ID, null);
    }

    public void saveRememberPwdState(boolean save){
        SPUtils.put(mCtx, IS_SAVE_PWD, save);
    }

    public boolean getRememberPwdState(){
        return (boolean)SPUtils.get(mCtx, IS_SAVE_PWD,false);
    }

    public void saveLastSerialNumber(String serialNumber){
        SPUtils.putString(mCtx, LAST_SERIALNUMBER, serialNumber);
    }

    public String getLastSerialNumber(){
       return SPUtils.getString(mCtx, LAST_SERIALNUMBER, "");
    }

    public void savePwd(String serialNumber, String pwd){
        SPUtils.putString(mCtx, serialNumber, pwd);
    }

    public String getPwdBySerialNumber(String serialNumber){
        return SPUtils.getString(mCtx, serialNumber, null);
    }

    public void savePwdChecked(String serialNumber, boolean isChecked){
        SPUtils.put(mCtx, serialNumber, isChecked);
    }

    public Boolean getPwdChecked(String serialNumber){
        return (Boolean) SPUtils.get(mCtx, serialNumber, null);
    }

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
    }

    public void loginOut() {
        SPUtils.putString(mCtx, Constants.USER_TOKEN, "");
    }
}
