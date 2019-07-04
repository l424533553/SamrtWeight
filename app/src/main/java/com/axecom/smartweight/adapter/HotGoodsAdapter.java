package com.axecom.smartweight.adapter;

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
import com.axecom.smartweight.entity.project.HotGood;
import com.axecom.smartweight.listener.MyOnItemClickListener2;

import java.util.List;

/**
 * author: luofaxin
 * date： 2018/10/22 0022.
 * email:424533553@qq.com
 * describe:
 */
public class HotGoodsAdapter extends RecyclerView.Adapter<HotGoodsAdapter.DragViewHolder> {
    private List<HotGood> hotGoodList;
    private final LayoutInflater mInflater;
    private boolean isDelete;

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public HotGoodsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<HotGood> hotGoodList) {
        this.hotGoodList = hotGoodList;
    }

    public HotGood getItem(int position) {
        if (hotGoodList != null) {
            return hotGoodList.get(position);
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
        if (hotGoodList != null) {
            hotGoodList.remove(position);//删除数据源,移除集合中当前下标的数据
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
        if (hotGoodList.get(position).getBatchCode() != null) {
            holder.selectedTv.setVisibility(View.VISIBLE);
        }
        holder.nameTv.setText(hotGoodList.get(position).getName());
        holder.tvPosition.setText(String.valueOf(position + 1));
        if (isDelete) {
            holder.deleteTv.setVisibility(View.VISIBLE);
        } else {
            holder.deleteTv.setVisibility(View.GONE);
        }

        holder.deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnItemClickListener.myOnItemClick(position, 1);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isDelete = !isDelete;
//                holder.deleteTv.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
                return false;
            }
        });
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return hotGoodList == null ? 0 : hotGoodList.size();
    }


    public void showDeleteTv(boolean show) {

        notifyDataSetChanged();
    }

    private void removeItemByPosition(int position) {
        hotGoodList.remove(position);
        notifyDataSetChanged();
    }


    public class DragViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTv;
        private final TextView tvPosition;
        private final ImageView deleteTv;
        private final ImageView selectedTv;

        private DragViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.commodity_item_2_name_tv);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            deleteTv = itemView.findViewById(R.id.commodity_item_2_delete_tv);
            selectedTv = itemView.findViewById(R.id.commodity_item_2_selected_iv);
        }
    }
}
