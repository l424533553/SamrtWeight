package com.axecom.smartweight.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/8/3.
 */

public class MoneyTextWatcher implements TextWatcher {
    private final EditText editText;
    private int digits = 1;

    public   MoneyTextWatcher(EditText et) {
        editText = et;
    }

    public MoneyTextWatcher setDigits(int d) {
        digits = d;
        return this;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        String s1 = s.toString();
        if(editText.isSelected()){
            editText.setText("");
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(editText.isSelected()){
            editText.setText("");
        }

        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + digits + 1);
                editText.setText(s);
                editText.setSelection(s.length()); //光标移到最后
            }
        }
        if (s.toString().trim().equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }

        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(1, 2));
                editText.setSelection(1);
                return;
            }
        }
        if (s.toString().contains(".")) {
            if (editText.getText().toString().indexOf(".", editText.getText().toString().indexOf(".") + 1) > 0) {
                editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                editText.setSelection(editText.getText().toString().length());
            }
        }



    }

    @Override
    public void afterTextChanged(Editable s) {
        String temp = s.toString();
        int posDot = temp.indexOf(".");
        //小数点之前保留3位数字或者一千
        if (posDot <= 0){
            //temp
            if(temp.equals("1000")){
                return;
            }else{
                if(temp.length()<=3){
                    return;
                }else{
                    s.delete(3, 4);
                    return;
                }
            }
        }
        //保留三位小数
        if (temp.length() - posDot - 1 > 1)
        {
            s.delete(posDot + 2, posDot + 3);
        }

    }
}