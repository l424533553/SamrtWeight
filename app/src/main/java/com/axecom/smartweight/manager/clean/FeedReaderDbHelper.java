//package com.axecom.smartweight.manager;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//
//
///**
// * Created by andy on 2018/10/13.
// */
//
//public class FeedReaderDbHelper extends SQLiteOpenHelper {
//    // If you change the database schema, you must increment the database version.
//    public static final int DATABASE_VERSION = 1;
//    public static final String DATABASE_NAME = "FeedReader.db";
//    private static final String SQL_CREATE_ORDER =
//            "CREATE TABLE " + OrderLocal.Entry.TABLE_NAME + " (" +
//                    OrderLocal.Entry.id + " INTEGER PRIMARY KEY," +
//                    OrderLocal.Entry.companyName + " TEXT," +
//                    OrderLocal.Entry.operator + " TEXT," +
//                    OrderLocal.Entry.orderNumber + " TEXT," +
//                    OrderLocal.Entry.orderTime + " TEXT," +
//                    OrderLocal.Entry.payId + " TEXT," +
//                    OrderLocal.Entry.qrCode + " TEXT," +
//                    OrderLocal.Entry.seller + " TEXT," +
//                    OrderLocal.Entry.sellerid + " INT," +
//                    OrderLocal.Entry.marketId + " INT," +
//                    OrderLocal.Entry.tid + " INT," +
//                    OrderLocal.Entry.stallNumber + " TEXT," +
//                    OrderLocal.Entry.totalAmount + " DOUBLE)";
//
//    private static final String SQL_CREATE_GOODS =
//            "CREATE TABLE " + OrderGoods.Entry.TABLE_NAME + " (" +
//                    OrderGoods.Entry.id + " INTEGER PRIMARY KEY," +
//                    OrderGoods.Entry.name + " TEXT," +
//                    OrderGoods.Entry.price + " FLOAT," +
//                    OrderGoods.Entry.weight + " FLOAT," +
//                    OrderGoods.Entry.count + " INT," +
//                    OrderGoods.Entry.countType + " INT," +
//                    OrderGoods.Entry.amount + " DOUBLE," +
//                    OrderGoods.Entry.orderNumber + " TEXT)";
//
//    public FeedReaderDbHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(SQL_CREATE_ORDER);
//        db.execSQL(SQL_CREATE_GOODS);
//    }
//
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // This database is only a cache for online data, so its upgrade policy is
//        // to simply to discard the data and start over
////        db.execSQL(SQL_DELETE_ENTRIES);
//        onCreate(db);
//    }
//
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onUpgrade(db, oldVersion, newVersion);
//    }
//}
