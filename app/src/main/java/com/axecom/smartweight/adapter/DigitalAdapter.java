package com.axecom.smartweight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.axecom.smartweight.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-5-16.
 */

public class DigitalAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> list;

    public DigitalAdapter(Context context) {
        this.context = context;
        this.list = getData();
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("删除");
        list.add("0");
        list.add(".");
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.digital_item, null);
            holder = new ViewHolder();
            holder.digitalBtn = convertView.findViewById(R.id.main_digitial_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.digitalBtn.setText(list.get(position));

        return convertView;
    }

    /**
     *
     */
    class ViewHolder {
        Button digitalBtn;
    }
}