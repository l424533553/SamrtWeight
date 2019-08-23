package com.xuanyuan.library.retrofit;

/**
 * 创建时间：2018/3/7
 * 编写人：
 * 功能描述 ：下载的 回调函数
 */

public interface DownloadCallBack {

    //开始下载
    void onStartDownload();

    //正在下载
    void onProgress(int progress);

    // 下载完成
    void onCompleted();

    // 下载错误
    void onError(String msg);

}
