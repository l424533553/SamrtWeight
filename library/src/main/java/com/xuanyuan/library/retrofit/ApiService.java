package com.xuanyuan.library.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 创建时间：2018/3/7
 * 编写人：czw
 * 功能描述 ：
 */

public interface ApiService {
//    String BASE_URL = "http://dldir1.qq.com";

    String BASE_URL = "http://wap.dl.pinyin.sogou.com";

    @Streaming
    @GET
    Observable<ResponseBody> executeDownload(@Header("Range") String range, @Url() String url);

//    Call<ResponseBody>  df();

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}


