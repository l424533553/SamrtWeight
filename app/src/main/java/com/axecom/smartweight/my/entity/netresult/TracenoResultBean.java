package com.axecom.smartweight.my.entity.netresult;

import java.util.List;

/**
 * author: luofaxin
 * date： 2018/10/25 0025.
 * email:424533553@qq.com
 * describe:
 */
public class TracenoResultBean {


    /**
     * status : 0
     * msg : ok
     * data : [{"traceno":"10242018145810591","shid":1136,"typeid":151},{"traceno":"3434334","shid":1136,"typeid":154},{"traceno":"10242018153345886","shid":1136,"typeid":157}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * traceno : 10242018145810591
         * shid : 1136
         * typeid : 151
         */

        private String traceno;
        private int shid;
        private int typeid;

        public String getTraceno() {
            return traceno;
        }

        public void setTraceno(String traceno) {
            this.traceno = traceno;
        }

        public int getShid() {
            return shid;
        }

        public void setShid(int shid) {
            this.shid = shid;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
    }
}
