package com.seedsoft.ykt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.activity.WebViewAcivity;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.Util;
import com.seedsoft.ykt.widget.ProgressDialogExtend;

@SuppressLint({ "ValidFragment", "JavascriptInterface" })
public class WebviewFragment extends ComMainFragment {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "WebviewFragment";
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
	private LinearLayout  location;
	private ImageView  lastiv;
	private TextView  back_tv;
	private TextView  comment_tv;
	private TextView  share_tv;
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
	
	
	public WebviewFragment(){}
	public WebviewFragment(String news_url,String news_title,String news_id,
			String comment_url,String news_isAllowComment,String news_location,
			String adTitleUrl,String adImageUrl,String adContentUrl ){
		this.news_id = news_id;
		this.news_title = news_title;
		this.news_url = news_url;
		this.comment_url = comment_url;
		this.news_location = news_location;
		this.news_isAllowComment = news_isAllowComment;
		this.adTitleUrl = adTitleUrl;
		this.adImageUrl = adImageUrl;
		this.adContentUrl = adContentUrl;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.com_webview,null);
		webView=(WebView) view.findViewById(R.id.webView);
		comment = (LinearLayout) view.findViewById(R.id.comment_bottom);
		share = (LinearLayout) view.findViewById(R.id.share_bottom);
		location = (LinearLayout) view.findViewById(R.id.loc_bottom);
		lastiv = (ImageView) view.findViewById(R.id.last_divider);
		back = (LinearLayout) view.findViewById(R.id.back_bottom);
		back_tv = (TextView) view.findViewById(R.id.back_bottom_textView);
		comment_tv = (TextView) view.findViewById(R.id.comment_bottom_textView);
		share_tv = (TextView) view.findViewById(R.id.share_bottom_textView);
		root_layout = (RelativeLayout) view.findViewById(R.id.root_layout);
		child_layout = (LinearLayout) view.findViewById(R.id.child_layout);
		adClose = (ImageView) view.findViewById(R.id.ad_close_icon);
		adImage = (ImageView) view.findViewById(R.id.ad_icon);
		adTitle = (TextView) view.findViewById(R.id.ad_textView);
		adLayout = (RelativeLayout) view.findViewById(R.id.ad_layout);
		
		
		if(adTitleUrl == null || adTitleUrl.equals("")){
			adLayout.setVisibility(View.GONE);
		}else
			if(adImageUrl == null || adImageUrl.equals("") || adImageUrl.equals(Constants.SERVER_URL)){
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
				startActivity(new Intent(getActivity(),WebViewAcivity.class)							
						.putExtra("URL", adContentUrl));
				getActivity().overridePendingTransition(R.anim.hold_enter, R.anim.fade_out);
			}
		});
		
		pd = ProgressDialogExtend.CreateSiglePDE(getActivity(),null);
		pd.show();
//		pd.setMessage("正在加载...");
//		pd.setCancelable(true);//设置进度条是否可以按回退键取消
//		pd.setCanceledOnTouchOutside(false);//设置进度条是否可以点击其他地方取消
//		pd.show();
//		pd.setOnCancelListener(new OnCancelListener() {
//			
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//				Toast toast = Toast.makeText(getActivity(), "网速不给力哦!", Toast.LENGTH_LONG);
//				toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
//				toast.getView().setPadding(20, 10, 20, 10);
//				toast.show();
////				Toast.makeText(getActivity(), "网速不给力哦！", Toast.LENGTH_LONG).show();
//			}
//		});
		
		System.out.println(TAG+"-news_url-->"+news_url);
		if(news_url==null)news_url="file:///android_asset/2.html";//显示错误页面
		if(news_isAllowComment == null || news_isAllowComment.equalsIgnoreCase("n")){
			comment_tv.setTextColor(Color.argb(33, 33, 33, 33));
			comment.setClickable(false);
		}
		if(news_location == null || news_location.trim().equals("")){
			location.setVisibility(View.GONE);
			lastiv.setVisibility(View.GONE);
		}else{
			
			location.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					startActivity(new Intent(getActivity(),MapActivity.class).putExtra("xy", news_location));
				}
			});
		}
		
		back_tv.setTextColor(Color.argb(33, 33, 33, 33));
//		 back.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				getActivity().onBackPressed();
//				getActivity().finish();
//			}
//		});

		 comment.setOnClickListener(new OnClickListener() {
			 
			 @Override
			 public void onClick(View v) {
				 // TODO Auto-generated method stub
//				startActivity(new Intent(getActivity(),CommentActivity.class)
//				.putExtra("title", news_title)
//				.putExtra("id", news_id)
//				.putExtra("comment_url", comment_url));
//				getActivity().overridePendingTransition(R.anim.hold_enter, R.anim.fade_out);
					
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
			      startActivity(Intent.createChooser(intent, getActivity().getTitle()));
			 }
		 });
		 
//		 save.setOnClickListener(new OnClickListener() {
//			 
//			 @Override
//			 public void onClick(View v) {
//				 // TODO Auto-generated method stub
//				 Toast.makeText(getActivity(), "收藏成功！", Toast.LENGTH_LONG).show();
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
			
//			 webView.loadUrl("http://192.168.1.71:8080/bb.html");
			 webView.loadUrl(news_url);
			
			if(savedInstanceState != null){
				webView.restoreState(savedInstanceState);
			}
			
			return view;
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
//		
//		
//		@Override
//		protected void onSaveInstanceState(Bundle outState) {
//			// TODO Auto-generated method stub
//			webView.saveState(outState);
//		}
		
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
			public void onPageFinished(WebView view, String url) {  
	                Log.i("===", "Finished loading URL: " +url); 
	                pd.dialog.dismiss();
	                webView.getSettings().setBlockNetworkImage(false);
	                
	        }  
			@Override
	        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
	                Log.e("===", "Error: " + description.substring(0, description.length()-1));  
//	                Toast.makeText(getActivity(), description.substring(0, description.length()-1), Toast.LENGTH_SHORT).show(); 
	                Toast toast = Toast.makeText(getActivity(), description.substring(0, description.length()-1), Toast.LENGTH_LONG);
					//toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
					toast.getView().setPadding(20, 10, 20, 10);
					toast.show();
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
//			public void onShowCustomView(View view, CustomViewCallback callback) {
//				if(myView != null){
//					callback.onCustomViewHidden();
//					return;
//				}
//				root_layout.removeView(child_layout);
//				root_layout.addView(view);
//				myView = view;
//				myCallBack = callback;
//			}
//			
//			@Override
//			public void onHideCustomView() {
//				if(myView == null){
//					return;
//				}
//				
//				root_layout.removeView(myView);
//				myView = null;
//				root_layout.addView(child_layout);
//				myCallBack.onCustomViewHidden();
//			}
			
//			@Override
//			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//				// TODO Auto-generated method stub
//				Log.d("ZR", consoleMessage.message()+" at "+consoleMessage.sourceId()+":"+consoleMessage.lineNumber());
//				return super.onConsoleMessage(consoleMessage);
//			}
		}
		
		//在安卓端写的javascript方法
				final class InJavaScript {
					@JavascriptInterface
			        public void runOnAndroidJavaScript() {
			        	handler.post(new Runnable() {
			                public void run() { 
//			                	int curMode = new SkinChangeManager(getActivity()).getSkinType();
//			                	
//			                	if(curMode == 0){
////			                		webView.loadUrl("javascript:dayMode()");	                		
//			                		webView.loadUrl("javascript:changeMode('1')");	                		
//			                	}else{
////			                		webView.loadUrl("javascript:nightMode()");                		
//			                		webView.loadUrl("javascript:changeMode('0')");                		
//			                	}
			                
			                }
			            });
			        }
			        @JavascriptInterface
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
		 
		 
		 
//		webView.getSettings().setBlockNetworkImage(true);
//		webView.loadUrl(news_url);
////		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setBuiltInZoomControls(false);
//		
//		// 设置支持Javascript
//				webView.getSettings().setJavaScriptEnabled(true);
//				
//				webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//				webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//
//				webView.getSettings().setPluginsEnabled(true);
//
//				//设置WebViewClient
//				webView.setWebViewClient(new WebViewClient(){   
//				    public boolean shouldOverrideUrlLoading(WebView view, String url) {   
//				        view.loadUrl(url);   
//				        return true;   
//				    }  
//					public void onPageFinished(WebView view, String url) {
//						super.onPageFinished(view, url);
//					}
//					public void onPageStarted(WebView view, String url, Bitmap favicon) {
//						super.onPageStarted(view, url, favicon);
//					}
//				});
//
//				//设置WebChromeClient
//				webView.setWebChromeClient(new WebChromeClient() {
//					// 处理javascript中的alert
//					public boolean onJsAlert(WebView view, String url, String message,
//							final JsResult result) {
//						// 构建一个Builder来显示网页中的对话框
//						Builder builder = new Builder(getActivity());
//						builder.setTitle("Alert");
//						builder.setMessage(message);
//						builder.setPositiveButton(android.R.string.ok,
//								new AlertDialog.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int which) {
//										// 点击确定按钮之后,继续执行网页中的操作
//										result.confirm();
//									}
//								});
//						builder.setCancelable(false);
//						builder.create();
//						builder.show();
//						return true;
//					};
//
//					// 处理javascript中的confirm
//					public boolean onJsConfirm(WebView view, String url,
//							String message, final JsResult result) {
//						Builder builder = new Builder(getActivity());
//						builder.setTitle("confirm");
//						builder.setMessage(message);
//						builder.setPositiveButton(android.R.string.ok,
//								new AlertDialog.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int which) {
//										result.confirm();
//									}
//								});
//						builder.setNegativeButton(android.R.string.cancel,
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int which) {
//										result.cancel();
//									}
//								});
//						builder.setCancelable(false);
//						builder.create();
//						builder.show();
//						return true;
//					};
//
//					// 设置应用程序的标题title
//					public void onReceivedTitle(WebView view, String title) {
//						getActivity().setTitle(title);
//						super.onReceivedTitle(view, title);
//					}
//
//					@Override
//					public void onShowCustomView(View view, CustomViewCallback callback) {
//						if (myCallback != null) {
//							myCallback.onCustomViewHidden();
//							myCallback = null;
//							return;
//						}
//
//						ViewGroup parent = (ViewGroup) webView.getParent();
//						parent.removeView(webView);
//						parent.addView(view);
//						myView = view;
//						myCallback = callback;
//						// chromeClient = this ;
//					}
//
//					private View myView = null;
//					private CustomViewCallback myCallback = null;
//
//					public void onHideCustomView() {
//
//						if (myView != null) {
//
//							if (myCallback != null) {
//								myCallback.onCustomViewHidden();
//								myCallback = null;
//							}
//
//							ViewGroup parent = (ViewGroup) myView.getParent();
//							parent.removeView(myView);
//							parent.addView(webView);
//							myView = null;
//						}
//					}
//				});

		
//		webView.setWebChromeClient(new WebChromeClient(){
//			@Override
//			public void onProgressChanged(WebView view, int newProgress) {
//				// TODO Auto-generated method stub
//				super.onProgressChanged(view, newProgress);
//			}
//		});
//		webView.setWebViewClient(new WebViewClient(){
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				// TODO Auto-generated method stub
//				webView.loadUrl(url);
//				return true;
//			}
//			
//			 public void onPageFinished(WebView view, String url) {  
//	                Log.i("===", "Finished loading URL: " +url);   
//	                webView.getSettings().setBlockNetworkImage(false);
//	            }  
//	   
//	            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
//	                Log.e("===", "Error: " + description.substring(0, description.length()-1));  
//	                Toast.makeText(WebViewAcivity.this, description.substring(0, description.length()-1), Toast.LENGTH_SHORT).show();  
//	                new AlertDialog.Builder(WebViewAcivity.this)
//	                .setTitle("网络故障")  
//	                .setMessage(description) 
//	                .setPositiveButton("OK", new DialogInterface.OnClickListener() {  
//	                    public void onClick(DialogInterface dialog, int which) {  
//	                    	dialog.dismiss();  
//	                    }  
//	                }).show();  
//	            }  
//		});
//		return view;
//	}
//	
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
				
				
//				public static Bitmap getHttpBitmap(String url) {
//				     URL myFileUrl = null;
//				     Bitmap bitmap = null;
//				     try {
//				          Log.d(TAG, url);
//				          myFileUrl = new URL(url);
//				     } catch (MalformedURLException e) {
//				          e.printStackTrace();
//				     }
//				     try {
//				          HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
//				          conn.setConnectTimeout(0);
//				          conn.setDoInput(true);
//				          conn.connect();
//				          InputStream is = conn.getInputStream();
//				          bitmap = BitmapFactory.decodeStream(is);
//				          is.close();
//				     } catch (IOException e) {
//				          e.printStackTrace();
//				     }
//				     return bitmap;
//				}
	
}