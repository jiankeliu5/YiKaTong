package com.seedsoft.ykt.activity;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.seedsoft.ykt.adpter.BaseFragmentAdapter;
import com.seedsoft.ykt.bean.ActBean;
import com.seedsoft.ykt.bean.AdBean;
import com.seedsoft.ykt.bean.FatherBean;
import com.seedsoft.ykt.bean.NewsBean;
import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.fragment.ComMainFragment;
import com.seedsoft.ykt.fragment.WebviewFragment;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.widget.BottomTabIndicator;
import com.seedsoft.ykt.widget.LoopProgressBar;
import com.seedsoft.ykt.widget.PageIndicator;

public class MainActivity extends BaseActivity {

	private RootBean rootBean;
	private List<ComMainFragment> comMainFragments = null;
	private int curPos = 0;// 当前的栏目序号
	private boolean isExit = true;// 是否退出标志，用于9宫格模式，点击事件后的退回操作
	private ArrayList<FatherBean> lsmpb;
	private String showType = "normal";// 展示模式grid或normal
	private boolean changeFlag;

	// protected ActionBar actionBar;
	TextView tv;
	private ViewPager pager;
	private PageIndicator indicator;
	private BaseFragmentAdapter fragmentAdapter;
	protected static final String[] CONTENT = new String[] { "一卡通", "商户", "我的",
			"更多" };
	protected static final int[] ICONS = new int[] {
			R.drawable.perm_group_card, R.drawable.perm_group_sh,
			R.drawable.perm_group_yh, R.drawable.perm_group_gd };

	private View contentView;

	private SharedPreferences configuration;

	private PopupWindow updatePop;

	private TextView version, cancel, ascertain, progressText;

	private LoopProgressBar bar;

	private ProgressBar progressBar;

	private RelativeLayout relaticeLayout;

	private View includeProgress;

	private String ip;

	private FragmentManager fragmentManager;

	@SuppressLint("InflateParams")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		// actionBar = getSupportActionBar();
		// actionBar.setTitle("");
		// View view = getLayoutInflater().inflate(R.layout.top_bar, null);
		// actionBar.setDisplayShowCustomEnabled(true);
		// actionBar.setCustomView(view);
		// tv = (TextView) view.findViewById(R.id.top_title);
		// tv.setText(CONTENT[0]);
		// actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_bj));

		// if(savedInstanceState != null){
		// rootBean = (RootBean) savedInstanceState.get("rootBean");
		// }else
		// if(getIntent().getSerializableExtra("ROOTBEAN_ID") != null){
		// curPos = getIntent().getIntExtra("ROOTBEAN_ID",0);
		// rootBean =
		// ((BaseApplication)getApplication()).getRootBeans().get(curPos);
		// }else{
		// rootBean =
		// ((BaseApplication)getApplication()).getRootBeans().get(curPos);
		// }
		//
		// //初始化数据源
		// lsmpb = loadGridData(rootBean);
		// comMainFragments = loadNormalData(rootBean);
		// //此处根据标示，选择展示模式
		// showType = rootBean.getShowType();
		// changeFlag = rootBean.isChanged();
		//
		//
		//
		contentView = getLayoutInflater().inflate(R.layout.main_activity, null);
		setContentView(contentView);
		configuration = getSharedPreferences(Constants.SP_SAVE_NAME,
				Context.MODE_PRIVATE);
		ip = getResources().getString(R.string.ip);
		// lbs();
		pager = (ViewPager) findViewById(R.id.pager);
		fragmentManager = getSupportFragmentManager();
		fragmentAdapter = new BaseFragmentAdapter(fragmentManager, CONTENT,
				ICONS, configuration);
		pager.setAdapter(fragmentAdapter);
		indicator = (BottomTabIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	/**
	 * 初始化主页数据
	 * */
	/*
	 * public void init(){
	 * 
	 * }
	 */
	@Override
	public void onBackPressed() {
		allExit(this);
	}

	/**
	 * 初始化横向展示数据源，加载fragment集合
	 * */
	private List<ComMainFragment> loadNormalData(RootBean rootBean) {
		// TODO Auto-generated method stub
		List<ComMainFragment> comMainFragments = new ArrayList<ComMainFragment>();
		ComMainFragment fragment = null;
		for (int i = 0; i < rootBean.getFatherBeans().size(); i++) {

			FatherBean fb = rootBean.getFatherBeans().get(i);
			if (fb.getIsShow().equals("y"))
				continue;
			if (fb.getFatherModuleBeans().size() == 1
					&& fb.getFatherModuleBeans().get(0).getType()
							.equalsIgnoreCase("activity")) {
				// 获取跳转activity的信息
				ActBean act = (ActBean) fb.getFatherModuleBeans().get(0)
						.getObjects();
				String actName = act.getPath();
				try {
					Class c = Class.forName(actName);
					fragment = (ComMainFragment) c.newInstance();

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (fb.getFatherModuleBeans().size() == 1
					&& fb.getFatherModuleBeans().get(0).getType()
							.equalsIgnoreCase("html5page")) {
				// 获取跳转html5page的信息
				NewsBean nb = (NewsBean) fb.getFatherModuleBeans().get(0)
						.getObjects();

				// System.out.println(
				// Constants.SERVER_URL + nb.getUrl()+"\n"+
				// nb.getTitle()+"\n"+
				// nb.getId()+"\n"+
				// Constants.SERVER_URL + nb.getCommentURL()+"\n"+
				// nb.getIsAllowComment());
				ArrayList<AdBean> ad = nb.getAdBeans();
				String ad_title = "";
				String ad_image = "";
				String ad_url = "";
				if (ad != null && ad.size() > 0) {
					ad_title = ad.get(0).getTitle();
					ad_image = Constants.SERVER_URL + ad.get(0).getImage();
					ad_url = Constants.SERVER_URL + ad.get(0).getUrl();
				}
				fragment = (ComMainFragment) (new WebviewFragment(
						Constants.SERVER_URL + nb.getUrl(), nb.getTitle(),
						nb.getId(), Constants.SERVER_URL + nb.getCommentURL(),
						nb.getIsAllowComment(), nb.getLocation(), ad_title,
						ad_image, ad_url));

			} else {
				fragment = new ComMainFragment(fb.getFatherModuleBeans());
			}

			fragment.setName(fb.getName());
			comMainFragments.add(fragment);
		}
		return comMainFragments;
	}

	/**
	 * 初始化九宫格展示元数据
	 */
	private ArrayList<FatherBean> loadGridData(RootBean rootBean) {
		ArrayList<FatherBean> lsfb = new ArrayList<FatherBean>();
		for (int i = 0; i < rootBean.getFatherBeans().size(); i++) {
			FatherBean fb = rootBean.getFatherBeans().get(i);
			if (fb.getIsShow().equals("y"))
				continue;
			lsfb.add(fb);
		}
		return lsfb;
	}

	public void lbs() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 0, new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub
						System.out.println("状态切换！");
					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						System.out.println("GPS is open!");
					}

					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
						System.out.println("GPS is closed!");
					}

					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						System.out.println("位置改变！");
						System.out.println("经度：" + location.getLatitude());
						System.out.println("维度：" + location.getLongitude());
						System.out.println("海拔：" + location.getAltitude());
					}
				});
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		location.getLatitude();
	}

	/**
	 * 登录/注销
	 */
	@Override
	public void login(View v) {
		boolean isLogin = configuration.getBoolean("isLogin", true);
		if(isLogin){
			configuration.edit().putBoolean("isLogin", false).commit();
			
		}else
		startActivityForResult(new Intent(this, LoginRegisterActviity.class),
				10);
	}

	/**
	 * 注册
	 */
	public void register(View v) {
		startActivity(new Intent(getApplication(), LoginRegisterActviity.class)
				.putExtra("isLogin", true));
	}

	/**
	 * 意见反馈
	 */
	public void idea(View v) {
		startActivity(new Intent(getApplication(), IdeaActivity.class));
	}

	/**
	 * 关于
	 */
	public void regarding(View v) {
		startActivity(new Intent(getApplication(), RegardingActivity.class));
	}

	/**
	 * 版本更新
	 */
	@SuppressLint("InflateParams")
	public void update(View v) {
		View view = getLayoutInflater().inflate(R.layout.update_pop, null);
		updatePop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		updatePop.setOutsideTouchable(true);
		updatePop.setBackgroundDrawable(new ColorDrawable());
		updatePop.showAtLocation(contentView, Gravity.CENTER, 0, 0);
		initPop(view);
		checkVersion();
	}

	/**
	 * 初始化检查更新的窗口
	 */
	private void initPop(View v) {
		progressText = (TextView) v.findViewById(R.id.progress);
		includeProgress = v.findViewById(R.id.progress_bar);
		relaticeLayout = (RelativeLayout) v.findViewById(R.id.relatice_layout);
		version = (TextView) v.findViewById(R.id.version);
		cancel = (TextView) v.findViewById(R.id.cancel);
		ascertain = (TextView) v.findViewById(R.id.background);
		bar = (LoopProgressBar) v.findViewById(R.id.loop_progress_bar);
		progressBar = (ProgressBar) v.findViewById(R.id.bar);
		IntentFilter filter = new IntentFilter(
				"com.sxwd.soft.progress.receiver");
		final ProgressBarUpdate broadcastReceiver = new ProgressBarUpdate();
		registerReceiver(broadcastReceiver, filter);
		updatePop.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				unregisterReceiver(broadcastReceiver);
			}
		});
	}

	/**
	 * 检查更新
	 */
	private void checkVersion() {
		int state = configuration.getInt("state", 0);
		switch (state) {
		// 正在更新
		case 2:
			relaticeLayout.setVisibility(View.VISIBLE);
			includeProgress.setVisibility(View.VISIBLE);
			cancel.setText(getResources().getString(R.string.cancel));
			ascertain.setText(getResources().getString(R.string.update));
			break;
		// 检查更新
		default:
			bar.setVisibility(View.VISIBLE);
			version.setVisibility(View.VISIBLE);
			bar.setColor(getResources().getColor(R.color.green));
			bar.start();
			version.setText(getResources().getString(R.string.check));
			new CheckVersion().execute(ip + "palmxian/public/version.xml");
			break;
		}
	}

	/**
	 * 关闭pop
	 */
	public void cancel(View v) {
		updatePop.dismiss();
	}

	/**
	 * 检查版本更新
	 */
	private class CheckVersion extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			// <title>西安一卡通</title>
			// <release>0</release>
			// <size>4545391</size>
			// <link>/download.action</link>
			// <description>测试用的</description>
			HttpClient client = new DefaultHttpClient();
			try {
				HttpResponse response = client.execute(new HttpGet(params[0]));
				if (response.getStatusLine().getStatusCode() == 200) {
					String[] key = getResources().getStringArray(
							R.array.version);
					InputStream is = response.getEntity().getContent();
					SAXBuilder saxBuilder = new SAXBuilder(false);
					org.jdom.Document doc = saxBuilder.build(is);
					Element root = doc.getRootElement();
					Editor editor = configuration.edit();
					for (int i = 0; i < key.length; i++) {
						String k = key[i];
						String value = root.getChildText(k);
						Log.i("xingwei", k + "=" + value);
						editor.putString(k, value);
					}
					editor.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 返回1时为更新
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			List<Object> versions = getVerions(getApplication());
			int versionCode = (int) versions.get(0);
			String versionName = (String) versions.get(1);
			int current = Integer.valueOf(configuration.getString("release",
					"0"));
			String sizeVersion = "";
			if (current > versionCode) {
				result = 1;
				sizeVersion = new DecimalFormat("##0.00").format(Float
						.valueOf(Float.valueOf(configuration.getString("size",
								"0")) / 1024 / 1024));
			}
			switch (result) {
			// 更新
			case 1:
				bar.stopBar();
				bar.setVisibility(View.GONE);
				version.setText("当前版本名称：" + versionName + "\n" + "最新版本大小："
						+ sizeVersion);
				relaticeLayout.setVisibility(View.VISIBLE);
				cancel.setText(getResources().getString(R.string.next));
				ascertain.setText(getResources()
						.getString(R.string.immediately));
				configuration.edit().putInt("state", 1).commit();
				break;
			// 不更新
			default:
				bar.stopBar();
				version.setText(getResources().getText(R.string.yet_version));
				break;
			}
		}
	}

	/**
	 * 取消更新
	 */
	public void cancelUpdate(View v) {
		updatePop.dismiss();
		configuration.edit().putInt("state", 0).commit();
	}

	/**
	 * 马上更新/后台更新
	 */
	public void updateVersion(View v) {
		int state = configuration.getInt("state", 0);
		switch (state) {
		case 1:
			version.setVisibility(View.GONE);
			cancel.setText(getResources().getString(R.string.cancel));
			ascertain.setText(getResources().getString(R.string.update));
			includeProgress.setVisibility(View.VISIBLE);
			configuration.edit().putInt("state", 2).commit();
			startService(new Intent(getApplication(), UpdateService.class));
			break;

		case 2:
			updatePop.dismiss();
			break;

		default:

			break;
		}
	}

	/**
	 * 获取当前版本
	 */
	public static List<Object> getVerions(Application application) {
		List<Object> verions = new ArrayList<Object>();
		// 到服务器检查软件是否有新版本
		PackageManager packageManager = application.getPackageManager();
		PackageInfo packInfo = null;
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		try {
			packInfo = packageManager.getPackageInfo(
					application.getPackageName(), 0);
			verions.add(packInfo.versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		verions.add(packInfo.versionName);
		return verions;
	};

	/**
	 * 广播接收，更新进度条
	 */
	private class ProgressBarUpdate extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int progress = intent.getIntExtra("progress", 0);
			switch (progress) {
			case -1:
				updatePop.dismiss();
				break;

			default:
				progressBar.setProgress(progress);
				progressText.setText(progress + "%");
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		configuration.edit().putBoolean("isLogin", false).commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		super.onActivityResult(requestCode, resultCode, arg2);
		configuration = getSharedPreferences(Constants.SP_SAVE_NAME,
				Context.MODE_PRIVATE);
		if (requestCode == 10) {
			boolean isLogin = configuration.getBoolean("isLogin", true);
			TextView v = (TextView) findViewById(R.id.login);
			if (isLogin)
				v.setText("注销");;
		}
	}

}