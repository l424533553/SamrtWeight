package com.axecom.smartweight.helper.printer;

import com.axecom.smartweight.activity.main.model.WeightFieldBean;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.axecom.smartweight.helper.weighter.AXEWeighter;
import com.axecom.smartweight.helper.weighter.MyBaseWeighter;
import com.axecom.smartweight.helper.weighter.STWeighter;
import com.axecom.smartweight.helper.weighter.SXWeighter;
import com.axecom.smartweight.helper.weighter.XSWeighter15;
import com.axecom.smartweight.helper.weighter.XSWeighter8;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.MyPreferenceUtils;

import static com.axecom.smartweight.config.IConstants.PRINTER_HHD;
import static com.axecom.smartweight.config.IConstants.PRINTER_TYPE;

/**
 * 作者：罗发新
 * 时间：2019/8/9 0009    星期五
 * 邮件：424533553@qq.com
 * 说明：打印机和称重模块工厂       0为商通   1 为XS15  2 为XS8寸  3 axe自研秤   4 深信秤
 */
public class AdapterFactory {

    private int tidType;
    // 打印机
    private MyBasePrinter printer;
    // 称重模块
    private MyBaseWeighter weighter;


    public AdapterFactory(int tidType) {
        this.tidType = tidType;
    }

    public MyBasePrinter getPrinter() {
        return printer;
    }

    public MyBaseWeighter getWeighter() {
        return weighter;
    }

    private boolean isOpenPrinter;

    /**
     * @param tidType 称的类型
     *                printerIndex 同一种秤可能 有多款打印机，用printerIndex 区分
     * @return 是否成功打开了 打印机
     */
    public boolean createPrinter(int tidType) {
        if (tidType == 0) {
            printer = new EPSPrint(EPSPrint.PATH_ST, EPSPrint.BAUDRATE_ST);
        } else if (tidType == 1) {
            int printerIndex = MyPreferenceUtils.getSp(SysApplication.getInstance()).getInt(PRINTER_TYPE, PRINTER_HHD);
            if (printerIndex == 0) {
                printer = new HDDPrinter(HDDPrinter.PATH_XS15, HDDPrinter.BAUDRATE_XS15);
            } else {
                printer = new WHPrinterE39(WHPrinterE39.PATH_XS15, WHPrinterE39.BAUDRATE_XS15);
            }
        } else if (tidType == 2) {
            printer = new WHPrinter8(WHPrinter8.PATH_XS8, WHPrinter8.BAUDRATE_XS8);
        } else if (tidType == 3) {
            //TODO  自研秤打印机好了之后 ，请恢复下面的代码
//            print = new WHPrinterAXE(WHPrinterAXE.PATH_AXE, WHPrinterAXE.BAUDRATE_AXE);
        } else if (tidType == 4) {
            //TODO  深信秤
        }

        if (printer != null) {
            isOpenPrinter = printer.open();
            return isOpenPrinter;
        }
        return false;
    }

    private boolean isOpenWeighter;

    /**
     * @param tidType 秤类型
     * @return 是否称重模块初始化成功
     */
    public boolean createWeighter(int tidType) {
        if (tidType == 0) {
            weighter = new STWeighter();
        } else if (tidType == 1) {
            weighter = new XSWeighter15();
        } else if (tidType == 2) {
            weighter = new XSWeighter8();
        } else if (tidType == 3) {
            weighter = new AXEWeighter();
        } else if (tidType == 4) {
            weighter = new SXWeighter();

        }
        if (weighter != null) {
            isOpenWeighter = weighter.open();
            return isOpenWeighter;
        }
        return false;
    }

    /**
     * 解析称重数据
     *
     * @return false 数据有作弊，true数据可用
     */
    public boolean parseWeightData(BaseBusEvent event, WeightFieldBean weightBean) {
        switch (tidType) {
            case 0:
                float weightFloat = Float.parseFloat((String) event.getOther());
                weightBean.setCurrentWeight(weightFloat);
                setWeightTime(weightBean);
                break;
            case 1:
                String[] array = (String[]) event.getOther();
                parseWeightDataXS15(array, weightBean);
                break;
            default:
                break;
        }
        return weightBean.getCheatSign() != 1;
    }

    /**
     * @param array 称重数据的解析
     * @return 返回数据是否可用
     */
    private void parseWeightDataXS15(String[] array, WeightFieldBean weightBean) {
        weightBean.setCurrentAd(Integer.parseInt(array[1]));
        weightBean.setZeroAd((int) (Integer.parseInt(array[2]) + Math.random() * 10));
        weightBean.setCurrentWeight(Float.parseFloat(array[3]) / 1000);
        weightBean.setTareWeight(Float.parseFloat(array[4]) / 1000);
        weightBean.setCheatSign(array[5].charAt(0) - 48);
        weightBean.setIsNegative(array[5].charAt(1) - 48);
        weightBean.setIsOver(array[5].charAt(2) - 48);
        weightBean.setIsZero(array[5].charAt(3) - 48);
        weightBean.setIsPeeled(array[5].charAt(4) - 48);
        weightBean.setIsStable(array[5].charAt(5) - 48);
        setWeightTime(weightBean);
    }

    private void setWeightTime(WeightFieldBean weightBean) {
        long currentTime = System.currentTimeMillis();
        weightBean.setCurrentTime(currentTime);

        MyLog.logTest("重量数据  ==="+weightBean.getCurrentWeight() +"时间   ==="+weightBean.getCurrentTime() +" 前面的重量");
        //稳定的
        if (Math.abs(weightBean.getCurrentWeight() - weightBean.getFrontWeight()) > 0.005) {
            weightBean.setFrontWeight(weightBean.getCurrentWeight());
            weightBean.setFrontTime(currentTime);
        }
    }

    public void closeConfig() {
        if (weighter != null) {
            if (isOpenWeighter) {
                weighter.closeSerialPort();
            }
        }
        if (printer != null) {
            if (isOpenPrinter) {
                printer.closeSerialPort();
            }
        }
    }
}
