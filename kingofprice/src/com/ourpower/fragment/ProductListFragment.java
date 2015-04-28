package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ourpower.adapter.ProductDetailsAdapter;
import com.ourpower.adapter.ProductListAdapter;
import com.ourpower.kingofprice.MainActivity;
import com.ourpower.kingofprice.R;
import com.ourpower.object.ProductListObject;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class ProductListFragment extends ListFragment {
	private final String TAG = "ProductListFragment";
	private OnProductItemClickListener mListener;
	static ListView listview_productlist;
	private static Context mContext;
	public static ProductListObject[] mProductlist;
	public static ProductListAdapter adapter;
	private HttpDataTransmitter mTransmitter;
	private HttpValue httpvalue;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_productlist, container, false);
		listview_productlist = (ListView) v.findViewById(android.R.id.list);
		return v;
	}

	private void initview(View v) {
		listview_productlist = (ListView) v.findViewById(android.R.id.list);
		mProductlist = httpvalue.getProductListInfo();
		adapter = new ProductListAdapter(mContext, mProductlist);

		listview_productlist.setAdapter(adapter);
		listview_productlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// proItemListener.OnItemClickListener(position);
				Log.d("Product", "itemclick");
				if (!httpvalue.mIsLowOrder) {
					position = mProductlist.length - 1 - position;
				}
				String county = httpvalue.cityselectcounty;
				String region = httpvalue.cityselectregion;

				switch (HttpValue.PRODUCTLIST_MODE) {
				case HttpValue.PRODUCTLIST_MODE_SEARCH:
					if (county.length() > 0 || region.length() > 0) {
						httpvalue.setCommodityNVP(
								mProductlist[position].getProduct_id(), "",
								county, region);
					} else {
						httpvalue.setCommodityNVP(
								mProductlist[position].getProduct_id(), "", "",
								"");
					}
					break;
				case HttpValue.PRODUCTLIST_MODE_ALLLIST:
					if (county.length() > 0 || region.length() > 0) {
						httpvalue.setCommodityNVP(
								mProductlist[position].getProduct_id(),
								httpvalue.ProductListchannelid, county, region);
					} else {
						httpvalue.setCommodityNVP(
								mProductlist[position].getProduct_id(),
								httpvalue.ProductListchannelid, "", "");
					}
					break;
				default:
					break;
				}

				Log.d("", "Ernest productlistfragment channelid"
						+ httpvalue.ProductListchannelid);
				mListener.OnItemClickListener(position);
				httpvalue.ProductListproductid = mProductlist[position]
						.getProduct_id();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		ActionBar actionBar = getActivity().getActionBar();
		mContext = getActivity();
		mTransmitter = new HttpDataTransmitter(mContext);
		httpvalue = HttpValue.getInstance(mContext);
		switch (HttpValue.PRODUCTLIST_MODE) {
		case HttpValue.PRODUCTLIST_MODE_ALLLIST:
			mTransmitter.getProductList(HttpValue.PRODUCTLIST_MODE_ALLLIST);
			break;
		case HttpValue.PRODUCTLIST_MODE_SEARCH:
			mTransmitter.getProductList(HttpValue.PRODUCTLIST_MODE_SEARCH);
			break;
		}
		mProductlist = httpvalue.getProductListInfo();
		this.setListAdapter(new ProductListAdapter(mContext, mProductlist));
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.d("", "Ernest" + "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.d("", "Ernest" + "onResume");
		MainActivity.current_page = MainActivity.PRODUCTLIST_PAGE;
		super.onResume();
	}

	public interface OnProductItemClickListener {
		public void OnItemClickListener(int resourceId);

	}

	/** Activity?�叫Fragment??要�?訴Activity要實作OnProductItemClickListener */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnProductItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnProductItemClickListener");
		}
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu()");
		super.onCreateOptionsMenu(menu, inflater);
		setTitle();
	}

	private void setTitle() {
		Log.d(TAG, "setTitle()");
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.show();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.drawer_menu_item_store_selection);
		Log.d(TAG, "getTitle() = " + actionBar.getTitle());
	}

	public static void AdapterRefresh() {
		adapter = new ProductListAdapter(mContext, mProductlist);
		listview_productlist.setAdapter(adapter);
	}
	public void AdapterRefresh1(){
		Log.d("", "Ernest AdapterRefresh1");
	}
	public void onListItemClick(ListView parent, View v, 
            int position, long id) {
		Log.d("Product", "onListItemClick");
		if (!httpvalue.mIsLowOrder) {
			position = mProductlist.length - 1 - position;
		}
		String county = httpvalue.cityselectcounty;
		String region = httpvalue.cityselectregion;

		switch (HttpValue.PRODUCTLIST_MODE) {
		case HttpValue.PRODUCTLIST_MODE_SEARCH:
			if (county.length() > 0 || region.length() > 0) {
				httpvalue.setCommodityNVP(
						mProductlist[position].getProduct_id(), "",
						county, region);
			} else {
				httpvalue.setCommodityNVP(
						mProductlist[position].getProduct_id(), "", "",
						"");
			}
			break;
		case HttpValue.PRODUCTLIST_MODE_ALLLIST:
			if (county.length() > 0 || region.length() > 0) {
				httpvalue.setCommodityNVP(
						mProductlist[position].getProduct_id(),
						httpvalue.ProductListchannelid, county, region);
			} else {
				httpvalue.setCommodityNVP(
						mProductlist[position].getProduct_id(),
						httpvalue.ProductListchannelid, "", "");
			}
			break;
		default:
			break;
		}

		Log.d("", "Ernest productlistfragment channelid"
				+ httpvalue.ProductListchannelid);
		mListener.OnItemClickListener(position);
		httpvalue.ProductListproductid = mProductlist[position]
				.getProduct_id();
	}

}
