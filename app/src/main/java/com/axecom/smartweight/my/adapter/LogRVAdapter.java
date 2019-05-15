package com.axecom.smartweight.my.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.entity.LogBean;

import java.util.List;

/**
 * author: luofaxin
 * date： 2018/9/27 0027.
 * email:424533553@qq.com
 * describe:
 */
public class LogRVAdapter extends RecyclerView.Adapter<LogRVAdapter.ViewHolder> {

    private List<LogBean> data;

    public void setData(List<LogBean> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public LogRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_logrv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvType.setText(data.get(position).getType());
        holder.tvMessage.setText(data.get(position).getMessage());
        holder.tvTime.setText(data.get(position).getTime());
        holder.tvLocation.setText(data.get(position).getLocation());
    }



    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvType;
        private final TextView tvMessage;
        private final TextView tvTime;
        private final TextView tvLocation;
        public ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}
