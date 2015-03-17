package com.seedsoft.ykt.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.bean.CardItemBean;

public class CardAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<CardItemBean> cardItemBeans;
	public CardAdapter(Context context,ArrayList<CardItemBean> cardItemBeans){
		this.context = context;
		this.cardItemBeans = cardItemBeans;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cardItemBeans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cardItemBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	class HoldView{
		ImageView iv;
		TextView tv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HoldView hv = null;
		if(convertView == null){
			hv = new HoldView();
			convertView = LayoutInflater.from(context).inflate(R.layout.a_card_gridview_item, null);
			hv.iv = (ImageView) convertView.findViewById(R.id.item_image);
			hv.tv = (TextView) convertView.findViewById(R.id.item_title);
			convertView.setTag(hv);
		}
		hv = (HoldView) convertView.getTag();
		CardItemBean cb = cardItemBeans.get(position);
		hv.iv.setImageResource(cb.getResource());
		hv.tv.setText(cb.getTitle());
		
		return convertView;
	}

}
