package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ourpower.adapter.ManufacturerItemAdapter;
import com.ourpower.kingofprice.R;
import com.ourpower.tool.HttpDataTransmitter;

public class ManufacturerItemFragment  extends Fragment {
	private final String TAG = "ManufacturerLoginGoodsInFragment";
	private OnMItemClickListener mListener;
	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		LayoutInflater Goodsinflater = LayoutInflater.from(context);
		View view = Goodsinflater.inflate(R.layout.fragment_manufactureritem, null);
		initView(view);
		return view;
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
			mListener = (OnMItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMItemClickListener");
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.global, menu);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.show();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		LayoutInflater iteminflater = LayoutInflater.from(context);
		View v = iteminflater.inflate(R.layout.actionbar_center_title, null);
		TextView title = (TextView) v.findViewById(R.id.actionbar_title);
		title.setText("品項選擇");
		ActionBar.LayoutParams alp = new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, 
	            LayoutParams.WRAP_CONTENT ,Gravity.CENTER);
		v.setLayoutParams(alp);
		actionBar.setCustomView(v);
	}
	
	private void initView(View view) {
		ListView listItem = (ListView) view.findViewById(R.id.manufacturer_List_Item);
		ManufacturerItemAdapter adapter = new ManufacturerItemAdapter(context);
		listItem.setAdapter(adapter);
	}

	public interface OnMItemClickListener {
		public void onMItemClick(int resourceId);
	}

}