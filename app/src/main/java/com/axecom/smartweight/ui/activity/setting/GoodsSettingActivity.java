package com.axecom.smartweight.ui.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BusEvent;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.bean.SaveGoodsReqBean;
import com.axecom.smartweight.impl.ItemDragHelperCallback;
import com.axecom.smartweight.my.adapter.GoodsAdapter;
import com.axecom.smartweight.my.adapter.GoodsTypeAdapter;
import com.axecom.smartweight.my.entity.AllGoods;
import com.axecom.smartweight.my.entity.Goods;
import com.axecom.smartweight.my.entity.GoodsType;
import com.axecom.smartweight.my.entity.dao.AllGoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsTypeDao;
import com.luofx.listener.MyOnItemClickListener2;
import com.luofx.utils.common.MyToast;
import com.shangtongyin.tools.serialport.IConstants_ST;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoodsSettingActivity extends Activity implements View.OnClickListener, MyOnItemClickListener2, IConstants_ST {

    private RecyclerView rvGoods;
    private RecyclerView rvGoodsType;
    private GridView rvGoodsSelect;


    private GoodsAdapter goodsAdapter;
    //    private GoodsSelectAdapter goodsSelectAdapter;
    private GoodsTypeAdapter goodsTypeAdapter;


    private boolean isShowDelTv = false;
    protected SysApplication sysApplication;

    private GoodsDao goodsDao;
    private AllGoodsDao allGoodsDao;
    private GoodsTypeDao  goodsTypeDao;
    private Context context;

    //           ((TextView) classTitleLayout.getChildAt(i)).setTextColor(GoodsSettingActivity.this.getResources().getColor(R.color.black));
//
//        allTitleTv.setTextColor(GoodsSettingActivity.this.getResources().getColor(R.color.green_3CB371));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.activity_goods_setting);
        sysApplication = (SysApplication) getApplication();
        context = this;

        setInitView();
//        ActivityController.addActivity(this);

        initHander();

        goodsDao = new GoodsDao(context);
        goodsTypeDao = new GoodsTypeDao(context);
        allGoodsDao = new AllGoodsDao(context);
        initRecycle();

        getGoods();

    }

    private Handler handler;

    private void initHander() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case NOTIFY_INITDAT:
//                        classAdapter.notifyDataSetChanged();
//                        goodsTypeAdapter.notifyDataSetChanged();
//                        goodsAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }

    private ClassAdapter classAdapter;

    private void initRecycle() {

        /*  初始化商品热键表   ***********************************************/
        rvGoods = findViewById(R.id.rvGoods);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvGoods.setLayoutManager(manager);
        ItemDragHelperCallback callback = new ItemDragHelperCallback() {
            @Override
            public boolean isLongPressDragEnabled() {
                // 长按拖拽打开
                return true;
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rvGoods);
        goodsAdapter = new GoodsAdapter(context);
        goodsAdapter.setMyOnItemClickListener(this);
        rvGoods.setAdapter(goodsAdapter);

        /*  初始化  类别表  ************************************/
        rvGoodsType = findViewById(R.id.rvGoodsType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGoodsType.setLayoutManager(linearLayoutManager);

//        ItemTouchHelper helper = new ItemTouchHelper(callback);
//        helper.attachToRecyclerView(rvGoods);
        goodsTypeAdapter = new GoodsTypeAdapter(context);
        goodsTypeAdapter.setMyOnItemClickListener(this);
        rvGoodsType.setAdapter(goodsTypeAdapter);

        /*  商品选择栏 **********************/
        rvGoodsSelect = findViewById(R.id.rvGoodsSelect);
        classAdapter = new ClassAdapter(context);
        rvGoodsSelect.setAdapter(classAdapter);

        rvGoodsSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AllGoods allGoods = classAdapter.getItem(position);
                Goods goods = new Goods();
                goods.setCid(allGoods.getCid());
                goods.setPrice(allGoods.getPrice());
                goods.setBatchCode(allGoods.getBatchCode());
                goods.setName(allGoods.getName());

                goods.setType(allGoods.getType());
                goods.setTypeid(allGoods.getTypeid());
                if (!hotGoodsList.contains(goods)) {
                    hotGoodsList.add(goods);
                    goodsAdapter.notifyItemChanged(hotGoodsList.size() - 1);
                    int count = goodsDao.insert(goods);
                    if(count>0){
                        MyToast.toastShort(context,"添加成功");
                    }


                }
            }
        });


//        GridLayoutManager selectmanager = new GridLayoutManager(this, 5);
//        rvGoodsSelect.setLayoutManager(selectmanager);
//        ItemDragHelperCallback callback2 = new ItemDragHelperCallback() {
//            @Override
//            public boolean isLongPressDragEnabled() {
//                // 长按拖拽打开
//                return true;
//            }
//        };
//        ItemTouchHelper helper2 = new ItemTouchHelper(callback2);
//        helper2.attachToRecyclerView(rvGoodsSelect);


//        goodsSelectAdapter = new GoodsSelectAdapter(context);
//        goodsSelectAdapter.setMyOnItemClickListener(new MyOnItemClickListener2() {
//            @Override
//            public void myOnItemClick(int position, int flag) {
//                //TODO   添加侧面 面板中
//                AllGoods allGoods = goodsSelectAdapter.getItem(position);
//                Goods goods = new Goods();
//                goods.setCid(allGoods.getCid());
//                goods.setPrice(allGoods.getPrice());
//                goods.setBatchCode(allGoods.getBatchCode());
//                goods.setName(allGoods.getName());
//
//                goods.setType(allGoods.getType());
//                goods.setTypeid(allGoods.getTypeid());
//                if (hotGoodsSet.add(goods)) {
//                    hotGoodsList.add(goods);
//                    goodsAdapter.notifyItemChanged(hotGoodsList.size() - 1);
//                    goodsDao.insert(goods);
//                }
//            }
//        });
//        rvGoodsSelect.setAdapter(goodsSelectAdapter);

    }


    @Override
    public void myOnItemClick(int position, int flag) {
        switch (flag) {
            case 1:
                // 进行 删除操作
                Goods goods = goodsAdapter.getItem(position);
                goodsAdapter.removeList(position);
                if (goods != null) {
                    goodsDao.delete(goods);
                }
                break;
            case 2:
                GoodsType goodsType = goodsTypeAdapter.getItem(position);
                int goodsTypeId = goodsType.getId();
                List<AllGoods> goodsList = allGoodsDao.queryByTypeId(goodsTypeId);
                classAdapter.setDatas(goodsList);
                classAdapter.notifyDataSetChanged();

                break;
        }
    }


    private Set<Goods> hotGoodsSet;
    private List<Goods> hotGoodsList;

    private void getGoods() {
        hotGoodsList = goodsDao.queryAll();
        hotGoodsSet = new HashSet<>(hotGoodsList);


        final List<GoodsType> goodsTypes = goodsTypeDao.queryAll();
        GoodsType goodsType = new GoodsType();
        goodsType.setId(-1);
        goodsType.setName("全部商品");
        goodsTypes.add(0, goodsType);

        new Thread(new Runnable() {
            @Override
            public void run() {
                goodsAdapter.setDatas(hotGoodsList);
                goodsTypeAdapter.setDatas(goodsTypes);
                List<AllGoods> goodsList = allGoodsDao.queryByTypeId(-1);
//                goodsSelectAdapter.setDatas(goodsList);
                classAdapter.setDatas(goodsList);

                handler.sendEmptyMessage(NOTIFY_INITDAT);


            }
        }).start();


//        goodsAdapter.setDatas(goodsList);

    }

    public void setInitView() {

        findViewById(R.id.btnSave).setOnClickListener(this);

        EditText searchEt = findViewById(R.id.commodity_management_search_et);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    return;
                }
//                Pattern pattern = Pattern.compile(s.toString());
//                List<CommodityBean> result = new ArrayList<>();


//                setClassTitleTxtColor();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.commodity_management_class_titlte_all_tv:
//                classAdapter = new ClassAdapter(this, allGoodsList);
//                classGv.setAdapter(classAdapter);
//                setClassTitleTxtColor();
//                break;
            case R.id.btnSave:
                MyToast.toastShort(context, "保存成功");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == 1001) {
            if (data != null) {
                int position = data.getIntExtra("position", -1);
                HotKeyBean goods = (HotKeyBean) Objects.requireNonNull(data.getExtras()).getSerializable("HotKeyBean");
                CommodityBean bean = new CommodityBean();
                bean.setHotKeyBean(goods);
                hotKeyList.set(position, bean);
            }

        }*/
    }



    private void saveSelectedGoods(String message) {
        EventBus.getDefault().post(new BusEvent(BusEvent.SAVE_COMMODITY_SUCCESS, true));
        Toast.makeText(GoodsSettingActivity.this, message, Toast.LENGTH_SHORT).show();


        goodsAdapter.showDeleteTv(false);
        rvGoods.setAdapter(goodsAdapter);
    }



    private SweetAlertDialog mSweetAlertDialog;

    public void closeLoading() {
        if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {
            mSweetAlertDialog.dismissWithAnimation();
        }
    }

    public void showLoading(String titleText) {
        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);


        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading() {
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    private class ClassAdapter extends BaseAdapter {
        List<AllGoods> list;
        private Context context;

        ClassAdapter(Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        public void setDatas(List<AllGoods> list) {
            this.list = list;
        }

        @Override
        public AllGoods getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.commodity_class_item, null);
                holder = new ViewHolder();
                holder.nameBtn = convertView.findViewById(R.id.commodity_class_name_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final AllGoods item = list.get(position);
            holder.nameBtn.setText(item.getName());
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    CommodityBean bean = (CommodityBean) classAdapter.getItem(position);
//                    AllGoods goods = item.getAllGoods();
//                    if (goods != null && hotKeyMap.containsKey(goods.getName() + goods.cid)) return;
//                    CategoryGoods.child categoryChilds = item.getCategoryChilds();
//                    if (categoryChilds != null && hotKeyMap.containsKey(categoryChilds.name + categoryChilds.cid))
//                        return;
//                    item.setShow(isShowDelTv);
//                    HotKeyBean HotKeyBean = new HotKeyBean();
//                    if (goods != null) {
//                        HotKeyBean.id = goods.id;
//                        HotKeyBean.cid = goods.cid;
//                        HotKeyBean.name = goods.name;
//                        HotKeyBean.price = goods.price;
//                        HotKeyBean.traceable_code = goods.traceable_code;
//                        HotKeyBean.is_default = goods.is_default;
//                    }
//                    if (categoryChilds != null) {
//                        HotKeyBean.id = categoryChilds.id;
//                        HotKeyBean.cid = categoryChilds.cid;
//                        HotKeyBean.name = categoryChilds.name;
//                        HotKeyBean.price = categoryChilds.price;
//                        HotKeyBean.traceable_code = categoryChilds.traceable_code;
//                        HotKeyBean.is_default = categoryChilds.is_default;
//                    }
//                    CommodityBean hotKeyBean = new CommodityBean();
//                    hotKeyBean.setHotKeyBean(HotKeyBean);
//                    hotKeyMap.put(hotKeyBean.getHotKeyBean().getName() + hotKeyBean.getHotKeyBean().cid, hotKeyBean);
//                    hotKeyList.add(hotKeyBean);
//
//                    for (int i = 0; i < hotKeyList.size() - 1; i++) {
//                        for (int j = hotKeyList.size() - 1; j > i; j--) {
//                            if (hotKeyList.get(j).getHotKeyBean().id == hotKeyList.get(i).getHotKeyBean().id) {
//                                hotKeyList.remove(j);
//                            }
//                        }
//                    }
//
//                    goodsAdapter.notifyDataSetChanged();
//                }
//            });
            return convertView;
        }

        class ViewHolder {
            Button nameBtn;
        }
    }


    public interface OnItemClickListener {
        void onItemClickListener(View v);
    }


    public <T> ObservableTransformer<T, T> setThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
