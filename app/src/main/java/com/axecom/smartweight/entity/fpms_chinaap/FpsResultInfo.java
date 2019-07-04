package com.axecom.smartweight.entity.fpms_chinaap;

/**
 * 作者：罗发新
 * 时间：2019/5/16 0016    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public class FpsResultInfo {


    /**
     * result : {"code":"OT-DATAKEY","message":"dataKey失效","success":false,"type":3}
     * result : {"message":"操作成功","success":true}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * code : OT-DATAKEY
         * message : dataKey失效
         * success : false
         * type : 3
         */

        private String code;
        private String message;
        private boolean success;
        private int type;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
