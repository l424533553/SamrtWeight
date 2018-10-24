//package com.axecom.iweight.ui.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//
//import com.axecom.iweight.R;
//
//import java.util.List;
//
///**
// * Created by Administrator on 2018-5-16.
// */
//
//public class DigitalAdapter extends BaseAdapter{
//
//    private Context context;
//    private List<String> list;
//
//    public DigitalAdapter(Context context, List<String> list){
//        this.context = context;
//        this.list = list;
//    }
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if(convertView == null){
//            convertView = LayoutInflater.from(context).inflate(R.layout.digital_item, null);
//            holder = new ViewHolder();
//            holder.digitalBtn = convertView.findViewById(R.id.main_digitial_btn);
//            convertView.setTag(holder);
//        }else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.digitalBtn.setText(list.get(position));
//        if(position == 9){
////            holder.digitalBtn.setTextSize(30);
//        }
////        convertView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////            }
////        });
//
//        return convertView;
//    }
//    class ViewHolder{
//        Button digitalBtn;
//    }
//}