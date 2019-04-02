package com.axecom.smartweight.carouselservice.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * author: luofaxin
 * date： 2018/11/12 0012.
 * email:424533553@qq.com
 * describe:
 */
@DatabaseTable(tableName = "AdUserbean")
public class AdUserBean {

    /**
     * licence : ups/uploads/file/20181112/IMG_20180713_110859.jpg;ups/uploads/file/20181112/IMG_20180713_110849.jpg;ups/uploads/file/20181112/IMG_20180713_110846.jpg;ups/uploads/file/20181112/IMG_20180713_110842.jpg;ups/uploads/file/20181112/IMG_20180713_110839.jpg;ups/uploads/file/20181112/IMG_20180713_110836.jpg;
     * ad : ups/uploads/file/20181112/IMG_20180713_111513.jpg;ups/uploads/file/20181112/IMG_20180713_111437.jpg;ups/uploads/file/20181112/IMG_20180713_111411.jpg;ups/uploads/file/20181112/IMG_20180713_111348.jpg;ups/uploads/file/20181112/IMG_20180713_111346.jpg;ups/uploads/file/20181112/IMG_20180713_111319.jpg;ups/uploads/file/20181112/IMG_20180713_111315.jpg;
     * photo : assets/files/20181112135404790.jpg
     * companyno : A066
     * introduce : 蔬菜档
     * companyname : 胡启城
     * linkphone : 15818546414
     * companyid : 1126
     * baseurl : https://data.axebao.com/smartsz/
     */
    @DatabaseField(id = true)
    private int id;
    private String licence;
    private String ad;
    private String photo;
    @DatabaseField
    private String companyno;
    @DatabaseField
    private String introduce;
    @DatabaseField
    private String companyname;
    @DatabaseField
    private String linkphone;
    @DatabaseField
    private String companyid;
    @DatabaseField
    private String adcontent;
    private String baseurl;
    private String status;

    public AdUserBean() {
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdcontent() {
        return adcontent;
    }

    public void setAdcontent(String adcontent) {
        this.adcontent = adcontent;
    }

    public String getLicence() {
        return licence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCompanyno() {
        return companyno;
    }

    public void setCompanyno(String companyno) {
        this.companyno = companyno;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getLinkphone() {
        return linkphone;
    }

    public void setLinkphone(String linkphone) {
        this.linkphone = linkphone;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    @Override
    public String toString() {
        return "AdUserBean{" +
                "id=" + id +
                ", licence='" + licence + '\'' +
                ", ad='" + ad + '\'' +
                ", photo='" + photo + '\'' +
                ", companyno='" + companyno + '\'' +
                ", introduce='" + introduce + '\'' +
                ", companyname='" + companyname + '\'' +
                ", linkphone='" + linkphone + '\'' +
                ", companyid='" + companyid + '\'' +
                ", adcontent='" + adcontent + '\'' +
                ", baseurl='" + baseurl + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
