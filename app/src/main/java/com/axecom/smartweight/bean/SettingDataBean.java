package com.axecom.smartweight.bean;

import com.axecom.smartweight.base.BaseEntity;

import java.util.List;

public class SettingDataBean<T> extends BaseEntity{
    public List<T> default_login_type;
    public List<T> default_pricing_model;
    public List<T> printer_configuration;
    public List<T> rounding_weight;
    public List<T> screen_unit_display;
    public List<T> balance_rounding;
    public Value value;

    public List<T> getDefault_login_type() {
        return default_login_type;
    }

    public void setDefault_login_type(List<T> default_login_type) {
        this.default_login_type = default_login_type;
    }


    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public List<T> getDefault_pricing_model() {
        return default_pricing_model;
    }

    public void setDefault_pricing_model(List<T> default_pricing_model) {
        this.default_pricing_model = default_pricing_model;
    }

    public List<T> getPrinter_configuration() {
        return printer_configuration;
    }

    public void setPrinter_configuration(List<T> printer_configuration) {
        this.printer_configuration = printer_configuration;
    }

    public List<T> getRounding_weight() {
        return rounding_weight;
    }

    public void setRounding_weight(List<T> rounding_weight) {
        this.rounding_weight = rounding_weight;
    }

    public List<T> getScreen_unit_display() {
        return screen_unit_display;
    }

    public void setScreen_unit_display(List<T> screen_unit_display) {
        this.screen_unit_display = screen_unit_display;
    }

    public List<T> getBalance_rounding() {
        return balance_rounding;
    }

    public void setBalance_rounding(List<T> balance_rounding) {
        this.balance_rounding = balance_rounding;
    }

    class Value<T>{
        public DefaultLoginType default_login_type;
        public DefaultPricingModel default_pricing_model;
        public PrinterConfiguration printer_configuration;
        public RoundingWeight rounding_weight;
        public DefaultVariety default_variety;
        public ScreenUnitDisplay screen_unit_display;
        public DefaultBuyerNumber default_buyer_number;
        public DefaultSellerNumber default_seller_number;
        public BalanceRounding balance_rounding;
        public PriceAfterSaving price_after_saving;
        public ConfirmThePreservation confirm_the_preservation;
        public BuyersAndSellersByDefault buyers_and_sellers_by_default;
        public OnlineSettlement online_settlement;
        public BuyersAndSellersAfterWeighing buyers_and_sellers_after_weighing;
        public CardSettlement card_settlement;
        public DisablePrinting disable_printing;
        public AllowBatchlessSettlement allow_batchless_settlement;
        public TakeaUnitPrice take_a_unit_price;
        public CashChangeRounding cash_change_rounding;
        public DisableCashMode disable_cash_mode;

        public DefaultLoginType getDefault_login_type() {
            return default_login_type;
        }

        public void setDefault_login_type(DefaultLoginType default_login_type) {
            this.default_login_type = default_login_type;
        }

        public DefaultPricingModel getDefault_pricing_model() {
            return default_pricing_model;
        }

        public void setDefault_pricing_model(DefaultPricingModel default_pricing_model) {
            this.default_pricing_model = default_pricing_model;
        }

        public PrinterConfiguration getPrinter_configuration() {
            return printer_configuration;
        }

        public void setPrinter_configuration(PrinterConfiguration printer_configuration) {
            this.printer_configuration = printer_configuration;
        }

        public RoundingWeight getRounding_weight() {
            return rounding_weight;
        }

        public void setRounding_weight(RoundingWeight rounding_weight) {
            this.rounding_weight = rounding_weight;
        }

        public DefaultVariety getDefault_variety() {
            return default_variety;
        }

        public void setDefault_variety(DefaultVariety default_variety) {
            this.default_variety = default_variety;
        }

        public ScreenUnitDisplay getScreen_unit_display() {
            return screen_unit_display;
        }

        public void setScreen_unit_display(ScreenUnitDisplay screen_unit_display) {
            this.screen_unit_display = screen_unit_display;
        }

        public DefaultBuyerNumber getDefault_buyer_number() {
            return default_buyer_number;
        }

        public void setDefault_buyer_number(DefaultBuyerNumber default_buyer_number) {
            this.default_buyer_number = default_buyer_number;
        }

        public DefaultSellerNumber getDefault_seller_number() {
            return default_seller_number;
        }

        public void setDefault_seller_number(DefaultSellerNumber default_seller_number) {
            this.default_seller_number = default_seller_number;
        }

        public BalanceRounding getBalance_rounding() {
            return balance_rounding;
        }

        public void setBalance_rounding(BalanceRounding balance_rounding) {
            this.balance_rounding = balance_rounding;
        }

        public PriceAfterSaving getPrice_after_saving() {
            return price_after_saving;
        }

        public void setPrice_after_saving(PriceAfterSaving price_after_saving) {
            this.price_after_saving = price_after_saving;
        }

        public ConfirmThePreservation getConfirm_the_preservation() {
            return confirm_the_preservation;
        }

        public void setConfirm_the_preservation(ConfirmThePreservation confirm_the_preservation) {
            this.confirm_the_preservation = confirm_the_preservation;
        }

        public BuyersAndSellersByDefault getBuyers_and_sellers_by_default() {
            return buyers_and_sellers_by_default;
        }

        public void setBuyers_and_sellers_by_default(BuyersAndSellersByDefault buyers_and_sellers_by_default) {
            this.buyers_and_sellers_by_default = buyers_and_sellers_by_default;
        }

        public OnlineSettlement getOnline_settlement() {
            return online_settlement;
        }

        public void setOnline_settlement(OnlineSettlement online_settlement) {
            this.online_settlement = online_settlement;
        }

        public BuyersAndSellersAfterWeighing getBuyers_and_sellers_after_weighing() {
            return buyers_and_sellers_after_weighing;
        }

        public void setBuyers_and_sellers_after_weighing(BuyersAndSellersAfterWeighing buyers_and_sellers_after_weighing) {
            this.buyers_and_sellers_after_weighing = buyers_and_sellers_after_weighing;
        }

        public CardSettlement getCard_settlement() {
            return card_settlement;
        }

        public void setCard_settlement(CardSettlement card_settlement) {
            this.card_settlement = card_settlement;
        }

        public DisablePrinting getDisable_printing() {
            return disable_printing;
        }

        public void setDisable_printing(DisablePrinting disable_printing) {
            this.disable_printing = disable_printing;
        }

        public AllowBatchlessSettlement getAllow_batchless_settlement() {
            return allow_batchless_settlement;
        }

        public void setAllow_batchless_settlement(AllowBatchlessSettlement allow_batchless_settlement) {
            this.allow_batchless_settlement = allow_batchless_settlement;
        }

        public TakeaUnitPrice getTake_a_unit_price() {
            return take_a_unit_price;
        }

        public void setTake_a_unit_price(TakeaUnitPrice take_a_unit_price) {
            this.take_a_unit_price = take_a_unit_price;
        }

        public CashChangeRounding getCash_change_rounding() {
            return cash_change_rounding;
        }

        public void setCash_change_rounding(CashChangeRounding cash_change_rounding) {
            this.cash_change_rounding = cash_change_rounding;
        }

        public DisableCashMode getDisable_cash_mode() {
            return disable_cash_mode;
        }

        public void setDisable_cash_mode(DisableCashMode disable_cash_mode) {
            this.disable_cash_mode = disable_cash_mode;
        }


        class DefaultLoginType{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class DefaultPricingModel{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class PrinterConfiguration{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class RoundingWeight{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class DefaultVariety{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class ScreenUnitDisplay{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class DefaultBuyerNumber{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class DefaultSellerNumber{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class BalanceRounding{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }
        class PriceAfterSaving{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }

        class ConfirmThePreservation{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }

        class BuyersAndSellersByDefault{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }

        class OnlineSettlement{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }

        class BuyersAndSellersAfterWeighing{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }

        class CardSettlement{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }


        class DisablePrinting{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }


        class AllowBatchlessSettlement{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }


        class TakeaUnitPrice{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }


        class CashChangeRounding{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }


        class DisableCashMode{
            private int val;
            private int update_time;

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }
        }


    }

}
