//package com.axecom.smartweight.my.entity.dao;
//
//import com.j256.ormlite.field.DatabaseField;
//import com.j256.ormlite.table.DatabaseTable;
//
///**
// * author: luofaxin
// * date： 2018/10/16 0016.
// * email:424533553@qq.com
// * describe:
// */
//
//@DatabaseTable(tableName="student")
//public class Student {
//
//    //用户编号
//    @DatabaseField(generatedId=true)
//    private int id;
//    //用户名
//    @DatabaseField
//    private String userName;
//
//    //用户所属部门
//    /**
//     * foreign = true:说明这是一个外部引用关系
//     * foreignAutoRefresh = true：当对象被查询时，外部属性自动刷新（暂时我也没看懂其作用）
//     *
//     */
//    @DatabaseField(foreign = true,foreignAutoRefresh = true)
//    private Teacher dept;
//
//
//
//    public Student() {
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public Teacher getDept() {
//        return dept;
//    }
//
//    public void setDept(Teacher dept) {
//        this.dept = dept;
//    }
//}
