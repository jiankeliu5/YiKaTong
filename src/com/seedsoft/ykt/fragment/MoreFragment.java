package com.seedsoft.ykt.fragment;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.util.Util;
import com.seedsoft.ykt.widget.LoopProgressBar;

@SuppressLint("NewApi")
public class MoreFragment extends Fragment {

	private View v;

	public ImageView messageIv;

	private LoopProgressBar bar;

	public boolean isMessage = true;

	public SharedPreferences configuration;

	private String titleText;

	private TextView title;

	private RelativeLayout message;

	private RelativeLayout empty;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				stopBar();
				empty.setClickable(true);
				Toast.makeText(getActivity(), "数据已清除！", Toast.LENGTH_SHORT).show();
			}
		}
	};

	public MoreFragment(SharedPreferences configuration, String titleText) {
		this.configuration = configuration;
		this.titleText = titleText;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isMessage = configuration.getBoolean("isMessage", true);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.more_fragment, null);
		Log.i("xingwei", "onCreateView");
		initView(v);
		return v;
	}

	public void initView(View v) {
		messageIv = (ImageView) v.findViewById(R.id.ic_message);
		bar = (LoopProgressBar) v.findViewById(R.id.empty_progress_bar);
		title = (TextView) v.findViewById(R.id.top_title);
		title.setText(titleText);
		message = (RelativeLayout) v.findViewById(R.id.message);
		empty = (RelativeLayout) v.findViewById(R.id.empty);
		setMessage();
		message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setMessage();
			}
		});
		empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setBar(getResources().getColor(R.color.green));
				v.setClickable(false);
				new Thread() {
					public void run() {
						String apkUrl = Util.getSDPath() + "/"
								+ getResources().getString(R.string.app_name)
								+ ".apk";
						if (configuration.getInt("state", 0) == 0) {
							File file = new File(apkUrl);
							if (file.exists()) {
								file.delete();
							}
						}
						handler.sendEmptyMessage(1);
					};
				}.start();
			}
		});
	}

	public void setMessage() {
		isMessage = configuration.getBoolean("isMessage", true);
		messageIv.setImageResource(isMessage ? R.drawable.off : R.drawable.on);
		configuration.edit().putBoolean("isMessage", !isMessage).commit();
	}

	public void setBar(int color) {
		bar.setColor(color);
		bar.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopBar();
	}

	public void stopBar() {
		bar.setColor(getResources().getColor(R.color.result_text));
		bar.stopBar();
	}
}
