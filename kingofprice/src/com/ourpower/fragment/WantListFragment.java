package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Toast;

import com.ourpower.adapter.WantListAdapter;
import com.ourpower.kingofprice.R;
import com.ourpower.object.LocationList;
import com.ourpower.object.WishListObj;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class WantListFragment extends Fragment implements OnClickListener {
	private OnWantListClickListener mListener;
	private Context context;
	private Button btn_goparity, btn_seletarea;
	private ListView lv_wantlist;
	private WantListAdapter adapter;
	private LocationList[] mLocationList;
	private String[][] mZip;
	private String[] mLocation;
	// private HttpDataTransmitter mHttpDataTransmitter = new
	// HttpDataTransmitter(context);
	private String county;
	private String zipcode_name;
	private WishListObj[] wlo;
	private HttpDataTransmitter hdt;
	private HttpValue httpValue ;
//	private boolean isSeleteArea = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_want_list, container,
				false);
		initview(view);
		return view;
	}

	private void initview(View view) {
		lv_wantlist = (ListView) view.findViewById(R.id.wl_wantlist);
		btn_goparity = (Button) view.findViewById(R.id.wl_btn_goparity);
		btn_seletarea = (Button) view.findViewById(R.id.wl_btn_seletarea);
		// btn_goparity.setOnClickListener(getOnClickListener(R.id.wl_btn_goparity));
		btn_seletarea.setOnClickListener(this);
		btn_goparity.setOnClickListener(this);
		adapter = new WantListAdapter(context, wlo);

		lv_wantlist.setAdapter(adapter);
		btn_goparity.setClickable(false);
		btn_goparity.setAlpha(0.6f);
//		lv_wantlist.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				httpValue.setCommodityNVP(wlo[position].getProduct_id(), null, null, null);
//				mListener.OnWantListClick(position);
//			}
//		});
		lv_wantlist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int item = position;
				AlertDialog.Builder deleteDialog = new AlertDialog.Builder(
						context);
				deleteDialog.setTitle(Html.fromHtml("<font color='#33b5e5'>"
						+ getString(R.string.wl_deletedialog_deletelist)
						+ "</font>"));
				deleteDialog.setPositiveButton(
						getString(R.string.wl_deletedialog_delete),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								httpValue.setDeleteUserWishListNVP(wlo[item]
										.getProduct_id());
								hdt = new HttpDataTransmitter(context);
								if (hdt.getDeleteUserWishList()) {
									Toast.makeText(
											context,
											getString(R.string.wl_deletedialog_deleteseccess),
											Toast.LENGTH_SHORT).show();
									reflashAdapter();
								} else {
									Toast.makeText(
											context,
											getString(R.string.wl_deletedialog_deletefalse),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
				deleteDialog.setNegativeButton(
						context.getString(R.string.wl_deletedialog_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				deleteDialog.show();
				return true;
			}
		});

	}

	private void reflashAdapter() {
		hdt.getWantList();
		wlo = httpValue.getWantListInfo();
		adapter = new WantListAdapter(context, wlo);
		lv_wantlist.setAdapter(adapter);
		// adapter.notifyDataSetChanged();
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
		httpValue = HttpValue.getInstance(context);
		hdt.getWantList();
		hdt.getLocationList();
		wlo = httpValue.getWantListInfo();
		mLocationList = httpValue.getLocationListInfo();
		if (mLocationList != null || mLocationList.length != 0) {
			// 宣告陣列長度
			mZip = new String[mLocationList.length][];
			mLocation = new String[mLocationList.length];
			for (int i = 0; i < mLocationList.length; i++) {
				mZip[i] = new String[mLocationList[i].getZipCode().length];
			}
			// 拿縣市資料
			for (int i = 0; i < mLocationList.length; i++) {
				mLocation[i] = mLocationList[i].getName();
			}

			// 拿行政區資料
			for (int i = 0; i < mLocationList.length; i++) {
				for (int j = 0; j < mLocationList[i].getZipCode().length; j++) {
					mZip[i][j] = mLocationList[i].getZipCode()[j]
							.getZipcodeName();
				}
			}
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnWantListClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnWantListClickListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
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
		actionBar.setTitle(R.string.wl_title);
	}

	public OnClickListener getOnClickListener(final int resourceId) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.OnWantListClick(resourceId);
			}
		};
	}

	public interface OnWantListClickListener {
		public void OnWantListClick(int resourceId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wl_btn_seletarea:
			if (mLocationList == null || mLocationList.length == 0) {
				break;
			}
			picker();
			break;
		case R.id.wl_btn_goparity:
			// 沒有channel_id可以傳
			httpValue.setTotalAlmountListNVP("", county, zipcode_name);
			mListener.OnWantListClick(R.id.wl_btn_goparity);
			break;

		default:
			break;
		}
	}

	void picker() {
		LayoutInflater ll = LayoutInflater.from(context);
		View pricepicker = ll.inflate(R.layout.wantlist_citypicker, null);
		final NumberPicker np_city = (NumberPicker) pricepicker
				.findViewById(R.id.wl_np_city);
		final NumberPicker area = (NumberPicker) pricepicker
				.findViewById(R.id.wl_np_area);
		Log.d("Wayne", "mLocation.length = " + mLocation.length);
		for (int i=0; i<mLocation.length; i++) {
			Log.d("Wayne", "mLocation" + i + ":" + mLocation[i]);
		}
		np_city.setDisplayedValues(mLocation);
		np_city.setMaxValue(mLocation.length - 1);
		np_city.setValue(0);

		area.setDisplayedValues(mZip[0]);
		area.setMaxValue(mZip[0].length - 1);
		area.setValue(0);

		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

		dialog.setTitle(Html.fromHtml("<font color='#33b5e5'>"
				+ getString(R.string.wl_select_price) + "</font>"));
		dialog.setView(pricepicker);
		dialog.setPositiveButton(getString(R.string.wl_dialog_ok),
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, getString(R.string.wl_choose_location)+" "+mLocation[np_city.getValue()]+"-"+mZip[np_city.getValue()][area.getValue()], 3000).show();
				county = mLocation[np_city.getValue()];
				if (area.getValue()==0) {
					zipcode_name = "";
				}else {
					zipcode_name = mZip[np_city.getValue()][area.getValue()];
				}
				btn_goparity.setClickable(true);
				btn_goparity.setAlpha(1f);
			}
		});

			

		dialog.setNegativeButton(getString(R.string.wl_dialog_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		dialog.show();

		np_city.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				area.setMaxValue(0);
				area.setDisplayedValues(mZip[picker.getValue()]);
				area.setMaxValue(mZip[picker.getValue()].length - 1);
				area.setValue(0);
			}
		});

	}

}