package com.axecom.smartweight.test;


import android.support.annotation.NonNull;

import com.axecom.smartweight.entity.project.OrderBean;

import java.util.Objects;

/**
 * 作者：罗发新
 * 时间：2019/1/4 0004    16:15
 * 邮件：424533553@qq.com
 * 说明：
 */
public class Student implements Cloneable {
    private String name;
    private int age;

    @Override
    public Student clone() {
        Student stu = null;
        try {
            stu = (Student) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }


    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age &&
                Objects.equals(name, student.name);
    }

    // 数据功能  代码使用
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }


    @NonNull
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
