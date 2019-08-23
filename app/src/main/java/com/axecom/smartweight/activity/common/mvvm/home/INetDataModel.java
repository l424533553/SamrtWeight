package com.axecom.smartweight.activity.common.mvvm.home;


import com.axecom.smartweight.entity.project.UserInfo;

/**
 * 作者： 周旭 on 2017年10月18日 0018.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public interface INetDataModel {

    void getUserInfoExByRetrofit();

    void getUserInfoEx();

    UserInfo getUserInfo();
}
