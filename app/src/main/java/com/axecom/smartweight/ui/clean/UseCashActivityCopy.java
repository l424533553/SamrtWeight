//package com.axecom.iweight.ui.activity;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//
//import com.axecom.iweight.R;
//import com.axecom.iweight.base.BaseActivity;
//import com.axecom.iweight.base.BusEvent;
//import com.axecom.iweight.bean.SubOrderReqBean;
//import com.axecom.iweight.manager.PayCheckManage;
//import com.axecom.iweight.utils.NetworkUtil;
//import com.axecom.iweight.utils.SPUtils;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//
///**
// * Created by Administrator on 2018-5-15.
// */
//
//public class UseCashActivityCopy extends BaseActivity implements View.OnClickListener {
//
//    private View rootView;
//
//    private Button aliPayBtn;
//    private Button wechatPayBtn;
//
//    private SubOrderReqBean orderBean;
//
//    private String payId;
//    private PayCheckManage mPayCheckManage;
//
//    @Override
//    public View setInitView() {
//        rootView = LayoutInflater.from(this).inflate(R.layout.cash_dialog_layout, null);
//
//        aliPayBtn = rootView.findViewById(R.id.cash_dialog_alipay_btn);
//        wechatPayBtn = rootView.findViewById(R.id.cash_dialog_wechat_pay_btn);
//
//        orderBean = (SubOrderReqBean) getIntent().getExtras().getSerializable("orderBean");
//
//        aliPayBtn.setOnClickListener(this);
//        wechatPayBtn.setOnClickListener(this);
//        return rootView;
//    }
//
//    @Override
//    public void initView() {
//        LinkedHashMap valueMap = (LinkedHashMap) SPUtils.readObject(this, SystemSettingsActivity.KEY_STOP_CASH);
//        if (NetworkUtil.isConnected(this)) {
//            aliPayBtn.setEnabled(true);
//            wechatPayBtn.setEnabled(true);
//        } else {
//            aliPayBtn.setEnabled(false);
//            wechatPayBtn.setEnabled(false);
//        }
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(wechatPayBtn.getVisibility()==View.VISIBLE){
//            wechatPayBtn.callOnClick();
//        }else if(wechatPayBtn.getVisibility()==View.GONE&&aliPayBtn.getVisibility()==View.VISIBLE){
//            aliPayBtn.callOnClick();
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//
//            case R.id.cash_dialog_alipay_btn:
//
//                aliPayBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_green_bg2));
//                aliPayBtn.setTextColor(this.getResources().getColor(R.color.white));
//                wechatPayBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
//                wechatPayBtn.setTextColor(this.getResources().getColor(R.color.black));
//
//                break;
//            case R.id.cash_dialog_wechat_pay_btn:
//
//                aliPayBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
//                aliPayBtn.setTextColor(this.getResources().getColor(R.color.black));
//                wechatPayBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_green_bg2));
//                wechatPayBtn.setTextColor(this.getResources().getColor(R.color.white));
//
//                payId = "1";
//                setOrderBean(payId);
//                break;
//        }
//    }
//
//    public void setOrderBean(String payId) {
//        orderBean.setPayment_id(payId);
//        if (NetworkUtil.isConnected(this)) {
//            if(mPayCheckManage!=null){
//                mPayCheckManage.setCancelCheck(true);
//                mPayCheckManage = null;
//            }
//
////            mPayCheckManage = new PayCheckManage(this, SysApplication.bannerActivity, qrCodeIv, orderBean, payId);
////            mPayCheckManage.submitOrder();
//
//
//        } else {
//            List<SubOrderReqBean> orders = (List<SubOrderReqBean>) SPUtils.readObject(this, "local_order");
//            if (orders != null) {
//                orders.add(orderBean);
//                SPUtils.saveObject(this, "local_order", orders);
//            } else {
//                List<SubOrderReqBean> localOrder = new ArrayList<>();
//                localOrder.add(orderBean);
//                SPUtils.saveObject(this, "local_order", localOrder);
//            }
//            EventBus.getDefault().post(new BusEvent(BusEvent.PRINTER_NO_BITMAP, "", payId, ""));
//            finish();
//        }
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(mPayCheckManage!=null){
//            mPayCheckManage.setCancelCheck(true);
//            mPayCheckManage = null;
//        }
//    }
//}
