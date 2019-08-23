package com.xuanyuan.library.help;

import android.app.Activity;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Longer on 2016/11/1.
 */
public class ActivityController {

    private static final List<Activity> activities = new ArrayList<>();
    private static boolean mIsFinishAll = false;

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        if (!mIsFinishAll) {
            activities.remove(activity);
        }
    }

    /**
     * @param isExit 是否需要完全退出 系统
     */
    public static void finishAll(boolean isExit) {
        for (Activity activity : activities) {
            if (activity != null) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        if(isExit){
            System.exit(0);
        }
    }

    /**
     * 安全结束Activity的方法
     *
     * @param whenTheArrayListFinish 借口回调,防止未完成遍历的情况就删除或者增加集合操作
     */
    public static void finishAllSafe(WhenTheArrayListFinish whenTheArrayListFinish) {
        mIsFinishAll = true;
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            if (!next.isFinishing()) {
                next.finish();
                iterator.remove();
            }
        }
        whenTheArrayListFinish.readComplete();
        mIsFinishAll = false;
    }

    public interface WhenTheArrayListFinish {
        void readComplete();
    }

    public static void finishActivityOutOfMainActivity() {
        for (Activity activity : activities) {
            String c = activity.getComponentName().getClassName();
            if (!activity.isFinishing() && !activity.getComponentName().getClassName().equals("com.fuexpress.kr.MyBuglyActivity")) {
                activity.finish();
            }
        }
    }

    private static final List<Activity> cardActiviies = new ArrayList<>();

    public static void cardAddActivity(Activity activity) {
        cardActiviies.add(activity);
    }

    public static void cardFinish() {
        for (Activity activity : cardActiviies) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
