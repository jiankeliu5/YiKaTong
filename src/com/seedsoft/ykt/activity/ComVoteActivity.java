package com.seedsoft.ykt.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.adpter.ComVoteAdapter;
import com.seedsoft.ykt.bean.VoteBean;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.ParseXMLUtil;
import com.seedsoft.ykt.widget.ProgressDialogExtend;
import com.seedsoft.ykt.widget.XListView;
import com.seedsoft.ykt.widget.XListView.IXListViewListener;

public class ComVoteActivity extends FragmentActivity implements IXListViewListener {

		private static String TAG = "ComVoteActivity";

		private ArrayList<VoteBean> lsnb = null;
		private SimpleDateFormat sdf = null;
		private Date curDate = null;// 当前时间
		private long time = 0;
		private XListView listView;// 新闻内容列表视图
		private String URL;

		private ComVoteAdapter comVoteAdapter = null;
		private ProgressDialogExtend pd;
		
		private String pre_title;
		private String cur_title;
		private TextView pre_tv;// 上层标题
		private TextView title_tv;// 本层标题	
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);					
			setContentView(R.layout.a_vote_list);
			URL = getIntent().getStringExtra("URL");
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
			
			pre_title = getIntent().getStringExtra("PRE_TITLE");
			cur_title = getIntent().getStringExtra("CUR_TITLE");
			pre_tv.setText(pre_title);
			title_tv.setText(cur_title);
			
			listView = (XListView)findViewById(R.id.vote_list);
			listView.setPullLoadEnable(false);
			listView.setXListViewListener(this);			
			new AsyncLoadVotes().execute(URL);
		}
		public void login(View v) {
			onBackPressed();
		}
		
		
		class AsyncLoadVotes extends AsyncTask<String, Void, ArrayList<VoteBean>> {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pd = ProgressDialogExtend.CreateSiglePDE(ComVoteActivity.this, null);
				pd.show();
			}

			@Override
			protected ArrayList<VoteBean> doInBackground(String... params) {
				// TODO Auto-generated method stub
				synchronized (this) {
					return new ParseXMLUtil().parseVoteSurvey(params[0]);
				}
			}

			@Override
			protected void onPostExecute(ArrayList<VoteBean> result) {
				// TODO Auto-generated method stub
				try {
					pd.dismiss();
					if (result != null && result.size() != 0) {
						lsnb = result;						

						if (comVoteAdapter != null) {
							comVoteAdapter.notifyDataSetChanged();
							listView.requestLayout();
							onLoadStop();
							// listView.setSelection(lsnb.size()-pageMax);//设置当前第一行显示的数据
						} else {

							comVoteAdapter = new ComVoteAdapter(ComVoteActivity.this,lsnb);

							// 此处可取当前时间，为第一次刷新时间
							time = System.currentTimeMillis();
							curDate = new Date(time);
							sdf = new SimpleDateFormat("MM-dd HH:mm");
							onLoadStop();

							listView.setAdapter(comVoteAdapter);
							listView.requestLayout();
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public synchronized void onItemClick(
										AdapterView<?> arg0, View arg1, int arg2,
										long arg3) {
									// 感谢上帝在此处帮助了我
									--arg2;
									// 打开文章内容
									String link = Constants.SERVER_URL
											+ lsnb.get(arg2).getUrl();
									String title = lsnb.get(arg2).getName();

									if (BuildConfig.DEBUG) {
										Log.d(TAG, "title:" + title);
										Log.d(TAG, "link:" + link);
									}
									try {
										startActivity(new Intent(
												ComVoteActivity.this,
												WebViewActivity.getInstance()
														.getClass())
												.putExtra("PRE_TITLE", cur_title)
												.putExtra("URL", link)
												.putExtra("CUR_TITLE", title));
										overridePendingTransition(
												R.anim.hold_enter, R.anim.fade_out);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										return;
									}
								}
							});
						}
					} else {
						onLoadStop();
						Toast toast = Toast.makeText(ComVoteActivity.this, "网速不给力哦!",
								Toast.LENGTH_LONG);
						// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
						toast.getView().setPadding(20, 10, 20, 10);
						toast.show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onResume() {
			super.onResume();
			if (comVoteAdapter != null) {
				comVoteAdapter.notifyDataSetChanged();
			}
			
		}

		@Override
		public void onPause() {
			super.onPause();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
		public synchronized void onRefresh() {
			// TODO Auto-generated method stub
			try {
				// 此处可取当前时间，为刷新时间
				curDate = new Date(System.currentTimeMillis());
				sdf = new SimpleDateFormat("MM-dd HH:mm");
				if (System.currentTimeMillis() - time > 500) {
					
					if (lsnb != null) {
						lsnb = null;
					}

					new AsyncLoadVotes().execute(URL);
				} else {
					Toast toast = Toast.makeText(this, "目前是最新数据!",
							Toast.LENGTH_LONG);
					// toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
					toast.getView().setPadding(20, 10, 20, 10);
					toast.show();
					onLoadStop();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public synchronized void onLoadMore() {
			// TODO Auto-generated method stub
		}

		private void onLoadStop() {
			listView.stopRefresh();
			listView.stopLoadMore();
			if (curDate != null) {
				listView.setRefreshTime(sdf.format(curDate));
			}

		}

	

}
