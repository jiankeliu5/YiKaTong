package com.seedsoft.ykt.activity;

import android.os.Bundle;

public class RegardingActivity extends BaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regarding_activity);
		initTitle(R.string.more_regarding);
		setLoginImage(R.drawable.pre_btn, 0, 0, 0, R.string.get_back);
	}
}
