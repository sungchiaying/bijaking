package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ourpower.adapter.LeadingAdapter;
import com.ourpower.kingofprice.R;

public class LeadingFragment extends Fragment {

	private Context context;
	private LeadingAdapter mAdapter;
	private ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View view = inflater.inflate(R.layout.fragment_leading, container,
				false);
		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mAdapter = new LeadingAdapter(context);
		mViewPager.setAdapter(mAdapter);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		System.gc();
		Log.d("", "Ernest LeadingFragment onCreate gc");
	}

	// @Override
	// public void onAttach(Activity activity) {
	// super.onAttach(activity);
	// try {
	// mListener = (OnAboutUsClickListener) activity;
	// } catch (ClassCastException e) {
	// throw new ClassCastException(activity.toString()
	// + " must implement OnAboutUsClickListener");
	// }
	// }

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
		actionBar.setTitle(R.string.drawer_menu_item_leadingapp);
	}

}