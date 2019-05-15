package com.axecom.smartweight.my.rzl.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//监控uri下载过程中的内容变化
public class DownloadChangeObserver extends ContentObserver {
    private final Handler _handler;//向ui线程发送消息
    private final Context _context;//下载管理器附属的context
    DownloadChangeObserver(Context context, Handler handler){
        super(handler);
        _handler=handler;
        _context=context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        DownloadManager.Query query=new DownloadManager.Query().setFilterById(ApkUtils.getInstance().getDownloadId());
        final DownloadManager dm=(DownloadManager) this._context.getSystemService(Context.DOWNLOAD_SERVICE);
        try (Cursor cs = dm.query(query)) {
            if (cs.moveToFirst()) {
                int downloadedBytes = cs.getInt(cs.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int totalBytes = cs.getInt(cs.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                Log.i("rzl", "download apk process " + downloadedBytes + "/" + totalBytes);
                Message msg = Message.obtain();
                msg.what = 10012;
                msg.arg1 = downloadedBytes;
                msg.arg2 = totalBytes;
                _handler.sendMessage(msg);
            }
        }
    }
}
