package com.axecom.smartweight.carouselservice.entity;

/**
 * author: luofaxin
 * date： 2018/11/12 0012.
 * email:424533553@qq.com
 * describe:
 */
public class AdUserInfoCopy {


    /**
     * status : 0
     * msg : ok
     * data : {
     * "licence":"ups/uploads/file/20181112/IMG_20180713_110859.jpg;ups/uploads/file/20181112/IMG_20180713_110849.jpg;ups/uploads/file/20181112/IMG_20180713_110846.jpg;ups/uploads/file/20181112/IMG_20180713_110842.jpg;ups/uploads/file/20181112/IMG_20180713_110839.jpg;ups/uploads/file/20181112/IMG_20180713_110836.jpg;",
     * "ad":"ups/uploads/file/20181112/IMG_20180713_111513.jpg;ups/uploads/file/20181112/IMG_20180713_111437.jpg;ups/uploads/file/20181112/IMG_20180713_111411.jpg;ups/uploads/file/20181112/IMG_20180713_111348.jpg;ups/uploads/file/20181112/IMG_20180713_111346.jpg;ups/uploads/file/20181112/IMG_20180713_111319.jpg;ups/uploads/file/20181112/IMG_20180713_111315.jpg;",
     * "photo":"assets/files/20181112135404790.jpg",
     * "companyno":"A066",
     * "introduce":"蔬菜档",
     * "companyname":"胡启城",
     * "linkphone":"15818546414",
     * "companyid":"1126",
     * "baseurl":"https://data.axebao.com/smartsz/"}
     */


    private int status;
    private String msg;
    private AdUserBean data;

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

    public AdUserBean getData() {
        return data;
    }

    public void setData(AdUserBean data) {
        this.data = data;
    }








}

