package com.luofx.listener;

/**
 * Created by User_luo on 2018/3/29.
 */

public interface IMyItemOnLongclick {
    void myItemChildLongClick(int groupPosition, int childPosition, boolean isLastChild);
    void myItemGroupLongClick(int groupPosition, boolean isExpanded);

}
