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
	 * ����Ƿ���ΰ�װ
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
	 * Ϊ��ǰӦ����������ݷ�ʽ
	 */
	public static void addShortcut(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		Intent shortcutIntent = context.getPackageManager()
				.getLaunchIntentForPackage(context.getPackageName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// ��ȡ��ǰӦ������
		String title = null;
		try {
			final PackageManager pm = context.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}
		// ��ݷ�ʽ����
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// �������ظ���������һ����Ч��
		shortcut.putExtra("duplicate", false);
		// ��ݷ�ʽ��ͼ��
		Parcelable iconResource = Intent.ShortcutIconResource.fromContext(
				context, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

		context.sendBroadcast(shortcut);
	}

	/**
	 * ɾ����ǰӦ�õ������ݷ�ʽ
	 */
	public static void delShortcut(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");

		// ��ȡ��ǰӦ������
		String title = null;
		try {
			final PackageManager pm = context.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}
		// ��ݷ�ʽ����
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		Intent shortcutIntent = context.getPackageManager()
				.getLaunchIntentForPackage(context.getPackageName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		context.sendBroadcast(shortcut);
	}

	/**
	 * �ж������Ƿ�����ӿ�ݷ�ʽ
	 */
	public static boolean hasShortcut(Context context) {
		boolean result = false;
		// ��ȡ��ǰӦ������
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

	/** ����pushMsg����SDCardд���ݣ� */
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

	/** ��ȡpushMsg����SDCard��ȡ���ݣ� */
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

	/** ����ͼƬ */
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

	/** �ļ�·��תΪbitmap */
	public static Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// ����Ϊtureֻ��ȡͼƬ��С
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// ����Ϊ��
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// ����
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
	 * ����ת��ͼ�ķ���
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

	/** �ٶȵ�ͼ��pop����ͼƬViewתBitmap */
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	/**
	 * ���ж��Ƿ�װ���Ѱ�װ������Ŀ��Ӧ�ó��򣬷����Ȱ�װ
	 * 
	 * @param packageName
	 *            Ŀ��Ӧ�ð�װ��İ���
	 * @param appPath
	 *            Ŀ��Ӧ��apk��װ�ļ����ڵ�·��
	 */
	public void launchApp(Context mContext, String packageName, String appPath,
			String name, String size) {

		if (new File("/data/data/" + packageName).exists()) {
			// ��ȡĿ��Ӧ�ð�װ����Intent,����Ŀ��Ӧ��
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
	 * ��ʾ������ʾ�Ի���
	 * 
	 * @param Context
	 * @param title
	 *            "��ʾ��"
	 * @param content
	 *            "��δ��װ����������ڰ�װ��"
	 * @param downloadpath
	 *            ����·��
	 * @param name
	 *            ���ص��ļ���
	 * @param size
	 *            ���ص��ļ���С
	 * @param downLoadPath
	 *            �����ļ�·��
	 * @param savePath
	 *            �����ļ�·��
	 * */
	public void showNoticeDialog(final Context mContext, String title,
			String content, final String downLoadPath, final String savePath,
			final String name, final String size) {

		new DialogExtend(mContext, title, content) {

			@Override
			protected void setOK(Dialog dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// �˴������Դ���һ����̨����ʵ����֪ͨ��������״̬���ѡ�
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
	 * ����Ӧ�õ�����,��ѯ��װ�İ���(��;�����磺������Ӧ��)
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

	/** �ж��������ӷ��� */
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

	/** �ر����뷨����� */
	public static void closeInput(Context context) {

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (((Activity) context).getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/** ��ȡSD������Ŀ¼ */
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
	 * ж��Ӧ��
	 */
	public void uninstallAPK(Context mContext, String packageName) {

		Uri uri = Uri.parse("package:" + packageName);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		mContext.startActivity(intent);

	}

	/**
	 * ���汾����״̬
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
	 * ����Ƿ��и��������ļ� �Ѿ��ᵽwelcomeactivity��ʵ��
	 * */
	public void checkIsRefresh(final Context mContext) {

		// ��ȡ����������ʱ��
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
	 * ��ʼ����ҳ���� ִ������ 1.���sd������Ӧ·�����Ƿ���������ļ���
	 * Y2.���ڣ�����չʾ��Y3.����һ���̣߳���̨����Ƿ��и��£�Y4.�и��£������أ����������ã�������ҳ��
	 * N2.�����ڣ�����(���ѣ��״γ�ʼ�������������´��ٶȻ�ǳ���...)
	 * */
	public void InitMainPageDate(Context mContext) throws Exception {
		// �ж��Ƿ������������ļ�
		File file = new File(getSDCardPath(mContext) + Constants.CONFIG_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] children = file.listFiles();
		// ���������ļ�
		if (children.length > 0) {
			// ���������ļ�����ת����ҳ
			new ParseAsynck(mContext, false, false).execute(children[0]
					.getAbsolutePath());
		} else {
			// �����ڣ����ΰ�װ����Ҫ����
			new DownLoadAsynTask(mContext, getSDCardPath(mContext)
					+ Constants.CONFIG_PATH + File.separator
					+ Constants.CFG_FILE_NAME, false)
					.execute(Constants.MAIN_ACTION_URL);
		}
	};

	/** ����ҳactivity����ĸ��� */
	public void InitMainPageDate(Context mContext, String result)
			throws Exception {
		String client_name = null;
		String server_name = result + ".cfg";

		// ��ȡ�����������ļ�������
		File file = new File(getSDCardPath(mContext) + Constants.CONFIG_PATH);
		File[] children = file.listFiles();
		client_name = children[0].getName();

		if (BuildConfig.DEBUG) {
			Log.d("-client-", client_name);
			Log.d("-server-", server_name);
		}

		if (!client_name.equals(server_name)) {
			// ɾ��ԭ�е�
			children[0].delete();
			// ��������
			new DownLoadAsynTask(mContext, getSDCardPath(mContext)
					+ Constants.CONFIG_PATH + File.separator + server_name,
					true).execute(Constants.MAIN_ACTION_URL);
		}

	};

	/**
	 * �첽���������ļ�
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
			// ���ѣ����������ݣ�֮���ɿ����...
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
			// ������ҳ �����ļ��󣬽�����
			new ParseAsynck(mContext, true, fromMainActivityUpdate)
					.execute(fileAbsolutePath);
		}

	}

	/** �����쳣��ʾ�� */
	public static void ConnectionExceptionDialog(final Context context) {
		new DialogExtend(context, context.getResources().getString(
				R.string.no_network_connection_dialog_title), context
				.getResources().getString(
						R.string.no_network_connection_dialog_info)) {
			@Override
			protected void setOK(Dialog dialog) {
				// TODO Auto-generated method stub
				// ��������������
				Intent intent = null;
				// �ж��ֻ�ϵͳ�İ汾 ��API����10 ����3.0�����ϰ汾
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
	 * �����ļ��ľ������
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
	 * ������ҳ�����ļ���������ҳ��
	 * 
	 * @param boolean hasUpdate; �������� �����ļ����޸��� true���и��� false���޸���
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
				String TAG = "CHECK_DATA��";
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

				// ����ȫ�����ö���
				((BaseApplication) ((Activity) mContext).getApplication())
						.setRootBeans(result);
				RootBean rootBean = result.get(0);
				// �����¼��־
				mContext.getSharedPreferences(Constants.SP_SAVE_NAME,
						Context.MODE_PRIVATE).edit()
						.putBoolean("open_flag", true).commit();
				// ��תactivity
				mContext.startActivity(new Intent(mContext, MainActivity.class)
						.putExtra("ROOTBEAN", rootBean));
				// ���붯��Ч��
				if (fromMainActivityUpdate) {
					// ��ҳ�������ĵ���
					((Activity) mContext).overridePendingTransition(
							R.anim.fade_enter, R.anim.fade_out);
				} else {
					// ��滶ӭҳ,���Ҳ����
					((Activity) mContext).overridePendingTransition(
							R.anim.hold_enter, R.anim.fade_out);
				}

			} else {
				Toast toast = Toast.makeText(mContext, "���ٲ�����Ŷ!",
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
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
		}
		if (sdDir == null && "".equals(sdDir)) {
			return null;
		}
		return sdDir.toString();
	}

	// ==============================================================
	/** ����Ƿ��и��������ļ� */
	public void checkIsRefreshOld(final Context mContext) {
		// ��ȡ����������ʱ��
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
						// �ж��Ƿ������������ļ�
						File file = new File(getSDCardPath(mContext)
								+ Constants.CONFIG_PATH);
						if (!file.exists()) {
							file.mkdirs();
						}
						File[] children = file.listFiles();
						String name = null;
						// �ǣ� ��ȡ�����֣��Ը���ʱ���������������صĸ���ʱ��Աȡ�
						if (children.length > 0) {
							name = children[0].getName();
							name = name.substring(0, name.indexOf("."));

							if (BuildConfig.DEBUG) {
								Log.d("--client--", name);
								Log.d("--server-", result);
							}

							if (!name.equals(result)) {
								// ɾ��ԭ�е�
								for (int i = 0; i < children.length; i++) {
									children[i].delete();
								}
								// ��������
								new DownLoadAsynTaskOld(mContext).execute(
										Constants.MAIN_ACTION_URL, result + ".0");
							} else {
								// ��ת����ҳ
								new ParseAsynckOld(mContext)
										.execute(getMyInputStream(mContext));
							}
						} else {
							System.out.println("xax");
							// ����������
							new DownLoadAsynTaskOld(mContext).execute(
									Constants.MAIN_ACTION_URL, result + ".0");
						}
					} else {
						Log.e(TAG, "checkIsRefresh is fail");
						// ��ת����ҳ
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
	 * �첽���������ļ�
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

	/** �����ļ��ľ������ */
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
				// ����ȫ�����ö���
				((BaseApplication) ((Activity) mContext).getApplication())
						.setRootBeans(result);
				RootBean rootBean = result.get(0);
				// �����¼��־
				mContext.getSharedPreferences(Constants.SP_SAVE_NAME,
						Context.MODE_PRIVATE).edit()
						.putBoolean("open_flag", true).commit();
				// ��תactivity
				mContext.startActivity(new Intent(mContext, MainActivity.class)
						.putExtra("ROOTBEAN", rootBean));
				((Activity) mContext).overridePendingTransition(
						R.anim.hold_enter, R.anim.fade_out);
				((Activity) mContext).finish();
			} else {
				Toast.makeText(mContext, "���ٲ�����Ŷ��", Toast.LENGTH_LONG).show();
				((Activity) mContext).finish();
			}

		}
	}

	/**
	 * ������ҳ�����ļ���������
	 * */
	public static InputStream getMyInputStream(Context mContext) {
		// 1.�ж��Ƿ������������ļ�
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
