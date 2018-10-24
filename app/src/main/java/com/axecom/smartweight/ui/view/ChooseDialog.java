package com.axecom.smartweight.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.axecom.smartweight.R;

import java.util.List;
import java.util.Map;

public class ChooseDialog extends Dialog {
    public ChooseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public ChooseDialog(@NonNull Context context) {
        super(context);
    }


    public interface OnSelectedListener {
        void onSelected(AdapterView<?> parent, View view, int position, long id);
    }

    public static class Builder implements AdapterView.OnItemClickListener {
        private ChooseDialog chooseDialog;
        private View view;
        private Context context;
        private ListView chooseListView;
        private OnSelectedListener onSelectedListener;
        private ChooseAdapter chooseAdapter;


        public Builder(Context context) {
            this.context = context;
            chooseDialog = new ChooseDialog(context, R.style.dialog);
            view = LayoutInflater.from(context).inflate(R.layout.choose_dialog_layout, null);
            chooseDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            chooseListView = view.findViewById(R.id.choose_dialog_listview);
        }

        public ChooseDialog create(List<Map<String, String>> list, int checkedPos, OnSelectedListener onSelectedListener) {
            this.onSelectedListener = onSelectedListener;
            chooseDialog.setContentView(view);
            chooseDialog.setCancelable(true);
            chooseDialog.setCanceledOnTouchOutside(true);
            chooseAdapter = new ChooseAdapter(list);
            chooseListView.setAdapter(chooseAdapter);
            chooseAdapter.setCheckedAtPosition(checkedPos);
            chooseListView.setOnItemClickListener(this);
            return chooseDialog;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            chooseAdapter.setCheckedAtPosition(position);
            onSelectedListener.onSelected(parent, view, position, id);
            chooseDialog.dismiss();
        }


        class ChooseAdapter extends BaseAdapter {
            private List<Map<String, String>> list;
            private int pos;

            public ChooseAdapter(List<Map<String, String>> list) {
                this.list = list;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            public void setCheckedAtPosition(int position) {
                this.pos = position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.choose_item, null);
                    holder = new ViewHolder();
                    holder.chooseCtv = convertView.findViewById(R.id.choose_dialog_choose_ctv);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Map<String, String> map = list.get(position);
                for (String key : map.keySet()) {
                    String value = map.get(key);
                    holder.chooseCtv.setText(value);
                }
                if (pos == position) {
//                    holder.chooseCtv.setChecked(true);
                } else {
//                    holder.chooseCtv.setChecked(false);
                }

                return convertView;
            }
        }

        class ViewHolder {
            CheckedTextView chooseCtv;
        }
    }
}
