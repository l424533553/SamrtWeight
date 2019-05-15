package com.axecom.smartweight.net;


import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017-11-27.
 */

public class NetEngine {
    static Retrofit retrofit;

//    public NetEngine() {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//    }
//
//    public static AdyenUserInfoBean postRequest() {
//        final AdyenUserInfoBean[] adyenUserInfo = {new AdyenUserInfoBean()};
//        RequestInterface request = retrofit.create(RequestInterface.class);
//        Call<AdyenUserInfoBean> call = request.getUserInfoByHttp(102802);
//        call.enqueue(new Callback<AdyenUserInfoBean>() {
//            @Override
//            public void onStringResponse(Call<AdyenUserInfoBean> call, Response<AdyenUserInfoBean> response) {
//                adyenUserInfo[0] = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<AdyenUserInfoBean> call, Throwable t) {
//
//            }
//        });
//        return adyenUserInfo[0];
//    }
//
//    public static void recurringPay(PayBean bean) {
//        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
//        Call<PayResultBaen> call = requestInterface.recurringPay(bean);
//        call.enqueue(new Callback<PayResultBaen>() {
//            @Override
//            public void onStringResponse(Call<PayResultBaen> call, Response<PayResultBaen> response) {
//                LogWriteUtils.d(response.body().authCode + "--------------------");
//            }
//
//            @Override
//            public void onFailure(Call<PayResultBaen> call, Throwable t) {
//
//            }
//        });
//    }
}
