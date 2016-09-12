package com.suncheng.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.suncheng.myapplication.R;
import com.suncheng.myapplication.fragment.RecommendFragment;
import com.suncheng.myapplication.framework.BaseActivity;
import com.suncheng.myapplication.utils.BlankUtil;
import com.suncheng.myapplication.view.PagerSlidingTabStrip;

import java.util.ArrayList;

public class TabActivity extends BaseActivity {
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tab);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerStrip);
        initFragmentPager();
    }

    public void initFragmentPager() {
        mTitleList.clear();
        mTitleList.add("经典");
        mTitleList.add("文艺");
        mTitleList.add("浪漫");
        mTitleList.add("校园");
        mTitleList.add("人物");
        mTitleList.add("自然");
        mTitleList.add("情怀");
        mTitleList.add("怀旧");
        mFragmentList.add(RecommendFragment.newInstance());
        mFragmentList.add(RecommendFragment.newInstance());
        mFragmentList.add(RecommendFragment.newInstance());
        mFragmentList.add(RecommendFragment.newInstance());
        mFragmentList.add(RecommendFragment.newInstance());
        mFragmentList.add(RecommendFragment.newInstance());
        mFragmentList.add(RecommendFragment.newInstance());
        mFragmentList.add(RecommendFragment.newInstance());

        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    private class TabPagerAdapter extends FragmentPagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (!BlankUtil.isBlank(mTitleList)) {
                return mTitleList.get(position);
            } else {
                return "";
            }
        }
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                applicationFinish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
