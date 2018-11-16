//package com.axecom.smartweight.my.service;
//
//import android.app.Presentation;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.Display;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.android.volley.VolleyError;
//import com.axecom.smartweight.R;
//import com.axecom.smartweight.base.SysApplication;
//import com.bumptech.glide.Glide;
//import com.luofx.listener.VolleyListener;
//import com.luofx.utils.log.MyLog;
//import com.shangtongyin.tools.serialport.IConstants_ST;
//import com.youth.banner.Banner;
//import com.youth.banner.BannerConfig;
//import com.youth.banner.Transformer;
//import com.youth.banner.listener.OnBannerListener;
//import com.youth.banner.loader.ImageLoader;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
///**
// * Created by Administrator on 2018/7/20.
// */
//public class SecondScreen extends Presentation implements VolleyListener, IConstants_ST, OnBannerListener {
//    //    private static final int[] banners = {R.drawable.logo, R.drawable.login_bg, R.drawable.banner_bg};
//    //    public ConvenientBanner convenientBanner;
//    private Context context;
////    private int currentPos = 0;
////    public ListView orderListView;
////    public TextView bannerTotalPriceTv;
////    public ImageView bannerQRCode;
////    public LinearLayout bannerOrderLayout;
////    public List<SubOrderReqBean.Goods> goodsList;
////    public MyAdapter adapter;
////    public List<String> list;
//
//
//    private Banner mBanner;
//    private MyImageLoader mMyImageLoader;
//    private ArrayList<Integer> imagePath;
//    private ArrayList<String> paths;
//    private ArrayList<String> imageTitle;
//
//    private String url = "http://farm.axebao.com:8082/android/image.json";
//
//    @Override
//    public void show() {
//        super.show();
//    }
//
//    public SecondScreen(Context outerContext, Display display) {
//        super(outerContext, display);
//        this.context = outerContext;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.screen_main);
//        imagePath = new ArrayList<>();
//        imageTitle = new ArrayList<>();
//        paths = new ArrayList<>();
//        initData();
//        initView();
//        SysApplication myApplication = (SysApplication) context.getApplicationContext();
//        myApplication.volleyGet(url, this, 1);
//    }
//
//    private void initData() {
//        imageTitle.clear();
//        imagePath.add(R.drawable.load);
//        imagePath.add(R.drawable.wechat_pay);
//        imagePath.add(R.drawable.alipay);
//
//        imageTitle.add("我是贵族一号");
//        imageTitle.add("我是贵族二号");
//        imageTitle.add("我是贵族三号");
//    }
//
//    private void initView() {
//        mMyImageLoader = new MyImageLoader();
//        mBanner = findViewById(R.id.banner22);
//        //设置样式，里面有很多种样式可以自己都看看效果
//        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
//        //设置图片加载器
//        mBanner.setImageLoader(mMyImageLoader);
//        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
//        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
//        //轮播图片的文字
//        mBanner.setBannerTitles(imageTitle);
//        //设置轮播间隔时间
//        mBanner.setDelayTime(3000);
//        //设置是否为自动轮播，默认是true
//        mBanner.isAutoPlay(true);
//        //设置指示器的位置，小点点，居中显示
//        mBanner.setIndicatorGravity(BannerConfig.CENTER);
//        //设置图片加载地址
//        mBanner.setImages(imagePath);
////        mBanner.setImages(imagePath);// 这是资源id集合
//        mBanner.setOnBannerListener(this)//轮播图的监听
//                //开始调用的方法，启动轮播图。
//                .start();
//    }
//
//    /**
//     * 轮播图的监听
//     *
//     * @param position 数据
//     */
//    @Override
//    public void OnBannerClick(int position) {
//        Toast.makeText(context, "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 图片加载类
//     */
//    private class MyImageLoader extends ImageLoader {
//        @Override
//        public void displayImage(Context context, Object path, ImageView imageView) {
//            Glide.with(context.getApplicationContext()).load(path).into(imageView);
//            //Glide 加载图片简单用法
//            Glide.with(context).load(path).into(imageView);
//        }
//    }
//
//    @Override
//    public void onResponse(VolleyError volleyError, int flag) {
//
//        MyLog.mylog("请求错误");
////        MyLog.logTest(volleyError.getLocalizedMessage());
//    }
//
//    @Override
//    public void onResponse(JSONObject jsonObject, int flag) {
//        NetImageInfo netImageInfo = JSON.parseObject(jsonObject.toString(), NetImageInfo.class);
//        if (netImageInfo != null) {
//            paths.clear();
//            paths.addAll(netImageInfo.getPaths());
//            imageTitle.clear();
//            imageTitle.addAll(netImageInfo.getTitles());
//            mBanner.setImages(paths);
//            mBanner.setBannerTitles(imageTitle);
//            mBanner.start();
//        }
//    }
//}
