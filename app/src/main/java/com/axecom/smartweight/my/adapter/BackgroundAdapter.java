package com.axecom.smartweight.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.axecom.smartweight.R;

import java.util.List;

/**
 * author: luofaxin
 * date： 2018/9/25 0025.
 * email:424533553@qq.com
 * describe:
 */
public class BackgroundAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> list;


    public BackgroundAdapter(Context context) {
        this.context = context;
        list = BackgroundData.getData();
    }

//    public BackgroundAdapter(Context context, boolean isSecondLayout) {
//        this.context = context;
//        boolean isSecondLayout1 = isSecondLayout;
//    }

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

//    public int getPosition(int position) {
//        return list.get(position);
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_background, null);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setImageResource(list.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView image;

    }
}
