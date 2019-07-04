//package com.axecom.smartweight.ui.activity;
//
//import android.annotation.SuppressLint;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.CheckedTextView;
//import android.widget.EditText;
//
//import com.axecom.smartweight.R;
//import com.axecom.smartweight.base.BaseActivity;
//
//public class ModityCommodityActivity extends BaseActivity {
//    private CheckedTextView isDefaultCtv;
//
//    @SuppressLint("InflateParams")
//    @Override
//    public View setInitView() {
//        View rootView = LayoutInflater.from(this).inflate(R.layout.modity_commodity_price_layout, null);
//        EditText priceEt = rootView.findViewById(R.id.modity_commodity_price_tv);
//        isDefaultCtv = rootView.findViewById(R.id.modity_commodity_is_default_tv);
//        rootView.findViewById(R.id.modity_commodity_confirm_btn).setOnClickListener(this);
//        rootView.findViewById(R.id.modity_commodity_cancel_btn).setOnClickListener(this);
//        priceEt.requestFocus();
//        disableShowInput(priceEt);
//        return rootView;
//    }
//
//    @Override
//    public void initAdapterView() {
//        isDefaultCtv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isDefaultCtv.setChecked(!isDefaultCtv.isChecked());
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.modity_commodity_cancel_btn:
//                finish();
//                break;
//        }
//    }
//}
