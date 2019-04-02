//package com.axecom.smartweight.my.entity.dao;
//
//import com.j256.ormlite.dao.ForeignCollection;
//import com.j256.ormlite.field.DatabaseField;
//import com.j256.ormlite.field.ForeignCollectionField;
//import com.j256.ormlite.table.DatabaseTable;
//
///**
// * author: luofaxin
// * date： 2018/10/16 0016.
// * email:424533553@qq.com
// * describe:
// */
//@DatabaseTable(tableName = "teacher")
//public class Teacher {
//
//    //部门编号
//    @DatabaseField(generatedId = true)
//    private int deptId;
//    //部门名称
//    @DatabaseField
//    private String deptName;
//    //用户信息集合
//    @ForeignCollectionField
//    /**
//     * 这里需要注意的是：属性类型只能是ForeignCollection<T>或者Collection<T>
//     * 如果需要懒加载（延迟加载）可以在@ForeignCollectionField加上参数eager=false
//     * 这个属性也就说明一个部门对应着多个用户
//     */
//    private ForeignCollection<Student> students;
//
//    public Teacher() {
//    }
//
//    public int getDeptId() {
//        return deptId;
//    }
//
//    public void setDeptId(int deptId) {
//        this.deptId = deptId;
//    }
//
//    public String getDeptName() {
//        return deptName;
//    }
//
//    public void setDeptName(String deptName) {
//        this.deptName = deptName;
//    }
//
//    public ForeignCollection<Student> getStudents() {
//        return students;
//    }
//
//    public void setStudents(ForeignCollection<Student> students) {
//        this.students = students;
//    }
//
//}
