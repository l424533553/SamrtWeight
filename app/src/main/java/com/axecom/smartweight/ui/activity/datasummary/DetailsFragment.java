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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.bean.ReportResultBean;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.dao.OrderInfoDao;
import com.luofx.listener.IMyItemOnLongclick;
import com.luofx.listener.IMyItemOnclick123;
import com.luofx.utils.DateUtils;
import com.luofx.utils.match.MyMatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 资料 模块
 * 待定
 * Created by Administrator on 2018/9/19.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener {

    /**
     * ViewPager控件，承载各Fragment的容器
     */

    private Context context;
    private SysApplication sysApplication;
    private ExpandableListView lvSearch;
    private List<OrderInfo> data;
    private Date time;
    private TextView tvMonth;
    private TextView tvTotalMoney;
    private String date;
    private MyAdapte adapter;
    private float totalMoney;

    private Calendar calendar;
    private int year;
    private int month;
    private OrderInfoDao orderInfoDao;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        sysApplication = (SysApplication) getActivity().getApplication();
        context = getContext();
        if (inflater != null) {
            view = inflater.inflate(R.layout.fragment_details_summary, container, false);
            orderInfoDao = new OrderInfoDao(context);
            initView(view);
            initHandler();
            getData(true);

            /* initListeners(view);*/
        }
        return view;
    }


    public void initView(View view) {
        calendar = Calendar.getInstance();
        time = calendar.getTime();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);

        tvMonth = view.findViewById(R.id.tvMonth);
        tvTotalMoney = view.findViewById(R.id.tvTotalMoney);
        date = DateUtils.getYYMMDD(time);
        tvMonth.setText(date);

        view.findViewById(R.id.btnBefore).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.btnPrinter).setOnClickListener(this);

        lvSearch = view.findViewById(R.id.lvSearch);
        data = new ArrayList<>();

        adapter = new MyAdapte(context, data);
//        adapter.setMyItemOnClickListener(this);
//        adapter.setMyItemOnclick(this);
//        adapter.setMyItemOnClickListener(this);
        lvSearch.setAdapter(adapter);
        lvSearch.setGroupIndicator(null);//这个是去掉父级的箭头
        lvSearch.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;  //返回true,表示不可点击
            }
        });
    }

    private void getData(final boolean isFirst) {
        sysApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
//                setData();
                List<OrderInfo> list = orderInfoDao.queryByDay(date, false);
                totalMoney = 0.00f;

                for (OrderInfo orderInfo : list) {
                    orderInfo.setOrderItem(new ArrayList<>(orderInfo.getOrderBeans()));
//                    int dayIndex = orderInfo.getDay() - 1;
//                    ReportResultBean resultBean = data.get(dayIndex);
//                    resultBean.setAll_num(resultBean.getAll_num() + 1);
//                    resultBean.setTotal_amount(resultBean.getTotal_amount() + Float.valueOf(orderInfo.getTotalamount()));
//                    resultBean.setTotal_weight(resultBean.getTotal_weight() + Float.valueOf(orderInfo.getTotalweight()));

                    totalMoney += Float.valueOf(orderInfo.getTotalamount());
                }
                data.addAll(list);

                handler.sendEmptyMessage(914);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBefore:

                calendar.add(Calendar.DATE, -1); //向
                time = calendar.getTime();
                date = DateUtils.getYYMMDD(time);
                tvMonth.setText(date);
                tvTotalMoney.setText("0.00");
                getData(false);
//                getReportsList(currentDay, typeVal + "", --currentPage <= 1 ? (currentPage = 1) + "" : --currentPage + "", pageNum + "");
                break;
            case R.id.btnNext:
                calendar.add(Calendar.DATE, 1); //向
                time = calendar.getTime();
                date = DateUtils.getYYMMDD(time);
                tvMonth.setText(date);
                tvTotalMoney.setText("0.00");
                getData(false);
//                getReportsList(currentDay, typeVal + "", --currentPage <= 1 ? (currentPage = 1) + "" : --currentPage + "", pageNum + "");
                break;

        }
    }

    Handler handler;

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 914:
                        adapter.notifyDataSetChanged();
                        for (int i = 0; i < adapter.getGroupCount(); i++) {
                            lvSearch.expandGroup(i);
                        }
                        tvTotalMoney.setText(MyMatch.accurate2(totalMoney)  + "元");
                        break;
                }
                return false;
            }
        });
    }


    /**
     * 说明：
     * 作者：User_luo on 2018/4/23 16:29
     * 邮箱：424533553@qq.com
     */
    public class MyAdapte extends BaseExpandableListAdapter {

        private IMyItemOnLongclick myItemOnLongclick;

        public void setMyItemOnClickListener(IMyItemOnLongclick myItemOnClickListener) {
            this.myItemOnLongclick = myItemOnClickListener;
        }

        private IMyItemOnclick123 myItemOnclick;

        public void setMyItemOnclick(IMyItemOnclick123 myItemOnclick) {
            this.myItemOnclick = myItemOnclick;
        }

        private Context context;
        private List<OrderInfo> data;

        public MyAdapte(Context context, List<OrderInfo> data) {
            this.data = data;
            this.context = context;
        }


        @Override
        public int getGroupCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            OrderInfo sampleFragmentBean = data.get(groupPosition);
            return sampleFragmentBean == null ? 0 : sampleFragmentBean.getOrderBeans().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return data.get(groupPosition);
        }

        @Override
        public OrderBean getChild(int groupPosition, int childPosition) {

            return data.get(groupPosition).getOrderItem().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = View.inflate(context, R.layout.item_sample_fragment_group, null);
                holder.tvTotalAmount = convertView.findViewById(R.id.tvTotalAmount);
                holder.tvOrderNo = convertView.findViewById(R.id.tvOrderNo);
                holder.tvTime = convertView.findViewById(R.id.tvTime);

                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            holder.tvOrderNo.setText(data.get(groupPosition).getBillcode());
            holder.tvTotalAmount.setText(data.get(groupPosition).getTotalamount());
            holder.tvTime.setText(data.get(groupPosition).getTime());
//            convertView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    myItemOnLongclick.myItemGroupLongClick(groupPosition, isExpanded);
//                    return false;
//                }
//            });
//
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    myItemOnclick.myItemGroupClick(groupPosition, isExpanded);
//                }
//            });
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = View.inflate(context, R.layout.item_child_details, null);
                holder.tvName = convertView.findViewById(R.id.tvName);
                holder.tvPrice = convertView.findViewById(R.id.tvPrice);
                holder.tvWeight = convertView.findViewById(R.id.tvWeight);
                holder.tvMoney = convertView.findViewById(R.id.tvMoney);
                holder.tvUnit = convertView.findViewById(R.id.tvUnit);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
//            convertView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    myItemOnLongclick.myItemChildLongClick(groupPosition, childPosition, isLastChild);
//                    return false;
//                }
//            });
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    myItemOnclick.myItemChildClick(groupPosition, childPosition, isLastChild);
//                }
//            });

            OrderBean item = getChild(groupPosition, childPosition);

            holder.tvName.setText(item.getName() + "");
            holder.tvPrice.setText(item.getPrice() + "");
            holder.tvWeight.setText(item.getWeight() + "");
            holder.tvMoney.setText(item.getMoney());
            holder.tvUnit.setText(item.getUnit() + "");
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private class GroupHolder {

            private TextView tvTotalAmount, tvOrderNo,tvTime;

        }

        private class ChildHolder {
            TextView tvName;
            TextView tvPrice;
            TextView tvWeight;
            TextView tvMoney;
            TextView tvUnit;
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
            MonthAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.data_summary_item, null);
                holder = new MonthAdapter.ViewHolder();
                holder.timeTv = convertView.findViewById(R.id.data_item_time_tv);
                holder.countTv = convertView.findViewById(R.id.data_item_count_tv);
                holder.weightTv = convertView.findViewById(R.id.data_item_weight_tv);
                holder.grandTotalTv = convertView.findViewById(R.id.data_item_grand_total_tv);
                holder.incomeTv = convertView.findViewById(R.id.data_item_income_tv);
                convertView.setTag(holder);
            } else {
                holder = (MonthAdapter.ViewHolder) convertView.getTag();
            }
            ReportResultBean item = list.get(position);
            holder.timeTv.setText(item.getTimes());
            holder.countTv.setText(item.getAll_num() + "");
            holder.incomeTv.setText(item.getTotal_amount() + "");
            holder.grandTotalTv.setText(item.getTotal_amount() + "");
            holder.weightTv.setText(item.getTotal_weight() + "");
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
