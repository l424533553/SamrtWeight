package com.axecom.smartweight.my.entity.netresult;

/**
 * author: luofaxin
 * date： 2018/10/24 0024.
 * email:424533553@qq.com
 * describe:
 */
public class OrderResultBean {


    /**
     * status : 0
     * msg : ok1
     * data : {"billstatus":"待支付","billcode":"AX20181024093038103"}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * billstatus : 待支付
         * billcode : AX20181024093038103
         */

        private String billstatus;
        private String billcode;

        public String getBillstatus() {
            return billstatus;
        }

        public void setBillstatus(String billstatus) {
            this.billstatus = billstatus;
        }

        public String getBillcode() {
            return billcode;
        }

        public void setBillcode(String billcode) {
            this.billcode = billcode;
        }
    }
}
