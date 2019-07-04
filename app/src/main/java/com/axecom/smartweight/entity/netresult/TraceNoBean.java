package com.axecom.smartweight.entity.netresult;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

/**
 * author: luofaxin
 * date： 2018/10/27 0027.
 * email:424533553@qq.com
 * describe: 描述功能
 */
@DatabaseTable(tableName = "traceNo")
public class TraceNoBean {

    /**
     * traceno : 10242018145810591
     * shid : 1136
     * typeid : 151
     */

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String traceno;
    @DatabaseField
    private int shid;
    @DatabaseField
    private int typeid;

    public TraceNoBean() {
    }

    public TraceNoBean(int id, String traceno, int shid, int typeid) {

        this.id = id;
        this.traceno = traceno;
        this.shid = shid;
        this.typeid = typeid;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceNoBean that = (TraceNoBean) o;
        return shid == that.shid &&
                typeid == that.typeid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shid, typeid);
    }
}
