package com.seedsoft.ykt.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.adpter.ComNewsAdapter;
import com.seedsoft.ykt.bean.CarouselBean;
import com.seedsoft.ykt.bean.FatherModuleBean;
import com.seedsoft.ykt.bean.MulChildrenBean;
import com.seedsoft.ykt.bean.MulListBean;
import com.seedsoft.ykt.bean.NewsBean;
import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.bitmap.util.ImageCache.ImageCacheParams;
import com.seedsoft.ykt.bitmap.util.ImageFetcher;
import com.seedsoft.ykt.util.BaseApplication;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.ParseXMLUtil;
import com.seedsoft.ykt.util.Util;
import com.seedsoft.ykt.widget.ComposerLayout;
import com.seedsoft.ykt.widget.ProgressDialogExtend;
import com.seedsoft.ykt.widget.XListView;
import com.seedsoft.ykt.widget.XListView.IXListViewListener;

public class ComActivity extends FragmentActivity implements IXListViewListener {

		private static String TAG = "ComActivity";
		private ImageFetcher mImageFetcher;
		private View view;// ��ǰ��Ļ��ʾ����ͼ��onCreateView�ķ���ֵ��
		public ArrayList<FatherModuleBean> fatherModuleBeans;// ȫ����ʾ�����ļ�����Դ

		private List<NewsBean> lsnb = null;
		private List<NewsBean> lsnb_temp = null;
		private SimpleDateFormat sdf = null;
		private Date curDate = null;// ��ǰʱ��
		private long time = 0;
		private XListView listView;// ���������б���ͼ
		private String pageNum = null;// ��ҳ��
		private int pageSize = 1;// ��ҳ��
		private int curPage = 1;// ��ǰҳ��
		private int pageMax = 15;// ÿҳ15����Ϣ
		private String URL;

		private ComposerLayout pathBtn;// ����Ŀʱ��ʹ��pathbtn�ؼ���ʾ����Ŀ��Ŀǰ֧�����
		private String[] group;// ����Ŀ�б�,�����������
		private Bitmap[] bmp;// ת��Ϊbitmap�������������
		private Map<String, String> child = null;// key:���,value:URL��ַ
		private List<MulChildrenBean> mulChildrenBeans;

		private ComNewsAdapter comNewsAdapter = null;
		private ProgressDialogExtend pd;
		
		private String pre_title;
		private String cur_title;
		private TextView pre_tv;// �ϲ����
		private TextView title_tv;// �������
		
		private RootBean rootBean;

		@Override
		public void onLowMemory() {
			// TODO Auto-generated method stub
			if (mImageFetcher != null) {
				mImageFetcher.setPauseWork(true);
				mImageFetcher.setExitTasksEarly(true);
				mImageFetcher.flushCache();
				mImageFetcher.closeCache();
			}
			super.onLowMemory();
		}

	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
			
			int index = getIntent().getIntExtra("INDEX", 0);
			rootBean = ((BaseApplication)getApplication()).getRootBeans().get(0);
			fatherModuleBeans = rootBean.getFatherBeans().get(index).getFatherModuleBeans();

			ImageCacheParams cacheParams = new ImageCacheParams(this,
					Constants.IMAGE_CACHE_DIR);

			cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
														// app memory

			// The ImageFetcher takes care of loading images into our ImageView
			// children asynchronously
			mImageFetcher = new ImageFetcher(this);
			// mImageFetcher.setLoadingImage(R.drawable.empty_photo);
			mImageFetcher.addImageCache(ComActivity.this.getSupportFragmentManager(),
					cacheParams);
			lsnb_temp = new ArrayList<NewsBean>();
			view = getLayoutInflater().inflate(R.layout.com_main_fragment, null);
			setContentView(view);
			
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
			
			listView = (XListView) view.findViewById(R.id.content_list_lv);
			listView.setVisibility(View.VISIBLE);
			listView.setPullLoadEnable(true);
			listView.setXListViewListener(this);
			listView.setOnScrollListener(new AbsListView.OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
						mImageFetcher.setPauseWork(true);
					} else {
						mImageFetcher.setPauseWork(false);
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

				}
			});

			selectShowOn(fatherModuleBeans);// �ж��Ƿ���ʾ�����ͼ
		}
		public void login(View v) {
			onBackPressed();
		}
		
		

		/**
		 * ���ݵ�ǰ��fragment�Ĺ��캯�������Ĳ���--������ͼ���--�жϵ�ǰfragment��ʾ��Щ��ͼ
		 * */
		private void selectShowOn(ArrayList<FatherModuleBean> fatherModuleBeans) {
			// TODO Auto-generated method stub

			for (int i = 0; i < fatherModuleBeans.size(); i++) {
				FatherModuleBean fb = fatherModuleBeans.get(0);
				Object objects = fb.getObjects();// ��ȡԪ���ݶ���
//				System.out.println("ComActivity--type----" + fb.getType());
				// ��Ϊ�������б�ʱ
				if (fb.getType().equalsIgnoreCase("singletextlist")) {
					CarouselBean cb = (CarouselBean) objects;// ��ȡ����������Դ

					URL = Constants.SERVER_URL + cb.getUrl();// ��ȡ�������б����Ϣ�б�·�������ݸ������࣬����һ���б����
					new AsyncLoadNews(lsnb, URL).execute();
				}

				// ��Ϊ��ͼ���б�ʱ
				if (fb.getType().equalsIgnoreCase("singleimagetextlist")) {
					CarouselBean cb = (CarouselBean) objects;// ��ȡ����������Դ

					URL = Constants.SERVER_URL + cb.getUrl();
					new AsyncLoadNews(lsnb, URL).execute();
				}

				// ��Ϊ����Ŀʱ�������б�
				if (fb.getType().equalsIgnoreCase("multtextlist")) {
					MulListBean mulListBean = (MulListBean) objects;

					mulChildrenBeans = mulListBean.getMulChildrenBeans();
					System.out.println("mul-"+mulChildrenBeans.size());
					group = new String[mulChildrenBeans.size()];
					bmp = new Bitmap[mulChildrenBeans.size()];
					child = new HashMap<String, String>();
					for (int j = 0; j < group.length; j++) {
						// �����String���鸳ֵ
						group[j] = mulChildrenBeans.get(j).getTitle();
						// ���������תΪ��ͼ����תΪbitmap,Ȼ��ֵ��bitmap����
						bmp[j] = Util.convertViewToBitmap(Util.getView(
								this, group[j]));
						// ��ÿ���������URL��ַ
						child.put(group[j], mulChildrenBeans.get(j).getUrl());
					}
					// �����Ŷ���ͼƬ����ʼ��
					pathBtn = (ComposerLayout) view.findViewById(R.id.pathBtn);
					pathBtn.setVisibility(View.VISIBLE);
					pathBtn.init(bmp, R.drawable.composer_button,
							R.drawable.composer_icn_plus,
							ComposerLayout.RIGHTBOTTOM, 180, 300);

					URL = Constants.SERVER_URL + child.get(group[0]);
					comNewsAdapter = null;
					lsnb = null;
					new AsyncLoadNews(lsnb, URL).execute();
					// ����¼���Ԥ��5�������������������࣬���������������ڴ˴����
					// ��ӵ�������¼���100+0-->���1��100+1-->���2�������Դ����ƣ��м������ͼӼ�����
					OnClickListener clickit = new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (v.getId() == 100 + 0) {

								URL = Constants.SERVER_URL + child.get(group[0]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 1) {
								URL = Constants.SERVER_URL + child.get(group[1]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 2) {
								URL = Constants.SERVER_URL + child.get(group[2]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 3) {
								URL = Constants.SERVER_URL + child.get(group[3]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 4) {
								URL = Constants.SERVER_URL + child.get(group[4]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 5) {
								URL = Constants.SERVER_URL + child.get(group[5]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							}
						}
					};
					pathBtn.setButtonsOnClickListener(clickit);
				}
				
				// ��Ϊ����Ŀʱ��ͼ���б�
				if (fb.getType().equalsIgnoreCase("multimagetextlist")) {
					MulListBean mulListBean = (MulListBean) objects;

					mulChildrenBeans = mulListBean.getMulChildrenBeans();
					group = new String[mulChildrenBeans.size()];
					bmp = new Bitmap[mulChildrenBeans.size()];
					child = new HashMap<String, String>();
					for (int j = 0; j < group.length; j++) {
						group[j] = mulChildrenBeans.get(j).getTitle();// ����������鸳ֵ
						bmp[j] = Util.convertViewToBitmap(Util.getView(
								this, group[j]));
						child.put(group[j], mulChildrenBeans.get(j).getUrl());// ��ÿ���������URL��ַ
					}
					pathBtn = (ComposerLayout) view.findViewById(R.id.pathBtn);
					pathBtn.setVisibility(View.VISIBLE);
					pathBtn.init(bmp, R.drawable.composer_button,
							R.drawable.composer_icn_plus,
							ComposerLayout.RIGHTBOTTOM, 180, 300);

					URL = Constants.SERVER_URL + child.get(group[0]);
					comNewsAdapter = null;
					lsnb = null;
					new AsyncLoadNews(lsnb, URL).execute();
					// ����¼���Ԥ��5�������������������࣬���������������ڴ˴����
					// ��ӵ�������¼���100+0-->���1��100+1-->���2�������Դ����ƣ��м������ͼӼ�����
					OnClickListener clickit = new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (v.getId() == 100 + 0) {
								URL = Constants.SERVER_URL + child.get(group[0]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 1) {
								URL = Constants.SERVER_URL + child.get(group[1]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 2) {
								URL = Constants.SERVER_URL + child.get(group[2]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 3) {
								URL = Constants.SERVER_URL + child.get(group[3]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 4) {
								URL = Constants.SERVER_URL + child.get(group[4]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							} else if (v.getId() == 100 + 5) {
								URL = Constants.SERVER_URL + child.get(group[5]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(lsnb, URL).execute();
							}
						}
					};
					pathBtn.setButtonsOnClickListener(clickit);
				}
			}
		}

		/**
		 * �첽���������б�����
		 * */
		class AsyncLoadNews extends AsyncTask<Void, Void, List<NewsBean>> {

			private List<NewsBean> ls = null;// ���󼯺�
			private String url = null;// ·��

			public AsyncLoadNews(List<NewsBean> ls, String url) {
				this.ls = ls;
				this.url = url;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pd = ProgressDialogExtend.CreateSiglePDE(ComActivity.this, null);
				pd.show();
			}

			@Override
			protected List<NewsBean> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				synchronized (this) {
					return new ParseXMLUtil().parseNewsXml(ls, url);
				}
			}

			@Override
			protected void onPostExecute(final List<NewsBean> result) {
				// TODO Auto-generated method stub
				try {
					pd.dismiss();
					if (result != null && result.size() != 0) {
						lsnb = result;
						lsnb_temp.clear();

						pageNum = result.get(0).getPageNum();
						if (pageNum != null) {
							pageSize = Integer.parseInt(pageNum);
						}

						if (pageSize == 1) {
							listView.setPullLoadEnable(false);
						} else {
							listView.setPullLoadEnable(true);
						}

						if (comNewsAdapter != null) {
							comNewsAdapter.notifyDataSetChanged();
							listView.requestLayout();
							onLoadStop();
							// listView.setSelection(lsnb.size()-pageMax);//���õ�ǰ��һ����ʾ������
						} else {

							comNewsAdapter = new ComNewsAdapter(ComActivity.this,
									lsnb, mImageFetcher);

							// �˴���ȡ��ǰʱ�䣬Ϊ��һ��ˢ��ʱ��
							time = System.currentTimeMillis();
							curDate = new Date(time);
							sdf = new SimpleDateFormat("MM-dd HH:mm");
							onLoadStop();

							listView.setAdapter(comNewsAdapter);
							listView.requestLayout();
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public synchronized void onItemClick(
										AdapterView<?> arg0, View arg1, int arg2,
										long arg3) {
									// ��л�ϵ��ڴ˴���������
									if (lsnb == null && lsnb_temp != null
											&& lsnb_temp.size() > 0) {
										// lsnb.addAll(lsnb_temp);
										lsnb = lsnb_temp;
									} else if (lsnb == null && lsnb_temp.isEmpty()) {
										throw new NullPointerException("[[" + TAG
												+ "]]--> NewsListAdatper Is Null !");
									}
									--arg2;
									// ����������
									String link = Constants.SERVER_URL
											+ lsnb.get(arg2).getUrl();
									String title = lsnb.get(arg2).getTitle();
									String id = lsnb.get(arg2).getId();
									String location = lsnb.get(arg2).getLocation();

									if (BuildConfig.DEBUG) {
										Log.d(TAG, "id:" + id);
										Log.d(TAG, "title:" + title);
										Log.d(TAG, "link:" + link);
										Log.d(TAG, "location:" + location);
									}
									try {
										startActivity(new Intent(
												ComActivity.this,
												WebViewActivity.getInstance()
														.getClass())
												.putExtra("PRE_TITLE", cur_title)
												.putExtra("URL", link)
												.putExtra("CUR_TITLE", title)
												.putExtra("id", id)
												.putExtra("location", location));
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
						Toast toast = Toast.makeText(ComActivity.this, "���ٲ�����Ŷ!",
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
			mImageFetcher.setExitTasksEarly(false);
			if (comNewsAdapter != null) {
				comNewsAdapter.notifyDataSetChanged();
			}
			
		}

		@Override
		public void onPause() {
			super.onPause();
			mImageFetcher.setPauseWork(false);
			mImageFetcher.setExitTasksEarly(true);
			mImageFetcher.flushCache();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mImageFetcher.closeCache();
		}

		@Override
		public synchronized void onRefresh() {
			// TODO Auto-generated method stub
			try {
				// �˴���ȡ��ǰʱ�䣬Ϊˢ��ʱ��
				curDate = new Date(System.currentTimeMillis());
				sdf = new SimpleDateFormat("MM-dd HH:mm");
				if (System.currentTimeMillis() - time > 500) {
					URL = URL.substring(0, URL.lastIndexOf("_") + 1);
					URL = URL + "1.xml";
					if (lsnb != null) {
						lsnb_temp.addAll(lsnb);
						lsnb = null;
					}

					new AsyncLoadNews(lsnb, URL).execute();
				} else {
					Toast toast = Toast.makeText(this, "Ŀǰ����������!",
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
			try {
				// �����ҳ�����ڵ�ǰҳ���������ظ���
				if (pageSize > curPage) {
					URL = URL.substring(0, URL.lastIndexOf("_") + 1);
					curPage++;
					URL = URL + curPage + ".xml";
					new AsyncLoadNews(lsnb, URL).execute();
				} else {
					listView.setPullLoadEnable(false);
					onLoadStop();
					Toast toast = Toast.makeText(this, "�����Ѿ��������!",
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

		private void onLoadStop() {
			listView.stopRefresh();
			listView.stopLoadMore();
			if (curDate != null) {
				listView.setRefreshTime(sdf.format(curDate));
			}

		}

	

}
