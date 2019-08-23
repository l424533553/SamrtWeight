package com.axecom.smartweight.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.axecom.smartweight.R;
import com.axecom.smartweight.activity.main.MainObservableBean;
import com.axecom.smartweight.adapter.BackgroundAdapter;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.databinding.ActivityLocalSettingBinding;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.help.CashierInputFilter;
import com.xuanyuan.library.utils.LiveBus;

import static com.axecom.smartweight.config.IEventBus.BACKGROUND_CHANGE;
import static com.axecom.smartweight.config.IEventBus.SHOPPING_BAG_PRICE_CHANGE;
import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;

public class LocalSettingActivity extends Activity implements View.OnClickListener, IConstants {
    private Context context;
    private ActivityLocalSettingBinding binding;
    private MainObservableBean mainBean;

    private void initData() {
        mainBean.getPriceLarge().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_LARGE, 0.3f));
        mainBean.getPriceMiddle().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_MIDDLE, 0.2f));
        mainBean.getPriceSmall().set(MyPreferenceUtils.getSp(context).getFloat(PRICE_SMALL, 0.1f));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_local_setting);
        mainBean = new MainObservableBean();
        binding.setMainBean(mainBean);
        binding.setOnClickListener(this);

        context = this;
        initData();
        initView();

    }


    public void doBack(View view) {
        onBackPressed();
    }


    private void initView() {
        InputFilter[] filters = {new CashierInputFilter(99)};
        binding.etLarge.setFilters(filters); //设置
        binding.etMiddle.setFilters(filters); //设置
        binding.etSmall.setFilters(filters); //设置

        int index = MyPreferenceUtils.getSp(context).getInt("BACKGROUND_INDEX", 0);

        /* 静态的显示下来出来的菜单选项，显示的数组元素提前已经设置好了
         * 第二个参数：已经编写好的数组
         * 第三个数据：默认的样式
         */
        final BackgroundAdapter adapter = new BackgroundAdapter(this);
        binding.ivSelect.setAdapter(adapter);
        binding.ivSelect.setSelection(index);
        binding.ivSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyPreferenceUtils.getSp(context).edit().putInt("BACKGROUND_INDEX", position).apply();
                LiveBus.post(EVENT_BUS_COMMON, String.class, BACKGROUND_CHANGE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        printerSpinner();

    }

    /**
     * 使用情况
     */
    public void printerSpinner() {
        Spinner mSpinnerSimple = findViewById(R.id.spPrinter);
        mSpinnerSimple.setDropDownWidth(150); //下拉宽度
        mSpinnerSimple.setDropDownHorizontalOffset(20); //下拉的横向偏移
        mSpinnerSimple.setDropDownVerticalOffset(40); //下拉的纵向偏移
        //mSpinnerSimple.setBackgroundColor(AppUtil.getColor(instance,R.color.wx_bg_gray)); //下拉的背景色
        //spinner mode ： dropdown or dialog , just edit in layout xml
//        mSpinnerSimple.setPrompt("Spinner Title"); //弹出框标题，在dialog下有效

        String[] spinnerItems = {"HDD打印机", "E39打印机"};
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, R.layout.item_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_drop);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        mSpinnerSimple.setAdapter(spinnerAdapter);

        int printerIndex = MyPreferenceUtils.getSp(context).getInt(PRINTER_TYPE, PRINTER_HHD);
        mSpinnerSimple.setSelection(printerIndex);

        //选择监听
        //parent就是父控件spinner
        //view就是spinner内填充的textview,id=@android:id/text1
        //position是值所在数组的位置
        //id是值所在行的位置，一般来说与 position一致
        mSpinnerSimple.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                MyPreferenceUtils.getSp(context).edit().putInt(PRINTER_TYPE, pos).apply();
                //设置spinner内的填充文字居中
                //((TextView)view).setGravity(Gravity.CENTER);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
                // 使用 功能 数据
                System.out.println("shuju");
                // 使用测试


            }
        });
    }


    //
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSelect:
                //弹出背景选择框
                break;
            case R.id.btnSubmitShoppingBag:
                if (TextUtils.isEmpty(binding.etLarge.getText().toString())
                        || TextUtils.isEmpty(binding.etMiddle.getText().toString())
                        || TextUtils.isEmpty(binding.etSmall.getText().toString())) {
                    MyToast.toastShort(context, "塑料袋的价格不可以设定为空！");
                } else {
                    float lagerPrice = Float.valueOf(binding.etLarge.getText().toString());
                    float middlePrice = Float.valueOf(binding.etMiddle.getText().toString());
                    float smallPrice = Float.valueOf(binding.etSmall.getText().toString());
                    if (lagerPrice > 0 && middlePrice > 0 && smallPrice > 0) {
                        MyPreferenceUtils.getSp(context).edit()
                                .putFloat(PRICE_LARGE, lagerPrice)
                                .putFloat(PRICE_MIDDLE, middlePrice)
                                .putFloat(PRICE_SMALL, smallPrice)
                                .apply();
                        LiveBus.post(EVENT_BUS_COMMON, String.class, SHOPPING_BAG_PRICE_CHANGE);
                        MyToast.toastShort(context, "价格改变设定成功！");
                    } else {
                        MyToast.toastShort(context, "价格不可设定小于等于零！");
                    }
                }
                break;
            default:
                break;
        }
    }
}
