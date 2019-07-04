package com.axecom.smartweight.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.axecom.smartweight.R;
import com.axecom.smartweight.activity.common.SettingsActivity;
import com.axecom.smartweight.activity.common.SystemSettingsActivity;
import com.axecom.smartweight.config.IConstants;

import com.axecom.smartweight.databinding.ActivitySystemLoginBinding;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.utils.KeybordUtils;

/**
 * 登陆进入  系统设置
 */
public class SystemLoginActivity extends Activity implements View.OnClickListener, IConstants {
    private Context context;
    private ActivitySystemLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_system_login);
        this.context = this;
        binding.setOnClickListener(this);
        initView();
    }

    public void initView() {
        binding.etPwd.requestFocus();
        KeybordUtils.closeKeybord(binding.etPwd, context);

        binding.softkey.setOnItemClickListener((parent, view, position, id) -> {
            String text = parent.getAdapter().getItem(position).toString();
            setEditText(binding.etPwd, position, text);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.staff_member_done_btn:
                String password = binding.etPwd.getText().toString();
                if (getString(R.string.Administrators_pwd).equals(password)) {
                    Intent intent=new Intent(this, SettingsActivity.class);
                    intent.putExtra(STRING_TYPE,1);
                    startActivity(intent);
                    this.finish();
                } else {
                    MyToast.showError(context, "密码输入错误");
                }
                break;
            case R.id.staff_member_back_btn:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
        }
    }

    public void setEditText(EditText editText, int position, String text) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            String content = editText.getText().toString() + text;
            editText.setText(content);
        }
    }
}
