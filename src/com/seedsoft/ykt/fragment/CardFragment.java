package com.seedsoft.ykt.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.adpter.CardAdapter;
import com.seedsoft.ykt.bean.CardItemBean;
import com.seedsoft.ykt.nfc.NFCMainActivity;

public class CardFragment extends Fragment {

	private GridView gv;
	private ArrayList<CardItemBean> cards;
	private CardAdapter ca;
	private String title;

	public CardFragment(String title) {
		this.title = title;
	}

	private ArrayList<CardItemBean> getData() {
		// TODO Auto-generated method stub
		cards = new ArrayList<CardItemBean>();
		CardItemBean cb = null;
		int[] res = { R.drawable.yecx, R.drawable.gsjs, R.drawable.kpzl,
				R.drawable.gwxz, R.drawable.cytp, R.drawable.wjdc,
				R.drawable.yjgw, R.drawable.yjkf, R.drawable.czwd };
		String[] titles = { "余额查询", "公司介绍", "卡片种类", "购卡须知", "参与投票", "问卷调查",
				"一键购物", "一键客服", "充值网点" };
		for (int i = 0; i < res.length; i++) {
			cb = new CardItemBean();
			cb.setTitle(titles[i]);
			cb.setResource(res[i]);
			cards.add(cb);
		}
		return cards;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.a_card_activity, null);
		TextView tv = (TextView) view.findViewById(R.id.top_title);
		tv.setText(title);

		gv = (GridView) view.findViewById(R.id.a_gridview);
		ca = new CardAdapter(getActivity(), getData());
		gv.setAdapter(ca);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					startActivity(new Intent(getActivity(),
							NFCMainActivity.class).putExtra("PRE_TITLE", title)
							.putExtra("CUR_TITLE",
									cards.get(position).getTitle()));
				}
			}
		});
		return view;
	}
}
