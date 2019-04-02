package com.axecom.smartweight.ui.activity.datasummary;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.axecom.smartweight.R;

import java.util.ArrayList;

public class SummaryActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {


    private RadioGroup radioGroup;
    private ArrayList<Fragment> fragementList;
    private Fragment lastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        findViewById(R.id.btnBack).setOnClickListener(this);
        initView();
        setData();
        setListener();

    }


    /**
     * 实例化 控件
     */
    private void initView() {
        radioGroup = findViewById(R.id.raidoGroup);
    }

    /**
     * 设置 监听
     */
    private void setListener() {
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.setOnClickListener(this);
//        this.recreate();//重新创建当前Activity实例
    }

    /**
     * 设置 数据
     */
    private void setData() {
        fragementList = new ArrayList<>();
//        fragementList.add(new HomeFragment());
        fragementList.add(new DaySummaryFragment());
        fragementList.add(new MonthFragment());
        fragementList.add(new DetailsFragment());
        // 如果 标签名 为空 ，显示 增加更新Fragment 、否则 显示 查询fragment
        Fragment firstFragment = fragementList.get(0);

        // 显示 第一个 fragment
        int index = fragementList.indexOf(firstFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, firstFragment, index + "").commit();

        // 设置 radioButton的 选中状态
        RadioButton childAt = (RadioButton) radioGroup.getChildAt(index);
        childAt.setChecked(true);
        lastFragment = firstFragment;
    }


    /**
     * radioGroup  的 选中 改变事件
     *
     * @param group     radioGroup 按钮
     * @param checkedId 选中的radio的 id
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int position = 0;
        switch (checkedId) {
//            case R.id.homeRtn:
//                position = 0;
//                break;
            case R.id.serviceRbtn:
                position = 0;
                break;
            case R.id.bottleRbtn:
                position = 1;
                break;
            case R.id.dataRbtn:
                position = 2;
                break;

        }
        changeFragment(position);
    }

    /**
     * 替换 framelayout 显示的内容
     *
     * @param position 选中的fragment
     */
    private void changeFragment(int position) {
        // 先 隐藏 上一个 显示的 fragment
        getSupportFragmentManager().beginTransaction().hide(lastFragment).commit();
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(position + "");
        if (fragmentByTag == null) {
            fragmentByTag = fragementList.get(position);
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragmentByTag, position + "").commit();
        } else {
            getSupportFragmentManager().beginTransaction().show(fragmentByTag).commit();
        }
        lastFragment = fragmentByTag;
    }


    /**
     * 横 竖屏 状态改变时，避免 Fragment重叠
     *
     * @param newConfig 配置参数
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                this.finish();
                break;
        }
    }
}
