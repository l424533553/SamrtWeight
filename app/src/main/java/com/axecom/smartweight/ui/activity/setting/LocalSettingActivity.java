package com.axecom.smartweight.ui.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.adapter.BackgroundAdapter;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.luofx.aar.help.CashierInputFilter;
import com.xuanyuan.library.MyPreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import static com.axecom.smartweight.my.config.IEventBus.BACKGROUND_CHANGE;

public class LocalSettingActivity extends Activity implements View.OnClickListener, IConstants {
//    CircleProgressBar color_progress_view;

    private Context context;
    private float priceLarge;
    private float priceMiddle;
    private float priceSmall;

    private void initData() {
        priceLarge = MyPreferenceUtils.getSp(context).getFloat(PRICE_LARGE, 0.3f);
        priceMiddle = MyPreferenceUtils.getSp(context).getFloat(PRICE_MIDDLE, 0.2f);
        priceSmall = MyPreferenceUtils.getSp(context).getFloat(PRICE_SMALL, 0.1f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_setting);
        context = this;
        initData();
        initView();

//        initHandler();

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


    public  void doBack(View view){
        onBackPressed();
    }


//    private Handler handler;
//
//    private void initHandler() {
//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                color_progress_view.update(stepCount, 1000);
//                return false;
//            }
//        });
//    }

    private int stepCount = 100;

    private void initView() {
        Spinner spinnerButton = findViewById(R.id.ivSelect);
//        InputFilter[] filters = {new CashierInputFilter()};
        InputFilter[] filters = {new CashierInputFilter(99)};

        EditText etLarge = findViewById(R.id.etLarge);
        EditText etMiddle = findViewById(R.id.etMiddle);
        EditText etSmall = findViewById(R.id.etSmall);

        etLarge.setFilters(filters); //设置
        etMiddle.setFilters(filters); //设置
        etSmall.setFilters(filters); //设置
//        etMiddle.setFilters(filters); //设置
//        etSmall.setFilters(filters); //设置

        etLarge.setText(String.valueOf(priceLarge));
        etMiddle.setText(String.valueOf(priceMiddle));
        etSmall.setText(String.valueOf(priceSmall));
        findViewById(R.id.btnSubmitShoppingBag).setOnClickListener(this);
        int index = MyPreferenceUtils.getSp(context).getInt("BACKGROUND_INDEX", 0);

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
                MyPreferenceUtils.getSp(context).edit().putInt("BACKGROUND_INDEX", position).apply();
//                Intent intent = new Intent();
//                intent.setAction("com.axecom.smartweight.ui.activity.setting.background.change");
//                sendBroadcast(intent);

                BaseBusEvent event = new BaseBusEvent();
                event.setEventType(BACKGROUND_CHANGE);
                EventBus.getDefault().post(event);//解除事件总线问题
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
            case R.id.btnSubmitShoppingBag:

                //TODO  确定购物袋价格

                break;
            default:
                break;
        }
    }
}
