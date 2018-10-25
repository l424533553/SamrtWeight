//package com.axecom.smartweight.ui.activity;
//
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.CheckedTextView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.axecom.smartweight.R;
//import com.axecom.smartweight.base.BaseActivity;
//import com.axecom.smartweight.base.BaseEntity;
//import com.axecom.smartweight.base.BusEvent;
//import com.axecom.smartweight.bean.FastLoginInfo;
//import com.axecom.smartweight.manager.AccountManager;
//import com.axecom.smartweight.manager.MacManager;
//import com.axecom.smartweight.net.RetrofitFactory;
//import com.axecom.smartweight.ui.view.ChooseDialog;
//import com.axecom.smartweight.ui.view.SoftKeyborad;
//import com.axecom.smartweight.utils.NetworkUtil;
//import com.axecom.smartweight.utils.SPUtils;
//import com.google.gson.internal.LinkedTreeMap;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//
//public class SystemSettingsActivity extends BaseActivity {
//
//    public static final String KEY_DEFAULT_LOGIN_TYPE = "key_default_login_type";
//    public static final String KEY_PRINTER = "key_printer";
//    public static final String KEY_BUYER_NUMBER = "key_buyer_number";
//    public static final String KEY_BALANCE_ROUNDING = "key_balance_rounding";
//    public static final String KEY_PRICEING_METHOD = "key_priceing_method";
//    public static final String KEY_WEIGHT_ROUNDING = "key_weight_rounding";
//    public static final String KEY_SELLER_NUMBER = "key_seller_number";
//    public static final String KEY_WEIGHT_UNIT = "key_weight_unit";
//
//    public static final String KEY_NOT_CLEAR = "key_not_clear";
//    public static final String KEY_SAVE_WEIGHT = "key_save_weight";
//    public static final String KEY_AUTO_OBTAIN = "key_auto_obtain";
//    public static final String KEY_CASH_SETTLEMENT = "key_cash_settlement";
//    public static final String KEY_DISTINGUISH = "key_distinguish";
//    public static final String KEY_ICCARD_SETTLEMENT = "key_iccard_settlement";
//    public static final String KEY_STOP_PRINT = "key_stop_print";
//    public static final String KEY_NO_PATCH_SETTLEMENT = "key_no_patch_settlement";
//    public static final String KEY_AUTO_PREV = "key_auto_prev";
//    public static final String KEY_CASH_ROUNDING = "key_cash_rounding";
//    public static final String KEY_STOP_CASH = "key_stop_cash";
//
//
//    private View rootView;
//    private Button loginTypeBtn, printerBtn, balanceRoundingBtn, priceingMethodBtn, weightRoundingBtn, weightUnitBtn;
//    private TextView buyerNumberTv, sellerNumberTv;
//    private TextView loginTypeTv, printerTv, balanceRoundingTv, priceingMethodTv, weightRoundingTv, weightUnitTv;
//    private Button backBtn, saveBtn;
//    private CheckedTextView notClearPriceCtv, saveWeightCtv, autoObtainCtv, cashEttlementCtv, distinguishCtv, icCardSettlementCtv, stopPrintCtv, noPatchSettlementCtv, autoPrevCtv, cashRoundingCtv, stopCashCtv, stopAlipayCtv, stopweichatpayCtv;
//
//
//    List<Map<String, String>> loginTypeList = new ArrayList<>();
//    List<Map<String, String>> pricingModelList = new ArrayList<>();
//    List<Map<String, String>> printerList = new ArrayList<>();
//    List<Map<String, String>> roundingWeightList = new ArrayList<>();
//    List<Map<String, String>> screenUnitList = new ArrayList<>();
//    List<Map<String, String>> balanceRoundingList = new ArrayList<>();
//    private int loginTypePos = 0, printerPos = 0, balanceRoundingPos = 0, priceingMethodPos = 0, weightRoundingPos = 0, weightUnitPos = 0;
//    LinkedTreeMap valueMap;
//
//    @Override
//    public View setInitView() {
//        rootView = LayoutInflater.from(this).inflate(R.layout.system_settings_activity, null);
//        loginTypeBtn = rootView.findViewById(R.id.system_settings_login_type_btn);
//        printerBtn = rootView.findViewById(R.id.system_settings_printer_btn);
//        balanceRoundingBtn = rootView.findViewById(R.id.system_settings_balance_rounding_btn);
//        priceingMethodBtn = rootView.findViewById(R.id.system_settings_default_priceing_method_btn);
//        weightRoundingBtn = rootView.findViewById(R.id.system_settings_weight_rounding_btn);
//        weightUnitBtn = rootView.findViewById(R.id.system_settings_weight_unit_btn);
//
//        buyerNumberTv = rootView.findViewById(R.id.system_settings_default_buyer_number_tv);
//        sellerNumberTv = rootView.findViewById(R.id.system_settings_default_seller_number_tv);
//
//        loginTypeTv = rootView.findViewById(R.id.system_settings_login_type_tv);
//        printerTv = rootView.findViewById(R.id.system_settings_printer_tv);
//        balanceRoundingTv = rootView.findViewById(R.id.system_settings_balance_rounding_tv);
//        priceingMethodTv = rootView.findViewById(R.id.system_settings_default_priceing_method_tv);
//        weightRoundingTv = rootView.findViewById(R.id.system_settings_weight_rounding_tv);
//        weightUnitTv = rootView.findViewById(R.id.system_settings_weight_unit_tv);
//
//        notClearPriceCtv = rootView.findViewById(R.id.system_settings_not_clear_price_ctv);
//        saveWeightCtv = rootView.findViewById(R.id.system_settings_save_weight_ctv);
//        autoObtainCtv = rootView.findViewById(R.id.system_settings_auto_obtain_ctv);
//        cashEttlementCtv = rootView.findViewById(R.id.system_settings_cash_ettlement_ctv);
//        distinguishCtv = rootView.findViewById(R.id.system_settings_distinguish_ctv);
//        icCardSettlementCtv = rootView.findViewById(R.id.system_settings_ic_card_settlement_ctv);
//        stopPrintCtv = rootView.findViewById(R.id.system_settings_stop_print_ctv);
//        noPatchSettlementCtv = rootView.findViewById(R.id.system_settings_no_patch_settlement_ctv);
//        autoPrevCtv = rootView.findViewById(R.id.system_settings_auto_prev_ctv);
//        cashRoundingCtv = rootView.findViewById(R.id.system_settings_cash_rounding_ctv);
//        stopCashCtv = rootView.findViewById(R.id.system_settings_stop_cash_ctv);
//        stopAlipayCtv = rootView.findViewById(R.id.system_settings_stop_alipay_ctv);
//        stopweichatpayCtv = rootView.findViewById(R.id.system_settings_stop_weichatpay_ctv);
//
//        backBtn = rootView.findViewById(R.id.system_settings_back_btn);
//        saveBtn = rootView.findViewById(R.id.system_settings_save_btn);
//
//        loginTypeBtn.setOnClickListener(this);
//        printerBtn.setOnClickListener(this);
//        balanceRoundingBtn.setOnClickListener(this);
//        priceingMethodBtn.setOnClickListener(this);
//        weightRoundingBtn.setOnClickListener(this);
//        weightUnitBtn.setOnClickListener(this);
//        buyerNumberTv.setOnClickListener(this);
//        sellerNumberTv.setOnClickListener(this);
//        backBtn.setOnClickListener(this);
//        saveBtn.setOnClickListener(this);
//
//        notClearPriceCtv.setOnClickListener(this);
//        saveWeightCtv.setOnClickListener(this);
//        autoObtainCtv.setOnClickListener(this);
//        cashEttlementCtv.setOnClickListener(this);
//        distinguishCtv.setOnClickListener(this);
//        stopPrintCtv.setOnClickListener(this);
//        noPatchSettlementCtv.setOnClickListener(this);
//        autoPrevCtv.setOnClickListener(this);
//        cashRoundingCtv.setOnClickListener(this);
//        stopCashCtv.setOnClickListener(this);
//        stopAlipayCtv.setOnClickListener(this);
//        stopweichatpayCtv.setOnClickListener(this);
//        icCardSettlementCtv.setOnClickListener(this);
//
//
//
//        loginTypeTv.setOnClickListener(this);
//
//        return rootView;
//    }
//
//    @Override
//    public void initView() {
//        getSettingData();
//    }
//
//    @Override
//    public void onClick(View v) {
//        ChooseDialog.Builder chooseBuilder = new ChooseDialog.Builder(this);
//        SoftKeyborad.Builder softBuilder = new SoftKeyborad.Builder(this);
//        switch (v.getId()) {
//            case R.id.system_settings_login_type_btn:
//                chooseBuilder.create(loginTypeList, loginTypePos, new ChooseDialog.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        loginTypePos = position;
//                        loginTypeTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(position + 1 + ""));
//                    }
//                }).show();
//                break;
//            case R.id.system_settings_printer_btn:
//                chooseBuilder.create(printerList, printerPos, new ChooseDialog.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        printerPos = position;
//                        printerTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(position + 1 + ""));
//                    }
//                }).show();
//                break;
//            case R.id.system_settings_balance_rounding_btn:
//                chooseBuilder.create(balanceRoundingList, balanceRoundingPos, new ChooseDialog.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        balanceRoundingPos = position;
//                        balanceRoundingTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(position + 1 + ""));
//                    }
//                }).show();
//                break;
//            case R.id.system_settings_default_priceing_method_btn:
//                chooseBuilder.create(pricingModelList, priceingMethodPos, new ChooseDialog.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        priceingMethodPos = position;
//                        SPUtils.put(SystemSettingsActivity.this, SettingsActivity.KET_SWITCH_SIMPLE_OR_COMPLEX, position == 0 ? false : true);
//                        priceingMethodTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(position + 1 + ""));
//                    }
//                }).show();
//                break;
//            case R.id.system_settings_weight_rounding_btn:
//                chooseBuilder.create(roundingWeightList, weightRoundingPos, new ChooseDialog.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        weightRoundingPos = position;
//                        weightRoundingTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(position + 1 + ""));
//                    }
//                }).show();
//                break;
//            case R.id.system_settings_weight_unit_btn:
//                chooseBuilder.create(screenUnitList, weightUnitPos, new ChooseDialog.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        weightUnitPos = position;
//                        weightUnitTv.setText(((Map<String, String>) parent.getAdapter().getItem(position)).get(position + 1 + ""));
//                    }
//                }).show();
//                break;
//            case R.id.system_settings_default_buyer_number_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        buyerNumberTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.system_settings_default_seller_number_tv:
//                if (!NetworkUtil.isConnected(this)) {
//                    Toast.makeText(this, "请先连接网络", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        sellerNumberTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.system_settings_not_clear_price_ctv:
//                notClearPriceCtv.setChecked(!notClearPriceCtv.isChecked());
//                break;
//            case R.id.system_settings_save_weight_ctv:
//                saveWeightCtv.setChecked(!saveWeightCtv.isChecked());
//                break;
//            case R.id.system_settings_auto_obtain_ctv:
//                autoObtainCtv.setChecked(!autoObtainCtv.isChecked());
//                break;
//            case R.id.system_settings_cash_ettlement_ctv:
//                cashEttlementCtv.setChecked(!cashEttlementCtv.isChecked());
//                break;
//            case R.id.system_settings_distinguish_ctv:
//                distinguishCtv.setChecked(!distinguishCtv.isChecked());
//                break;
//            case R.id.system_settings_ic_card_settlement_ctv:
//                icCardSettlementCtv.setChecked(!icCardSettlementCtv.isChecked());
//                break;
//            case R.id.system_settings_stop_print_ctv:
//                stopPrintCtv.setChecked(!stopPrintCtv.isChecked());
//                break;
//            case R.id.system_settings_no_patch_settlement_ctv:
//                noPatchSettlementCtv.setChecked(!noPatchSettlementCtv.isChecked());
//                break;
//            case R.id.system_settings_auto_prev_ctv:
//                autoPrevCtv.setChecked(!autoPrevCtv.isChecked());
//                break;
//            case R.id.system_settings_cash_rounding_ctv:
//                cashRoundingCtv.setChecked(!cashRoundingCtv.isChecked());
//                break;
//            case R.id.system_settings_stop_cash_ctv:
//                stopCashCtv.setChecked(!stopCashCtv.isChecked());
//                break;
//            case R.id.system_settings_stop_alipay_ctv:
//                stopAlipayCtv.setChecked(!stopCashCtv.isChecked());
//                break;
//            case R.id.system_settings_stop_weichatpay_ctv:
//                stopweichatpayCtv.setChecked(!stopweichatpayCtv.isChecked());
//                break;
//            case R.id.system_settings_back_btn:
//                finish();
//                break;
//            case R.id.system_settings_login_type_tv:
//            case R.id.system_settings_save_btn:
//                saveSettingsToSP();
//                break;
//        }
//    }
//
//    public void saveSettingsToSP() {
//
//        String sellerNumber = sellerNumberTv.getText().toString();
//        if (!TextUtils.isEmpty(sellerNumber)) {
//            requestBindSeller(sellerNumber);
//        }
//
//        if (valueMap == null)
//            return;
//
//        LinkedHashMap v = new LinkedHashMap();
//        v.put("update_time", System.currentTimeMillis());
//        v.put("val", loginTypeTv.getText().toString());
//        valueMap.put("default_login_type", v);
//        SPUtils.saveObject(this, KEY_DEFAULT_LOGIN_TYPE, valueMap.get("default_login_type"));
//
//
//        LinkedHashMap printerMap = new LinkedHashMap();
//        printerMap.put("update_time", System.currentTimeMillis());
//        printerMap.put("val", printerTv.getText().toString());
//        valueMap.put("printer_configuration", printerMap);
//        SPUtils.saveObject(this, KEY_PRINTER, valueMap.get("printer_configuration"));
//
//        LinkedHashMap buyerNumberMap = new LinkedHashMap();
//        buyerNumberMap.put("update_time", System.currentTimeMillis());
//        buyerNumberMap.put("val", buyerNumberTv.getText().toString());
//        valueMap.put("default_buyer_number", buyerNumberMap);
//        SPUtils.saveObject(this, KEY_BUYER_NUMBER, valueMap.get("default_buyer_number"));
//
//        LinkedHashMap balanceRoundingMap = new LinkedHashMap();
//        balanceRoundingMap.put("update_time", System.currentTimeMillis());
//        balanceRoundingMap.put("val", balanceRoundingTv.getText().toString());
//        valueMap.put("balance_rounding", balanceRoundingMap);
//        SPUtils.saveObject(this, KEY_BALANCE_ROUNDING, valueMap.get("balance_rounding"));
//
//        LinkedHashMap priceingMethodMap = new LinkedHashMap();
//        priceingMethodMap.put("update_time", System.currentTimeMillis());
//        priceingMethodMap.put("val", priceingMethodTv.getText().toString());
//        valueMap.put("default_pricing_model", priceingMethodMap);
//        SPUtils.saveObject(this, KEY_PRICEING_METHOD, valueMap.get("default_pricing_model"));
//
//
//        LinkedHashMap weightRoundingMap = new LinkedHashMap();
//        weightRoundingMap.put("update_time", System.currentTimeMillis());
//        weightRoundingMap.put("val", weightRoundingTv.getText().toString());
//        valueMap.put("rounding_weight", weightRoundingMap);
//        SPUtils.saveObject(this, KEY_WEIGHT_ROUNDING, valueMap.get("rounding_weight"));
//
//        LinkedHashMap sellerNumberMap = new LinkedHashMap();
//        sellerNumberMap.put("update_time", System.currentTimeMillis());
//        sellerNumberMap.put("val", sellerNumber);
//        valueMap.put("default_seller_number", sellerNumberMap);
//        SPUtils.saveObject(this, KEY_SELLER_NUMBER, valueMap.get("default_seller_number"));
//
//        LinkedHashMap weightUnitMap = new LinkedHashMap();
//        weightUnitMap.put("update_time", System.currentTimeMillis());
//        weightUnitMap.put("val", weightUnitTv.getText().toString());
//        valueMap.put("screen_unit_display", weightUnitMap);
//        SPUtils.saveObject(this, KEY_WEIGHT_UNIT, valueMap.get("screen_unit_display"));
//
//        LinkedHashMap notClearPriceMap = new LinkedHashMap();
//        notClearPriceMap.put("update_time", System.currentTimeMillis());
//        notClearPriceMap.put("val", notClearPriceCtv.isChecked());
//        valueMap.put("price_after_saving", notClearPriceMap);
//        SPUtils.saveObject(this, KEY_NOT_CLEAR, valueMap.get("price_after_saving"));
//
//        LinkedHashMap saveWeightMap = new LinkedHashMap();
//        saveWeightMap.put("update_time", System.currentTimeMillis());
//        saveWeightMap.put("val", saveWeightCtv.isChecked());
//        valueMap.put("confirm_the_preservation", saveWeightMap);
//        SPUtils.saveObject(this, KEY_SAVE_WEIGHT, valueMap.get("confirm_the_preservation"));
//
//        LinkedHashMap autoObtainMap = new LinkedHashMap();
//        autoObtainMap.put("update_time", System.currentTimeMillis());
//        autoObtainMap.put("val", autoObtainCtv.isChecked());
//        valueMap.put("buyers_and_sellers_by_default", autoObtainMap);
//        SPUtils.saveObject(this, KEY_AUTO_OBTAIN, valueMap.get("buyers_and_sellers_by_default"));
//
//        LinkedHashMap cashEttlementMap = new LinkedHashMap();
//        cashEttlementMap.put("update_time", System.currentTimeMillis());
//        cashEttlementMap.put("val", cashEttlementCtv.isChecked());
//        valueMap.put("online_settlement", cashEttlementMap);
//        SPUtils.saveObject(this, KEY_CASH_SETTLEMENT, valueMap.get("online_settlement"));
//
//        LinkedHashMap distinguishMap = new LinkedHashMap();
//        distinguishMap.put("update_time", System.currentTimeMillis());
//        distinguishMap.put("val", distinguishCtv.isChecked());
//        valueMap.put("buyers_and_sellers_after_weighing", distinguishMap);
//        SPUtils.saveObject(this, KEY_DISTINGUISH, valueMap.get("buyers_and_sellers_after_weighing"));
//
//        LinkedHashMap icCardSettlementMap = new LinkedHashMap();
//        icCardSettlementMap.put("update_time", System.currentTimeMillis());
//        icCardSettlementMap.put("val", icCardSettlementCtv.isChecked());
//        valueMap.put("card_settlement", icCardSettlementMap);
//        SPUtils.saveObject(this, KEY_ICCARD_SETTLEMENT, valueMap.get("card_settlement"));
//
//        LinkedHashMap stopPrintMap = new LinkedHashMap();
//        stopPrintMap.put("update_time", System.currentTimeMillis());
//        stopPrintMap.put("val", stopPrintCtv.isChecked());
//        valueMap.put("disable_printing", stopPrintMap);
//        SPUtils.saveObject(this, KEY_STOP_PRINT, valueMap.get("disable_printing"));
//
//        LinkedHashMap noPatchSettlementMap = new LinkedHashMap();
//        noPatchSettlementMap.put("update_time", System.currentTimeMillis());
//        noPatchSettlementMap.put("val", noPatchSettlementCtv.isChecked());
//        valueMap.put("allow_batchless_settlement", noPatchSettlementMap);
//        SPUtils.saveObject(this, KEY_NO_PATCH_SETTLEMENT, valueMap.get("allow_batchless_settlement"));
//
//        LinkedHashMap autoPrevMap = new LinkedHashMap();
//        autoPrevMap.put("update_time", System.currentTimeMillis());
//        autoPrevMap.put("val", autoPrevCtv.isChecked());
//        valueMap.put("take_a_unit_price", autoPrevMap);
//        SPUtils.saveObject(this, KEY_AUTO_PREV, valueMap.get("take_a_unit_price"));
//
//        LinkedHashMap cashRoundingMap = new LinkedHashMap();
//        cashRoundingMap.put("update_time", System.currentTimeMillis());
//        cashRoundingMap.put("val", cashRoundingCtv.isChecked());
//        valueMap.put("cash_change_rounding", cashRoundingMap);
//        SPUtils.saveObject(this, KEY_CASH_ROUNDING, valueMap.get("cash_change_rounding"));
//
//        LinkedHashMap stopCashMap = new LinkedHashMap();
//        stopCashMap.put("update_time", System.currentTimeMillis());
//        stopCashMap.put("val", stopCashCtv.isChecked());
//        valueMap.put("disable_cash_mode", stopCashMap);
//        SPUtils.saveObject(this, KEY_STOP_CASH, valueMap.get("disable_cash_mode"));
//
//        showLoading("保存成功");
//    }
//
//    private void requestBindSeller(String sellerNumber) {
//        RetrofitFactory.getInstance().API()
//                .fastLogin(AccountManager.getInstance().getScalesId(), sellerNumber)
//                .compose(this.<BaseEntity<FastLoginInfo>>setThread())
//                .subscribe(new Observer<BaseEntity<FastLoginInfo>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(final BaseEntity<FastLoginInfo> fastLoginInfo) {
//                        AccountManager instance = AccountManager.getInstance();
//                        boolean success = fastLoginInfo.isSuccess();
//                        if (success) {
//                            Toast.makeText(SystemSettingsActivity.this,"绑定卖家成功",Toast.LENGTH_SHORT).show();
//                            instance.saveToken(fastLoginInfo.getData().getToken());
//                        }else{
////                            instance.saveToken(AccountManager.getInstance().getAdminToken());
//                            Toast.makeText(SystemSettingsActivity.this,"绑定卖家失败",Toast.LENGTH_SHORT).show();
//                        }
//                        EventBus.getDefault().post(new BusEvent(BusEvent.notifiySellerInfo,success));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//
//    }
//
//    public void getSettingData() {
//        String mac = MacManager.getInstace(this).getMac();
//        mac = "10:d0:7a:6f:23:23";
//        RetrofitFactory.getInstance().API()
//                .getSettingData(AccountManager.getInstance().getAdminToken(), mac)
//                .compose(this.<BaseEntity>setThread())
//                .subscribe(new Observer<BaseEntity>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        showLoading();
//
//                    }
//
//                    @Override
//                    public void onNext(BaseEntity settingDataBeanBaseEntity) {
//                        boolean isSu = settingDataBeanBaseEntity.isSuccess();
//                        if (settingDataBeanBaseEntity.isSuccess()) {
//                            LinkedTreeMap map = (LinkedTreeMap) settingDataBeanBaseEntity.getData();
//
//                            loginTypeList.addAll((Collection<? extends Map<String, String>>) map.get("default_login_type"));
//                            pricingModelList.addAll((Collection<? extends Map<String, String>>) map.get("default_pricing_model"));
//                            printerList.addAll((Collection<? extends Map<String, String>>) map.get("printer_configuration"));
//                            roundingWeightList.addAll((Collection<? extends Map<String, String>>) map.get("rounding_weight"));
//                            screenUnitList.addAll((Collection<? extends Map<String, String>>) map.get("screen_unit_display"));
//                            balanceRoundingList.addAll((Collection<? extends Map<String, String>>) map.get("balance_rounding"));
//                            valueMap = (LinkedTreeMap) ((LinkedTreeMap) settingDataBeanBaseEntity.getData()).get("value");
//
//                            LinkedHashMap saveMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_DEFAULT_LOGIN_TYPE);
//                            if (saveMap != null) {
//                                Long loginDate = (Long) saveMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("default_login_type")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    loginTypeTv.setText((saveMap.get("val")).toString());
//                                } else {
//                                    loginTypeTv.setText((((LinkedTreeMap) valueMap.get("default_login_type")).get("val")).toString());
//                                }
//                            } else {
//                                loginTypeTv.setText((((LinkedTreeMap) valueMap.get("default_login_type")).get("val")) != null ? (((LinkedTreeMap) valueMap.get("default_login_type")).get("val")).toString() : "");
//                            }
//                            LinkedHashMap printerMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_PRINTER);
//                            if (printerMap != null) {
//                                Long loginDate = (Long) printerMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("printer_configuration")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    printerTv.setText((printerMap.get("val")).toString());
//                                } else {
//                                    printerTv.setText((((LinkedTreeMap) valueMap.get("printer_configuration")).get("val")).toString());
//                                }
//                            } else {
//                                printerTv.setText((((LinkedTreeMap) valueMap.get("printer_configuration")).get("val")) != null ? (((LinkedTreeMap) valueMap.get("printer_configuration")).get("val")).toString() : "");
//                            }
//                            LinkedHashMap buyerNumberMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_BUYER_NUMBER);
//                            if (buyerNumberMap != null) {
//                                Long loginDate = (Long) buyerNumberMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("default_buyer_number")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    buyerNumberTv.setText((buyerNumberMap.get("val")).toString());
//                                } else {
//                                    buyerNumberTv.setText((((LinkedTreeMap) valueMap.get("default_buyer_number")).get("val")).toString());
//                                }
//                            } else {
//                                buyerNumberTv.setText((((LinkedTreeMap) valueMap.get("default_buyer_number")).get("val")) != null ? (((LinkedTreeMap) valueMap.get("default_buyer_number")).get("val")).toString() : "");
//                            }
//                            LinkedHashMap balanceRoundingMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_BALANCE_ROUNDING);
//                            if (balanceRoundingMap != null) {
//                                Long loginDate = (Long) saveMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("balance_rounding")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    balanceRoundingTv.setText((balanceRoundingMap.get("val")).toString());
//                                } else {
//                                    balanceRoundingTv.setText(((LinkedTreeMap) valueMap.get("balance_rounding")).get("val").toString());
//                                }
//                            } else {
//                                balanceRoundingTv.setText(((LinkedTreeMap) valueMap.get("balance_rounding")).get("val") != null ? ((LinkedTreeMap) valueMap.get("balance_rounding")).get("val").toString() : "");
//                            }
//                            LinkedHashMap priceingMethodMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_PRICEING_METHOD);
//                            if (priceingMethodMap != null) {
//                                Long loginDate = (Long) priceingMethodMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("default_pricing_model")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    priceingMethodTv.setText((priceingMethodMap.get("val")).toString());
//                                } else {
//                                    priceingMethodTv.setText(((LinkedTreeMap) valueMap.get("default_pricing_model")).get("val").toString());
//                                }
//                            } else {
//                                priceingMethodTv.setText(((LinkedTreeMap) valueMap.get("default_pricing_model")).get("val") != null ? ((LinkedTreeMap) valueMap.get("default_pricing_model")).get("val").toString() : "");
//                            }
//                            LinkedHashMap weightRoundingMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_WEIGHT_ROUNDING);
//                            if (weightRoundingMap != null) {
//                                Long loginDate = (Long) weightRoundingMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("rounding_weight")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    weightRoundingTv.setText((weightRoundingMap.get("val")).toString());
//                                } else {
//                                    weightRoundingTv.setText(((LinkedTreeMap) valueMap.get("rounding_weight")).get("val").toString());
//                                }
//                            } else {
//                                weightRoundingTv.setText(((LinkedTreeMap) valueMap.get("rounding_weight")).get("val") != null ? ((LinkedTreeMap) valueMap.get("rounding_weight")).get("val").toString() : "");
//                            }
//                            LinkedHashMap sellerNumberMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_SELLER_NUMBER);
//                            if (sellerNumberMap != null) {
//                                Long loginDate = (Long) sellerNumberMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("default_seller_number")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    sellerNumberTv.setText((sellerNumberMap.get("val")).toString());
//                                } else {
//                                    sellerNumberTv.setText(((LinkedTreeMap) valueMap.get("default_seller_number")).get("val").toString());
//                                }
//                            } else {
//                                sellerNumberTv.setText(((LinkedTreeMap) valueMap.get("default_seller_number")).get("val") != null ? ((LinkedTreeMap) valueMap.get("default_seller_number")).get("val").toString() : "");
//                            }
//                            LinkedHashMap weightUnitMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_WEIGHT_UNIT);
//                            if (weightUnitMap != null) {
//                                Long loginDate = (Long) weightUnitMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("screen_unit_display")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    weightUnitTv.setText((weightUnitMap.get("val")).toString());
//                                } else {
//                                    weightUnitTv.setText(((LinkedTreeMap) valueMap.get("screen_unit_display")).get("val").toString());
//                                }
//                            } else {
//                                weightUnitTv.setText(((LinkedTreeMap) valueMap.get("screen_unit_display")).get("val") != null ? ((LinkedTreeMap) valueMap.get("screen_unit_display")).get("val").toString() : "");
//                            }
//                            LinkedHashMap notClearPriceMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_NOT_CLEAR);
//                            if (notClearPriceMap != null) {
//                                Long loginDate = (Long) notClearPriceMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("price_after_saving")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    notClearPriceCtv.setChecked((Boolean) notClearPriceMap.get("val"));
//                                } else {
//                                    notClearPriceCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("price_after_saving")).get("val"));
//                                }
//                            } else {
//                                notClearPriceCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("price_after_saving")).get("val"));
//                            }
//
//                            LinkedHashMap saveWeightMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_SAVE_WEIGHT);
//                            if (saveWeightMap != null) {
//                                Long loginDate = (Long) saveWeightMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("confirm_the_preservation")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    saveWeightCtv.setChecked((Boolean) saveWeightMap.get("val"));
//                                } else {
//                                    saveWeightCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("confirm_the_preservation")).get("val"));
//                                }
//                            } else {
//                                saveWeightCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("confirm_the_preservation")).get("val"));
//                            }
//                            LinkedHashMap autoObtainMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_AUTO_OBTAIN);
//                            if (autoObtainMap != null) {
//                                Long loginDate = (Long) autoObtainMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("buyers_and_sellers_by_default")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    autoObtainCtv.setChecked((Boolean) autoObtainMap.get("val"));
//                                } else {
//                                    autoObtainCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("buyers_and_sellers_by_default")).get("val"));
//                                }
//                            } else {
//                                autoObtainCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("buyers_and_sellers_by_default")).get("val"));
//                            }
//                            LinkedHashMap cashEttlementMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_CASH_SETTLEMENT);
//                            if (cashEttlementMap != null) {
//                                Long loginDate = (Long) cashEttlementMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("online_settlement")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    cashEttlementCtv.setChecked((Boolean) cashEttlementMap.get("val"));
//                                } else {
//                                    cashEttlementCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("online_settlement")).get("val"));
//                                }
//                            } else {
//                                cashEttlementCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("online_settlement")).get("val"));
//                            }
//                            LinkedHashMap distinguishMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_DISTINGUISH);
//                            if (distinguishMap != null) {
//                                Long loginDate = (Long) distinguishMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("buyers_and_sellers_after_weighing")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    distinguishCtv.setChecked((Boolean) distinguishMap.get("val"));
//                                } else {
//                                    distinguishCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("buyers_and_sellers_after_weighing")).get("val"));
//                                }
//                            } else {
//                                distinguishCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("buyers_and_sellers_after_weighing")).get("val"));
//                            }
//                            LinkedHashMap icCardSettlementMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_ICCARD_SETTLEMENT);
//                            if (icCardSettlementMap != null) {
//                                Long loginDate = (Long) icCardSettlementMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("card_settlement")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    icCardSettlementCtv.setChecked((Boolean) icCardSettlementMap.get("val"));
//                                } else {
//                                    icCardSettlementCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("card_settlement")).get("val"));
//                                }
//                            } else {
//                                icCardSettlementCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("card_settlement")).get("val"));
//                            }
//                            LinkedHashMap stopPrintMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_STOP_PRINT);
//                            if (stopPrintMap != null) {
//                                Long loginDate = (Long) stopPrintMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("disable_printing")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    stopPrintCtv.setChecked((Boolean) stopPrintMap.get("val"));
//                                } else {
//                                    stopPrintCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("disable_printing")).get("val"));
//                                }
//                            } else {
//                                stopPrintCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("disable_printing")).get("val"));
//                            }
//                            LinkedHashMap noPatchSettlementMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_NO_PATCH_SETTLEMENT);
//                            if (noPatchSettlementMap != null) {
//                                Long loginDate = (Long) noPatchSettlementMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("allow_batchless_settlement")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    noPatchSettlementCtv.setChecked((Boolean) noPatchSettlementMap.get("val"));
//                                } else {
//                                    noPatchSettlementCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("allow_batchless_settlement")).get("val"));
//                                }
//                            } else {
//                                noPatchSettlementCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("allow_batchless_settlement")).get("val"));
//                            }
//                            LinkedHashMap autoPrevMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_AUTO_PREV);
//                            if (autoPrevMap != null) {
//                                Long loginDate = (Long) autoPrevMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("take_a_unit_price")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    autoPrevCtv.setChecked((Boolean) autoPrevMap.get("val"));
//                                } else {
//                                    autoPrevCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("take_a_unit_price")).get("val"));
//                                }
//                            } else {
//                                autoPrevCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("take_a_unit_price")).get("val"));
//                            }
//                            LinkedHashMap cashRoundingMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_CASH_ROUNDING);
//                            if (cashRoundingMap != null) {
//                                Long loginDate = (Long) cashRoundingMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("cash_change_rounding")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    cashRoundingCtv.setChecked((Boolean) cashRoundingMap.get("val"));
//                                } else {
//                                    cashRoundingCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("cash_change_rounding")).get("val"));
//                                }
//                            } else {
//                                cashRoundingCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("cash_change_rounding")).get("val"));
//                            }
//                            LinkedHashMap stopCashMap = (LinkedHashMap) SPUtils.readObject(SystemSettingsActivity.this, KEY_STOP_CASH);
//                            if (stopCashMap != null) {
//                                Long loginDate = (Long) stopCashMap.get("update_time");
//                                Long valueDate = ((Double) ((LinkedTreeMap) valueMap.get("disable_cash_mode")).get("update_time")).longValue();
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    stopCashCtv.setChecked((Boolean) stopCashMap.get("val"));
//                                } else {
//                                    stopCashCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("disable_cash_mode")).get("val"));
//                                }
//                            } else {
//                                stopCashCtv.setChecked((Boolean) ((LinkedTreeMap) valueMap.get("disable_cash_mode")).get("val"));
//                            }
//
//                        } else {
//                            showLoading(settingDataBeanBaseEntity.getMsg());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        closeLoading();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        closeLoading();
//                    }
//                });
//
////                .getSettingData(MacManager.getInstace(this).getMac())
//
//    }
//}
//
