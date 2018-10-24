package com.axecom.smartweight.manager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.axecom.smartweight.R;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
//import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
//import com.gprinter.io.GpDevice;
//import com.gprinter.io.PortParameters;
//import com.gprinter.service.GpPrintService;

import java.util.Vector;

import static com.axecom.smartweight.ui.uiutils.UIUtils.getResources;

/**
 * Created by Administrator on 2018-5-28.
 */

public class GPprinterManager {
    private static Context context;
    private int mPrinterId = 1;
    private static final int REQUEST_TOAST_PRINTER_STATUS = 1;
    private int mPrinterIndex = 0;

    private static final String TAG = "ServiceConnection";
    private GpService mService;
    private PrinterServiceConnection conn = null;

    public GPprinterManager(Context context){
        this.context = context;
           /* 绑定服务，绑定成功后调用ServiceConnection中的onServiceConnected方法 */
        conn = new PrinterServiceConnection();
        registerBroadcast();

//        Intent intent = new Intent(context, GpPrintService.class);
//        context.bindService(intent, conn, context.BIND_AUTO_CREATE);
    }

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mService = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = GpService.Stub.asInterface(service);
        }
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
//        filter.addAction(GpCom.ACTION_CONNECT_STATUS);
        context.registerReceiver(PrinterStatusBroadcastReceiver, filter);
    }
    /**
     * 打印机连接回调
     */

    private BroadcastReceiver PrinterStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
//                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
//                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
//                Log.d(TAG, "connect status " + type);
//                if (type == GpDevice.STATE_CONNECTING) {
//
//                } else if (type == GpDevice.STATE_NONE) {
//                } else if (type == GpDevice.STATE_VALID_PRINTER) {
//                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
//                }
//            }
        }
    };
//        @Override
//        public IBinder asBinder() {
//            return null;
//        }
    public static byte[] ByteTo_byte(Vector<Byte> vector) {
        int len = vector.size();
        byte[] data = new byte[len];

        for(int i = 0; i < len; ++i) {
            data[i] = ((Byte)vector.get(i)).byteValue();
        }

        return data;
    }

    public void printTest(){

    }


    /**
     * 打开连接
     *
     */
    public void openConnect(String deviceAddress) {
        //id为打印服务操作的打印机的id，最大可以操作3台
//        try {
            Toast.makeText(context, "openConnect", Toast.LENGTH_SHORT).show();;
//            mService.openPort(mPrinterId, PortParameters.USB, deviceAddress, 0);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * 打印测试页
     *
     */
    public void printTestPaper() {
        try {
            mService.printeTestPage(mPrinterId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     *
     */
    public void closeConnect() {
        if (mService != null) {
            try {
                mService.closePort(mPrinterId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        context.unbindService(conn);
    }

    /**
     * 获取命令类型
     *
     */
    public void getCommandTypes() {
        try {
            final int type = mService.getPrinterCommandType(mPrinterId);
//            if(type == GpCom.ESC_COMMAND){
//                LogUtils.d("ESC Command");
//            }else {
//                LogUtils.d("TSC Command");
//            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    public void sendReceipt(){
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("Sample\n"); // 打印文字
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("Print text\n"); // 打印文字
        esc.addText("Welcome to use SMARNET printer!\n"); // 打印文字

		/* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        // esc.addText(message,"BIG5");
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

		/* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 6);
        esc.addText("网络");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("设备");
        esc.addPrintAndLineFeed();

		/* 打印图片 */
        esc.addText("Print bitmap!\n"); // 打印文字
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        esc.addRastBitImage(b, 384, 0); // 打印图片

		/* 打印一维条码 */
        esc.addText("Print code128\n"); // 打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//
        // 设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
        esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
        esc.addCODE128(esc.genCodeB("SMARNET")); // 打印Code128码
        esc.addPrintAndLineFeed();

		/*
		 * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
        esc.addText("Print QRcode\n"); // 打印文字
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 3);// 设置qrcode模块大小
        esc.addStoreQRCodeData("www.smarnet.cc");// 设置qrcode内容
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("Completed!\r\n"); // 打印结束
        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 8);

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mService.sendEscCommand(mPrinterIndex, sss);
//            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
//            if (r != GpCom.ERROR_CODE.SUCCESS) {
//                Toast.makeText(context, GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
//            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   public void sendLabel() {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        tsc.addText(20, 30, LabelCommand.FONTTYPE.KOREAN, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "조선말");
        tsc.addText(100, 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "简体字");
        tsc.addText(180, 30, LabelCommand.FONTTYPE.TRADITIONAL_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "繁體字");

        // 绘制图片
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        tsc.addBitmap(20, 60, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);
        //绘制二维码
        tsc.addQRCode(105, 75, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
        // 绘制一维条码
        tsc.add1DBarcode(50, 350, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
//        try {
//            rel = mService.sendLabelCommand(mPrinterIndex, str);
//            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
//            if (r != GpCom.ERROR_CODE.SUCCESS) {
//                Toast.makeText(context, GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

}
