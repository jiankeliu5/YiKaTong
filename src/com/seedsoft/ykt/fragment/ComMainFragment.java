package com.seedsoft.ykt.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.activity.MainActivity;
import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.activity.WebViewAcivity;
import com.seedsoft.ykt.adpter.ComAppImageAdapter;
import com.seedsoft.ykt.adpter.ComImageAdapter;
import com.seedsoft.ykt.adpter.ComNewsAdapter;
import com.seedsoft.ykt.adpter.ComTextAdapter;
import com.seedsoft.ykt.bean.AdBean;
import com.seedsoft.ykt.bean.AppBean;
import com.seedsoft.ykt.bean.CarouselBean;
import com.seedsoft.ykt.bean.FatherModuleBean;
import com.seedsoft.ykt.bean.LinkBean;
import com.seedsoft.ykt.bean.MulChildrenBean;
import com.seedsoft.ykt.bean.MulListBean;
import com.seedsoft.ykt.bean.NewsBean;
import com.seedsoft.ykt.bitmap.util.ImageCache.ImageCacheParams;
import com.seedsoft.ykt.bitmap.util.ImageFetcher;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.ParseXMLUtil;
import com.seedsoft.ykt.util.Util;
import com.seedsoft.ykt.widget.ComposerLayout;
import com.seedsoft.ykt.widget.MyViewPager;
import com.seedsoft.ykt.widget.MyViewPager.onSimpleClickListener;
import com.seedsoft.ykt.widget.ProgressDialogExtend;
import com.seedsoft.ykt.widget.XListView;
import com.seedsoft.ykt.widget.XListView.IXListViewListener;

@SuppressLint("ValidFragment")
public class ComMainFragment extends Fragment implements onSimpleClickListener,IXListViewListener{

	private static final long serialVersionUID = 1L;
	private static String TAG = "ComMainFragment";
	private String name;//fragment的名字
	private ImageFetcher mImageFetcher;
	private static final String IMAGE_CACHE_DIR = "thumbs";

	private View view;//当前屏幕显示的视图（onCreateView的返回值）	
	public ArrayList<FatherModuleBean> fatherModuleBeans;//全局显示配置文件数据源		
	private LayoutInflater lin;	
	private List<NewsBean> course_list = null;
	
	private CarouselBean cb = null;
	private boolean isCourse = false;
	private int num = 0;
	private int cur = 0;
	
	private MyViewPager scroll_pics_viewPager;//图片轮播视图
	
	private float xDistance, yDistance;	//记录轮播图片上手势位置坐标
	
	private float mLastMotionX,mLastMotionY;// 记录按下的XY坐标	
	
	private boolean mIsBeingDragged = true;//是否是左右滑动
	
	private List<NewsBean> lsnb = null;
	private List<NewsBean> lsnb_temp = null;
	private SimpleDateFormat sdf = null;
    private Date curDate = null;//当前时间
    private long time = 0;	
	private XListView listView;//新闻内容列表视图	
	private String pageNum = null;//总页数
	private int pageSize = 1;//总页数
	private int curPage = 1;//当前页码
	private int pageMax = 15;//每页15条信息
	private String URL;
	private boolean flag = false;//是否为图文列表
	private Map<String, Object> map ;
		
	private List<LinkBean> lsPicLink;//imagelink类型数据源
	
	private List<LinkBean> lsTextLink;//textlink类型数据源
	
	private List<AppBean> lsPicApp;//imageapp类型数据源
	
	private List<AppBean> lsTextApp;//textapp类型数据源
	
	private ComImageAdapter picLinkAdapter;
	private ComTextAdapter textLinkAdapter;
	private ComAppImageAdapter picAPPAdapter;
	private ComAppImageAdapter textAppAdapter;
	
	private ComposerLayout pathBtn;//多栏目时，使用pathbtn控件显示子栏目，目前支持五个
	private String[] group ;//多栏目列表,类别名称数组
	private Bitmap[] bmp;//转换为bitmap的类别名称数组
	private Map<String, String> child = null;//key:类别,value:URL地址
	private List<MulChildrenBean> mulChildrenBeans;
	
	private ComNewsAdapter comNewsAdapter = null;	
	
	private ProgressDialogExtend pd;	

	//广告
	private String ad_title = "";
	private String ad_image = "";
	private String ad_url = "";
	
	public ComMainFragment(ArrayList<FatherModuleBean> fatherModuleBeans) {
		this.fatherModuleBeans = fatherModuleBeans;
	}

	public ComMainFragment(){}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		if(mImageFetcher != null){
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
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity());
//        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(((FragmentActivity) getActivity()).getSupportFragmentManager(), cacheParams);
        lsnb_temp = new ArrayList<NewsBean>();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.com_main_fragment, container,false);
		listView = (XListView) view.findViewById(R.id.content_list_lv);
		listView.setVisibility(View.VISIBLE);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING){
					mImageFetcher.setPauseWork(true);
				}else{
					mImageFetcher.setPauseWork(false);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		lin = inflater;
		
		selectShowOn(fatherModuleBeans);//判断是否显示组件视图		
			
		return view;
	}

	/**
	 * 根据当前的fragment的构造函数传来的参数--组件类型集合--判断当前fragment显示哪些视图
	 * */
	private void selectShowOn(ArrayList<FatherModuleBean> fatherModuleBeans) {
		// TODO Auto-generated method stub
		map = new HashMap<String, Object>();
		lsPicLink = new ArrayList<LinkBean>();
		lsTextLink = new ArrayList<LinkBean>();
		lsPicApp = new ArrayList<AppBean>();
		lsTextApp = new ArrayList<AppBean>();
		
		for (int i = 0; i < fatherModuleBeans.size(); i++) {
			FatherModuleBean fb = fatherModuleBeans.get(i);
			Object objects = fb.getObjects();//获取元数据对象
	
			//当为轮播组件类型时，头部viewpager广告控件时
			if(fb.getType().equalsIgnoreCase("carousel")){			
				cb = (CarouselBean) objects;//获取该类型数据源
				isCourse = true;
				//添加头部广告轮播视图
				addHeadView(cb);
			}
			
			//当为图片链接时，maybe a lot
			if(fb.getType().equalsIgnoreCase("imagelink")){
				LinkBean linkBean = (LinkBean) objects;
				lsPicLink.add(linkBean);
				map.put(Constants.PIC_LINK, lsPicLink);
			}
			
			//当为文字链接时，maybe a lot
			if(fb.getType().equalsIgnoreCase("textlink")){
				LinkBean linkBean = (LinkBean) objects;
				lsTextLink.add(linkBean);
				map.put(Constants.TXT_LINK, lsTextLink);
			}
			
			//当为图片下载时，maybe a lot
			if(fb.getType().equalsIgnoreCase("imageapp")){
				AppBean appBean = (AppBean) objects;
				lsPicApp.add(appBean);
				map.put(Constants.PIC_APP, lsPicApp);
			}
			
			//当为文字下载接时，maybe a lot
			if(fb.getType().equalsIgnoreCase("textapp")){
				AppBean appBean = (AppBean) objects;
				lsTextApp.add(appBean);
				map.put(Constants.TXT_APP, lsTextApp);
			}
			
			//当为单文字列表时
			if(fb.getType().equalsIgnoreCase("singletextlist")){
				CarouselBean cb = (CarouselBean) objects;//获取该类型数据源
				//获取广告
				ArrayList<AdBean> ad = cb.getAdBeans();
				
				if(ad != null && ad.size() > 0){
					ad_title = ad.get(0).getTitle();
					ad_image = Constants.SERVER_URL + ad.get(0).getImage();
					ad_url = Constants.SERVER_URL + ad.get(0).getUrl();
				}	
				
				URL = Constants.SERVER_URL + cb.getUrl();//获取单文字列表的信息列表路径，传递给解析类，返回一个列表对象
				new AsyncLoadNews(flag,lsnb,URL).execute();
			}
			
			//当为单图文列表时
			if(fb.getType().equalsIgnoreCase("singleimagetextlist")){
				CarouselBean cb = (CarouselBean) objects;//获取该类型数据源
				//获取广告
				ArrayList<AdBean> ad = cb.getAdBeans();
				
				if(ad != null && ad.size() > 0){
					ad_title = ad.get(0).getTitle();
					ad_image = Constants.SERVER_URL + ad.get(0).getImage();
					ad_url = Constants.SERVER_URL + ad.get(0).getUrl();
				}	
				
				URL = Constants.SERVER_URL + cb.getUrl();
				flag = true;
				new AsyncLoadNews(flag,lsnb,URL).execute();
			}			
			
			//当为多栏目时，文字列表
			if(fb.getType().equalsIgnoreCase("multtextlist")){
				MulListBean mulListBean = (MulListBean) objects;
				//获取广告
				ArrayList<AdBean> ad = mulListBean.getAdBeans();
				
				if(ad != null && ad.size() > 0){
					ad_title = ad.get(0).getTitle();
					ad_image = Constants.SERVER_URL + ad.get(0).getImage();
					ad_url = Constants.SERVER_URL + ad.get(0).getUrl();
				}	
				
				
				mulChildrenBeans = mulListBean.getMulChildrenBeans();
				group = new String[mulChildrenBeans.size()];
				bmp = new Bitmap[mulChildrenBeans.size()];
				child = new HashMap<String, String>();
				for (int j = 0; j < group.length; j++) {
					//给类别String数组赋值
					group[j] = mulChildrenBeans.get(j).getTitle();
					//将类别名字转为视图，再转为bitmap,然后赋值给bitmap数组
					bmp[j] = Util.convertViewToBitmap(Util.getView(getActivity(),group[j]));
					//给每个类别赋数据URL地址
					child.put(group[j], mulChildrenBeans.get(j).getUrl());
				}
				//给＋号动画图片，初始化
				pathBtn = (ComposerLayout) view.findViewById(R.id.pathBtn);
				pathBtn.setVisibility(View.VISIBLE);				
				pathBtn.init(bmp,R.drawable.composer_button,R.drawable.composer_icn_plus, 
						ComposerLayout.RIGHTBOTTOM , 180, 300);
				
				URL =Constants.SERVER_URL + child.get(group[0]);
				comNewsAdapter = null;
				lsnb = null;
				new AsyncLoadNews(flag,lsnb,URL).execute();
					//点击事件先预留5个，即最多设置五个分类，若大于五个类别，则在此处添加
			        //添加点击监听事件；100+0-->类别1，100+1-->类别2，……以此类推，有几个类别就加几个。
			        OnClickListener clickit=new OnClickListener() {			
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(v.getId()==100+0){
								
								URL =Constants.SERVER_URL + child.get(group[0]);	
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+1){
								URL =Constants.SERVER_URL + child.get(group[1]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+2){
								URL =Constants.SERVER_URL + child.get(group[2]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+3){
								URL =Constants.SERVER_URL + child.get(group[3]);								
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+4){
								URL =Constants.SERVER_URL + child.get(group[4]);								
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+5){
								URL =Constants.SERVER_URL + child.get(group[5]);								
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}
						}
					};
					pathBtn.setButtonsOnClickListener(clickit);
			}
			
			//当为多栏目时，图文列表
			if(fb.getType().equalsIgnoreCase("multimagetextlist")){
				MulListBean mulListBean = (MulListBean) objects;
				
				//获取广告
				ArrayList<AdBean> ad = mulListBean.getAdBeans();
				
				if(ad != null && ad.size() > 0){
					ad_title = ad.get(0).getTitle();
					ad_image = Constants.SERVER_URL + ad.get(0).getImage();
					ad_url = Constants.SERVER_URL + ad.get(0).getUrl();
				}	
				
				mulChildrenBeans = mulListBean.getMulChildrenBeans();
				group = new String[mulChildrenBeans.size()];
				bmp = new Bitmap[mulChildrenBeans.size()];
				child = new HashMap<String, String>();				
				for (int j = 0; j < group.length; j++) {
					group[j] = mulChildrenBeans.get(j).getTitle();//给多类别数组赋值
					bmp[j] = Util.convertViewToBitmap(Util.getView(getActivity(),group[j])); 
					child.put(group[j], mulChildrenBeans.get(j).getUrl());//给每个类别赋数据URL地址
				}	
				pathBtn = (ComposerLayout) view.findViewById(R.id.pathBtn);
				pathBtn.setVisibility(View.VISIBLE);				
				pathBtn.init(bmp,R.drawable.composer_button,R.drawable.composer_icn_plus, 
						ComposerLayout.RIGHTBOTTOM , 180, 300);
				
				URL =Constants.SERVER_URL + child.get(group[0]);	
				flag = true;
				comNewsAdapter = null;
				lsnb = null;
				new AsyncLoadNews(flag,lsnb,URL).execute();
				//点击事件先预留5个，即最多设置五个分类，若大于五个类别，则在此处添加
		        //添加点击监听事件；100+0-->类别1，100+1-->类别2，……以此类推，有几个类别就加几个。
			        OnClickListener clickit=new OnClickListener() {			
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(v.getId()==100+0){
								URL =Constants.SERVER_URL + child.get(group[0]);	
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+1){
								URL =Constants.SERVER_URL + child.get(group[1]);
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+2){
								URL =Constants.SERVER_URL + child.get(group[2]);		
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+3){
								URL =Constants.SERVER_URL + child.get(group[3]);								
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+4){
								URL =Constants.SERVER_URL + child.get(group[4]);								
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}else if(v.getId()==100+5){
								URL =Constants.SERVER_URL + child.get(group[5]);								
								comNewsAdapter = null;
								lsnb = null;
								new AsyncLoadNews(flag,lsnb,URL).execute();
							}
						}
					};
					pathBtn.setButtonsOnClickListener(clickit);	
			}			
		}				
	}


	/**
	 * 异步加载新闻列表内容
	 * @parm boolean hasImage = false;//是否显示图片
	 * */
	class AsyncLoadNews extends AsyncTask<Void, Void, List<NewsBean>>{

		
		private boolean hasImage = false;//是否显示图片
		private List<NewsBean> ls = null;//对象集合
		private String url = null;//路径		
		
		public AsyncLoadNews(boolean hasImage,List<NewsBean> ls,String url){
			this.hasImage = hasImage;
			this.ls = ls;
			this.url = url;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = ProgressDialogExtend.CreateSiglePDE(getActivity(),null);
			pd.show();
		}
		@Override
		protected List<NewsBean> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			synchronized (this) {			
				return new ParseXMLUtil().parseNewsXml(ls,url);
			}			
		}
		
		@Override
		protected void onPostExecute(final List<NewsBean> result) {
			// TODO Auto-generated method stub
			try {
				pd.dialog.dismiss();
				if(result != null && result.size() != 0){
					lsnb = result;
					lsnb_temp.clear();
					
					pageNum = result.get(0).getPageNum();
					if(pageNum != null){
						pageSize = Integer.parseInt(pageNum);
					}
					
					if(pageSize == 1) {
						listView.setPullLoadEnable(false);
					}else{
						listView.setPullLoadEnable(true);
					}
					
					if(comNewsAdapter != null){
						comNewsAdapter.notifyDataSetChanged();
						listView.requestLayout();
						onLoadStop();
//				listView.setSelection(lsnb.size()-pageMax);//设置当前第一行显示的数据
					}else{
						if(hasImage){
							comNewsAdapter = new ComNewsAdapter(getActivity(), lsnb,mImageFetcher,map);
						}else{
							comNewsAdapter = new ComNewsAdapter(getActivity(), lsnb);
						}
						
						//此处可取当前时间，为第一次刷新时间
						time = System.currentTimeMillis();
						curDate = new Date(time);
						sdf = new SimpleDateFormat("MM-dd HH:mm");
						onLoadStop();
						
						
					
						listView.setAdapter(comNewsAdapter);
						listView.requestLayout();
						listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public synchronized void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							//arg2莫名其妙的多一行
							//感谢上帝在此处帮助了我
							if(map.size() > 0 && isCourse == true){
								num = 2;
							}else
								if((map.size() > 0 && isCourse == false) || (map.size() == 0 && isCourse == true)){
									num = 1;
								}
							if(arg2>num){
								cur = arg2-num-1;
							}else{
								cur = arg2-1;
							}
							
							if(lsnb == null && lsnb_temp != null && lsnb_temp.size() > 0){
//							lsnb.addAll(lsnb_temp);
								lsnb = lsnb_temp;
							}else if(lsnb == null && lsnb_temp.isEmpty()){
								throw new NullPointerException("[["+TAG+"]]--> NewsListAdatper Is Null !");
							}
							//打开文章内容
							String link = Constants.SERVER_URL + lsnb.get(cur).getUrl();
							String comment_url = Constants.SERVER_URL + lsnb.get(cur).getCommentURL();
							String isAllowComment = lsnb.get(cur).getIsAllowComment();
							String title = lsnb.get(cur).getTitle();
							String id = lsnb.get(cur).getId();
							String location = lsnb.get(cur).getLocation();
						
							try {
								startActivity(new Intent((MainActivity) getActivity(),
										WebViewAcivity.getInstance().getClass())
										.putExtra("AD_TITLE", ad_title)
										.putExtra("AD_IMAGE", ad_image)
										.putExtra("AD_URL", ad_url)
										.putExtra("URL", link)
										.putExtra("title", title)
										.putExtra("id", id)
										.putExtra("comment_url", comment_url)
										.putExtra("location", location)
										.putExtra("isAllowComment", isAllowComment));
								getActivity().overridePendingTransition(R.anim.hold_enter, R.anim.fade_out);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}
						}				
					});
					}
				}else{
					onLoadStop();
					Toast toast = Toast.makeText(getActivity(), "网速不给力哦!", Toast.LENGTH_LONG);
//					toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
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
	        if(comNewsAdapter!=null){
	        	comNewsAdapter.notifyDataSetChanged();
	        }
	        if(picLinkAdapter!=null){
	        	picLinkAdapter.notifyDataSetChanged();
	        }
	        if(textLinkAdapter!=null){
	        	textLinkAdapter.notifyDataSetChanged();
	        }
	        if(picAPPAdapter!=null){
	        	picAPPAdapter.notifyDataSetChanged();
	        }
	        if(textAppAdapter!=null){
	        	textAppAdapter.notifyDataSetChanged();
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
	    
	    /** 添加HeadView(广告控件) **/
		private void addHeadView(CarouselBean cb) {
			View mHeadView = lin.inflate(R.layout.fragment_head_view, null);
			scroll_pics_viewPager = (MyViewPager) mHeadView.findViewById(R.id.fragment_view_pager);
			scroll_pics_viewPager.setOnSimpleClickListener(this);	
			scroll_pics_viewPager.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					scroll_pics_viewPager.getGestureDetector().onTouchEvent(event);
					// TODO Auto-generated method stub
					final float x = event.getRawX();
					final float y = event.getRawY();
					
	                switch (event.getAction()) {  
	                case MotionEvent.ACTION_DOWN:  
	                    xDistance = yDistance = 0f;
	                	mLastMotionX = x;
	                	mLastMotionY = y;
	                case MotionEvent.ACTION_MOVE:  
	                    final float xDiff = Math.abs(x - mLastMotionX);
	                    final float yDiff = Math.abs(y - mLastMotionY);
	                    xDistance += xDiff;
	                    yDistance += yDiff;
	                    
	                    float dx = xDistance - yDistance;
	                    /** 左右滑动避免和下拉刷新冲突   **/
	                    if (xDistance > yDistance || Math.abs(xDistance - yDistance) < 0.00001f) {
	                        mIsBeingDragged = true;
	                        mLastMotionX =  x;
	                        mLastMotionY = y;
	                        ((ViewParent) v.getParent()).requestDisallowInterceptTouchEvent(true);
	                    } else {
	                        mIsBeingDragged = false;
	                        ((ViewParent) v.getParent()).requestDisallowInterceptTouchEvent(false);
	                    }
	                    break;  
	                case MotionEvent.ACTION_UP:  
	                 	break;  
	                case MotionEvent.ACTION_CANCEL:
	                	if(mIsBeingDragged) {
	                		((ViewParent) v.getParent()).requestDisallowInterceptTouchEvent(false);
						}
	                	break;
	                default:  
	                    break;  
	                }  
	                return false;  
				}
			});			
			new AsyncLoadCarousel().execute(cb);
			listView.addHeaderView(mHeadView);
		}
		@Override
		public void setOnSimpleClickListenr(int position) {
			// TODO Auto-generated method stub
			NewsBean nb = course_list.get(position);
			try {
				startActivity(new Intent(getActivity(),
						WebViewAcivity.getInstance().getClass())
				.putExtra("URL", Constants.SERVER_URL + nb.getUrl())
				.putExtra("title", nb.getTitle())
				.putExtra("id", nb.getId())
				.putExtra("location", nb.getLocation())
				.putExtra("comment_url", Constants.SERVER_URL + nb.getCommentURL())
				.putExtra("isAllowComment", nb.getIsAllowComment()));
				getActivity().overridePendingTransition(R.anim.hold_enter, R.anim.fade_out);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	 

		
		/**异步加载头部viewpager广告控件数据*/
		class AsyncLoadCarousel extends AsyncTask<CarouselBean, Void, List<NewsBean>>{
			
			@Override
			protected List<NewsBean> doInBackground(CarouselBean... params) {
				// TODO Auto-generated method stub
				
				synchronized (params) {
					return new ParseXMLUtil().parseCarouselXml(params[0]);
				}
			}
			
			@Override
			protected void onPostExecute(List<NewsBean> result) {
				// TODO Auto-generated method stub
				
				try {
					if(result != null){
						course_list = result;
						//感谢上帝在此处帮助了我
						AdvAdapter aa = new AdvAdapter();
						scroll_pics_viewPager.setAdapter(aa);			
					}else{
						Toast toast = Toast.makeText(getActivity(), "网速不给力哦!", Toast.LENGTH_LONG);
//						toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
						toast.getView().setPadding(20, 10, 20, 10);
						toast.show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/** 广告控件适配器  **/
		public class AdvAdapter extends PagerAdapter{

			public AdvAdapter() {
				super();
			}						
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return course_list.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				 return arg0 == arg1;  
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				// TODO Auto-generated method stub
				((ViewPager) container).removeView((View) object);
			}
			
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub				
					View view = null;
					TextView index_tv = null;
					TextView title_tv = null;
					ImageView scroll_iv = null;
					view = lin.inflate(R.layout.viewpager_layout, null);
			    	index_tv = (TextView) view.findViewById(R.id.index_tv);
			        title_tv = (TextView) view.findViewById(R.id.index_title_tv);
			    	scroll_iv = (ImageView) view.findViewById(R.id.scroll_iv);
			    	
				//感谢上帝在此处帮助我。
					NewsBean nb = course_list.get(position);
		        	index_tv.setText((position+1)+"/"+course_list.size());
		        	title_tv.setText(nb.getTitle());
		        	title_tv.setSingleLine(true);
		        	title_tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
		        	index_tv.setBackgroundColor(Color.argb(120, 255, 0, 0));
		        	title_tv.setBackgroundColor(Color.argb(120, 0, 0, 0));
		        	mImageFetcher.loadImage(Constants.SERVER_URL + nb.getImage(), scroll_iv);
		    	
		        ((ViewPager) container).addView(view, 0);  
		        return view;
			}
		}


		@Override
		public synchronized void onRefresh() {
			// TODO Auto-generated method stub
			try {
				//此处可取当前时间，为刷新时间
				curDate = new Date(System.currentTimeMillis());
				sdf = new SimpleDateFormat("MM-dd HH:mm");
				if(System.currentTimeMillis()-time>500){
					URL = URL.substring(0, URL.lastIndexOf("_")+1);
					URL = URL+"1.xml";
					if(lsnb != null){
						lsnb_temp.addAll(lsnb);
						lsnb = null;
					}
					
					new AsyncLoadNews(flag,lsnb,URL).execute();
				}else{
					Toast toast = Toast.makeText(getActivity(), "目前是最新数据!", Toast.LENGTH_LONG);
//					toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
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
				//如果总页数大于当前页码数，加载更多
				if(pageSize > curPage){
					URL = URL.substring(0, URL.lastIndexOf("_")+1);
					curPage++;
					URL = URL+curPage+".xml";
					new AsyncLoadNews(flag,lsnb,URL).execute();
				}else{
					listView.setPullLoadEnable(false);
					onLoadStop();
					Toast toast = Toast.makeText(getActivity(), "数据已经加载完毕!", Toast.LENGTH_LONG);
//					toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
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
			if(curDate != null){
				listView.setRefreshTime(sdf.format(curDate));
			}		
			
		}

		
		
}
