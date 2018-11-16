package com.axecom.smartweight.my.entity.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.axecom.smartweight.carouselservice1.entity.AdImageInfo;
import com.axecom.smartweight.carouselservice1.entity.AdUserBean;
import com.axecom.smartweight.my.entity.AllGoods;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.GoodsType;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.axecom.smartweight.my.entity.netresult.TraceNoBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明：  Ormlite 的基础工具类
 * 作者：User_luo on 2018/4/23 10:26
 * 邮箱：424533553@qq.com
 * 数据库操作管理工具类
 * <p>
 * 我们需要自定义一个类继承自ORMlite给我们提供的OrmLiteSqliteOpenHelper，创建一个构造方法，重写两个方法onCreate()和onUpgrade()
 * 在onCreate()方法中使用TableUtils类中的createTable()方法初始化数据表
 * 在onUpgrade()方法中我们可以先删除所有表，然后调用onCreate()方法中的代码重新创建表
 * <p>
 * 我们需要对这个类进行单例，保证整个APP中只有一个SQLite Connection对象
 * <p>
 * 这个类通过一个Map集合来管理APP中所有的DAO，只有当第一次调用这个DAO类时才会创建这个对象（并存入Map集合中）
 * 其他时候都是直接根据实体类的路径从Map集合中取出DAO对象直接调用
 */
public class OrmliteBaseHelper extends OrmLiteSqliteOpenHelper {
    // 数据库名称
    private static String DATABASE_NAME = "r2011.db";

    public final static int version = 3;

    // 本类的单例实例
    private static OrmliteBaseHelper instance;

    // 存储APP中所有的DAO对象的Map集合
    private Map<String, Dao> daos = new HashMap<>();

    // 获取本类单例对象的方法
    public static synchronized OrmliteBaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (OrmliteBaseHelper.class) {
                if (instance == null) {
                    instance = new OrmliteBaseHelper(context);
                }
            }
        }
        return instance;
    }

    // 私有的构造方法
    private OrmliteBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    // 根据传入的DAO的路径获取到这个DAO的单例对象（要么从daos这个Map中获取，要么新创建一个并存入daos）
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override // 创建数据库时调用的方法
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
//            TableUtils.createTable(connectionSource, Goods.class);

            TableUtils.createTable(connectionSource, OrderInfo.class);
            TableUtils.createTable(connectionSource, OrderBean.class);
            TableUtils.createTable(connectionSource, Teacher.class);
            TableUtils.createTable(connectionSource, UserInfo.class);
            TableUtils.createTable(connectionSource, Goods.class);
            TableUtils.createTable(connectionSource, GoodsType.class);
            TableUtils.createTable(connectionSource, AllGoods.class);
            TableUtils.createTable(connectionSource, TraceNoBean.class);

            TableUtils.createTable(connectionSource, AdImageInfo.class);
            TableUtils.createTable(connectionSource, AdUserBean.class);

//            TableUtils.createTable(connectionSource, TimeBase.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override // 数据库版本更新时调用的方法
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        try {

        if (oldVersion < 2) {
            try {
                TableUtils.createTable(connectionSource, TraceNoBean.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //增加表 的列
//        if (oldVersion == 2) {
//            String sql2 = "ALTER TABLE `GoodsType` ADD COLUMN traceno VARCHAR";
//            database.execSQL(sql2);
//
////            getUserDao().executeRaw();
//        }else if(oldVersion == 1){
//            String sql = "ALTER TABLE `orderinfo` ADD COLUMN state INTEGER DEFAULT 0";
//            database.execSQL(sql);
//
//            String sql2 = "ALTER TABLE `GoodsType` ADD COLUMN traceno VARCHAR";
//            database.execSQL(sql2);
//        }

        // 数据库更新 删除表 ,在重建
//            TableUtils.dropTable(connectionSource, Goods.class, true);
//            TableUtils.dropTable(connectionSource, OrderInfo.class, true);
//            TableUtils.dropTable(connectionSource, OrderBean.class, true);
//            TableUtils.dropTable(connectionSource, AdUserInfo.class, true);
//            TableUtils.dropTable(connectionSource, Goods.class, true);
//            TableUtils.dropTable(connectionSource, GoodsType.class, true);
//            TableUtils.dropTable(connectionSource, AllGoods.class, true);
//            onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    // 释放资源
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            if (dao != null) {
                dao = null;
            }
        }
    }
}
