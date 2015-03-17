package com.seedsoft.ykt.activity;

import com.seedsoft.ykt.widget.LoopProgressBar;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class IdeaActivity extends BaseActivity {

	private EditText edit;

	private AutoCompleteTextView email;

	private LoopProgressBar bar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea_activity);
		init();
		initTitle(R.string.more_idea);
		setLoginImage(R.drawable.pre_btn, 0, 0, 0, R.string.get_back);
	}

	private void init() {
		email = (AutoCompleteTextView) findViewById(R.id.email);
		edit = (EditText) findViewById(R.id.edit);
		bar = (LoopProgressBar) findViewById(R.id.send_progress_bar);
	}

	/**
	 * 发送反馈
	 */
	public void send(View v) {
		String emailText = email.getText().toString().trim();
		String editText = edit.getText().toString().trim();
		if (emailText == null || emailText.equals("") || editText == null
				|| editText.equals("")) {
			Toast.makeText(getApplication(), "您输入的的信息为空，请您重新输入！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		email.setFocusable(false);
		edit.setFocusable(false);
		v.setClickable(false);
		bar.setColor(getResources().getColor(R.color.green));
		bar.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bar.stopBar();
	}
}
