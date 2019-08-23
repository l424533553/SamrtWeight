package com.xuanyuan.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.xuanyuan.library.R;

/**
 * 弹出的  密码软键盘
 * 输入秘密 ，进行响应处理
 */
public class SoftKey extends GridView {
    private static final String[] DATA_DIGITAL = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "删除", "0", "."};
    private final Context context;

    public SoftKey(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SoftKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public SoftKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void initView() {
        MyAdapter mAdapter = new MyAdapter(context, DATA_DIGITAL);
        setAdapter(mAdapter);
    }

    class MyAdapter extends BaseAdapter {
        private final Context context;
        private final String[] digitals;

        public MyAdapter(Context context, String[] digitals) {
            this.context = context;
            this.digitals = digitals;
        }

        @Override
        public int getCount() {
            return digitals.length;
        }

        @Override
        public Object getItem(int position) {
            return digitals[position];
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
                convertView = LayoutInflater.from(context).inflate(R.layout.soft_keyborad_item, null);
                holder = new ViewHolder();
                holder.keyBtn = convertView.findViewById(R.id.keyboard_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.keyBtn.setText(digitals[position]);
            return convertView;
        }
    }

    class ViewHolder {
        Button keyBtn;
    }
}
