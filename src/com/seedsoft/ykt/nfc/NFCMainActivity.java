/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.seedsoft.ykt.nfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.nfc.ui.AboutPage;
import com.seedsoft.ykt.nfc.ui.MainPage;
import com.seedsoft.ykt.nfc.ui.NfcPage;
import com.seedsoft.ykt.nfc.ui.Toolbar;
import com.seedsoft.ykt.nfc.util.NfcManager;

public class NFCMainActivity extends Activity {

	private TextView pre_tv;// 上层标题
	private TextView title_tv;// 本层标题

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		// getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.db_bj));
		setContentView(R.layout.a_nfc_activity);

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
		

		pre_tv.setText(getIntent().getStringExtra("PRE_TITLE"));
		title_tv.setText(getIntent().getStringExtra("CUR_TITLE"));

		initViews();

		nfc = new NfcManager(this);

		onNewIntent(getIntent());
	}

	public void login(View v) {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		if (isCurrentPage(SPEC.PAGE.ABOUT))
			loadDefaultPage();
		else if (safeExit)
			super.onBackPressed();
	}

	@Override
	public void setIntent(Intent intent) {
		if (NfcPage.isSendByMe(intent))
			loadNfcPage(intent);
		else if (AboutPage.isSendByMe(intent))
			loadAboutPage();
		else
			super.setIntent(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		nfc.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		nfc.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			if (nfc.updateStatus())
				loadDefaultPage();
			board.postDelayed(new Runnable() {
				public void run() {
					safeExit = true;
				}
			}, 800);
		} else {
			safeExit = false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadDefaultPage();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (!nfc.readCard(intent, new NfcPage(this)))
			loadDefaultPage();
	}

	public void onSwitch2DefaultPage(View view) {
		if (!isCurrentPage(SPEC.PAGE.DEFAULT))
			loadDefaultPage();
	}

	public void onSwitch2AboutPage(View view) {
		if (!isCurrentPage(SPEC.PAGE.ABOUT))
			loadAboutPage();
	}

	public void onCopyPageContent(View view) {
		toolbar.copyPageContent(getFrontPage());
	}

	public void onSharePageContent(View view) {
		toolbar.sharePageContent(getFrontPage());
	}

	private void loadDefaultPage() {
		toolbar.show(null);

		TextView ta = getBackPage();

		resetTextArea(ta, SPEC.PAGE.DEFAULT, Gravity.CENTER);
		ta.setText(MainPage.getContent(this));

		board.showNext();
	}

	private void loadAboutPage() {
		toolbar.show(R.id.btnBack);

		TextView ta = getBackPage();

		resetTextArea(ta, SPEC.PAGE.ABOUT, Gravity.LEFT);
		ta.setText(AboutPage.getContent(this));

		board.showNext();
	}

	private void loadNfcPage(Intent intent) {
		final CharSequence info = NfcPage.getContent(this, intent);

		TextView ta = getBackPage();

		if (NfcPage.isNormalInfo(intent)) {
			toolbar.show(R.id.btnCopy, R.id.btnShare, R.id.btnReset);
			resetTextArea(ta, SPEC.PAGE.INFO, Gravity.LEFT);
		} else {
			toolbar.show(R.id.btnBack);
			resetTextArea(ta, SPEC.PAGE.INFO, Gravity.CENTER);
		}

		ta.setText(info);

		board.showNext();
	}

	private boolean isCurrentPage(SPEC.PAGE which) {
		Object obj = getFrontPage().getTag();

		if (obj == null)
			return which.equals(SPEC.PAGE.DEFAULT);

		return which.equals(obj);
	}

	private void resetTextArea(TextView textArea, SPEC.PAGE type, int gravity) {

		((View) textArea.getParent()).scrollTo(0, 0);

		textArea.setTag(type);
		textArea.setGravity(gravity);
	}

	private TextView getFrontPage() {
		return (TextView) ((ViewGroup) board.getCurrentView()).getChildAt(0);
	}

	private TextView getBackPage() {
		return (TextView) ((ViewGroup) board.getNextView()).getChildAt(0);
	}

	private void initViews() {
		board = (ViewSwitcher) findViewById(R.id.switcher);

		Typeface tf = ThisApplication.getFontResource(R.string.font_oem1);
		TextView tv = (TextView) findViewById(R.id.txtAppName);
		tv.setTypeface(tf);

		tf = ThisApplication.getFontResource(R.string.font_oem2);

		tv = getFrontPage();
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		tv.setTypeface(tf);

		tv = getBackPage();
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		tv.setTypeface(tf);

		toolbar = new Toolbar((ViewGroup) findViewById(R.id.toolbar));
	}

	private ViewSwitcher board;
	private Toolbar toolbar;
	private NfcManager nfc;
	private boolean safeExit;
}
