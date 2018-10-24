package com.axecom.smartweight.ui.activity;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.luofx.help.QRHelper;
import com.luofx.utils.PreferenceUtils;

import java.security.cert.TrustAnchor;
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

    public ConvenientBanner convenientBanner;
    private Context context;

    public TextView tvPayWay, tvPayWaySecond;
    public TextView tvPayMoney, tvPayMoneySecond;
    public ImageView ivQR;
    public ImageView ivQRSecond;
    public LinearLayout bannerOrderLayout;


    public List<OrderBean> goodsList;
    public OrderAdapter adapter;

    public List<OrderBean> goodsListSecond;
    public OrderAdapter adapterSecond;


    public List<String> list;
    private LinearLayout alertView;
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
                OrderInfo orderInfoFirst = data.get(0);
                adapter.setDatas(orderInfoFirst.getOrderItem());
                tvPayMoney.setText(orderInfoFirst.getTotalamount());
                if (orderInfoFirst.getSettlemethod() == 1) {
                    tvPayWay.setText("微信支付");
                } else if (orderInfoFirst.getSettlemethod() == 2) {
                    tvPayWay.setText("支付宝支付");
                }

                int newPrice = (int) (Float.valueOf(orderInfoFirst.getTotalamount()) * 100);
                String urlFirst = baseUrl + "&orderno=" + orderInfoFirst.getBillcode() + "&fee=" + newPrice;
                Bitmap bitmapFirst = QRHelper.createQRImage(urlFirst);
                ivQR.setImageBitmap(bitmapFirst);

            if(data.size()>=2){
                OrderInfo orderInfoSecond = data.get(1);
                adapterSecond.setDatas(orderInfoSecond.getOrderItem());
                tvPayMoneySecond.setText(orderInfoSecond.getTotalamount());
                if (orderInfoSecond.getSettlemethod() == 1) {
                    tvPayWaySecond.setText("微信支付");
                } else if (orderInfoSecond.getSettlemethod() == 2) {
                    tvPayWaySecond.setText("支付宝支付");
                }

                int newPriceSecond = (int) (Float.valueOf(orderInfoSecond.getTotalamount()) * 100);
                String urlSecond = baseUrl + "&orderno=" + orderInfoSecond.getBillcode() + "&fee=" + newPriceSecond;
                Bitmap bitmapSecond = QRHelper.createQRImage(urlSecond);
                ivQRSecond.setImageBitmap(bitmapSecond);
            }else {
                tvPayMoneySecond.setText("0.00");
                tvPayWaySecond.setText("");
                ivQRSecond.setImageResource(R.drawable.logo);
                adapterSecond.notifyDataSetChanged();
            }
        }else {
            tvPayMoney.setText("0.00");
            tvPayWay.setText("");
            ivQR.setImageResource(R.drawable.logo);
            tvPayMoneySecond.setText("0.00");
            tvPayWaySecond.setText("");
            ivQRSecond.setImageResource(R.drawable.logo);
            adapterSecond.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
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
        adapter = new OrderAdapter(context,true);
        adapterSecond = new OrderAdapter(context,true);
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
        key = PreferenceUtils.getString(context, KEY, null);
        mchid = PreferenceUtils.getString(context, MCHID, null);
        baseUrl = "http://pay.axebao.com/payInterface_gzzh/pay?key=" + key + "&mchid=" + mchid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_activity);
        initView();

        initData();



    }

    public void showPayResult(String titleText, String confirmText, long times) {
        bannerOrderLayout.setVisibility(View.VISIBLE);
        Glide.with(context).load(R.drawable.logo).into(ivQR);
        titleTv.setText(titleText);
        messageBtn.setText(confirmText);
        alertView.setVisibility(View.VISIBLE);

        UIUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertView.setVisibility(View.GONE);
            }
        }, times);
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


}
