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
		// ��ǰҳ������
		 pre_title = rootBean.getName();
		// ��ǰ���������
		 cur_title = rootBean.getFatherBeans().get(position).getName();
		Log.d(TAG, pre_title);
		Log.d(TAG, cur_title);
		Log.d(TAG, position+"");
//		
//		if(cur_title.contains("ͶƱ")){
//			new GoVoteSurvey().execute(Constants.VOTE_URL);
//			return;
//		}
//		if(cur_title.contains("�ʾ�")){
//			new GoVoteSurvey().execute(Constants.SURVEY_URL);
//			return;
//		}
		
		// ��ǰ����������
		ArrayList<FatherModuleBean> fatherModuleBeans = rootBean
				.getFatherBeans().get(position).getFatherModuleBeans();
		Intent intent = null;
		String actName = null;
		// ����������ͣ�����
		if (fatherModuleBeans.size() > 0) {
			String type = fatherModuleBeans.get(0).getType();
			switch (type) {
			case "activity":
				// ��ȡ��תactivity����Ϣ
				ActBean act = (ActBean) fatherModuleBeans.get(0).getObjects();
				actName = act.getPath();
				if(actName.contains("tel")){
					new AlertDialog.Builder(context)
	    			.setTitle("��Ϣ")
	    			.setMessage("�Ƿ�������ͨһ��ͨ�ͷ����ߣ�")
	    			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	    				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) {
	    					// TODO Auto-generated method stub
	    					context.startActivity(new Intent(Intent.ACTION_CALL,
	    							Uri.parse("tel:"+Constants.TEL_NUM1)));
	    					
	    				}
	    			}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	    				
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
				if(cur_title.contains("ͶƱ")){
					intent.putExtra("URL", Constants.VOTE_URL);
				}
				if(cur_title.contains("�ʾ�")){
					intent.putExtra("URL", Constants.SURVEY_URL);
				}
				break;
			case "html5page":
				// ��ȡ��תhtml5page����Ϣ
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
