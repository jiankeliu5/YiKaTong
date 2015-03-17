package com.seedsoft.ykt.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.seedsoft.ykt.adpter.WelcomeFragmentAdapter;
import com.seedsoft.ykt.widget.PageIndicator;


public class WelcomePageActivity extends FragmentActivity {

//	private final static String TAG = "WelcomePageActivity";	

	private WelcomeFragmentAdapter mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome_circles);
		
		
//		//创建快捷方式
//		boolean bl = Util.hasShortcut(this);
//		if(bl){
//			Util.delShortcut(this);
//		}
//		Util.addShortcut(this);
//		
		mPager = (ViewPager) this.findViewById(R.id.welcome_circle_pager);
		mIndicator = (PageIndicator) this.findViewById(R.id.welcome_circle_page_indicator);
		mAdapter = new WelcomeFragmentAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
		
	}
	

	
}
