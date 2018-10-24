package com.luofx.adapter;//package com.coolshow.mybmobtest.luofx.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//
//import com.coolshow.mybmobtest.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 说明： 双层 扩展 列表
// * 作者：User_luo on 2018/5/24 08:55
// * 邮箱：424533553@qq.com
// */
//public class QueryExListViewAdapter extends BaseExpandableListAdapter {
//    private Context mContext = null;
//    private List<UpLoadInfo> mGroupList = null;
//    private List<List<SampleTestBean>> mItemList = null;
//
//    public QueryExListViewAdapter(Context context, List<UpLoadInfo> groupList) {
//        this.mContext = context;
//        mItemList = new ArrayList<>();
//        this.mGroupList = groupList;
//        initData();
////        this.mItemList = itemList;
//    }
//
//    public void setDatas(List<UpLoadInfo> groupList) {
//        this.mGroupList = groupList;
//        initData();
//    }
//
//    private void initData() {
//        mItemList.clear();
//        if (mGroupList != null) {
//            for (int i = 0; i < mGroupList.size(); i++) {
//                List<SampleTestBean> list = new ArrayList<>(mGroupList.get(i).getBeans());
//                mItemList.add(list);
//            }
//        }
//    }
//
//    /**
//     * 获取组的个数
//     *
//     * @return
//     * @see android.widget.ExpandableListAdapter#getGroupCount()
//     */
//    @Override
//    public int getGroupCount() {
//        return mGroupList == null ? 0 : mGroupList.size();
//    }
//
//    /**
//     * 获取指定组中的子元素个数
//     *
//     * @param groupPosition
//     * @return
//     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
//     */
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return mItemList.get(groupPosition).size();
//    }
//
//    /**
//     * 获取指定组中的数据
//     *
//     * @param groupPosition
//     * @return
//     * @see android.widget.ExpandableListAdapter#getGroup(int)
//     */
//    @Override
//    public UpLoadInfo getGroup(int groupPosition) {
//        return mGroupList.get(groupPosition);
//    }
//
//    /**
//     * 获取指定组中的指定子元素数据。
//     *
//     * @param groupPosition
//     * @param childPosition
//     * @return
//     * @see android.widget.ExpandableListAdapter#getChild(int, int)
//     */
//    @Override
//    public SampleTestBean getChild(int groupPosition, int childPosition) {
//
//        return mItemList.get(groupPosition).get(childPosition);
//    }
//
//    /**
//     * 获取指定组的ID，这个组ID必须是唯一的
//     *
//     * @param groupPosition
//     * @return
//     * @see android.widget.ExpandableListAdapter#getGroupId(int)
//     */
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    /**
//     * 获取指定组中的指定子元素ID
//     *
//     * @param groupPosition
//     * @param childPosition
//     * @return
//     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
//     */
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    /**
//     * 获取显示指定组的视图对象
//     *
//     * @param groupPosition 组位置
//     * @param isExpanded    该组是展开状态还是伸缩状态
//     * @param convertView   重用已有的视图对象
//     * @param parent        返回的视图对象始终依附于的视图组
//     * @return
//     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
//     * android.view.ViewGroup)
//     */
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
//                             ViewGroup parent) {
//        GroupHolder groupHolder = null;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_query_adapter_group, null);
//            groupHolder = new GroupHolder();
//            groupHolder.index = convertView.findViewById(R.id.index);
//            groupHolder.ChkDate = convertView.findViewById(R.id.ChkDate);
//            groupHolder.UserName = convertView.findViewById(R.id.UserName);
//            groupHolder.ChkOpInfo = convertView.findViewById(R.id.ChkOpInfo);
//            convertView.setTag(groupHolder);
//        } else {
//            groupHolder = (GroupHolder) convertView.getTag();
//        }
//
////        if (isExpanded) {
////            groupHolder.groupImg.setImageResource(R.drawable.group_open);
////        } else {
////            groupHolder.groupImg.setImageResource(R.drawable.group_close);
////        }
//        groupHolder.index.setText((groupPosition + 1) + "");
//        groupHolder.ChkDate.setText(mGroupList.get(groupPosition).getChkDate());
//        groupHolder.ChkOpInfo.setText(mGroupList.get(groupPosition).getChkOpInfo());
//        groupHolder.UserName.setText(mGroupList.get(groupPosition).getUserName());
//
//        return convertView;
//    }
//
//    /**
//     * 获取一个视图对象，显示指定组中的指定子元素数据。
//     *
//     * @param groupPosition 组位置
//     * @param childPosition 子元素位置
//     * @param isLastChild   子元素是否处于组中的最后一个
//     * @param convertView   重用已有的视图(View)对象
//     * @param parent        返回的视图(View)对象始终依附于的视图组
//     * @return
//     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
//     * android.view.ViewGroup)
//     */
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
//                             View convertView, ViewGroup parent) {
//        ItemHolder itemHolder = null;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_query_adapter_child, null);
//            itemHolder = new ItemHolder();
//
//            itemHolder.indexChild = convertView.findViewById(R.id.indexChild);
//            itemHolder.sampleNo = convertView.findViewById(R.id.sampleNo);
//            itemHolder.sampleName = convertView.findViewById(R.id.sampleName);
//            itemHolder.testUnit = convertView.findViewById(R.id.testUnit);
//            itemHolder.result = convertView.findViewById(R.id.result);
//            itemHolder.determine = convertView.findViewById(R.id.determine);
//            convertView.setTag(itemHolder);
//        } else {
//            itemHolder = (ItemHolder) convertView.getTag();
//        }
//        SampleTestBean userBean = mItemList.get(groupPosition).get(childPosition);
//
//        itemHolder.indexChild.setText((childPosition + 1) + "");
//        itemHolder.sampleNo.setText(userBean.getSampleNo());
//        itemHolder.sampleName.setText(userBean.getSampleName());
//        itemHolder.testUnit.setText(userBean.getTestUnit());
//        itemHolder.result.setText(userBean.getResult());
//
//        String determine = userBean.getDetermine();
//
//        if ("0".equals(determine)) {
//            itemHolder.determine.setText("合格");
//        } else if ("1".equals(determine)) {
//            itemHolder.determine.setText("不合格");
//        }
//
//        return convertView;
//    }
//
//    /**
//     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
//     *
//     * @return
//     * @see android.widget.ExpandableListAdapter#hasStableIds()
//     */
//    @Override
//    public boolean hasStableIds() {
//        return true;
//    }
//
//    /**
//     * 是否选中指定位置上的子元素。
//     *
//     * @param groupPosition
//     * @param childPosition
//     * @return
//     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
//     */
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return true;
//    }
//
//
//    class GroupHolder {
//        public TextView ChkDate;
//        public TextView ChkOpInfo;
//        public TextView UserName;
//        public TextView index;
//    }
//
//    class ItemHolder {
//
//        public TextView indexChild;
//        public TextView sampleNo;
//        public TextView sampleName;
//        public TextView testUnit;
//        public TextView result;
//        public TextView determine;
//    }
//}