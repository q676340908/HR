package com.zhongyu.hr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.setting_bt)
    Button settingBt;
    @Bind(R.id.username_et)
    EditText usernameEt;
    @Bind(R.id.passwd_et)
    EditText passwdEt;
    @Bind(R.id.remember_userid_cb)
    CheckBox rememberUseridCb;
    @Bind(R.id.remember_pwd_cb)
    CheckBox rememberPwdCb;
    @Bind(R.id.login_bt)
    Button loginBt;

    @OnClick(R.id.setting_bt)
    public void goSetting(){

        Intent intent = new Intent(LoginActivity.this,ServerSettingActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.login_bt)
    public void login(){
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

}
