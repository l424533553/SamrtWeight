package com.axecom.smartweight.adapter;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.axecom.smartweight.R;
import com.axecom.smartweight.databinding.ItemGoodsTradeBinding;
import com.axecom.smartweight.listener.MyOnItemClickListener2;
import com.axecom.smartweight.mvvm.model.GoodsTradeBean;

import java.util.List;

/**
 * author: luofaxin
 * date： 2018/10/22 0022.
 * email:424533553@qq.com
 * describe:  数据汇总中，商品交易适配器
 */
public class GoodsTradeAdapter extends RecyclerView.Adapter<GoodsTradeAdapter.ViewHolder> {
    private List<GoodsTradeBean> goodsList;

    public GoodsTradeAdapter(List<GoodsTradeBean> data) {
        goodsList = data;
    }

    public void setDatas(List<GoodsTradeBean> goodsList) {
        this.goodsList.addAll(goodsList);
    }

    public GoodsTradeBean getItem(int position) {
        return goodsList.get(position);
    }

    @NonNull
    @Override
    public GoodsTradeAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        ItemGoodsTradeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_goods_trade, parent, false);
        return new ViewHolder(binding);
    }


    public void removeList(int position) {
        if (goodsList != null) {
            goodsList.remove(position);//删除数据源,移除集合中当前下标的数据
            notifyItemRemoved(position);//刷新被删除的地方
            notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
        }
    }

    private MyOnItemClickListener2 myOnItemClickListener;

    public void setMyOnItemClickListener(MyOnItemClickListener2 myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.getBinding().setGoodTrade(goodsList.get(position));
        holder.itemView.setOnClickListener(v -> myOnItemClickListener.myOnItemClick(position, 2));
    }

    @Override
    public int getItemCount() {
        return goodsList == null ? 0 : goodsList.size();
    }

    private void removeItemByPosition(int position) {
        goodsList.remove(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGoodsTradeBinding binding;

        private ViewHolder(ItemGoodsTradeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        ItemGoodsTradeBinding getBinding() {
            return binding;
        }
    }

    // 数据库
    private void test(){

    }

    //使用 功能 开发 设计
    private void test(String str){

    }

}
