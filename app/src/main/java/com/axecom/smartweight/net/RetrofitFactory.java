package com.axecom.smartweight.net;


/**
 * Created by Administrator on 2017-11-29.
 */

public class RetrofitFactory {
    private static RetrofitFactory mRetrofitFactory;
    private RequestInterface requestInterface;

/*    private RetrofitFactory() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(InterceptorUtil.HeaderInterceptor())
                .addInterceptor(InterceptorUtil.LogInterceptor())
                .build();

//        LocalSettingsBean.Value.ServerPort serverPort = (LocalSettingsBean.Value.ServerPort) SPUtils.readObject(SysApplication.mContext, KEY_SVERVER_PORT);
//        LocalSettingsBean.Value.ServerIp serverIp = (LocalSettingsBean.Value.ServerIp) SPUtils.readObject(SysApplication.mContext, KEY_SERVER_IP);
        String serverIp = SPUtils.getString(SysApplication.getContext(), KEY_SERVER_IP, "");
        String serverPort = SPUtils.getString(SysApplication.getContext(), KEY_SVERVER_PORT, "");
        String url = "";
        String port = "";
        if (serverIp != null) {
            url = serverIp;
            if (serverPort != null && !TextUtils.isEmpty(serverPort)) {
                port = serverPort;
                url = url + ":" + port;
            }
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(!TextUtils.isEmpty(url) ? "http://" + url + "/api/" : Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        requestInterface = retrofit.create(RequestInterface.class);
    }*/

    public static void getInstance() {
        if (mRetrofitFactory == null) {
            synchronized (RetrofitFactory.class) {
                mRetrofitFactory = new RetrofitFactory();
            }
        }
    }

    public static void reSetServiceIp() {
        mRetrofitFactory = null;
        getInstance();
    }

    public RequestInterface API() {
        return requestInterface;
    }
}
