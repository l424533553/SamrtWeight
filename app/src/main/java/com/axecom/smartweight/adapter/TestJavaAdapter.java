package com.axecom.smartweight.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.entity.system.SettingsBean;
import com.axecom.smartweight.service.RoundedCornerImageView;

import java.util.List;

/**
 * author: luofaxin
 * date： 2018/9/27 0027.
 * email:424533553@qq.com
 * describe:
 */
public class TestJavaAdapter extends RecyclerView.Adapter<TestJavaAdapter.ViewHolder> {

    private List<SettingsBean> data;
    private final Context context;

    public TestJavaAdapter(Context context) {
        this.context = context;
    }

    public SettingsBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<SettingsBean> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public TestJavaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingsBean item = data.get(position);

        if (position == 4) {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            holder.itemView.measure(w, h);

            int height = holder.itemView.getMeasuredHeight();
            int width = holder.itemView.getMeasuredWidth();


            int height1 = holder.roundedCornerImageView.getMeasuredHeight();
            int width1 = holder.roundedCornerImageView.getMeasuredWidth();

            ViewGroup.LayoutParams para1;
            para1 = holder.itemView.getLayoutParams();
            para1.height = height * 2;
            para1.width = width * 2;

            ViewGroup.LayoutParams para2;
            para2 = holder.roundedCornerImageView.getLayoutParams();
            para2.height = height * 2;
            para2.width = width * 2;

//            holder.titleTv.setVisibility(View.GONE);


            holder.itemView.setLayoutParams(para1);

            holder.roundedCornerImageView.setLayoutParams(para2);



            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_settings17));
        }


        holder.roundedCornerImageView.setImageResource(item.getIcon());
        holder.titleTv.setText(item.getTitle());
//            holder.lladapter.setBackgroundColor(context.getResources().getColor(R.color.main_cyan_color));
        holder.roundedCornerImageView.setBackgroundColor(context.getResources().getColor(item.getColor()));


    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RoundedCornerImageView roundedCornerImageView;
        private final TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);

            roundedCornerImageView = itemView.findViewById(R.id.roundedCornerImageView);
            titleTv = itemView.findViewById(R.id.settings_item_title_tv);

        }
    }
}
