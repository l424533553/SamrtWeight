package com.luofx.mvp.login;

import com.luofx.mvp.base.presenter.HttpResponseListener;
import com.luofx.mvp.base.presenter.IBasePresenter;
import com.xuanyuan.library.mvp.view.IBaseView;

// 建立了一个契约类
final class LoginContacts {
    /**
     * view 层接口
     */
    public interface LoginUI extends IBaseView {
        /**
         * 登录成功
         */
        void loginSuccess();

        /**
         * 登录失败
         */
        void loginFailure();


    }

    /**
     * presenter 层接口
     */
    public interface LoginPtr extends IBasePresenter {
        void login(String username, String password);
    }

    /**
     * model 层接口
     */
    public interface LoginMdl {
        void login(String username, String password, HttpResponseListener callbak);
    }
}
