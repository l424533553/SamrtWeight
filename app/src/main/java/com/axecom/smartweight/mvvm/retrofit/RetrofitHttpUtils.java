package com.axecom.smartweight.mvvm.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者： 周旭 on 2017年10月18日 0018.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class RetrofitHttpUtils {

    private static Retrofit retrofit;
    private String baseUrl;
    private Context context;

    public void init(String baseUrl, Context context) {
        if (TextUtils.isEmpty(baseUrl)) {
            return;
        }
        if (context == null) {
            return;
        }
        this.baseUrl = baseUrl;
        this.context = context.getApplicationContext();
        getRetrofit();
    }

    public synchronized Retrofit getRetrofit() {
        //初始化retrofit的配置
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getOkHttpClientBuilder())//设置客户端 设置
//                   开启后，会自动把请求返回的结果（json字符串）自动转化成与其结构相符的实体。
//                    .addConverterFactory(GsonConverterFactory.create())//设置自动转gson 格式，具体区别待弄清
                    .addConverterFactory(GsonConverterFactory.create(gson))//设置自动转gson 格式
                    //配置回调库，采用RxJava
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private OkHttpClient getOkHttpClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置log拦截器 ，这样设置后，可以在log中看到网络请求的相关信息
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //缓存文件 及文件名
        File cacheFile = new File(context.getExternalCacheDir(), "HttpCache");//缓存地址
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //大小50Mb

        // 设置缓存方式、时长、地址
        Interceptor cacheIntercepter = getCacheIntercepter();

        //连接 超时的时间，单位：秒
        int DEFAULT_TIMEOUT = 8;
        OkHttpClient client = builder.
                connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).//设置连接超时时间
                readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).//设置读取超时时间
                writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS). //设置写入超时时间
                addInterceptor(logging)//添加log拦截器
                // 设置头部过滤 ,一般在代码中单独设置
                //  .addInterceptor(getHeaderInterceptor())

                //设置https访问(验证证书，请把服务器给的证书文件放在R.raw文件夹下)
                //.sslSocketFactory(getSSLSocketFactory(context, new int[]{R.raw.tomcat}))
                //.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)

//                .socketFactory(getSSLSocketFactory())
                // 可以锁定多个证书
                //.certificatePinner(getCertificatePinner())
                // 网络拦截器
                .addNetworkInterceptor(cacheIntercepter)
                // 网络缓存拦截器
                .addInterceptor(cacheIntercepter)
                .cache(cache)
                .build();
        return client;
    }


    /**
     * 获取证书设置
     */
    private CertificatePinner getCertificatePinner() {
        return new CertificatePinner.Builder().add("sbbic.com", "sha1/C8xoaOSEzPC6BgGmxAt/EAcsajw=")
                .add("closedevice.com", "sha1/T5x9IXmcrQ7YuQxXnxoCmeeQ84c=")
                .build();
    }

    /**
     * 验证 证书的 工厂，可以使用
     */
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                //证书中的公钥
                final String PUB_KEY = "3082010a0282010100d52ff5dd432b3a05113ec1a7065fa5a80308810e4e181cf14f7598c8d553cccb7d5111fdcdb55f6ee84fc92cd594adc1245a9c4cd41cbe407a919c5b4d4a37a012f8834df8cfe947c490464602fc05c18960374198336ba1c2e56d2e984bdfb8683610520e417a1a9a5053a10457355cf45878612f04bb134e3d670cf96c6e598fd0c693308fe3d084a0a91692bbd9722f05852f507d910b782db4ab13a92a7df814ee4304dccdad1b766bb671b6f8de578b7f27e76a2000d8d9e6b429d4fef8ffaa4e8037e167a2ce48752f1435f08923ed7e2dafef52ff30fef9ab66fdb556a82b257443ba30a93fda7a0af20418aa0b45403a2f829ea6e4b8ddbb9987f1bf0203010001";

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                //客户端并为对ssl证书的有效性进行校验
                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                    if (chain == null) {
                        throw new IllegalArgumentException("checkServerTrusted:x509Certificate array isnull");
                    }

                    if (!(chain.length > 0)) {
                        throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                    }

                    if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
                        throw new CertificateException("checkServerTrusted: AuthType is not RSA");
                    }

                    // Perform customary SSL/TLS checks
                    try {
                        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                        tmf.init((KeyStore) null);
                        for (TrustManager trustManager : tmf.getTrustManagers()) {
                            ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                        }
                    } catch (Exception e) {
                        throw new CertificateException(e);
                    }
                    // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
                    // with 0×30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0×00 to drop.
                    RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();

                    String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);
                    // Pin it!
                    final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);

                    if (!expected) {
                        throw new CertificateException("checkServerTrusted: Expected public key: "
                                + PUB_KEY + ", got public key:" + encoded);
                    }
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            return sslContext.getSocketFactory();
        } catch (
                NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    //设置https证书 ，这种验证 证书的方式 可能不太准确
    protected SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }

        //CertificateFactory用来证书生成
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书
                InputStream is = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory
                        .generateCertificate(is));

                is.close();
            }

            //Create a TrustManager that trusts the CAs in our keyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获得 缓存拦截器
     */
    private Interceptor getCacheIntercepter() {
        return chain -> {
            //对request的设置用来指定有网/无网下所走的方式
            //对response的设置用来指定有网/无网下的缓存时长

            Request request = chain.request();
            if (!MyNetWorkUtils.isAvailable(context)) {
                //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
                //有网络时则根据缓存时长来决定是否发出请求
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE).build();
            }

            Response response = chain.proceed(request);
            if (MyNetWorkUtils.isAvailable(context)) {
                //有网络情况下，超过1分钟，则重新请求，否则直接使用缓存数据
                int maxAge = 30; //缓存一分钟，现改成了30秒
                String cacheControl = "public,max-age=" + maxAge;
                //当然如果你想在有网络的情况下都直接走网络，那么只需要
                //将其超时时间maxAge设为0即可
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            } else {
                //无网络时直接取缓存数据，该缓存数据保存1周
                int maxStale = 60 * 60 * 24 * 7;  //1周
                return response.newBuilder()
                        .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                        .removeHeader("Pragma").build();
            }
        };
    }

    /**
     * 获取头配置拦截器
     */
    private Interceptor getHeaderInterceptor() {
        return chain -> {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder();
            //设置具体的header内容
            builder.header("timestamp", System.currentTimeMillis() + "");
            Request.Builder requestBuilder =
                    builder.method(originalRequest.method(), originalRequest.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    public static <T> T getRetrofitAPI(Class<T> service) {
        if (retrofit == null) {
            try {
                throw new NullPointerException();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return retrofit.create(service);
    }

}
