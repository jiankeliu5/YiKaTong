package com.seedsoft.ykt.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.Util;
import com.seedsoft.ykt.widget.ProgressDialogExtend;

@SuppressLint("JavascriptInterface")
public class WebViewAcivity extends Activity{
	private static final String TAG = "WebViewAcivity";
	private static WebViewAcivity wva = new WebViewAcivity();
	public static WebViewAcivity getInstance(){		
			
		return wva;
	}
	private Handler handler = new Handler();
	private ProgressDialogExtend pd;
	private String news_url;
	private String news_title;
	private String news_id;
	private String comment_url;
	private String news_location;
	private String news_isAllowComment;
	private WebView webView;
	private LinearLayout  back;
	private LinearLayout  comment;
	private LinearLayout  share;
	private TextView  back_tv;
	private TextView  comment_tv;
	private TextView  share_tv;
	private LinearLayout  location;
	private ImageView  lastiv;
	private RelativeLayout  root_layout;
	private LinearLayout  child_layout;
	private WebChromeClient chromeClient = null;
	private View myView = null;
	private WebChromeClient.CustomViewCallback myCallBack = null;
	
	//广告部分
	private ImageView adClose;
	private ImageView adImage;
	private TextView adTitle;
	private String adImageUrl;
	private String adTitleUrl;
	private String adContentUrl;
	private RelativeLayout adLayout;
	
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.com_webview);
		webView=(WebView) this.findViewById(R.id.webView);
		comment = (LinearLayout) this.findViewById(R.id.comment_bottom);
		share = (LinearLayout) this.findViewById(R.id.share_bottom);
		location = (LinearLayout) this.findViewById(R.id.loc_bottom);
		back = (LinearLayout) this.findViewById(R.id.back_bottom);
		back_tv = (TextView) this.findViewById(R.id.back_bottom_textView);
		comment_tv = (TextView) this.findViewById(R.id.comment_bottom_textView);
		share_tv = (TextView) this.findViewById(R.id.share_bottom_textView);
		root_layout = (RelativeLayout) this.findViewById(R.id.root_layout);
		child_layout = (LinearLayout) this.findViewById(R.id.child_layout);
		lastiv = (ImageView) this.findViewById(R.id.last_divider);
		adClose = (ImageView) this.findViewById(R.id.ad_close_icon);
		adImage = (ImageView) this.findViewById(R.id.ad_icon);
		adTitle = (TextView) this.findViewById(R.id.ad_textView);
		adLayout = (RelativeLayout) this.findViewById(R.id.ad_layout);
		
		adTitleUrl = getIntent().getStringExtra("AD_TITLE");
		adImageUrl = getIntent().getStringExtra("AD_IMAGE");
		adContentUrl = getIntent().getStringExtra("AD_URL");
		if(adTitleUrl == null || adTitleUrl.equals("")){
			adLayout.setVisibility(View.GONE);
		}else
			if(adImageUrl == null || adImageUrl.trim().equals("") || adImageUrl.equals(Constants.SERVER_URL)){
				adImage.setVisibility(View.GONE);
			}else{
				System.out.println("==webview=adImageUrl="+adImageUrl);
				new AsyncTask<String, Void, Bitmap>() {

					@Override
					protected Bitmap doInBackground(String... params) {
						// TODO Auto-generated method stub
						
						return Util.getHttpBitmap(params[0]);
					}
					@Override
					protected void onPostExecute(Bitmap result) {
						// TODO Auto-generated method stub
						if(result != null){
							adImage.setImageBitmap(result);
						}else{
							adImage.setVisibility(View.GONE);
						}
						
					}
				}.execute(adImageUrl);
			}
		adClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adLayout.setVisibility(View.GONE);
			}
		});
		adTitle.setText(adTitleUrl);
		adLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(WebViewAcivity.this,WebViewAcivity.class)							
						.putExtra("URL", adContentUrl));
				overridePendingTransition(R.anim.hold_enter, R.anim.fade_out);
			}
		});
		
		
		pd = ProgressDialogExtend.CreateSiglePDE(this,null);
		pd.show();
////		pd.setMessage("正在加载...");
//		pd.setCancelable(true);//设置进度条是否可以按回退键取消
//		pd.setCanceledOnTouchOutside(false);//设置进度条是否可以点击其他地方取消
//		pd.setOnCancelListener(new OnCancelListener() {
//			
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//				Toast toast = Toast.makeText(WebViewAcivity.this, "网速不给力哦!", Toast.LENGTH_LONG);
//				toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
//				toast.getView().setPadding(20, 10, 20, 10);
//				toast.show();
////				Toast.makeText(WebViewAcivity.this, "网速不给力哦！", Toast.LENGTH_LONG).show();
//			}
//		});
//		pd.show();
		
		news_title = getIntent().getStringExtra("title");
		news_id = getIntent().getStringExtra("id");
		comment_url = getIntent().getStringExtra("comment_url");
		news_url = getIntent().getStringExtra("URL");
		news_location = getIntent().getStringExtra("location");
		news_isAllowComment = getIntent().getStringExtra("isAllowComment");
		System.out.println(TAG+"-news_url-->"+news_url);
		System.out.println(TAG+"-news_location-->"+news_location);
		if(news_url==null)news_url="file:///android_asset/error_page/error.htm";
		if(news_location == null || news_location.trim().equals("")){
			location.setVisibility(View.GONE);
			lastiv.setVisibility(View.GONE);
		}else{
			
			location.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					startActivity(new Intent(WebViewAcivity.this,MapActivity.class).putExtra("xy", news_location));
				}
			});
		}
		if(news_isAllowComment == null || news_isAllowComment.equalsIgnoreCase("n")){
//			comment.setBackgroundColor(Color.GRAY);
			comment_tv.setTextColor(Color.argb(33, 33, 33, 33));
			comment.setClickable(false);
		}else{
			 comment.setOnClickListener(new OnClickListener() {
				 
				 @Override
				 public void onClick(View v) {
					 // TODO Auto-generated method stub
//					startActivity(new Intent(WebViewAcivity.this,CommentActivity.class)
//					.putExtra("title", news_title)
//					.putExtra("id", news_id)
//					.putExtra("comment_url", comment_url));
//					overridePendingTransition(R.anim.hold_enter, R.anim.fade_out);
						
				 }
			 });
		}
		
		
		 back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});

		
		 
		 share.setOnClickListener(new OnClickListener() {
			 
			 @Override
			 public void onClick(View v) {
				 // TODO Auto-generated method stub
				 Intent intent=new Intent(Intent.ACTION_SEND);	      
			      intent.setType("text/plain");
			      intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			      intent.putExtra(Intent.EXTRA_TEXT, news_url);
			      startActivity(Intent.createChooser(intent, getTitle()));
			 }
		 });
		 
//		 save.setOnClickListener(new OnClickListener() {
//			 
//			 @Override
//			 public void onClick(View v) {
//				 // TODO Auto-generated method stub
//				 Toast.makeText(WebViewAcivity.this, "收藏成功！", Toast.LENGTH_LONG).show();
//			 }
//		 });
		 
		 	/*把本类的一个实例添加到js的全局对象window中，
	         *这样就可以使用window.injs来调用它的方法*/
	        webView.addJavascriptInterface(new InJavaScript(), "injs");
		 	//设置webview参数
		 	webView.getSettings().setBlockNetworkImage(true);
		 	webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			
			
			webView.setWebViewClient(new MyWebviewCient());
			
			chromeClient = new MyChromeClient();
			
			webView.setWebChromeClient(chromeClient);
			webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

			webView.setHorizontalScrollBarEnabled(false);
			webView.setVerticalScrollBarEnabled(false);	
			
			final String USER_AGENT_STRING = webView.getSettings().getUserAgentString() + " Rong/2.0";
			webView.getSettings().setUserAgentString( USER_AGENT_STRING );

			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setPluginState(WebSettings.PluginState.ON);
			webView.getSettings().setLoadWithOverviewMode(true);
			
//			 webView.loadUrl("http://192.168.1.71:8080/cc.html");
			 webView.loadUrl(news_url);
			 
			
			if(savedInstanceState != null){
				webView.restoreState(savedInstanceState);
			}
		}	
		
//		@Override
//		public void onBackPressed() {
//			if(myView == null){
//				super.onBackPressed();
//			}
//			else{
//				chromeClient.onHideCustomView();
//			}
//		}
		
		@Override
		protected void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			webView.saveState(outState);
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
				
				webView.loadUrl(news_url);
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
	                pd.dialog.dismiss();
	                webView.getSettings().setBlockNetworkImage(false);
	                new InJavaScript().runOnAndroidJavaScript();
	        }  
			@Override
	        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
	                Log.e("===", "Error: " + description.substring(0, description.length()-1));  
	                Toast toast = Toast.makeText(WebViewAcivity.this, description.substring(0, description.length()-1), Toast.LENGTH_LONG);
//					toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
					toast.getView().setPadding(20, 10, 20, 10);
					toast.show();
//	                Toast.makeText(WebViewAcivity.this, description.substring(0, description.length()-1), Toast.LENGTH_SHORT).show();  
//	                new AlertDialog.Builder(WebViewAcivity.this)
//	                .setTitle("网络故障")  
//	                .setMessage(description) 
//	                .setPositiveButton("OK", new DialogInterface.OnClickListener() {  
//	                    public void onClick(DialogInterface dialog, int which) {  
//	                    	dialog.dismiss();  
//	                    }  
//	                }).show();  
	                pd.dialog.dismiss();
	                webView.loadUrl("file:///android_asset/error_page/error.htm");	                
	            }  
		
		}
		
		public class MyChromeClient extends WebChromeClient{
			
//			@Override
//			public Bitmap getDefaultVideoPoster() {
//				// TODO Auto-generated method stub
//				
//				return BitmapFactory.decodeResource(getResources(), R.drawable.ico_video);
//			}
			/*
			@Override
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				
				return super.onJsBeforeUnload(view, url, message, result);
			}
			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				if(myView != null){
					callback.onCustomViewHidden();
					return;
				}
				root_layout.removeView(child_layout);
				root_layout.addView(view);
				myView = view;
				myCallBack = callback;
			}
			
			@Override
			public void onHideCustomView() {
				if(myView == null){
					return;
				}
				root_layout.removeView(myView);
				myView = null;
				root_layout.addView(child_layout);
				myCallBack.onCustomViewHidden();
			}
			
			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
				// TODO Auto-generated method stub
				Log.d("ZR", consoleMessage.message()+" at "+consoleMessage.sourceId()+":"+consoleMessage.lineNumber());
				return super.onConsoleMessage(consoleMessage);
			}*/
		}
		
		//在安卓端写的javascript方法
		final class InJavaScript {
			@JavascriptInterface//android 4.2以后加入的安全使用js,注入验证
	        public void runOnAndroidJavaScript() {
	        	handler.post(new Runnable() {
	                public void run() { 
//	                	int curMode = new SkinChangeManager(WebViewAcivity.this).getSkinType();
//	                	
//	                	if(curMode == 0){
////	                		webView.loadUrl("javascript:dayMode()");	                		
//	                		webView.loadUrl("javascript:changeMode('1')");	                		
//	                	}else{
////	                		webView.loadUrl("javascript:nightMode()");                		
//	                		webView.loadUrl("javascript:changeMode('0')");                		
//	                	}
	                
	                }
	            });
	        }
			@JavascriptInterface
	        public void openVeidoPlayer(final String url){
//	        	System.out.println("---vedio--url--"+url);
				if(url == null || url.equals(""))return;
	        	handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println("---");
						Intent intent = new Intent();
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.parse(Constants.SERVER_URL+url), "video/*");
						startActivity(intent);
						System.out.println("====");
						
//						Intent newIntent=new Intent();
//						newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						newIntent.setAction(android.content.Intent.ACTION_VIEW);
//						newIntent.setDataAndType(Uri.parse("http://192.168.1.14:8888/palmcity/upload/content/1407308955703.mp4"),"video/*");
//						newIntent.setDataAndType(Uri.fromFile(new File(path)),"video/*");
//						newIntent.setDataAndType(Uri.fromFile(new File(path)),"audio/*");
//						startActivity(newIntent);
					}
				});
	        }
	    }

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			// 此处处理暂停视频播放
			if (webView != null) {
				webView.onPause();
			}
		}
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if(webView != null){
				webView.onResume();
			}
		}
		
		
		
}
