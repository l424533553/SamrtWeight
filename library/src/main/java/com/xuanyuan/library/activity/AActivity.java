package com.xuanyuan.library.activity;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.xuanyuan.library.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 测试开发
 */
public class AActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        Log.i("5555555","AA  onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("5555555","AA  onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("5555555","AA  onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("5555555","AA  onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("5555555","AA  onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("5555555","AA  onStop");
    }

    /**
     * 使用情况，
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("5555555","AA  onDestroy");
    }
}
