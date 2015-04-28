package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ourpower.adapter.ParityDetailAdapter;
import com.ourpower.kingofprice.MainActivity;
import com.ourpower.kingofprice.R;
import com.ourpower.object.TotalAlmountObject;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class ParityDetailsFragment extends Fragment{
	
	private Context context;
	ListView lv_detaillist;
	ParityDetailAdapter adapter;
	TextView stroeName;
	TotalAlmountObject totalAlmount;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_paritydetails, container, false);
		initView(view);
		return view;
	}
	
	private void initView(View view) {
		stroeName = (TextView) view.findViewById(R.id.pd_tv_stroename);
		lv_detaillist = (ListView) view.findViewById(R.id.pd_lv_detaillist);
		if(totalAlmount != null){
			adapter = new ParityDetailAdapter(context , totalAlmount);
			lv_detaillist.setAdapter(adapter);
			//開頭名稱
			stroeName.setText(totalAlmount.getMarket_name());
		}else {
			//空值處理
		}
		
//		lv_detaillist.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent i = getActivity().getIntent();
//				Log.d("ooxx" , "iiii: " + i.getStringExtra("0"));
//				Log.d("ooxx" , "iiii: " + ((MainActivity)getActivity()).aa);
//				
//			}
//		});
//		stroeName.setText(getActivity().getIntent().getStringExtra("store"));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		setHasOptionsMenu(true);
		HttpValue httpValue = HttpValue.getInstance(context);
		HttpDataTransmitter hdt = new HttpDataTransmitter(context);
		hdt.getTotalAlmount();
		totalAlmount = httpValue.getTotalAlmountInfo();
		
	}

	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
		actionBar.setTitle(R.string.pd_title);
	}
	

	
	
}