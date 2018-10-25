package com.axecom.smartweight.ui.activity;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.adapter.OrderAdapter;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.ui.uiutils.UIUtils;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.luofx.help.QRHelper;
import com.luofx.utils.PreferenceUtils;
import com.luofx.utils.thread.MyTimeTask;

import java.util.List;
import java.util.TimerTask;

import static com.shangtongyin.tools.serialport.IConstants_ST.KEY;
import static com.shangtongyin.tools.serialport.IConstants_ST.MARKET_ID;
import static com.shangtongyin.tools.serialport.IConstants_ST.MCHID;
import static com.shangtongyin.tools.serialport.IConstants_ST.SELLER;
import static com.shangtongyin.tools.serialport.IConstants_ST.SELLER_ID;
import static com.shangtongyin.tools.serialport.IConstants_ST.TID;

/**
 * Created by Administrator on 2018/7/20.
 */
public class SecondPresentation extends Presentation {

    private Context context;
    public TextView tvPayWay, tvPayWaySecond;
    public TextView tvPayMoney, tvPayMoneySecond;
    public ImageView ivQR;
    public ImageView ivQRSecond;
    public LinearLayout bannerOrderLayout;

    public List<OrderBean> goodsList;
    public OrderAdapter adapter;
    public OrderAdapter adapterSecond;


    public List<String> list;
    private Button messageBtn;
    private TextView titleTv;

    private ListView lvOne;
    private ListView lvSecond;

    public SecondPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
    }

    public void notifyData(List<OrderInfo> data) {
        if (data != null && data.size() > 0) {
            if (data.size() <= 2) {
                key = PreferenceUtils.getString(context, KEY, null);
                mchid = PreferenceUtils.getString(context, MCHID, null);
                baseUrl = "http://pay.axebao.com/payInterface_gzzh/pay?key=" + key + "&mchid=" + mchid;

                OrderInfo orderInfoFirst = data.get(0);
                adapter.setDatas(orderInfoFirst.getOrderItem());
                tvPayMoney.setText(orderInfoFirst.getTotalamount());
                if (orderInfoFirst.getSettlemethod() == 1) {
                    tvPayWay.setText("微信支付");
                } else if (orderInfoFirst.getSettlemethod() == 2) {
                    tvPayWay.setText("支付宝支付");
                }else if (orderInfoFirst.getSettlemethod() == 0) {
                    tvPayWaySecond.setText("现金支付");
                }

                if (orderInfoFirst.getSettlemethod() > 0) {
                    int newPrice = (int) (Float.valueOf(orderInfoFirst.getTotalamount()) * 100);
                    String urlFirst = baseUrl + "&orderno=" + orderInfoFirst.getBillcode() + "&fee=" + newPrice;
                    Bitmap bitmapFirst = QRHelper.createQRImage(urlFirst);
                    ivQR.setImageBitmap(bitmapFirst);

                }

                if (data.size() == 1) {
                    tvPayMoneySecond.setText("0.00");
                    tvPayWaySecond.setText("");
                    ivQRSecond.setImageResource(R.drawable.logo);
                    adapterSecond.setDatas(null);
                } else {
                    OrderInfo orderInfoSecond = data.get(1);
                    adapterSecond.setDatas(orderInfoSecond.getOrderItem());
                    tvPayMoneySecond.setText(orderInfoSecond.getTotalamount());
                    if (orderInfoSecond.getSettlemethod() == 1) {
                        tvPayWaySecond.setText("微信支付");
                    } else if (orderInfoSecond.getSettlemethod() == 2) {
                        tvPayWaySecond.setText("支付宝支付");
                    } else if (orderInfoSecond.getSettlemethod() == 0) {
                        tvPayWaySecond.setText("现金支付");
                    }

                    if (orderInfoFirst.getSettlemethod() > 0) {
                        int newPriceSecond = (int) (Float.valueOf(orderInfoSecond.getTotalamount()) * 100);
                        String urlSecond = baseUrl + "&orderno=" + orderInfoSecond.getBillcode() + "&fee=" + newPriceSecond;
                        Bitmap bitmapSecond = QRHelper.createQRImage(urlSecond);
                        ivQRSecond.setImageBitmap(bitmapSecond);
                    }
                }
            } else {
                //无反应
            }
        } else {// 无数据
            tvPayMoney.setText("0.00");
            tvPayWay.setText("");
            ivQR.setImageResource(R.drawable.logo);
            tvPayMoneySecond.setText("0.00");
            tvPayWaySecond.setText("");
            ivQRSecond.setImageResource(R.drawable.logo);
            adapterSecond.setDatas(null);
            adapter.setDatas(null);
        }

    }

    private void initView() {
        ivQR = findViewById(R.id.ivQROne);
        ivQRSecond = findViewById(R.id.ivQRSecond);
        tvPayWay = findViewById(R.id.tvPayWay);
        tvPayWaySecond = findViewById(R.id.tvPayWaySecond);
        tvPayMoney = findViewById(R.id.tvPayMoney);
        tvPayMoneySecond = findViewById(R.id.tvPayMoneySecond);

        lvOne = findViewById(R.id.lvOne);
        lvSecond = findViewById(R.id.lvSecond);
        adapter = new OrderAdapter(context, true);
        adapterSecond = new OrderAdapter(context, true);
        lvOne.setAdapter(adapter);
        lvSecond.setAdapter(adapterSecond);

    }

    private int tid = -1;  //秤的编号
    private int marketId = -1;  // 市场id
    private String seller;  //售卖人
    private String key;  //售卖人
    private String mchid;  //售卖人
    private int sellerid;  // 售卖人id
    private String baseUrl;

    /**
     * 初始化心跳
     */

    private void initData() {
        tid = PreferenceUtils.getInt(context, TID, -1);
        marketId = PreferenceUtils.getInt(context, MARKET_ID, -1);
        sellerid = PreferenceUtils.getInt(context, SELLER_ID, -1);
        seller = PreferenceUtils.getString(context, SELLER, null);
    }


    private MyTimeTask task;

    private void setTimer() {
        task = new MyTimeTask(60000, new TimerTask() {
            @Override
            public void run() {
                if (data.size() > 0) {
                    data.remove(0);
                    handler.sendEmptyMessage(1100);
                }
                //或者发广播，启动服务都是可以的
            }
        });
        task.start();
    }

    private Handler handler;

    private void inithander() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                notifyData(data);
                return false;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_activity);
        initView();

//        data = new ArrayList<>();
        initData();
        initData();
        inithander();
        setTimer();

    }

    private List<OrderInfo> data;

    public void setData(List<OrderInfo> data) {
        this.data = data;
    }

    //    public void addData(OrderInfo orderInfo) {
//        data.add(orderInfo);
//        handler.sendEmptyMessage(1100);
//    }


    public void showPayResult(String titleText, String confirmText, long times) {
        bannerOrderLayout.setVisibility(View.VISIBLE);
        Glide.with(context).load(R.drawable.logo).into(ivQR);
        titleTv.setText(titleText);
        messageBtn.setText(confirmText);


//        UIUtils.getMainThreadHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                alertView.setVisibility(View.GONE);
//            }
//        }, times);


    }

    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            imageView.setImageResource(data);
        }
    }

    @Override
    public void dismiss() {
        task.stop();
        super.dismiss();
    }
}
