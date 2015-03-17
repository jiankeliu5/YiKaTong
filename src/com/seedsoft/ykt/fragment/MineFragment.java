package com.seedsoft.ykt.fragment;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;

@SuppressLint("NewApi")
public class MineFragment extends Fragment {

	public static final int UP_DATA = 3000;

	private View v;

	private String titleText;

	private SharedPreferences configuration;

	private boolean isLogin = false;

	private TextView login, register;


	public MineFragment(String titleText, SharedPreferences configuration) {
		this.titleText = titleText;
		this.configuration = configuration;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLogin = configuration.getBoolean("isLogin", false);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.mine_fragment, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		TextView title = (TextView) v.findViewById(R.id.top_title);
		title.setText(titleText);
		register = (TextView) v.findViewById(R.id.register);
		login = (TextView) v.findViewById(R.id.login);
		setVisible(configuration);
		register.setVisibility(View.VISIBLE);
	}

	public void setVisible(SharedPreferences configuration) {
		isLogin = configuration.getBoolean("isLogin", false);
		if (isLogin) {
			login.setVisibility(View.INVISIBLE);
			Log.i("xingwei", "isLogin=" + isLogin);
		} else {
			login.setVisibility(View.VISIBLE);
		}
		Log.i("xingwei", "isLogin=" + isLogin);
	}
}
