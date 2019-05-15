package com.axecom.smartweight.my.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.ui.view.SoftKey;
import com.luofx.utils.KeybordUtils;

/**
 * 登陆进入  系统设置
 */
public class SystemLoginActivity extends Activity implements View.OnClickListener, IConstants {
    private EditText pwdEt;
    private SoftKey softKey;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_member_login_activity);
        this.context = this;
        setInitView();
        initView();

    }

    public void setInitView() {
        pwdEt = findViewById(R.id.staff_member_pwd_et);
        pwdEt.requestFocus();
        KeybordUtils.closeKeybord(pwdEt, context);
        //TODO 测试
//        disableShowInput(pwdEt);
        softKey = findViewById(R.id.staff_member_softkey);
        Button doneBtn = findViewById(R.id.staff_member_done_btn);
        Button backBtn = findViewById(R.id.staff_member_back_btn);

        doneBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public void initView() {
        softKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getAdapter().getItem(position).toString();
                setEditText(pwdEt, position, text);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.staff_member_done_btn:
                String password = pwdEt.getText().toString();
                if (getString(R.string.Administrators_pwd).equals(password)) {
                    Intent intentData = new Intent();
                    setResult(RESULT_OK, intentData);
                    this.finish();
                }
                break;
            case R.id.staff_member_back_btn:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
        }
    }



    @SuppressLint("SetTextI18n")
    public void setEditText(EditText editText, int position, String text) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText() + text);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setEditText(EditText editText, int position, String text, int type) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText() + text);
        }
    }


}
