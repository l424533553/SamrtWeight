package com.axecom.smartweight.ui.activity.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.adapter.BackgroundAdapter;
import com.luofx.view.CircleProgressBar;

public class LocalSettingActivity extends AppCompatActivity implements View.OnClickListener {

    CircleProgressBar color_progress_view;

    private boolean isLoop = true;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_setting);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        initView();
        initHandler();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (isLoop) {
//                        stepCount += 10;
//                        handler.sendEmptyMessage(1111);
//                        Thread.sleep(1500);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }


    private Handler handler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLoop = false;
    }

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                color_progress_view.update(stepCount, 1000);
                return false;
            }
        });
    }

    private int stepCount = 100;

    private void initView() {
        Spinner spinnerButton = findViewById(R.id.ivSelect);
        int index = sharedPreferences.getInt("BACKGROUND_INDEX", 0);

        /* 静态的显示下来出来的菜单选项，显示的数组元素提前已经设置好了
         * 第二个参数：已经编写好的数组
         * 第三个数据：默认的样式
         */
        final BackgroundAdapter adapter = new BackgroundAdapter(this);
        spinnerButton.setAdapter(adapter);
//        spinnerButton.setBackgroundResource(index);
        spinnerButton.setSelection(index);
        spinnerButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putInt("BACKGROUND_INDEX", position).apply();
//                Intent intent = new Intent();
//                intent.setAction("com.axecom.smartweight.ui.activity.setting.background.change");
//                sendBroadcast(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        color_progress_view = findViewById(R.id.color_progress_view);
//        color_progress_view.setMaxStepNum(500);
//        color_progress_view.update(stepCount, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSelect:
                //弹出背景选择框
                break;
            default:
                break;
        }
    }
}
