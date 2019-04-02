package com.axecom.smartweight.ui.activity;

import android.content.Context;
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
import com.axecom.smartweight.bean.CalibrationBean;

/**
 *
 * 订单作废 处理Activity
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
    private Button precisionBtn;
    private GridView digitalGridView;
    private LinearLayout firstLayout;
    private LinearLayout secondLayout;
    private LinearLayout nextStepLayout;
    private ImageView nextStepLineIv;
    private boolean isChecked = false;
    private Context context;

    @Override
    public View setInitView() {
        context = this;
        rootView = LayoutInflater.from(this).inflate(R.layout.calibration_activity, null);
        firstStepTv = rootView.findViewById(R.id.calibration_first_step_tv);
        secondStepTv = rootView.findViewById(R.id.calibration_second_step_tv);
        Button backBtn = rootView.findViewById(R.id.calibration_back_btn);
        Button backBtn2 = rootView.findViewById(R.id.calibration_back_btn2);
        Button nextStepBtn = rootView.findViewById(R.id.calibration_next_step_btn);
        Button doneBtn = rootView.findViewById(R.id.calibration_done_btn);
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

        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(this, DATA_DIGITAL);
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
                firstStepTv.setTextColor(this.getResources().getColor(R.color.white));
                secondStepTv.setTextColor(this.getResources().getColor(R.color.black));
                firstStepTv.setBackground(this.getResources().getDrawable(R.drawable.green_arrow_right));
                secondStepTv.setBackground(this.getResources().getDrawable(R.drawable.white_arrow_right));
                firstLayout.setVisibility(View.VISIBLE);
                secondLayout.setVisibility(View.GONE);
                nextStepLineIv.setVisibility(View.VISIBLE);
                nextStepLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.calibration_second_step_tv:
                secondStepTv.setTextColor(this.getResources().getColor(R.color.white));
                firstStepTv.setTextColor(this.getResources().getColor(R.color.black));
                firstStepTv.setBackground(this.getResources().getDrawable(R.drawable.white_arrow_right));
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
                secondStepTv.setTextColor(this.getResources().getColor(R.color.white));
                firstStepTv.setTextColor(this.getResources().getColor(R.color.black));
                firstStepTv.setBackground(this.getResources().getDrawable(R.drawable.white_arrow_right));
                secondStepTv.setBackground(this.getResources().getDrawable(R.drawable.green_arrow_right));
                firstLayout.setVisibility(View.GONE);
                secondLayout.setVisibility(View.VISIBLE);
                nextStepLineIv.setVisibility(View.GONE);
                nextStepLayout.setVisibility(View.GONE);
                break;
            case R.id.calibration_done_btn:
                CalibrationBean bean = new CalibrationBean();

                bean.setMax_ange(maxAngeEt.getText().toString());
                bean.setCalibration_value(calibrationValueEt.getText().toString());
                bean.setDividing_value(dividingValueEt.getText().toString());
                bean.setStandard_weighing(standardWeighingTv.getText().toString());


                break;
            case R.id.calibration_dcalibration_precision_btn:
                if (isChecked) {
                    precisionBtn.setBackground(this.getResources().getDrawable(R.drawable.shape_green_bg));
                    precisionBtn.setTextColor(this.getResources().getColor(R.color.white));
                } else {
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


/*    class WeightThread extends Thread {
        @Override
        public void run() {

            InputStream inputStream = BtHelperClient.from(CalibrationActivity.this).mInputStream;
            if (inputStream != null) {
                while (flag) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        String s = reader.readLine();
//                        LogWriteUtils.d("-----------weight: " + s);
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


    class KeyBoardAdapter extends BaseAdapter {
        private Context context;
        private String[] digitals;

        KeyBoardAdapter(Context context, String[] digitals) {
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
            ViewHolder holder;
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
