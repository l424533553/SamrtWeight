package com.axecom.smartweight.ui.activity;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.adapter.OrderAdapter;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.bigkoo.convenientbanner.holder.Holder;
import com.luofx.help.QRHelper;
import com.luofx.utils.PreferenceUtils;

import java.util.List;

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
    public List<OrderBean> goodsList;
    public OrderAdapter adapter;
    private OrderAdapter adapterSecond;

    public List<String> list;
    private ListView lvOne;
    private ListView lvSecond;

    public SecondPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
    }

    public void notifyData(final List<OrderInfo> data) {
        this.data = data;
        if (data == null || data.size() < 1) {
            return;
        }
        if (TextUtils.isEmpty(tvPayWay.getText().toString())) {
            OrderInfo orderInfoFirst = data.get(0);
            tt1(orderInfoFirst);
            data.remove(0);
            notifyData(data);

        } else if (TextUtils.isEmpty(tvPayWaySecond.getText().toString())) {
            OrderInfo orderInfoFirst = data.get(0);
            tt2(orderInfoFirst);
            data.remove(0);
            notifyData(data);
        }

    }

    private void tt2(OrderInfo orderInfo) {
        key = PreferenceUtils.getString(context, KEY, null);
        mchid = PreferenceUtils.getString(context, MCHID, null);
        baseUrl = "http://pay.axebao.com/payInterface_gzzh/pay?key=" + key + "&mchid=" + mchid;

        adapterSecond.setDatas(orderInfo.getOrderItem());
        tvPayMoneySecond.setText(orderInfo.getTotalamount());
        if (orderInfo.getSettlemethod() == 1) {
            tvPayWaySecond.setText("微信支付");
        } else if (orderInfo.getSettlemethod() == 2) {
            tvPayWaySecond.setText("支付宝支付");
        } else if (orderInfo.getSettlemethod() == 0) {
            tvPayWaySecond.setText("现金支付");
        }

        int showTime = 10000;
        if (orderInfo.getSettlemethod() > 0) {
            int newPriceSecond = (int) (Float.valueOf(orderInfo.getTotalamount()) * 100);
            String urlSecond = baseUrl + "&orderno=" + orderInfo.getBillcode() + "&fee=" + newPriceSecond;
            Bitmap bitmapSecond = QRHelper.createQRImage(urlSecond);
            ivQRSecond.setImageBitmap(bitmapSecond);
            showTime = 60000;
        }
        handler.sendEmptyMessageDelayed(666, showTime);



    }

    private void tt1(OrderInfo orderInfoFirst) {
        key = PreferenceUtils.getString(context, KEY, null);
        mchid = PreferenceUtils.getString(context, MCHID, null);
        baseUrl = "http://pay.axebao.com/payInterface_gzzh/pay?key=" + key + "&mchid=" + mchid;

        adapter.setDatas(orderInfoFirst.getOrderItem());
        tvPayMoney.setText(orderInfoFirst.getTotalamount());
        if (orderInfoFirst.getSettlemethod() == 1) {
            tvPayWay.setText("微信支付");
        } else if (orderInfoFirst.getSettlemethod() == 2) {
            tvPayWay.setText("支付宝支付");
        } else if (orderInfoFirst.getSettlemethod() == 0) {
            tvPayWay.setText("现金支付");
        }

        int showTime = 10000;
        if (orderInfoFirst.getSettlemethod() > 0) {
            int newPrice = (int) (Float.valueOf(orderInfoFirst.getTotalamount()) * 100);
            String urlFirst = baseUrl + "&orderno=" + orderInfoFirst.getBillcode() + "&fee=" + newPrice;
            Bitmap bitmapFirst = QRHelper.createQRImage(urlFirst);
            ivQR.setImageBitmap(bitmapFirst);
            showTime = 60000;
        }
        handler.sendEmptyMessageDelayed(555, showTime);
    }


    public void notifyData2(final List<OrderInfo> data) {
        if (data != null && data.size() > 0) {
            if (data.size() <= 2) {
                key = PreferenceUtils.getString(context, KEY, null);
                mchid = PreferenceUtils.getString(context, MCHID, null);
                baseUrl = "http://pay.axebao.com/payInterface_gzzh/pay?key=" + key + "&mchid=" + mchid;

                final OrderInfo orderInfoFirst = data.get(0);
                adapter.setDatas(orderInfoFirst.getOrderItem());
                tvPayMoney.setText(orderInfoFirst.getTotalamount());
                if (orderInfoFirst.getSettlemethod() == 1) {
                    tvPayWay.setText("微信支付");
                } else if (orderInfoFirst.getSettlemethod() == 2) {
                    tvPayWay.setText("支付宝支付");
                } else if (orderInfoFirst.getSettlemethod() == 0) {
                    tvPayWaySecond.setText("现金支付");
                }

                if (orderInfoFirst.getSettlemethod() > 0) {
                    int newPrice = (int) (Float.valueOf(orderInfoFirst.getTotalamount()) * 100);
                    String urlFirst = baseUrl + "&orderno=" + orderInfoFirst.getBillcode() + "&fee=" + newPrice;
                    Bitmap bitmapFirst = QRHelper.createQRImage(urlFirst);
                    ivQR.setImageBitmap(bitmapFirst);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (data.contains(orderInfoFirst)) {
                            data.remove(orderInfoFirst);
                            handler.sendEmptyMessage(1100);//刷新
                        }
                    }
                }, 30000);


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

                    if (orderInfoSecond.getSettlemethod() > 0) {
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
            adapter.setDatas(null);

            tvPayMoneySecond.setText("0.00");
            tvPayWaySecond.setText("");
            ivQRSecond.setImageResource(R.drawable.logo);
            adapterSecond.setDatas(null);

        }

    }

    private void clearnFirst() {
        tvPayMoney.setText("0.00");
        tvPayWay.setText("");
        ivQR.setImageResource(R.drawable.logo);
        adapter.setDatas(null);
        notifyData(data);
    }

    private void clearnSecond() {
        tvPayMoneySecond.setText("0.00");
        tvPayWaySecond.setText("");
        ivQRSecond.setImageResource(R.drawable.logo);
        adapterSecond.setDatas(null);
        notifyData(data);
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


//    private MyTimeTask task;
//
//    private void setTimer() {
//        task = new MyTimeTask(30000,,60000, new TimerTask() {
//            @Override
//            public void run() {
//                if (data.size() > 0) {
//                    data.remove(0);
//                    handler.sendEmptyMessage(1100);
//                }
//                //或者发广播，启动服务都是可以的
//            }
//        });
//
//        task.start();
//    }

    private Handler handler;

    private void inithander() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 555:
                        //清空First
                        clearnFirst();
//                        notifyData(data);
                        break;
                    case 666:
                        //清空First
                        clearnSecond();
                        break;
                    default:
//                        notifyData(data);
                        break;
                }
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
//        setTimer();

    }

    private List<OrderInfo> data;

    public void setData(List<OrderInfo> data) {
        this.data = data;
    }

    //    public void addData(OrderInfo orderInfo) {
//        data.add(orderInfo);
//        handler.sendEmptyMessage(1100);
//    }


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
//        task.stop();
        super.dismiss();
    }
}
