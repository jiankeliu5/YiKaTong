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
    
	    //����������ӷ���    
	    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   
	    // State state = connManager.getActiveNetworkInfo().getState();    
	    // ��ȡWIFI��������״̬   
	    State state1 = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();   
	    // ��ȡGPRS��������״̬    
	    State state2 = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
	   //��ȡ����������״̬
//	    boolean bl=Util.getSQLState();
	    
	    // �ж��Ƿ�����ʹ��WIFI����    
	    if (State.CONNECTED == state1) {  
	    	 return;
	    }
	    
	    // �ж��Ƿ�����ʹ��GPRS����
	    else if (State.CONNECTED == state2) {       
	    	return;
	    }   
	    
	     /**�ڴ˴���ʾ�����쳣 */
	    Toast.makeText(context, "�����쳣!", Toast.LENGTH_LONG).show();
	    }
}
