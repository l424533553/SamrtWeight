package com.axecom.smartweight.my.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;

import java.util.List;


/**
 * 说明：详细订单的 适配器 ,双层折叠式
 * 作者：User_luo on 2018/4/23 16:29
 * 邮箱：424533553@qq.com
 */
public class DetailsAdapter extends BaseExpandableListAdapter {

    private IMyItemOnclick myItemOnclick;

    public void setMyItemOnclick(IMyItemOnclick myItemOnclick) {
        this.myItemOnclick = myItemOnclick;
    }


    public interface IMyItemOnclick {
        void myItemGroupClick(int groupPosition, OrderInfo orderInfo);

    }


    private final Context context;
    private final List<OrderInfo> data;

    public DetailsAdapter(Context context, List<OrderInfo> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        OrderInfo sampleFragmentBean = data.get(groupPosition);
        return sampleFragmentBean == null ? 0 : sampleFragmentBean.getOrderBeans().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public OrderBean getChild(int groupPosition, int childPosition) {

        return data.get(groupPosition).getOrderItem().get(childPosition);
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
            convertView = View.inflate(context, R.layout.item_sample_fragment_group, null);
            holder.tvTotalAmount = convertView.findViewById(R.id.tvTotalAmount);
            holder.tvOrderNo = convertView.findViewById(R.id.tvOrderNo);
            holder.tvUploadStatus = convertView.findViewById(R.id.tvUploadStatus);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            holder.btnPrinter = convertView.findViewById(R.id.btnPrinter);
            holder.imPayWay = convertView.findViewById(R.id.imPayWay);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        final OrderInfo orderInfo = data.get(groupPosition);
        holder.tvOrderNo.setText(data.get(groupPosition).getBillcode());
        holder.tvTotalAmount.setText(data.get(groupPosition).getTotalamount());
        holder.tvTime.setText(data.get(groupPosition).getTime());
        int state = data.get(groupPosition).getState();

        holder.btnPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderInfo != null) {
                    myItemOnclick.myItemGroupClick(groupPosition, orderInfo);
                }
            }
        });

        if (orderInfo.getSettlemethod() == 0) {
            holder.imPayWay.setImageResource(R.mipmap.crash);
        }else {
            holder.imPayWay.setImageResource(R.mipmap.scan);
        }


        if (state == 0) {
//                holder.tvUploadStatus.setText("未上传");
            holder.tvUploadStatus.setImageResource(R.mipmap.no_upload);
        } else if (state == 1) {
//                holder.tvUploadStatus.setText("已上传");
//                holder.tvUploadStatus.setTextColor(context.getResources().getColor(R.color.green_008B00));
            holder.tvUploadStatus.setImageResource(R.mipmap.upload);
        }
//            convertView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    myItemOnLongclick.myItemGroupLongClick(groupPosition, isExpanded);
//                    return false;
//                }
//            });
//
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    myItemOnclick.myItemGroupClick(groupPosition, isExpanded);
//                }
//            });
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

        OrderBean item = getChild(groupPosition, childPosition);

        holder.tvName.setText(item.getName() + "");
        holder.tvPrice.setText(item.getPrice() + "");
        holder.tvWeight.setText(item.getWeight() + "");
        holder.tvMoney.setText(item.getMoney());
        holder.tvUnit.setText(item.getUnit() + "");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder {

        private TextView tvTotalAmount, tvOrderNo, tvTime;
        private ImageView tvUploadStatus;
        private ImageView imPayWay;
        private TextView btnPrinter; // 打印按钮

    }

    private class ChildHolder {
        TextView tvName;
        TextView tvPrice;
        TextView tvWeight;
        TextView tvMoney;
        TextView tvUnit;
    }

}
