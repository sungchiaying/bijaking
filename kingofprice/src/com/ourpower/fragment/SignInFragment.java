package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ourpower.kingofprice.R;
import com.ourpower.object.GetChannels;
import com.ourpower.object.LocatedChannelMarketsObject;
import com.ourpower.object.LocationList;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class SignInFragment extends Fragment implements OnClickListener {
	private final int MSG_START_GET_CHANNEL_MARKET = 1;
	
	private OnSignInClickListener mListener;
	private Context context;
	private TextView tv_commName;
	private Button btn_pathChoose, btn_area, btn_storeName, btn_send;
	private EditText et_commprice;
	private String product_id;
	private String market_id;

	private HttpDataTransmitter hdt;
	private HttpValue httpValue;
	private LocationList[] mLocationList;
	private String[] mLocation;
	private String[][] mZip;

	private LocatedChannelMarketsObject[] mLocatedChannelMarkets;
	private String[] markets;
	private String[] channel;
	private GetChannels[] mGetChannel;
	private String channelID, location_name, Zip_name;
	private Bundle b;
	private boolean choosePath = false;
	private boolean chooseArea = false;
	private boolean chooseStoreName = false;
	
	private DialogHandler mDialogHandler = new DialogHandler();
	private ProgressDialog mProgressDialog;

	// private boolean chooseprice = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_signin, container, false);
		initview(view);
		return view;
	}

	private void initview(View view) {
		mProgressDialog = new ProgressDialog(context);
		btn_pathChoose = (Button) view.findViewById(R.id.si_btn_pathchoose);
		btn_area = (Button) view.findViewById(R.id.si_btn_area);
		btn_storeName = (Button) view.findViewById(R.id.si_btn_storename);
		btn_send = (Button) view.findViewById(R.id.si_btn_send);
		et_commprice = (EditText) view.findViewById(R.id.si_et_commprice);
		tv_commName = (TextView) view.findViewById(R.id.si_tv_commname);
		btn_pathChoose.setOnClickListener(this);
		btn_area.setOnClickListener(this);
		btn_storeName.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		tv_commName.setText(b.getString("product_name"));
		// et_commprice.getText()
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		setHasOptionsMenu(true);
		hdt = new HttpDataTransmitter(context);
		httpValue = HttpValue.getInstance(context);
		mLocationList = httpValue.getLocationListInfo();
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
				mZip[i][j] = mLocationList[i].getZipCode()[j].getZipcodeName();
			}
		}

		hdt.getChannel();
		mGetChannel = httpValue.getChannelInfo();
		channel = new String[mGetChannel.length];
		for (int i = 0; i < mGetChannel.length; i++) {
			channel[i] = mGetChannel[i].getChannel_name();
		}

		Intent i = getActivity().getIntent();
		b = new Bundle();
		b = i.getExtras();

		product_id = b.getString("product_id");

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnSignInClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSignInClickListener");
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
		actionBar.setTitle(R.string.signin_title);
	}

	public OnClickListener getOnClickListener(final int resourceId) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.OnSignInClick(resourceId);
			}
		};
	}

	public interface OnSignInClickListener {
		public void OnSignInClick(int resourceId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.si_btn_pathchoose:
			AlertDialog.Builder pathChooseDialog = new AlertDialog.Builder(
					context);
			pathChooseDialog.setTitle(Html.fromHtml("<font color='#33b5e5'>"
					+ getString(R.string.dialog_choose_channel) + "</font>"));
			pathChooseDialog.setItems(channel,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							btn_pathChoose.setText(channel[which]);
							channelID = String.valueOf((which + 1));
							choosePath = true;
						}
					});
			pathChooseDialog.show();
			break;
		case R.id.si_btn_area:
			if (choosePath) {
				picker();
			} else {
				Toast.makeText(context,
						getString(R.string.signin_plz_choose_storename),
						Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.si_btn_storename:
			if (chooseArea) {
				mProgressDialog.show();
				httpValue.setLocatedChannelMarketsNVP(channelID, location_name,
						Zip_name);
				DialogThread dialogThread = new DialogThread();
				dialogThread.start();

			} else {
				Toast.makeText(context,
						getString(R.string.signin_plz_choose_area),
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.si_btn_send:
			if (et_commprice.getText().length() != 0 && chooseStoreName) {
				httpValue.setAddQuotedPriceNVP(product_id, market_id,
						et_commprice.getText().toString());
				if (hdt.addQuotedPrice()) {
					Toast.makeText(context,
							getString(R.string.signin_signin_success),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context,
							getString(R.string.signin_signin_fales),
							Toast.LENGTH_SHORT).show();
				}
				mListener.OnSignInClick(R.id.si_btn_send);
			} else {
				Toast.makeText(context, getString(R.string.signin_cant_signin),
						Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}
	}

	public void getLocatedChannelMarkets() {
		if (hdt.getLocatedChannelMarkets()) {
			mProgressDialog.dismiss();
			mLocatedChannelMarkets = httpValue.getLocatedChannelMarketsInfo();
			markets = new String[mLocatedChannelMarkets.length];
			for (int i = 0; i < mLocatedChannelMarkets.length; i++) {
				markets[i] = mLocatedChannelMarkets[i].getMarket_name();
			}
			AlertDialog.Builder storeNameDialog = new AlertDialog.Builder(
					context);
			storeNameDialog.setTitle(Html.fromHtml("<font color='#33b5e5'>"
					+ getString(R.string.signin_choose_storename) + "</font>"));
			storeNameDialog.setItems(markets,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							btn_storeName.setText(markets[which]);
							market_id = mLocatedChannelMarkets[which]
									.getMarket_id();
							chooseStoreName = true;
						}
					});
			storeNameDialog.show();
		} else {
			mProgressDialog.dismiss();
			Toast.makeText(context, getString(R.string.signin_nostorename),
					Toast.LENGTH_SHORT).show();
		}
	}

	void picker() {
		LayoutInflater ll = LayoutInflater.from(context);
		View pricepicker = ll.inflate(R.layout.wantlist_citypicker, null);
		final NumberPicker np_city = (NumberPicker) pricepicker
				.findViewById(R.id.wl_np_city);
		final NumberPicker area = (NumberPicker) pricepicker
				.findViewById(R.id.wl_np_area);

		np_city.setDisplayedValues(mLocation);
		np_city.setMaxValue(mLocation.length - 1);
		np_city.setValue(0);

		area.setDisplayedValues(mZip[0]);
		area.setMaxValue(mZip[0].length - 1);
		area.setValue(0);

		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

		dialog.setTitle(Html.fromHtml("<font color='#33b5e5'>"
				+ getString(R.string.signin_choose_area) + "</font>"));
		dialog.setView(pricepicker);
		dialog.setPositiveButton(getString(R.string.signin_dialog_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						btn_area.setText(mLocation[np_city.getValue()] + "-"
								+ mZip[np_city.getValue()][area.getValue()]);
						location_name = mLocation[np_city.getValue()];
						if (area.getValue() == 0) {
							Zip_name = "";
						} else {
							Zip_name = mZip[np_city.getValue()][area.getValue()];
						}
						// httpValue.setLocatedChannelMarketsNVP(channelID, , );
						chooseArea = true;
					}
				});

		dialog.setNegativeButton(getString(R.string.signin_dialog_cancel),
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

	private class DialogThread extends Thread {
		@Override
		public void run() {
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mDialogHandler.sendEmptyMessage(MSG_START_GET_CHANNEL_MARKET);
		}
	}

	private class DialogHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_START_GET_CHANNEL_MARKET:
				getLocatedChannelMarkets();
				break;
			}
		}
	}

}