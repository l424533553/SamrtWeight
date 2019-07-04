package com.axecom.smartweight.entity.fpms_chinaap;

/**
 * 作者：罗发新
 * 时间：2019/2/12 0012    13:50
 * 邮件：424533553@qq.com
 * 说明：
 */
public class LoginFpmsInfo {


    /**
     * result : {"message":"操作成功","success":true}
     * logintime : 2019-02-12 09:31:35
     * expiretime : 2019-02-12 21:31:35
     * thirdKey : Y1TC7LL82YSNTV9TWPXXBNP3
     * authenCode : fpms_vender_axb
     * datakey : aefa140f2e2545f78812cf14
     */

    private ResultBean result;
    private String logintime;
    private String expiretime;
    private String thirdKey;
    private String authenCode;
    private String datakey;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result1) {
        result = result1;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public String getThirdKey() {
        return thirdKey;
    }

    public void setThirdKey(String thirdKey) {
        this.thirdKey = thirdKey;
    }

    public String getAuthenCode() {
        return authenCode;
    }

    public void setAuthenCode(String authenCode) {
        this.authenCode = authenCode;
    }

    public String getDatakey() {
        return datakey;
    }

    public void setDatakey(String datakey) {
        this.datakey = datakey;
    }

    public static class ResultBean {
        /**
         * message : 操作成功
         * success : true
         */

        private String message;
        private boolean success;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}

// 签到请求
// http://fpms.chinaap.com/admin/trade?executor=http&appCode=FPMSWS&data=1338EC0CF5D288B7F5694100704A1978E7E2506C8D7EE17E94FD4BFA7C6AD12EC00EA1F620E91FBD40A3ACB407A2E0D048E0A976CEC4AB9A5656A17BB56A2755007BC3F4E143AD90A4DF50EAFDBF1A13
