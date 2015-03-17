package com.seedsoft.ykt.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.seedsoft.ykt.activity.R;

public class ProgressDialogExtend{

	public Dialog dialog;
	public String temp = "������...";
	public static ProgressDialogExtend pde;
	public static ProgressDialogExtend CreateSiglePDE(Context context,String title){
//		if(pde == null){
//			pde = new ProgressDialogExtend(context);
//		}
//		return pde;
		return new ProgressDialogExtend(context,title);
	}
	
	public ProgressDialogExtend(final Context context,String title){
		dialog = new Dialog(context,R.style.new_circle_progress);
		Window window = dialog.getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);//��л�ϵۣ������ҽ���˶�����������������Ӵ˴��룬����ڽ����������Ϸ��ճ�һ���ֿռ䡣
		window.setContentView(R.layout.custom_progress_dialog);
		TextView info = (TextView) window.findViewById(R.id.custom_progress_dialog_info);
		if(title == null)title = temp;
		info.setText(title);
		dialog.setCancelable(true);//���ý������Ƿ���԰����˼�ȡ��
		dialog.setCanceledOnTouchOutside(false);//���ý������Ƿ���Ե�������ط�ȡ��
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Toast toast = Toast.makeText(context, "���ٲ�����Ŷ!", Toast.LENGTH_LONG);
//				toast.getView().setBackgroundResource(R.drawable.red_toast_bg);
				toast.getView().setPadding(20, 10, 20, 10);
				toast.show();
				}
		});		
	}
	public void show(){
		if(dialog.isShowing()) return;
		dialog.show();
	}
	public void dismiss(){
		dialog.dismiss();
	}	

}
