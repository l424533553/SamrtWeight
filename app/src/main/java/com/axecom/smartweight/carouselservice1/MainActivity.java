//package com.axecom.smartweight.carouselservice1;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.axecom.iweight.carouselservice1.config.IConstants;
//import com.axecom.iweight.carouselservice1.config.MyText;
//import com.axecom.smartweight.R;
//import com.bumptech.glide.Glide;
//import com.sunfusheng.marqueeview.MarqueeView;
//import com.youth.banner.Banner;
//import com.youth.banner.BannerConfig;
//import com.youth.banner.Transformer;
//import com.youth.banner.listener.OnBannerListener;
//import com.youth.banner.loader.ImageLoader;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///*
// * 测试功能
// */
//public class MainActivity extends AppCompatActivity implements IConstants, OnBannerListener {
//    private Banner mBanner;
//    private MyImageLoader mMyImageLoader;
//    private ArrayList<Integer> imagePath;
//    private ArrayList<String> paths;
//    private ArrayList<String> imageTitle;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(ACTION_START);
//                sendBroadcast(it);
//            }
//        });
//
//
//        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(ACTION_DESTORY);
//                sendBroadcast(it);
//            }
//        });
//
//
//        findViewById(R.id.btnImage).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                mBanner.setBannerTitles(imageTitle);
////                mBanner.setImages(imagePath);
////                mBanner.update(imagePath, imageTitle);
////                mMyImageLoader.displayImage(MainActivity.this,imagePath,image);
//
//                paths.clear();
//                paths.add("https://goss.veer.com/creative/vcg/veer/612/veer-130129793.jpg");
//                paths.add("https://goss.veer.com/creative/vcg/veer/612/veer-140799698.jpg");
//                paths.add("https://goss.veer.com/creative/vcg/veer/612/veer-130136081.jpg");
//
////                mBanner.update(imagePath);
//                mBanner.setImages(imagePath);
//                mBanner.start();
//            }
//        });
//
//        imagePath = new ArrayList<>();
//        imageTitle = new ArrayList<>();
//        initData();
////        initData2();
//        initView();
//
//        MyText textView = findViewById(R.id.tvId);
//        textView.setFocusable(true);
//
//        String StringEmpty = "        ";
//        String text = "你打完了今天冷热家庭纠纷了解到了过来发了发了发了";
//        textView.setText(text + StringEmpty);
//
//
//        MarqueeView marqueeView = (MarqueeView) findViewById(R.id.marqueeView1);
//        List<String> info = new ArrayList<>();
//        info.add(text);
//        info.add(text);
//
//        // 在代码里设置自己的动画
//        marqueeView.startWithList(info, R.anim.anim_right_in, R.anim.anim_left_out);
//
////        marqueeView.startWithText(text + StringEmpty, R.anim.anim_right_in, R.anim.anim_left_out);
//    }
//
//
//    private void initData() {
//        imageTitle.clear();
//        imagePath.add(R.drawable.default1);
//        imagePath.add(R.drawable.default2);
//        imagePath.add(R.drawable.default3);
//
//        imageTitle.add("市场图1");
//        imageTitle.add("市场图2");
//        imageTitle.add("市场图3");
//    }
////
////    private void initData2() {
////        paths = new ArrayList<>();
////        paths.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1752944080,3710769079&fm=26&gp=0.jpg");
////        paths.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1284120377,389274510&fm=26&gp=0.jpg");
////        paths.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3588991392,2684915208&fm=26&gp=0.jpg");
//////        paths.add("https://gss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D600%2C800/sign=e9873bfca944ad342eea8f81e09220cc/a8ec8a13632762d08fa73daea8ec08fa513dc602.jpg");
////
////        imageTitle.clear();
////        imageTitle.add("我爱NBA");
////        imageTitle.add("我爱科比布莱恩特");
////        imageTitle.add("我爱NBA");
//////        imageTitle.add("我爱科比布莱恩特");
////    }
//
//    private void initView() {
//        mMyImageLoader = new MyImageLoader();
//        mBanner = findViewById(R.id.banner);
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
//     * @param position
//     */
//    @Override
//    public void OnBannerClick(int position) {
//        Toast.makeText(this, "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 图片加载类
//     */
//    private class MyImageLoader extends ImageLoader {
//        @Override
//        public void displayImage(Context context, Object path, ImageView imageView) {
//            Glide.with(context.getApplicationContext())
//                    .load(path)
//                    .into(imageView);
//
//            //Glide 加载图片简单用法
//            Glide.with(context).load(path).into(imageView);
//
////            //Picasso 加载图片简单用法
////            Picasso.with(context).load(path).into(imageView);
////
//            //用fresco加载图片简单用法，记得要写下面的createImageView方法
////            Uri uri = Uri.parse((String) path);
////            imageView.setImageURI(uri);
//        }
//
//        //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
////        @Override
////        public ImageView createImageView(Context context) {
////            //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
////            SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
////            return simpleDraweeView;
////        }
//
//
//    }
//
//}
