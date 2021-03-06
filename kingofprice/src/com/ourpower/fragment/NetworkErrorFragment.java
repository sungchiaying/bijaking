package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ourpower.kingofprice.BaseActivity;
import com.ourpower.kingofprice.R;

public class NetworkErrorFragment extends Fragment {
	private final String TAG = "NetworkErrorFragment";

	private Button mTryAgainButton;
	private OnTryAgainClickListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_network_error, container, false);
		Log.d(TAG, "onCreateView");
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
			mListener = (OnTryAgainClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTryAgainClickListener");
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.global, menu);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.show();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionBar.setCustomView(R.layout.actionbar_center_title);
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.actionbar_center_title, null);
		TextView title = (TextView) v.findViewById(R.id.actionbar_title);
		title.setText(R.string.action_bar_network_error);
		actionBar.setCustomView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void initView(View view) {
		mTryAgainButton = (Button) view.findViewById(R.id.button_try_again);
		mTryAgainButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onTryAgainClick();
			}
		});
	}

	public interface OnTryAgainClickListener {
		public void onTryAgainClick();
	}
	
}
