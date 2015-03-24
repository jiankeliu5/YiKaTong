package com.seedsoft.ykt.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.widget.ProgressDialogExtend;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity{
	private static final String TAG = "WebViewActivity";
	private static WebViewActivity wva = new WebViewActivity();
	public static WebViewActivity getInstance(){		
			
		return wva;
	}
	private Intent intent;
	private ProgressDialogExtend pd;
	private String news_url;
	private String news_title;
	private String news_id;
	private String news_location;
	private WebView webView;
	
	private TextView pre_tv;// 上层标题
	private TextView title_tv;// 本层标题
	
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.com_webview);
		
		pre_tv = (TextView) findViewById(R.id.login);
		title_tv = (TextView) findViewById(R.id.top_title);
		Drawable drawable = getResources().getDrawable(R.drawable.pre_btn);
		int sysVersion = Integer.parseInt(VERSION.SDK);
		   if (sysVersion < 14) {
		    drawable.setBounds(0, 0, 30,30);
		    pre_tv.setCompoundDrawables(drawable,null,null, null);
		   } else {
		    pre_tv.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable,null, null, null);
		   }
		pre_tv.setVisibility(View.VISIBLE);
		

		pre_tv.setText(getIntent().getStringExtra("PRE_TITLE"));
		title_tv.setText(getIntent().getStringExtra("CUR_TITLE"));
		
		webView=(WebView) this.findViewById(R.id.webView);				
		pd = ProgressDialogExtend.CreateSiglePDE(this,null);
		pd.show();
		intent = getIntent();
//		news_title = intent.getStringExtra("title");
		news_id = intent.getStringExtra("id");
		news_url = intent.getStringExtra("URL");
		news_location = intent.getStringExtra("location");
		if (BuildConfig.DEBUG) {
			Log.d(TAG,"-news_url-->"+news_url );
		}
		if(news_url==null)news_url="file:///android_asset/error_page/error.htm";
		if(news_location != null && !news_location.trim().equals("")){
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "-news_location-->"+news_location);
			}
		}
		 	//设置webview参数	
			handler = new Handler();
			webView.addJavascriptInterface(new GoBack(), "goback");
			webView.setWebViewClient(new MyWebviewCient());	
			webView.getSettings().setJavaScriptEnabled(true);
//			webView.loadUrl("http://192.168.1.14:8888/palmxian/public/exame/16.html");
			webView.loadUrl(news_url);
			
		}	
		public void login(View v) {
			onBackPressed();
		}
		
		public class MyWebviewCient extends WebViewClient{		
		
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
				view.loadUrl(url);
			return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {  
	                Log.i("===", "Finished loading URL: " +url); 
	                pd.dismiss();//dialog.dismiss();
	        }  
			@Override
	        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
	                Log.e("===", "Error: " + description.substring(0, description.length()-1));  
	                Toast toast = Toast.makeText(WebViewActivity.this, description.substring(0, description.length()-1), Toast.LENGTH_LONG);
//					toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
					toast.getView().setPadding(20, 10, 20, 10);
					toast.show();
	                pd.dismiss();
	                webView.loadUrl("file:///android_asset/error_page/error.htm");	                
	            }  
		
		}
		
		//js返回事件
		final class GoBack {
			@JavascriptInterface//android 4.2以后加入的安全使用js,注入验证
	        public void doGoBack() {
	        	handler.post(new Runnable() {
	                public void run() { 
	                	onBackPressed();	                
	                }
	            });
	        }	        
	    }
			
		
}
