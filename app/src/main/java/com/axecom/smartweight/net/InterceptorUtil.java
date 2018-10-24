package com.axecom.smartweight.net;


import com.axecom.smartweight.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017-11-29.
 */

public class InterceptorUtil {
    public static HttpLoggingInterceptor LogInterceptor(){
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.d(message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static Interceptor HeaderInterceptor(){
        return  new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
//                        .addHeader("Token", Token)
                        .build();
                Response mResponse = chain.proceed(request);
                //假设返回的code为201时表示token失效
//                if (mResponse.code()==201){
//                    //重新获取新token
//                    //这用了一个特殊接口来获取新的Token
//                    Call<String> call = RetrofitFactory.getInstence().API().loginByToken("123456", Token);
//                    //拿到请求体
//                    Request tokenRequest = call.request();
//                    //获取响应体
//                    Response tokenResponse = chain.proceed(tokenRequest);
//                    //我这假设新的token是在header里返回的
//                    //我们拿到新的token头
//                    List<String> listHeader = tokenResponse.headers().values("Token");
//                    if (listHeader != null) {
//                        //重新赋值到新的token
//                        Token = listHeader.get(0);
//                    }
//
//                    //这是只需要替换掉之前的token重新请求接口就行了
//                    Request newRequest = mRequest.newBuilder()
//                            .header("Token", Token)
//                            .build();
//                    return chain.proceed(newRequest);
//                }
                return mResponse;
            }
        };
    }
}
