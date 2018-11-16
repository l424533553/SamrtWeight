package com.axecom.smartweight.carouselservice1.service;

import android.app.Presentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.carouselservice1.entity.ImageDao;
import com.axecom.smartweight.carouselservice1.entity.UserDao;
import com.axecom.smartweight.carouselservice1.entity.AdImageInfo;
import com.axecom.smartweight.carouselservice1.entity.AdUserBean;
import com.axecom.smartweight.carouselservice1.entity.AdUserInfo;
import com.axecom.smartweight.utils.FileUtils;
import com.bumptech.glide.Glide;
import com.luofx.listener.VolleyStringListener;
import com.luofx.utils.PreferenceUtils;
import com.luofx.utils.log.MyLog;
import com.luofx.utils.net.NetWorkJudge;
import com.shangtongyin.tools.serialport.IConstants_ST;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class SecondScreen extends Presentation implements VolleyStringListener, IConstants_ST, OnBannerListener {
    //    private static final int[] banners = {R.drawable.logo, R.drawable.login_bg, R.drawable.banner_bg};
    //    public ConvenientBanner convenientBanner;
    private Context context;
//    private int currentPos = 0;
//    public ListView orderListView;
//    public TextView bannerTotalPriceTv;
//    public ImageView bannerQRCode;
//    public LinearLayout bannerOrderLayout;
//    public List<SubOrderReqBean.Goods> goodsList;
//    public MyAdapter adapter;
//    public List<String> list;

    private Banner mBanner;
    private ImageView ivPhoto;
    private MyImageLoader mMyImageLoader;

    private List<Integer> imagePath;
    private ArrayList<String> paths;
    private ArrayList<String> imageTitle;


    private String url = "http://119.23.43.64/api/smartsz/getadinfo?companyid=";

    @Override
    public void show() {
        super.show();
    }

    public SecondScreen(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
    }

    private SysApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        paths = new ArrayList<>();
        myApplication = (SysApplication) context.getApplicationContext();

        initView();
        dir = FileUtils.getDownloadDir(context, FileUtils.DOWNLOAD_DIR);
        imageDao = new ImageDao(context);
        userDao = new UserDao(context);
        initData();
        initHandler();
        questImage();


//        info.add(text);
//        info.add(text);
//
//        // 在代码里设置自己的动画
//        marqueeView.startWithList(info, R.anim.anim_right_in, R.anim.anim_left_out);
    }

    public void questImage() {
        if (NetWorkJudge.isNetworkAvailable(context)) {

            Context sharedAppContext = null;
            try {
                sharedAppContext = context.createPackageContext("com.axecom.smartweight", 0);
                int MODE = Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;
                SharedPreferences share = sharedAppContext.getSharedPreferences("share", MODE);
                int shellerid = share.getInt("shellerid", 0);
                if (shellerid > 0) {
                    String path = url + shellerid;
                    myApplication.volleyStringGet(path, this, 1);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handler;// 数据

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                initData();
                return false;
            }
        });
    }

    private String dir;
    private ImageDao imageDao;
    private UserDao userDao;
    private List<String> info;

    private void initData() {
        imageTitle.clear();
        if (info == null) {
            info = new ArrayList<>();
        } else {
            info.clear();
        }
        List<AdImageInfo> list = imageDao.queryAll();
        List<AdImageInfo> photos = imageDao.queryPhoto();
        List<AdUserBean> userBeans = userDao.queryById2(1);
        if (photos.size() > 0) {
            String photo = photos.get(0).getLocalPath();

            Glide.with(context).load(photo).into(ivPhoto);
//            ivPhoto.setImageURI(Uri.parse(photo));
        }
        if (dir == null || list.size() == 0) {
            imagePath.clear();
            imagePath.add(R.drawable.default1);
            imagePath.add(R.drawable.default2);
            imagePath.add(R.drawable.default3);

            imageTitle.add("市场图一");
            imageTitle.add("市场图二");
            imageTitle.add("市场图三");
            mBanner.setImages(imagePath);
        } else {
            paths.clear();
            for (AdImageInfo image : list) {
                paths.add(image.getLocalPath());
                imageTitle.add(image.getTitle());
            }
            mBanner.setImages(paths);
        }
        mBanner.setBannerTitles(imageTitle);
        mBanner.setDelayTime(3000);
        mBanner.start();

        if (userBeans.size() > 0) {
            AdUserBean AdUserBean = userBeans.get(0);
            companyno.setText(AdUserBean.getCompanyno());
            introduce.setText(AdUserBean.getIntroduce());
            companyname.setText(AdUserBean.getCompanyname());
            linkphone.setText(AdUserBean.getLinkphone());
            companyid.setText(AdUserBean.getCompanyid());
            String text = AdUserBean.getAdcontent();
            info.add(text);
            info.add(text);
            marqueeView.startWithList(info, R.anim.anim_right_in, R.anim.anim_left_out);
        }

    }

    private TextView companyno, introduce, companyname, linkphone, companyid;
    private MarqueeView marqueeView;

    private void initView() {
        mMyImageLoader = new MyImageLoader();
        marqueeView = findViewById(R.id.marqueeView);
        companyno = findViewById(R.id.companyno);
        introduce = findViewById(R.id.introduce);
        companyname = findViewById(R.id.companyname);
        linkphone = findViewById(R.id.linkphone);
        companyid = findViewById(R.id.companyid);

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
        mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
//        mBanner.setImages(imagePath);
//        mBanner.setImages(imagePath);// 这是资源id集合
        mBanner.setOnBannerListener(this)//轮播图的监听
                //开始调用的方法，启动轮播图。
                .start();
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

    @Override
    public void onResponseError(VolleyError volleyError, int flag) {
        MyLog.blue("请求错误");
    }

    /**
     * 图片加载类
     */
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext()).load(path).into(imageView);
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
        }
    }


    @Override
    public void onResponse(String response, int flag) {
        AdUserInfo netImageInfo = JSON.parseObject(response, AdUserInfo.class);
        if (netImageInfo != null) {
            if (netImageInfo.getStatus() == 0) {
                AdUserBean adUserBean = netImageInfo.getData();
                if (adUserBean != null) {
                    String screenImageState = PreferenceUtils.getSp(context).getString(IMAGE_STATE, "default");
                    if (!screenImageState.equals(adUserBean.getStatus())) {
                        new ImageDownThread(adUserBean).start();
                        PreferenceUtils.getSp(context).edit().putString(IMAGE_STATE, screenImageState).apply();
                    }
                }
            }
        }
    }

    //为了下载图片资源，开辟一个新的子线程
    private class ImageDownThread extends Thread {
        private AdUserBean AdUserBean;

        public ImageDownThread(AdUserBean AdUserBean) {
            this.AdUserBean = AdUserBean;
        }

        public void run() {
            //下载图片的路径
            String iPath;
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                String baseUrl = AdUserBean.getBaseurl();
                List<AdImageInfo> imageInfos = new ArrayList<>();
                String licences = AdUserBean.getLicence();
                boolean issuccess = FileUtils.deleteDir(dir);

                if (licences != null) {
                    String[] adArray = licences.split(";");
                    if (adArray.length > 0) {
                        for (int i = 0; i < adArray.length; i++) {
                            String netUrl = baseUrl + adArray[i];
                            URL url = new URL(netUrl);
                            //再一次打开
                            inputStream = url.openStream();
                            String localPath = dir + "licence" + i + ".png";
                            File file = new File(localPath);
                            fileOutputStream = new FileOutputStream(file);
                            int hasRead = 0;
                            while ((hasRead = inputStream.read()) != -1) {
                                fileOutputStream.write(hasRead);
                            }
                            AdImageInfo AdImageInfo = new AdImageInfo();
                            AdImageInfo.setNetPath(netUrl);
                            AdImageInfo.setLocalPath(localPath);
                            AdImageInfo.setType(2);
                            imageInfos.add(AdImageInfo);
                        }
                    }
                }

                String ads = AdUserBean.getAd();
                if (ads != null) {
                    String[] adArray = ads.split(";");
                    if (adArray.length > 0) {
                        for (int i = 0; i < adArray.length; i++) {
                            String netUrl = baseUrl + adArray[i];
                            URL url = new URL(netUrl);
                            //再一次打开
                            inputStream = url.openStream();
                            String localPath = dir + "ad" + i + ".png";
                            File file = new File(localPath);
                            fileOutputStream = new FileOutputStream(file);
                            int hasRead = 0;
                            while ((hasRead = inputStream.read()) != -1) {
                                fileOutputStream.write(hasRead);
                            }
                            AdImageInfo AdImageInfo = new AdImageInfo();
                            AdImageInfo.setNetPath(netUrl);
                            AdImageInfo.setLocalPath(localPath);
                            AdImageInfo.setType(1);
                            imageInfos.add(AdImageInfo);
                        }
                    }
                }

                String photo = AdUserBean.getPhoto();
                if (photo != null) {
                    String netUrl = baseUrl + photo;
                    URL url = new URL(netUrl);
                    //再一次打开
                    inputStream = url.openStream();
                    String localPath = dir + "photo.png";
                    File file = new File(localPath);
                    fileOutputStream = new FileOutputStream(file);
                    int hasRead = 0;
                    while ((hasRead = inputStream.read()) != -1) {
                        fileOutputStream.write(hasRead);
                    }
                    AdImageInfo AdImageInfo = new AdImageInfo();
                    AdImageInfo.setNetPath(netUrl);
                    AdImageInfo.setLocalPath(localPath);
                    AdImageInfo.setType(0);
                    imageInfos.add(AdImageInfo);
                }

                imageDao.deleteAll();
                imageDao.inserts(imageInfos);
                userDao.deleteAll();
                AdUserBean.setId(1);
                userDao.insert(AdUserBean);
                handler.sendEmptyMessage(1002);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
