//package com.axecom.smartweight.manager;
//
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.axecom.smartweight.base.SysApplication;
//import com.axecom.smartweight.bean.OrderLocal;
//
///**
// * Created by andy on 2018/10/13.
// */
//
//public class RecordDao {
//
//    private static SQLiteDatabase mWritableDatabase;
//
//    public synchronized SQLiteDatabase getInstance() {
//        if (mWritableDatabase == null) {
//            FeedReaderDbHelper helper = new FeedReaderDbHelper(SysApplication.getContext());
//            mWritableDatabase = helper.getWritableDatabase();
//        }
//        return mWritableDatabase;
//    }
//
//
//    public void insertOrder(OrderLocal orderLocal) {
//        getInstance().beginTransaction();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(OrderLocal.Entry.companyName, orderLocal.companyName);
//        contentValues.put(OrderLocal.Entry.operator, orderLocal.operator);
//        contentValues.put(OrderLocal.Entry.orderNumber, orderLocal.orderNumber);
//        contentValues.put(OrderLocal.Entry.orderTime, orderLocal.orderTime);
//        contentValues.put(OrderLocal.Entry.payId, orderLocal.payId);
//        contentValues.put(OrderLocal.Entry.qrCode, orderLocal.qrCode);
//        contentValues.put(OrderLocal.Entry.seller, orderLocal.seller);
//        contentValues.put(OrderLocal.Entry.sellerid, orderLocal.sellerid);
//        contentValues.put(OrderLocal.Entry.marketId, orderLocal.marketId);
//        contentValues.put(OrderLocal.Entry.tid, orderLocal.tid);
//        contentValues.put(OrderLocal.Entry.stallNumber, orderLocal.stallNumber);
//        contentValues.put(OrderLocal.Entry.totalAmount, orderLocal.totalAmount);
//        getInstance().insertOrThrow(OrderLocal.Entry.TABLE_NAME, null, contentValues);
//        getInstance().setTransactionSuccessful();
//    }
//
//    public void getOrderLlist(){
//
////        execSQL("select * from");
////        rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});
//
//
//    }
//
//
//}
