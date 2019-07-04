package com.axecom.smartweight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.entity.project.OrderBean;

import java.util.List;

/**
 * author: luofaxin
 * date： 2018/9/25 0025.
 * email:424533553@qq.com
 * describe:
 */
public class OrderAdapter extends BaseAdapter {
    private final Context context;
    private List<OrderBean> list;
    private boolean isSecondLayout;

    public OrderAdapter(Context context, List<OrderBean> list) {
        this.context = context;
        this.list = list;
    }

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public OrderAdapter(Context context, boolean isSecondLayout) {
        this.context = context;
        this.isSecondLayout = isSecondLayout;
    }

    public void setDatas(List<OrderBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            if (isSecondLayout) {
                //副屏显示
                convertView = LayoutInflater.from(context).inflate(R.layout.commodity_second_item, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.commodity_item, null);
            }

            holder = new ViewHolder();
            holder.tvIndex = convertView.findViewById(R.id.tvIndex);
            holder.nameTv = convertView.findViewById(R.id.commodity_name_tv);
            holder.priceTv = convertView.findViewById(R.id.commodity_price_tv);
//                holder.countTv = convertView.findViewById(R.id.commodity_count_tv);
            holder.weightTv = convertView.findViewById(R.id.commodity_weight_tv);
            holder.subtotalTv = convertView.findViewById(R.id.commodity_subtotal_tv);
//            holder.deleteBtn = convertView.findViewById(R.id.commodity_delete_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderBean orderBean = list.get(position);
        holder.nameTv.setText(orderBean.getName());
        holder.tvIndex.setText(String.valueOf(getCount() - position));

//        if ((orderBean.getWeight()).indexOf('.') == -1 || orderBean.getWeight().length() - (orderBean.getWeight().indexOf(".") + 1) <= 1) {
//            holder.weightTv.setText(Float.parseFloat(orderBean.getWeight()) / 1000 + "");
////                holder.weightTv.setText(context.getResources().getString(R.string.string_weight_unit_kg, Float.parseFloat(orderBean.weight) / 1000 + ""));
//        } else {
//            holder.weightTv.setText(orderBean.getWeight());
////                holder.weightTv.setText(context.getResources().getString(R.string.string_weight_unit_kg, orderBean.weight));
//        }

        holder.weightTv.setText(orderBean.getWeight());
        holder.priceTv.setText(orderBean.getPrice());
        holder.subtotalTv.setText(orderBean.getMoney());
        return convertView;
    }

    class ViewHolder {
        TextView nameTv;
        TextView priceTv;
        TextView weightTv;
        TextView subtotalTv;
        TextView tvIndex;
//        Button deleteBtn;

    }
}
