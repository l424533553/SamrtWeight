package com.axecom.smartweight.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.my.entity.HotGood;
import com.axecom.smartweight.my.entity.ResultInfo;
import com.axecom.smartweight.my.entity.dao.HotGoodsDao;
import com.luofx.listener.VolleyListener;
import com.xuanyuan.library.MyToast;

import org.json.JSONObject;

import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, VolleyListener {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        context = this;
        findViewById(R.id.btnTest).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

//    private void initGoods(int tid) {
//        String url = "http://119.23.43.64/api/smartsz/getquick?terid=" + tid;
//        sysApplication.volleyGet(url, this, 2);
//    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
//        MyToast.toastShort(context, "网络请求失败");
        switch (flag) {
            case 1:
                break;
            case 2:
                MyToast.toastShort(context, "初始化数据不完全");
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
        if (flag == 2) {
            if (resultInfo != null) {
                List<HotGood> hotGoodList = JSON.parseArray(resultInfo.getData(), HotGood.class);
                if (hotGoodList != null && hotGoodList.size() > 0) {
                    HotGoodsDao hotGoodsDao = new HotGoodsDao(context);
                    hotGoodsDao.insert(hotGoodList);
                }
            }
        }
    }

}
