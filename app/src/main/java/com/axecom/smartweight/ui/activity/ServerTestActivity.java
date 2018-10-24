package com.axecom.smartweight.ui.activity;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BaseActivity;
import com.axecom.smartweight.base.BaseEntity;
import com.axecom.smartweight.net.RetrofitFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018-5-29.
 */

public class ServerTestActivity extends BaseActivity {

    private View rootView;
    private Button cancelBtn;
    private TextView titleTv;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.server_test_activity, null);
        cancelBtn = rootView.findViewById(R.id.server_test_cancel_btn);
        titleTv = rootView.findViewById(R.id.server_test_title_tv);

        testConnection();
        cancelBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.server_test_cancel_btn:
                finish();
                break;
        }
    }

    public void testConnection(){
        RetrofitFactory.getInstance().API()
                .testConnection()
                .compose(this.<BaseEntity>setThread())
                .subscribe(new Observer<BaseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                            titleTv.setText(baseEntity.getMsg());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
