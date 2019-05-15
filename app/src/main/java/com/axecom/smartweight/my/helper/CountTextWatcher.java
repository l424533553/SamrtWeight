package com.axecom.smartweight.my.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * author: luofaxin
 * date： 2018/10/24 0024.
 * email:424533553@qq.com
 * describe:
 */
public class CountTextWatcher implements TextWatcher {

    private final EditText countEt;

    public CountTextWatcher(EditText countEt) {
        this.countEt = countEt;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                countEt.setText(s.subSequence(1, 2));
                countEt.setSelection(1);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String temp = s.toString();
        int posDot = temp.indexOf(".");
        //小数点之前保留3位数字或者一千
        if (posDot <= 0) {
            //temp
            if (temp.equals("10000")) {
                return;
            } else {
                if (temp.length() <= 4) {
                    return;
                } else {
                    s.delete(4, 5);
                    return;
                }
            }
        }
        //保留三位小数
        if (temp.length() - posDot - 1 > 1) {
            s.delete(posDot + 2, posDot + 3);
        }
    }
}
