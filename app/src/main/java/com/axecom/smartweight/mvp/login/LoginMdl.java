package com.axecom.smartweight.mvp.login;

import com.axecom.smartweight.mvp.base.model.BaseModel;
import com.axecom.smartweight.mvp.base.presenter.HttpResponseListener;


/**
 * model 模型层 写 网络请求 相关  纯 Java代码
 */
public class LoginMdl extends BaseModel implements LoginContacts.LoginMdl {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param callbak  网络请求回调
     */
    @Override
    public void login(String username, String password, HttpResponseListener callbak) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("username", username);
//        map.put("password", MyTextUtils.getMD5(password));

//        RequestBody body = ReqBodyHelper.createJson(map);
//        // 发送网络请求
//        sendRequest(RetrofitHttpUtils.getApi().getLoginInfo(body),callbak);


    }


}
