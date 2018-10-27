package com.axecom.smartweight.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.entity.Goods;

import java.util.List;

/**
 * 主界面 中的菜单  列表
 */
public class GoodMenuAdapter extends BaseAdapter {
    private Context context;
    //    private List<HotKeyBean> list;
    private List<Goods> list;
    private int pos = -1;

    //    public GoodMenuAdapter(Context context, List<HotKeyBean> list) {
//        this.context = context;
//        this.list = list;
//    }
    public GoodMenuAdapter(Context context) {
        this.context = context;
    }

    public List<Goods> getList() {
        return list;
    }

    public void setDatas(List<Goods> data) {
        list = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Goods getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setCheckedAtPosition(int position) {
        this.pos = position;
    }

    public void cleanCheckedPosition() {
        this.pos = -1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_grid_item, null);
            holder = new ViewHolder();
            holder.commodityBtn = convertView.findViewById(R.id.main_grid_item_btn);
            holder.tag = convertView.findViewById(R.id.img_tag);
            holder.tvIndex = convertView.findViewById(R.id.tvIndex);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Goods goods = list.get(position);
        holder.commodityBtn.setText(goods.getName());
        holder.tvIndex.setText(String.valueOf(position + 1));

        if (pos == position) {
            holder.commodityBtn.setBackground(context.getResources().getDrawable(R.drawable.shape_green_bg));
            holder.commodityBtn.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.commodityBtn.setBackground(context.getResources().getDrawable(R.drawable.shape_weight_display_bg));
            holder.commodityBtn.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.tag.setVisibility(TextUtils.isEmpty(goods.getBatchCode()) ? View.GONE : View.VISIBLE);


//        if(position%3==0){
//            holder.tvIndex.setVisibility(View.VISIBLE);
//            holder.tvIndex.setText(String.valueOf(position/3+1));
//        }

    /*    if(position%3==0){
            holder.tvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(String.valueOf(position/3+1));
        }else{
            holder.tvIndex.setVisibility(View.GONE);
        }
*/

        return convertView;
    }

    class ViewHolder {
        TextView commodityBtn, tvIndex;
        ImageView tag;
    }
}