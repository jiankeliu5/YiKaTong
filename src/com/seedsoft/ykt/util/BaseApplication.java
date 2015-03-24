package com.seedsoft.ykt.util;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.nfc.ThisApplication;

public class BaseApplication extends ThisApplication {

	// ==============ϵͳ��ܻ�������Դ==================
	ArrayList<RootBean> rootBeans;

	public ArrayList<RootBean> getRootBeans() {
		return rootBeans;
	}

	public void setRootBeans(ArrayList<RootBean> rootBeans) {
		this.rootBeans = rootBeans;
	}

	// ===============�ٶȵ�ͼӦ��=====================
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
	// "BMapManager  ��ʼ������!", Toast.LENGTH_LONG);
	// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
	// toast.getView().setPadding(20, 10, 20, 10);
	// toast.show();
	// }
	// }

	public static BaseApplication getInstance() {
		return mInstance;
	}

	//
	// // �����¼���������������ͨ�������������Ȩ��֤�����
	// public static class MyGeneralListener implements MKGeneralListener {
	//
	// @Override
	// public void onGetNetworkState(int iError) {
	// if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	// Toast toast =
	// Toast.makeText(BaseApplication.getInstance().getApplicationContext(),
	// "���������������",
	// Toast.LENGTH_LONG);
	// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
	// toast.getView().setPadding(20, 10, 20, 10);
	// toast.show();
	// }
	// else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	// Toast toast =
	// Toast.makeText(BaseApplication.getInstance().getApplicationContext(),
	// "������ȷ�ļ���������",
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
	// //��ȨKey����
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
	// //=======================�ٶȶ�λ===========================================
	// public LocationClient mLocationClient = null;
	// public MyLocationListenner myListener = new MyLocationListenner();
	// // public String time = null;
	// public String address = null;
	// // public NotifyLister mNotifyer=null;
	// // public Vibrator mVibrator01;
	//
	// /**
	// * ��������������λ�õ�ʱ�򣬸�ʽ�����ַ���
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
		// //��λ���
		// mLocationClient = new LocationClient(this);
		// mLocationClient.registerLocationListener(myListener);
		super.onCreate();
		// //��ͼ���
		mInstance = this;
		// initEngineManager(this);
		// //�������
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
