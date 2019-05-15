package com.axecom.smartweight.ui.activity.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BusEvent;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.impl.ItemDragHelperCallback;
import com.axecom.smartweight.my.adapter.GoodsTypeAdapter;
import com.axecom.smartweight.my.adapter.HotGoodsAdapter;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.AllGoods;
import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.axecom.smartweight.my.entity.GoodsType;
import com.axecom.smartweight.my.entity.HotGood;
import com.axecom.smartweight.my.entity.dao.AllGoodsDao;
import com.axecom.smartweight.my.entity.dao.GoodsTypeDao;
import com.axecom.smartweight.my.entity.dao.HotGoodsDao;
import com.luofx.listener.MyOnItemClickListener2;
import com.luofx.newclass.ActivityController;
import com.xuanyuan.library.MyToast;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.axecom.smartweight.my.config.IEventBus.NOTIFY_HOT_GOOD_CHANGE;


/**
 * 商品 设置  ： 提供功能，添加删除菜品菜单
 */
public class GoodsSettingActivity extends Activity implements View.OnClickListener, MyOnItemClickListener2, IConstants {


    // 热键商品适配器
    private HotGoodsAdapter hotGoodsAdapter;
    private GoodsTypeAdapter goodsTypeAdapter;
    protected SysApplication sysApplication;

    private HotGoodsDao hotGoodsDao;
    // 所有商品
    private AllGoodsDao allGoodsDao;
    // 货物类型 Dao
    private GoodsTypeDao goodsTypeDao;
    private Context context;
    //热键 商品 列表
    private RecyclerView rvHotGoods;

    //商品类型列表
    private RecyclerView rvGoodsType;
    // 产品选择列表
    private GridView rvGoodsSelect;

    /**
     * 销售商品列表
     */
    private List<HotGood> hotHotGoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.activity_goods_setting);
        sysApplication = (SysApplication) getApplication();
        context = this;
        ActivityController.addActivity(this);

        initView();
        initHander();
        hotGoodsDao = new HotGoodsDao(context);
        allGoodsDao = new AllGoodsDao(context);
        goodsTypeDao = new GoodsTypeDao(context);
        initRecycle();
        getGoods();
    }

    private Handler handler;

    private void initHander() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == NOTIFY_INITDAT) {
                    classAdapter.notifyDataSetChanged();
                    goodsTypeAdapter.notifyDataSetChanged();
                    hotGoodsAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private ClassAdapter classAdapter;


    private int addCount;//添加的数量

    private void initRecycle() {

        /*  初始化商品热键表   ***********************************************/

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvHotGoods.setLayoutManager(manager);
        // 回拽 分割线
        ItemDragHelperCallback callback = new ItemDragHelperCallback() {
            @Override
            public boolean isLongPressDragEnabled() {
                // 长按拖拽打开
                return true;
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //设置拖拽方向，上下左右
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //拖拽元素交换
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                HotGood from = hotHotGoodList.get(fromPosition);
                HotGood to = hotHotGoodList.get(toPosition);
                Log.i("rzl", "from:" + from.getName() + ",to:" + to.getName() + "," + fromPosition + "<->" + toPosition);
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        HotGood from1 = hotHotGoodList.get(i);
                        HotGood to1 = hotHotGoodList.get(i + 1);
                        //内存排序
                        Collections.swap(hotHotGoodList, i, i + 1);
                        //将新的排序写入数据库,交换id(这个id是排序用的,并非唯一标志)
                        int fromId = from1.getId();
                        int toId = to1.getId();
                        from1.setId(toId);
                        to1.setId(fromId);
                        hotGoodsDao.update(from1);
                        hotGoodsDao.update(to1);

                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        HotGood from1 = hotHotGoodList.get(i);
                        HotGood to1 = hotHotGoodList.get(i - 1);
                        //内存排序
                        Collections.swap(hotHotGoodList, i, i - 1);
                        //数据库排序
                        int fromId = from1.getId();
                        int toId = to1.getId();
                        from1.setId(toId);
                        to1.setId(fromId);
                        hotGoodsDao.update(from1);
                        hotGoodsDao.update(to1);
                    }
                }
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        helper.attachToRecyclerView(rvHotGoods);
        hotGoodsAdapter = new HotGoodsAdapter(context);
        hotGoodsAdapter.setMyOnItemClickListener(this);
        rvHotGoods.setAdapter(hotGoodsAdapter);

        /*  初始化  类别表  ************************************/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGoodsType.setLayoutManager(linearLayoutManager);

//        ItemTouchHelper helper = new ItemTouchHelper(callback);
//        helper.attachToRecyclerView(rvGoods);
        goodsTypeAdapter = new GoodsTypeAdapter(context);
        goodsTypeAdapter.setMyOnItemClickListener(this);
        rvGoodsType.setAdapter(goodsTypeAdapter);

        /*  商品选择栏 **********************/

        classAdapter = new ClassAdapter(context);
        rvGoodsSelect.setAdapter(classAdapter);

        rvGoodsSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AllGoods allGoods = classAdapter.getItem(position);
                HotGood hotGood = new HotGood();
                hotGood.setCid(allGoods.getCid());
                hotGood.setPrice(allGoods.getPrice());
                hotGood.setBatchCode(allGoods.getBatchCode());
                hotGood.setName(allGoods.getName());

                hotGood.setType(allGoods.getType());
                hotGood.setTypeid(allGoods.getTypeid());
                if (!hotHotGoodList.contains(hotGood)) {
                    hotHotGoodList.add(hotGood);
                    hotGoodsAdapter.notifyItemChanged(hotHotGoodList.size() - 1);
                    int typeid = allGoods.getTypeid();

                    GoodsType goodsType = goodsTypeDao.queryById(typeid);
                    if (goodsType != null) {
                        hotGood.setBatchCode(goodsType.getTraceno());
                    }

                    int count = hotGoodsDao.insert(hotGood);
                    if (count > 0) {
                        addCount++;
                        MyToast.toastShort(context, "添加成功");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (addCount > 0) {
            BaseBusEvent event = new BaseBusEvent();
            event.setEventType(NOTIFY_HOT_GOOD_CHANGE);
            EventBus.getDefault().post(event);
        }
    }

    /**
     * @param position 位置
     * @param flag     标识
     */
    @Override
    public void myOnItemClick(int position, int flag) {
        switch (flag) {
            case 1:
                try {
                    // 进行 删除操作
                    HotGood hotGood = hotGoodsAdapter.getItem(position);
                    hotGoodsAdapter.removeList(position);
                    if (hotGood != null) {
                        addCount++;
                        hotGoodsDao.delete(hotGood);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                GoodsType goodsType = goodsTypeAdapter.getItem(position);
                goodsTypeAdapter.setSelected(position);
                goodsTypeAdapter.notifyDataSetChanged();

                int goodsTypeId = goodsType.getId();
                List<AllGoods> goodsList = allGoodsDao.queryByTypeId(goodsTypeId);
                classAdapter.setDatas(goodsList);
                classAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void getGoods() {

        final List<GoodsType> goodsTypes = goodsTypeDao.queryAll();
        if (goodsTypes != null) {
            GoodsType goodsType = new GoodsType();
            goodsType.setId(-1);
            goodsType.setName("全部商品");
            goodsTypes.add(0, goodsType);

            sysApplication.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    hotHotGoodList = hotGoodsDao.queryAll();//获取热键商品
                    hotGoodsAdapter.setDatas(hotHotGoodList);
                    goodsTypeAdapter.setDatas(goodsTypes);
                    List<AllGoods> goodsList = allGoodsDao.queryByTypeId(-1);
//                goodsSelectAdapter.setDatas(goodsList);
                    classAdapter.setDatas(goodsList);
                    handler.sendEmptyMessage(NOTIFY_INITDAT);
                }
            });
        }
    }

    //编辑框
    private EditText etGoodName;

    public void initView() {
        etGoodName = findViewById(R.id.etGoodName);
        findViewById(R.id.btnSearch).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        rvHotGoods = findViewById(R.id.rvHotGoods);
        rvGoodsType = findViewById(R.id.rvGoodsType);
        rvGoodsSelect = findViewById(R.id.rvGoodsSelect);

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
            case R.id.btnSearch:
                String goodName = etGoodName.getText().toString();
                List<AllGoods> lists = allGoodsDao.queryByName(goodName);
                classAdapter.setDatas(lists);
                hintKeyBoard();
                break;
        }
    }

    /**
     * 隐藏键盘
     */
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
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

        hotGoodsAdapter.showDeleteTv(false);
        rvHotGoods.setAdapter(hotGoodsAdapter);
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
        private final Context context;

        ClassAdapter(Context context) {
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

        @SuppressLint("InflateParams")
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
            return convertView;
        }

        private class ViewHolder {
            private TextView nameBtn;
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
