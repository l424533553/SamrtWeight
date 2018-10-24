package com.axecom.smartweight.ui.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BaseActivity;
import com.axecom.smartweight.base.BaseEntity;
import com.axecom.smartweight.base.BusEvent;
import com.axecom.smartweight.bean.FastLoginInfo;
import com.axecom.smartweight.manager.AccountManager;
import com.axecom.smartweight.manager.SystemSettingManager;
import com.axecom.smartweight.net.RetrofitFactory;
import com.axecom.smartweight.ui.view.ChooseDialog;
import com.axecom.smartweight.ui.view.SoftKeyborad;
import com.axecom.smartweight.utils.NetworkUtil;
import com.axecom.smartweight.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SystemSettingsActivity2 extends BaseActivity {

    private View rootView;
    private Button loginTypeBtn, printerBtn, balanceRoundingBtn, priceingMethodBtn, weightRoundingBtn, weightUnitBtn;
    private TextView buyerNumberTv, sellerNumberTv;
    private TextView loginTypeTv, printerTv, balanceRoundingTv, priceingMethodTv, weightRoundingTv, weightUnitTv;
    private Button backBtn, saveBtn;
    private CheckedTextView notClearPriceCtv, saveWeightCtv, autoObtainCtv, cashEttlementCtv, distinguishCtv, icCardSettlementCtv, stopPrintCtv, noPatchSettlementCtv, autoPrevCtv, cashRoundingCtv, stopCashCtv, stopAlipayCtv, stopweichatpayCtv;


    List<Map<String, String>> loginTypeList = SystemSettingManager.getLoginTypeList();
    List<Map<String, String>> pricingModelList = SystemSettingManager.getDefaultPricingTypeList();
    List<Map<String, String>> printerList = SystemSettingManager.getPrinterConfigurationList();
    List<Map<String, String>> roundingWeightList =SystemSettingManager.getRoundingWeightOptions();
    List<Map<String, String>> screenUnitList = SystemSettingManager.getScreenUnitDisplayOptions();
    List<Map<String, String>> balanceRoundingList = SystemSettingManager.getBalanceRoundingOptions();
    private int loginTypePos = 0, printerPos = 0, balanceRoundingPos = 0, priceingMethodPos = 0, weightRoundingPos = 0, weightUnitPos = 0;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.system_settings_activity, null);
        loginTypeBtn = rootView.findViewById(R.id.system_settings_login_type_btn);
        printerBtn = rootView.findViewById(R.id.system_settings_printer_btn);
        balanceRoundingBtn = rootView.findViewById(R.id.system_settings_balance_rounding_btn);
        priceingMethodBtn = rootView.findViewById(R.id.system_settings_default_priceing_method_btn);
        weightRoundingBtn = rootView.findViewById(R.id.system_settings_weight_rounding_btn);
        weightUnitBtn = rootView.findViewById(R.id.system_settings_weight_unit_btn);

        buyerNumberTv = rootView.findViewById(R.id.system_settings_default_buyer_number_tv);
        sellerNumberTv = rootView.findViewById(R.id.system_settings_default_seller_number_tv);

        loginTypeTv = rootView.findViewById(R.id.system_settings_login_type_tv);
        printerTv = rootView.findViewById(R.id.system_settings_printer_tv);
        balanceRoundingTv = rootView.findViewById(R.id.system_settings_balance_rounding_tv);
        priceingMethodTv = rootView.findViewById(R.id.system_settings_default_priceing_method_tv);
        weightRoundingTv = rootView.findViewById(R.id.system_settings_weight_rounding_tv);
        weightUnitTv = rootView.findViewById(R.id.system_settings_weight_unit_tv);

        notClearPriceCtv = rootView.findViewById(R.id.system_settings_not_clear_price_ctv);
        saveWeightCtv = rootView.findViewById(R.id.system_settings_save_weight_ctv);
        autoObtainCtv = rootView.findViewById(R.id.system_settings_auto_obtain_ctv);
        cashEttlementCtv = rootView.findViewById(R.id.system_settings_cash_ettlement_ctv);
        distinguishCtv = rootView.findViewById(R.id.system_settings_distinguish_ctv);
        icCardSettlementCtv = rootView.findViewById(R.id.system_settings_ic_card_settlement_ctv);
        stopPrintCtv = rootView.findViewById(R.id.system_settings_stop_print_ctv);
        noPatchSettlementCtv = rootView.findViewById(R.id.system_settings_no_patch_settlement_ctv);
        autoPrevCtv = rootView.findViewById(R.id.system_settings_auto_prev_ctv);
        cashRoundingCtv = rootView.findViewById(R.id.system_settings_cash_rounding_ctv);
        stopCashCtv = rootView.findViewById(R.id.system_settings_stop_cash_ctv);
        stopAlipayCtv = rootView.findViewById(R.id.system_settings_stop_alipay_ctv);
        stopweichatpayCtv = rootView.findViewById(R.id.system_settings_stop_weichatpay_ctv);

        backBtn = rootView.findViewById(R.id.system_settings_back_btn);
        saveBtn = rootView.findViewById(R.id.system_settings_save_btn);

        loginTypeBtn.setOnClickListener(this);
        printerBtn.setOnClickListener(this);
        balanceRoundingBtn.setOnClickListener(this);
        priceingMethodBtn.setOnClickListener(this);
        weightRoundingBtn.setOnClickListener(this);
        weightUnitBtn.setOnClickListener(this);
        buyerNumberTv.setOnClickListener(this);
        sellerNumberTv.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        notClearPriceCtv.setOnClickListener(this);
        saveWeightCtv.setOnClickListener(this);
        autoObtainCtv.setOnClickListener(this);
        cashEttlementCtv.setOnClickListener(this);
        distinguishCtv.setOnClickListener(this);
        stopPrintCtv.setOnClickListener(this);
        noPatchSettlementCtv.setOnClickListener(this);
        autoPrevCtv.setOnClickListener(this);
        cashRoundingCtv.setOnClickListener(this);
        stopCashCtv.setOnClickListener(this);
        stopAlipayCtv.setOnClickListener(this);
        stopweichatpayCtv.setOnClickListener(this);
        icCardSettlementCtv.setOnClickListener(this);



        loginTypeTv.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void initView() {
        getSettingData();
    }

    @Override
    public void onClick(View v) {
        ChooseDialog.Builder chooseBuilder = new ChooseDialog.Builder(this);
        SoftKeyborad.Builder softBuilder = new SoftKeyborad.Builder(this);
        switch (v.getId()) {
            case R.id.system_settings_login_type_btn:
                chooseBuilder.create(loginTypeList, loginTypePos, new ChooseDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
                        loginTypePos = position;
                        String optionKey = position + 1 + "";
                        loginTypeTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(optionKey));
                        loginTypeTv.setTag(position);
                    }
                }).show();
                break;
            case R.id.system_settings_printer_btn:
                chooseBuilder.create(printerList, printerPos, new ChooseDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
                        printerPos = position;
                        String optionKey = position + 1 + "";
                        printerTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(optionKey));
                        printerTv.setTag(optionKey);
                    }
                }).show();
                break;
            case R.id.system_settings_balance_rounding_btn:
                chooseBuilder.create(balanceRoundingList, balanceRoundingPos, new ChooseDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
                        balanceRoundingPos = position;
                        String optionKey = position + 1 + "";
                        balanceRoundingTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(optionKey));
                        balanceRoundingTv.setTag(optionKey);
                    }
                }).show();
                break;
            case R.id.system_settings_default_priceing_method_btn:
                chooseBuilder.create(pricingModelList, priceingMethodPos, new ChooseDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
                        priceingMethodPos = position;
                        SPUtils.put(SystemSettingsActivity2.this, SettingsActivity.KET_SWITCH_SIMPLE_OR_COMPLEX, position == 0 ? false : true);
                        String optionKey = position + 1 + "";
                        priceingMethodTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(optionKey));
                        priceingMethodTv.setTag(optionKey);
                    }
                }).show();
                break;
            case R.id.system_settings_weight_rounding_btn:
                chooseBuilder.create(roundingWeightList, weightRoundingPos, new ChooseDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
                        weightRoundingPos = position;
                        String optionKey = position + 1 + "";
                        weightRoundingTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(optionKey));
                        weightRoundingTv.setTag(optionKey);
                    }
                }).show();
                break;
            case R.id.system_settings_weight_unit_btn:
                chooseBuilder.create(screenUnitList, weightUnitPos, new ChooseDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
                        weightUnitPos = position;
                        String key = position + 1 + "";
                        weightUnitTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(key));
                        weightUnitTv.setTag(key);
                    }
                }).show();
                break;
            case R.id.system_settings_default_buyer_number_tv:
                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
                    @Override
                    public void onConfirmed(String result) {
                        buyerNumberTv.setText(result);
                    }
                }).show();
                break;
            case R.id.system_settings_default_seller_number_tv:
                if (!NetworkUtil.isConnected(this)) {
                    Toast.makeText(this, "请先连接网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
                    @Override
                    public void onConfirmed(String result) {
                        sellerNumberTv.setText(result);
                    }
                }).show();
                break;
            case R.id.system_settings_not_clear_price_ctv:
                notClearPriceCtv.setChecked(!notClearPriceCtv.isChecked());
                break;
            case R.id.system_settings_save_weight_ctv:
                saveWeightCtv.setChecked(!saveWeightCtv.isChecked());
                break;
            case R.id.system_settings_auto_obtain_ctv:
                autoObtainCtv.setChecked(!autoObtainCtv.isChecked());
                break;
            case R.id.system_settings_cash_ettlement_ctv:
                cashEttlementCtv.setChecked(!cashEttlementCtv.isChecked());
                break;
            case R.id.system_settings_distinguish_ctv:
                distinguishCtv.setChecked(!distinguishCtv.isChecked());
                break;
            case R.id.system_settings_ic_card_settlement_ctv:
                icCardSettlementCtv.setChecked(!icCardSettlementCtv.isChecked());
                break;
            case R.id.system_settings_stop_print_ctv:
                stopPrintCtv.setChecked(!stopPrintCtv.isChecked());
                break;
            case R.id.system_settings_no_patch_settlement_ctv:
                noPatchSettlementCtv.setChecked(!noPatchSettlementCtv.isChecked());
                break;
            case R.id.system_settings_auto_prev_ctv:
                autoPrevCtv.setChecked(!autoPrevCtv.isChecked());
                break;
            case R.id.system_settings_cash_rounding_ctv:
                cashRoundingCtv.setChecked(!cashRoundingCtv.isChecked());
                break;
            case R.id.system_settings_stop_cash_ctv:
                stopCashCtv.setChecked(!stopCashCtv.isChecked());
                break;
            case R.id.system_settings_stop_alipay_ctv:
                stopAlipayCtv.setChecked(!stopAlipayCtv.isChecked());
                break;
            case R.id.system_settings_stop_weichatpay_ctv:
                stopweichatpayCtv.setChecked(!stopweichatpayCtv.isChecked());
                break;
            case R.id.system_settings_back_btn:
                finish();
                break;
            case R.id.system_settings_login_type_tv:
            case R.id.system_settings_save_btn:
                saveSettingsToSP();
                break;
        }
    }

    public void saveSettingsToSP() {

        String sellerNumber = sellerNumberTv.getText().toString();
        if (!TextUtils.isEmpty(sellerNumber)) {
            requestBindSeller(sellerNumber);
        }
        SystemSettingManager.default_login_type_save(loginTypeTv.getText().toString());
        SystemSettingManager.printer_configuration_save(printerTv.getText().toString());
        SystemSettingManager.default_buyer_number_save(buyerNumberTv.getText().toString());
        SystemSettingManager.balance_rounding_save(balanceRoundingTv.getText().toString());
        SystemSettingManager.default_pricing_model_save(priceingMethodTv.getText().toString());
        SystemSettingManager.rounding_weight_save(weightRoundingTv.getText().toString());
        SystemSettingManager.default_seller_number_save(sellerNumber);
        SystemSettingManager.screen_unit_display_save( weightUnitTv.getText().toString());
        SystemSettingManager.price_after_saving_save( notClearPriceCtv.isChecked());
        SystemSettingManager.confirm_the_preservation_save( saveWeightCtv.isChecked());
        SystemSettingManager.buyers_and_sellers_by_default_save( autoObtainCtv.isChecked());
        SystemSettingManager.online_settlement_save( cashEttlementCtv.isChecked());
        SystemSettingManager.buyers_and_sellers_after_weighing_save( distinguishCtv.isChecked());
        SystemSettingManager.card_settlement_save( icCardSettlementCtv.isChecked());
        SystemSettingManager.disable_printing_save( stopPrintCtv.isChecked());
        SystemSettingManager.allow_batchless_settlement_save( noPatchSettlementCtv.isChecked());
        SystemSettingManager.take_a_unit_price_save( autoPrevCtv.isChecked());
        SystemSettingManager.cash_change_rounding_save( cashRoundingCtv.isChecked());
        SystemSettingManager.disable_cash_mode_save( stopCashCtv.isChecked());
        SystemSettingManager.disable_alipay_mode_save( stopAlipayCtv.isChecked());
        SystemSettingManager.disable_weixin_mode_save( stopweichatpayCtv.isChecked());

        showLoading("保存成功");
    }

    private void requestBindSeller(String sellerNumber) {
        RetrofitFactory.getInstance().API()
                .fastLogin(AccountManager.getInstance().getScalesId(), sellerNumber)
                .compose(this.<BaseEntity<FastLoginInfo>>setThread())
                .subscribe(new Observer<BaseEntity<FastLoginInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final BaseEntity<FastLoginInfo> fastLoginInfo) {
                        AccountManager instance = AccountManager.getInstance();
                        boolean success = fastLoginInfo.isSuccess();
                        if (success) {
                            Toast.makeText(SystemSettingsActivity2.this,"绑定卖家成功",Toast.LENGTH_SHORT).show();
                            instance.saveToken(fastLoginInfo.getData().getToken());
                        }else{
//                            instance.saveToken(AccountManager.getInstance().getAdminToken());
                            Toast.makeText(SystemSettingsActivity2.this,"绑定卖家失败",Toast.LENGTH_SHORT).show();
                        }
                        EventBus.getDefault().post(new BusEvent(BusEvent.notifiySellerInfo,success));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void getSettingData() {

        loginTypeTv.setText(SystemSettingManager.default_login_type());
        printerTv.setText(SystemSettingManager.printer_configuration());
        buyerNumberTv.setText(SystemSettingManager.default_buyer_number());
        balanceRoundingTv.setText(SystemSettingManager.balance_rounding());
        priceingMethodTv.setText(SystemSettingManager.default_pricing_model());
        weightRoundingTv.setText(SystemSettingManager.rounding_weight());
        sellerNumberTv.setText(SystemSettingManager.default_seller_number());
        weightUnitTv.setText(SystemSettingManager.screen_unit_display());


        notClearPriceCtv.setChecked(SystemSettingManager.price_after_saving());
        saveWeightCtv.setChecked(SystemSettingManager.confirm_the_preservation());
        autoObtainCtv.setChecked(SystemSettingManager.buyers_and_sellers_by_default());
        cashEttlementCtv.setChecked(SystemSettingManager.online_settlement());
        distinguishCtv.setChecked(SystemSettingManager.buyers_and_sellers_after_weighing());
        icCardSettlementCtv.setChecked(SystemSettingManager.card_settlement());
        stopPrintCtv.setChecked(SystemSettingManager.disable_printing());
        noPatchSettlementCtv.setChecked(SystemSettingManager.allow_batchless_settlement());
        autoPrevCtv.setChecked(SystemSettingManager.allow_batchless_settlement());
        cashRoundingCtv.setChecked(SystemSettingManager.cash_change_rounding());
        stopCashCtv.setChecked(SystemSettingManager.disable_cash_mode());
        stopAlipayCtv.setChecked(SystemSettingManager.disable_alipay_mode());
        stopweichatpayCtv.setChecked(SystemSettingManager.disable_weixin_mode());
    }
}

