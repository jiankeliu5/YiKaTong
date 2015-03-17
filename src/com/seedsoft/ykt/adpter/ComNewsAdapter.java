package com.seedsoft.ykt.adpter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seedsoft.ykt.activity.MainActivity;
import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.activity.WebViewAcivity;
import com.seedsoft.ykt.bean.AdBean;
import com.seedsoft.ykt.bean.AppBean;
import com.seedsoft.ykt.bean.LinkBean;
import com.seedsoft.ykt.bean.NewsBean;
import com.seedsoft.ykt.bitmap.util.ImageFetcher;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.Util;
import com.seedsoft.ykt.widget.MyGridView;

/**
 * 新闻列表适配器
 * */
public class ComNewsAdapter extends BaseAdapter{
	    
    private ImageFetcher mImageFetcher = null;
    private Context ctx;
    private List<NewsBean> list ;     
	
    private LayoutInflater lin;
    private Map<String, Object> map = new HashMap<String, Object>();
    private int num = 0;
    
	public ComNewsAdapter(Context ctx,List<NewsBean> list,ImageFetcher mImageFetcher,
			Map<String, Object> map){
		this.ctx = ctx;
		this.list = list;
		this.lin = lin.from(ctx);
		this.mImageFetcher = mImageFetcher;
		this.map = map;
		if(map.size() > 0){
			num = 1;
		}	
				
	}
	@SuppressWarnings("static-access")
	public ComNewsAdapter(Context ctx,List<NewsBean> list ){
		this.ctx = ctx;
		this.list = list;
		this.lin = lin.from(ctx);
		if(map.size() > 0){
			num = 1;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return list.size()+num;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class holdView{
		ImageView content_list_item_icon;
		TextView content_list_item_titel;
		TextView content_list_item_desp;
		TextView comment_num;
		ImageView small_icon_comment;
		ImageView small_icon_vedio;
		LinearLayout content_list_item_bottom_layout;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub			
		holdView hv = null;
		if(convertView == null || convertView.getTag() == null){
						
			hv = new holdView();
			convertView = lin.inflate(R.layout.content_list_item, null);
			hv.content_list_item_titel = (TextView) convertView.findViewById(R.id.content_list_item_titel);
			hv.content_list_item_desp = (TextView) convertView.findViewById(R.id.content_list_item_desp);
			hv.comment_num = (TextView) convertView.findViewById(R.id.comment_num);
			hv.content_list_item_icon = (ImageView) convertView.findViewById(R.id.content_list_item_icon);
			hv.small_icon_comment = (ImageView) convertView.findViewById(R.id.small_icon_comment);
			hv.small_icon_vedio = (ImageView) convertView.findViewById(R.id.small_icon_vedio);
			hv.content_list_item_bottom_layout = (LinearLayout) convertView.findViewById(R.id.content_list_item_bottom_layout);
			convertView.setTag(hv);
			
		}else{
			hv = (holdView) convertView.getTag();
		}
			
		if(position == 0 && map.size() != 0 && map.containsKey(Constants.PIC_LINK)){								
			convertView = lin.inflate(R.layout.com_gridview, null);				
			MyGridView gridview = (MyGridView) convertView.findViewById(R.id.com_gridView);
			
			//图片链接								
				
				final List<LinkBean> lslb = (List<LinkBean>) map.get(Constants.PIC_LINK);
				gridview.setNumColumns(lslb.size());
				gridview.setAdapter(new ComImageAdapter(ctx,lslb, mImageFetcher));
				gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						LinkBean plink = lslb.get(arg2);
						String isAllowComment = "n";
						String title = plink.getTitle();
						ArrayList<AdBean> ad = plink.getAdBeans();
						String ad_title = "";
						String ad_image = "";
						String ad_url = "";
						if(ad != null && ad.size() > 0){
							ad_title = ad.get(0).getTitle();
							ad_image = Constants.SERVER_URL + ad.get(0).getImage();
							ad_url = Constants.SERVER_URL + ad.get(0).getUrl();
						}
						String url = plink.getUrl();
						if(url.subSequence(0, 1).equals("/")){
							url = Constants.SERVER_URL + url;
						}
						
						try {
							ctx.startActivity(new Intent((MainActivity) ctx,
									WebViewAcivity.getInstance().getClass())
									.putExtra("AD_TITLE", ad_title)
									.putExtra("AD_IMAGE", ad_image)
									.putExtra("AD_URL", ad_url)
									.putExtra("URL", url)
									.putExtra("title", title)
									.putExtra("isAllowComment", isAllowComment));
							((Activity) ctx).overridePendingTransition(R.anim.hold_enter, R.anim.fade_out);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				
				return convertView;
	}else
		
	if(position == 0 && map.size() != 0 && map.containsKey(Constants.TXT_LINK)){								
		convertView = lin.inflate(R.layout.com_gridview, null);				
		MyGridView gridview = (MyGridView) convertView.findViewById(R.id.com_gridView);
	
			//文字链接
								
				final List<LinkBean> lslb = (List<LinkBean>) map.get(Constants.TXT_LINK);
				gridview.setNumColumns(lslb.size());
				gridview.setAdapter(new ComTextAdapter(ctx,lslb));
				gridview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						LinkBean plink = lslb.get(arg2);
						String isAllowComment = "n";
						String title = plink.getTitle();
						ArrayList<AdBean> ad = plink.getAdBeans();
						String ad_title = "";
						String ad_image = "";
						String ad_url = "";
						if(ad != null && ad.size() > 0){
							ad_title = ad.get(0).getTitle();
							ad_image = Constants.SERVER_URL + ad.get(0).getImage();
							ad_url = Constants.SERVER_URL + ad.get(0).getUrl();
						}
						String url = plink.getUrl();
						if(url.subSequence(0, 1).equals("/")){
							url = Constants.SERVER_URL + url;
						}
						
						try {
							ctx.startActivity(new Intent((MainActivity) ctx,
									WebViewAcivity.getInstance().getClass())
									.putExtra("AD_TITLE", ad_title)
									.putExtra("AD_IMAGE", ad_image)
									.putExtra("AD_URL", ad_url)
									.putExtra("URL", url)
									.putExtra("title", title)
									.putExtra("isAllowComment", isAllowComment));
							((Activity) ctx).overridePendingTransition(R.anim.hold_enter, R.anim.fade_out);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
			return convertView;
		}else
			
	if(position == 0 && map.size() != 0 && map.containsKey(Constants.PIC_APP)){								
		convertView = lin.inflate(R.layout.com_gridview, null);				
		MyGridView gridview = (MyGridView) convertView.findViewById(R.id.com_gridView);
	
			//图片APP
								
				final List<AppBean> lslb = (List<AppBean>) map.get(Constants.PIC_APP);
				gridview.setNumColumns(lslb.size());
				gridview.setAdapter(new ComAppImageAdapter(ctx,lslb, mImageFetcher));
				gridview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						AppBean ab = lslb.get(arg2);
						String appPacket = ab.getPath();
						String downLoadUrl = Constants.SERVER_URL + ab.getUrl();					
						String appName = ab.getTitle()+".apk";
						String appSize = ab.getSize();
						System.out.println("downpicapp-->"+appPacket+appName+downLoadUrl+appSize);
						//在此处判断是否安装过该APP，没有的话，提示下载！！！！！
						new Util().launchApp(ctx,appPacket, downLoadUrl, appName, appSize);
					}
				});
				
			return convertView;
	}else
		
	if(position == 0 && map.size() != 0 && map.containsKey(Constants.TXT_APP)){								
		convertView = lin.inflate(R.layout.com_gridview, null);				
		MyGridView gridview = (MyGridView) convertView.findViewById(R.id.com_gridView);
	
			//文字APP
								
				final List<AppBean> lslb = (List<AppBean>) map.get(Constants.TXT_APP);
				gridview.setNumColumns(lslb.size());
				gridview.setAdapter(new ComAppImageAdapter(ctx,lslb, mImageFetcher));
				gridview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						AppBean ab = lslb.get(arg2);
						String appPacket = ab.getPath();
						String downLoadUrl = Constants.SERVER_URL + ab.getUrl();					
						String appName = ab.getTitle()+".apk";
						String appSize = ab.getSize();
						System.out.println("downpicapp-->"+appPacket+appName+downLoadUrl+appSize);
						//在此处判断是否安装过该APP，没有的话，提示下载！！！！！
						new Util().launchApp(ctx,appPacket, downLoadUrl, appName, appSize);
					}
				});
				
			return convertView;
	}
			
			if(num != 0) position = position-num;
//			System.out.println("--position--adpter--"+position);
			NewsBean nb = list.get(position);
			hv.content_list_item_titel.setText(nb.getTitle());
			hv.content_list_item_desp.setText(nb.getDesp());
			String num = nb.getCommentNum();
			int n = Integer.valueOf(num);
			if(n>0){
				hv.comment_num.setText(num);
			}else{
//				hv.small_icon_comment.setVisibility(View.INVISIBLE);
//				hv.comment_num.setVisibility(View.INVISIBLE);
				hv.content_list_item_bottom_layout.setVisibility(View.GONE);
			}
			
			String isHasVedio = nb.getVedio();
			if(isHasVedio==null||isHasVedio.equals("")) hv.small_icon_vedio.setVisibility(View.INVISIBLE);
			
			if(mImageFetcher == null){
				hv.content_list_item_icon.setVisibility(View.GONE);
			}else{
				mImageFetcher.loadImage(Constants.SERVER_URL + nb.getImage(), hv.content_list_item_icon);
			}
		
		return convertView;
	}	

}
