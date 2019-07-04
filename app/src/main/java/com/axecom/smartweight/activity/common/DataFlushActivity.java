package com.axecom.smartweight.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.entity.secondpresent.AdImageInfo;
import com.axecom.smartweight.entity.secondpresent.AdUserBean;
import com.axecom.smartweight.entity.secondpresent.AdUserDao;
import com.axecom.smartweight.entity.secondpresent.AdUserInfo;
import com.axecom.smartweight.entity.secondpresent.ImageDao;
import com.axecom.smartweight.activity.SecondScreen;
import com.axecom.smartweight.config.IEventBus;
import com.axecom.smartweight.entity.project.AllGoods;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.axecom.smartweight.entity.project.GoodsType;
import com.axecom.smartweight.entity.project.HotGood;
import com.axecom.smartweight.entity.netresult.ResultInfo;
import com.axecom.smartweight.entity.netresult.ResultInfoSmall;
import com.axecom.smartweight.entity.dao.AllGoodsDao;
import com.axecom.smartweight.entity.dao.GoodsTypeDao;
import com.axecom.smartweight.entity.dao.HotGoodsDao;
import com.axecom.smartweight.helper.HttpHelper;
import com.xuanyuan.library.help.ActivityController;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.listener.VolleyListener;
import com.xuanyuan.library.mvp.view.MyBaseCommonActivity;
import com.xuanyuan.library.utils.LiveBus;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.axecom.smartweight.config.IConstants.HANDLER_IMAGE_FINISH;
import static com.axecom.smartweight.config.IConstants.HANDLER_SECOND_IMAGE;
import static com.axecom.smartweight.config.IConstants.HANDLER_SMALL_ROUTINE;
import static com.axecom.smartweight.config.IConstants.HANDLER_UPDATE_ALLGOOD;
import static com.axecom.smartweight.config.IConstants.HANDLER_UPDATE_FINISH;
import static com.axecom.smartweight.config.IConstants.HANDLER_UPDATE_GOOD_TYPE;
import static com.axecom.smartweight.config.IConstants.INTENT_AUTO_UPDATE;
import static com.axecom.smartweight.config.IConstants.SMALLROUTINE_URL;
import static com.axecom.smartweight.config.IConstants.SP_IS_FIRST_INIT;
import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;

public class DataFlushActivity extends MyBaseCommonActivity implements IEventBus, VolleyListener, View.OnClickListener {

    /**
     * 底部标题 、热键商品更新  ,商品类型更新  ，所有商品更新,  小程序 图片  ,强制更新热键
     */
    private TextView tvTitle, tvHotGoodUpdate, tvGoodTypeUpdate, tvAllGoodUpdate, tvSmallRoutine, tvHotGoodQZ;

    private TextView tvSecondImage;
    private HotGoodsDao hotGoodsDao;
    private GoodsTypeDao goodsTypeDao;
    private AllGoodsDao allGoodsDao;

    //是否自动更新   0:首次接入自动更新   1.更新按键进入，可以支持单项更新    2.普通更新必须选项
    private int isAutoUp;

    private void initView() {
        hotGoodsDao = new HotGoodsDao();
        goodsTypeDao = new GoodsTypeDao(context);
        allGoodsDao = new AllGoodsDao(context);
        tvTitle = findViewById(R.id.tvTitle);

        tvHotGoodUpdate = findViewById(R.id.tvHotGoodUpdate);
        tvHotGoodQZ = findViewById(R.id.tvHotGoodQZ);
        tvGoodTypeUpdate = findViewById(R.id.tvGoodTypeUpdate);
        tvAllGoodUpdate = findViewById(R.id.tvAllGoodUpdate);
        tvSmallRoutine = findViewById(R.id.tvSmallRoutine);
        tvSecondImage = findViewById(R.id.tvSecondImage);

        findViewById(R.id.btnUpData).setOnClickListener(this);
        findViewById(R.id.btnHotGoodUpdate).setOnClickListener(this);
        findViewById(R.id.btnGoodTypeUpdate).setOnClickListener(this);
        findViewById(R.id.btnAllGoodUpdate).setOnClickListener(this);
        findViewById(R.id.btnSecondImage).setOnClickListener(this);
        findViewById(R.id.btnHotGoodQZ).setOnClickListener(this);
        Button btnSmallRoutine = findViewById(R.id.btnSmallRoutine);
        btnSmallRoutine.setOnClickListener(this);
    }

    private Handler handler;

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case HANDLER_UPDATE_GOOD_TYPE:
                        if (!isGoodsType) {
                            upGoodType();
                        } else {
                            tvGoodTypeUpdate.setText("更新成功");
                            handler.sendEmptyMessage(HANDLER_UPDATE_ALLGOOD);
                        }
                        break;
                    case HANDLER_UPDATE_ALLGOOD:
                        if (!isAllGoods) {
                            upAllGoods();
                        } else {
                            tvAllGoodUpdate.setText("更新成功");
                            handler.sendEmptyMessage(HANDLER_SMALL_ROUTINE);
                        }
                        break;
                    case HANDLER_SMALL_ROUTINE:
                        if (!isSmallRoutine) {
                            upSmallRoutine();
                        } else {
                            tvSmallRoutine.setText("更新成功");
                            handler.sendEmptyMessage(HANDLER_SECOND_IMAGE);
                        }
                        break;
                    case HANDLER_SECOND_IMAGE:
                        if (!isSecondImage) {
                            upSecondImage();
                        } else {
                            tvSecondImage.setText("更新成功");
                            handler.sendEmptyMessageDelayed(HANDLER_UPDATE_FINISH, 500);
                        }
                        break;
                    case HANDLER_UPDATE_FINISH:
                        if (isAllGoods && isHotGood && isGoodsType && isSmallRoutine && isSecondImage) {
                            finishUpdate = true;
                            if (isAutoUp != 1) {
                                MyPreferenceUtils.getSp(context).edit().putBoolean(SP_IS_FIRST_INIT, false).apply();
                            }

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            DataFlushActivity.this.finish();
                        } else {
                            finishUpdate = false;
                            MyToast.showError(context, "部分信息更新失败，请继续更新");
                        }
                        break;
                    case HANDLER_IMAGE_FINISH:
                        SecondScreen screen = sysApplication.getBanner();
                        if (screen != null) {
                            screen.initData();
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!finishUpdate) {
            MyToast.showError(context, "信息未更新完成，请继续更新");
        }
        super.onBackPressed();
    }

    /**
     * 热点商品   ， 货物种类  ，所有货物  ， 小程序  ， 第二界面图片
     */
    private boolean isHotGood, isGoodsType, isAllGoods, isSmallRoutine, isSecondImage;

    private SysApplication sysApplication;

    /**
     * 确定初始化状态
     */
    private void initData() {
        isAutoUp = getIntent().getIntExtra(INTENT_AUTO_UPDATE, 0);
        if (isAutoUp == 0) {
            isHotGood = false;
            isGoodsType = false;
            isAllGoods = false;
            isSmallRoutine = false;
            isSecondImage = false;
        } else if (isAutoUp == 1) {// 商户名未变，只可能小程序图片不变
            isHotGood = false;
            isGoodsType = false;
            isAllGoods = false;
            isSmallRoutine = false;
            isSecondImage = false;
        } else if (isAutoUp == 2) {  // 只进行常规数据的更新
            // 1. 如果用户变了，则状态会变成0
            isHotGood = true;
            isGoodsType = true;
            isAllGoods = true;
            isSmallRoutine = true;
            isSecondImage = true;
        }
//        isSmallRoutine = getIntent().getBooleanExtra(INTENT_COMMON_UPDATE, false);
    }

    private AdUserDao adUserDao;
    private ImageDao imageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_flush);
        ActivityController.addActivity(this);
        sysApplication = (SysApplication) getApplication();

        initView();
        initData();
        initHandler();
        adUserDao = new AdUserDao(context);
        imageDao = new ImageDao(context);

        if (isAutoUp == 0) {
            if (!isHotGood) {
                upHotGood();
            }
        }

    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        MyToast.toastShort(context, "网络请求失败");
        switch (flag) {
            case 2:
                isHotGood = false;
                tvHotGoodUpdate.setText("更新失败");
                handler.sendEmptyMessage(HANDLER_UPDATE_GOOD_TYPE);
                break;
            case 3:
                isGoodsType = false;
                tvGoodTypeUpdate.setText("更新失败");
                handler.sendEmptyMessage(HANDLER_UPDATE_ALLGOOD);
                break;
            case 4:
                isAllGoods = false;
                tvAllGoodUpdate.setText("更新失败");
                handler.sendEmptyMessage(HANDLER_SMALL_ROUTINE);

                break;
            case 5:
                isSmallRoutine = false;
                tvSmallRoutine.setText("更新失败");
                break;
            case 6:
                isSecondImage = false;
                tvSecondImage.setText("更新失败");
                break;
            case 7:
                tvHotGoodQZ.setText("更新失败");
                break;
            default:
                break;
        }
    }

    private boolean finishUpdate;

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        try {
            ResultInfo resultInfo;
            switch (flag) {
                case 2:
                    resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                    if (resultInfo != null && resultInfo.getStatus() == 0) {
                        tvHotGoodUpdate.setText("更新成功");
                        isHotGood = true;
//                        hotGoodsDao.deleteAll();
                        long count = hotGoodsDao.count();
                        if (count <= 0) {
                            List<HotGood> hotGoodList = JSON.parseArray(resultInfo.getData(), HotGood.class);
                            if (hotGoodList != null && hotGoodList.size() > 0) {
                                hotGoodsDao.insert(hotGoodList);
                            }
                            // 通知热键菜单数据更新了
                            BaseBusEvent event = new BaseBusEvent();
                            event.setEventType(NOTIFY_HOT_GOOD_CHANGE);
                            EventBus.getDefault().post(event);
                        }
                    } else {
                        isHotGood = false;
                        tvHotGoodUpdate.setText("更新失败");
                    }
                    if (isAutoUp == 0) {
                        handler.sendEmptyMessage(HANDLER_UPDATE_GOOD_TYPE);
                    }
                    break;
                case 7:
                    resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                    if (resultInfo != null && resultInfo.getStatus() == 0) {
                        tvHotGoodQZ.setText("更新成功");
//                        isHotGood = true;
                        hotGoodsDao.deleteAll();
                        List<HotGood> hotGoodList = JSON.parseArray(resultInfo.getData(), HotGood.class);
                        if (hotGoodList != null && hotGoodList.size() > 0) {
                            hotGoodsDao.insert(hotGoodList);
                        }
                        // 通知热键菜单数据更新了
                        BaseBusEvent event = new BaseBusEvent();
                        event.setEventType(NOTIFY_HOT_GOOD_CHANGE);
                        EventBus.getDefault().post(event);
                    } else {
                        isHotGood = false;
                        tvHotGoodQZ.setText("更新失败");
                    }
                    break;
                case 3:
                    resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                    if (resultInfo != null && resultInfo.getStatus() == 0) {
                        goodsTypeDao.deleteAll();
                        List<GoodsType> goodsList = JSON.parseArray(resultInfo.getData(), GoodsType.class);
                        if (goodsList != null && goodsList.size() > 0) {
                            goodsTypeDao.insert(goodsList);
                        }
                        isGoodsType = true;
                        tvGoodTypeUpdate.setText("更新成功");
                    } else {
                        isGoodsType = false;
                        tvHotGoodUpdate.setText("更新失败");
                    }
                    if (isAutoUp == 0) {
                        handler.sendEmptyMessage(HANDLER_UPDATE_ALLGOOD);
                    }
                    break;
                case 4:
                    resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                    if (resultInfo != null && resultInfo.getStatus() == 0) {
                        allGoodsDao.deleteAll();
                        List<AllGoods> goodsList = JSON.parseArray(resultInfo.getData(), AllGoods.class);
                        if (goodsList != null && goodsList.size() > 0) {
                            allGoodsDao.insert(goodsList);
                        }
                        isAllGoods = true;
                        tvAllGoodUpdate.setText("更新成功");
                    } else {
                        isAllGoods = false;
                        tvAllGoodUpdate.setText("更新失败");
                    }
                    if (isAutoUp == 0) {
                        handler.sendEmptyMessage(HANDLER_SMALL_ROUTINE);
                    }
                    break;
                case 5:
                    ResultInfoSmall result = JSON.parseObject(jsonObject.toString(), ResultInfoSmall.class);
                    if (result != null && result.getCode() == 0) {
                        String url = result.getUrl();
                        MyPreferenceUtils.getSp(context).edit().putString(SMALLROUTINE_URL, url).apply();
//                        LiveEventBus.get().with(EVENT_BUS_COMMON, String.class).post(NOTIFY_SMALLL_ROUTINE);
                        LiveBus.post(EVENT_BUS_COMMON, String.class,NOTIFY_SMALLL_ROUTINE);
                        isSmallRoutine = true;
                        tvSmallRoutine.setText("更新成功");
                    } else {
                        isSmallRoutine = false;
                        tvSmallRoutine.setText("更新失败");
                    }
                    if (isAutoUp == 0) {
                        handler.sendEmptyMessage(HANDLER_SECOND_IMAGE);
                    }
                    break;
                case 6:
                    AdUserInfo resultAd = JSON.parseObject(jsonObject.toString(), AdUserInfo.class);
                    if (resultAd != null && resultAd.getStatus() == 0) {
                        AdUserBean adUserBean = resultAd.getData();
                        saveSecondImageUrl(adUserBean);
                        isSecondImage = true;
                        tvSecondImage.setText("更新成功");
                    } else {
                        isSecondImage = false;
                        tvSecondImage.setText("更新失败");
                    }
                    if (isAutoUp == 0) {
                        handler.sendEmptyMessageDelayed(HANDLER_UPDATE_FINISH, 1000);
                    }
                    break;
                default:
                    break;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存 副屏图片地址
     */
    private void saveSecondImageUrl(final AdUserBean adUserBean) {
        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (adUserBean != null) {
                    // 更新时间
                    adUserBean.setId(1);
                    adUserDao.updateOrInsert(adUserBean);
                    imageDao.deleteAll();

                    /* 图片修改   *******************/
                    String baseUrl = adUserBean.getBaseurl();//开头路径
                    List<AdImageInfo> imageInfos = new ArrayList<>();

                    String ads = adUserBean.getAd();
                    if (ads != null) {
                        String[] adArray = ads.split(";");
                        if (adArray.length > 0) {
                            // 限制长度最长 为8
                            int piclength;
                            if (adArray.length > 8) {
                                piclength = 8;
                            } else {
                                piclength = adArray.length;
                            }

                            for (int i = 0; i < piclength; i++) {
                                String comUrl = adArray[i].replace(" ", "");
                                if (!TextUtils.isEmpty(comUrl)) {
                                    String netUrl = baseUrl + comUrl;
                                    AdImageInfo AdImageInfo = new AdImageInfo();
                                    AdImageInfo.setNetPath(netUrl);
                                    AdImageInfo.setType(1);
                                    imageInfos.add(AdImageInfo);
                                }
                            }
                        }
                    }

                    String photo = adUserBean.getPhoto();
                    if (photo != null) {
                        String comUrl = photo.replace(" ", "");
                        if (!TextUtils.isEmpty(comUrl)) {
                            String netUrl = baseUrl + comUrl;
                            AdImageInfo AdImageInfo = new AdImageInfo();
                            AdImageInfo.setNetPath(netUrl);
                            AdImageInfo.setType(0);
                            imageInfos.add(AdImageInfo);
                        }
                    }
                    imageDao.inserts(imageInfos);
                    handler.sendEmptyMessage(HANDLER_IMAGE_FINISH);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpData:
                //自动更新 模式
                isAutoUp = 0;
                upAgain();
                break;
            case R.id.btnHotGoodQZ:
                upHotGoodQZ();
                break;
            case R.id.btnHotGoodUpdate:
                upHotGood();
                break;
            case R.id.btnGoodTypeUpdate:
                upGoodType();
                break;
            case R.id.btnAllGoodUpdate:
                upAllGoods();
                break;
            case R.id.btnSmallRoutine:
                upSmallRoutine();
                break;
            case R.id.btnSecondImage:
                upSecondImage();
                break;
            default:
                break;
        }
    }

    /**
     * 重新更新
     */
    private void upAgain() {
        if (!isHotGood) {
            HttpHelper.getmInstants(sysApplication).initHotGoodEx(DataFlushActivity.this, sysApplication.getUserInfo().getTid(), 2);
            tvHotGoodUpdate.setText("正在更新。。。");
            return;
        }

        if (!isGoodsType) {
            HttpHelper.getmInstants(sysApplication).initGoodsType(DataFlushActivity.this, 3);
            tvGoodTypeUpdate.setText("正在更新。。。");
            return;
        }

        if (!isAllGoods) {
            HttpHelper.getmInstants(sysApplication).initAllGoods(DataFlushActivity.this, 4);
            tvAllGoodUpdate.setText("正在更新。。。");
            return;
        }
        if (!isSmallRoutine) {
            HttpHelper.getmInstants(sysApplication).getSmallRoutine(DataFlushActivity.this, sysApplication.getUserInfo().getSellerid(), 5);
            tvSmallRoutine.setText("正在更新。。。");

        }
    }


    /**
     * 更新商品种类
     */
    private void upGoodType() {
        if (MyNetWorkUtils.isNetworkAvailable(context)) {
            HttpHelper.getmInstants(sysApplication).initGoodsType(DataFlushActivity.this, 3);
            tvGoodTypeUpdate.setText("正在更新。。。");
        } else {
            tvTitle.setText("无网络，请配置网络");
        }
    }

    /**
     * 更新 热键商品
     */
    private void upHotGood() {
        if (MyNetWorkUtils.isNetworkAvailable(context)) {
            HttpHelper.getmInstants(sysApplication).initHotGoodEx(DataFlushActivity.this, sysApplication.getUserInfo().getTid(), 2);
            tvHotGoodUpdate.setText("正在更新。。。");
        } else {
            tvTitle.setText("无网络，请配置网络");
        }
    }

    /**
     * 更新 热键商品
     */
    private void upHotGoodQZ() {
        if (MyNetWorkUtils.isNetworkAvailable(context)) {
            HttpHelper.getmInstants(sysApplication).initHotGoodEx(DataFlushActivity.this, sysApplication.getUserInfo().getTid(), 7);
            tvHotGoodQZ.setText("正在更新。。。");
        } else {
            tvTitle.setText("无网络，请配置网络");
        }
    }

    /**
     * 更新所有商品
     */
    private void upAllGoods() {
        if (MyNetWorkUtils.isNetworkAvailable(context)) {
            HttpHelper.getmInstants(sysApplication).initAllGoods(DataFlushActivity.this, 4);
            tvAllGoodUpdate.setText("正在更新。。。");
        } else {
            tvTitle.setText("无网络，请配置网络");
        }
    }

    /**
     * 更新小程序图片，该请求尽可能少的申请，有次数限制
     */
    private void upSmallRoutine() {
        if (MyNetWorkUtils.isNetworkAvailable(context)) {
            HttpHelper.getmInstants(sysApplication).getSmallRoutine(DataFlushActivity.this, sysApplication.getUserInfo().getSellerid(), 5);
            tvSmallRoutine.setText("正在更新。。。");
        } else {
            tvTitle.setText("无网络，请配置网络");
        }
    }

    /**
     * 更新副屏证件照及广告图
     */
    private void upSecondImage() {
        if (MyNetWorkUtils.isNetworkAvailable(context)) {
            HttpHelper.getmInstants(sysApplication).httpQuestImageEx22(DataFlushActivity.this, sysApplication.getUserInfo().getSellerid(), 6);
            tvSecondImage.setText("正在更新。。。");
        } else {
            tvTitle.setText("无网络，请配置网络");
        }
    }
}
