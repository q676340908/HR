package com.zhongyu.hr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.zhongyu.hr.fragment.MainFragment;

import butterknife.Bind;
import rx.Subscription;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private Subscription mSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigateMain();
      /*  mSubscription = HRApp.getInstance().getRxBus().toObservable()
                        .filter(new Func1() {
                            @Override
                            public Object call(Object o) {
                                return null;
                            }
                        })*/


    }

    private void navigateMain(){
        Fragment fragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                .commitAllowingStateLoss();

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscription != null && mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }
}
