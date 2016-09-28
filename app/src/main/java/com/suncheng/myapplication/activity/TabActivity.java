package com.suncheng.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.suncheng.commontools.framwork.BaseActivity;
import com.suncheng.commontools.utils.BlankUtil;
import com.suncheng.myapplication.R;
import com.suncheng.myapplication.fragment.RecommendFragment;
import com.suncheng.myapplication.framework.Constants;
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
        mTitleList.add("推荐");
        mTitleList.add("热门");
        mTitleList.add("潜力");
        mTitleList.add("最新");
        mTitleList.add("约约");
        mFragmentList.add(RecommendFragment.newInstance(Constants.POCO_URL_RECOMMEND));
        mFragmentList.add(RecommendFragment.newInstance(Constants.POCO_URL_HOT));
        mFragmentList.add(RecommendFragment.newInstance(Constants.POCO_URL_POTENTIAL));
        mFragmentList.add(RecommendFragment.newInstance(Constants.POCO_URL_NEW));
        mFragmentList.add(RecommendFragment.newInstance(Constants.POCO_URL_YUE));

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
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
