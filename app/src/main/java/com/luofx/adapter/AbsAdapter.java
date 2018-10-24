package com.luofx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义 Adapter 抽象 基类
 * Created by luofaxin on 2018/2/15.
 */
public abstract class AbsAdapter<T> extends BaseAdapter {
    private Context context;
    protected ArrayList<T> list;
    private int resId;

    public AbsAdapter(Context context, int resId) {
        this.context = context;
        this.resId = resId;
        this.list = new ArrayList<>();
    }

    /**
     * 设置 单个数据
     * 原有数据 将被清除
     *
     * @param t
     */
    public void setData(T t) {
        this.list.clear();
        addData(t);
    }

    /**
     * 设置 多个 数据
     * 原有数据 将被清除
     *
     * @param list
     */
    public void setDatas(List<T> list) {
        this.list.clear();
        addDatas(list);
    }

    /**
     * 添加 单个 数据
     * 原有数据不会 被清除
     *
     * @param t
     */
    public void addData(T t) {
        if (t == null) {
            return;
        }
        if (this.list.contains(t)) {
            return;
        }
        this.list.add(t);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * 添加 多个 数据
     * 原有 数据 不会被清除
     *
     * @param list
     */
    public void addDatas(List<T> list) {
        if (list == null) {
            return;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        if (position < list.size()) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resId, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bindDatas(viewHolder, list.get(position), position);
        return convertView;
    }

    public abstract void bindDatas(ViewHolder viewHolder, T t, int position);

    public class ViewHolder {
        private HashMap<Integer, View> map;
        private View viewGroup;

        public ViewHolder(View viewGroup) {
            this.viewGroup = viewGroup;
            this.map = new HashMap<>();
        }

        public View getView(int viewId) {
            if (map.containsKey(viewId)) {
                return map.get(viewId);
            } else {
                View viewFind = this.viewGroup.findViewById(viewId);
                map.put(viewId, viewFind);
                return viewFind;
            }
        }

        public View getViewGroup() {
            return viewGroup;
        }

        public void setViewGroup(View viewGroup) {
            this.viewGroup = viewGroup;
        }
    }
}
