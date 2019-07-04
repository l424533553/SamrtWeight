package com.axecom.smartweight.activity.common;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.adapter.OrderAdapter;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.xuanyuan.library.help.qr.QRHelper2;
import com.xuanyuan.library.utils.text.StringUtils;
import com.xuanyuan.library.MyToast;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public class SecondPresentation extends Presentation {
    private final Context context;
    /**
     * 交易笔数
     */
    private TextView tvPayWay, tvPayMoney, tvOrderCount;
    private ImageView ivQR;
    public OrderAdapter adapter;
    public List<String> list;
    private OrderInfo data;
    private final int SHOW_TIME = 20000;
    private final SysApplication sysApplication;

    public  SecondPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
        sysApplication = (SysApplication) context.getApplicationContext();
    }

 public    void notifyData(final OrderInfo data) {
        this.data = data;
        if (data == null || data.getOrderItem().size() == 0) {
            return;
        }
        tt1(data);
    }

    @SuppressLint("SetTextI18n")
    private void tt1(OrderInfo orderInfoFirst) {
        indexCount++;
        show();
        this.data = orderInfoFirst;
        adapter.setDatas(orderInfoFirst.getOrderItem());
        tvPayMoney.setText(orderInfoFirst.getTotalamount());
        if (orderInfoFirst.getSettlemethod() == 1) {
            tvPayWay.setText("扫码支付");
        } else if (orderInfoFirst.getSettlemethod() == 2) {
            tvPayWay.setText("扫码支付");
        } else if (orderInfoFirst.getSettlemethod() == 0) {
            tvPayWay.setText("现金支付");
        }

        tvOrderCount.setText(adapter.getCount() + "笔");

        if (orderInfoFirst.getSettlemethod() > 0) {
            //售卖人
            String key = sysApplication.getUserInfo().getKey();
//                    .getString(context, KEY, null);
            //售卖人
            String mchid = sysApplication.getUserInfo().getMchid();
            //未配置 则不能显示支付二维码
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(mchid)) {
                String baseUrl = "http://pay.axebao.com/payInterface_gzzh/pay?key=" + key + "&mchid=" + mchid;
                int newPrice = (int) (Float.valueOf(orderInfoFirst.getTotalamount()) * 100);
                String urlFirst = baseUrl + "&orderno=" + orderInfoFirst.getBillcode() + "&fee=" + newPrice;
                Bitmap bitmapFirst = QRHelper2.createQRImage(urlFirst);
                ivQR.setImageBitmap(bitmapFirst);
            } else {
                MyToast.showError(context, "支付参数未配置！");
            }
        } else {
            ivQR.setImageResource(R.mipmap.logo);
        }

        handler.sendEmptyMessageDelayed(1001, SHOW_TIME);
    }

    private void clearnFirst() {
        tvPayMoney.setText(R.string.default_price);
        tvPayWay.setText("");
        ivQR.setImageResource(R.mipmap.logo);
        adapter.setDatas(null);
        notifyData(data);
    }


    private ListView lvOne;

    private void initView() {
        if (ivQR == null) {
            ivQR = findViewById(R.id.ivQROne);
        }
        if (tvPayMoney == null) {
            tvPayMoney = findViewById(R.id.tvPayMoney);
        }
        if (tvPayWay == null) {
            tvPayWay = findViewById(R.id.tvPayWay);
        }

        if (tvOrderCount == null) {
            tvOrderCount = findViewById(R.id.tvOrderCount);
        }
        if (adapter == null) {
            adapter = new OrderAdapter(context, true);
        }
        if (lvOne == null) {
            lvOne = findViewById(R.id.lvOne);
            lvOne.setAdapter(adapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }


    @Override
    public void hide() {
        tvOrderCount.setText("");
        tvPayMoney.setText("");
        tvPayWay.setText("");
        super.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_activity);
        initView();
        initHandler();
    }

    private Handler handler;

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                indexCount--;
                if (indexCount == 0) {
                    hide();
                }
                return false;
            }
        });
    }

    /**
     * 设置订单类
     */
    @SuppressLint("SetTextI18n")
   public void setOrderBean(List<OrderBean> data, String money) {
        indexCount++;
        show();
        adapter.setDatas(data);
        tvOrderCount.setText(adapter.getCount() + "笔");
        tvPayMoney.setText(money);
        handler.sendEmptyMessageDelayed(1001, SHOW_TIME);
    }

    private int indexCount;

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
