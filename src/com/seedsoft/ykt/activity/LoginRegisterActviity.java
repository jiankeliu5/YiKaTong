package com.seedsoft.ykt.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.MD5Util;
import com.seedsoft.ykt.widget.LoopProgressBar;

public class LoginRegisterActviity extends BaseActivity {

	private int layoutId = R.layout.login_activity;

	private LoopProgressBar bar;

	private EditText name, password, password2, email;

	private TextView check, submit;

	private boolean isCheck = true;

	private boolean isLogin;

	private String IMEI, IMSI, token;

	public static final String systemIdentify = "zhangshanghuayin";

	private String ip;

	private String url;

	private PopupWindow pop;

	private View contentView;

	private boolean isAlter = false;

	private String nameText, passwordText;

	private SharedPreferences configuration;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configuration = getSharedPreferences(Constants.SP_SAVE_NAME,
				Context.MODE_PRIVATE);
		isLogin = getIntent().getBooleanExtra("isLogin", false);
		ip = getResources().getString(R.string.ip);
		if (isLogin) {
			layoutId = R.layout.register_activity;
			url = ip + "palmxian/reg.action";
		} else {
			url = ip + "palmxian/auth.action";
		}
		contentView = getLayoutInflater().inflate(layoutId, null);
		setContentView(contentView);
		bar = (LoopProgressBar) findViewById(R.id.login_progress_bar);
		initTitle(R.string.get_back);
		setLoginImage(R.drawable.pre_btn, 0, 0, 0, R.string.get_back);
		setRegister(View.VISIBLE);
		if (isLogin) {
			initRegister();
		} else {
			initLogin();
		}
		if (!isLogin)
			return;
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();
		if (telephonyManager.getSimState() != 5) {
			IMSI = null;
		} else {
			IMSI = telephonyManager.getSubscriberId();
		}
		ip = getResources().getString(R.string.ip);
	}

	private void initLogin() {
		name = (EditText) findViewById(R.id.name);
		password = (EditText) findViewById(R.id.password);
		check = (TextView) findViewById(R.id.login_activity_cb);
		isCheck = configuration.getBoolean("isCheck", true);
		nameText = configuration.getString("name", "");
		passwordText = configuration.getString("password", "");
		String registerName = getIntent().getStringExtra("name");
		if (registerName != null && !"".equals(registerName)) {
			nameText = registerName;
			passwordText = "";
		}
		if (isCheck) {
			check.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.chec_box_on, 0, 0, 0);
			name.setText(nameText);
			password.setText(passwordText);
		} else {
			check.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.chec_box_off, 0, 0, 0);
		}
	}
	
	/**
	 * 记住密码
	 */
	public void checkBox(View v) {
		if (isCheck) {
			check.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.chec_box_off, 0, 0, 0);
			isCheck = false;
		} else {
			check.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.chec_box_on, 0, 0, 0);
			isCheck = true;
		}

	}

	private void initRegister() {
		name = (EditText) findViewById(R.id.name);
		password = (EditText) findViewById(R.id.password);
		password2 = (EditText) findViewById(R.id.password2);
		email = (EditText) findViewById(R.id.email);

	}

	public void register(View v) {
		startActivity(new Intent(getApplication(), LoginRegisterActviity.class)
				.putExtra("isLogin", true));
		finish();
	}

	/**
	 * 登录
	 */
	public void submit(View v) {
		submit = (TextView) v;
		nameText = name.getText().toString().trim();
		passwordText = password.getText().toString().trim();
		String emailText = null;
		if (nameText == null || passwordText == null || nameText.equals("")
				|| passwordText.equals("")) {
			Toast.makeText(getApplication(), "您输入的账户或密码为空，请您重新输入！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (nameText.length() < 4) {
			Toast toast = Toast.makeText(getApplicationContext(), "账号至少四个字符!",
					Toast.LENGTH_LONG);
			// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
			toast.getView().setPadding(20, 10, 20, 10);
			toast.show();
			// Toast.makeText(RegActivity.this, "账号至少四个字符！",
			// Toast.LENGTH_LONG).show();
			return;
		}

		if (isLogin) {
			// 注册账号
			String passwordText2 = password2.getText().toString().trim();
			emailText = email.getText().toString().trim();
			if (passwordText2 == null || passwordText2.equals("")
					|| emailText == null || emailText.equals("")) {
				Toast.makeText(getApplication(), "您输入的的密码或邮箱为空，请您从新输入！",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (!emailText.contains("@")) {
				Toast.makeText(getApplication(), "您输入的邮箱不合法，请您重新输入！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!passwordText2.equals(passwordText)) {
				Toast.makeText(getApplication(), "您输入的密码不一致，请您重新输入！",
						Toast.LENGTH_SHORT).show();
				password.getText().clear();
				password2.getText().clear();
				return;
			}

			if (passwordText2.length() < 6) {
				Toast toast = Toast.makeText(getApplication(), "密码至少6位!",
						Toast.LENGTH_LONG);
				// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
				toast.getView().setPadding(20, 10, 20, 10);
				toast.show();
				// Toast.makeText(RegActivity.this, "密码至少6位！",
				// Toast.LENGTH_LONG).show();
				return;
			}
			name.setFocusable(false);
			password.setFocusable(false);
			password2.setFocusable(true);
			submit.setText("正在申请，请稍后！");
		} else {
			// 登录账号
			submit.setText("正在登录...");
			name.setFocusable(false);
			password.setFocusable(false);
			check.setFocusable(false);
			configuration.edit().putBoolean("isCheck", isCheck).commit();
		}
		submit.setClickable(false);
		bar.setColor(getResources().getColor(R.color.green));
		bar.start();
		token = MD5Util.MD5(Constants.systemIdentify + nameText);
		new SubmitAsync().execute(url, nameText, passwordText, IMEI, IMSI,
				token, emailText);
	}

	/**
	 * 登录失败/注册失败
	 */
	private void failure() {
		int submitText = R.string.login;
		name.setFocusable(true);
		password.setFocusable(true);
		submit.setFocusable(true);
		submit.setClickable(true);
		if (isLogin) {
			submitText = R.string.register;
			password2.setFocusable(true);
			email.setFocusable(true);
		} else {
			check.setFocusable(true);
		}
		submit.setText(submitText);
		bar.stopBar();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bar.stopBar();
	}

	/*
	 * 找回密码
	 */
	@SuppressLint("InflateParams")
	public void find(View v) {
		isAlter = true;
		View view = getLayoutInflater().inflate(R.layout.find_password, null);
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		pop.setOutsideTouchable(false);
		pop.setBackgroundDrawable(new ColorDrawable());
		pop.showAtLocation(contentView, Gravity.CENTER, 0, 0);
		email = (EditText) view.findViewById(R.id.email);
		bar = (LoopProgressBar) view.findViewById(R.id.alter_progress_bar);
	}

	/**
	 * 修改密码提交
	 */
	public void alter(View v) {
		String em = email.getText().toString();
		if (em == null || em.equals("")) {
			Toast.makeText(this, "请输入邮箱地址!", Toast.LENGTH_LONG).show();
			;
		} else {
			bar.setColor(getResources().getColor(R.color.green));
			bar.start();
			v.setClickable(false);
			new SubmitAsync().execute(ip + "palmxian/reset.action", "", "", "",
					"", MD5Util.MD5(systemIdentify + em), em);
		}
	}

	/**
	 * 关闭pop窗口
	 */
	public void cancel(View v) {
		isAlter = false;
		pop.dismiss();
	}

	/** 提交注册信息方法 */
	class SubmitAsync extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			return submitMember(params[0], params[1], params[2], params[3],
					params[4], params[5], params[6]);
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				try {
					JSONObject ja = new JSONObject(result);
					String re = ja.getString("result");
					String reason = ja.getString("reason");
					if (isLogin) {
						Log.i("xingwei", "type=注册resaon=" + reason);
					} else {
						Log.i("xingwei", "type=登录resaon=" + reason);
					}
					if (!isAlter) {
						if (re.equals("success")) {
							if (isLogin) {
								startActivity(new Intent(getApplication(),
										LoginRegisterActviity.class).putExtra(
										"name", nameText));
							} else {
								Editor editor = configuration.edit();
								if (isCheck) {
									editor.putString("name", nameText)
											.putString("password", passwordText);
								} else {
									editor.putString("name", "").putString(
											"password", "");
								}
								editor.putBoolean("isLogin", true).commit();
							}
							finish();
						} else {
							Toast.makeText(getApplicationContext(), reason,
									Toast.LENGTH_LONG).show();
							failure();
						}
					} else {
						if (re.equals("success")) {
							pop.dismiss();
							isAlter = false;
							if (bar != null)
								bar.stopBar();
							Toast.makeText(getApplicationContext(),
									getResources().getString(R.string.win),
									Toast.LENGTH_SHORT).show();
						} else {
							isAlter = false;
							if (bar != null)
								bar.stopBar();
							Toast.makeText(getApplicationContext(),
									getResources().getString(R.string.defeat),
									Toast.LENGTH_SHORT).show();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "数据解析异常",
							Toast.LENGTH_LONG).show();
					failure();
				}
			}
		}

	}

	public String submitMember(String url, String account, String password,
			String IMEI, String IMSI, String token, String email) {
		String line = null;
		/*
		 * 注册成功会返回如下格式JSON字符串
		 * {"result":"success","reason":"注册成功!","additional":""}
		 * 注册失败会返回如下格式JSON字符串
		 * {"result":"fail","reason":"XXXXXX","additional":""}
		 * {"type":"fail","info":"未知账号错误."}登录失败
		 */
		// String account = "张三是张三21";//至少4个字符
		// String password = "123456";//至少6个字符
		// String IMEI = "123123123";//从手机取得
		// String IMSI = "sdfsfsfsf";//从手机取得
		// String token = MD5Util.MD5(systemIdentify + account);//必需的
		// String email = "haizhengstar@126.com";//邮箱格式需要正确
		// System.out.println("-----------------");
		// System.out.println(account);
		// System.out.println(password);
		// System.out.println(token);
		// if (isLogin) {
		// System.out.println(IMEI);
		// System.out.println(IMSI);
		// System.out.println(email);
		// }
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// HttpClient httpclient = AndroidHttpClient.newInstance("");
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
		HttpPost httpPost = new HttpPost(url);
		// HttpPost httpPost = new HttpPost(
		// "http://192.168.1.14:8888/palmcity/reg.action");
		// 以上是注册提交的地址

		MultipartEntity reqEntity = new MultipartEntity();
		try {
			if (!isAlter) {
				reqEntity.addPart("account",
						new StringBody(account, Charset.forName("UTF-8")));
				reqEntity.addPart("password",
						new StringBody(password, Charset.forName("UTF-8")));
				reqEntity.addPart("token",
						new StringBody(token, Charset.forName("UTF-8")));
				if (isLogin) {
					reqEntity.addPart("IMEI",
							new StringBody(IMEI, Charset.forName("UTF-8")));
					reqEntity.addPart("IMSI",
							new StringBody(IMSI, Charset.forName("UTF-8")));
					reqEntity.addPart("email",
							new StringBody(email, Charset.forName("UTF-8")));
				}
			} else {
				reqEntity.addPart("token",
						new StringBody(token, Charset.forName("UTF-8")));
				reqEntity.addPart("email",
						new StringBody(email, Charset.forName("UTF-8")));
			}
			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine());
			if (entity != null) {
				System.out.println("Response content length: "
						+ entity.getContentLength());
			}
			// 显示结果
			InputStreamReader is = new InputStreamReader(entity.getContent(),
					"utf-8");
			BufferedReader reader = new BufferedReader(is);
			while ((line = reader.readLine()) != null) {
				System.out.println("line---->" + line);
				return line;
			}
			if (is != null)
				is.close();

			if (reader != null)
				reader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			JSONObject json = new JSONObject();
			try {
				json.put("result", "fail");
				json.put("reason", "编码错误");
				line = json.toString();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			JSONObject json = new JSONObject();
			try {
				json.put("result", "fail");
				json.put("reason", "连接异常");
				line = json.toString();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject json = new JSONObject();
			try {
				json.put("result", "fail");
				json.put("reason", "网络访问出错");
				line = json.toString();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return line;
	}
}
