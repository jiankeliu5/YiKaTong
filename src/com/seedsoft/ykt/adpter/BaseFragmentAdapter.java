package com.seedsoft.ykt.adpter;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.fragment.CardFragment;
import com.seedsoft.ykt.fragment.ComMainFragment;
import com.seedsoft.ykt.fragment.MineFragment;
import com.seedsoft.ykt.fragment.MoreFragment;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.widget.IconPagerAdapter;

public class BaseFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {

	private ArrayList<RootBean> rootBeans;
	private SharedPreferences configuration;

	public BaseFragmentAdapter(FragmentManager fragmentManager,
			ArrayList<RootBean> rootBeans, SharedPreferences configuration) {
		super(fragmentManager);
		// TODO Auto-generated constructor stub		
		this.configuration = configuration;
		this.rootBeans = rootBeans;
	}


	@Override
	public Fragment getItem(int position) {
		
		switch (position) {		
		case 0:
			return new CardFragment(rootBeans.get(position));
		case 1:
			return new ComMainFragment(rootBeans.get(position));
		case 2:
			return new MineFragment(rootBeans.get(position).getName(), configuration);			
		default:
			return new MoreFragment(configuration, rootBeans.get(position).getName());
		}
		
//		return new CardFragment(rootBeans.get(position));
	}

	public Fragment getFragment(int itme) {

		return getItem(itme);
	}
	


	@Override
	public int getCount() {
		return rootBeans.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return rootBeans.get(position).getName();
	}

	@Override
	public int getIconResId(int index) {
		return Constants.ICONS[index];
	}


	@Override
	public Drawable[] getIconRes(int index) {
		// TODO Auto-generated method stub
		Drawable[] d = new Drawable[2];
		d[0] = Drawable.createFromPath("");
		d[1] = Drawable.createFromPath("");

		return d;
	}


	@Override
	public String getIconURL(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String[] getIconURLS(int index) {
		// TODO Auto-generated method stub
		
		return new String[]{Constants.SERVER_URL+rootBeans.get(index).getFrontImage(),Constants.SERVER_URL+rootBeans.get(index).getBackImage()};
	}


	@Override
	public Object getIconObject(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
}