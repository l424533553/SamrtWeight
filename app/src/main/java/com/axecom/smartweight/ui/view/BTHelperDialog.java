//package com.axecom.iweight.ui.view;
//
//import android.app.Dialog;
//import android.bluetooth.BluetoothDevice;
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.axecom.iweight.R;
//import com.axecom.iweight.base.SysApplication;
//import com.axecom.iweight.bean.BleAdvertisedData;
//import com.axecom.iweight.bean.DetailItem;
//import com.axecom.iweight.manager.ClientManager;
//import com.axecom.iweight.ui.adapter.DeviceListAdapter;
//import com.axecom.iweight.ui.uiutils.BleUtil;
//import com.axecom.iweight.utils.SPUtils;
//import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
//import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
//import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
//import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
//import com.inuker.bluetooth.library.connect.response.BleReadResponse;
//import com.inuker.bluetooth.library.model.BleGattCharacter;
//import com.inuker.bluetooth.library.model.BleGattProfile;
//import com.inuker.bluetooth.library.model.BleGattService;
//import com.inuker.bluetooth.library.search.SearchRequest;
//import com.inuker.bluetooth.library.search.SearchResult;
//import com.inuker.bluetooth.library.search.response.SearchResponse;
//import com.inuker.bluetooth.library.utils.BluetoothLog;
//import com.inuker.bluetooth.library.utils.ByteUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import top.wuhaojie.bthelper.BtHelperClient;
//import top.wuhaojie.bthelper.IErrorListener;
//import top.wuhaojie.bthelper.OnSearchDeviceListener;
//
//import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
//
//public class BTHelperDialog extends Dialog {
//    private static final String TAG = "BTHelperDialog";
//
//    public static final String KEY_BT_ADDRESS = "key_bt_address";
//    public static BtHelperClient btHelperClient;
//
//    public BTHelperDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//    }
//
//    public BTHelperDialog(@NonNull Context context) {
//        super(context);
//    }
//
//    public interface OnBtnClickListener {
//        void onConfirmed(BtHelperClient.STATUS mCurrStatus, String deviceAddress);
//
//        void onCanceled(String result);
//    }
//
//    public static class Builder implements View.OnClickListener {
//        private final String[] DATA_DIGITAL = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "删除", "0", "."};
//
//        private View contentView;
//        private BTHelperDialog bluetoothDialog;
//        private View view;
//        private ListView mListView;
//        private List<BluetoothDevice> mDevices;
//        private Button scanBtn;
//        private Button confirmBtn;
//        private TextView mTvTitle;
//        private BluetoothAdapter mAdapter;
//        private OnBtnClickListener onBtnClickListener;
//        private Context context;
//        private ProgressBar mPbar;
//        private BleGattProfile mProfile;
//
//        public Builder(final Context context) {
//            this.context = context;
//            bluetoothDialog = new BTHelperDialog(context, R.style.dialog);
//            view = LayoutInflater.from(context).inflate(R.layout.bluetooth_activity, null);
//            bluetoothDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SysApplication.mHeightPixels));
//            mTvTitle = view.findViewById(R.id.bluetooth_title_tv);
//            mListView = view.findViewById(R.id.bluetooth_listview);
//            mPbar = view.findViewById(R.id.bluetooth_pbar);
//            scanBtn = view.findViewById(R.id.bluetooth_scan_btn);
//            confirmBtn = view.findViewById(R.id.bluetooth_cancel_btn);
//
//            mDevices = new ArrayList<BluetoothDevice>();
//            mAdapter = new BluetoothAdapter(context, mDevices);
//            mListView.setAdapter(mAdapter);
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    btHelperConnect(((BluetoothDevice) parent.getAdapter().getItem(position)).getAddress());
//                }
//            });
//
//            btHelperClient = BtHelperClient.from(context);
//            btHelperClient.requestEnableBt();
//
//            searchDevice();
//
//            scanBtn.setOnClickListener(this);
//            confirmBtn.setOnClickListener(this);
//        }
//
//        public BTHelperDialog create(OnBtnClickListener onBtnClickListener) {
//            this.onBtnClickListener = onBtnClickListener;
//            bluetoothDialog.setContentView(view);
//            bluetoothDialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
//            bluetoothDialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
//
//            return bluetoothDialog;
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.bluetooth_scan_btn:
//                    searchDevice();
//                    break;
//                case R.id.bluetooth_cancel_btn:
////                    onBtnClickListener.onConfirmed("close");
//                    bluetoothDialog.dismiss();
//                    break;
//            }
//        }
//
//
//        public void searchDevice() {
//            btHelperClient.searchDevices(new OnSearchDeviceListener() {
//                @Override
//                public void onStartDiscovery() {
//                    mPbar.setVisibility(View.VISIBLE);
//                    mTvTitle.setText(context.getString(R.string.scanning));
//                }
//
//                @Override
//                public void onNewDeviceFound(BluetoothDevice device) {
//                    Log.d(TAG, "new device: " + device.getName() + " " + device.getAddress());
//                    if (!mDevices.contains(device)) {
//                        mDevices.add(device);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onSearchCompleted(List<BluetoothDevice> bondedList, List<BluetoothDevice> newList) {
//                    Log.d(TAG, "SearchCompleted: bondedList" + bondedList.toString());
//                    Log.d(TAG, "SearchCompleted: newList" + newList.toString());
//                    mPbar.setVisibility(View.GONE);
//                    mTvTitle.setText(context.getString(R.string.devices));
//
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    mPbar.setVisibility(View.GONE);
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onConnected(BtHelperClient.STATUS mCurrStatus) {
//
//                }
//            });
//        }
//
//
//        public void btHelperConnect(final String deviceAddress) {
//            btHelperClient.connectDevice(deviceAddress, new IErrorListener() {
//                @Override
//                public void onError(Exception e) {
//
//                }
//
//                @Override
//                public void onConnected(BtHelperClient.STATUS mCurrStatus) {
//                    bluetoothDialog.dismiss();
//                    if (onBtnClickListener != null)
//                        onBtnClickListener.onConfirmed(mCurrStatus, deviceAddress);
//                }
//
//            });
//        }
//
//        public void btHelperConnect(final BluetoothDevice device) {
//            btHelperClient.connectDevice(device.getAddress(), new IErrorListener() {
//                @Override
//                public void onError(Exception e) {
//
//                }
//
//                @Override
//                public void onConnected(BtHelperClient.STATUS mCurrStatus) {
//                    bluetoothDialog.dismiss();
////                    onBtnClickListener.onConfirmed(mCurrStatus);
//                }
//
//            });
//        }
//
//        private void connectDevice(final BluetoothDevice mDevice) {
//            mTvTitle.setText(String.format("%s%s", context.getString(R.string.connecting), mDevice.getAddress()));
//            mPbar.setVisibility(View.VISIBLE);
//            mListView.setVisibility(View.GONE);
//
//            BleConnectOptions options = new BleConnectOptions.Builder()
//                    .setConnectRetry(3)
//                    .setConnectTimeout(20000)
//                    .setServiceDiscoverRetry(3)
//                    .setServiceDiscoverTimeout(10000)
//                    .build();
//
//            ClientManager.getClient().connect(mDevice.getAddress(), options, new BleConnectResponse() {
//                @Override
//                public void onStringResponse(int code, BleGattProfile profile) {
//                    BluetoothLog.v(String.format("profile:\n%s", profile));
//                    mTvTitle.setText(String.format("%s", mDevice.getAddress()));
//                    mPbar.setVisibility(View.GONE);
//                    mListView.setVisibility(View.VISIBLE);
//                    Toast.makeText(context, "code" + code, Toast.LENGTH_LONG).show();
//
//                    if (code == REQUEST_SUCCESS) {
//                        Toast.makeText(context, "chenggong", Toast.LENGTH_LONG).show();
//                        mProfile = profile;
//                        sendMsg(mDevice);
////                        mAdapter.setGattProfile(profile);
//                    }
//                }
//            });
//        }
//
//        public void sendMsg(BluetoothDevice device) {
//            List<DetailItem> items = new ArrayList<DetailItem>();
//            List<BleGattService> services = mProfile.getServices();
//
//            for (BleGattService service : services) {
//                items.add(new DetailItem(DetailItem.TYPE_SERVICE, service.getUUID(), null));
//                List<BleGattCharacter> characters = service.getCharacters();
//                for (BleGattCharacter character : characters) {
//                    items.add(new DetailItem(DetailItem.TYPE_CHARACTER, character.getUuid(), service.getUUID()));
//                }
//            }
//            ClientManager.getClient().read(device.getAddress(), mProfile.getServices().get(0).getUUID(), mProfile.getServices().get(0).getCharacters().get(0).getUuid(), new BleReadResponse() {
//                @Override
//                public void onStringResponse(int code, byte[] data) {
//                    mTvTitle.setText(code + ByteUtils.byteToString(data));
//                }
//            });
//            ClientManager.getClient().notify(device.getAddress(), mProfile.getServices().get(0).getUUID(), mProfile.getServices().get(0).getCharacters().get(0).getUuid(), new BleNotifyResponse() {
//                @Override
//                public void onNotify(UUID service, UUID character, byte[] value) {
//                    mTvTitle.setText(service + ByteUtils.byteToString(value));
//
//                }
//
//                @Override
//                public void onStringResponse(int code) {
//
//                }
//            });
//        }
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
////        btHelperClient.close();
//    }
//
//    static class BluetoothAdapter extends BaseAdapter {
//
//        private Context context;
//        private List<BluetoothDevice> list;
//
//        public BluetoothAdapter(Context context, List<BluetoothDevice> list) {
//            this.context = context;
//            this.list = list;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(context).inflate(R.layout.bluetooth_item, null);
//                holder = new ViewHolder();
//                holder.nameTv = convertView.findViewById(R.id.bluetooth_item_name);
//                holder.addressTv = convertView.findViewById(R.id.bluetooth_item_address);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            BluetoothDevice device = list.get(position);
//            if (TextUtils.isEmpty(device.getName())) {
//                holder.nameTv.setText("unknow");
//
//            } else {
//                holder.nameTv.setText(device.getName());
//            }
//            holder.addressTv.setText(device.getAddress());
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView nameTv;
//            TextView addressTv;
//        }
//    }
//}
