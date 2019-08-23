package com.axecom.smartweight.entity.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.axecom.smartweight.mvvm.model.GoodsTradeBean;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/5/28 0028    星期二
 * 邮件：424533553@qq.com
 * 说明：GoodsTradeBean  的 数据库dao类
 */
//注解配置sql语句, dao 层
@Dao
public interface GoodsTradeDao {
    //所有的CURD根据primary key进行匹配
    String TABLE_NAME = "goodsTrade";

    //------------------------query------------------------
    // 简单sql语句，查询user表所有的column
    @Query("SELECT * FROM " + TABLE_NAME)
    List<GoodsTradeBean> getAll();

    //    //根据条件查询，方法参数和注解的sql语句参数一一对应
    //    @Query("SELECT * FROM " + TABLE_NAME + " WHERE uid IN (:userIds)")
    //    List<User> loadAllByIds(int[] userIds);

    // 同上
    //    @Query("SELECT * FROM user WHERE first_name LIKE :first AND "
    //            + "last_name LIKE :last LIMIT 1")
    //    GoodsTradeBean findByName(String first, String last);

    //同上
    @Query("SELECT * FROM " + TABLE_NAME + " WHERE mid = :id")
    GoodsTradeBean findById(int id);

    //----------------------- insert ----------------------
    // OnConflictStrategy.REPLACE表示如果已经有数据，那么就覆盖掉
    // 数据的判断通过主键进行匹配，也就是uid，非整个user对象
    // 返回Long数据表示，插入条目的主键值（uid）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GoodsTradeBean user);

    //返回List<Long>数据表示被插入数据的主键uid列表
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(GoodsTradeBean... users);

    //返回List<Long>数据表示被插入数据的主键uid列表
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<GoodsTradeBean> users);

    // --------------------- update ------------------------
    // 更新已有数据，根据主键（uid）匹配，而非整个user对象
    // 返回类型int代表更新的条目数目，而非主键uid的值。
    // 表示更新了多少条目
    @Update()
    int update(GoodsTradeBean user);

    @Update()
    int updateAll(GoodsTradeBean... user);

    @Update()
    int updateAll(List<GoodsTradeBean> user);

    //-------------------delete-------------------
    // 删除user数据，数据的匹配通过主键uid实现。
    // 返回int数据表示删除了多少条。非主键uid值。
    @Delete
    int delete(GoodsTradeBean user);

    @Delete
    int deleteAll(List<GoodsTradeBean> users);

    @Delete
    int deleteAll(GoodsTradeBean... users);
}
