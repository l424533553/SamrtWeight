//package com.axecom.smartweight.ui.activity;
//
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.GridView;
//
//import com.axecom.smartweight.R;
//import com.axecom.smartweight.base.BaseActivity;
//import com.axecom.smartweight.my.activity.common.SettingsActivity;
//import com.axecom.smartweight.my.adapter.DigitalAdapter;
//import com.axecom.smartweight.my.adapter.GoodMenuAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Administrator on 2018-5-16.
// */
//
//public class SimpleModeActivity extends BaseActivity {
//    private static final String[] DATA_DIGITAL = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "删除", "0", "."};
//
//    private View rootView;
//
//    private GridView commoditysGridView;
//    private GridView digitalGridView;
//    private GoodMenuAdapter goodMenuAdapter;
//    private DigitalAdapter digitalAdapter;
//    private Button bankCardBtn;
//    private Button cashBtn;
//    private Button settingsBtn;
//
//    @Override
//    public View setInitView() {
//        rootView = LayoutInflater.from(this).inflate(R.layout.simple_mode_activity, null);
//        commoditysGridView = rootView.findViewById(R.id.simple_mode_commoditys_grid_view);
//        digitalGridView = rootView.findViewById(R.id.simple_mode_digital_keys_grid_view);
//        bankCardBtn = rootView.findViewById(R.id.simple_mode_bank_card_btn);
//        cashBtn = rootView.findViewById(R.id.simple_mode_cash_btn);
//        settingsBtn = rootView.findViewById(R.id.simple_mode_settings_btn);
//
//        bankCardBtn.setOnClickListener(this);
//        cashBtn.setOnClickListener(this);
//        settingsBtn.setOnClickListener(this);
//        return rootView;
//    }
//
//    @Override
//    public void initView() {
//        List<String> list2 = new ArrayList<>();
//        for (int i = 0; i < 23; i++) {
//            list2.add("白菜111111111" + i);
//        }
//        list2.add("上翻");
//        list2.add("下翻");
////        goodMenuAdapter = new GoodMenuAdapter(this, list2);
////        commoditysGridView.setAdapter(goodMenuAdapter);
//
//        List<String> digitaList = new ArrayList<>();
//
//        for (int i = 0; i < DATA_DIGITAL.length; i++) {
//            digitaList.add(DATA_DIGITAL[i]);
//        }
//        digitalAdapter = new DigitalAdapter(this, digitaList);
//        digitalGridView.setAdapter(digitalAdapter);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
////            case R.id.main_bank_card_btn:
//            case R.id.main_cash_btn:
//                showDialog(v);
//                break;
//            case R.id.simple_mode_settings_btn:
////                startDDMActivity(SettingsActivity.class, false);
//
//                Intent intent = new Intent(SimpleModeActivity.this, SettingsActivity.class);
//                startActivity(intent);
//
//                break;
//        }
//    }
//
//    public void showDialog(View v) {
//        Intent intent = new Intent();
//        switch (v.getId()) {
////            case R.id.main_bank_card_btn:
////                intent.setClass(this, UseBankCardActivity.class);
////                break;
//            case R.id.main_cash_btn:
//                intent.setClass(this, UseCashActivity.class);
//                break;
//        }
//        startActivity(intent);
//
//    }
//}
