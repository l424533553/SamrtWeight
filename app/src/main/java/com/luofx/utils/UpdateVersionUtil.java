package com.luofx.utils;//package com.luofx.utils;//package com.coolshow.mybmobtest.luofx.utils;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Environment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.luofx.config.MyConstants;
//import com.luofx.entity.OutputInfo;
//import com.luofx.listener.MyOnClickListener;
//import com.luofx.utils.apk.MyApkUtils;
//import com.shangtongyin.tools.serialport.R;
//
///**
// * @author wenjie 版本更新的工具类
// */
//public class UpdateVersionUtil implements MyConstants {
//
//    private static Context context;
//
//    /**
//     * 接口回调
//     *
//     * @author wenjie
//     */
//    public interface UpdateListener {
//        void onUpdateReturned(int updateStatus, OutputInfo versionInfo);
//    }
//
//    public UpdateListener updateListener;
//
//    public void setUpdateListener(UpdateListener updateListener) {
//        this.updateListener = updateListener;
//    }
//
//    /**
//     * 获取下载路径
//     *
//     * @return
//     */
//    public static String getDownDir(Context context) {
////        String publicDir = context.getExternalCacheDir().toString();
//
//
//        String publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
////        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
////            File sd = new File(Environment.getExternalStorageDirectory().getPath());
////            MyLog.logTest("sd = " + sd.canWrite());//sd = /storage/emulated/0
////
////        }
//
//        return publicDir;
//    }
//
//    /**
//     * 网络测试 检测版本
//     * @param context 上下文
//     */
////	public static void checkVersion(final Context context, final UpdateListener updateListener){
////		HttpRequest.get(ServerReqAddress.UPDATA_VERSION_REQ, new HttpRequest.RequestCallBackListener() {
////
////			@Override
////			public void onSuccess(String resultData) {
////				try {
////					JSONObject jsonObject = JsonUtil.stringToJson(resultData);
////					JSONArray array = jsonObject.getJSONArray("data");
////					VersionInfo mVersionInfo = JsonUtil.jsonToBean(array.getJSONObject(0).toString(), VersionInfo.class);
////					int clientVersionCode = MyApkUtils.getVersionCode(context);
////					int serverVersionCode = mVersionInfo.getVersionCode();
////					//有新版本
////					if(clientVersionCode < serverVersionCode){
////						int i = NetworkUtil.checkedNetWorkType(context);
////						if(i == NetworkUtil.NOWIFI){
////							updateListener.onUpdateReturned(UpdateStatus.NOWIFI,mVersionInfo);
////						}else if(i == NetworkUtil.WIFI){
////							updateListener.onUpdateReturned(UpdateStatus.YES,mVersionInfo);
////						}
////					}else{
////						//无新本
////						updateListener.onUpdateReturned(UpdateStatus.NO,null);
////					}
////				} catch (Exception e) {
////					e.printStackTrace();
////					updateListener.onUpdateReturned(UpdateStatus.ERROR,null);
////				}
////			}
////
////			@Override
////			public void onFailure(String error) {
////				updateListener.onUpdateReturned(UpdateStatus.TIMEOUT,null);
////
////			}
////		});
////	}
//
//
//    /**
//     * 本地测试
//     */
////	public static void localCheckedVersion(final Context context, final UpdateListener updateListener){
////		try {
//////			JSONObject jsonObject = JsonUtil.stringToJson(resultData);
//////			JSONArray array = jsonObject.getJSONArray("data");
//////			VersionInfo mVersionInfo = JsonUtil.jsonToBean(array.getJSONObject(0).toString(), VersionInfo.class);
////			VersionInfo mVersionInfo = new VersionInfo();
////			mVersionInfo.setDownloadUrl("http://gdown.baidu.com/data/wisegame/57a788487345e938/QQ_358.apk");
////			mVersionInfo.setVersionDesc("\n更新内容：\n1、增加xxxxx功能\n2、增加xxxx显示！\n3、用户界面优化！\n4、xxxxxx！");
////			mVersionInfo.setVersionCode(2);
////			mVersionInfo.setVersionName("v2020");
////			mVersionInfo.setVersionSize("20.1M");
////			mVersionInfo.setId("1");
////
////
////			int clientVersionCode = MyApkUtils.getVersionCode(context);
////			int serverVersionCode = mVersionInfo.getVersionCode();
////
////
//////			//TODO
//////			clientVersionCode=2;
//////			serverVersionCode=2;
////			//有新版本
////			if(clientVersionCode < serverVersionCode){
////				int i = NetworkUtil.checkedNetWorkType(context);
////				if(i == NetworkUtil.NOWIFI){
////					updateListener.onUpdateReturned(UpdateStatus.NOWIFI,mVersionInfo);
////				}else if(i == NetworkUtil.WIFI){
////					updateListener.onUpdateReturned(UpdateStatus.YES,mVersionInfo);
////				}
////			}else{
////				//无新本
////				updateListener.onUpdateReturned(UpdateStatus.NO,null);
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////			updateListener.onUpdateReturned(UpdateStatus.ERROR,null);
////		}
////	}
//
//    private static Dialog dialog;
//
//    /**
//     * 弹出新版本提示
//     *
//     * @param context     上下文
//     * @param versionInfo 更新内容
//     */
//    public  void charge(final Context context, final OutputInfo versionInfo) {
//        if (dialog != null) {
//            dialog = null;
//        }
//
//        dialog = new AlertDialog.Builder(context).create();
////        final File file = new File(getDownDir() + "/" + versionInfo.getOutputFile());
//        dialog.setCancelable(false);// 可以用“返回键”取消
//        dialog.setCanceledOnTouchOutside(false);//
//        dialog.show();
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_version_update, null);
//        dialog.setContentView(view);
//
//        final Button btnOk = view.findViewById(R.id.btn_update_id_ok);
//        Button btnCancel = view.findViewById(R.id.btn_update_id_cancel);
//        TextView tvContent = view.findViewById(R.id.tv_update_content);
//        TextView tvUpdateTile = view.findViewById(R.id.tv_update_title);
//        final TextView tvUpdateMsgSize = view.findViewById(R.id.tv_update_msg_size);
//
//        tvContent.setText(versionInfo.getVersionDesc());
//        tvUpdateTile.setText("最新版本：" + versionInfo.getVersionName());
//
////        if (file.exists() && file.getName().equals(versionInfo.getApkName())) {
////            tvUpdateMsgSize.setText("新版本已经下载，是否安装？");
////        } else {
////        }
//
//        tvUpdateMsgSize.setText("新版本大小：" + versionInfo.getVersionSize());
//
//        btnOk.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (v.getId() == R.id.btn_update_id_ok) {
//                    //新版本已经下载
////                    if (file.exists() && APK_NAME.equals(file.getName())) {
////                    if (file.exists()) {
////                        Intent intent = MyApkUtils.getInstallIntent(file);
////                        context.startActivity(intent);
////                    } else {
//                    //没有下载，则开启服务下载新版本
//
//
////                    Intent intent = new Intent(context, UpdateVersionService.class);
////                    //TODO 需要修改 添加
////                    intent.putExtra("downloadUrl", versionInfo.getDownloadUrl());
////                    intent.putExtra("apkName", versionInfo.getApkName());
////                    context.startService(intent);
//////                    }
//
//
////                    downFile(final String url);
//
//                    myOnClickListener.myOnClick(versionInfo.getApkName());
//
//
//                }
//            }
//        });
//
//        btnCancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ceshi
////                MyApkUtils.installUseAS(context, "/storage/emulated/0/Download/TraceBack.apk");
//
//
//                dialog.dismiss();
//            }
//        });
//    }
//
//
//    private static MyOnClickListener myOnClickListener;
//
//    public MyOnClickListener getMyOnClickListener() {
//        return myOnClickListener;
//    }
//
//    public  void setMyOnClickListener(MyOnClickListener myOnClickListener) {
//        UpdateVersionUtil.myOnClickListener = myOnClickListener;
//    }
//
//    /**
//     * 弹出  关于我们信息
//     *
//     * @param context 上下文
//     */
//    public static void showHelpDialog(final Context context) {
//        if (dialog != null) {
//            dialog = null;
//        }
//
//        dialog = new AlertDialog.Builder(context).create();
//
//        dialog.setCancelable(false);// 可以用“返回键”取消
//        dialog.setCanceledOnTouchOutside(false);//
//        dialog.show();
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_version_help, null);
//
//        TextView tvAppName = view.findViewById(R.id.tvAppName);
//        String versionName = "软件版本名:    软件名" ;
//        tvAppName.setText(versionName);
//
//        TextView tvAppCode = view.findViewById(R.id.tvAppCode);
//        String apkCode = "软件版本号:" + MyApkUtils.getVersionName(context);
//        tvAppCode.setText(apkCode);
//
//        dialog.setContentView(view);
//        Button btnOk = view.findViewById(R.id.btn_update_id_ok);
//        btnOk.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }
//}
