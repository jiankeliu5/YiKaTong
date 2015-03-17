package com.seedsoft.ykt.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.seedsoft.ykt.util.ConnectionChangeReceiver;
import com.seedsoft.ykt.util.Constants;

@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity {

	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();
	
	public static ConnectionChangeReceiver mNetworkStateReceiver = new ConnectionChangeReceiver();	
	
	private TextView login,title,register;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		allActivity.add(this);
		
		//注册网络监听 
		IntentFilter filter = new IntentFilter();  
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE"); 
		registerReceiver(mNetworkStateReceiver, filter); 
		
	}
	
	public void initTitle(int titleId){
		login=(TextView) findViewById(R.id.login);
		title=(TextView) findViewById(R.id.top_title);
		register=(TextView) findViewById(R.id.register);
		title.setText(titleId);
	}
	
	public void setLoginImage(int left,int right,int top,int bottom,int textId){
		login.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
		login.setPadding(0, 0, 10, 0);
		login.setText(textId);
		login.setVisibility(View.VISIBLE);
	}
	
	public void setRegister(int visible){
		register.setVisibility(visible);
	}
	
	/**
	 * 
	 */
	public void login(View v){
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mNetworkStateReceiver);
	}
	
	/**退出方法*/
	public void exitAll(){
		//保存退出标志
		getSharedPreferences(Constants.SP_SAVE_NAME, MODE_PRIVATE).edit().putBoolean("open_flag", false).commit();
		getSharedPreferences(Constants.SP_SAVE_NAME, MODE_PRIVATE).edit().putBoolean("is_login", false).commit();
		//循环退出
		for (Activity ac : allActivity) {
			if(ac!=null){
				ac.finish();
			}
		}
		
	}

	/**退出对话框*/
	@SuppressWarnings("deprecation")
	public void allExit(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
			AlertDialog alertDialog = builder.create();
			alertDialog.setTitle("提示：");
			alertDialog.setMessage("是否退出一卡通应用？");
			alertDialog.setButton("退出", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					exitAll();
				}
			});
			alertDialog.setButton2("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			alertDialog.show();
//			new DialogExtend(context,"提示：","是否退出应用？") {		
//				@Override
//				protected void setOK(Dialog dialog) {
//					dialog.dismiss();
//					exitAll();
//				}
//
//				@Override
//				protected void setCancle(Dialog dialog) {
//					dialog.dismiss();
//				}
//			};			
	}

	
}
