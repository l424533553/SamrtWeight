package com.axecom.smartweight.entity.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * author: luofaxin
 * date： 2018/10/16 0016.
 * email:424533553@qq.com
 * describe:
 */

@DatabaseTable(tableName="stud")
public class Stud {

    //用户编号
    @DatabaseField(generatedId=true)
    private int id;
    //用户名
    @DatabaseField
    private String userName;

    public Stud() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
