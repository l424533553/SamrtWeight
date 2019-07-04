package com.axecom.smartweight.entity.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.axecom.smartweight.mvvm.model.GoodsTradeBean;

/**
 * 作者：罗发新
 * 时间：2019/5/29 0029    星期三
 * 邮件：424533553@qq.com
 * 说明：
 */

@Database(entities = {GoodsTradeBean.class}, version = 3, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract GoodsTradeDao getGoodsTradeDao();

}
