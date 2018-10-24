package com.axecom.smartweight.bean;

public class SubOrderBean {
    private String order_no;
    private String print_code;
    private String print_code_img;
    private String code_img_url;

    public String getCode_img_url() {
        return code_img_url;
    }

    public void setCode_img_url(String code_img_url) {
        this.code_img_url = code_img_url;
    }

    public String getPrint_code_img() {
        return print_code_img;
    }

    public void setPrint_code_img(String print_code_img) {
        this.print_code_img = print_code_img;
    }

    public String getPrint_code() {
        return print_code;
    }

    public void setPrint_code(String print_code) {
        this.print_code = print_code;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
