//package com.axecom.smartweight.base;
//
//import com.axecom.smartweight.bean.HotKeyBean;
//import com.raizlabs.android.dbflow.annotation.Database;
//import com.raizlabs.android.dbflow.annotation.Migration;
//import com.raizlabs.android.dbflow.sql.SQLiteType;
//import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
//
///**
// * Created by Administrator on 2018/7/22.
// */
//
//@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
//public final class AppDatabase {
//    public static final String NAME = "AppDatabase";
//
//    public static final int VERSION = 2;
//
//    @Migration(version = 2, database = AppDatabase.class)
//    public static class Migration2PS extends AlterTableMigration<HotKeyBean> {
//
//        public Migration2PS(Class<HotKeyBean> table) {
//            super(table);
//        }
//
//        @Override
//        public void onPreMigrate() {
//            addColumn(SQLiteType.TEXT, "batch_code");
//        }
//    }
//
//}
