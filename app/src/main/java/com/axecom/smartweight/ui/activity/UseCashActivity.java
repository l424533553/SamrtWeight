package com.axecom.smartweight.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BusEvent;
import com.axecom.smartweight.bean.SubOrderReqBean;
import com.axecom.smartweight.manager.PayCheckManage;
import com.axecom.smartweight.utils.NetworkUtil;
import com.axecom.smartweight.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-5-15.
 */

public class UseCashActivity extends Activity implements View.OnClickListener {

    private View rootView;

    private Button aliPayBtn;
    private Button wechatPayBtn;

    private SubOrderReqBean orderBean;

    private String payId;
    private PayCheckManage mPayCheckManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitView();
    }



    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.cash_dialog_layout, null);
        aliPayBtn = rootView.findViewById(R.id.cash_dialog_alipay_btn);
        wechatPayBtn = rootView.findViewById(R.id.cash_dialog_wechat_pay_btn);

        aliPayBtn.setOnClickListener(this);
        wechatPayBtn.setOnClickListener(this);
        return rootView;
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(wechatPayBtn.getVisibility()==View.VISIBLE){
            wechatPayBtn.callOnClick();
        }else if(wechatPayBtn.getVisibility()==View.GONE&&aliPayBtn.getVisibility()==View.VISIBLE){
            aliPayBtn.callOnClick();
        }
    }


    private  void  returnActivity(int type){
        Intent intent = new Intent();
        // 获取用户计算后的结果

        intent.putExtra("type", type); //将计算的值回传回去
        //通过intent对象返回结果，必须要调用一个setResult方法，
        //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
        setResult(RESULT_OK, intent);
        finish(); //结束当前的activity的生命周期
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cash_dialog_alipay_btn:

                aliPayBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_green_bg2));
                aliPayBtn.setTextColor(this.getResources().getColor(R.color.white));
                wechatPayBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
                wechatPayBtn.setTextColor(this.getResources().getColor(R.color.black));




                break;
            case R.id.cash_dialog_wechat_pay_btn:

                aliPayBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
                aliPayBtn.setTextColor(this.getResources().getColor(R.color.black));
                wechatPayBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_green_bg2));
                wechatPayBtn.setTextColor(this.getResources().getColor(R.color.white));


                payId = "1";
                setOrderBean(payId);
                break;
        }
    }

    public void setOrderBean(String payId) {
        orderBean.setPayment_id(payId);
        if (NetworkUtil.isConnected(this)) {
            if(mPayCheckManage!=null){
                mPayCheckManage.setCancelCheck(true);
                mPayCheckManage = null;
            }

//            mPayCheckManage = new PayCheckManage(this, SysApplication.bannerActivity, qrCodeIv, orderBean, payId);
//            mPayCheckManage.submitOrder();


        } else {
            List<SubOrderReqBean> orders = (List<SubOrderReqBean>) SPUtils.readObject(this, "local_order");
            if (orders != null) {
                orders.add(orderBean);
                SPUtils.saveObject(this, "local_order", orders);
            } else {
                List<SubOrderReqBean> localOrder = new ArrayList<>();
                localOrder.add(orderBean);
                SPUtils.saveObject(this, "local_order", localOrder);
            }
            EventBus.getDefault().post(new BusEvent(BusEvent.PRINTER_NO_BITMAP, "", payId, ""));
            finish();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mPayCheckManage!=null){
            mPayCheckManage.setCancelCheck(true);
            mPayCheckManage = null;
        }
    }
}
