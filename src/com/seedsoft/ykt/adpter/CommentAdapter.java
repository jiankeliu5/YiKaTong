package com.seedsoft.ykt.adpter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.bean.CommentBean;
import com.seedsoft.ykt.bitmap.util.ImageFetcher;

public class CommentAdapter extends BaseAdapter {
	
	private List<CommentBean> lscb;
	private LayoutInflater lin;
	private ImageFetcher mImageFetcher;
	private HoldView hv;
	private Date date;
	private SimpleDateFormat sdf;
	private String curTime;
	private String curDay;
	private String curHour;
	private String curMini;
	
	
	public CommentAdapter(Context ctx,List<CommentBean> lscb,ImageFetcher mImageFetcher){		
		this.lscb = lscb;
		this.lin = lin.from(ctx);
		this.mImageFetcher = mImageFetcher;
		this.date = new Date(System.currentTimeMillis());
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.curTime = sdf.format(date);
		this.curDay = curTime.substring(0, 10);
		this.curHour = curTime.substring(11, 13);
		this.curMini = curTime.substring(14);
		System.out.println("--curtime --"+curTime);
		System.out.println("--curtime --"+curDay);
		System.out.println("--curtime --"+curHour);
		System.out.println("--curtime --"+curMini);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lscb.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lscb.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class HoldView{
		TextView name;
//		TextView num;
		TextView time;
		TextView content;
		ImageView icon;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView == null){
			hv = new HoldView();
			convertView = lin.inflate(R.layout.com_coment_list_item, null);
			hv.icon = (ImageView) convertView.findViewById(R.id.comment_item_icon);
			hv.name = (TextView) convertView.findViewById(R.id.comment_item_name);
//			hv.num = (TextView) convertView.findViewById(R.id.comment_item_like_num);
			hv.time = (TextView) convertView.findViewById(R.id.comment_item_time);
			hv.content = (TextView) convertView.findViewById(R.id.comment_item_content);
			convertView.setTag(hv);
		}
		hv = (HoldView) convertView.getTag();
		CommentBean cb = lscb.get(position);
//		String num = cb.getLikeNUM();
		String time = cb.getCommentTime();
		
		String day = time.substring(0, 10);
		String hour = time.substring(11, 13);
		String mini = time.substring(14, 16);
		System.out.println("--time--"+day);
		System.out.println("--time--"+hour);
		System.out.println("--time--"+mini);
//		if(num == null || num.equals("")) num = "0";		
//		hv.num.setText(num);
		String temp = null;
		if(day.equals(curDay)){
			if( hour.equals(curHour)){
				temp = (Integer.valueOf(curMini)-Integer.valueOf(mini))+"分钟前";			
			}else{
				temp = (Integer.valueOf(curHour)-Integer.valueOf(hour))+"小时前";
			}
		}else{
			temp = time;
		}
		hv.time.setText(temp);
		hv.content.setText(cb.getCommentContent());
		hv.name.setText(cb.getAuthor());
		if(cb.getIconURL() != null && !cb.getIconURL().equals("")){
			mImageFetcher.loadImage(cb.getIconURL(), hv.icon);
		}
		
		
		return convertView;
	}

}
