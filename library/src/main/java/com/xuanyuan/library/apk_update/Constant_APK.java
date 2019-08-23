package com.xuanyuan.library.apk_update;


import android.content.Context;
import android.os.Environment;


/**
 * 创建时间：2018/3/7
 * 编写人：czw
 * 功能描述 ：
 */

public class Constant_APK {

    //    String APP_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    public static final String DOWNLOAD_DIR = "/downlaod/";

    public static String getRootPath(Context context) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName();
    }
}
