package com.seedsoft.ykt.adpter;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.seedsoft.ykt.fragment.CardFragment;
import com.seedsoft.ykt.fragment.MineFragment;
import com.seedsoft.ykt.fragment.MoreFragment;
import com.seedsoft.ykt.widget.IconPagerAdapter;

public class BaseFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {

	private String[] content = null;

	private int[] icons = null;

	private SharedPreferences configuration;

	private int position = 0;

	public BaseFragmentAdapter(FragmentManager fragmentManager,
			String[] content, int[] icons, SharedPreferences configuration) {
		super(fragmentManager);
		// TODO Auto-generated constructor stub
		this.content = content;
		this.icons = icons;
		this.configuration = configuration;
	}

	// // 从服务器端解析出底部导航的大类别名称，保存在CONTENT[]数组中。
	// protected static final String[] CONTENT = new String[] { "一卡通", "商户",
	// "我的",
	// "更多" };
	// protected static final int[] ICONS = new int[] {
	// R.drawable.perm_group_card,
	// R.drawable.perm_group_sh, R.drawable.perm_group_yh,
	// R.drawable.perm_group_gd };

	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;

		String title = content[position];
		switch (position) {
		case 0:
			fragment = new CardFragment(title);
			Log.i("xingwei", "item=" + 0);
			break;

		case 1:
			fragment = new CardFragment(title);
			Log.i("xingwei", "item=" + 1);
			break;

		case 2:
			fragment = new MineFragment(title, configuration);
			Log.i("xingwei", "item=" + 2);
			break;

		case 3:
			fragment = new MoreFragment(configuration, title);
			Log.i("xingwei", "item=" + 3);
			break;
		}
		return fragment;
	}

	public Fragment getFragment(int itme) {

		return getItem(itme);
	}

	@Override
	public int getCount() {
		return icons.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return content[position % content.length];
	}

	@Override
	public int getIconResId(int index) {
		this.position = index;
		return icons[index];
	}
}