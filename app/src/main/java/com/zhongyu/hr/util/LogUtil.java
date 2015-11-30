package com.zhongyu.hr.util;

import android.util.Log;

import com.zhongyu.hr.BuildConfig;

/**
 * Created by Administrator on 2015/11/30.
 */
public class LogUtil {
    private static final boolean DEBUG = true;

    public static void i(String tag,String msg){
        if(BuildConfig.DEBUG && DEBUG){
            Log.i(tag,msg);
        }
    }
}
