package com.seedsoft.ykt.push;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * 
 * �?��建议在自定义 Application 类里初始化�?也可以在�?Activity 里�?
 */
public class ExampleApplication extends Application {
    private static final String TAG = "JPush";

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[ExampleApplication] onCreate");
         super.onCreate();
         
         JPushInterface.setDebugMode(true); 	// 设置�?��日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始�?JPush
    }
}
