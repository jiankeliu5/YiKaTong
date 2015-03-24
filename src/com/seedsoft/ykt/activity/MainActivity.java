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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seedsoft.ykt.adpter.BaseFragmentAdapter;
import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.util.BaseApplication;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.widget.BottomTabIndicator;
import com.seedsoft.ykt.widget.LoopProgressBar;
import com.seedsoft.ykt.widget.PageIndicator;

public class MainActivity extends BaseActivity {

	private ViewPager pager;
	private PageIndicator indicator;
	private BaseFragmentAdapter fragmentAdapter;

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
		contentView = getLayoutInflater().inflate(R.layout.main_activity, null);
		setContentView(contentView);
		configuration = getSharedPreferences(Constants.SP_SAVE_NAME,
				Context.MODE_PRIVATE);
		ip = getResources().getString(R.string.ip);
		pager = (ViewPager) findViewById(R.id.pager);
		fragmentManager = getSupportFragmentManager();
		fragmentAdapter = new BaseFragmentAdapter(fragmentManager, getRootBeans(), configuration);
		pager.setAdapter(fragmentAdapter);
		indicator = (BottomTabIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	public ArrayList<RootBean> getRootBeans(){
		return ((BaseApplication)getApplication()).getRootBeans();
	}

	@Override
	public void onBackPressed() {
		allExit(this);
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