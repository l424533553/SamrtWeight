package com.axecom.smartweight.my;

import android.os.Bundle;
import android.view.View;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BusEvent;
import com.luofx.base.BaseAppCompatActivity;
import com.xuanyuan.library.MyLog;

import org.greenrobot.eventbus.EventBus;

public class TestJavaActivity extends BaseAppCompatActivity {


//    @Override
//    public void setNeedRgEventBus(boolean needRgEventBus) {
//        isNeedRgEventBus = needRgEventBus;
//    }

    @Override
    public void setNeedRgEventBus() {
        this.isNeedRgEventBus = true;
    }

    @Override
    protected void onEventMainBack(BusEvent event) {
        MyLog.blue("收到消息" + event.getQrString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_java);

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusEvent even = new BusEvent();
                even.setQrString("儿子 ，我是你爸爸");
                EventBus.getDefault().post(even);

            }
        });


    }


}
