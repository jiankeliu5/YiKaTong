package com.seedsoft.ykt.push;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.bean.PushMsg;
import com.seedsoft.ykt.util.Constants;
import com.seedsoft.ykt.util.Util;


public class PushMsgListActivity extends Activity {

	private ListView listView;
	private List<PushMsg> list = null;
	
	private String pre_title;
	private String cur_title;
	private TextView pre_tv;// 上层标题
	private TextView title_tv;// 本层标题
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_msg_list);
		
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
		
//		pre_title = getIntent().getStringExtra("PRE_TITLE");
//		cur_title = getIntent().getStringExtra("CUR_TITLE");
		pre_tv.setText("一卡通");
		title_tv.setText("推送信息");
		
		listView = (ListView) findViewById(R.id.push_msg_listview);
		String fileName = Util.getSDCardPath(this)+Constants.DOWNLOAD_PATH + Constants.separator + Constants.PUSH_FILE;
		String content = Util.getFileRead(fileName);
		if(content!=null&&content.length()>0){
			list = new ArrayList<PushMsg>();
			String [] msgList = content.split("<%%>");
			for(int i=0; i<msgList.length;i++){
				
				String msg=msgList[i];
				String [] md = msg.split("<@@>");
				
				PushMsg pm=new PushMsg();
				pm.setTitle(md[0]);
				pm.setTime(md[1]);
				pm.setContent(md[2]);
				list.add(pm);
				pm=null;
			}
			
			listView.setAdapter(new MyAdatper());
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String title = list.get(position).getTitle();
					String content = list.get(position).getContent();
					startActivity(new Intent(PushMsgListActivity.this,PushInfoActivity.class).putExtra("msg_title", title).putExtra("msg_content", content));
					overridePendingTransition(R.anim.hold_enter, R.anim.hold_enter);
				}
			});
			
		}else{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
		
	}
	public void login(View v) {
		onBackPressed();
	}
	class MyAdatper extends BaseAdapter{

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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = getLayoutInflater().inflate(R.layout.push_msg_list_item, null);
			TextView tv1 = (TextView) convertView.findViewById(R.id.push_listitem_title);
			TextView tv2 = (TextView) convertView.findViewById(R.id.push_listitem_time);
			tv1.setText(list.get(position).getTitle());
			tv2.setText(list.get(position).getTime());
			
			return convertView;
		}

		
		
	}
}
