package com.xuanyuan.library.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xuanyuan.library.R;
import com.xuanyuan.library.adapter.LogRVAdapter;

public class LogActivity extends Activity {

    private LogRVAdapter adapter;

    private void initView() {
        initRecyclerView();
    }

    /**
     * 初始化控件
     */
    private void initRecyclerView() {
        RecyclerView rvLog = findViewById(R.id.rvLog);
        LinearLayoutManager connectedLayoutManager = new LinearLayoutManager(context);
        connectedLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLog.setLayoutManager(connectedLayoutManager);
        // list 大小是否固定 ，则不用刷新
        rvLog.setHasFixedSize(true);
        adapter = new LogRVAdapter();
        rvLog.setAdapter(adapter);
    }

    /**
     * 初始
     */
    private void initData() {

        adapter.setData(null);
    }

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        context = this;
        initView();
        initData();
    }
}
