package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ourpower.kingofprice.BaseActivity;
import com.ourpower.kingofprice.R;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class MemberCenterModifyFragment extends Fragment{
	
	private OnMemberCenterModifyClickListener mListener;
	private Context context;
	Button btn_send;
	EditText et_name;
	EditText et_email;
	EditText et_oldpass1;
	EditText et_oldpass2;
	EditText et_newpass;
	private OnMemberNameChangeListener mOnMemberNameChangeListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View view = inflater.inflate(R.layout.fragment_membercenter_modify, container, false);
		initView(view);
		return view;
	}
	
	private void initView(View view) {
		btn_send = (Button) view.findViewById(R.id.mcm_btn_send);
		et_name = (EditText) view.findViewById(R.id.mcm_et_name);
		et_email = (EditText) view.findViewById(R.id.mcm_et_email);
		et_oldpass1 = (EditText) view.findViewById(R.id.mcm_et_oldpass1);
		et_oldpass2 = (EditText) view.findViewById(R.id.mcm_et_oldpass2);
		et_newpass = (EditText) view.findViewById(R.id.mcm_et_newpass);
		
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HttpDataTransmitter hdt = new HttpDataTransmitter(context);
				HttpValue httpValue = HttpValue.getInstance(context);
				if (et_oldpass2.getText().toString().equals(et_newpass.getText().toString())) {
					httpValue.setEditProfileNVP(httpValue.getServiceID(), et_oldpass1.getText().toString(),
							et_newpass.getText().toString(), et_email.getText().toString(), et_name.getText().toString());
					
					if (((BaseActivity) getActivity()).isNetworkAlive() &&
							hdt.editProfile()) {
						Toast.makeText(context, getString(R.string.membercentermodify_success_change), Toast.LENGTH_SHORT).show();
						mListener.OnMemberCenterModifyClick(R.id.mcm_btn_send);
						mOnMemberNameChangeListener.onMemberNameChange();
					} else {
						Toast.makeText(context, getString(R.string.membercentermodify_false), Toast.LENGTH_SHORT).show();
						
					}
				}else {
					Toast.makeText(context, getString(R.string.membercentermodify_pass_error), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnMemberCenterModifyClickListener) activity;
			mOnMemberNameChangeListener = (OnMemberNameChangeListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMemberCenterModifyClickListener");
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
		actionBar.setTitle(R.string.mcmodify_title);
	}

	public OnClickListener getOnClickListener(final int resourceId) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.OnMemberCenterModifyClick(resourceId);
			}
		};
	}

	public interface OnMemberCenterModifyClickListener {
		public void OnMemberCenterModifyClick(int resourceId);
	}
	
	public interface OnMemberNameChangeListener{
		void onMemberNameChange();
	}
}