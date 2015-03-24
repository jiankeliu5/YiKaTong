package com.seedsoft.ykt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.seedsoft.ykt.activity.MainActivity;
import com.seedsoft.ykt.activity.R;
import com.seedsoft.ykt.activity.WelcomeActivity;


@SuppressLint("ValidFragment")
public final class WelcomeFragment extends Fragment {
    
     WelcomeFragment fragment = null;
     int pos = 0;
     int[] ICONS = new int[] {
        R.drawable.guide_a,
        R.drawable.guide_b,
        R.drawable.guide_c,
        R.drawable.guide_d
    };
     public WelcomeFragment(int position) {
		this.pos = position;
	}
    public WelcomeFragment newInstance() {
    	
    	if(fragment == null){
    		fragment = new WelcomeFragment(pos);
    	}
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.welcome_fragment, null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.welcome_fragment_relativelayout);
    	relativeLayout.setBackgroundResource(ICONS[pos]);
    	Button start = (Button) view.findViewById(R.id.start_btn);
        if(pos==3){
        	start.setVisibility(View.VISIBLE);
        	start.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// TODO 提醒用户初次配置文件
					startActivity(new Intent(getActivity(), WelcomeActivity.class));
					getActivity().finish();
				}
			});
		}
        return view;
    }
}
