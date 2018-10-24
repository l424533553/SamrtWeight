package com.axecom.smartweight.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BaseActivity;
import com.axecom.smartweight.base.BaseEntity;
import com.axecom.smartweight.base.BusEvent;
import com.axecom.smartweight.bean.LoginData;
import com.axecom.smartweight.bean.LoginInfo;
import com.axecom.smartweight.manager.AccountManager;
import com.axecom.smartweight.net.RetrofitFactory;
import com.axecom.smartweight.ui.view.SoftKey;
import com.axecom.smartweight.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class StaffMemberLoginActivity extends BaseActivity {

    private View rootView;
    private EditText numberEt, pwdEt;
    private SoftKey softKey;
    private Button doneBtn, backBtn;

    UsbManager manager;
    public boolean threadStatus = false; //线程状态，为了安全终止线程

    private String loginType;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.staff_member_login_activity, null);
        numberEt = rootView.findViewById(R.id.staff_member_number_et);
        disableShowInput(numberEt);
        numberEt.requestFocus();
        pwdEt = rootView.findViewById(R.id.staff_member_pwd_et);
        disableShowInput(pwdEt);
        softKey = rootView.findViewById(R.id.staff_member_softkey);
        doneBtn = rootView.findViewById(R.id.staff_member_done_btn);
        backBtn = rootView.findViewById(R.id.staff_member_back_btn);
        rootView.findViewById(R.id.login_out_btn).setOnClickListener(this);


        doneBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        registerReceiver(usbReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        threadStatus = true;
        unregisterReceiver(usbReceiver);
    }

    @Override
    public void initView() {

        softKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getAdapter().getItem(position).toString();
                switch (rootView.findFocus().getId()) {
                    case R.id.staff_member_number_et:
                        setEditText(numberEt, position, text);
                        break;
                    case R.id.staff_member_pwd_et:
                        setEditText(pwdEt, position, text);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.staff_member_done_btn:
                String name = numberEt.getText().toString();
                String password = pwdEt.getText().toString();
                if (TextUtils.isEmpty(name) && getString(R.string.Administrators_pwd).equals(password)) {
                    startDDMActivity(SettingsActivity.class, true);
                    return;
                }




//                if (NetworkUtil.isConnected(this)) {
//                    clientLogin(AccountManager.getInstance().getScalesId() + "", name, password);
//                } else {  //  离线状态
//                    User user = SQLite.select().from(User.class).where(User_Table.card_number.is(name)).querySingle();
//                    if (user != null) {
//                        if (TextUtils.equals(pwdEt.getText(), user.password)) {
//                            //TODO 测试 功能
//                            AccountManager.getInstance().setAdminToken(user.user_token);
//                            setResult(RESULT_OK);
//                            finish();
//                        } else {
//                            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(this, "没有该卡号", Toast.LENGTH_SHORT).show();
//                    }
//                }



                break;
            case R.id.staff_member_back_btn:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.login_out_btn:
                AccountManager.getInstance().loginOut();
                EventBus.getDefault().post(new BusEvent(BusEvent.LOGIN_OUT,""));
                startDDMActivity(HomeActivity.class,true);
                finish();
                break;
        }
    }

    public void clientLogin(String scalesId, final String serialNumber, final String password) {
        RetrofitFactory.getInstance().API()
                .clientLogin(scalesId, serialNumber, password)
                .compose(this.<BaseEntity<LoginData>>setThread())
                .subscribe(new Observer<BaseEntity<LoginData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();

                    }

                    @Override
                    public void onNext(BaseEntity<LoginData> loginDataBaseEntity) {
                        if (loginDataBaseEntity.isSuccess()) {
                            LoginData data = loginDataBaseEntity.getData();
                            if(data.getUser_type()== LoginInfo.TYPE_seller){
                                AccountManager.getInstance().saveToken(loginDataBaseEntity.getData().getToken());
                            }
                            AccountManager.getInstance().setAdminToken(data.getToken());
                            AccountManager.getInstance().setUserType(data.getUser_type());
                            setResult(RESULT_OK);
                            EventBus.getDefault().post(new BusEvent(BusEvent.notifiySellerInfo,true));
                            finish();
                        } else {
                            showLoading(loginDataBaseEntity.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        closeLoading();

                    }

                    @Override
                    public void onComplete() {
                        closeLoading();

                    }
                });
    }

    public void staffMemberLogin(String scalesId, String serialNumber, String password) {
        RetrofitFactory.getInstance().API()
                .staffMemberLogin(scalesId, serialNumber, password)
                .compose(this.<BaseEntity<LoginData>>setThread())
                .subscribe(new Observer<BaseEntity<LoginData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();

                    }

                    @Override
                    public void onNext(BaseEntity<LoginData> loginDataBaseEntity) {
                        if (loginDataBaseEntity.isSuccess()) {
                            AccountManager.getInstance().setAdminToken(loginDataBaseEntity.getData().getAdminToken());
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showLoading(loginDataBaseEntity.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        closeLoading();
                    }

                    @Override
                    public void onComplete() {
                        closeLoading();
                    }
                });
    }


//    public void usbOpen() {
//        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
//        if (availableDrivers.isEmpty()) {
//            return;
//        }
//
//        for (int i = 0; i < availableDrivers.size(); i++) {
//            UsbSerialDriver driver = availableDrivers.get(i);
//            if (driver.getDevice().getVendorId() == 6790 && driver.getDevice().getProductId() == 29987) {
//                SysApplication.getInstances().setCardDevice(driver.getDevice());
//                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
//                UsbDeviceConnection connection = null;
//
//                if (manager.hasPermission(driver.getDevice())) {
//                    //if has already got permission, just goto connect it
//                    //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
//                    //and also choose option: not ask again
//                    connection = manager.openDevice(driver.getDevice());
//                } else {
//                    //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
//                    manager.requestPermission(driver.getDevice(), mPermissionIntent);
//                }
//
//                if (connection == null) {
//                    // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
//                    return;
//                }
//                port = driver.getPorts().get(0);
//                try {
//                    port.open(connection);
//                    port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
//                    new ReadThread().start();
//                } catch (IOException e) {
//                    // Deal with error.
//                } finally {
//                }
//            }
////            if (driver.getDevice().getVendorId() == 26728 && driver.getDevice().getProductId() == 1280) {
////                SysApplication.getInstances().setGpDriver(driver);
////            }
//        }
//
//        if (SysApplication.getInstances().getCardDevice() == null) {
//            showLoading("没有插入读卡器，请检查设备");
//        }
////        if (SysApplication.getInstances().getGpDriver() == null) {
////            showLoading("没有插入打印机，请检查设备");
////        }
//// Read some data! Most have just one port (port 0).
//
//    }

    /**
     * 单开一线程，来读数据
     * <p>
     * 55 aa 14 16 ff 05 d6 29 95 a2 c8 08 04 00 01 62 b9 9d b5 0f b8 1d 00 ff 00 00 00 00 00 00 00 00
     * 55 aa 14 16 ff 05 a6 45 9e e2 9f 08 04 00 01 be d6 7a 56 ab 67 1d 00 ff 00 00 00 00 00 00 0
     * 55 aa 14 16 ff 05 06 e9 93 a2 de 08 04 00 01 82 60 7a 68 ec 9f 1d 00 ff 00 00 00 00 00 00 00 00  bytes.
     * 55 aa 14 16 ff 05 d3 12 da 2d 36 08 04 00 01 6a a7 b0 d8 5e 50 1d 00 ff 00 00 00 00 00 00 00 00  bytes.
     */
//    private class ReadThread extends Thread {
//
//        @Override
//        public void run() {
//            super.run();
//            //判断进程是否在运行，更安全的结束进程
//            while (!threadStatus) {
////                LogUtils.d("进入线程run");
//                //64   1024
//                byte[] buffer = new byte[32];
//                int size; //读取数据的大小
//                try {
//                    int numBytesRead = port.read(buffer, 1000);
//                    if (numBytesRead > 0) {
//                        String s = "";
//                        for (byte b : buffer) {
//                            s += String.format("%02x ", b);
//                        }
////                        LogUtils.d("Read " + s + " bytes.");
//                        String[] cards = s.split(" ");
//                        String cardNo = "";
//                        for (int i = 9; i > 5; i--) {
//                            cardNo += cards[i];
//                        }
//                        final String finalCardNo = cardNo;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                numberEt.setText(finalCardNo.toUpperCase());
//                            }
//                        });
//                    }
//
//                } catch (IOException e) {
//                    LogUtils.e("run: 数据读取异常：" + e.toString());
//                }
//            }
//
//        }
//    }

    private BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                threadStatus = true;
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    if (device.getVendorId() == 6790 && device.getProductId() == 29987) {
                        showLoading("读卡器被拔出，请检查设备");
//                        if(readThread != null){
//                            readThread.interrupt();
//                        }
                    }
//                    if (device.getVendorId() == 26728 && device.getProductId() == 1280) {
//                        showLoading("打印机被拔出，请检查设备");
//                    }
                }
            }
            if (intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                threadStatus = false;
//                usbOpen();
                LogUtils.d("ACTION_USB_DEVICE_ATTACHED");
            }
        }
    };
}
