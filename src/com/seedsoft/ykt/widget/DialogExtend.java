package com.seedsoft.ykt.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;

public abstract class DialogExtend{

	private TextView title1;
	private TextView message1;
	private TextView ok;
	private TextView cancle;
	private Dialog dialog1;

	/**
	 * @name 自定义对话框
	 * @param context,title,message
	 * */
	public DialogExtend(Context context,String title,String message){		
		 try {
			dialog1 = new Dialog(context);
			 dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
			 //you can move the dialog, so that is not centered
			 // dialog.getWindow().getAttributes().y = 50; //50 should be based on density
			 Window v = dialog1.getWindow();
//	     DisplayMetrics outMetrics = new DisplayMetrics();
//	     v.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
//	     v.getAttributes().y = outMetrics.heightPixels-v.getAttributes().height;
			 v.setContentView(R.layout.custom_dialog);
//	     View v = LayoutInflater.from(context).inflate(R.layout.alert_dialog_message, null,true);
			 title1 = (TextView) v.findViewById(R.id.title);
			 message1 = (TextView) v.findViewById(R.id.message);
			 ok = (TextView) v.findViewById(R.id.ok);
			 cancle = (TextView) v.findViewById(R.id.cancle);	
			 title1.setText(title);
			 message1.setText(message);	    
			 cancle.setOnClickListener(new View.OnClickListener() {
				 
				 @Override
				 public void onClick(View v) {
					 // TODO Auto-generated method stub
					setCancle(dialog1);
				 }
			 });
			 ok.setOnClickListener(new View.OnClickListener() {
				 
				 @Override
				 public void onClick(View v) {
					 // TODO Auto-generated method stub
					 setOK(dialog1);
				 }
			 });
			 
//	     dialog1.setContentView(v);
			 dialog1.setCancelable(true);
			 dialog1.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected abstract void setOK(Dialog dialog);
	protected abstract void setCancle(Dialog dialog);
	
}
