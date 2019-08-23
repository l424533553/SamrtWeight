package com.axecom.smartweight.activity.common.what;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.adapter.UpDataAdapter;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.databinding.FragmentUpDataBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpDataFragment extends Fragment implements View.OnClickListener {


    public UpDataFragment() {
        // Required empty public constructor
    }

    private SysApplication application;
    private FragmentUpDataBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_up_data, container, false);
        binding.setOnClickListener(this);
        binding.include2.setOnClickListener(this);

//        View view = inflater.inflate(R.layout.fragment_up_data, container, false);
        application = (SysApplication) Objects.requireNonNull(getActivity()).getApplication();
        initList(binding);
        return binding.getRoot();
    }

    /**
     * 初始化控件
     */
    private void initList(FragmentUpDataBinding binding) {
        UpDataAdapter axeAdapter = new UpDataAdapter(getContext(), application.getUpdateBeanAxeList());
        binding.evAxe.setAdapter(axeAdapter);
        binding.evAxe.setGroupIndicator(null);//这个是去掉父级的箭头
        binding.evAxe.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;  //返回true,表示不可点击
            }
        });

        // 使用情况
        UpDataAdapter fpmsAdapter = new UpDataAdapter(getContext(), application.getUpdateBeanFpmsList());
        binding.evFpms.setAdapter(fpmsAdapter);
        binding.evFpms.setGroupIndicator(null);//这个是去掉父级的箭头
        binding.evFpms.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;  //返回true,表示不可点击
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLayoutTitleBack:
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
            case R.id.btnTestback:
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
            default:
                break;
        }
    }
}