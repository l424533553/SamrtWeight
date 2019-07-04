package com.axecom.smartweight.mvvm.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * 作者：罗发新
 * 时间：2019/5/28 0028    星期二
 * 邮件：424533553@qq.com
 * 说明：商品明细 统计类 实体 ，显示某菜品一天中的交易笔数，总价，总重，及平均价
 */
//默认表名不区分大小小
@Entity(tableName = "goodsTrade")
public class GoodsTradeBean {
    //设置主键，并且定义自增增
    @PrimaryKey(autoGenerate = true)
    //字段映射具体的数据表字段名
    @ColumnInfo(name = "mid")
    private int mid;
    //    @ForeignKey 外键约束
    private String goodName;
    private int tradeCount;
    private String totalMoney;
    private String totalWeight;
    private String price;
    private String tradeDate;
    //进行忽略处理，因为Room默认每个字段会生成一个表字段
    @Ignore
    private int state;

//  @Embedded  实体类中引用其他实体类。
//  private List<UserInfo> userInfo;

    //必须指定一个构造方法，room框架需要。并且只能指定一个
//，如果有其他构造方法，则其他的构造方法必须添加@Ignore注解
    public GoodsTradeBean() {

    }

    //其他构造方法要添加@Ignore注解
    @Ignore
    public GoodsTradeBean(int mid) {
        this.mid = mid;
    }


    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(int tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
