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
import android.widget.ListView;
import android.widget.Toast;

import com.ourpower.adapter.StoreParityAdapter;
import com.ourpower.kingofprice.R;
import com.ourpower.object.TotalAlmountListObject;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class StoreParityFragment  extends Fragment{
	private OnStoreParityClickListener mListener;
	private Context context;
	private ListView lv_parity;
	private StoreParityAdapter adapter;
	private TotalAlmountListObject[] totalAlmount ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_storeparity, container, false);
		initview(view);
		return view;
	}
	

	private void initview(View view) {
		lv_parity = (ListView) view.findViewById(R.id.sp_paritylist);
		if(totalAlmount != null && totalAlmount.length != 0){
			Log.d("ooxx" , "have totalAlmount");
			adapter = new StoreParityAdapter(context, totalAlmount);
			lv_parity.setAdapter(adapter);
		}else {
			Log.d("ooxx" , "no totalAlmount");
			Toast.makeText(context, getString(R.string.sp_date_error), Toast.LENGTH_SHORT).show();
		}
		View emptyView = getActivity().getLayoutInflater().inflate(R.layout.storeparity_empty_view,null);
		((ViewGroup)lv_parity.getParent()).addView(emptyView); 
		lv_parity.setEmptyView(emptyView);
		 
				
		lv_parity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Intent i = getActivity().getIntent();
//				i.putExtra("store", storename[position]);
				HttpValue httpvValue = HttpValue.getInstance(context);
				httpvValue.setTotalAlmountNVP(totalAlmount[position].getMarketId());
				mListener.OnStoreParityClick(position);
				
			}
		});
		
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		context = getActivity();
		getValue();
	}

	
	private void getValue() {
		HttpDataTransmitter hdt = new HttpDataTransmitter(context);
		HttpValue httpValue = HttpValue.getInstance(context);
		hdt.getTotalAlmountList();
		totalAlmount = httpValue.getTotalAlmountListInfo();
//		Log.d("ooxx" ,"totalAlmount : " + totalAlmount);
//		for (int i = 0; i < totalAlmount.length; i++) {
//			Log.d("ooxx" ,"totalAlmount : " + totalAlmount[i].getMarketName() +" "+ totalAlmount[i].getTotalAmount());
//		}
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
		actionBar.setTitle(R.string.sp_store_title);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnStoreParityClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnAboutUsClickListener");
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public OnClickListener getOnClickListener(final int resourceId) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.OnStoreParityClick(resourceId);
			}
		};
	}

	public interface OnStoreParityClickListener {
		public void OnStoreParityClick(int resourceId);
	}

	
}