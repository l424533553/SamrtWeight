package com.axecom.smartweight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.entity.fpms_chinaap.UpdateTempBean;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.j256.ormlite.misc.BaseDaoEnabled;

import java.util.List;


/**
 * 说明：详细订单的 适配器 ,双层折叠式
 * 作者：User_luo on 2018/4/23 16:29
 * 邮箱：424533553@qq.com
 */
public class UpDataAdapter extends BaseExpandableListAdapter {

    private IMyItemOnclick myItemOnclick;

    public void setMyItemOnclick(IMyItemOnclick myItemOnclick) {
        this.myItemOnclick = myItemOnclick;
    }

    public interface IMyItemOnclick {
        void myItemGroupClick(int groupPosition, OrderInfo orderInfo);
    }

    private final Context context;
    private final List<UpdateTempBean> data;

    public UpDataAdapter(Context context, List<UpdateTempBean> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        UpdateTempBean bean = data.get(groupPosition);
        return bean == null ? 0 : bean.getOrderNames().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {

        return data.get(groupPosition).getOrderNames().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = View.inflate(context, R.layout.item_updata_temp_fragment_group, null);
            holder.tvOrderNo = convertView.findViewById(R.id.tvOrderNo);
            holder.ivUploadStatus = convertView.findViewById(R.id.ivUploadStatus);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        UpdateTempBean bean = data.get(groupPosition);
        holder.tvOrderNo.setText(bean.getOrderNo());

//        holder.btnPrinter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (orderInfo != null) {
//                    myItemOnclick.myItemGroupClick(groupPosition, orderInfo);
//                }
//            }
//        });

        if (bean.getState() == 0) {
            holder.ivUploadStatus.setImageResource(R.mipmap.no_upload);
        } else if (bean.getState() == 1) {
            holder.ivUploadStatus.setImageResource(R.mipmap.upload);
        }
        return convertView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = View.inflate(context, R.layout.item_child_details, null);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            holder.tvWeight = convertView.findViewById(R.id.tvWeight);
            holder.tvMoney = convertView.findViewById(R.id.tvMoney);
            holder.tvUnit = convertView.findViewById(R.id.tvUnit);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
//            convertView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    myItemOnLongclick.myItemChildLongClick(groupPosition, childPosition, isLastChild);
//                    return false;
//                }
//            });
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    myItemOnclick.myItemChildClick(groupPosition, childPosition, isLastChild);
//                }
//            });

        String item = getChild(groupPosition, childPosition);

        holder.tvName.setText(item);
//        holder.tvPrice.setText(item.getPrice() + "");
//        holder.tvWeight.setText(item.getWeight() + "");
//        holder.tvMoney.setText(item.getMoney());
//        holder.tvUnit.setText(item.getUnit() + "");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder {
        private TextView tvOrderNo;
        private ImageView ivUploadStatus;
    }

    /**
     * 子item
     */
    private class ChildHolder {
        TextView tvName;
        TextView tvPrice;
        TextView tvWeight;
        TextView tvMoney;
        TextView tvUnit;
    }
}
