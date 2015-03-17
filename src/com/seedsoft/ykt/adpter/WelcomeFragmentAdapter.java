package com.seedsoft.ykt.adpter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.seedsoft.ykt.fragment.WelcomeFragment;

public class WelcomeFragmentAdapter extends FragmentPagerAdapter  {
	
	private int mCount = 4;
    public WelcomeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
		return new WelcomeFragment(position).newInstance();   	
    }

    @Override
    public int getCount() {
        return mCount;
    }


    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}