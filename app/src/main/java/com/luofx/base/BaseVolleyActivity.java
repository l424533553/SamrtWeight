package com.luofx.base;//package com.luofx.base;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//import com.luofx.listener.VolleyListener;
//
//public abstract class BaseVolleyActivity extends AppCompatActivity implements VolleyListener {
//
//    protected Context context;  // 上下文
//    protected SharedPreferences preferences;  // 偏好设置
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.context = this;
//        preferences = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
//    }
//}
