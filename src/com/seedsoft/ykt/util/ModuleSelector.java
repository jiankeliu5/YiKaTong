package com.seedsoft.ykt.util;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.seedsoft.ykt.activity.MainActivity;
import com.seedsoft.ykt.bean.ActBean;
import com.seedsoft.ykt.bean.FatherBean;
import com.seedsoft.ykt.bean.FatherModuleBean;
import com.seedsoft.ykt.bean.NewsBean;
import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.bean.VoteBean;

public class ModuleSelector {

	private static String TAG = "ModuleSelector:";
	private static String pre_title ;
	private static String cur_title ;
	private static Context context ;

	public static void go(Context cxt, RootBean rootBean, int position) {
		context = cxt;
		// 当前页面的类别
		 pre_title = rootBean.getName();
		// 当前方块的名字
		 cur_title = rootBean.getFatherBeans().get(position).getName();
		Log.d(TAG, pre_title);
		Log.d(TAG, cur_title);
		Log.d(TAG, position+"");
//		
//		if(cur_title.contains("投票")){
//			new GoVoteSurvey().execute(Constants.VOTE_URL);
//			return;
//		}
//		if(cur_title.contains("问卷")){
//			new GoVoteSurvey().execute(Constants.SURVEY_URL);
//			return;
//		}
		
		// 当前方块的组件库
		ArrayList<FatherModuleBean> fatherModuleBeans = rootBean
				.getFatherBeans().get(position).getFatherModuleBeans();
		Intent intent = null;
		String actName = null;
		// 根据组件类型，分流
		if (fatherModuleBeans.size() > 0) {
			String type = fatherModuleBeans.get(0).getType();
			switch (type) {
			case "activity":
				// 获取跳转activity的信息
				ActBean act = (ActBean) fatherModuleBeans.get(0).getObjects();
				actName = act.getPath();
				if(actName.contains("tel")){
					new AlertDialog.Builder(context)
	    			.setTitle("信息")
	    			.setMessage("是否立即拨通一卡通客服热线？")
	    			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	    				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) {
	    					// TODO Auto-generated method stub
	    					context.startActivity(new Intent(Intent.ACTION_CALL,
	    							Uri.parse("tel:"+Constants.TEL_NUM1)));
	    					
	    				}
	    			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
	    				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) {
	    					// TODO Auto-generated method stub
	    					dialog.dismiss();
	    				}
	    			}).show();
					return;
				}
				try {
					intent = new Intent(context, Class.forName(actName));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				if(cur_title.contains("投票")){
					intent.putExtra("URL", Constants.VOTE_URL);
				}
				if(cur_title.contains("问卷")){
					intent.putExtra("URL", Constants.SURVEY_URL);
				}
				break;
			case "html5page":
				// 获取跳转html5page的信息
				NewsBean nb = (NewsBean) fatherModuleBeans.get(0).getObjects();
				actName = "com.seedsoft.ykt.activity.WebViewActivity";
				try {
					intent = new Intent(context, Class.forName(actName));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				intent.putExtra("URL", Constants.SERVER_URL+nb.getUrl())
				.putExtra("title", nb.getTitle())
				.putExtra("id", nb.getId())
				.putExtra("location", nb.getLocation());
				break;			
			default:
				actName = "com.seedsoft.ykt.activity.ComActivity";
				try {
					intent = new Intent(context, Class.forName(actName));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				intent.putExtra("INDEX", position);
				break;
			}

			intent.putExtra("PRE_TITLE", pre_title);
			intent.putExtra("CUR_TITLE", cur_title);
			context.startActivity(intent);
		}

	}
	
//	static class GoVoteSurvey extends AsyncTask<String, Void, ArrayList<VoteBean>>{
//
//		@Override
//		protected ArrayList<VoteBean> doInBackground(String... params) {
//			// TODO Auto-generated method stub
//			return new ParseXMLUtil().parseVoteSurvey(params[0]);
//		}
//
//		@Override
//		protected void onPostExecute(ArrayList<VoteBean> result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			if(result != null && result.size() > 0){
//				Intent intent = null;
//				String actName = "com.seedsoft.ykt.activity.WebViewActivity";
//				try {
//					intent = new Intent(context, Class.forName(actName));
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
////				intent.putExtra("URL", Constants.SERVER_URL + result.getUrl());
//				intent.putExtra("PRE_TITLE", pre_title);
//				intent.putExtra("CUR_TITLE", cur_title);
//				context.startActivity(intent);
//			}
//		}
		
		
//	}
}
