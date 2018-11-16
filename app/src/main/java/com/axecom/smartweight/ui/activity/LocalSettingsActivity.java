//package com.axecom.smartweight.ui.activity;
//
//import android.content.Intent;
//import android.hardware.usb.UsbManager;
//import android.provider.Settings;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.axecom.smartweight.R;
//import com.axecom.smartweight.base.BaseActivity;
//import com.axecom.smartweight.base.BaseEntity;
//import com.axecom.smartweight.base.BusEvent;
//import com.axecom.smartweight.bean.ChooseBean;
//import com.axecom.smartweight.bean.LocalSettingsBean;
//import com.axecom.smartweight.conf.Constants;
//import com.axecom.smartweight.manager.AccountManager;
//import com.axecom.smartweight.manager.MacManager;
//import com.axecom.smartweight.net.RetrofitFactory;
//import com.axecom.smartweight.ui.view.ChooseDialog2;
//import com.axecom.smartweight.ui.view.SoftKeyborad;
//import com.axecom.smartweight.utils.NetworkUtil;
//import com.axecom.smartweight.utils.SPUtils;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//
//import static com.shangtongyin.tools.serialport.IConstants_ST.IS_RE_BOOT;
//
//
//public class LocalSettingsActivity extends BaseActivity {
//
//    public static final String KEY_PRINTER_PORT = "printer_port";
//    public static final String KEY_PRINTER_COUNT = "printer_count";
//    public static final String KEY_CLEAR_DATA = "clear_data";
//    public static final String KEY_READ_CARD_PORT = "read_card_port";
//    public static final String KEY_DATA_DAYS = "data_days";
//    public static final String KEY_SCREEN_SLEEP = "screen_sleep";
//    public static final String KEY_SERVER_IP = "server_ip";
//    public static final String KEY_SVERVER_PORT = "server_port";
//
//    private View rootView;
//
//    private Button weightPortBtn, printerPortBtn, ledPortBtn, readCardPortBtn, readCardTypeBtn, saveBtn, backBtn;
//    private TextView weightPortTv, printerPortTv, printerCountTv, transactionDataTv, baudRateTv, dataDaysTv, sleepTimeTv, serverPortTv;
//    EditText serverIPTv;
//    private TextView weightPortChooseTv, printerPortChooseTv, ledPortChooseTv, readCardPortChooseTv, readCardTypeChooseTv;
//
//    private List<LocalSettingsBean.CardReaderTypeList> cardReaderTypeList;
//    private List<LocalSettingsBean.WeightPort> weightPorts;
//    private List<LocalSettingsBean.PrinterPort> printerPorts;
//    private List<LocalSettingsBean.ExternalLedPort> externalLedPorts;
//    private List<LocalSettingsBean.CardReaderPort> cardReaderPorts;
//    private int weightPos = 0, printerPos = 0, ledPos = 0, readCardPortPos = 0, readCardTypePos = 0;
//
////    private List<UsbSerialDriver> availableDrivers;
//    private UsbManager manager;
//    private ArrayList<ChooseBean> deviceList;
//    private ArrayList<ChooseBean> printerList;
//
//    private LocalSettingsBean.Value valueMap;
//
//
//    @Override
//    public View setInitView() {
//        rootView = LayoutInflater.from(this).inflate(R.layout.local_settings_activity, null);
//
//        weightPortBtn = rootView.findViewById(R.id.local_settings_weight_port_choose_btn);
//        printerPortBtn = rootView.findViewById(R.id.local_settings_printer_port_choose_btn);
//        ledPortBtn = rootView.findViewById(R.id.local_settings_led_port_choose_btn);
//        readCardPortBtn = rootView.findViewById(R.id.local_settings_read_card_port_choose_btn);
//        readCardTypeBtn = rootView.findViewById(R.id.local_settings_read_card_type_choose_btn);
//        saveBtn = rootView.findViewById(R.id.local_settings_save_btn);
//        backBtn = rootView.findViewById(R.id.local_settings_back_btn);
//
//        weightPortTv = rootView.findViewById(R.id.local_settings_weight_port_tv);
//        printerPortTv = rootView.findViewById(R.id.local_settings_printer_port_tv);
//        printerCountTv = rootView.findViewById(R.id.local_settings_printer_count_tv);
//        transactionDataTv = rootView.findViewById(R.id.local_settings_transaction_data_tv);
//        baudRateTv = rootView.findViewById(R.id.local_settings_weight_baudrate_tv);
//        serverIPTv = rootView.findViewById(R.id.local_settings_server_ip_tv);
//        dataDaysTv = rootView.findViewById(R.id.local_settings_data_days_tv);
//        sleepTimeTv = rootView.findViewById(R.id.local_settings_sleep_time_tv);
//        serverPortTv = rootView.findViewById(R.id.local_settings_server_port_tv);
//
//        weightPortChooseTv = rootView.findViewById(R.id.local_settings_weight_port_choose_tv);
//        printerPortChooseTv = rootView.findViewById(R.id.local_settings_printer_port_choose_tv);
//        ledPortChooseTv = rootView.findViewById(R.id.local_settings_led_port_choose_tv);
//        readCardPortChooseTv = rootView.findViewById(R.id.local_settings_read_card_port_choose_tv);
//        readCardTypeChooseTv = rootView.findViewById(R.id.local_settings_read_card_type_choose_tv);
//
//        weightPortBtn.setOnClickListener(this);
//        printerPortBtn.setOnClickListener(this);
//        ledPortBtn.setOnClickListener(this);
//        readCardPortBtn.setOnClickListener(this);
//        readCardTypeBtn.setOnClickListener(this);
//        saveBtn.setOnClickListener(this);
//        backBtn.setOnClickListener(this);
//
//        weightPortTv.setOnClickListener(this);
//        printerPortTv.setOnClickListener(this);
//        printerCountTv.setOnClickListener(this);
//        transactionDataTv.setOnClickListener(this);
//        baudRateTv.setOnClickListener(this);
//        serverIPTv.setOnClickListener(this);
//        dataDaysTv.setOnClickListener(this);
//        sleepTimeTv.setOnClickListener(this);
//        serverPortTv.setOnClickListener(this);
//
//        return rootView;
//    }
//
//    @Override
//    public void initView() {
////        getUsbDevices();
//        getScalesSettingData();
//        cardReaderTypeList = new ArrayList<>();
//        weightPorts = new ArrayList<>();
//        printerPorts = new ArrayList<>();
//        externalLedPorts = new ArrayList<>();
//        cardReaderPorts = new ArrayList<>();
//        serverPortTv.setText(Constants.URL);
////        ChooseBean bean;
////        for (int i = 0; i < 4; i++) {
////            bean= new ChooseBean();
////            bean.setChecked(false);
////            bean.setChooseItem("测试数据 " + i);
////            list.add(bean);
////        }
//        showLocalIp();
//    }
//
//    private void showLocalIp() {
//        serverIPTv.setText(SPUtils.getString(this,KEY_SERVER_IP,""));
//        serverPortTv.setText(SPUtils.getString(this,KEY_SVERVER_PORT,""));
//    }
//
////    public void getUsbDevices() {
////        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
////        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
////        if (availableDrivers.isEmpty()) {
////            return;
////        }
////        ChooseBean chooseBean;
////        deviceList = new ArrayList();
////        printerList = new ArrayList<>();
////        chooseBean = new ChooseBean();
////        chooseBean.setChooseItem("内置打印机：/dev/ttyS4");
////        printerList.add(chooseBean);
////        for (int i = 0; i < availableDrivers.size(); i++) {
////            if (availableDrivers.get(i).getDevice().getVendorId() == 26728 && availableDrivers.get(i).getDevice().getProductId() == 1280) {
////                chooseBean = new ChooseBean();
////                chooseBean.setChooseItem("外部打印机：" + availableDrivers.get(i).getDevice().getDeviceName());
////                printerList.add(chooseBean);
////            }
////            if (availableDrivers.get(i).getDevice().getVendorId() == 6790 && availableDrivers.get(i).getDevice().getProductId() == 29987) {
////                chooseBean = new ChooseBean();
////                chooseBean.setChooseItem("外部读卡器：" + availableDrivers.get(i).getDevice().getDeviceName());
////                deviceList.add(chooseBean);
////            }
////        }
////
////    }
//
//    public void getScalesSettingData() {
//        if (!NetworkUtil.isConnected(LocalSettingsActivity.this)) {
//            return;
//        }
//        RetrofitFactory.getInstance().API()
//                .getScalesSettingData(AccountManager.getInstance().getAdminToken(), MacManager.getInstace(this).getMac())
//                .compose(this.<BaseEntity<LocalSettingsBean>>setThread())
//                .subscribe(new Observer<BaseEntity<LocalSettingsBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        showLoading();
//                    }
//
//                    @Override
//                    public void onNext(BaseEntity<LocalSettingsBean> localSettingsBeanBaseEntity) {
//
//                        if (localSettingsBeanBaseEntity.isSuccess()) {
//                            Long saveDate = (Long) SPUtils.get(LocalSettingsActivity.this, "currentDate", null);
//                            if (saveDate != null) {
//                                if (saveDate.compareTo(Long.parseLong(localSettingsBeanBaseEntity.getData().value.card_reader_type.update_time)) > 0) {
//                                }
//                            }
//                            valueMap = localSettingsBeanBaseEntity.getData().value;
//
//                            cardReaderTypeList.addAll(localSettingsBeanBaseEntity.getData().card_reader_type_list);
//                            weightPorts.addAll(localSettingsBeanBaseEntity.getData().weight_port);
//                            printerPorts.addAll(localSettingsBeanBaseEntity.getData().printer_port);
//                            externalLedPorts.addAll(localSettingsBeanBaseEntity.getData().external_led_port);
//                            cardReaderPorts.addAll(localSettingsBeanBaseEntity.getData().card_reader_port);
//
//                            LocalSettingsBean.Value.PrinterPort printerPort = (LocalSettingsBean.Value.PrinterPort) SPUtils.readObject(LocalSettingsActivity.this, KEY_PRINTER_PORT);
//                            if (printerPort != null) {
//                                Long loginDate = Long.parseLong(printerPort.update_time);
//                                Long valueDate = Long.parseLong(valueMap.printer_port.update_time);
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    printerPortChooseTv.setText(printerPort.val);
//                                } else {
//                                    printerPortChooseTv.setText(valueMap.printer_port.val);
//                                }
//                            } else {
//                                printerPortChooseTv.setText(valueMap.printer_port.val);
//                            }
//                            LocalSettingsBean.Value.NumberOfPrintsConfiguration printCount = (LocalSettingsBean.Value.NumberOfPrintsConfiguration) SPUtils.readObject(LocalSettingsActivity.this, KEY_PRINTER_COUNT);
//                            if (printCount != null) {
//                                Long loginDate = Long.parseLong(printCount.update_time);
//                                Long valueDate = Long.parseLong(valueMap.number_of_prints_configuration.update_time);
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    printerCountTv.setText(printCount.val);
//                                } else {
//                                    printerCountTv.setText(valueMap.number_of_prints_configuration.val);
//                                }
//                            } else {
//                                printerCountTv.setText(valueMap.number_of_prints_configuration.val);
//                            }
//                            LocalSettingsBean.Value.ClearTransactionData clearData = (LocalSettingsBean.Value.ClearTransactionData) SPUtils.readObject(LocalSettingsActivity.this, KEY_CLEAR_DATA);
//                            if (clearData != null) {
//                                Long loginDate = Long.parseLong(clearData.update_time);
//                                Long valueDate = Long.parseLong(valueMap.clear_transaction_data.update_time);
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    transactionDataTv.setText(clearData.val);
//                                } else {
//                                    transactionDataTv.setText(valueMap.clear_transaction_data.val);
//                                }
//                            } else {
//                                transactionDataTv.setText(valueMap.clear_transaction_data.val);
//                            }
//                            LocalSettingsBean.Value.CardReaderPort readerPort = (LocalSettingsBean.Value.CardReaderPort) SPUtils.readObject(LocalSettingsActivity.this, KEY_READ_CARD_PORT);
//                            if (readerPort != null) {
//                                Long loginDate = Long.parseLong(readerPort.update_time);
//                                Long valueDate = Long.parseLong(valueMap.card_reader_port.update_time);
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    readCardPortChooseTv.setText(readerPort.val);
//                                } else {
//                                    readCardPortChooseTv.setText(valueMap.card_reader_port.val);
//                                }
//                            } else {
//                                readCardPortChooseTv.setText(valueMap.card_reader_port.val);
//                            }
//                            LocalSettingsBean.Value.LotValidityTime dataDays = (LocalSettingsBean.Value.LotValidityTime) SPUtils.readObject(LocalSettingsActivity.this, KEY_DATA_DAYS);
//                            if (dataDays != null) {
//                                Long loginDate = Long.parseLong(dataDays.update_time);
//                                Long valueDate = Long.parseLong(valueMap.lot_validity_time.update_time);
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    dataDaysTv.setText(dataDays.val);
//                                } else {
//                                    dataDaysTv.setText(valueMap.lot_validity_time.val);
//                                }
//                            } else {
//                                dataDaysTv.setText(valueMap.lot_validity_time.val);
//                            }
//                            LocalSettingsBean.Value.ScreenOff screenOff = (LocalSettingsBean.Value.ScreenOff) SPUtils.readObject(LocalSettingsActivity.this, KEY_SCREEN_SLEEP);
//                            if (screenOff != null) {
//                                Long loginDate = Long.parseLong(screenOff.update_time);
//                                Long valueDate = Long.parseLong(valueMap.screen_off.update_time);
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    sleepTimeTv.setText(screenOff.val);
//                                } else {
//                                    sleepTimeTv.setText(valueMap.screen_off.val);
//                                }
//                            } else {
//                                sleepTimeTv.setText(valueMap.screen_off.val);
//                            }
//
//                            LocalSettingsBean.Value.ServerIp serverIp = (LocalSettingsBean.Value.ServerIp) SPUtils.readObject(LocalSettingsActivity.this, KEY_SERVER_IP);
//                            /*if (serverIp != null) {
//                                Long loginDate = Long.parseLong(serverIp.update_time);
//                                Long valueDate = Long.parseLong(valueMap.server_ip.update_time);
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    serverIPTv.setText(serverIp.val);
//                                } else {
//                                    serverIPTv.setText(valueMap.server_ip.val);
//                                }
//                            } else {
//                                serverIPTv.setText(valueMap.server_ip.val);
//                            }*/
//                            LocalSettingsBean.Value.ServerPort serverPort = (LocalSettingsBean.Value.ServerPort) SPUtils.readObject(LocalSettingsActivity.this, KEY_SVERVER_PORT);
//                            /*if (serverPort != null) {
//                                Long loginDate = Long.parseLong(serverPort.update_time);
//                                Long valueDate = Long.parseLong(valueMap.server_port.update_time);
//                                if (loginDate.compareTo(valueDate) > 0) {
//                                    serverPortTv.setText(serverPort.val);
//                                } else {
//                                    serverPortTv.setText(valueMap.server_port.val);
//                                }
//                            } else {
//                                serverPortTv.setText(valueMap.server_port.val);
//                            }*/
//
//                            weightPortChooseTv.setText(((LocalSettingsBean.WeightPort) localSettingsBeanBaseEntity.getData().weight_port.get(0)).val);
//
//                            ledPortChooseTv.setText(((LocalSettingsBean.ExternalLedPort) localSettingsBeanBaseEntity.getData().external_led_port.get(0)).val);
//                            readCardTypeChooseTv.setText(((LocalSettingsBean.CardReaderTypeList) localSettingsBeanBaseEntity.getData().card_reader_type_list.get(0)).val);
//
//                            weightPortTv.setText(localSettingsBeanBaseEntity.getData().value.weight_port.val);
//                            baudRateTv.setText(localSettingsBeanBaseEntity.getData().value.weighing_plate_baud_rate.val);
////                            serverIPTv.setText(localSettingsBeanBaseEntity.getData().value.server_ip.val);
////                            serverPortTv.setText(localSettingsBeanBaseEntity.getData().value.server_port.val);
//                        } else {
//                            showLoading(localSettingsBeanBaseEntity.getMsg());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        closeLoading();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        closeLoading();
//                    }
//                });
//    }
//
//
//    public void saveSettingsToSP() {
//
//        String ip = serverIPTv.getText().toString();
//        SPUtils.putString(this, KEY_SERVER_IP, ip);
//
//        String port = serverPortTv.getText().toString();
//        SPUtils.putString(this, KEY_SVERVER_PORT, port);
//
//        Toast.makeText(this, "保存服务器地址成功", Toast.LENGTH_SHORT).show();
//        if (valueMap == null){
//            resetIp();
//            return;
//        }
//        if (!TextUtils.isEmpty(printerPortChooseTv.getText())) {
//            valueMap.printer_port.update_time = System.currentTimeMillis() + "";
//            valueMap.printer_port.val = printerPortChooseTv.getText().toString();
//            SPUtils.saveObject(this, KEY_PRINTER_PORT, valueMap.printer_port);
//        }
//
//        valueMap.number_of_prints_configuration.update_time = System.currentTimeMillis() + "";
//        valueMap.number_of_prints_configuration.val = printerCountTv.getText().toString();
//        SPUtils.saveObject(this, KEY_PRINTER_COUNT, valueMap.number_of_prints_configuration);
//
//        valueMap.clear_transaction_data.update_time = System.currentTimeMillis() + "";
//        valueMap.clear_transaction_data.val = transactionDataTv.getText().toString();
//        SPUtils.saveObject(this, KEY_CLEAR_DATA, valueMap.clear_transaction_data);
//
//        valueMap.card_reader_port.update_time = System.currentTimeMillis() + "";
//        valueMap.card_reader_port.val = readCardPortChooseTv.getText().toString();
//        SPUtils.saveObject(this, KEY_READ_CARD_PORT, valueMap.card_reader_port);
//
//        valueMap.lot_validity_time.update_time = System.currentTimeMillis() + "";
//        valueMap.lot_validity_time.val = dataDaysTv.getText().toString();
//        SPUtils.saveObject(this, KEY_DATA_DAYS, valueMap.lot_validity_time);
//
//        valueMap.screen_off.update_time = System.currentTimeMillis() + "";
//        valueMap.screen_off.val = sleepTimeTv.getText().toString();
//        SPUtils.saveObject(this, KEY_SCREEN_SLEEP, valueMap.screen_off);
//
//        valueMap.server_ip.val = serverIPTv.getText().toString();
//        valueMap.server_ip.update_time = System.currentTimeMillis() + "";
//        SPUtils.saveObject(this, KEY_SERVER_IP, valueMap.server_ip);
//
//        valueMap.server_port.val = serverPortTv.getText().toString();
//        valueMap.server_port.update_time = System.currentTimeMillis() + "";
//        SPUtils.saveObject(this, KEY_SVERVER_PORT, valueMap.server_port);
//
//        Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 60 * 1000 * Integer.parseInt(sleepTimeTv.getText().toString()));
//        EventBus.getDefault().post(new BusEvent(BusEvent.SAVE_LOCAL_SUCCESS, true));
//        showLoading("保存成功");
//    }
//
//    private void resetIp() {
//        RetrofitFactory.reSetServiceIp();
//        EventBus.getDefault().post(new BusEvent(BusEvent.GO_HOME_PAGE, true));
//        Intent intent = new Intent();
//        intent.setClass(this, HomeActivity.class);
//        intent.putExtra(IS_RE_BOOT, true);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onClick(View v) {
//        ChooseDialog2.Builder chooseBuilder = new ChooseDialog2.Builder(this);
//        SoftKeyborad.Builder softBuilder = new SoftKeyborad.Builder(this);
//        switch (v.getId()) {
//            case R.id.local_settings_weight_port_choose_btn:
//                chooseBuilder.create(deviceList, weightPos, new ChooseDialog2.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        weightPos = position;
//                        weightPortChooseTv.setText(((ChooseBean) parent.getAdapter().getItem(position)).getChooseItem());
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_printer_port_choose_btn:
//                chooseBuilder.create(printerList, printerPos, new ChooseDialog2.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        printerPos = position;
//                        printerPortChooseTv.setText(((ChooseBean) parent.getAdapter().getItem(position)).getChooseItem());
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_led_port_choose_btn:
//                ChooseBean externalLed;
//                ArrayList<ChooseBean> externalLedList = new ArrayList();
//                for (int i = 0; i < externalLedPorts.size(); i++) {
//                    externalLed = new ChooseBean();
//                    externalLed.setChooseItem(externalLedPorts.get(i).val);
//                    externalLedList.add(externalLed);
//                }
//                chooseBuilder.create(externalLedList, ledPos, new ChooseDialog2.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        ledPos = position;
//                        ledPortChooseTv.setText(((ChooseBean) parent.getAdapter().getItem(position)).getChooseItem());
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_read_card_port_choose_btn:
//                if (deviceList == null) {
//                    showLoading("没有接入外部设备");
//                } else {
//                    chooseBuilder.create(deviceList, readCardPortPos, new ChooseDialog2.OnSelectedListener() {
//                        @Override
//                        public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                            readCardPortPos = position;
//                            readCardPortChooseTv.setText(((ChooseBean) parent.getAdapter().getItem(position)).getChooseItem());
//                        }
//                    }).show();
//                }
//                break;
//            case R.id.local_settings_read_card_type_choose_btn:
//                ChooseBean cardReaderType;
//                ArrayList<ChooseBean> cardTypeList = new ArrayList();
//                for (int i = 0; i < cardReaderTypeList.size(); i++) {
//                    cardReaderType = new ChooseBean();
//                    cardReaderType.setChooseItem(cardReaderTypeList.get(i).val);
//                    cardTypeList.add(cardReaderType);
//                }
//                chooseBuilder.create(cardTypeList, readCardTypePos, new ChooseDialog2.OnSelectedListener() {
//                    @Override
//                    public void onSelected(AdapterView<?> parent, View view, int position, long id) {
//                        readCardTypePos = position;
//                        readCardTypeChooseTv.setText(((ChooseBean) parent.getAdapter().getItem(position)).getChooseItem());
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_save_btn:
//                saveSettingsToSP();
//                break;
//            case R.id.local_settings_back_btn:
//                finish();
//                break;
//            case R.id.local_settings_weight_port_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        weightPortTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_printer_port_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        printerPortTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_printer_count_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        if (result.equals("0")) {
//                            showLoading("最小为1份");
//                            return;
//                        }
//                        printerCountTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_transaction_data_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        transactionDataTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_weight_baudrate_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        baudRateTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_server_ip_tv:
//               /* softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        serverIPTv.setText(result);
//                    }
//                }).show();*/
//                break;
//            case R.id.local_settings_data_days_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        dataDaysTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_sleep_time_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        sleepTimeTv.setText(result);
//                    }
//                }).show();
//                break;
//            case R.id.local_settings_server_port_tv:
//                softBuilder.create(new SoftKeyborad.OnConfirmedListener() {
//                    @Override
//                    public void onConfirmed(String result) {
//                        serverPortTv.setText(result);
//                    }
//                }).show();
//                break;
//
//        }
//    }
//}
