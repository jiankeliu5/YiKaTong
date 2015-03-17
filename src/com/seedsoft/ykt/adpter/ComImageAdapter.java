package com.seedsoft.ykt.adpter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.bean.LinkBean;
import com.seedsoft.ykt.bitmap.util.ImageFetcher;
import com.seedsoft.ykt.util.Constants;

public class ComImageAdapter extends BaseAdapter{
	  
    ImageFetcher mImageFetcher;
	List<LinkBean> list ;
	LayoutInflater lin;
	public ComImageAdapter(Context ctx,List<LinkBean> list,ImageFetcher mImageFetcher ){		
		this.list = list;
		this.lin = lin.from(ctx);
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
		TextView tv1;
		ImageView iv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		holdView hv = null;
		if(convertView == null){
			hv = new holdView();
			convertView = lin.inflate(R.layout.com_image_adpter_item, null);
			hv.tv1 = (TextView) convertView.findViewById(R.id.com_title);
			hv.iv = (ImageView) convertView.findViewById(R.id.com_pic);
			convertView.setTag(hv);
		}
		hv = (holdView) convertView.getTag();
		LinkBean lb = list.get(position);
		hv.tv1.setText(lb.getTitle());
		hv.tv1.setSingleLine(true);
		hv.tv1.setBackgroundColor(Color.argb(120, 0, 0, 0));
		mImageFetcher.loadImage(Constants.SERVER_URL + lb.getImage(), hv.iv);
		
		return convertView;
	}

}
