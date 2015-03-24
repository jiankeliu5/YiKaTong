package com.seedsoft.ykt.adpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.bean.NewsBean;
import com.seedsoft.ykt.bitmap.util.ImageFetcher;
import com.seedsoft.ykt.util.Constants;

/**
 * 新闻列表适配器
 * */
public class ComNewsAdapter extends BaseAdapter{
	    
    private ImageFetcher mImageFetcher = null;
    private Context ctx;
    private List<NewsBean> list ;     
    
	public ComNewsAdapter(Context ctx,List<NewsBean> list,ImageFetcher mImageFetcher){
		this.ctx = ctx;
		this.list = list;
		this.mImageFetcher = mImageFetcher;	
	}
	


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return list.size();
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
		ImageView icon;
		TextView titel;
		TextView desp;
		TextView author;
		TextView time;
		String authorStr;
		String iconStr;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub			
		holdView hv = null;
		if(convertView == null || convertView.getTag() == null){
						
			hv = new holdView();
			convertView = LayoutInflater.from(ctx).inflate(R.layout.content_list_item, null);
			hv.titel = (TextView) convertView.findViewById(R.id.content_list_item_titel);
			hv.desp = (TextView) convertView.findViewById(R.id.content_list_item_desp);
			hv.author = (TextView) convertView.findViewById(R.id.author);
			hv.time = (TextView) convertView.findViewById(R.id.time);
			hv.icon = (ImageView) convertView.findViewById(R.id.content_list_item_icon);
			convertView.setTag(hv);
			
		}else{
			hv = (holdView) convertView.getTag();
		}
		NewsBean nb = list.get(position);
		hv.titel.setText(nb.getTitle());
		hv.desp.setText(nb.getDesp());
		hv.authorStr = nb.getAuthor();
		if(hv.authorStr != null && !hv.authorStr.equals("")){
			hv.author.setText(hv.authorStr);
		}else{
			hv.author.setText("管理员");
		}
		hv.time.setText(nb.getDate().subSequence(0, 11));
		hv.iconStr = nb.getImage();
		if(hv.iconStr != null && !hv.iconStr.equals("")){
			hv.icon.setVisibility(View.VISIBLE);
			mImageFetcher.loadImage(Constants.SERVER_URL + nb.getImage(), hv.icon);
		}else{
			hv.icon.setVisibility(View.GONE);
		}
		
		return convertView;
	}	

}
