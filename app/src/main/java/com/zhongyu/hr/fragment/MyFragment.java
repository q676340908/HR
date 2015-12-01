package com.zhongyu.hr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongyu.hr.R;
import com.zhongyu.hr.adapter.MyAdapter;
import com.zhongyu.hr.model.MyInfo;
import com.zhongyu.hr.util.LogUtil;
import com.zhongyu.hr.widgets.DividerItemDecoration;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2015/11/30.
 */
public class MyFragment extends Fragment {

    @Bind(R.id.recyclerview)
    protected RecyclerView mRecyclerView;

    protected MyAdapter mMyAdapter;
    private CompositeSubscription mCompositeSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my,container,false);
        ButterKnife.bind(this,rootView);

        mCompositeSubscription = new CompositeSubscription();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMyAdapter = new MyAdapter(getActivity());
        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        loadMyitems();

        return rootView;
    }

    private void loadMyitems(){
    Subscription subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String json = null;
                InputStream is = MyFragment.this.getResources().openRawResource(R.raw.check_items);
                try {
                    if (is != null) {
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        json = new String(buffer, "UTF-8");
                        subscriber.onNext(json);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        })
        .flatMap(new Func1<String, Observable<MyInfo>>() {
            @Override
            public Observable<MyInfo> call(String json) {
                Gson gson = new Gson();
                Type collection = new TypeToken<ArrayList<MyInfo>>() {
                }.getType();
                List<MyInfo> checkItems = gson.fromJson(json, collection);
                return Observable.from(checkItems);
            }
        })
        .filter(new Func1<MyInfo, Boolean>() {
            @Override
            public Boolean call(MyInfo myInfo) {
                return myInfo != null;
            }
        })
        .toList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<MyInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<MyInfo> myInfos) {
                LogUtil.i("BBBB","myInfos size = "+myInfos.size());
                mMyAdapter.setMyInfoList(myInfos);
            }
        })
        ;
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.clear();
        }
    }
}
