package com.axecom.smartweight.activity.main.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.activity.SecondScreen;
import com.axecom.smartweight.activity.common.LockActivity;
import com.axecom.smartweight.activity.common.SettingsActivity;
import com.axecom.smartweight.activity.common.mvvm.home.HomeActivity;
import com.axecom.smartweight.activity.main.viewmodel.MainVM;
import com.axecom.smartweight.activity.setting.SystemLoginActivity;
import com.axecom.smartweight.adapter.BackgroundData;
import com.axecom.smartweight.adapter.DigitalAdapter;
import com.axecom.smartweight.adapter.GoodMenuAdapter;
import com.axecom.smartweight.adapter.OrderAdapter;
import com.axecom.smartweight.config.IEventBus;
import com.axecom.smartweight.databinding.ActivityMainBinding;
import com.axecom.smartweight.entity.netresult.OrderResultBean;
import com.axecom.smartweight.entity.netresult.ResultInfo;
import com.axecom.smartweight.entity.project.AdResultBean;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.axecom.smartweight.helper.HttpHelper;
import com.axecom.smartweight.mvvm.view.IAllView;
import com.axecom.smartweight.rzl.utils.ApkUtils;
import com.axecom.smartweight.rzl.utils.DownloadDialog;
import com.axecom.smartweight.rzl.utils.Version;
import com.axecom.smartweight.service.CustomAlertDialog;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.PowerConsumptionRankingsBatteryView;
import com.xuanyuan.library.base2.BaseBuglyApplication;
import com.xuanyuan.library.help.CashierInputFilter2;
import com.xuanyuan.library.listener.VolleyListener;
import com.xuanyuan.library.listener.VolleyStringListener;
import com.xuanyuan.library.utils.MyDateUtils;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;
import com.xuanyuan.library.utils.system.SystemInfoUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.axecom.smartweight.mvp.MainPresenter.NOTIFY_GOODS_DATA_CHANGE;

public class MainActivity extends MainBaseACActivity implements IAllView, IEventBus, VolleyListener, VolleyStringListener, View.OnClickListener, View.OnLongClickListener {

    /************************************************************************************/
    private DownloadDialog downloadDialog;
    private ActivityMainBinding binding;

    /**
     * VM
     * 初始化 第一界面
     */
    private void initViewFirst() {
        binding.mainSettingsBtn.setOnLongClickListener(this);
        binding.date.setText(MyDateUtils.getYYMMDD(System.currentTimeMillis()));//显示年月日
        //聚焦
        binding.etPrice.requestFocus();
        InputFilter[] filters = {new CashierInputFilter2()};
        //设置金额过滤器
        binding.etPrice.setFilters(filters);
        //设置金额的输入监控器
        disableShowInput(binding.etPrice);
        listenerAmountInput(binding.etPrice);

        String localVersion = SystemInfoUtils.getVersionName(context) + "." + SystemInfoUtils.getVersionCode(context);
        mainBean.getVersion().set(localVersion);
    }

    /**
     * VM
     * OK 设置软件  背景色
     */
    @Override
    protected void setBackground() {
        int index = MyPreferenceUtils.getSp(context).getInt("BACKGROUND_INDEX", 0);
        binding.llKey.setBackgroundResource(BackgroundData.getData().get(index));
        binding.llorder.setBackgroundResource(BackgroundData.getData().get(index));
        binding.gvGoodMenu.setBackgroundResource(BackgroundData.getData().get(index));
    }

    //检查版本更新
    private void checkVersion(int marketId) {
        downloadDialog = new DownloadDialog(context);
        ApkUtils.checkRemoteVersion(marketId, sysApplication, handler);
    }

    /**
     * 使用
     *
     * @param savedInstanceState 0000J5034789
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setUserInfo(sysApplication.getUserInfo());
        binding.setMainBean(mainBean);
        binding.setOnClickListener(this);

        //1.用户信息可以使用
        mainVM = new MainVM(this);
        mainVM.userInfo = presenter.detectionUserInfo();

        if (mainVM.userInfo != null) {
            initViewFirst();
            initHandler();
            initAdapterView();
            //初始化背景色
            setBackground();
            //初始化信息获得 热键售卖商品信息
            mainVM.initHotGoods();
            initBaseData();
            checkVersion(mainVM.userInfo.getMarketid());//检查版本更新
//          HttpHelper.getmInstants(sysApplication).onCheckVersion(this, userInfo.getMarketid(), 777);
            askOrderState();
        } else {
            jumpActivity(HomeActivity.class, true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        judegIsLock();
    }

    /**
     * TODO 研究测试 Thread 的唤醒 机制
     * 轮询 询问订单信息
     */
    private void askOrderState() {
        sysApplication.getThreadPool().execute(() -> {
            while (loopAskOrdering) {
                try {
                    if (sysApplication.getUserInfo().isConfigureEpayParams()) {//有配置才进行询问请求
                        if (askInfos.size() > 0) {
                            for (int i = askInfos.size() - 1; i >= 0; i--) {
                                OrderInfo orderInfo = askInfos.get(i);
                                long timingTime = orderInfo.getTimingTime();
                                if (timingTime <= 0) {
                                    orderInfo.setTimingTime(System.currentTimeMillis());
                                } else {
                                    if (System.currentTimeMillis() - timingTime >= PAYMENT_TIMEOUT) {//等待支付超过180000（3分钟）
                                        askInfos.remove(i);
                                        continue;
                                    }
                                }
                                HttpHelper.getmInstants(sysApplication).askOrderEx(mainVM.userInfo.getMchid(), askInfos.get(i).getBillcode(), MainActivity.this, 4);
                                Thread.sleep(100);
                            }
                        }
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * VM
     * 获取称重重量信息
     */
    private void initHandler() {
        handler = new Handler(msg -> {
            int what = msg.what;
            switch (what) {
                case NOTIFY_GOODS_DATA_CHANGE:
                    goodMenuAdapter.setDatas(presenter.getHotGoodList());
                    break;
                case NOTIFY_EPAY_SUCCESS:
                    MyLog.blue("支付测试   微信支付");
                    String message = msg.obj.toString();
                    showLoading("0", message, 1500);
                    clearnContext();
                    break;
                case NOTIFY_CLEAR:
                    clearnContext();
                    break;
                case NOTIFY_AD_PICTURE_FORMAT:
                    MyToast.showError(MainActivity.this, "后台图片格式有误");
                    break;
                case 10012:
                    runOnUiThread(() -> {
                        if (msg.arg2 > 0) {
                            downloadDialog.setProgress(msg.arg1, msg.arg2);//arg1:已下载字节数,arg2:总字节数
                        }
                    });
                    break;
                case 10013:
                    final Version v = (Version) msg.obj;
                    runOnUiThread(() -> {
                        downloadDialog.setVersion(v.version);//版本
                        downloadDialog.setDate(v.date);//更新日期
                        downloadDialog.setMarketId(String.valueOf(v.getMarketId()));//市场编号
                        downloadDialog.setDescription(v.description);//更新描述
                        downloadDialog.setApkPath(v.apkPath);//apk路径
                        downloadDialog.show();
                    });
                    break;
                case 10014:
                    runOnUiThread(() -> {
                        downloadDialog.canForceQuit();
                        Toast.makeText(MainActivity.this, "更新App失败,请联系运营人员", Toast.LENGTH_LONG).show();
                    });
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    private void dealWeight() {
//        if (weightBean.getCurrentWeight() <= 0) {
//            if (oldFloat > weightBean.getCurrentWeight()) {
//                binding.etPrice.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
//                Selection.selectAll(binding.etPrice.getText());
//                isEdSelect = true;
//            } else {
//                if (!isEdSelect) {
//                    binding.etPrice.setTextColor(context.getResources().getColor(R.color.black));
//                }
//            }
//        }

        //TODO 测试
        if (weightBean.getCurrentWeight() <= 0) {
            if (!mainBean.isEtPriceSelect()) {
                binding.etPrice.setTextColor(context.getResources().getColor(R.color.color_carsh_pay));
                Selection.selectAll(binding.etPrice.getText());
                mainBean.setEtPriceSelect(true);
            }
        } else {
            binding.etPrice.setTextColor(context.getResources().getColor(R.color.black));
        }

        String weight2 = weightFormat.format(weightBean.getCurrentWeight() * 2);//format 返回的是字符串
        if (weightBean.isNegative()) {
            mainBean.getNetWeight().set("-" + weightFormat.format(weightBean.getCurrentWeight()));
            mainBean.getCarryWewight().set("-" + weight2);
//            binding.tvWeightTop.setTextColor(context.getResources().getColor(R.color.red_000));
        } else {
            mainBean.getNetWeight().set(weightFormat.format(weightBean.getCurrentWeight()));
            mainBean.getCarryWewight().set(weight2);
//            binding.tvWeightTop.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    /**
     * 交易完成后清理内容
     */
    private void clearnContext() {
        orderBeans.clear();
        orderAdapter.notifyDataSetChanged();
        mainBean.getTotalWeight().set(getResources().getString(R.string.default_weight));
        mainBean.getTotalPrice().set(getResources().getString(R.string.default_price));
        mainBean.getHintPrice().set(mainBean.getPrice().get());
//        binding.etPrice.setText("");
    }

    /**
     * 数字按键操作
     */
    private DigitalAdapter digitalAdapter;

    /**
     * 数字 按键操作
     */
    @Override
    public void pressDigital(int position) {
        if (TextUtils.isEmpty(mainBean.getGoodName().get())) {
            return;
        }

        if (mainBean.isEtPriceSelect()) {
            binding.etPrice.getText().clear();
            mainBean.setEtPriceSelect(false);
        }

        String text = digitalAdapter.getItem(position);
        String price = binding.etPrice.getText().toString();
        if ("删除".equals(text)) {
            if (price.length() > 0) {
                String priceNew = price.substring(0, price.length() - 1);
                binding.etPrice.setText(priceNew);
                mainBean.getHintPrice().set(binding.etPrice.getText().toString());
            }
        } else {
            if (".".equals(text)) {
                if (!price.contains(".")) {
                    String priceNew = price + text;
                    binding.etPrice.setText(priceNew);
                    mainBean.getHintPrice().set(binding.etPrice.getText().toString());
                }
            } else {
                if ("0".equals(price)) {
                    price = "";
                }
                String priceNew = price + text;
                binding.etPrice.setText(priceNew);
                mainBean.getHintPrice().set(binding.etPrice.getText().toString());
            }
        }
        dealGrandMoney();
    }

    /**
     * VM
     * 初始化控件
     */
    public void initAdapterView() {

        orderAdapter = new OrderAdapter(this, orderBeans);
        binding.lvCommoditys.setAdapter(orderAdapter);
        binding.lvCommoditys.setOnItemLongClickListener((parent, view, position, id) -> {
            orderBeans.remove(position);
            notifyOrderInfo();
            return true;
        });

        goodMenuAdapter = new GoodMenuAdapter(this);
        binding.gvGoodMenu.setAdapter(goodMenuAdapter);
        binding.gvGoodMenu.setOnItemClickListener((parent, view, position, id) -> selectOrderBean(position));

        digitalAdapter = new DigitalAdapter(this);
        binding.gvDigital.setAdapter(digitalAdapter);
        binding.gvDigital.setOnItemClickListener((parent, view, position, id) -> pressDigital(position));
    }


    /**
     * 进行菜单选择操作
     *
     * @param position 菜单索引
     */
    @Override
    public void selectOrderBean(int position) {
        selectedHotGood = goodMenuAdapter.getItem(position);
        mainBean.getGoodName().set(selectedHotGood.getName());
        mainBean.getPrice().set("");
        binding.etPrice.setText("");
        mainBean.setCid(String.valueOf(selectedHotGood.getCid()));
        mainBean.setBatchCode(selectedHotGood.getBatchCode());
        mainBean.getHintPrice().set(selectedHotGood.getPrice());

        BigDecimal bigPrice = new BigDecimal(mainBean.getHintPrice().get());
        BigDecimal bigWeight = new BigDecimal(mainBean.getNetWeight().get());
        bigPrice = bigPrice.multiply(bigWeight).setScale(2, BigDecimal.ROUND_HALF_UP);
        mainBean.getGrandMoney().set(bigPrice.toPlainString());
        goodMenuAdapter.setCheckedAtPosition(position);
        goodMenuAdapter.notifyDataSetChanged();
    }


    /**
     * 进行LiveDataBus事件处理
     *
     * @param type 事件类型
     */
    @Override
    protected void onLiveEvent(String type) {
        if (EVENT_NET_WORK_AVAILABLE.equals(type)) {
            HttpHelper.getmInstants(sysApplication).updateDataEx();
        } else if (NOTIFY_USERINFO.equals(type)) {
            mainVM.setUserInfo(sysApplication.getUserInfo());
            binding.setUserInfo(sysApplication.getUserInfo());
        } else if (LIVE_EVENT_NOTIFY_HOT_GOOD.equals(type)) {
            goodMenuAdapter.setDatas(mainVM.getHotGoods());
        } else if (BACKGROUND_CHANGE.equals(type)) {
            setBackground();
        } else if (SHOPPING_BAG_PRICE_CHANGE.equals(type)) {
            initBaseData();
        } else if (LOCK_STATE.equals(type)) {
            Intent intent = new Intent(context, LockActivity.class);
            startActivity(intent);
        }
    }

    /**
     * VM
     * 处理称重数据
     *
     * @param event 总线事件数据
     */
    private void weightData(BaseBusEvent event) {
        // 两次重量一样，那么不必再刷新控件了
        float lastWeight = weightBean.getCurrentWeight();
        if (adapterFactory.parseWeightData(event, weightBean)) {
//            if (Math.abs(lastWeight - weightBean.getCurrentWeight()) != 0) {
            dealWeight();
            dealGrandMoney();
//            }
            autoSendOrder();
        } else {
            startCheatLock();
        }
    }

    /**
     * TODO 需要 由 liveDataBus 取代
     * 定义处理接收的方法
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseBusEvent event) {
        String[] array;
        switch (event.getEventType()) {
            case MARKET_NOTICE:
                AdResultBean.DataBean dataBean = (AdResultBean.DataBean) event.getOther();
                CustomAlertDialog alertDialog = new CustomAlertDialog(context, dataBean);
                alertDialog.show();
                break;
            case NOTIFY_AD_SECOND:// 副屏刷新广告图
                if (!BaseBuglyApplication.DEBUG_MODE) {
                    SecondScreen secondScreen = sysApplication.getBanner();
                    if (secondScreen != null) {
                        secondScreen.initData();
                    }
                }
                break;
            case WEIGHT_ST://称重数据变化
                weightData(event);
                break;
            case WEIGHT_SX15:// 重量
                weightData(event);
                break;
            case WEIGHT_ELECTRIC:
                array = (String[]) event.getOther();
                float eleData = Float.valueOf(array[1]);
                if (eleData < 1130) {
                    eleData = 1130f;
                }
                int precent = (int) ((eleData - PowerConsumptionRankingsBatteryView.MinEleve) * PowerConsumptionRankingsBatteryView.ConstantEleve);
//                    int precent = electric / 100;
                int electricState = array[2].charAt(0) - 48;
                if (electricState == 0) {//无充电
                    if (precent <= 20) {
                        binding.ivImage.setImageResource(R.mipmap.ele1);
                        MyToast.showError(context, "电量不足,请及时接通电源！");
                    } else if (precent <= 50) {
                        binding.ivImage.setImageResource(R.mipmap.ele2);
                    } else if (precent <= 80) {
                        binding.ivImage.setImageResource(R.mipmap.ele3);
                    } else {
                        binding.ivImage.setImageResource(R.mipmap.ele4);
                    }
                } else if (electricState == 1) {//充电状态
                    binding.ivImage.setImageResource(R.mipmap.ele0);
                }
                if (precent < 0) {
                    precent = 0;
                } else if (precent >= 100) {
                    precent = 100;
                }
                String precentString = precent + "%";
                binding.tvElePrecent.setText(precentString);

                break;
            case NOTIFY_HOT_GOOD_CHANGE:
                //初始化信息
                mainVM.initHotGoods();
                break;
            default:
                break;
        }
    }


    /*
 * 设置功能 待定算法
 *
 * @param requestCode 请求码
 * @param resultCode  响应码
 * @param data        参数

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_SYS_SET) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(STRING_TYPE, 1);
                startActivityForResult(intent, 1111);
            } else if (requestCode == 1111) {// 数据改变了
                boolean isDataChange = data.getBooleanExtra("isDataChange", false); //将计算的值回传回去
                if (isDataChange) {// 设置改变了
                    if (userInfoDao == null) {
                        userInfoDao = new UserInfoDao();
                    }
                    mainVM.userInfo = userInfoDao.queryById(1);
                    sysApplication.setUserInfo(mainVM.userInfo);
                }
            }
        }
    }
*/

    /**
     * VM
     * 点击操作
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_cash_btn:
                AddDirectlyOrder();
                payCashDirect(0);
                break;
            case R.id.main_scan_pay:
                if (MyNetWorkUtils.isNetWorkError(context)) {
                    MyToast.showError(this, "使用第三方支付需要连接网络！");
                } else {
                    AddDirectlyOrder();
                    payCashDirect(1);
                }
                break;
            case R.id.main_settings_btn:
                jumpActivity(SettingsActivity.class, false);
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivityForResult(intent, 1111);
                break;
            case R.id.tvTid:
                jumpActivity(SettingsActivity.class, false);
//                Intent intent2 = new Intent(this, SettingsActivity.class);
//                startActivityForResult(intent2, 1111);
                break;
            case R.id.btnSendZer://准备置零
                if (weightBean.getCurrentWeight() <= 1.2f) {
                    sysApplication.getMyBaseWeighter().sendZer();
                }
                break;
            case R.id.btnClearn:
                clear();
                break;
            case R.id.btnAdd://累计
                addGoods2OrderList();
                binding.etPrice.getText().clear();
                break;
            case R.id.rlBagLarge:
                btnClickBags(3);
                break;
            case R.id.rlBagMiddle:
                btnClickBags(2);
                break;
            case R.id.rlBagSmall:
                btnClickBags(1);
                break;
            default:
                break;
        }
    }

    /**
     * 直接添加商品
     */
    private void AddDirectlyOrder() {
        if (orderBeans.size() != 0) {
            return;
        }
        if (verifySendDataIllegal(false)) {
            return;
        }
        //同时 进行价格的保存
        presenter.updateHotGoodPrice(selectedHotGood, mainBean.getReallyPrice());
        long currentLongTime = System.currentTimeMillis();
        OrderBean bean = createOrderBean(currentLongTime);
        orderBeans.add(bean);
    }

    /**
     * 累计 菜品价格
     */
    private void addGoods2OrderList() {
        if (verifySendDataIllegal(false)) {
            return;
        }

        //同时 进行价格的保存
        presenter.updateHotGoodPrice(selectedHotGood, mainBean.getReallyPrice());
        long currentLongTime = System.currentTimeMillis();
        OrderBean orderBean = createOrderBean(currentLongTime);
        orderBeans.add(orderBean);

        notifyOrderInfo();
    }


    /**
     * VM
     * 支付结算
     *
     * @param payType 0:現金支付   1：扫码支付
     */
    public void payCashDirect(int payType) {
        final List<OrderBean> orderlist = new ArrayList<>(orderBeans);
        OrderInfo orderInfo = sendOrder2AxeAndFmps(orderlist, mainVM.userInfo, false, payType);
        if (orderInfo == null) {
            return;
        }

        String message = "支付金额：" + priceFormat.format(Float.valueOf(orderInfo.getTotalamount())) + "元";
        int delayTimes = 1500;
        showLoading("0", message, delayTimes);
        handler.sendEmptyMessage(NOTIFY_CLEAR);
        sysApplication.getPrint().printOrder(sysApplication.getSingleThread(), orderInfo);

        for (int i = 0; i < orderlist.size(); i++) {
            OrderBean goods = orderlist.get(i);
            goods.setOrderInfo(orderInfo);
        }
        mainVM.getiMainModel().insertOrderInfo(orderInfo);
        mainVM.getiMainModel().insertOrderBean(orderlist);

        askInfos.add(orderInfo);
        banner.notifyData(orderInfo);
    }


    /**
     * 连接错误，如果网络失败
     *
     * @param volleyError 错误信息
     * @param flag        请求表示索引
     */
    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        switch (flag) {
            case 3:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
            case 4:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
            case 5:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
            case 777:
                MyLog.myInfo("更新错误" + volleyError.getMessage());
                break;
        }
    }

    @Override
    public void onResponse(final JSONObject jsonObject, final int flag) {
        switch (flag) {
            case 3:
                MyLog.myInfo("成功" + jsonObject.toString());
                break;
            case 777:
                MyLog.myInfo("成功" + jsonObject.toString());
                break;
            case 4://询问支付状态
                sysApplication.getThreadPool().execute(() -> {
                    ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
                    if (resultInfo != null) {
                        if (resultInfo.getStatus() == 0) {//支付成功
                            String orderNo = resultInfo.getData();
                            for (OrderInfo orderInfo : askInfos) {
                                if (orderInfo.getBillcode().equals(orderNo)) {
                                    askInfos.remove(orderInfo);
                                    Message message = new Message();
                                    String msg = "支付金额：" + orderInfo.getTotalamount() + "元";
                                    message.what = NOTIFY_EPAY_SUCCESS;// 订单支付成功
                                    message.obj = msg;
                                    handler.sendMessage(message);
                                    break;
                                }
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onStringResponse(VolleyError volleyError, int flag) {
        MyLog.myInfo("错误" + volleyError);
        switch (flag) {
            case 3:
                MyLog.myInfo("失败" + volleyError.toString());
                break;
            case 5:
                MyLog.myInfo("错误" + volleyError.getMessage());
                break;
            default:
                break;
        }
    }

    //  返回支付状态
    @Override
    public void onStringResponse(String response, int flag) {
        try {
            if (flag == 3) {
                OrderResultBean resultInfo = JSON.parseObject(response, OrderResultBean.class);
                if (resultInfo != null && resultInfo.getStatus() == 0) {
                    String billcode = resultInfo.getData().getBillcode();//交易号
                    mainVM.getiMainModel().updateOrderBillcode(billcode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * VM
     * 主页面点击返回键
     */
    @Override
    public void onBackPressed() {
        if (!mainVM.finishAll()) {
            MyToast.toastShort(this, "再次点击退出！");
        }
    }

    /**
     * VM,点击长按按钮数据
     */
    @Override
    public boolean onLongClick(View v) {
        //进入系统设置，密码验证界面
        if (v.getId() == R.id.main_settings_btn) {
            jumpActivity(SystemLoginActivity.class, false);
        }
        return false;
    }


}