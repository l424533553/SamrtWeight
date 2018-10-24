package com.shangtongyin.tools.test;//package com.shangtongyin.tools.test;
//
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.display.DisplayManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Display;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.axecom.iweight.R;
//import com.axecom.iweight.base.SysApplication;
//import com.luofx.help.QRHelper;
//import com.shangtongyin.tools.serialport.ReadWeightClick;
//import com.shangtongyin.tools.serialport.WeightHelper;
//
//import java.io.IOException;
//
//public class MainActivity22 extends AppCompatActivity implements ReadWeightClick {
//
//    private WeightHelper weighUtils;
//
//
//    private Button btnZero;
//    private TextView tvZero;
//    private Button btnWeight;
//    private TextView tvWeight;
//
//    private SysApplication app;
//
//    private ImageView image;
//
//    private void initView() {
//        //二维码显示图片
//        image = findViewById(R.id.image);
//        QRHelper.createQRImage(image, "测试二维码图片");
//
//        //重量 显示控件
//        tvWeight = findViewById(R.id.tv_weight);
//    }
//
//    private Handler handler;
//
//    private void initWeight() {
//        weighUtils = new WeightHelper(handler);
//        weighUtils.open();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
//        //获取屏幕数量
//        Display[] presentationDisplays = displayManager.getDisplays();
//
//        app = (SysApplication) getApplication();
//        initView();
//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                return false;
//            }
//        });
//
//        initWeight();
//
//        findViewById(R.id.btn_zero).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (weighUtils != null) {
//                    weighUtils.resetBalance();
//                }
//            }
//        });
//
//        //================================我是分割线==================================
//        findViewById(R.id.btn_print).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    app.getPrint().reset();
//                    app.getPrint().isWordBlod((byte) 0);
//                    app.getPrint().PrintltString("你大爷爷爷你大爷爷爷你大爷爷爷你大爷爷爷你大爷爷爷你大爷爷爷你大爷爷爷");
//
//                } catch (IOException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
////        findViewById(R.id.btnJump).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(MainActivity22.this, TestActivity.class);
////                startActivity(intent);
////            }
////        });
//
//
//        Button btnNext = findViewById(R.id.btn_next);
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    app.getPrint().reset();
//                    app.getPrint().PrintNetLine(MainActivity22.this);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//        Button btnQR = findViewById(R.id.btnQR);
//        btnQR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                app.getPrint().printQR();
//            }
//        });
//
//        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("com.axecom.iweight.carouselservice.start");
//                sendBroadcast(intent);
//            }
//        });
//    }
//
//    @Override
//    public void readData(final String weight) {
//        tvWeight.post(new Runnable() {
//            @Override
//            public void run() {
//                tvWeight.setText(weight);
//            }
//        });
//    }
//}
