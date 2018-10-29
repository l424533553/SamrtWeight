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
 * Created by user on 2015/12/15.
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {
    private Context context;
    private int resId;
    public List<T> list;

    public AbstractAdapter(Context context, int resId) {
        this.context = context;
        this.resId = resId;
        this.list = new ArrayList<>();
    }


    public AbstractAdapter(Context context, List<T> list) {
        this.context = context;
        if(list==null){
            list = new ArrayList<>();
        }
        this.list = list;
    }
    public AbstractAdapter(Context context) {
        this.context = context;
        if(list==null){
            list = new ArrayList<>();
        }
    }

//    public AbstractAdapter(Context context, int resId, List<T> list) {
//        this.context = context;
//        this.resId = resId;
//        if(list==null){
//           list = new ArrayList<>();
//        }
//        this.list = list;
//    }

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
    public void setDataList(List<T> list) {
        this.list.clear();
        addDataList(list);
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

    /**
     * 添加 多个 数据
     * 原有 数据 不会被清除
     *
     * @param list
     */
    public void addDataList(List<T> list) {
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

    public List<T> getList() {
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
        return list==null ? 0:list.size();
    }

    @Override
    public T getItem(int position) {
        return  list==null ? null:list.get(position);
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

        bindDatas(viewHolder, position);
        return convertView;
    }

    public abstract void bindDatas(ViewHolder convertView, int position);

    public class ViewHolder {
        private HashMap<Integer, View> map;
        private View viewGroup;

        public ViewHolder(View viewGroup) {
            this.viewGroup = viewGroup;
            this.map = new HashMap<>();
        }

        public View findViewById(int viewId) {
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
