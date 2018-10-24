package com.axecom.smartweight.my.entity;

/**
 * author: luofaxin
 * date： 2018/10/17 0017.
 * email:424533553@qq.com
 * describe:
 */
public class TT {


    /**
     * status : 0
     * msg : ok
     * data : {"marketid":10,"marketname":"钟屋市场","companyno":"B0057","tid":103,"seller":"张三1","sellerid":128,"key":"24","mchid":"13"}
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
         * marketid : 10
         * marketname : 钟屋市场
         * companyno : B0057
         * tid : 103
         * seller : 张三1
         * sellerid : 128
         * key : 24
         * mchid : 13
         */

        private int marketid;
        private String marketname;
        private String companyno;
        private int tid;
        private String seller;
        private int sellerid;
        private String key;
        private String mchid;

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
}
