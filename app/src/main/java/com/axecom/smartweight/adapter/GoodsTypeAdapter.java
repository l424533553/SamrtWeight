package com.axecom.smartweight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.entity.project.GoodsType;
import com.axecom.smartweight.listener.MyOnItemClickListener2;

import java.util.ArrayList;
import java.util.List;

/**
 * author: luofaxin
 * date： 2018/10/22 0022.
 * email:424533553@qq.com
 * describe:
 */
public class GoodsTypeAdapter extends RecyclerView.Adapter<GoodsTypeAdapter.ViewHolder> {
    private final List<GoodsType> goodsList;
    private final LayoutInflater mInflater;
    private int selected;

    public void setSelected(int selected) {
        this.selected = selected;
    }

    private final Context context;

    public GoodsTypeAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        goodsList = new ArrayList<>();
        this.context = context;
    }

    public void setDatas(List<GoodsType> goodsList) {
        this.goodsList.addAll(goodsList);
    }

    public GoodsType getItem(int position) {
        return goodsList.get(position);
    }

    @NonNull
    @Override
    public GoodsTypeAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_goods_type, parent, false);
        return new ViewHolder(view);
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

        holder.tvGoodsName.setText(goodsList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnItemClickListener.myOnItemClick(position, 2);
            }
        });
        if (selected == position) {
            holder.tvGoodsName.setBackgroundResource(R.color.white);
            holder.tvGoodsName.setTextColor(context.getResources().getColor(R.color.main_blue_color));
        } else {
            holder.tvGoodsName.setBackgroundResource(R.color.main_blue_color);
            holder.tvGoodsName.setTextColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return goodsList == null ? 0 : goodsList.size();
    }


    public void showDeleteTv(boolean show) {

        notifyDataSetChanged();
    }

    private void removeItemByPosition(int position) {
        goodsList.remove(position);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvGoodsName;
//        private ImageView deleteTv;
//        private ImageView selectedTv;

        private ViewHolder(View itemView) {
            super(itemView);
            tvGoodsName = itemView.findViewById(R.id.tvGoodsName);
//            deleteTv = itemView.findViewById(R.id.commodity_item_2_delete_tv);
//            selectedTv = itemView.findViewById(R.id.commodity_item_2_selected_iv);
        }


    }
}
