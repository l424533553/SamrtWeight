package com.xuanyuan.library.entity;

/**
 * 作者：罗发新
 * 时间：2018/12/6 0006    9:51
 * 邮件：424533553@qq.com
 * 说明：
 */
public class Errorinfo {

    String classpath = "";
    String errormessage = "";
    String errortime = " ";

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getErrortime() {
        return errortime;
    }

    public void setErrortime(String errortime) {
        this.errortime = errortime;
    }
}
