package com.seedsoft.ykt.adpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.bean.LinkBean;

public class ComTextAdapter extends BaseAdapter{
	
	Context ctx;
	List<LinkBean> list ;
	LayoutInflater lin;
	public ComTextAdapter(Context ctx,List<LinkBean> list ){
		this.ctx = ctx;
		this.list = list;
		this.lin = lin.from(ctx);
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
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		holdView hv = null;
		if(convertView == null){
			hv = new holdView();
			convertView = lin.inflate(R.layout.com_text_adpter_item, null);
			convertView.setTag(hv);
		}
		hv = (holdView) convertView.getTag();
		LinkBean lb = list.get(position);
		hv.tv1.setText(lb.getTitle());
		
		return convertView;
	}

}
