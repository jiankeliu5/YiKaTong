package com.seedsoft.ykt.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.adpter.CardAdapter;
import com.seedsoft.ykt.bean.FatherBean;
import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.bitmap.util.ImageCache.ImageCacheParams;
import com.seedsoft.ykt.bitmap.util.ImageFetcher;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.ModuleSelector;

public class CardFragment extends Fragment {

	private GridView gv;
	private ImageFetcher mImageFetcher;
	private CardAdapter ca;
	private String pre_title;
	private ArrayList<FatherBean> fatherBeans;	
	private RootBean rootBean;

	
	public CardFragment(RootBean rootBean) {
		super();
		this.fatherBeans = rootBean.getFatherBeans();
		this.pre_title = rootBean.getName();
		this.rootBean = rootBean;
	}

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
		
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				Constants.IMAGE_CACHE_DIR);
		// Set memory cache to 25% of app memory
		cacheParams.setMemCacheSizePercent(0.25f);
		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		mImageFetcher = new ImageFetcher(getActivity());
	    mImageFetcher.setLoadingImage(R.drawable.yecx);
		mImageFetcher.addImageCache(
				((FragmentActivity) getActivity()).getSupportFragmentManager(),
				cacheParams);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.a_card_activity, null);
		TextView tv = (TextView) view.findViewById(R.id.top_title);
		tv.setText(pre_title);
		gv = (GridView) view.findViewById(R.id.a_gridview);
		ca = new CardAdapter(getActivity(), fatherBeans, mImageFetcher);
		gv.setAdapter(ca);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ModuleSelector.go(getActivity(), rootBean, position);
				

				
			}
		});
		gv.setOnScrollListener(new AbsListView.OnScrollListener() {

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
		return view;
	}
}
