package com.axecom.smartweight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.entity.system.SettingsBean;
import com.axecom.smartweight.service.RoundedCornerImageView;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2018/12/18 0018    8:59
 * 邮件：424533553@qq.com
 * 说明：系统设置 的适配器
 */
public class SettingsAdapter extends BaseAdapter {


    private final Context context;
    private final List<SettingsBean> settingList;

    public SettingsAdapter(Context context, List<SettingsBean> settingList) {
        this.context = context;
        this.settingList = settingList;
    }

    @Override
    public int getCount() {
        return settingList.size();
    }

    @Override
    public SettingsBean getItem(int position) {
        return settingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.settings_item, null);
            holder = new ViewHolder();
//                holder.iconIv = convertView.findViewById(R.id.roundedCornerImageView);
//                holder.lladapter = convertView.findViewById(R.id.lladapter);
            holder.roundedCornerImageView = convertView.findViewById(R.id.roundedCornerImageView);
            holder.titleTv = convertView.findViewById(R.id.settings_item_title_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SettingsBean item = settingList.get(position);
        holder.roundedCornerImageView.setImageResource(item.getIcon());
        holder.titleTv.setText(item.getTitle());

//            holder.lladapter.setBackgroundColor(context.getResources().getColor(R.color.main_cyan_color));
        holder.roundedCornerImageView.setBackgroundColor(context.getResources().getColor(item.getColor()));

        return convertView;
    }

    private class ViewHolder {
        // ImageView iconIv;
        // LinearLayout lladapter;
        private RoundedCornerImageView roundedCornerImageView;
        private TextView titleTv;
    }


}