package com.seedsoft.ykt.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.activity.BuildConfig;
import com.seedsoft.ykt.activity.MainActivity;
import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.bean.FatherBean;
import com.seedsoft.ykt.bean.FatherModuleBean;
import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.widget.DialogExtend;
import com.seedsoft.ykt.widget.ProgressDialogExtend;

public class Util {
	private final static String TAG = "Util";

	/**
	 * 检查是否初次安装
	 * */
	public static boolean checkIsFirst(Context mContext) {
		// TODO Auto-generated method stub
		// int i = 0;
		SharedPreferences sp = mContext.getSharedPreferences("isFirst",
				Context.MODE_PRIVATE);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "--installInfo--" + sp.getString("isFirst", ""));
		}

		if (sp.getString("isFirst", "").equals("1")) {
			// sp.edit().putInt("openNum", 1).commit();
			return false;
		} else {
			// i = sp.getInt("openNum", 0);
			sp.edit().putString("isFirst", "1").commit();
			// sp.edit().putInt("openNum", ++i).commit();
		}
		;
		return true;
	}

	/**
	 * 为当前应用添加桌面快捷方式
	 */
	public static void addShortcut(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		Intent shortcutIntent = context.getPackageManager()
				.getLaunchIntentForPackage(context.getPackageName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = context.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}
		// 快捷方式名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 不允许重复创建（不一定有效）
		shortcut.putExtra("duplicate", false);
		// 快捷方式的图标
		Parcelable iconResource = Intent.ShortcutIconResource.fromContext(
				context, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

		context.sendBroadcast(shortcut);
	}

	/**
	 * 删除当前应用的桌面快捷方式
	 */
	public static void delShortcut(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = context.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}
		// 快捷方式名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		Intent shortcutIntent = context.getPackageManager()
				.getLaunchIntentForPackage(context.getPackageName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		context.sendBroadcast(shortcut);
	}

	/**
	 * 判断桌面是否已添加快捷方式
	 */
	public static boolean hasShortcut(Context context) {
		boolean result = false;
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = context.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}

		final String uriStr;
		if (android.os.Build.VERSION.SDK_INT < 8) {
			uriStr = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		final Uri CONTENT_URI = Uri.parse(uriStr);
		final Cursor c = context.getContentResolver().query(CONTENT_URI, null,
				"title=?", new String[] { title }, null);
		if (c != null && c.getCount() > 0) {
			result = true;
		}
		return result;
	}

	/** 保存pushMsg（向SDCard写数据） */
	public static void saveFile(String fileName, String fileContent) {
		BufferedWriter bw = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(new File(fileName), true);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);

			bw.write(fileContent);
			bw.newLine();
			bw.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (osw != null) {
					osw.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/** 读取pushMsg（从SDCard读取数据） */
	public static String getFileRead(String filePath) {
		FileReader fr = null;
		BufferedReader br = null;
		String content = null;
		File file = new File(filePath);
		if (file.exists()) {
			String line = "";
			content = new String();
			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				while ((line = br.readLine()) != null) {
					content += line.trim();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
					}
					if (fr != null) {
						fr.close();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return content;

	}

	/** 下载图片 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			Log.d(TAG, url);
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/** 文件路径转为bitmap */
	public static Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	/**
	 * 文字转视图的方法
	 * */
	public static TextView getView(Context mContext, String string) {

		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView tv = new TextView(mContext);
		tv.setLayoutParams(layoutParams);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(3, 3, 3, 3);
		tv.setText(string);
		tv.setTextSize(18);
		tv.setTextColor(Color.WHITE);
		tv.setBackgroundResource(R.drawable.radio_btn_bg);
		return tv;
	}

	/** 百度地图的pop弹出图片View转Bitmap */
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	/**
	 * 先判断是否安装，已安装则启动目标应用程序，否则先安装
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @param appPath
	 *            目标应用apk安装文件所在的路径
	 */
	public void launchApp(Context mContext, String packageName, String appPath,
			String name, String size) {

		if (new File("/data/data/" + packageName).exists()) {
			// 获取目标应用安装包的Intent,启动目标应用
			Intent intent = mContext.getPackageManager()
					.getLaunchIntentForPackage(packageName);
			mContext.startActivity(intent);
		} else {
			showNoticeDialog(
					mContext,
					mContext.getResources().getString(
							R.string.soft_download_title),
					mContext.getResources().getString(
							R.string.soft_download_info), appPath,
					getSDCardPath(mContext) + Constants.DOWNLOAD_PATH, name,
					size);
		}
	}

	/**
	 * 显示下载提示对话框
	 * 
	 * @param Context
	 * @param title
	 *            "提示："
	 * @param content
	 *            "您未安装此软件，现在安装吗？"
	 * @param downloadpath
	 *            下载路径
	 * @param name
	 *            下载的文件名
	 * @param size
	 *            下载的文件大小
	 * @param downLoadPath
	 *            下载文件路径
	 * @param savePath
	 *            保存文件路径
	 * */
	public void showNoticeDialog(final Context mContext, String title,
			String content, final String downLoadPath, final String savePath,
			final String name, final String size) {

		new DialogExtend(mContext, title, content) {

			@Override
			protected void setOK(Dialog dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// 此处，可以创建一个后台服务，实现在通知栏的下载状态提醒。
				Intent service = new Intent(
						"com.seedsoft.zstysupport.util.BackgroundDownloadService");
				service.putExtra("downLoadPath", downLoadPath)
						.putExtra("savePath", savePath).putExtra("name", name)
						.putExtra("size", size);

				mContext.startService(service);

			}

			@Override
			protected void setCancle(Dialog dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};
	}

	/**
	 * 根据应用的名字,查询安装的包名(用途：例如：用来打开应用)
	 * */
	public static String showPackageNames(Context context, String name) {

		List<ApplicationInfo> app = context.getPackageManager()
				.getInstalledApplications(0);
		for (ApplicationInfo applicationInfo : app) {
			if (name.equals(context.getPackageManager().getApplicationLabel(
					applicationInfo))) {
				return applicationInfo.packageName;
			}
		}
		return null;
	}

	/** 判断网络连接方法 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getApplicationContext().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			if (manager != null) {
				NetworkInfo networkInfo = manager.getActiveNetworkInfo();
				if (networkInfo != null) {
					return networkInfo.isAvailable();
				}
			}
		}
		return false;
	}

	/** 关闭输入法软键盘 */
	public static void closeInput(Context context) {

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (((Activity) context).getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/** 获取SD卡缓存目录 */
	public static String getSDCardPath(Context mContext) {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(mContext)
				.getPath() : mContext.getCacheDir().getPath();
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * Get the external app cache directory.
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	@TargetApi(8)
	public static File getExternalCacheDir(Context context) {
		if (hasFroyo()) {
			return context.getExternalCacheDir();
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + cacheDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return file;
	}

	/**
	 * Check how much usable space is available at a given path.
	 * 
	 * @param path
	 *            The path to check
	 * @return The space available in bytes
	 */
	@TargetApi(9)
	public static long getUsableSpace(File path) {
		if (hasGingerbread()) {
			return path.getUsableSpace();
		}
		final StatFs stats = new StatFs(path.getPath());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	/**
	 * 卸载应用
	 */
	public void uninstallAPK(Context mContext, String packageName) {

		Uri uri = Uri.parse("package:" + packageName);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		mContext.startActivity(intent);

	}

	/**
	 * 检查版本更新状态
	 * */

	public void isUpdate(final Context mContext) {

		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub

				synchronized (mContext) {
					return new ParseXMLUtil().parseUpdateXml(params[0]);
				}
			}

			protected void onPostExecute(String result) {

				if (result != null) {
					String[] str = result.split("@");
					int sCode = Integer.valueOf(str[1]);

					int cCode = 0;
					try {
						cCode = mContext.getPackageManager().getPackageInfo(
								mContext.getPackageName(), 0).versionCode;
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (BuildConfig.DEBUG) {
						Log.d(TAG, "--cCode===" + cCode);
						Log.d(TAG, "--sCode===" + sCode);
					}

					if (sCode > cCode) {
						showNoticeDialog(
								mContext,
								mContext.getResources().getString(
										R.string.soft_update_title),
								mContext.getResources().getString(
										R.string.soft_update_info),
								Constants.SHARE_URL,
								getSDCardPath(mContext)
										+ Constants.DOWNLOAD_PATH,
								mContext.getResources().getString(
										R.string.soft_update_apk_name), str[2]);
					} else {
						Toast toast = Toast.makeText(mContext,
								R.string.soft_update_no, Toast.LENGTH_LONG);
						// toast.getView().setBackgroundResource(
						// R.drawable.red_toast_bg);
						toast.getView().setPadding(20, 10, 20, 10);
						toast.show();

					}
					;
				} else {
					Toast toast = Toast.makeText(mContext,
							R.string.soft_update_no, Toast.LENGTH_LONG);
					// toast.getView().setBackgroundResource(
					// R.drawable.red_toast_bg);
					toast.getView().setPadding(20, 10, 20, 10);
					toast.show();
				}
				;

			};
		}.execute(Constants.UPDATE_URL);
	}

	/**
	 * 检查是否有更新配置文件 已经提到welcomeactivity中实现
	 * */
	public void checkIsRefresh(final Context mContext) {

		// 获取服务器更新时间
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub

				synchronized (mContext) {
					return new ParseXMLUtil()
							.parseRefreshXml(Constants.REFRESH_TIME_URL);
				}

			}

			@Override
			protected void onPostExecute(String result) {
				if (result == null) {
					Log.e(TAG, "checkIsRefresh is fail");
				} else {
					try {
						InitMainPageDate(mContext, result);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}.execute(Constants.REFRESH_TIME_URL);
	}

	/**
	 * 初始化主页数据 执行流程 1.检查sd卡，相应路径下是否存在配置文件？
	 * Y2.存在，解析展示。Y3.开启一个线程，后台检查是否有更新？Y4.有更新，则下载，解析新配置，更新主页。
	 * N2.不存在，下载(提醒：首次初始化工作较慢，下次速度会非常快...)
	 * */
	public void InitMainPageDate(Context mContext) throws Exception {
		// 判断是否已下载配置文件
		File file = new File(getSDCardPath(mContext) + Constants.CONFIG_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] children = file.listFiles();
		// 存在配置文件
		if (children.length > 0) {
			// 解析配置文件，跳转到主页
			new ParseAsynck(mContext, false, false).execute(children[0]
					.getAbsolutePath());
		} else {
			// 不存在，初次安装，需要下载
			new DownLoadAsynTask(mContext, getSDCardPath(mContext)
					+ Constants.CONFIG_PATH + File.separator
					+ Constants.CFG_FILE_NAME, false)
					.execute(Constants.MAIN_ACTION_URL);
		}
	};

	/** 从主页activity发起的更新 */
	public void InitMainPageDate(Context mContext, String result)
			throws Exception {
		String client_name = null;
		String server_name = result + ".cfg";

		// 获取已下载配置文件的名字
		File file = new File(getSDCardPath(mContext) + Constants.CONFIG_PATH);
		File[] children = file.listFiles();
		client_name = children[0].getName();

		if (BuildConfig.DEBUG) {
			Log.d("-client-", client_name);
			Log.d("-server-", server_name);
		}

		if (!client_name.equals(server_name)) {
			// 删掉原有的
			children[0].delete();
			// 重新下载
			new DownLoadAsynTask(mContext, getSDCardPath(mContext)
					+ Constants.CONFIG_PATH + File.separator + server_name,
					true).execute(Constants.MAIN_ACTION_URL);
		}

	};

	/**
	 * 异步下载配置文件
	 * */
	public class DownLoadAsynTask extends AsyncTask<String, Void, Void> {

		private Context mContext;
		private String fileAbsolutePath;
		private ProgressDialogExtend pde;
		private boolean fromMainActivityUpdate;

		public DownLoadAsynTask(Context mContext, String fileAbsolutePath,
				boolean fromMainActivityUpdate) {
			this.mContext = mContext;
			this.fileAbsolutePath = fileAbsolutePath;
			this.fromMainActivityUpdate = fromMainActivityUpdate;
		};

		@Override
		protected void onPreExecute() {
			// 提醒：正更新数据，之后会飞快进入...
			pde = ProgressDialogExtend.CreateSiglePDE(mContext, "init");
			pde.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			down(mContext, params[0], fileAbsolutePath);
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			pde.dialog.dismiss();
			// 下载主页 配置文件后，解析它
			new ParseAsynck(mContext, true, fromMainActivityUpdate)
					.execute(fileAbsolutePath);
		}

	}

	/** 网络异常提示框 */
	public static void ConnectionExceptionDialog(final Context context) {
		new DialogExtend(context, context.getResources().getString(
				R.string.no_network_connection_dialog_title), context
				.getResources().getString(
						R.string.no_network_connection_dialog_info)) {
			@Override
			protected void setOK(Dialog dialog) {
				// TODO Auto-generated method stub
				// 打开网络连接设置
				Intent intent = null;
				// 判断手机系统的版本 即API大于10 就是3.0或以上版本
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(
							android.provider.Settings.ACTION_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				context.startActivity(intent);
				dialog.dismiss();
				if (context.getClass().getName()
						.equals(context.getPackageName() + ".WelcomeActivity")) {
					((Activity) context).finish();
				}
			}

			@Override
			protected void setCancle(Dialog dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if ((context.getPackageName() + ".WelcomeActivity")
						.equals(context.getClass().getName())) {
					((Activity) context).finish();
				}
			}
		};

	}

	/**
	 * 下载文件的具体操作
	 * 
	 * */
	private void down(Context mContext, String urlPath, String fileAbsolutePath) {
	
		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// conn .setRequestProperty("Accept-Encoding", "identity");
			conn.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			conn.connect();
			InputStream is = conn.getInputStream();
			// int length = conn.getContentLength();

			File downFile = new File(fileAbsolutePath);
			FileOutputStream fos = new FileOutputStream(downFile);
			int count = 0;
			byte buf[] = new byte[1024];
			do {
				int numread = is.read(buf);
				if (numread > 0) {
					count += numread;
				} else {
					break;
				}
				fos.write(buf, 0, numread);
			} while (true);//
			fos.close();
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 解析主页配置文件，进入主页面
	 * 
	 * @param boolean hasUpdate; 服务器端 配置文件有无更新 true：有更新 false：无更新
	 * */
	public class ParseAsynck extends
			AsyncTask<String, Void, ArrayList<RootBean>> {

		private Context mContext;
		private boolean hasUpdate;
		private boolean fromMainActivityUpdate;

		public ParseAsynck(Context mContext, boolean hasUpdate,
				boolean fromMainActivityUpdate) {
			this.mContext = mContext;
			this.hasUpdate = hasUpdate;
			this.fromMainActivityUpdate = fromMainActivityUpdate;
		}

		@Override
		protected ArrayList<RootBean> doInBackground(String... params) {
			// TODO Auto-generated method stub

			synchronized (params) {
				return ParseXMLUtil
						.parseMainXml(mContext, params[0], hasUpdate);
			}

		}

		@Override
		protected void onPostExecute(ArrayList<RootBean> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				String TAG = "CHECK_DATA：";
				if (BuildConfig.DEBUG) {
					int a = result.size();
					for (int i = 0; i < a; i++) {

						RootBean rb = result.get(i);
						Log.e(TAG,
								"========" + rb.getName() + ":"
										+ rb.getShowType() + "========");
						ArrayList<FatherBean> afb = rb.getFatherBeans();
						int b = afb.size();
						for (int j = 0; j < b; j++) {
							FatherBean fb = afb.get(j);
							Log.i(TAG,
									"+++++" + fb.getName() + ":"
											+ fb.getFrontImage() + "+++++");
							ArrayList<FatherModuleBean> afmb = fb
									.getFatherModuleBeans();
							int c = afmb.size();
							for (FatherModuleBean fmb : afmb) {
								Log.i(TAG, "0000000000000");
								Log.i(TAG,
										"00" + fmb.getName() + ":"
												+ fmb.getType() + "00");
							}
						}
					}

				}

				// 保存全局配置对象
				((BaseApplication) ((Activity) mContext).getApplication())
						.setRootBeans(result);
				RootBean rootBean = result.get(0);
				// 保存登录标志
				mContext.getSharedPreferences(Constants.SP_SAVE_NAME,
						Context.MODE_PRIVATE).edit()
						.putBoolean("open_flag", true).commit();
				// 跳转activity
				mContext.startActivity(new Intent(mContext, MainActivity.class)
						.putExtra("ROOTBEAN", rootBean));
				// 进入动画效果
				if (fromMainActivityUpdate) {
					// 主页，从中心淡入
					((Activity) mContext).overridePendingTransition(
							R.anim.fade_enter, R.anim.fade_out);
				} else {
					// 广告欢迎页,从右侧进入
					((Activity) mContext).overridePendingTransition(
							R.anim.hold_enter, R.anim.fade_out);
				}

			} else {
				Toast toast = Toast.makeText(mContext, "网速不给力哦!",
						Toast.LENGTH_LONG);
				// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
				toast.getView().setPadding(20, 10, 20, 10);
				toast.show();
				ConnectionExceptionDialog(mContext);
			}

		}
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		if (sdDir == null && "".equals(sdDir)) {
			return null;
		}
		return sdDir.toString();
	}

	// ==============================================================
	/** 检查是否有更新配置文件 */
	public void checkIsRefreshOld(final Context mContext) {
		// 获取服务器更新时间
		try {
			new AsyncTask<String, Void, String>() {

				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					String re = null;
					try {
						re = new ParseXMLUtil()
						.parseRefreshXml(Constants.REFRESH_TIME_URL);
						System.out.println("doingllll"+re);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return re;

				}

				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					if (result != null) {
						// 判断是否已下载配置文件
						File file = new File(getSDCardPath(mContext)
								+ Constants.CONFIG_PATH);
						if (!file.exists()) {
							file.mkdirs();
						}
						File[] children = file.listFiles();
						String name = null;
						// 是， 则取其名字（以更新时间命名）与新下载的更新时间对比。
						if (children.length > 0) {
							name = children[0].getName();
							name = name.substring(0, name.indexOf("."));

							if (BuildConfig.DEBUG) {
								Log.d("--client--", name);
								Log.d("--server-", result);
							}

							if (!name.equals(result)) {
								// 删掉原有的
								for (int i = 0; i < children.length; i++) {
									children[i].delete();
								}
								// 重新下载
								new DownLoadAsynTaskOld(mContext).execute(
										Constants.MAIN_ACTION_URL, result + ".0");
							} else {
								// 跳转到主页
								new ParseAsynckOld(mContext)
										.execute(getMyInputStream(mContext));
							}
						} else {
							System.out.println("xax");
							// 否，联网下载
							new DownLoadAsynTaskOld(mContext).execute(
									Constants.MAIN_ACTION_URL, result + ".0");
						}
					} else {
						Log.e(TAG, "checkIsRefresh is fail");
						// 跳转到主页
						new ParseAsynckOld(mContext)
								.execute(getMyInputStream(mContext));
					}
				};


				
			}.execute(Constants.REFRESH_TIME_URL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("eeee");
			e.printStackTrace();
		}
	}

	/**
	 * 异步下载配置文件
	 * */
	public class DownLoadAsynTaskOld extends AsyncTask<String, Void, Void> {

		private Context mContext;

		public DownLoadAsynTaskOld(Context mContext) {
			this.mContext = mContext;
		};

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			synchronized (TAG) {
				down(mContext, params[0], params[1]);

				return null;
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			new ParseAsynckOld(mContext).execute(getMyInputStream(mContext));
		}

	}

	/** 下载文件的具体操作 */
	private void downOld(Context mContext, String path, String fileName) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// conn .setRequestProperty("Accept-Encoding", "identity");
			conn.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			conn.connect();
			InputStream is = conn.getInputStream();
			// int length = conn.getContentLength();

			File downFile = new File(getSDCardPath(mContext)
					+ Constants.CONFIG_PATH, fileName);
			FileOutputStream fos = new FileOutputStream(downFile);
			int count = 0;
			byte buf[] = new byte[1024];
			do {
				int numread = is.read(buf);
				if (numread > 0) {
					count += numread;
				} else {
					break;
				}
				fos.write(buf, 0, numread);
			} while (true);//
			fos.close();
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class ParseAsynckOld extends
			AsyncTask<InputStream, Void, ArrayList<RootBean>> {

		private Context mContext;

		public ParseAsynckOld(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		protected ArrayList<RootBean> doInBackground(InputStream... params) {
			// TODO Auto-generated method stub

			synchronized (params) {
				return ParseXMLUtil.parseMainXml(params[0]);
			}

		}

		@Override
		protected void onPostExecute(ArrayList<RootBean> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				// 保存全局配置对象
				((BaseApplication) ((Activity) mContext).getApplication())
						.setRootBeans(result);
				RootBean rootBean = result.get(0);
				// 保存登录标志
				mContext.getSharedPreferences(Constants.SP_SAVE_NAME,
						Context.MODE_PRIVATE).edit()
						.putBoolean("open_flag", true).commit();
				// 跳转activity
				mContext.startActivity(new Intent(mContext, MainActivity.class)
						.putExtra("ROOTBEAN", rootBean));
				((Activity) mContext).overridePendingTransition(
						R.anim.hold_enter, R.anim.fade_out);
				((Activity) mContext).finish();
			} else {
				Toast.makeText(mContext, "网速不给力哦！", Toast.LENGTH_LONG).show();
				((Activity) mContext).finish();
			}

		}
	}

	/**
	 * 返回主页配置文件的输入流
	 * */
	public static InputStream getMyInputStream(Context mContext) {
		// 1.判断是否已下载配置文件
		File file = new File(getSDCardPath(mContext) + Constants.CONFIG_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] children = file.listFiles();
		if (children.length > 0) {
			try {
				return new FileInputStream(children[0]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				URLConnection conn = new URL(Constants.MAIN_ACTION_URL)
						.openConnection();
				conn.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
				return conn.getInputStream();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return null;

	}
}
