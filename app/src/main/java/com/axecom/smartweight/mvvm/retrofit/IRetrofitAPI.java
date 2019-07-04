package com.axecom.smartweight.mvvm.retrofit;

import com.alibaba.fastjson.JSONObject;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.mvvm.entity.FamousInfo;
import com.axecom.smartweight.mvvm.entity.ResultRtInfo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

/**
 * 作者：罗发新
 * 时间：2019/5/31 0031    星期五
 * 邮件：424533553@qq.com
 * 说明：Call 是普通的方法，Observable 是Rx的请求
 */
public interface IRetrofitAPI {

    //IP地址
    String BASE_IP_HOST = "http://119.23.43.64/";
    String BASE_IP_WEB = "https://data.axebao.com/smartsz";

    //可以在 URL 中指定查询参数
    @GET("api/smart/getinfobymac?")
    Call<JSONObject> getResultInfo(@Query("desdata") String desdata);

    @GET("/avatardata/mingrenmingyan/lookup")
    Call<FamousInfo> getFamousResult(@Header("apiKey") String apiKey,
                                     @Query("keyword") String keyword,
                                     @Query("page") int page,
                                     @Query("rows") int rows);

    /*************************************************************************/

    @GET("api/smart/getinfobymac?")
    Observable<ResultRtInfo<UserInfo>> getUserInfo(@Query("desdata") String desdata);


    // 如下带有请求头的 请求
    @Headers("Cache-Control: max-age=120")
    @GET("请求地址")
    Observable<ResultRtInfo> getInfo();

    @GET("请求地址")
    Observable<ResultRtInfo> getInfo(@Header("token") String token);

    /**
     * 请求参数 一次性传入，参数都是    ?key=vale%key=value... 形式传入
     */
    @GET("v2/movie/in_theaters")
    Observable<ResultRtInfo> getPlayingMovie(@QueryMap Map<String, String> map);

    /**
     * 以"/value"的方式拼接到地址后面（restful模式？） 则以如下的形式
     */
    @GET("v2/movie/in_theaters/{start}/{count}")
    Observable<ResultRtInfo> getPlayingMovie(@Path("start") int start, @Path("count") int count);

    /**
     * POST 请求     Start
     ******************************************/
    @FormUrlEncoded
    @POST("请求地址")
    Observable<ResultRtInfo> getInfo(@Field("token") String token, @Field("id") int id);

    @FormUrlEncoded
    @POST("请求地址")
    Observable<ResultRtInfo> getInfo(@FieldMap Map<String, String> map);

    /**
     * 上传文本 和 单个文件
     */
    /**
     * @param textBody @Part("textKey")中的"textKey"为文本参数的参数名。
     *                 RequestBody textBody为文本参数的参数值，生成方式如下：
     *                 RequestBody textBody = RequestBody.create(MediaType.parse("text/plain"), text);
     * @param fileBody 其中的"fileKey"为文件参数的参数名（由服务器后台提供）
     *                 其中的"test.png"一般是指你希望保存在服务器的文件名字，传入File.getName()即可
     *                 RequestBody fileBody  为文件参数的参数值，生成方法如下：
     *                 RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
     */
    @Multipart
    @POST("请求地址")
    Observable<ResultRtInfo> upLoadTextAndFile(@Part("textKey") RequestBody textBody,
                                               @Part("fileKey\"; filename=\"test.png") RequestBody fileBody);

    /**
     * 上传多个文本和多个文件 ,参考单个上传的 ，一一对应
     */
    @Multipart
    @POST("请求地址")
    Observable<ResultRtInfo> upLoadTextAndFiles(@PartMap Map<String, RequestBody> textBodyMap, @PartMap Map<String, RequestBody> fileBodyMap);


    /**
     * 下面是  MultipartBody的生成方式
     *
     * @param multipartBody MultipartBody.Builder builder = new MultipartBody.Builder();
     *                      //文本部分
     *                      builder.addFormDataPart("fromType", "1");
     *                      builder.addFormDataPart("content", "意见反馈内容");
     *                      builder.addFormDataPart("phone", "17700000066");
     *                      <p>
     *                      //文件部分
     *                      RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
     *                      builder.addFormDataPart("image", file.getName(), requestBody); // “image”为文件参数的参数名（由服务器后台提供）
     *                      <p>
     *                      builder.setType(MultipartBody.FORM);
     *                      MultipartBody multipartBody = builder.build();
     * @return
     */
    @POST("请求地址")
    Observable<ResultRtInfo> upLoadTextAndFiles(@Body MultipartBody multipartBody);

    /**
     * ResponseBody是Retrofit提供的返回实体，要下载的文件数据将包含在其中
     * 下载大文件时，请加上@Streaming，否则容易出现IO异常
     */
    @Streaming
    @GET("请求地址")
    Observable<ResponseBody> downloadFile();


    // 两种请求方式
//    //    @其他声明
////    //    @请求方式("api/smart/getinfobymac?") //需要baseurl后部分
////    Observable<JSONObject> getResultInfo();
////    //    @其他声明
////    //    @请求方式    //该方法中地址需要全地址  http://119.23.43.64/api/smart/getinfobymac?
////    Observable<JSONObject> getResultInfo(@Url String 请求地址，请求参数);


//    @GET("test.php")
//    Observable<String> test();
//    @POST("submitOrder")
//    Observable<BaseEntity<SubOrderBean>> submitOrder(@Body SubOrderReqBean subOrderReqBean);
//    @POST("submitOrders")
//    Observable<BaseEntity> submitOrders(@Body List<SubOrderReqBean> list);


//    @Multipart
//    @POST("http://api.stay4it.com/v1/public/core/?service=user.updateAvatar")
//    Observable<HttpResponse<String>> uploadFile(@Part("data") String des,
//                                                @PartMap Map<String, RequestBody> params);

}


//下面列举“客户端内置证书”时的配置方法，其他方式请参考
//        http://blog.csdn.net/dd864140130/article/details/52625666
//
//
//        （这里文件类型以png图片为例，所以MediaType为"image/png"，
//        不同文件类型对应不同的type，具体请参考http://tool.oschina.net/commons）
//
//
//        参考自https://zhuanlan.zhihu.com/p/21966621
//
//        示意图：