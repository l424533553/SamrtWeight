package com.axecom.smartweight.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BaseActivity;
import com.axecom.smartweight.entity.system.WifiBean;

import java.util.ArrayList;
import java.util.List;

public class WifiSettingsActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_SSID_WIFI_SAVED = "key_ssid_wifi_saved";
    private static final String ACTION_NET_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    private GridView wifiGridView;
    private WifiManager wifiManager;
    private List<ScanResult> mWifiList;
    private WifiAdapter wifiAdapter;
    private final List<WifiBean> wifiSSIDList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_settings_activity);
        setInitView();
        initView();
        setInitView();
    }

    public void setInitView() {

        wifiGridView = findViewById(R.id.wifi_settings_grid_view);
        Button backBtn = findViewById(R.id.wifi_settings_back_btn);
        Button saveBtn = findViewById(R.id.wifi_settings_save_btn);

        wifiAdapter = new WifiAdapter(this, wifiSSIDList);
        wifiGridView.setAdapter(wifiAdapter);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            networkInfo.isAvailable();
        }//            SPUtils.putString(WifiSettingsActivity.this, KEY_SSID_WIFI_SAVED, networkInfo.getExtraInfo().subSequence(1, networkInfo.getExtraInfo().length() - 1).toString());
        registerReceiver(netWorkReceiver, new IntentFilter(ACTION_NET_CHANGE));
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkReceiver);
    }


    public void initView() {
        startScan(this);
        WifiBean wifiBean;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        for (int i = 0; i < mWifiList.size(); i++) {
            wifiBean = new WifiBean();
            if (wifiInfo.getBSSID().equals(mWifiList.get(i).BSSID)) {
                wifiBean.setConnected(true);
            } else {
                wifiBean.setConnected(false);
            }
            wifiBean.setSsid(mWifiList.get(i).SSID);
            wifiSSIDList.add(wifiBean);
        }

        wifiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                // 连接到外网
                final WifiConfiguration[] mWifiConfiguration = new WifiConfiguration[1];
                //检测指定SSID的WifiConfiguration 是否存在
                WifiConfiguration tempConfig = IsExsits(mWifiList.get(position).SSID);
//                if (tempConfig == null) {
//                    WifiPwdDialog.Builder builder = new WifiPwdDialog.Builder(WifiSettingsActivity.this);
//                    builder.create(new WifiPwdDialog.OnConfirmedListener() {
//                        @Override
//                        public void onConfirmed(String result) {
//                            //创建一个新的WifiConfiguration ，CreateWifiInfo()需要自己实现
//                            mWifiConfiguration[0] = createWifiInfo(mWifiList.get(position).SSID, result, 3);
//                            int wcgID = wifiManager.addNetwork(mWifiConfiguration[0]);
//                            boolean b = wifiManager.enableNetwork(wcgID, true);
//                            ssid = mWifiList.get(position).SSID;
//                            pos = position;
//                        }
//                    }).show();
//                } else {
//                    //发现指定WiFi，并且这个WiFi以前连接成功过
//                    mWifiConfiguration[0] = tempConfig;
//                    boolean b = wifiManager.enableNetwork(mWifiConfiguration[0].networkId, true);
////                    updateListView(b, position, mWifiList.get(position).SSID);
//                }


            }
        });
    }

    String ssid = "";
    int pos;

    private final BroadcastReceiver netWorkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_NET_CHANGE.equals(intent.getAction())) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    updateListView(true, pos, networkInfo.getExtraInfo());
                } else {
                    updateListView(false, pos, ssid);
                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void updateListView(boolean flag, int position, String SSID) {
        WifiBean bean;
        for (int i = 0; i < wifiSSIDList.size(); i++) {
            bean = new WifiBean();
            bean.setSsid(wifiSSIDList.get(i).getSsid());
            if (!flag) {
                bean.setConnected(false);
            } else if (!TextUtils.isEmpty(SSID)) {
                String s = SSID.subSequence(1, SSID.length() - 1).toString();
                if (wifiSSIDList.get(i).getSsid().equals(s)) {
                    bean.setConnected(true);
                } else {
                    bean.setConnected(false);
                }
            } else {
                if (position == i) {
                    bean.setConnected(true);
                } else {
                    bean.setConnected(false);
                }
            }
            wifiSSIDList.set(i, bean);
        }
        wifiAdapter.notifyDataSetChanged();
    }

    public WifiConfiguration createWifiInfo(String SSID, String Password,
                                            int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) // WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 2) // WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) // WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }


    // 添加一个网络并连接
    public boolean addNetwork(WifiConfiguration wcg) {
        int wcgID = wifiManager.addNetwork(wcg);
        boolean b = wifiManager.enableNetwork(wcgID, true);
        System.out.println("a--" + wcgID);
        System.out.println("b--" + b);
        return b;
    }

    private void Wificonnect(String SSID, String password) {
        // 连接到外网
        WifiConfiguration mWifiConfiguration;
        //检测指定SSID的WifiConfiguration 是否存在
        WifiConfiguration tempConfig = IsExsits(SSID);
        if (tempConfig == null) {
            //创建一个新的WifiConfiguration ，CreateWifiInfo()需要自己实现
            mWifiConfiguration = createWifiInfo(SSID, password, 3);
            int wcgID = wifiManager.addNetwork(mWifiConfiguration);
            boolean b = wifiManager.enableNetwork(wcgID, true);
        } else {
            //发现指定WiFi，并且这个WiFi以前连接成功过
            mWifiConfiguration = tempConfig;
            boolean b = wifiManager.enableNetwork(mWifiConfiguration.networkId, true);
        }

    }

    //判断曾经连接过得WiFi中是否存在指定SSID的WifiConfiguration
    public WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    @SuppressLint("WrongConstant")
    public void startScan(Context context) {

        wifiManager.startScan();
        //得到扫描结果
        List<ScanResult> results = wifiManager.getScanResults();
        // 得到配置好的网络连接
        List<WifiConfiguration> mWifiConfiguration = wifiManager.getConfiguredNetworks();
        if (results == null) {
            if (wifiManager.getWifiState() == 3) {
                Toast.makeText(context, "当前区域没有无线网络", Toast.LENGTH_SHORT).show();
            } else if (wifiManager.getWifiState() == 2) {
                Toast.makeText(context, "wifi正在开启，请稍后扫描", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WiFi没有开启", Toast.LENGTH_SHORT).show();
            }
        } else {
            mWifiList = new ArrayList<>();
            for (ScanResult result : results) {
                if (result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]")) {
                    continue;
                }
                boolean found = false;
                for (ScanResult item : mWifiList) {
                    if (item.SSID.equals(result.SSID) && item.capabilities.equals(result.capabilities)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    mWifiList.add(result);
                }
            }
            wifiAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifi_settings_back_btn:
            case R.id.wifi_settings_save_btn:
                finish();
                break;
        }
    }

    class WifiAdapter extends BaseAdapter {
        private final Context context;
        private final List<WifiBean> ssidList;

        public WifiAdapter(Context context, List<WifiBean> ssidList) {
            this.context = context;
            this.ssidList = ssidList;
        }

        @Override
        public int getCount() {
            return ssidList.size();
        }

        @Override
        public Object getItem(int position) {
            return ssidList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.wifi_settings_item, null);
                holder = new ViewHolder();
                holder.SSIDTv = convertView.findViewById(R.id.wifi_settings_ssid_tv);
                holder.connectedIv = convertView.findViewById(R.id.wifi_settings_connected_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            WifiBean item = ssidList.get(position);
            if (item.isConnected()) {
                holder.connectedIv.setVisibility(View.VISIBLE);
            } else {
                holder.connectedIv.setVisibility(View.GONE);
            }
            holder.SSIDTv.setText(item.getSsid());

            return convertView;
        }

        class ViewHolder {
            TextView SSIDTv;
            ImageView connectedIv;
        }
    }
}
