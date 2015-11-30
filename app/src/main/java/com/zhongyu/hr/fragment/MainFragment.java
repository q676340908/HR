package com.zhongyu.hr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongyu.hr.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/30.
 */
public class MainFragment extends Fragment{

    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        ButterKnife.bind(this,rootView);

        setUpViewPager();
        mTabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    private void setUpViewPager(){
        PageAdapter pageAdapter = new PageAdapter(getChildFragmentManager());
        pageAdapter.addFragment(new MyFragment(),getString(R.string.tab_my));
        pageAdapter.addFragment(new AttendanceFragment(),getString(R.string.tab_attendance));
        pageAdapter.addFragment(new DailyReportFragment(),getString(R.string.tab_daily_report));
        pageAdapter.addFragment(new WorkFlowFragment(),getString(R.string.tab_workflow));
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    static class PageAdapter extends FragmentPagerAdapter{

        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitles = new ArrayList<>();

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment,String title){
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
