package com.seedsoft.ykt.util;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.nfc.ThisApplication;

public class BaseApplication extends ThisApplication {

	// ==============系统框架基类数据源==================
	ArrayList<RootBean> rootBeans;

	public ArrayList<RootBean> getRootBeans() {
		return rootBeans;
	}

	public void setRootBeans(ArrayList<RootBean> rootBeans) {
		this.rootBeans = rootBeans;
	}

	// ===============百度地图应用=====================
	private static BaseApplication mInstance = null;

	// public boolean m_bKeyRight = true;
	// public BMapManager mBMapManager = null;
	// public static final String strKey = "8f110426274bb8a3788ce9352720c457";
	//
	//
	// public void initEngineManager(Context context) {
	// if (mBMapManager == null) {
	// mBMapManager = new BMapManager(context);
	// }
	//
	// if (!mBMapManager.init(strKey,new MyGeneralListener())) {
	// Toast toast =
	// Toast.makeText(BaseApplication.getInstance().getApplicationContext(),
	// "BMapManager  初始化错误!", Toast.LENGTH_LONG);
	// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
	// toast.getView().setPadding(20, 10, 20, 10);
	// toast.show();
	// }
	// }

	public static BaseApplication getInstance() {
		return mInstance;
	}

	//
	// // 常用事件监听，用来处理通常的网络错误，授权验证错误等
	// public static class MyGeneralListener implements MKGeneralListener {
	//
	// @Override
	// public void onGetNetworkState(int iError) {
	// if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	// Toast toast =
	// Toast.makeText(BaseApplication.getInstance().getApplicationContext(),
	// "您的网络出错啦！",
	// Toast.LENGTH_LONG);
	// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
	// toast.getView().setPadding(20, 10, 20, 10);
	// toast.show();
	// }
	// else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	// Toast toast =
	// Toast.makeText(BaseApplication.getInstance().getApplicationContext(),
	// "输入正确的检索条件！",
	// Toast.LENGTH_LONG);
	// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
	// toast.getView().setPadding(20, 10, 20, 10);
	// toast.show();
	// }
	// // ...
	// }
	//
	// @Override
	// public void onGetPermissionState(int iError) {
	// if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
	// //授权Key错误：
	// Toast toast =
	// Toast.makeText(BaseApplication.getInstance().getApplicationContext(),
	// "Key", Toast.LENGTH_LONG);
	// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
	// toast.getView().setPadding(20, 10, 20, 10);
	// toast.show();
	// BaseApplication.getInstance().m_bKeyRight = false;
	// }
	// }
	// }
	//
	// //=======================百度定位===========================================
	// public LocationClient mLocationClient = null;
	// public MyLocationListenner myListener = new MyLocationListenner();
	// // public String time = null;
	// public String address = null;
	// // public NotifyLister mNotifyer=null;
	// // public Vibrator mVibrator01;
	//
	// /**
	// * 监听函数，有新位置的时候，格式化成字符串
	// */
	// public class MyLocationListenner implements BDLocationListener {
	// @Override
	// public void onReceiveLocation(BDLocation location) {
	// if (location == null)
	// return ;
	//
	// // time = location.getTime();
	//
	// if (location.getLocType() == BDLocation.TypeNetWorkLocation){
	// address = location.getAddrStr();
	// }
	// if(location.getLocType() == BDLocation.TypeGpsLocation){
	// address = location.getAddrStr();
	//
	// }
	//
	// // System.out.println("==1==onReceiveLocation==time=="+time);
	// System.out.println("==2==onReceiveLocation==address=="+address);
	// }
	//
	// public void onReceivePoi(BDLocation poiLocation) {
	// if (poiLocation == null){
	// return ;
	// }
	//
	// // time = poiLocation.getTime();
	// if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
	// address = poiLocation.getAddrStr();
	// }
	// if (poiLocation.getLocType() == BDLocation.TypeGpsLocation){
	// address = poiLocation.getAddrStr();
	// }
	//
	// // System.out.println("==1==onReceivePoi==time=="+time);
	// System.out.println("==2==onReceivePoi==address=="+address);
	// }
	// }
	//
	// public class NotifyLister extends BDNotifyListener{
	// public void onNotify(BDLocation mlocation, float distance){
	// mVibrator01.vibrate(1000);
	// }
	// }
	// ============================================================================

	//
	@Override
	public void onCreate() {
		// //定位相关
		// mLocationClient = new LocationClient(this);
		// mLocationClient.registerLocationListener(myListener);
		super.onCreate();
		// //地图相关
		mInstance = this;
		// initEngineManager(this);
		// //推送相关
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
