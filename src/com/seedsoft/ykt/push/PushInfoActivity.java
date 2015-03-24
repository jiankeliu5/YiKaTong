package com.seedsoft.ykt.push;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.Util;

public class PushInfoActivity extends Activity {
	private boolean open_flag;
	private WebView webView;
	private Handler handler = new Handler();
	private ProgressDialog pd;
	
	private String pre_title;
	private String cur_title;
	private TextView pre_tv;// 上层标题
	private TextView title_tv;// 本层标题
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        View view =  getLayoutInflater().inflate(R.layout.push_content, null);
        
        pre_tv = (TextView) view.findViewById(R.id.login);
		title_tv = (TextView) view.findViewById(R.id.top_title);
		Drawable drawable = getResources().getDrawable(R.drawable.pre_btn);
		int sysVersion = Integer.parseInt(VERSION.SDK);
		   if (sysVersion < 14) {
		    drawable.setBounds(0, 0, 30,30);
		    pre_tv.setCompoundDrawables(drawable,null,null, null);
		   } else {
		    pre_tv.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable,null, null, null);
		   }
		pre_tv.setVisibility(View.VISIBLE);
		
//		pre_title = getIntent().getStringExtra("PRE_TITLE");
//		cur_title = getIntent().getStringExtra("CUR_TITLE");
		pre_tv.setText("一卡通");
		title_tv.setText("推送信息");
		
        
        TextView titletv = (TextView) view.findViewById(R.id.push_content_title);
        TextView tv = (TextView) view.findViewById(R.id.push_content_tv);   
        LinearLayout info = (LinearLayout) view.findViewById(R.id.layout_info);
        LinearLayout netpage = (LinearLayout) view.findViewById(R.id.layout_netpage);
        webView = (WebView) view.findViewById(R.id.webView_push);
        
        //获取应用是否已经打开的标志
        open_flag = getSharedPreferences(Constants.SP_SAVE_NAME, MODE_PRIVATE).getBoolean("open_flag", true);
        
      
        
        //获取推送来的intent
        Intent intent = getIntent();
        if (null != intent) {
	        Bundle bundle = getIntent().getExtras();
	        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
	        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
	        
	        if(title != null && content != null){
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		        Date date = new Date(System.currentTimeMillis());
		        String time = sdf.format(date);
		        //保存推送数据到本地
		        String fileName = Util.getSDCardPath(this)+Constants.DOWNLOAD_PATH + Constants.separator + Constants.PUSH_FILE;
		        String fileContent = title +"<@@>"+time+"<@@>"+content+"<%%>";
		        Util.saveFile(fileName, fileContent);
	        }else{
	        	title = intent.getStringExtra("msg_title");
	        	content = intent.getStringExtra("msg_content");
	        }
	        
	        if(content.endsWith("###")){
	        	info.setVisibility(View.GONE);
	        	String url = content.split("###")[1];	        	
	        	if(url.startsWith("/")) url = Constants.SERVER_URL+url;
	        	
	        	pd = new ProgressDialog(this);
	    		pd.setMessage("正在加载...");
	    		pd.setCancelable(true);//设置进度条是否可以按回退键取消
	    		pd.setCanceledOnTouchOutside(false);//设置进度条是否可以点击其他地方取消
	    		pd.setOnCancelListener(new OnCancelListener() {
	    			
	    			@Override
	    			public void onCancel(DialogInterface dialog) {
	    				// TODO Auto-generated method stub
	    				dialog.dismiss();
	    				Toast.makeText(PushInfoActivity.this, "网速不给力哦！", Toast.LENGTH_LONG).show();
	    			}
	    		});
	    		pd.show();
	    		
	    		webView.addJavascriptInterface(new InJavaScript(), "injs");
	        	webView.getSettings().setBlockNetworkImage(true);
			 	webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);								
				webView.setWebViewClient(new MyWebviewCient());								
				webView.setWebChromeClient(new WebChromeClient());
				webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
				webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
				webView.setHorizontalScrollBarEnabled(false);
				webView.setVerticalScrollBarEnabled(false);					
				final String USER_AGENT_STRING = webView.getSettings().getUserAgentString() + " Rong/2.0";
				webView.getSettings().setUserAgentString( USER_AGENT_STRING );
				webView.getSettings().setBuiltInZoomControls(true);
				webView.getSettings().setPluginState(WebSettings.PluginState.ON);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.loadUrl(url);
	        	
	        }else{
	        	netpage.setVisibility(View.GONE);
	        	
	        	titletv.setSingleLine(true);
	            titletv.setEllipsize(TextUtils.TruncateAt.END);
	            titletv.setTextColor(getResources().getColor(R.color.lightgray));
	            titletv.setTextSize(20);
	            titletv.setBackgroundColor(getResources().getColor(R.color.web_view));
	              
	            tv.setTextColor(Color.BLACK);
	            tv.setTextSize(16);
	            tv.setBackgroundColor(getResources().getColor(R.color.web_view));
	            
	        	titletv.setText(title);
		        tv.setText(content);
	        }
	        
        }
        addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    public void addJavaScriptMap(Object obj, String objName){
		webView.addJavascriptInterface(obj, objName);
	}
	
	public class MyWebviewCient extends WebViewClient{
		@SuppressLint("NewApi")
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			WebResourceResponse response = null;
			response = super.shouldInterceptRequest(view, url);
			return response;
		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			
			webView.loadUrl(url);
			return true;
		}
		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			
			super.onLoadResource(view, url);
		}
		@Override
		public void onPageFinished(WebView view, String url) {  
                Log.i("===", "Finished loading URL: " +url); 
                pd.dismiss();
                webView.getSettings().setBlockNetworkImage(false);
        }  
		@Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
                Log.e("===", "Error: " + description.substring(0, description.length()-1));  
                Toast.makeText(PushInfoActivity.this, description.substring(0, description.length()-1), Toast.LENGTH_SHORT).show();  
                pd.dismiss();
                webView.loadUrl("file:///android_asset/error_page/error.htm");	                
            }  
	
	}	
	
	//在安卓端写的javascript方法
	final class InJavaScript {
		@JavascriptInterface//android 4.2以后加入的安全使用js,注入验证     
        public void openVeidoPlayer(final String url){
			if(url == null || url.equals(""))return;
        	handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(Constants.SERVER_URL+url), "video/*");
					startActivity(intent);			
				}
			});
        }
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	JPushInterface.onResume(this);
    	if(webView != null){
			webView.onResume();
		}
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	JPushInterface.onPause(this);
    	// 此处处理暂停视频播放
    				if (webView != null) {
    					webView.onPause();
    				}
    }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(open_flag){
			super.onBackPressed();
		}else{
			startActivity(new Intent(PushInfoActivity.this, com.seedsoft.ykt.activity.WelcomeActivity.class));
			PushInfoActivity.this.finish();
		}
	}
	
	public void login(View v) {
		onBackPressed();
	}
	
}