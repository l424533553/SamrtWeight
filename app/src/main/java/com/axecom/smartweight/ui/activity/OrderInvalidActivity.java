package com.axecom.smartweight.ui.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BaseActivity;
import com.axecom.smartweight.base.BaseEntity;
import com.axecom.smartweight.bean.UnusualOrdersBean;
import com.axecom.smartweight.manager.AccountManager;
import com.axecom.smartweight.manager.MacManager;
import com.axecom.smartweight.net.RetrofitFactory;
import com.axecom.smartweight.ui.view.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018-5-22.
 */

public class OrderInvalidActivity extends BaseActivity {

    private View rootView;
    private ListView orderListView;
    private OrderAdapter orderAdapter;
    private Button previousBtn, nextBtn, backBtn;
    private int previousPos = 12;
    private int nextPos = 16;
    private CustomDialog mDialog;
    private CustomDialog.Builder builder;
    private List<UnusualOrdersBean.Order> orderList;
    private Set<UnusualOrdersBean.Order> orderSet;
    private Map<String, UnusualOrdersBean.Order> orderMap;
    private int currentPage = 1;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.order_invalid_activity, null);
        orderListView = rootView.findViewById(R.id.order_invalid_listview);
        previousBtn = rootView.findViewById(R.id.order_invalid_previous_btn);
        nextBtn = rootView.findViewById(R.id.order_invalid_next_btn);
        backBtn = rootView.findViewById(R.id.order_invalid_back_btn);
        builder = new CustomDialog.Builder(this);

        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void initView() {
        getOrders(currentPage + "", previousPos + "", "1");
        orderList = new ArrayList<>();
        orderMap = new HashMap<>();
        orderAdapter = new OrderAdapter(this, orderMap, orderList);
        orderListView.setAdapter(orderAdapter);
//        orderListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    public void getOrders(String page, String pageNum, String typeVal) {
        RetrofitFactory.getInstance().API()
                .getOrders(AccountManager.getInstance().getAdminToken(), MacManager.getInstace(this).getMac(), page, pageNum, typeVal)
                .compose(this.<BaseEntity<UnusualOrdersBean>>setThread())
                .subscribe(new Observer<BaseEntity<UnusualOrdersBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();
                    }

                    @Override
                    public void onNext(BaseEntity<UnusualOrdersBean> unusualOrdersBeanBaseEntity) {
                        if (unusualOrdersBeanBaseEntity.isSuccess()) {
                            orderList.addAll(unusualOrdersBeanBaseEntity.getData().list);
                            for (int i = 0; i < unusualOrdersBeanBaseEntity.getData().list.size(); i++) {
                                orderMap.put(unusualOrdersBeanBaseEntity.getData().list.get(i).order_no, unusualOrdersBeanBaseEntity.getData().list.get(i));
                            }
                            orderAdapter.notifyDataSetChanged();
//                            scrollTo(orderListView.getFirstVisiblePosition() - previousPos <= 0 ? 0 : orderListView.getFirstVisiblePosition() - previousPos);
                            scrollTo(orderListView.getCount() - 1);
                        } else {
                            showLoading(unusualOrdersBeanBaseEntity.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        closeLoading();
                    }

                    @Override
                    public void onComplete() {
                        closeLoading();
                    }
                });
    }

    public void invalidOrders(final UnusualOrdersBean.Order order) {
        RetrofitFactory.getInstance().API()
                .invalidOrders(AccountManager.getInstance().getAdminToken(), MacManager.getInstace(this).getMac(), order.order_no)
                .compose(this.<BaseEntity>setThread())
                .subscribe(new Observer<BaseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        showLoading(baseEntity.getMsg());
                        order.status = "已作废";
                        orderMap.put(order.order_no, order);
//                        orderList.remove(order);
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        closeLoading();
                    }

                    @Override
                    public void onComplete() {
                        closeLoading();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_invalid_previous_btn:
                scrollTo(orderListView.getFirstVisiblePosition() - previousPos <= 0 ? 0 : orderListView.getFirstVisiblePosition() - previousPos);
                break;
            case R.id.order_invalid_next_btn:
                getOrders(++currentPage + "", previousPos + "", "1");
                break;
            case R.id.order_invalid_back_btn:
                finish();
                break;
        }
    }

    public void scrollTo(final int position) {
        orderListView.post(new Runnable() {
            @Override
            public void run() {
                orderListView.smoothScrollToPosition(position);
            }
        });
    }

    private void showTwoButtonDialog(String alertText, String confirmText, String cancelText, View.OnClickListener conFirmListener, View.OnClickListener cancelListener) {
        mDialog = builder.setMessage(alertText)
                .setPositiveButton(confirmText, conFirmListener)
                .setNegativeButton(cancelText, cancelListener)
                .createTwoButtonDialog();
        mDialog.show();
    }

    class OrderAdapter extends BaseAdapter {

        private Context context;
        private Map<String, UnusualOrdersBean.Order> orderMap;
        private List<UnusualOrdersBean.Order> orderList;

        public OrderAdapter(Context context, Map<String, UnusualOrdersBean.Order> orderMap, List<UnusualOrdersBean.Order> orderList) {
            this.context = context;
            this.orderMap = orderMap;
            this.orderList = orderList;
        }

        @Override
        public int getCount() {
            return orderMap.size();
        }

        @Override
        public Object getItem(int position) {
            return orderMap.get(orderList.get(position).order_no);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.order_item, null);
                holder = new ViewHolder();
                holder.orderIdTv = convertView.findViewById(R.id.order_item_id_tv);
                holder.orderNumberTv = convertView.findViewById(R.id.order_item_number_tv);
                holder.dealTimeTv = convertView.findViewById(R.id.order_item_dealTime_tv);
                holder.sellerTv = convertView.findViewById(R.id.order_item_seller_tv);
                holder.buyerTv = convertView.findViewById(R.id.order_item_buyer_tv);
                holder.weightTv = convertView.findViewById(R.id.order_item_weight_tv);
                holder.incomeTv = convertView.findViewById(R.id.order_item_income_tv);
                holder.billingTv = convertView.findViewById(R.id.order_item_billing_tv);
                holder.orderStatusTv = convertView.findViewById(R.id.order_item_status_tv);
                holder.invalidBtn = convertView.findViewById(R.id.order_item_invalid_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final UnusualOrdersBean.Order order = orderMap.get(orderList.get(position).order_no);

            holder.orderIdTv.setText(order.id);
            holder.orderNumberTv.setText(order.order_no);
            holder.dealTimeTv.setText(order.create_time);
            holder.sellerTv.setText(order.client_name);
            holder.buyerTv.setText(order.buyer_name);
            holder.weightTv.setText(order.total_amount);
            holder.incomeTv.setText(order.total_weight);
            holder.billingTv.setText(order.payment_type);
            holder.orderStatusTv.setText(order.status);
            if(TextUtils.equals(order.status, "已作废")){
                holder.invalidBtn.setEnabled(false);
                holder.invalidBtn.setTextColor(ContextCompat.getColor(context, R.color.gray_e3));
            }else {
                holder.invalidBtn.setEnabled(true);
                holder.invalidBtn.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            holder.invalidBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTwoButtonDialog(getString(R.string.string_confirm_invalid_txt),
                            getString(R.string.string_confirm),
                            getString(R.string.string_cancel_txt),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                    invalidOrders(order);
                                }
                            },
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();

                                }
                            });

                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView orderIdTv;
            TextView orderNumberTv;
            TextView dealTimeTv;
            TextView sellerTv;
            TextView buyerTv;
            TextView weightTv;
            TextView incomeTv;
            TextView billingTv;
            TextView orderStatusTv;
            Button invalidBtn;
        }
    }
}
