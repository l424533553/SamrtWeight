//package com.axecom.iweight.ui.activity;
//
//import android.content.Context;
//import android.os.SystemClock;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.axecom.iweight.R;
//import com.axecom.iweight.base.BaseActivity;
//import com.axecom.iweight.base.BaseEntity;
//import com.axecom.iweight.bean.LocalSettingsBean;
//import com.axecom.iweight.bean.OrderGoods;
//import com.axecom.iweight.bean.OrderListResultBean;
//import com.axecom.iweight.bean.OrderLocal;
//import com.axecom.iweight.bean.ReportResultBean;
//import com.axecom.iweight.manager.AccountManager;
//import com.axecom.iweight.manager.MacManager;
//import com.axecom.iweight.manager.PrinterManager;
//import com.axecom.iweight.manager.ThreadPool;
//import com.axecom.iweight.my.entity.OrderInfo;
//import com.axecom.iweight.my.entity.dao.OrderInfoDao;
//import com.axecom.iweight.net.RetrofitFactory;
//import com.axecom.iweight.utils.LogUtils;
//import com.axecom.iweight.utils.SPUtils;
//import com.raizlabs.android.dbflow.sql.language.SQLite;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//
///**
// * Created by Administrator on 2018-5-24.
// */
//
//public class DataSummaryActivity extends BaseActivity {
//
//    private View rootView;
//    private ListView dataListView;
//    private ListView salesDetailsListView;
//    private DataAdapter dataAdapter;
//    private SalesAdapter salesAdapter;
//    private LinearLayout reportTitleLayout;
//    private LinearLayout reportTotalLayout;
//    private LinearLayout salesTitleLayout;
//    private LinearLayout salesTotalLayout;
//    private TextView dayReportTv;
//    private TextView monthReportTv;
//    private TextView salesDetailsReportTv;
//    private TextView backTv;
//    private TextView printTv;
//    private TextView dateTv;
//
//    private List<OrderListResultBean.list> orderList;
//
//    private TextView countTotalTv, weightTotalTv, grandTotalTv, amountTotalTv;
//    private TextView orderAmountTv;
//    private Button prevPageBtn, nextPageBtn, prevMonthBtn, nextMonthBtn, prevDayBtn, nextDayBtn;
//    private Button salesDetailsPrevPageBtn, salesDetailsNextPageBtn, salesDetailsPrevDayBtn, salesDetailsNextDayBtn;
//    private int currentPage = 1;
//    private String currentDay;
//    private int typeVal = 1;
//    private int pageNum = 9;
//    private int previousPos = 10;
//    private int orderType = 1;
//    private ReportResultBean reportResultBean;
//    private OrderListResultBean orderListResultBean;
//    private PrinterManager printerManager;
//    private String port = "/dev/ttyS4";
//
//    @Override
//    public View setInitView() {
//        rootView = LayoutInflater.from(this).inflate(R.layout.data_summary_activity, null);
//        dataListView = rootView.findViewById(R.id.data_summary_listview);
//        salesDetailsListView = rootView.findViewById(R.id.sales_details_listview);
//        reportTitleLayout = rootView.findViewById(R.id.data_summary_reports_title_layout);
//        reportTotalLayout = rootView.findViewById(R.id.data_summary_reports_total_layout);
//        salesTitleLayout = rootView.findViewById(R.id.data_summary_sales_title_layout);
//        salesTotalLayout = rootView.findViewById(R.id.data_summary_sales_total_layout);
//        dayReportTv = rootView.findViewById(R.id.data_summary_day_report_tv);
//        monthReportTv = rootView.findViewById(R.id.data_summary_month_report_tv);
//        salesDetailsReportTv = rootView.findViewById(R.id.data_summary_sales_details_report_tv);
//        backTv = rootView.findViewById(R.id.data_summary_back_tv);
//        dateTv = rootView.findViewById(R.id.data_summary_date_tv);
//        printTv = rootView.findViewById(R.id.data_summary_print_tv);
//        countTotalTv = rootView.findViewById(R.id.data_summary_reports_count_total_tv);
//        orderAmountTv = rootView.findViewById(R.id.data_summary_order_amount_total_tv);
//        weightTotalTv = rootView.findViewById(R.id.data_summary_reports_weight_total_tv);
//        grandTotalTv = rootView.findViewById(R.id.data_summary_reports_grand_total_tv);
//        amountTotalTv = rootView.findViewById(R.id.data_summary_reports_amount_total_tv);
//        prevPageBtn = rootView.findViewById(R.id.data_summary_reports_prev_page_btn);
//        nextPageBtn = rootView.findViewById(R.id.data_summary_reports_next_page_btn);
//        prevMonthBtn = rootView.findViewById(R.id.data_summary_reports_prev_month_btn);
//        nextMonthBtn = rootView.findViewById(R.id.data_summary_reports_next_month_btn);
//        prevDayBtn = rootView.findViewById(R.id.data_summary_reports_prev_day_btn);
//        nextDayBtn = rootView.findViewById(R.id.data_summary_reports_next_day_btn);
//        salesDetailsPrevPageBtn = rootView.findViewById(R.id.data_summary_sales_details_prev_page_btn);
//        salesDetailsNextPageBtn = rootView.findViewById(R.id.data_summary_sales_details_next_page_btn);
//        salesDetailsPrevDayBtn = rootView.findViewById(R.id.data_summary_sales_details_prev_day_btn);
//        salesDetailsNextDayBtn = rootView.findViewById(R.id.data_summary_sales_details_next_day_btn);
//
//
//        dayReportTv.setOnClickListener(this);
//        printTv.setOnClickListener(this);
//        monthReportTv.setOnClickListener(this);
//        salesDetailsReportTv.setOnClickListener(this);
//        backTv.setOnClickListener(this);
//        prevPageBtn.setOnClickListener(this);
//        nextPageBtn.setOnClickListener(this);
//        prevMonthBtn.setOnClickListener(this);
//        nextMonthBtn.setOnClickListener(this);
//        prevDayBtn.setOnClickListener(this);
//        nextDayBtn.setOnClickListener(this);
//        salesDetailsPrevPageBtn.setOnClickListener(this);
//        salesDetailsNextPageBtn.setOnClickListener(this);
//        salesDetailsPrevDayBtn.setOnClickListener(this);
//        salesDetailsNextDayBtn.setOnClickListener(this);
//        return rootView;
//    }
//
//    @Override
//    public void initView() {
//        printerManager = new PrinterManager(this);
//        LocalSettingsBean.Value.PrinterPort printerPort = (LocalSettingsBean.Value.PrinterPort) SPUtils.readObject(this, LocalSettingsActivity.KEY_PRINTER_PORT);
//        if (printerPort != null) {
//            port = printerPort.val.split("：")[1];
//        }
//        if (TextUtils.equals(port, "/dev/ttyS4")) {
//            printerManager.openGpinter();
//        } else {
//            printerManager.usbConn();
//        }
//        currentDay = getCurrentTime("yyyy-MM-dd");
//        getReportsList(currentDay, typeVal + "", currentPage + "", pageNum + "");
////        dataList = new ArrayList<>();
////        dataAdapter = new DataAdapter(this, dataList);
////        dataListView.setAdapter(dataAdapter);
//
//        orderList = new ArrayList<>();
//        salesAdapter = new SalesAdapter(this, orderList);
//        salesDetailsListView.setAdapter(salesAdapter);
//        dateTv.setText(getCurrentTime("yyyy-MM-dd"));
//
//        Context context = this;
//        OrderInfoDao<OrderInfo> orderInfoDao = new OrderInfoDao<>(context);
////        List<OrderInfo> orderInfos = orderInfoDao.queryAll();
//
//    }
//
//    public void getReportsList(final String dateVal, String typeVal, String page, final String pNum) {
//        RetrofitFactory.getInstance().API()
//                .getReportsList(AccountManager.getInstance().getAdminToken(), MacManager.getInstace(this).getMac(), dateVal, typeVal, page, pNum)
//                .compose(this.<BaseEntity<ReportResultBean>>setThread())
//                .subscribe(new Observer<BaseEntity<ReportResultBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        showLoading();
//                    }
//
//                    @Override
//                    public void onNext(BaseEntity<ReportResultBean> reportResultBeanBaseEntity) {
//                        if (reportResultBeanBaseEntity.isSuccess()) {
//                            reportResultBean = reportResultBeanBaseEntity.getData();
////                            dataList.clear();
////                            dataList.addAll(reportResultBean.list);
////                            LogUtils.d("-----list size " + dataList.size());
////                            dataAdapter.notifyDataSetChanged();
////                            countTotalTv.setText(reportResultBean.total_num + "");
////                            weightTotalTv.setText(reportResultBean.total_weight + "kg/" + reportResultBean.all_number + "件");
////                            grandTotalTv.setText(reportResultBean.total_amount);
////                            amountTotalTv.setText(reportResultBean.total_amount);
//                            closeLoading();
//                        } else {
//                            showLoading(reportResultBeanBaseEntity.getMsg());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        closeLoading();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        closeLoading();
//                        dataAdapter.notifyDataSetChanged();
//                    }
//                });
//    }
//
//    public void getOrderList(String dateVal, String page, String pageNum) {
//        RetrofitFactory.getInstance().API()
//                .getOrderList(AccountManager.getInstance().getAdminToken(), MacManager.getInstace(this).getMac(), dateVal, page, pageNum)
//                .compose(this.<BaseEntity<OrderListResultBean>>setThread())
//                .subscribe(new Observer<BaseEntity<OrderListResultBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        showLoading();
//                    }
//
//                    @Override
//                    public void onNext(BaseEntity<OrderListResultBean> orderListResultBeanBaseEntity) {
//                        if (orderListResultBeanBaseEntity.isSuccess()) {
//                            orderListResultBean = orderListResultBeanBaseEntity.getData();
//                            orderList.clear();
//                            orderList.addAll(orderListResultBean.list);
//                            salesAdapter.notifyDataSetChanged();
//                            scrollTo(salesDetailsListView, salesDetailsListView.getCount() - 1);
//
//                            orderAmountTv.setText(orderListResultBeanBaseEntity.getData().total_amount);
//                        } else {
//                            showLoading(orderListResultBeanBaseEntity.getMsg());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        closeLoading();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        closeLoading();
//                    }
//                });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.data_summary_day_report_tv:
//                orderType = 1;
//                currentDay = getCurrentTime("yyyy-MM-dd");
//                dateTv.setText(currentDay);
////                dataList.clear();
//                currentPage = 1;
//                typeVal = 1;
//                reportTitleLayout.setVisibility(View.VISIBLE);
//                reportTotalLayout.setVisibility(View.VISIBLE);
//                dataListView.setVisibility(View.VISIBLE);
//                salesTitleLayout.setVisibility(View.GONE);
//                salesTotalLayout.setVisibility(View.GONE);
//                salesDetailsListView.setVisibility(View.GONE);
//                prevMonthBtn.setVisibility(View.GONE);
//                nextMonthBtn.setVisibility(View.GONE);
//                prevDayBtn.setVisibility(View.VISIBLE);
//                nextDayBtn.setVisibility(View.VISIBLE);
//                prevPageBtn.setVisibility(View.GONE);
//                nextPageBtn.setVisibility(View.GONE);
//
//                salesDetailsPrevPageBtn.setVisibility(View.GONE);
//                salesDetailsNextPageBtn.setVisibility(View.GONE);
//                salesDetailsPrevDayBtn.setVisibility(View.GONE);
//                salesDetailsNextDayBtn.setVisibility(View.GONE);
//                dayReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_gray_btn_bg));
//                monthReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
//                salesDetailsReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
//                getReportsList(currentDay, typeVal + "", currentPage + "", pageNum + "");
//                break;
//            case R.id.data_summary_month_report_tv:
//                orderType = 2;
//                currentDay = getCurrentTime("yyyy-MM");
//                dateTv.setText(currentDay);
////                dataList.clear();
//                currentPage = 1;
//                typeVal = 2;
//                reportTitleLayout.setVisibility(View.VISIBLE);
//                reportTotalLayout.setVisibility(View.VISIBLE);
//                dataListView.setVisibility(View.VISIBLE);
//                salesTitleLayout.setVisibility(View.GONE);
//                salesTotalLayout.setVisibility(View.GONE);
//                salesDetailsListView.setVisibility(View.GONE);
//                prevMonthBtn.setVisibility(View.VISIBLE);
//                nextMonthBtn.setVisibility(View.VISIBLE);
//                prevDayBtn.setVisibility(View.GONE);
//                nextDayBtn.setVisibility(View.GONE);
//                prevPageBtn.setVisibility(View.GONE);
//                nextPageBtn.setVisibility(View.GONE);
//
//                salesDetailsPrevPageBtn.setVisibility(View.GONE);
//                salesDetailsNextPageBtn.setVisibility(View.GONE);
//                salesDetailsPrevDayBtn.setVisibility(View.GONE);
//                salesDetailsNextDayBtn.setVisibility(View.GONE);
//                dayReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
//                monthReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_gray_btn_bg));
//                salesDetailsReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
//                getReportsList(currentDay, typeVal + "", currentPage + "", pageNum + "");
//                break;
//            case R.id.data_summary_sales_details_report_tv:
//                orderType = 3;
//                currentDay = getCurrentTime("yyyy-MM-dd");
//                dateTv.setText(currentDay);
//                orderList.clear();
//                reportTitleLayout.setVisibility(View.GONE);
//                reportTotalLayout.setVisibility(View.GONE);
//                dataListView.setVisibility(View.GONE);
//                salesTitleLayout.setVisibility(View.VISIBLE);
//                salesTotalLayout.setVisibility(View.VISIBLE);
//                salesDetailsListView.setVisibility(View.VISIBLE);
//                prevPageBtn.setVisibility(View.GONE);
//                nextPageBtn.setVisibility(View.GONE);
//                prevMonthBtn.setVisibility(View.GONE);
//                nextMonthBtn.setVisibility(View.GONE);
//                prevDayBtn.setVisibility(View.GONE);
//                nextDayBtn.setVisibility(View.GONE);
//
//                salesDetailsPrevPageBtn.setVisibility(View.VISIBLE);
//                salesDetailsNextPageBtn.setVisibility(View.VISIBLE);
//                salesDetailsPrevDayBtn.setVisibility(View.VISIBLE);
//                salesDetailsNextDayBtn.setVisibility(View.VISIBLE);
//
//                dayReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
//                monthReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_white_btn_bg));
//                salesDetailsReportTv.setBackground(DataSummaryActivity.this.getResources().getDrawable(R.drawable.shape_gray_btn_bg));
//                getOrderList(getCurrentTime("yyyy-MM-dd"), "1", "10");
//                break;
//            case R.id.data_summary_back_tv:
//                finish();
//                break;
//            case R.id.data_summary_reports_prev_page_btn:
//                getReportsList(currentDay, typeVal + "", --currentPage <= 1 ? (currentPage = 1) + "" : --currentPage + "", pageNum + "");
//                break;
//            case R.id.data_summary_reports_next_page_btn:
//                getReportsList(currentDay, typeVal + "", ++currentPage + "", pageNum + "");
//                break;
//            case R.id.data_summary_reports_prev_month_btn:
//                currentPage = 1;
//                currentDay = getMonthTime(currentDay, "yyyy-MM", 1);
//                dateTv.setText(currentDay);
//                getReportsList(currentDay, typeVal + "", (currentPage = 1) + "", pageNum + "");
//                break;
//            case R.id.data_summary_reports_next_month_btn:
//                currentPage = 1;
//                currentDay = getMonthTime(currentDay, "yyyy-MM", 2);
//                dateTv.setText(currentDay);
//                getReportsList(currentDay, typeVal + "", (currentPage = 1) + "", pageNum + "");
//                break;
//            case R.id.data_summary_reports_prev_day_btn:
//                currentDay = getCurrentTime(currentDay, "yyyy-MM-dd", 3);
//                dateTv.setText(currentDay);
//                getReportsList(currentDay, typeVal + "", (currentPage = 1) + "", pageNum + "");
//                break;
//            case R.id.data_summary_reports_next_day_btn:
//                currentDay = getCurrentTime(currentDay, "yyyy-MM-dd", 4);
//                dateTv.setText(currentDay);
//                getReportsList(currentDay, typeVal + "", (currentPage = 1) + "", pageNum + "");
//                break;
//            case R.id.data_summary_sales_details_prev_page_btn:
//                scrollTo(salesDetailsListView, salesDetailsListView.getFirstVisiblePosition() - previousPos <= 0 ? 0 : salesDetailsListView.getFirstVisiblePosition() - previousPos);
//
////                getOrderList(currentDay,  --currentPage <= 1 ? (currentPage=1)+"" : --currentPage + "", "10");
//                break;
//            case R.id.data_summary_sales_details_next_page_btn:
//                getOrderList(currentDay, ++currentPage + "", previousPos + "");
//                break;
//            case R.id.data_summary_sales_details_prev_day_btn:
//                currentDay = getCurrentTime(currentDay, "yyyy-MM-dd", 3);
//                dateTv.setText(currentDay);
//                getOrderList(currentDay, "1", "10");
//                break;
//            case R.id.data_summary_sales_details_next_day_btn:
//                currentDay = getCurrentTime(currentDay, "yyyy-MM-dd", 4);
//                dateTv.setText(currentDay);
//                getOrderList(currentDay, "1", "10");
//                break;
//            case R.id.data_summary_print_tv:
//                printerOrder(orderType, reportResultBean);
//                break;
//        }
//    }
//
//    private ThreadPool threadPool;
//
//    public void printerOrder(final int orderType, final ReportResultBean reportResultBean) {
//        threadPool = ThreadPool.getInstantiation();
//        threadPool.addTask(new Runnable() {
//            @Override
//            public void run() {
//                switch (orderType) {
//                    case 1:
//                    case 2:
////                        printerManager.printerOrderOfDayAndMonth(dataList, reportResultBean);
//                        break;
//                    case 3:
//                        printerManager.printerOrderDetails(orderList, orderListResultBean);
//                        break;
//                }
//            }
//        });
//    }
//
//    public void insert(View view) {
//        String uuid = UUID.randomUUID().toString();
//        OrderLocal orderLocal = new OrderLocal();
//        orderLocal.companyName = "teset";
////                orderLocal.orderTime = new Date(System.currentTimeMillis());
//        orderLocal.orderNumber = uuid;
//        orderLocal.save();
//
//        for (int i = 0; i < 10; i++) {
//            OrderGoods goods = new OrderGoods();
//            goods.name = "good" + i;
//            goods.orderNumber = uuid;
//            goods.save();
//        }
//    }
//
//
//    public void read(View view) {
//        List<OrderGoods> goods = SQLite.select().from(OrderGoods.class).queryList();
//        for (OrderGoods goods1 : goods) {
//            LogUtils.d(goods1.toString());
//        }
////        select DATE_FORMAT(start_time,'%Y%m') months,count(product_no) count from test group bymonths;
//
////        SQLite.select(count(Employee_Table.name), sum(Employee_Table.salary))
////                .from(Employee.class)
////        SELECT COUNT(`name`), SUM(`salary`) FROM `Employee`;
//
//
//    }
//
//    public void readGoods(View view) {
//        List<OrderLocal> orderLocals = SQLite.select().from(OrderLocal.class).queryList();
//        for (OrderLocal orderLocal : orderLocals) {
//            LogUtils.d(orderLocal.toString());
//        }
////        SQLite.select().from(Goo)
//    }
//
////    class DataAdapter extends BaseAdapter {
////        private Context context;
//////        private List<ReportResultBean.list> list;
////
////        public DataAdapter(Context context, List<ReportResultBean.list> list) {
////            this.context = context;
////            this.list = list;
////        }
////
////        @Override
////        public int getCount() {
////            return list.size();
////        }
////
////        @Override
////        public Object getItem(int position) {
////            return list.get(position);
////        }
////
////        @Override
////        public long getItemId(int position) {
////            return position;
////        }
////
////        @Override
////        public View getView(int position, View convertView, ViewGroup parent) {
////            ViewHolder holder;
////            if (convertView == null) {
////                convertView = LayoutInflater.from(context).inflate(R.layout.data_summary_item, null);
////                holder = new ViewHolder();
////                holder.timeTv = convertView.findViewById(R.id.data_item_time_tv);
////                holder.countTv = convertView.findViewById(R.id.data_item_count_tv);
////                holder.weightTv = convertView.findViewById(R.id.data_item_weight_tv);
////                holder.grandTotalTv = convertView.findViewById(R.id.data_item_grand_total_tv);
////                holder.incomeTv = convertView.findViewById(R.id.data_item_income_tv);
////                convertView.setTag(holder);
////            } else {
////                holder = (ViewHolder) convertView.getTag();
////            }
////            ReportResultBean.list item = list.get(position);
////            holder.timeTv.setText(item.times);
////            holder.countTv.setText(item.all_num + "");
////            holder.incomeTv.setText(item.total_amount);
////            holder.grandTotalTv.setText(item.total_amount);
////            holder.weightTv.setText(item.total_weight + "kg/" + item.total_number + "件");
////            return convertView;
////        }
////
////        class ViewHolder {
////            TextView timeTv;
////            TextView countTv;
////            TextView weightTv;
////            TextView grandTotalTv;
////            TextView incomeTv;
////        }
////    }
////
////    class SalesAdapter extends BaseAdapter {
////
////        private Context context;
////        private List<OrderListResultBean.list> list;
////
////
////        public SalesAdapter(Context context, List<OrderListResultBean.list> list) {
////            this.context = context;
////            this.list = list;
////        }
////
////        @Override
////        public int getCount() {
////            return list.size();
////        }
////
////        @Override
////        public Object getItem(int position) {
////            return list.get(position);
////        }
////
////        @Override
////        public long getItemId(int position) {
////            return position;
////        }
////
////        @Override
////        public View getView(int position, View convertView, ViewGroup parent) {
////            ViewHolder holder = null;
////            if (convertView == null) {
////                convertView = LayoutInflater.from(context).inflate(R.layout.sales_data_item, null);
////                holder = new ViewHolder();
////                holder.goodsNameTv = convertView.findViewById(R.id.sales_data_item_goods_name_tv);
////                holder.timeTv = convertView.findViewById(R.id.sales_data_item_time_tv);
////                holder.weightTv = convertView.findViewById(R.id.sales_data_item_weight_tv);
////                holder.pricePriceTv = convertView.findViewById(R.id.sales_data_item_number_tv);
////                holder.priceNumberTv = convertView.findViewById(R.id.sales_data_item_price_number_tv);
////                holder.totalAmountTv = convertView.findViewById(R.id.sales_data_item_total_amount_tv);
////                holder.payTypeTv = convertView.findViewById(R.id.sales_data_item_pay_type_tv);
////                convertView.setTag(holder);
////            } else {
////                holder = (ViewHolder) convertView.getTag();
////            }
////
////            OrderListResultBean.list item = list.get(position);
////            holder.goodsNameTv.setText(item.goods_name);
////            holder.timeTv.setText(item.times);
////            holder.weightTv.setText(item.goods_weight);
////            holder.pricePriceTv.setText(item.goods_price);
////            holder.priceNumberTv.setText(item.goods_number);
////            holder.totalAmountTv.setText(item.total_amount);
////            holder.payTypeTv.setText(item.payment_type);
////
////            return convertView;
////        }
////
////        class ViewHolder {
////            TextView goodsNameTv;
////            TextView timeTv;
////            TextView weightTv;
////            TextView pricePriceTv;
////            TextView priceNumberTv;
////            TextView totalAmountTv;
////            TextView payTypeTv;
////        }
////    }
//}
