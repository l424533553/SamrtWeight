package com.axecom.smartweight.mvvm.view;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axecom.smartweight.R;
import com.axecom.smartweight.adapter.GoodsTradeAdapter;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.databinding.FragmentProductSummaryBinding;
import com.axecom.smartweight.entity.room.DynamicChangeCallback;
import com.axecom.smartweight.helper.DateTimeDialogOnlyYMD;
import com.axecom.smartweight.mvvm.model.GoodsTradeBean;
import com.axecom.smartweight.mvvm.viewmodel.ProductFragmentVM;
import com.xuanyuan.library.utils.LiveBus;
import com.xuanyuan.library.utils.MyDateUtils;

import java.util.Date;
import java.util.Objects;

import static com.axecom.smartweight.config.IConstants.LIVE_EVENT_NOTIFY_GOOD_TRADE;
import static com.axecom.smartweight.config.IConstants.LOAD_DATA_TYPE_TWO;
import static com.axecom.smartweight.config.IConstants.TEXT_TIME_FROM;
import static com.axecom.smartweight.config.IConstants.TEXT_TIME_TO;
import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;

/**
 * 商品详情 模块
 * Created by Administrator on 2019/5/19.
 */
@SuppressLint("SetTextI18n")
public class ProductFragment extends Fragment implements View.OnClickListener, IAllView {
    /***************************************************************************/
    private Context context;
    private SysApplication sysApplication;
    private ObservableArrayList<GoodsTradeBean> data;
    private GoodsTradeAdapter adapter;
    private ProductFragmentVM viewMode;
    private DateTimeDialogOnlyYMD dateTimeDialogOnlyYM2;
    private MySlideDateTimeListener mySlideDateTimeListener;
    private FragmentProductSummaryBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //观察者注册,订阅消息  ，不需要主动取消订阅
        LiveBus.observeForever(EVENT_BUS_COMMON, String.class, observer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveBus.removeObserver(EVENT_BUS_COMMON, String.class, observer);
    }

    /**
     * 观察者实例
     */
    protected Observer<String> observer = s -> {
        if (LIVE_EVENT_NOTIFY_GOOD_TRADE.equals(s)) {
            data.clear();
            if (viewMode.getList() != null) {
                data.addAll(viewMode.getList());
            }
        }
    };

    /**
     * 初始化控件
     */
    public void initView(FragmentProductSummaryBinding binding) {
        binding.btnBeforeDay.setOnClickListener(this);
        binding.btnNextDay.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvView.setLayoutManager(linearLayoutManager);
        adapter = new GoodsTradeAdapter(data);
        binding.rvView.setAdapter(adapter);
        data.addOnListChangedCallback(new DynamicChangeCallback(adapter));

        mySlideDateTimeListener = new MySlideDateTimeListener(LOAD_DATA_TYPE_TWO);
        dateTimeDialogOnlyYM2 = new DateTimeDialogOnlyYMD(this.getContext(), mySlideDateTimeListener, true);
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater != null) {
            sysApplication = (SysApplication) Objects.requireNonNull(getActivity()).getApplication();
            context = getContext();
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_summary, container, false);
            data = new ObservableArrayList<>();
            initView(binding);
            viewMode = new ProductFragmentVM(this, adapter);
            viewMode.getNewsData();
            binding.setOnClickListener(this);
            binding.setViewMode(viewMode);
            return binding.getRoot();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBeforeDay:
                viewMode.findDataBefore();
                break;
            case R.id.btnNextDay:
                viewMode.findDataNext();
                break;
            case R.id.tvDayDateStart:
                mySlideDateTimeListener.setType(TEXT_TIME_FROM);
                dateTimeDialogOnlyYM2.hideOrShow();
                break;
            case R.id.tvDayDateEnd:
                mySlideDateTimeListener.setType(TEXT_TIME_TO);
                dateTimeDialogOnlyYM2.hideOrShow();
                break;
            case R.id.btnDateSubmit:
                //查找数据
                viewMode.findDataSearch(binding.tvDayDateStart.getText().toString(), binding.tvDayDateEnd.getText().toString());
            default:
                break;
        }
    }

    @Override
    public Application getApplication() {
        return sysApplication;
    }

    class MySlideDateTimeListener implements DateTimeDialogOnlyYMD.MyOnDateSetListener {
        private int type;

        private MySlideDateTimeListener(int type) {
            this.type = type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public void onDateSet(Date date) {
            switch (type) {
                case TEXT_TIME_FROM:
                    String timeFrom = MyDateUtils.getY2D(date.getTime());
                    binding.tvDayDateStart.setText(timeFrom);
                    break;
                case TEXT_TIME_TO:
                    String timeTo = MyDateUtils.getY2D(date.getTime());
                    binding.tvDayDateEnd.setText(timeTo);
                    break;
            }
        }
    }

}
