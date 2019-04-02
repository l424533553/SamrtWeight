//package com.luofx.mvp.login;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.axecom.smartweight.R;
//import com.luofx.mvp.base.view.BaseActivity;
//
//public class LoginActivity extends BaseActivity<LoginContacts.LoginPtr> implements LoginContacts.LoginUI {
//
//    @Override
//    public LoginContacts.LoginPtr onBindPresenter() {
//        return new LoginPtr(this);
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_java);
//
//        TextView btn_login = findViewById(R.id.btnTest12);
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 向 Presenter 层发送登录请求
//                getPresenter().login("admin", "123456");
//            }
//        });
//    }
//
//
//
//
//
//    @Override
//    public void loginSuccess() {
//        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void loginFailure() {
//        Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
//    }
//}
//
