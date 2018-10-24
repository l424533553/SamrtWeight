package com.axecom.smartweight.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BaseActivity;
import com.axecom.smartweight.base.BaseEntity;
import com.axecom.smartweight.bean.CalibrationBean;
import com.axecom.smartweight.manager.AccountManager;
import com.axecom.smartweight.net.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018-5-29.
 */

public class CalibrationActivity extends BaseActivity {
    private static final String[] DATA_DIGITAL = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "删除", "0", "."};

    private View rootView;
    private TextView firstStepTv;
    private TextView secondStepTv;
    private TextView standardWeighingTv;
    private EditText maxAngeEt;
    private EditText dividingValueEt;
    private EditText calibrationValueEt;
    private Button backBtn;
    private Button backBtn2;
    private Button nextStepBtn;
    private Button doneBtn;
    private Button precisionBtn;
    private GridView digitalGridView;
    private KeyBoardAdapter keyBoardAdapter;
    private LinearLayout firstLayout;
    private LinearLayout secondLayout;
    private LinearLayout nextStepLayout;
    private ImageView nextStepLineIv;
    private boolean isShowStaffLogin = true;
    private boolean isChecked = false;
    private String deviceAddress;
    ThreadPoolExecutor executor;
    private TextView weightTv;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.calibration_activity, null);
        firstStepTv = rootView.findViewById(R.id.calibration_first_step_tv);
        secondStepTv = rootView.findViewById(R.id.calibration_second_step_tv);
        backBtn = rootView.findViewById(R.id.calibration_back_btn);
        backBtn2 = rootView.findViewById(R.id.calibration_back_btn2);
        nextStepBtn = rootView.findViewById(R.id.calibration_next_step_btn);
        doneBtn = rootView.findViewById(R.id.calibration_done_btn);
        precisionBtn = rootView.findViewById(R.id.calibration_dcalibration_precision_btn);
        digitalGridView = rootView.findViewById(R.id.calibration_digital_keys_grid_view);
        firstLayout = rootView.findViewById(R.id.calibration_first_layout);
        secondLayout = rootView.findViewById(R.id.calibration_second_layout);
        standardWeighingTv = rootView.findViewById(R.id.calibration_standard_weighing_tv);
        maxAngeEt = rootView.findViewById(R.id.calibration_max_ange_et);
        maxAngeEt.requestFocus();
        dividingValueEt = rootView.findViewById(R.id.calibration_dividing_value_et);
        calibrationValueEt = rootView.findViewById(R.id.calibration_dcalibration_value_et);
        nextStepLineIv = rootView.findViewById(R.id.calibration_next_step_line_iv);
        nextStepLayout = rootView.findViewById(R.id.calibration_next_step_layout);
        weightTv = rootView.findViewById(R.id.calibration_weight_tv);


        disableShowInput(maxAngeEt);
        disableShowInput(dividingValueEt);
        disableShowInput(calibrationValueEt);
        firstStepTv.setOnClickListener(this);
        secondStepTv.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        backBtn2.setOnClickListener(this);
        nextStepBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        precisionBtn.setOnClickListener(this);
        return rootView;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initView() {
        BlockingQueue workQueue = new LinkedBlockingDeque<>();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        executor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.DAYS, workQueue, threadFactory);
//        executor.submit(new WeightThread());
//
//        builder = new BTHelperDialog.Builder(this);
//        deviceAddress = SPUtils.getString(this, BTHelperDialog.KEY_BT_ADDRESS, "");
//        reConnect(deviceAddress);

        List<String> digitaList = new ArrayList<>();
        for (int i = 0; i < DATA_DIGITAL.length; i++) {
            digitaList.add(DATA_DIGITAL[i]);
        }
        keyBoardAdapter = new KeyBoardAdapter(this, DATA_DIGITAL);
        digitalGridView.setAdapter(keyBoardAdapter);
        digitalGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();
                if (rootView.findFocus() != null) {
                    switch (rootView.findFocus().getId()) {
                        case R.id.calibration_max_ange_et:
                            setEditText(maxAngeEt, position, item);
                            break;
                        case R.id.calibration_dividing_value_et:
                            setEditText(dividingValueEt, position, item);
                            break;
                        case R.id.calibration_dcalibration_value_et:
                            setEditText(calibrationValueEt, position, item);
                            break;
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calibration_first_step_tv:
                firstStepTv.setTextColor(this.getResources().getColor( R.color.white));
                secondStepTv.setTextColor(this.getResources().getColor( R.color.black));
                firstStepTv.setBackground(this.getResources().getDrawable( R.drawable.green_arrow_right));
                secondStepTv.setBackground(this.getResources().getDrawable( R.drawable.white_arrow_right));
                firstLayout.setVisibility(View.VISIBLE);
                secondLayout.setVisibility(View.GONE);
                nextStepLineIv.setVisibility(View.VISIBLE);
                nextStepLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.calibration_second_step_tv:
                secondStepTv.setTextColor(this.getResources().getColor( R.color.white));
                firstStepTv.setTextColor(this.getResources().getColor(R.color.black));
                firstStepTv.setBackground(this.getResources().getDrawable( R.drawable.white_arrow_right));
                secondStepTv.setBackground(this.getResources().getDrawable(R.drawable.green_arrow_right));
                firstLayout.setVisibility(View.GONE);
                secondLayout.setVisibility(View.VISIBLE);
                nextStepLineIv.setVisibility(View.GONE);
                nextStepLayout.setVisibility(View.GONE);
                break;
            case R.id.calibration_back_btn:
            case R.id.calibration_back_btn2:
                finish();
                break;
            case R.id.calibration_next_step_btn:
                secondStepTv.setTextColor(this.getResources().getColor( R.color.white));
                firstStepTv.setTextColor(this.getResources().getColor( R.color.black));
                firstStepTv.setBackground(this.getResources().getDrawable(R.drawable.white_arrow_right));
                secondStepTv.setBackground(this.getResources().getDrawable( R.drawable.green_arrow_right));
                firstLayout.setVisibility(View.GONE);
                secondLayout.setVisibility(View.VISIBLE);
                nextStepLineIv.setVisibility(View.GONE);
                nextStepLayout.setVisibility(View.GONE);
                break;
            case R.id.calibration_done_btn:
                CalibrationBean bean = new CalibrationBean();
                bean.setAdminToken(AccountManager.getInstance().getAdminToken());
                bean.setMax_ange(maxAngeEt.getText().toString());
                bean.setCalibration_value(calibrationValueEt.getText().toString());
                bean.setDividing_value(dividingValueEt.getText().toString());
                bean.setStandard_weighing(standardWeighingTv.getText().toString());
                bean.setScales_id(AccountManager.getInstance().getScalesId());
                storeCalibrationRecord(bean);
                break;
            case R.id.calibration_dcalibration_precision_btn:
                if(isChecked){
                    precisionBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_green_bg));
                    precisionBtn.setTextColor(this.getResources().getColor( R.color.white));
                }else {
                    precisionBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_weight_display_bg));
                    precisionBtn.setTextColor(this.getResources().getColor(R.color.gray_ccc));
                }
                isChecked = !isChecked;
                break;
        }

    }

//    public void reConnect(final String deviceAddress) {
//        if (!TextUtils.isEmpty(deviceAddress)) {
//            BtHelperClient btHelperClient = BtHelperClient.from(this);
//            btHelperClient.requestEnableBt();
//            btHelperClient.connectDevice(deviceAddress, new IErrorListener() {
//                @Override
//                public void onError(Exception e) {
//                    showLoading("蓝牙秤连接失败，请重试");
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onConnected(BtHelperClient.STATUS mCurrStatus) {
//                    SPUtils.putString(CalibrationActivity.this, BTHelperDialog.KEY_BT_ADDRESS, deviceAddress);
//                    executor.submit(new WeightThread());
//                }
//
//            });
//        } else {
//            builder.create(new BTHelperDialog.OnBtnClickListener() {
//
//                @Override
//                public void onConfirmed(BtHelperClient.STATUS mCurrStatus, String deviceAddress) {
//                    SPUtils.putString(CalibrationActivity.this, BTHelperDialog.KEY_BT_ADDRESS, deviceAddress);
//                    executor.submit(new WeightThread());
//                }
//
//                @Override
//                public void onCanceled(String result) {
//
//                }
//            }).show();
//        }
//
//    }

    private boolean flag = true;

/*    class WeightThread extends Thread {
        @Override
        public void run() {

            InputStream inputStream = BtHelperClient.from(CalibrationActivity.this).mInputStream;
            if (inputStream != null) {
                while (flag) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        String s = reader.readLine();
//                        LogUtils.d("-----------weight: " + s);
                        Message msg = Message.obtain();
                        msg.obj = s;
                        weightHandler.sendMessage(msg);
                        Thread.sleep(500);
//                        if (inputStream.available() == 0) {
//                            break;
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }


            }
                } else {
                }
        }
    }*/

    private Handler weightHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weightTv.setText(msg.obj.toString().trim());
            standardWeighingTv.setText(msg.obj.toString().trim());
        }
    };

    public void storeCalibrationRecord(CalibrationBean calibrationBean) {
        RetrofitFactory.getInstance().API()
                .storeCalibrationRecord(calibrationBean)
                .compose(this.<BaseEntity>setThread())
                .subscribe(new Observer<BaseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();

                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        showLoading(baseEntity.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeLoading();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeLoading();
                            }
                        });
                    }
                });

    }

    class KeyBoardAdapter extends BaseAdapter {
        private Context context;
        private String[] digitals;

        public KeyBoardAdapter(Context context, String[] digitals) {
            this.context = context;
            this.digitals = digitals;
        }

        @Override
        public int getCount() {
            return digitals.length;
        }

        @Override
        public Object getItem(int position) {
            return digitals[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.calibration_keyborad_item, null);
                holder = new ViewHolder();
                holder.keyBtn = convertView.findViewById(R.id.keyboard_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.keyBtn.setText(digitals[position]);
            return convertView;
        }
    }

    class ViewHolder {
        Button keyBtn;
    }
}
