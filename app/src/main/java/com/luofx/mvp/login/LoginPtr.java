//package com.luofx.mvp.login;
//
//import android.support.annotation.NonNull;
//
//import com.luofx.entity.LoginBean;
//import com.luofx.mvp.base.presenter.BasePresenter;
//import com.luofx.mvp.base.presenter.HttpResponseListener;
//
//public class LoginPtr extends BasePresenter<LoginContacts.LoginUI, LoginBean> implements LoginContacts.LoginPtr, HttpResponseListener<LoginBean> {
//    private LoginContacts.LoginMdl mLoginMdl;
//
//    public LoginPtr(@NonNull LoginContacts.LoginUI view) {
//        super(view);
//        // 实例化 Model 层
//        mLoginMdl = new LoginMdl();
//    }
//
//    @Override
//    public void login(String username, String password) {
//        //显示进度条
//        getView().showLoading();
//        mLoginMdl.login(username, password, this);
//
//
//    }
//
//    @Override
//    public void onSuccess(Object tag, LoginBean t) {
//        // 先判断是否已经与 View 建立联系
//        if (isViewAttach()) {
//            // 隐藏进度条
//            getView().hideLoading();
//            // 登录成功调用
//            getView().loginSuccess();
//        }
//    }
//
//    @Override
//    public void onFailure(Object tag, Object failure) {
//        if (isViewAttach()) {
//            // 隐藏进度条
//            getView().hideLoading();
//            // 登录失败调用
//            getView().loginFailure();
//        }
//    }
//
//}
//
