package com.axecom.smartweight.mvp;

import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.axecom.smartweight.my.entity.UserInfo;
import com.xuanyuan.library.mvp.view.IBaseView;

public interface IMainContract {

    interface IView extends IBaseView {

        SysApplication getMyAppliaction();
        //发送空消息
        void sendHanderEmptyMessage(int what);
    }


    interface IPresenter extends IBasePresenter {
        //通知model要获取数据并把model返回的数据交给view层
//        void getData();
        UserInfo detectionUserInfo();

        //上传订单信息
        void send2AxeWebOrderInfo(OrderInfo orderInfo);

        //发送信息给计量院
        void send2FpmsWebOrderInfo(OrderInfo orderInfo);


        //获取批次号
        void requestWebTraceNo(int sellerid);
        void getData(int dataType);

    }

    /**
     * 基本的控制器接口
     */
    interface IBasePresenter {

    }

    interface IModel {
        //获取数据
        String doData();
    }

}
