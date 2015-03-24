package com.seedsoft.ykt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.seedsoft.ykt.util.Util;

public class WelcomeActivity extends BaseActivity {

	private String TAG = "WelcomeActivity";	
	private boolean isFirst = false;
	private ImageView iv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		// 判断是否初次安装
		isFirst = Util.checkIsFirst(this);
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "--isFirst?--" + isFirst);
		}

		if (isFirst == true) {
			// 初次运行，则进入welcomepage页面
			startActivity(new Intent(this, WelcomePageActivity.class));
			finish();
			return;
		}
		setContentView(R.layout.welcome_activity);
		iv = (ImageView) this.findViewById(R.id.wel_iv);

		Animation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(2000);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				new Util().checkIsRefreshOld(WelcomeActivity.this);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
//				try {
//					new Util().InitMainPageDate(WelcomeActivity.this);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
		iv.setAnimation(animation);
	}

}
