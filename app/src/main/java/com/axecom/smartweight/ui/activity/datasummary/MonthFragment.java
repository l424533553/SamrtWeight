package com.axecom.smartweight.ui.activity.datasummary;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 服务 模块
 * Created by Administrator on 2016/9/19.
 */
public class MonthFragment extends Fragment implements View.OnClickListener {

    /***************************************************************************/


    private Context context;
    private SysApplication sysApplication;
    private ListView lvDaySummary;
    private List<ReportResultBean> data;
    private Date time;
    private TextView tvMonth;
    private TextView tvTotalMoney;
    private String date;
    private MonthAdapter dataAdapter;
    private float totalMoney;

    private Calendar calendar;
    private int year;
    private int month;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        sysApplication = (SysApplication) getActivity().getApplication();
        context = getContext();
        if (inflater != null) {
            view = inflater.inflate(R.layout.fragment_month_summary, container, false);
            initView(view);
            initHandler();
            getData(true);
        }
        return view;
    }


    public void initView(View view) {
        calendar = Calendar.getInstance();
        time = calendar.getTime();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        tvMonth = view.findViewById(R.id.tvMonth);
        tvTotalMoney = view.findViewById(R.id.tvTotalMoney);
        date = DateUtils.getYYMM(time);
        tvMonth.setText(date);


        view.findViewById(R.id.btnBeforeMonth).setOnClickListener(this);
        view.findViewById(R.id.btnNextMonth).setOnClickListener(this);

        lvDaySummary = view.findViewById(R.id.lvMonthSummary);
        data = new ArrayList<>();
        dataAdapter = new MonthAdapter(context, data);
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

    Handler handler;

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 913:
                        dataAdapter.notifyDataSetChanged();
                        tvTotalMoney.setText(MyMatch.accurate2(totalMoney) + "元");
                        break;
                }

                return false;
            }
        });
    }

    private void getData(final boolean isFirst) {
        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                setData();
                OrderInfoDao orderInfoDao = new OrderInfoDao(context);
                List<OrderInfo> list = orderInfoDao.queryByMonth(date);
                totalMoney = 0.00f;

                for (OrderInfo orderInfo : list) {
                    int dayIndex = orderInfo.getDay() - 1;
                    ReportResultBean resultBean = data.get(dayIndex);
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

                handler.sendEmptyMessage(913);
            }

            private void setData() {
                data.clear();
                data.add(0, new ReportResultBean(year + "-" + month + "-01"));
                data.add(1, new ReportResultBean(year + "-" + month + "-02"));
                data.add(2, new ReportResultBean(year + "-" + month + "-03"));
                data.add(3, new ReportResultBean(year + "-" + month + "-04"));
                data.add(4, new ReportResultBean(year + "-" + month + "-05"));
                data.add(5, new ReportResultBean(year + "-" + month + "-06"));
                data.add(6, new ReportResultBean(year + "-" + month + "-07"));
                data.add(7, new ReportResultBean(year + "-" + month + "-08"));
                data.add(8, new ReportResultBean(year + "-" + month + "-09"));
                data.add(9, new ReportResultBean(year + "-" + month + "-10"));
                data.add(10, new ReportResultBean(year + "-" + month + "-11"));
                data.add(11, new ReportResultBean(year + "-" + month + "-12"));
                data.add(12, new ReportResultBean(year + "-" + month + "-13"));
                data.add(13, new ReportResultBean(year + "-" + month + "-14"));
                data.add(14, new ReportResultBean(year + "-" + month + "-15"));
                data.add(15, new ReportResultBean(year + "-" + month + "-16"));
                data.add(16, new ReportResultBean(year + "-" + month + "-17"));
                data.add(17, new ReportResultBean(year + "-" + month + "-18"));
                data.add(18, new ReportResultBean(year + "-" + month + "-19"));
                data.add(19, new ReportResultBean(year + "-" + month + "-20"));
                data.add(20, new ReportResultBean(year + "-" + month + "-21"));
                data.add(21, new ReportResultBean(year + "-" + month + "-22"));
                data.add(22, new ReportResultBean(year + "-" + month + "-23"));
                data.add(23, new ReportResultBean(year + "-" + month + "-24"));
                data.add(24, new ReportResultBean(year + "-" + month + "-25"));
                data.add(25, new ReportResultBean(year + "-" + month + "-26"));
                data.add(26, new ReportResultBean(year + "-" + month + "-27"));
                data.add(27, new ReportResultBean(year + "-" + month + "-28"));
                data.add(28, new ReportResultBean(year + "-" + month + "-29"));
                data.add(29, new ReportResultBean(year + "-" + month + "-30"));
                data.add(30, new ReportResultBean(year + "-" + month + "-31"));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBeforeMonth:

                calendar.add(Calendar.MONTH, -1); //向
                time = calendar.getTime();
                date = DateUtils.getYYMM(time);
                tvMonth.setText(date);
                tvTotalMoney.setText("0.00");
                getData(false);
//                getReportsList(currentDay, typeVal + "", --currentPage <= 1 ? (currentPage = 1) + "" : --currentPage + "", pageNum + "");
                break;
            case R.id.btnNextMonth:
                calendar.add(Calendar.MONTH, 1); //向
                time = calendar.getTime();
                date = DateUtils.getYYMM(time);
                tvMonth.setText(date);
                tvTotalMoney.setText("0.00");
                getData(false);
//                getReportsList(currentDay, typeVal + "", --currentPage <= 1 ? (currentPage = 1) + "" : --currentPage + "", pageNum + "");
                break;

        }
    }


    public class MonthAdapter extends BaseAdapter {
        private Context context;
        private List<ReportResultBean> list;

        public MonthAdapter(Context context, List<ReportResultBean> list) {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.data_summary_item, null);
                holder = new ViewHolder();
                holder.timeTv = convertView.findViewById(R.id.data_item_time_tv);
                holder.countTv = convertView.findViewById(R.id.data_item_count_tv);
                holder.weightTv = convertView.findViewById(R.id.data_item_weight_tv);
                holder.grandTotalTv = convertView.findViewById(R.id.data_item_grand_total_tv);
                holder.incomeTv = convertView.findViewById(R.id.data_item_income_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ReportResultBean item = list.get(position);
            holder.timeTv.setText(item.getTimes());
            holder.countTv.setText(item.getAll_num() + "");
            holder.incomeTv.setText(MyMatch.accurate2(item.getTotal_amount()));
            holder.grandTotalTv.setText(MyMatch.accurate2(item.getTotal_amount()));
            holder.weightTv.setText(MyMatch.accurate3(item.getTotal_weight()));
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
