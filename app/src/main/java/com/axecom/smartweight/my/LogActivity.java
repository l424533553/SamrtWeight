package com.axecom.smartweight.my;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.adapter.LogRVAdapter;
import com.axecom.smartweight.my.entity.LogBean;

import java.util.List;

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
//        LogBean logBean=new LogBean();
//        logBean.setLocation(this.getPackageCodePath());
//        logBean.setTime(DateUtils.getYY_TO_ss(new Date()));
//        logBean.setType("Info");
//        logBean.setMessage("异常测试");
//
//        LogBean logBean2=new LogBean();
//        logBean2.setLocation(this.getPackageCodePath());
//        logBean2.setTime(DateUtils.getYY_TO_ss(new Date()));
//        logBean2.setType("error");
//        logBean2.setMessage("异常测试2");
//        List<LogBean > list=new ArrayList<>();
//        list.add(logBean2);
//        list.add(logBean);


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
