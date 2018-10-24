package com.axecom.smartweight.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.axecom.smartweight.R;

/**
 * Created by Administrator on 2018-5-15.
 */

public class UseBankCardActivity extends Activity implements View.OnClickListener{

    private TextView cancelTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_dialog_layout);
        cancelTv = findViewById(R.id.bank_card_dialog_cancel_tv);
        cancelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bank_card_dialog_cancel_tv:
                finish();
                break;
        }
    }
}
