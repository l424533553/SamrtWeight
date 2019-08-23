package com.xuanyuan.library.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.xuanyuan.library.R;


/**
 * Created by Administrator on 2018-5-23.
 */

/**
 *  软件盘弹出框 ，监听 输入
 */
public class SoftKeyDialog extends Dialog {


    protected SoftKeyDialog(@NonNull Context context) {
        super(context);
    }

    protected SoftKeyDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected SoftKeyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public interface OnConfirmedListener{
        void onConfirmed(String result);
    }

    public static class Builder implements View.OnClickListener{
        private static final String[] DATA_DIGITAL = {"1","2","3","4","5","6","7","8","9","删除","0","."};

        private View contentView;
        private final SoftKeyDialog softKeyDialog;
        private final View view;
        private final TextView inputTv;
        private final GridView keyboardGV;
        private final KeyBoardAdapter keyAdapter;
        private final Button confirmBtn;
        private final Button cancelBtn;
        private OnConfirmedListener onConfrimedListener;

        @SuppressLint("InflateParams")
        public Builder(Context context){
            softKeyDialog = new SoftKeyDialog(context, R.style.dialog);
            view = LayoutInflater.from(context).inflate(R.layout.soft_keyboard_layout, null);
            softKeyDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            confirmBtn = view.findViewById(R.id.soft_keyboard_confirm_btn);
            cancelBtn = view.findViewById(R.id.soft_keyboard_cancel_btn);
            inputTv = view.findViewById(R.id.soft_keybroad_input_tv);
            keyboardGV = view.findViewById(R.id.soft_keyboard_gridview);
            keyAdapter = new KeyBoardAdapter(context, DATA_DIGITAL);
            keyboardGV.setAdapter(keyAdapter);

            confirmBtn.setOnClickListener(this);
            cancelBtn.setOnClickListener(this);
        }

        public SoftKeyDialog create(OnConfirmedListener onConfrimedListener){
            this.onConfrimedListener = onConfrimedListener;
            softKeyDialog.setContentView(view);
            softKeyDialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            softKeyDialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示

            keyboardGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 9){
                        if(!TextUtils.isEmpty(inputTv.getText()))
                        inputTv.setText(inputTv.getText().subSequence(0, inputTv.getText().length() - 1));
                    }else {
                        inputTv.setText(inputTv.getText() + DATA_DIGITAL[position]);
                    }
                }
            });
            return softKeyDialog;
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.soft_keyboard_confirm_btn) {
                if (onConfrimedListener != null) {
                    onConfrimedListener.onConfirmed(inputTv.getText().toString());
                    softKeyDialog.dismiss();
                }
            } else if (i == R.id.soft_keyboard_cancel_btn) {
                softKeyDialog.dismiss();
            }
        }

        class KeyBoardAdapter extends BaseAdapter{
            private final Context context;
            private final String[] digitals;

            public KeyBoardAdapter(Context context, String[] digitals){
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
                if(convertView == null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.soft_keyborad_item, null);
                    holder = new ViewHolder();
                    holder.keyBtn = convertView.findViewById(R.id.keyboard_btn);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.keyBtn.setText(digitals[position]);
                return convertView;
            }
        }

        class ViewHolder{
            Button keyBtn;
        }
    }
}
