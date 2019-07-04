package com.axecom.smartweight.activity;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.axecom.smartweight.R;
import com.axecom.smartweight.entity.secondpresent.AdImageInfo;
import com.axecom.smartweight.entity.secondpresent.AdUserBean;
import com.axecom.smartweight.entity.secondpresent.AdUserDao;
import com.axecom.smartweight.entity.secondpresent.ImageDao;
import com.axecom.smartweight.config.IConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xuanyuan.library.utils.FileUtils;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class SecondScreen extends Presentation implements IConstants, OnBannerListener {

    /**
     * 上下文内容
     */
    private final Context context;
    private Banner mBanner;
    private ImageView ivPhoto;

    private List<Integer> imagePath;
    private ArrayList<String> paths;
    private ArrayList<String> imageTitle;

    @Override
    public void show() {
        super.show();
    }

    public SecondScreen(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        paths = new ArrayList<>();

        initView();
        dir = FileUtils.getDownloadDir(context, FileUtils.DOWNLOAD_DIR);
        imageDao = new ImageDao(context);
        adUserDao = new AdUserDao(context);
        initData();
    }


    /**
     * 路径
     */
    private String dir;
    private ImageDao imageDao;
    private AdUserDao adUserDao;
    private List<String> info;

    @SuppressLint("CheckResult")
    public void initData() {
        imageTitle.clear();
        if (info == null) {
            info = new ArrayList<>();
        } else {
            info.clear();
        }
        List<AdImageInfo> list = imageDao.queryAll();
        List<AdImageInfo> photos = imageDao.queryPhoto();
        List<AdUserBean> userBeans = adUserDao.queryAll();

        if (photos != null && photos.size() > 0) {
            String photo = photos.get(0).getNetPath();
            RequestOptions requestOptions = new RequestOptions();
            //忽略 警告 ，不使用内存 ,这样就失去了意义了
            requestOptions.skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE); // 不使用磁盘缓存

            Glide.with(context).load(photo).error(R.mipmap.head).into(ivPhoto);
//            Glide.with(context).load(photo).apply(requestOptions).into(ivPhoto);
        } else {
            ivPhoto.setImageResource(R.mipmap.head);
        }

        if (dir == null || list == null || list.size() == 0) {
            imagePath.clear();
            imagePath.add(R.mipmap.img1);
            imagePath.add(R.mipmap.img2);
            imagePath.add(R.mipmap.img3);

            imageTitle.add("市场图一");
            imageTitle.add("市场图二");
            imageTitle.add("市场图三");
            mBanner.setImages(imagePath);
        } else {
            paths.clear();
            for (AdImageInfo image : list) {
                paths.add(image.getNetPath());
                imageTitle.add(image.getTitle());
            }
            mBanner.setImages(paths);
        }
        mBanner.setBannerTitles(imageTitle);
        mBanner.setDelayTime(5000);
        mBanner.start();

        if (userBeans != null && userBeans.size() > 0) {
            AdUserBean AdUserBean = userBeans.get(0);
            companyno.setText(AdUserBean.getCompanyno());
            introduce.setText(AdUserBean.getIntroduce());
            companyname.setText(AdUserBean.getCompanyname());
            linkphone.setText(AdUserBean.getLinkphone());
            String text = AdUserBean.getAdcontent();
            info.add(text);
            info.add(text);
            marqueeView.startWithList(info, R.anim.anim_right_in, R.anim.anim_left_out);
        }
    }

    private TextView companyno, introduce, companyname, linkphone;
    private MarqueeView marqueeView;

    private void initView() {
        MyImageLoader mMyImageLoader = new MyImageLoader();
        marqueeView = findViewById(R.id.marqueeView);
        companyno = findViewById(R.id.companyno);
        introduce = findViewById(R.id.introduce);
        companyname = findViewById(R.id.companyname);
        linkphone = findViewById(R.id.linkphone);
//        companyid = findViewById(R.id.companyid);

        ivPhoto = findViewById(R.id.ivPhoto);
        mBanner = findViewById(R.id.banner22);

        //设置样式，里面有很多种样式可以自己都看看效果
        mBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(mMyImageLoader);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        mBanner.setBannerAnimation(Transformer.Default);
        //轮播图片的文字
//        mBanner.setBannerTitles(imageTitle);
        //设置轮播间隔时间
        mBanner.setDelayTime(9000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
//        mBanner.setImages(imagePath);
//        mBanner.setImages(imagePath);// 这是资源id集合

        //开始调用的方法，启动轮播图。//轮播图的监听
        mBanner.setOnBannerListener(this).start();
    }

    /**
     * 轮播图的监听
     *
     * @param position 数据
     */
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(context, "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
    }

    /**
     * 图片加载类
     */
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext()).load(path).into(imageView);
//            //Glide 加载图片简单用法
//            Glide.with(context).load(path).into(imageView);


        }
    }

}









