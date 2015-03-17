package com.seedsoft.ykt.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;


public class ConnectionChangeReceiver extends BroadcastReceiver {

	 private static final String TAG =ConnectionChangeReceiver.class.getSimpleName();  
	 
	    @Override   
	    public void onReceive(final Context context, Intent intent) {        
    
	    //获得网络连接服务    
	    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   
	    // State state = connManager.getActiveNetworkInfo().getState();    
	    // 获取WIFI网络连接状态   
	    State state1 = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();   
	    // 获取GPRS网络连接状态    
	    State state2 = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
	   //获取服务器连接状态
//	    boolean bl=Util.getSQLState();
	    
	    // 判断是否正在使用WIFI网络    
	    if (State.CONNECTED == state1) {  
	    	 return;
	    }
	    
	    // 判断是否正在使用GPRS网络
	    else if (State.CONNECTED == state2) {       
	    	return;
	    }   
	    
	     /**在此处提示网络异常 */
	    Toast.makeText(context, "网络异常!", Toast.LENGTH_LONG).show();
	    }
}
