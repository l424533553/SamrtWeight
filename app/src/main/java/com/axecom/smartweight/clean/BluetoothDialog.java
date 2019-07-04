//package com.axecom.smartweight.ui.view;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.axecom.smartweight.R;
//import com.axecom.smartweight.bean.BleAdvertisedData;
//import com.axecom.smartweight.manager.ClientManager;
//import com.axecom.smartweight.clean.DeviceListAdapter;
//import com.axecom.smartweight.ui.uiutils.BleUtil;
//import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
//import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
//import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
//import com.inuker.bluetooth.library.model.BleGattProfile;
//import com.inuker.bluetooth.library.search.SearchRequest;
//import com.inuker.bluetooth.library.search.SearchResult;
//import com.inuker.bluetooth.library.search.response.SearchResponse;
//import com.inuker.bluetooth.library.utils.BluetoothLog;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
//
//public class BluetoothDialog extends Dialog {
//    public BluetoothDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//    }
//
//    public BluetoothDialog(@NonNull Context context) {
//        super(context);
//    }
//
//    public interface OnBtnClickListener {
//        void onConfirmed(String result);
//
//        void onCanceled(String result);
//    }
//
//    public static class Builder implements View.OnClickListener {
//        private static final String[] DATA_DIGITAL = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "删除", "0", "."};
//
//        private View contentView;
//        private BluetoothDialog bluetoothDialog;
//        private View view;
//        private ListView mListView;
//        private List<SearchResult> mDevices;
//        private TextView mTvTitle;
//        private DeviceListAdapter mAdapter;
//        private OnBtnClickListener onBtnClickListener;
//        private Context context;
//        private ProgressBar mPbar;
//
//        public Builder(Context context) {
//            this.context = context;
//            bluetoothDialog = new BluetoothDialog(context, R.style.dialog);
//            view = LayoutInflater.from(context).inflate(R.layout.bluetooth_activity, null);
//            bluetoothDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            mTvTitle = view.findViewById(R.id.bluetooth_title_tv);
//            mListView = view.findViewById(R.id.bluetooth_listview);
//            mPbar = view.findViewById(R.id.bluetooth_pbar);
//            mDevices = new ArrayList<SearchResult>();
//            mAdapter = new DeviceListAdapter(context);
//            mListView.setAdapter(mAdapter);
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    connectDevice((SearchResult) parent.getAdapter().getItem(position));
//                }
//            });
//            searchDevice();
//
//            ClientManager.getClient(context).registerBluetoothStateListener(new BluetoothStateListener() {
//                @Override
//                public void onBluetoothStateChanged(boolean openOrClosed) {
//                    BluetoothLog.v(String.format("onBluetoothStateChanged %b", openOrClosed));
//                    if (openOrClosed) {
//                        searchDevice();
//                    }
//                }
//            });
//        }
//
//        public BluetoothDialog create(OnBtnClickListener onBtnClickListener) {
//            bluetoothDialog.setContentView(view);
//            bluetoothDialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
//            bluetoothDialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
//
//            return bluetoothDialog;
//        }
//
//        @Override
//        public void onClick(View v) {
//
//        }
//
//        private void searchDevice() {
//            SearchRequest request = new SearchRequest.Builder()
//                    .searchBluetoothLeDevice(5000, 3)   // 先扫BLE设备3次，每次3s
//                    .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
//                    .searchBluetoothLeDevice(5000, 3)
//                    .build();
//
//            ClientManager.getClient(context).search(request, mSearchResponse);
//        }
//
//        private final SearchResponse mSearchResponse = new SearchResponse() {
//            @Override
//            public void onSearchStarted() {
//                BluetoothLog.w("MyBuglyActivity.onSearchStarted");
////                mListView.onRefreshComplete(true);
////                mRefreshLayout.showState(AppConstants.LIST);
//                mTvTitle.setText(R.string.string_refreshing);
//                mDevices.clear();
//            }
//
//            @Override
//            public void onDeviceFounded(SearchResult device) {
////            BluetoothLog.w("MyBuglyActivity.onDeviceFounded " + device.device.getAddress());
//                Toast.makeText(context, device.getAddress(), Toast.LENGTH_SHORT).show();
//                if (!mDevices.contains(device)) {
//                    final BleAdvertisedData badata = BleUtil.parseAdertisedData(device.scanRecord);
////                    String s = BleUtil.bytesToHex(device.scanRecord);
//                    String name = device.getName();
//                    mDevices.add(device);
//                    mAdapter.setDataList(mDevices);
//
////                Beacon beacon = new Beacon(device.scanRecord);
////                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));
////
////                BeaconItem beaconItem = null;
////                BeaconParser beaconParser = new BeaconParser(beaconItem);
////                int firstByte = beaconParser.readByte(); // 读取第1个字节
////                int secondByte = beaconParser.readByte(); // 读取第2个字节
////                int productId = beaconParser.readShort(); // 读取第3,4个字节
////                boolean bit1 = beaconParser.getBit(firstByte, 0); // 获取第1字节的第1bit
////                boolean bit2 = beaconParser.getBit(firstByte, 1); // 获取第1字节的第2bit
////                beaconParser.setPosition(0); // 将读取起点设置到第1字节处
//                }
//
//                if (mDevices.size() > 0) {
////                    mRefreshLayout.showState(AppConstants.LIST);
//                }
//            }
//
//            @Override
//            public void onSearchStopped() {
//                BluetoothLog.w("MyBuglyActivity.onSearchStopped");
////                mListView.onRefreshComplete(true);
////                mRefreshLayout.showState(AppConstants.LIST);
//
//                mTvTitle.setText(R.string.devices);
//            }
//
//            @Override
//            public void onSearchCanceled() {
//                BluetoothLog.w("MyBuglyActivity.onSearchCanceled");
//
////                mListView.onRefreshComplete(true);
////                mRefreshLayout.showState(AppConstants.LIST);
//
//                mTvTitle.setText(R.string.devices);
//            }
//        };
//
//        private void connectDevice(final SearchResult mDevice) {
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
//            ClientManager.getClient(context).connect(mDevice.getAddress(), options, new BleConnectResponse() {
//                @Override
//                public void onStringResponse(int code, BleGattProfile profile) {
//                    BluetoothLog.v(String.format("profile:\n%s", profile));
//                    mTvTitle.setText(String.format("%s", mDevice.getAddress()));
//                    mPbar.setVisibility(View.GONE);
//                    mListView.setVisibility(View.VISIBLE);
//
//                    if (code == REQUEST_SUCCESS) {
//                        Toast.makeText(context, "chenggong", Toast.LENGTH_LONG).show();
////                        mAdapter.setGattProfile(profile);
//                    }
//                }
//            });
//        }
//
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        ClientManager.getClient(Context).stopSearch();
//    }
//}
