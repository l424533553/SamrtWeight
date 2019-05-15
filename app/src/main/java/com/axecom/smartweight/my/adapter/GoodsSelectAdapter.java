package com.axecom.smartweight.my.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.entity.AllGoods;
import com.luofx.listener.MyOnItemClickListener2;

import java.util.ArrayList;
import java.util.List;

/**
 * author: luofaxin
 * date： 2018/10/22 0022.
 * email:424533553@qq.com
 * describe:
 */
public class GoodsSelectAdapter extends RecyclerView.Adapter<GoodsSelectAdapter.DragViewHolder> {
    private final List<AllGoods> goodsList;
    private final LayoutInflater mInflater;


    public GoodsSelectAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        goodsList = new ArrayList<>();
    }

    public void setDatas(List<AllGoods> goodsList) {
        this.goodsList.addAll(goodsList);
    }

    public AllGoods getItem(int position) {
        if (goodsList != null) {
            return goodsList.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public DragViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_goods, parent, false);
        return new DragViewHolder(view);
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
    public void onBindViewHolder(@NonNull final DragViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (goodsList.get(position).getBatchCode() != null) {
            holder.selectedTv.setVisibility(View.VISIBLE);
        }
        holder.nameTv.setText(goodsList.get(position).getName());
        holder.deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnItemClickListener.myOnItemClick(position, 1);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.deleteTv.setVisibility(View.VISIBLE);
                return false;
            }
        });
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return goodsList == null ? 0 : goodsList.size();
    }


    private void removeItemByPosition(int position) {
        goodsList.remove(position);
        notifyDataSetChanged();
    }


    public class DragViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTv;
        private final ImageView deleteTv;
        private final ImageView selectedTv;

        private DragViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.commodity_item_2_name_tv);
            deleteTv = itemView.findViewById(R.id.commodity_item_2_delete_tv);
            selectedTv = itemView.findViewById(R.id.commodity_item_2_selected_iv);
        }
    }
}
