package com.xuanyuan.library.config;

/**
 * 作者：罗发新
 * 时间：2019/4/24 0024    星期三
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface IConfig {
    String SP_FLAG_BOOLEAN_SCREEN_STATE = "booleanScreenState";

    // 是否已经有了硬件信息 ,并将其保存在了数据库中
    String IS_HAS_DEVICE = "isHaveDevice";

    /**
     * 事件总线,普通类别的
     */
    String EVENT_BUS_COMMON = "eventBusCommon";

    /* *   int 区域  ********************************************************/
    /**
     * 每一天的 毫秒数
     */
    long TIME = 86400000;

    // 称重稳定时间
    long STABLE_TIME = 1000;
    long STABLE_MIN_TIME = 300;
    String PATCH_TIME = "*20190906 15:52";

    //
}
