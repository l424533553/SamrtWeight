package com.xuanyuan.library.retrofit;

import android.content.Context;

import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.apk_update.Constant_APK;
import com.xuanyuan.library.apk_update.download.JsDownloadInterceptor;
import com.xuanyuan.library.base.BaseApplication;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 实用类     Retrofit框架
 */
public class RetrofitHttp {

    private static final int DEFAULT_TIMEOUT = 10;
    private static final String TAG = "RetrofitClient";

    private final ApiService apiService;
    private static RetrofitHttp sIsntance;

    private final Context appContext;

    /**
     * @return 获取单例对象
     */
    public static RetrofitHttp getInstance(Context context) {

        if (sIsntance == null) {
            synchronized (RetrofitHttp.class) {
                if (sIsntance == null) {
                    sIsntance = new RetrofitHttp(context);
                }
            }
        }
        return sIsntance;
    }

    private RetrofitHttp(Context context) {
        this.appContext = context.getApplicationContext();
        //TODO 请求 拦截器待完善
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)// 单位秒
                //TODO 慎重使用拦截器，原理不太清楚
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)  // 允许连接失败重试
                .build();
        String baseUrl = ApiService.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 添加RxJava的支持
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private DownloadCallBack downloadCallback;

    public void setDownloadCallback(DownloadCallBack downloadCallback) {
        this.downloadCallback = downloadCallback;
    }

    /**
     * 文件先下载的 方法 支持进度条的方式
     */
    public void downloadFile(final long range, final String url, final String fileName, final DownloadCallBack downloadCallback) {
        downloadCallback.onStartDownload();//开始下载了
        //断点续传时请求的总长度
        File file = new File(Constant_APK.getRootPath(appContext) + Constant_APK.DOWNLOAD_DIR, fileName);
        String totalLength = "-";
        if (file.exists()) {
            totalLength += file.length();
        }
        // ResponseBody 后面紧跟着多种方法， 需要后续跟进学习
        apiService.executeDownload("bytes=" + range + totalLength, url)
                .subscribeOn(Schedulers.io())  //指定Observable自身在哪个调度器上执行
                //TODO SDK 26 ==8.0  下面的内容在6.0 以下 需要屏蔽
//                .observeOn(AndroidSchedulers.mainThread())//指定一个观察者在哪个调度器上观察这个Observable
                .unsubscribeOn(Schedulers.io())  //  取消订阅
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressWarnings("ResultOfMethodCallIgnored")
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        RandomAccessFile randomAccessFile = null;
                        InputStream inputStream = null;
                        long total = range;
                        long responseLength;
                        try {
                            byte[] buf = new byte[2048];
                            int len;

                            responseLength = responseBody.contentLength();
                            inputStream = responseBody.byteStream();
                            String filePath = Constant_APK.getRootPath(appContext) + Constant_APK.DOWNLOAD_DIR;
                            File file = new File(filePath, fileName);
                            File dir = new File(filePath);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            randomAccessFile = new RandomAccessFile(file, "rwd");
                            if (range == 0) {
                                randomAccessFile.setLength(responseLength);
                            }
                            randomAccessFile.seek(range);

                            int progress = 0;
                            int lastProgress;
                            while ((len = inputStream.read(buf)) != -1) {
                                randomAccessFile.write(buf, 0, len);
                                total += len;
                                lastProgress = progress;
                                progress = (int) (total * 100 / randomAccessFile.length());
                                if (progress > 0 && progress != lastProgress) {
                                    downloadCallback.onProgress(progress);
                                }
                            }
                            downloadCallback.onCompleted();
                        } catch (Exception e) {
                            downloadCallback.onError(e.getMessage());
                            e.printStackTrace();
                        } finally {
                            try {
                                MyPreferenceUtils.getSp(BaseApplication.getInstance()).edit().putLong(url, total).apply();
                                if (randomAccessFile != null) {
                                    randomAccessFile.close();
                                }
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        downloadCallback.onError(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}