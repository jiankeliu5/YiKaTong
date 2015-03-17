package com.seedsoft.ykt.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 更新版本服务
 */
public class UpdateService extends Service {

	private Notification notification;

	private NotificationManager notificationManager;

	private int state = 1;

	private String savePath;

	private String ip;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.ic_launcher, getResources()
				.getString(R.string.notification), System.currentTimeMillis());
		notification.contentView = new RemoteViews(getPackageName(),
				R.layout.notification);
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.contentView.setProgressBar(R.id.progress, 100, 0, false);
		notificationManager.notify(0, notification);
		ip = getResources().getString(R.string.ip);
		savePath = Util.getSDPath() + "/"
				+ getResources().getString(R.string.app_name) + ".apk";
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (getSharedPreferences(Constants.SP_SAVE_NAME, Context.MODE_PRIVATE).getInt(
				"state", 0) != 0)
			new UpdateThead().start();

		return super.onStartCommand(intent, flags, startId);
	}

	public class UpdateThead extends Thread {

		@Override
		public void run() {
			File file = new File(savePath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Log.i("xingwei", "savePath=" + savePath);
			SharedPreferences sharedPreferences = getSharedPreferences(
					Constants.SP_SAVE_NAME, Context.MODE_PRIVATE);
			Intent intent = new Intent("com.sxwd.soft.progress.receiver");
			String url = ip + "palmxian"
					+ sharedPreferences.getString("link", "");
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			int len = 0;
			try {
				HttpResponse response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == 200) {
					FileOutputStream fo = new FileOutputStream(file);
					InputStream is = response.getEntity().getContent();
					byte[] b = new byte[2048];
					int fileSize = Integer.valueOf(sharedPreferences.getString(
							"size", ""));
					int progress = 0;
					int pr = 0;
					Log.i("xingwei", "state=" + state);
					while ((len = is.read(b)) != -1 && state != 0) {
						fo.write(b, 0, len);
						progress += len;
						if (pr != (progress * 100) / fileSize) {
							pr = (progress * 100) / fileSize;
							intent.putExtra("progress", pr);
							Log.i("xingwei", "pr=" + pr + "len=" + len
									+ "fileSize" + fileSize);
							sendBroadcast(intent);
							notification.contentView.setProgressBar(
									R.id.progress, 100, pr, false);
							notification.contentView.setTextViewText(R.id.text,
									pr + "%");
							notificationManager.notify(0, notification);
							state = getSharedPreferences(Constants.SP_SAVE_NAME,
									Context.MODE_PRIVATE).getInt("state", 0);
						}
					}
					if (fo != null)
						fo.close();
					if (is != null)
						is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			intent.putExtra("progress", -1);
			sharedPreferences.edit().putInt("state", 0).commit();
			sendBroadcast(intent);
			notificationManager.cancel(0);
			if (len == -1) {
				intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				notification.contentIntent = PendingIntent.getActivity(
						getApplicationContext(), 0, intent, 0);
				startActivity(intent);
			}
			stopSelf();
		}
	}
}
