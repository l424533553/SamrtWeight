package com.luofx.mvp.login;

import com.luofx.mvp.base.model.BaseModel;
import com.luofx.mvp.base.presenter.HttpResponseListener;
import com.luofx.utils.text.MyTextUtils;

import java.util.HashMap;


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
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);


        //TODO

        map.put("password", MyTextUtils.getMD5(password));

//        RequestBody body = ReqBodyHelper.createJson(map);
//        // 发送网络请求
//        sendRequest(HttpUtils.getApi().getLoginInfo(body),callbak);


    }


}
