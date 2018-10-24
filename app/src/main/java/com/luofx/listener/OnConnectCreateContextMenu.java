package com.luofx.listener;

import android.view.ContextMenu;
import android.view.View;

/**
 * Created by Administrator on 2017/5/23.
 */

public interface OnConnectCreateContextMenu {
    void onItemCreateContextMenu(ContextMenu menu, View view, int position);
}
