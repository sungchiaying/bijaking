package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ourpower.kingofprice.R;

public class ManufacturerModifyItemsFragment extends Fragment {
	private final String TAG = "ManufacturerModifyItemsFragment";

	private OnMModifyItemClickListener mListener;
	Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		LayoutInflater Goodsinflater = LayoutInflater.from(context);
		View view = Goodsinflater.inflate(R.layout.fragment_manufacturermodifyitems, null);
		initView(view);
		return view;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnMModifyItemClickListener) activity;
		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnLoginOptionsSelectedListener");
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu()");
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.global, menu);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.show();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		LayoutInflater iteminflater = LayoutInflater.from(context);
		View v = iteminflater.inflate(R.layout.actionbar_center_title, null);
		TextView title = (TextView) v.findViewById(R.id.actionbar_title);
		title.setText(getString(R.string.manufacturermodify_title));
		ActionBar.LayoutParams alp = new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, 
	            LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		v.setLayoutParams(alp);
		actionBar.setCustomView(v);
	}
	
	private void initView(View view) {
		Button delete = (Button) view.findViewById(R.id.mmi_delete);
		Button save = (Button) view.findViewById(R.id.mmi_save);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.OnMModifyItemClick(R.id.mmi_delete);
			}
		});
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.OnMModifyItemClick(R.id.mmi_save);
			}
		});
	}
	

	public interface OnMModifyItemClickListener {
		public void OnMModifyItemClick(int resourceId);
	}

}