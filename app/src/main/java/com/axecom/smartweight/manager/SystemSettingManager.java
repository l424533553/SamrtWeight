package com.axecom.smartweight.manager;

import com.axecom.smartweight.base.BaseActivity;
import com.axecom.smartweight.base.BaseEntity;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.net.RetrofitFactory;
import com.axecom.smartweight.utils.FileUtils;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by andy on 2018/10/10.
 */

public class SystemSettingManager {

    private static final String SYS_SETTING = "sys_setting";
    private static LinkedHashMap mSetting;

    public static void getSettingData(final BaseActivity context) {
        mSetting = (LinkedHashMap) FileUtils.readObject(context, SYS_SETTING);
        if (mSetting != null) {
            return;
        }
        requestData(context);
    }

    private static void requestData(final BaseActivity context) {
        RetrofitFactory.getInstance().API()
                .getSettingData("", MacManager.getInstace(context).getMac())
                .compose(context.<BaseEntity>setThread())
                .subscribe(new Observer<BaseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseEntity settingDataBeanBaseEntity) {
                        if (settingDataBeanBaseEntity.isSuccess()) {
                            LinkedTreeMap map = (LinkedTreeMap) settingDataBeanBaseEntity.getData();
                            FileUtils.saveObject(SysApplication.getContext(), map, SYS_SETTING);
                            mSetting = null;
                            mSetting = (LinkedHashMap) FileUtils.readObject(context, SYS_SETTING);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static String default_login_type() {
        LinkedHashMap map = getValueMap("default_login_type");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val == null ? "" : val.toString();
        }
        return "";
    }

    public static String getOptionName(List<Map<String, String>> defaultList,String key){
        HashMap<String,String> mapSet = new HashMap<>();
        for(Map<String,String> map :defaultList){
            for(String kk:map.keySet()){
                mapSet.put(kk,map.get(kk));
            }
        }
        String name = mapSet.get(key);
        if(name!=null){
            return name;
        }
        return "";
    }

    public static void default_login_type_save(String type) {
        LinkedHashMap map = getValueMap("default_login_type");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", type);
        }
    }

        public static boolean disable_cash_mode() {
        LinkedHashMap map = getValueMap("disable_cash_mode");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void disable_cash_mode_save(boolean b) {
        LinkedHashMap map = getValueMap("disable_cash_mode");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }

    //    自动取上一笔单价
    public static boolean take_a_unit_price() {
        LinkedHashMap map = getValueMap("take_a_unit_price");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void take_a_unit_price_save(boolean b) {
        LinkedHashMap map = getValueMap("take_a_unit_price");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }

    public static String printer_configuration() {
        LinkedHashMap map = getValueMap("printer_configuration");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val == null ? "" : val.toString();
        }
        return "";
    }

    public static void printer_configuration_save(String con) {
        LinkedHashMap map = getValueMap("printer_configuration");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", con);
        }
    }

    //    默认买方编号
    public static String default_buyer_number() {
        LinkedHashMap map = getValueMap("default_buyer_number");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val == null ? "" : val.toString();
        }
        return "";
    }

    public static void default_buyer_number_save(String number) {
        LinkedHashMap map = getValueMap("default_buyer_number");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", number);
        }
    }

    //    余额取整
    public static String balance_rounding() {
        LinkedHashMap map = getValueMap("balance_rounding");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val == null ? "" : val.toString();
        }
        return "";
    }

    public static void balance_rounding_save(String type) {
        LinkedHashMap map = getValueMap("balance_rounding");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", type);
        }
    }


    //    默认计价模式
    public static String default_pricing_model() {
        LinkedHashMap map = getValueMap("default_pricing_model");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val == null ? "" : val.toString();
        }
        return "";
    }

    public static void default_pricing_model_save(String type) {
        LinkedHashMap map = getValueMap("default_pricing_model");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", type);
        }
    }


    //    重量取整
    public static String rounding_weight() {
        LinkedHashMap map = getValueMap("rounding_weight");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val == null ? "" : val.toString();
        }
        return "";
    }

    public static void rounding_weight_save(String type) {
        LinkedHashMap map = getValueMap("rounding_weight");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", type);
        }
    }


    public static String default_seller_number() {
        LinkedHashMap map = getValueMap("default_seller_number");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val == null ? "" : val.toString();
        }
        return "";
    }

    public static void default_seller_number_save(String type) {
        LinkedHashMap map = getValueMap("default_seller_number");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", type);
        }
    }

    public static String screen_unit_display() {
        LinkedHashMap map = getValueMap("screen_unit_display");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val == null ? "" : val.toString();
        }
        return "";
    }

    public static void screen_unit_display_save(String type) {
        LinkedHashMap map = getValueMap("screen_unit_display");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", type);
        }
    }

    //    保存后不清空价格
    public static boolean price_after_saving() {
        LinkedHashMap map = getValueMap("price_after_saving");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void price_after_saving_save(boolean b) {
        LinkedHashMap map = getValueMap("price_after_saving");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }


    //    重量稳定后才能确认保存
    public static boolean confirm_the_preservation() {
        LinkedHashMap map = getValueMap("confirm_the_preservation");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void confirm_the_preservation_save(boolean b) {
        LinkedHashMap map = getValueMap("confirm_the_preservation");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }


    //    默认自动获取买卖方
    public static boolean buyers_and_sellers_by_default() {
        LinkedHashMap map = getValueMap("buyers_and_sellers_by_default");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void buyers_and_sellers_by_default_save(boolean b) {
        LinkedHashMap map = getValueMap("buyers_and_sellers_by_default");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }


    //    联机结算(现金结算
    public static boolean online_settlement() {
        LinkedHashMap map = getValueMap("online_settlement");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void online_settlement_save(boolean b) {
        LinkedHashMap map = getValueMap("online_settlement");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }


    //    客户开秤后不区分买卖方
    public static boolean buyers_and_sellers_after_weighing() {
        LinkedHashMap map = getValueMap("buyers_and_sellers_after_weighing");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void buyers_and_sellers_after_weighing_save(boolean b) {
        LinkedHashMap map = getValueMap("buyers_and_sellers_after_weighing");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }


    //    IC卡结算时再刷卡
    public static boolean card_settlement() {
        LinkedHashMap map = getValueMap("card_settlement");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void card_settlement_save(boolean b) {
        LinkedHashMap map = getValueMap("card_settlement");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }


    //    关闭打印
    public static boolean disable_printing() {
        LinkedHashMap map = getValueMap("disable_printing");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void disable_printing_save(boolean b) {
        LinkedHashMap map = getValueMap("disable_printing");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }


    //    允许无批次结算
    public static boolean allow_batchless_settlement() {
        LinkedHashMap map = getValueMap("allow_batchless_settlement");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void allow_batchless_settlement_save(boolean b) {
        LinkedHashMap map = getValueMap("allow_batchless_settlement");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }

    //    现金四舍五入
    public static boolean cash_change_rounding() {
        LinkedHashMap map = getValueMap("cash_change_rounding");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void cash_change_rounding_save(boolean b) {
        LinkedHashMap map = getValueMap("cash_change_rounding");
        if (map != null) {
            Long valueDate = ((Double) map.get("update_time")).longValue();
            map.put("val", b);
        }
    }

    //停用支付宝支付
    public static boolean disable_alipay_mode() {
        LinkedHashMap map = getValueMap("disable_alipay_mode");
        if (map != null) {
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void disable_alipay_mode_save(boolean b) {
        String key = "disable_alipay_mode";

        LinkedHashMap map = getValueMap(key);
        if (map == null) {
            map = new LinkedHashMap<>();
            putValueMap(key, map);
        }
        if (map != null) {
            map.put("val", b);
        }
    }

    //    停用微信支付
    public static boolean disable_weixin_mode() {
        LinkedHashMap map = getValueMap("disable_weixin_mode");
        if (map != null) {
            Object val = map.get("val");
            return val != null && (boolean) val;
        }
        return false;
    }

    public static void disable_weixin_mode_save(boolean b) {
        String key = "disable_weixin_mode";
        LinkedHashMap map = getValueMap(key);
        if (map == null) {
            map = new LinkedHashMap<>();
            putValueMap(key, map);
        }
        if (map != null) {
            map.put("val", b);
        }
    }


    public static List<Map<String, String>> getLoginTypeList() {
        String key = "default_login_type";
        return getOptionList(key);
    }

    public static List<Map<String, String>> getDefaultPricingTypeList() {
        String key = "default_pricing_model";
        return getOptionList(key);
    }

    public static List<Map<String, String>> getPrinterConfigurationList() {
        String key = "printer_configuration";
        return getOptionList(key);
    }

    public static List<Map<String, String>> getRoundingWeightOptions() {
        String key = "rounding_weight";
        return getOptionList(key);
    }

    public static List<Map<String, String>> getScreenUnitDisplayOptions() {
        String key = "screen_unit_display";
        return getOptionList(key);
    }

    public static List<Map<String, String>> getBalanceRoundingOptions() {
        String key = "balance_rounding";
        return getOptionList(key);
    }

    public static List<Map<String, String>> getOptionList(String key) {
        List<Map<String, String>> array = new ArrayList<Map<String, String>>();
        LinkedHashMap setting = getmSetting();
        if (setting != null) {
            array.addAll((Collection<? extends Map<String, String>>) setting.get(key));
        }
        return array;
    }

    private static LinkedHashMap getValueMap(String key) {
        if (mSetting != null) {
            LinkedHashMap valueMap = (LinkedHashMap) mSetting.get("value");
            return (LinkedHashMap) valueMap.get(key);
        }
        return null;
    }

    private static void putValueMap(String key, Object obj) {
        if (mSetting != null) {
            LinkedHashMap valueMap = (LinkedHashMap) mSetting.get("value");
            valueMap.put(key, obj);
        }
    }

    public static LinkedHashMap getmSetting() {
//        if(mSetting==null){}
        return mSetting;
    }

    private static void save() {
        FileUtils.saveObject(SysApplication.getContext(), mSetting, SYS_SETTING);
    }

    public static void updateData(BaseActivity activity) {
        mSetting = null;
        requestData(activity);
    }
}
