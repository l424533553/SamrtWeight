package com.axecom.smartweight.ui.activity.datasummary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.bean.ReportResultBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.dao.OrderInfoDao;
import com.luofx.utils.DateUtils;
import com.luofx.utils.match.MyMatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 服务 模块 Ok
 * Created by Administrator on 2018/9/19.
 */
public class DaySummaryFragment extends Fragment implements View.OnClickListener {

    private Button prevPageBtn, nextPageBtn, prevMonthBtn, nextMonthBtn, prevDayBtn, nextDayBtn;
    private Button salesDetailsPrevPageBtn, salesDetailsNextPageBtn, salesDetailsPrevDayBtn, salesDetailsNextDayBtn;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private Context context;
    private SysApplication sysApplication;
    long TIME = 86400000;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 不让 软键盘 弹出

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        sysApplication = (SysApplication) getActivity().getApplication();
        context = getContext();
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.fragment_day_summary, container, false);

            initView(view);
            initHandler();
            getData(true);
        }
        return view;
    }

    Handler handler;

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 912:
                        dataAdapter.notifyDataSetChanged();
                        tvTotalMoney.setText(MyMatch.accurate2(totalMoney)  + "元");
                        break;
                }

                return false;
            }
        });
    }


    private ListView lvDaySummary;
    private List<ReportResultBean> data;
    private long time;
    private TextView tvDate;
    private TextView tvTotalMoney;
    private String date;
    private DataAdapter dataAdapter;
    private float totalMoney;

    public void initView(View view) {
        tvDate = view.findViewById(R.id.tvDate);
        tvTotalMoney = view.findViewById(R.id.tvTotalMoney);
        time = System.currentTimeMillis();
        date = DateUtils.getYYMMDD(time);
        tvDate.setText(date);

        view.findViewById(R.id.btnBeforeDay).setOnClickListener(this);
        view.findViewById(R.id.btnNextDay).setOnClickListener(this);

        lvDaySummary = view.findViewById(R.id.lvDaySummary);
        data = new ArrayList<>();
        dataAdapter = new DataAdapter(context, data);
        lvDaySummary.setAdapter(dataAdapter);


//        getReportsList(currentDay, typeVal + "", currentPage + "", pageNum + "");
//        dataList = new ArrayList<>();
//        dataAdapter = new DataSummaryActivity.DataAdapter(this, dataList);
//        dataListView.setAdapter(dataAdapter);
//
//        orderList = new ArrayList<>();
//        salesAdapter = new DataSummaryActivity.SalesAdapter(this, orderList);
//        salesDetailsListView.setAdapter(salesAdapter);
//        dateTv.setText(getCurrentTime("yyyy-MM-dd"));
//
//        Context context = this;
//        OrderInfoDao<OrderInfo> orderInfoDao = new OrderInfoDao<>(context);
//        List<OrderInfo> orderInfos = orderInfoDao.queryAll();

    }

    private void getData(final boolean isFirst) {
        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                setData();
                OrderInfoDao orderInfoDao = new OrderInfoDao(context);
                List<OrderInfo> list = orderInfoDao.queryByDay(date);
                totalMoney = 0.00f;
                for (OrderInfo orderInfo : list) {
                    int hourIndex = orderInfo.getHour();
                    ReportResultBean resultBean = data.get(hourIndex);
                    resultBean.setAll_num(resultBean.getAll_num() + 1);
                    resultBean.setTotal_amount(resultBean.getTotal_amount() + Float.valueOf(orderInfo.getTotalamount()));
                    resultBean.setTotal_weight(resultBean.getTotal_weight() + Float.valueOf(orderInfo.getTotalweight()));
                    totalMoney += Float.valueOf(orderInfo.getTotalamount());
                }
                for (int j = data.size() - 1; j >= 0; j--) {
                    if (data.get(j).getTotal_amount() <= 0.00f) {
                        data.remove(j);
                    }
                }
                handler.sendEmptyMessage(912);
            }

            private void setData() {
              data.clear();
                    data.add(0, new ReportResultBean("0"));
                    data.add(1, new ReportResultBean("1"));
                    data.add(2, new ReportResultBean("2:00——3:00"));
                    data.add(3, new ReportResultBean("3:00——4:00"));
                    data.add(4, new ReportResultBean("4:00——5:00"));
                    data.add(5, new ReportResultBean("5:00——6:00"));
                    data.add(6, new ReportResultBean("6:00——7:00"));
                    data.add(7, new ReportResultBean("7:00——8:00"));
                    data.add(8, new ReportResultBean("8:00——9:00"));
                    data.add(9, new ReportResultBean("9:00——10:00"));
                    data.add(10, new ReportResultBean("10:00——11:00"));
                    data.add(11, new ReportResultBean("11:00——12:00"));
                    data.add(12, new ReportResultBean("12:00——13:00"));
                    data.add(13, new ReportResultBean("13:00——14:00"));
                    data.add(14, new ReportResultBean("14:00——15:00"));
                    data.add(15, new ReportResultBean("15:00——16:00"));
                    data.add(16, new ReportResultBean("16:00——17:00"));
                    data.add(17, new ReportResultBean("17:00——18:00"));
                    data.add(18, new ReportResultBean("18:00——19:00"));
                    data.add(19, new ReportResultBean("19:00——20:00"));
                    data.add(20, new ReportResultBean("20:00——21:00"));
                    data.add(21, new ReportResultBean("21:00——22:00"));
                    data.add(22, new ReportResultBean("22:00——23:00"));
                    data.add(23, new ReportResultBean("23:00——24:00"));
//                } else {
//                    for (ReportResultBean reportResultBean : data) {
//                        reportResultBean.setAll_num(0);
//                        reportResultBean.setTotal_amount(0.00f);
//                        reportResultBean.setTotal_weight(0.000f);
//
//                    }
//
//                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btnBeforeDay:
                time = time - TIME;
                date = DateUtils.getYYMMDD(time);
                tvDate.setText(date);
                tvTotalMoney.setText("0.00");
                getData(false);
//                getReportsList(currentDay, typeVal + "", --currentPage <= 1 ? (currentPage = 1) + "" : --currentPage + "", pageNum + "");
                break;
            case R.id.btnNextDay:
                time = time + TIME;
                date = DateUtils.getYYMMDD(time);
                tvDate.setText(date);
                tvTotalMoney.setText("0.00");
                getData(false);
//                getReportsList(currentDay, typeVal + "", --currentPage <= 1 ? (currentPage = 1) + "" : --currentPage + "", pageNum + "");
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public class DataAdapter extends BaseAdapter {
        private Context context;
        private List<ReportResultBean> list;

        public DataAdapter(Context context, List<ReportResultBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DataAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.data_summary_item, null);
                holder = new DataAdapter.ViewHolder();
                holder.timeTv = convertView.findViewById(R.id.data_item_time_tv);
                holder.countTv = convertView.findViewById(R.id.data_item_count_tv);
                holder.weightTv = convertView.findViewById(R.id.data_item_weight_tv);
                holder.grandTotalTv = convertView.findViewById(R.id.data_item_grand_total_tv);
                holder.incomeTv = convertView.findViewById(R.id.data_item_income_tv);
                convertView.setTag(holder);
            } else {
                holder = (DataAdapter.ViewHolder) convertView.getTag();
            }
            ReportResultBean item = list.get(position);
            holder.timeTv.setText(item.getTimes());
            holder.countTv.setText(item.getAll_num() + "");
            holder.incomeTv.setText( MyMatch.accurate2(item.getTotal_amount())  );
            holder.grandTotalTv.setText(MyMatch.accurate2(item.getTotal_amount()) );
            holder.weightTv.setText(MyMatch.accurate3(item.getTotal_weight() ));
            return convertView;
        }

        class ViewHolder {
            TextView timeTv;
            TextView countTv;
            TextView weightTv;
            TextView grandTotalTv;
            TextView incomeTv;
        }
    }


}
