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
		
		//ע��������� 
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
	
	/**�˳�����*/
	public void exitAll(){
		//�����˳���־
		getSharedPreferences(Constants.SP_SAVE_NAME, MODE_PRIVATE).edit().putBoolean("open_flag", false).commit();
		getSharedPreferences(Constants.SP_SAVE_NAME, MODE_PRIVATE).edit().putBoolean("is_login", false).commit();
		//ѭ���˳�
		for (Activity ac : allActivity) {
			if(ac!=null){
				ac.finish();
			}
		}
		
	}

	/**�˳��Ի���*/
	@SuppressWarnings("deprecation")
	public void allExit(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
			AlertDialog alertDialog = builder.create();
			alertDialog.setTitle("��ʾ��");
			alertDialog.setMessage("�Ƿ��˳�һ��ͨӦ�ã�");
			alertDialog.setButton("�˳�", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					exitAll();
				}
			});
			alertDialog.setButton2("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			alertDialog.show();
//			new DialogExtend(context,"��ʾ��","�Ƿ��˳�Ӧ�ã�") {		
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
