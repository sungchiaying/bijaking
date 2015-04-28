package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.text.NoCopySpan.Concrete;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ourpower.kingofprice.R;
import com.ourpower.object.MemberProfile;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class MemberCenterFragment extends Fragment{
	
	private OnMemberCenterClickListener mListener;
	private Context context;
	Button btn_gomodify;
	TextView tv_name;
	TextView tv_phone;
	TextView tv_email;
	TextView tv_press;
	MemberProfile mNumberProfile;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_membercenter, container, false);
		initView(view);
		return view;
	}
	
	private void initView(View view) {
		btn_gomodify = (Button) view.findViewById(R.id.mc_btn_gomodify);
		tv_name = (TextView) view.findViewById(R.id.mc_tv_name);
		tv_phone = (TextView) view.findViewById(R.id.mc_tv_phone);
		tv_email = (TextView) view.findViewById(R.id.mc_tv_email);
		tv_press = (TextView) view.findViewById(R.id.mc_tv_press);
		btn_gomodify.setOnClickListener(getOnClickListener(R.id.mc_btn_gomodify));
		
		tv_name.setText(mNumberProfile.getName());
		tv_phone.setText(mNumberProfile.getCellphone());
		tv_email.setText(mNumberProfile.getEmail());
//		int PasswordStart = mNumberProfile.getPassword().length();
		tv_press.setText("");
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		setHasOptionsMenu(true);
		HttpDataTransmitter hdt = new HttpDataTransmitter(context);
		HttpValue httpValue = HttpValue.getInstance(context);
		hdt.getProfile();
		if (httpValue.getProfileInfo() != null) {
			mNumberProfile = httpValue.getProfileInfo();
		}else {
			mNumberProfile = new MemberProfile();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnMemberCenterClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMemberCenterClickListener");
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
//		menu.clear();
//		inflater.inflate(R.menu.global, menu);
//		ActionBar actionBar = getActivity().getActionBar();
//		actionBar.show();
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
//		LayoutInflater iteminflater = LayoutInflater.from(context);
//		View v = iteminflater.inflate(R.layout.actionbar_center_title, null);
//		TextView title = (TextView) v.findViewById(R.id.actionbar_title);
//		title.setText(R.string.mc_title);
//		ActionBar.LayoutParams alp = new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, 
//	            LayoutParams.WRAP_CONTENT ,Gravity.CENTER);
//		v.setLayoutParams(alp);
//		actionBar.setCustomView(v);
		menu.clear();
		setTitle();
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	private void setTitle() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.show();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.mc_title);
	}
	

	public OnClickListener getOnClickListener(final int resourceId) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.OnMemberCenterClick(resourceId);
			}
		};
	}

	public interface OnMemberCenterClickListener {
		public void OnMemberCenterClick(int resourceId);
	}
	
	
}
