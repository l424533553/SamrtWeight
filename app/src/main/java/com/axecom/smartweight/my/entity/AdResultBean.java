package com.axecom.smartweight.my.entity;

import java.io.Serializable;

public class AdResultBean {
    /**
     * status : 0
     * msg : ok
     * data : {"id":1,"marketid":11,"title":"test","content":"1212121","vdate":"2018-11-21","nickname":"axecom","marketname":"公明市场"}
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

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * marketid : 11
         * title : test
         * content : 1212121
         * vdate : 2018-11-21
         * nickname : axecom
         * marketname : 公明市场
         */

        private int id;
        private int marketid;
        private String title;
        private String content;
        private String vdate;
        private String nickname;
        private String marketname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMarketid() {
            return marketid;
        }

        public void setMarketid(int marketid) {
            this.marketid = marketid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVdate() {
            return vdate;
        }

        public void setVdate(String vdate) {
            this.vdate = vdate;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMarketname() {
            return marketname;
        }

        public void setMarketname(String marketname) {
            this.marketname = marketname;
        }
    }
}
