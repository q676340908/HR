package com.zhongyu.hr.model;

/**
 * Created by Administrator on 2015/11/30.
 */
public class MyInfo {
    public static final int TYPE_CATEGORY = 0;
    public static final int TYPE_PERSONAL_CENTER = 1;
    public static final int TYPE_EVALUATION_HISTORY = 2;
    public static final int TYPE_REWARD_PUNISHIMENT = 3;
    public static final int TYPE_PROMOTION_RECORD = 4;
    public static final int TYPE_SALARY_CARD = 5;
    public static final int TYPE_PERSONAL_ASSETS = 6;
    public static final int TYPE_MODIFIED_PASSWORD = 7;

    public String titleResName;
    public String summaryResName;
    public String iconResName;
    public int type;

    public MyInfo(){

    }

}
