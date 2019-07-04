package com.axecom.smartweight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.entity.secondpresent.AdImageInfo;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/1/2 0002    11:17
 * 邮件：424533553@qq.com
 * 说明：
 */
public class ImageTestAdapter extends BaseAdapter {
    private final Context context;
    private List<AdImageInfo> data;

    public void setData(List<AdImageInfo> data) {
        this.data = data;
        if (data != null) {
            notifyDataSetChanged();
        }
    }

    public ImageTestAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image_test, null);
            holder = new ViewHolder();


            holder.tvType = convertView.findViewById(R.id.tvType);
            holder.tvNetPath = convertView.findViewById(R.id.tvNetPath);
            holder.tvLocalPath = convertView.findViewById(R.id.tvLocalPath);

            holder.imageView = convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AdImageInfo adImageInfo = data.get(position);
//            //Glide 加载图片简单用法
        Glide.with(context).load(adImageInfo.getLocalPath()).into(holder.imageView);
        holder.tvLocalPath.setText(adImageInfo.getLocalPath());
        holder.tvNetPath.setText(adImageInfo.getNetPath());
        if (adImageInfo.getType() == 0) {
            holder.tvType.setText("头像图片");
        } else {
            holder.tvType.setText("广告图片");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvType, tvNetPath, tvLocalPath;
        ImageView imageView;
    }
}
