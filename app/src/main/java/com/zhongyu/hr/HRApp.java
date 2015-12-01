package com.zhongyu.hr;

import com.orm.SugarApp;
import com.zhongyu.hr.rxbus.RxBus;

/**
 * Created by Administrator on 2015/11/27.
 */
public class HRApp extends SugarApp{

    private static HRApp instance;
    private  RxBus rxBus;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = (HRApp)getApplicationContext();
        rxBus = RxBus.getInstance();

    }

    public static HRApp getInstance(){
        return instance;
    }

    public  RxBus getRxBus(){
        return rxBus;
    }
}
