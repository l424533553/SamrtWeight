package com.axecom.smartweight.entity.room;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * 作者：罗发新
 * 时间：2019/5/29 0029    星期三
 * 邮件：424533553@qq.com
 * 说明：
 */

public class DBInstance {
    private static final String DB_NAME = "room_db";
    private static AppDataBase appDataBase;

    public static AppDataBase getInstance(Context context) {
        if (appDataBase == null) {
            synchronized (DBInstance.class) {
                if (appDataBase == null) {
                    return Room.databaseBuilder(context, AppDataBase.class, DB_NAME)
                            //添加下面这一行会删掉之前的表结构, 更新数据库方案一
                            .fallbackToDestructiveMigration()
                            //增加下面这一行  ，更新数据库方案二
//                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return appDataBase;
    }

//    /**
//     * 使用migration  测试
//     */
//    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            //此处对于数据库中的所有更新都需要写下面的代码
////            database.execSQL("ALTER TABLE users " + " ADD COLUMN last_update INTEGER");
//        }
//    };
//
//    public static void setAppDataBase(AppDataBase appDataBase) {
//        DBInstance.appDataBase = appDataBase;
//    }

}
