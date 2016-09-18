package com.yunxian.carousel.app.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 公共的一个ViewPager的适配器
 * 
 * @author A Shuai
 *
 */
public final class CommonViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mDataSet;
    
    public CommonViewPagerAdapter(FragmentManager fm, List<Fragment> mDataSet) {
        super(fm);
        if( mDataSet == null ){
            throw new NullPointerException();
        }
        this.mDataSet = mDataSet;
    }

    @Override
    public Fragment getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

}
