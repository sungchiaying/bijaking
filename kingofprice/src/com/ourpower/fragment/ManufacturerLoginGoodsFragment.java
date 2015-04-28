package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ourpower.kingofprice.R;

public class ManufacturerLoginGoodsFragment extends Fragment {
	private final String TAG = "ManufacturerLoginGoodsInFragment";

	private String[] mTitleArray;
	private int mAmountOfCategory = 6;
	private OnMLogingGoodsClickListener mListener;
	private String[] lunch = { "基隆市", "台北市", "新北市", "桃園市", "新竹市" };
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Context context = getActivity();
		LayoutInflater Goodsinflater = LayoutInflater.from(context);
		View view = Goodsinflater.inflate(
				R.layout.fragment_manufacturerlogingoods, null);
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
			mListener = (OnMLogingGoodsClickListener) activity;
		} catch (ClassCastException e) {
			// throw new ClassCastException(activity.toString()
			// + " must implement OnLoginOptionsSelectedListener");
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
		menu.clear();
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
		actionBar.setTitle(R.string.drawer_menu_item_manufacturer_login_goods);
		Log.d(TAG, "getTitle() = " + actionBar.getTitle());
	}

	private void initView(View view) {
		Button btnArea, btnRetail, btnMainCategory, btnSecCategory, btnThirdCategory, btnItem, btnEdit, btnCounties;

		btnArea = (Button) view.findViewById(R.id.goods_btn_area);
		btnCounties = (Button) view.findViewById(R.id.goods_btn_counties);
		btnRetail = (Button) view.findViewById(R.id.goods_btn_retail);
		btnMainCategory = (Button) view
				.findViewById(R.id.goods_btn_mainCategory);
		btnSecCategory = (Button) view.findViewById(R.id.goods_btn_secCategory);
		btnThirdCategory = (Button) view
				.findViewById(R.id.goods_btn_thirdCategory);
		btnItem = (Button) view.findViewById(R.id.goods_btn_item);
		btnEdit = (Button) view.findViewById(R.id.goods_btn_edit);
		btnArea.setOnClickListener(goodsOnClickListener());
		btnCounties.setOnClickListener(goodsOnClickListener());
		btnRetail.setOnClickListener(goodsOnClickListener());
		btnMainCategory.setOnClickListener(goodsOnClickListener());
		btnSecCategory.setOnClickListener(goodsOnClickListener());
		btnThirdCategory.setOnClickListener(goodsOnClickListener());
		btnItem.setOnClickListener(goodsOnClickListener());
		btnEdit.setOnClickListener(goodsOnClickListener());

	}

	public OnClickListener goodsOnClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.goods_btn_area:
					
					break;
				case R.id.goods_btn_counties:		
					Builder MyAlertDialog = new AlertDialog.Builder(getActivity());
					MyAlertDialog.setItems(lunch, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}});
					MyAlertDialog.show();
					break;
				case R.id.goods_btn_retail:

					break;
				case R.id.goods_btn_mainCategory:

					break;
				case R.id.goods_btn_secCategory:

					break;
				case R.id.goods_btn_thirdCategory:

					break;
				case R.id.goods_btn_item:
					mListener.OnMLogingGoodsClick(R.id.goods_btn_item);
					break;
				case R.id.goods_btn_edit:
					mListener.OnMLogingGoodsClick(R.id.goods_btn_edit);
					break;

				default:
					break;
				}
			}
		};
	}

	public interface OnMLogingGoodsClickListener {
		public void OnMLogingGoodsClick(int resourceId);
	}

}