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

public class LoginFragment extends Fragment {
	private final String TAG = "LoginFragment";
	private final int OPTION_INDEX_START = 0;
	public final int OPTION_LOG_IN = OPTION_INDEX_START + 0;
	public final int OPTION_FACEBOOK_LOG_IN = OPTION_INDEX_START + 1;
	public final int OPTION_DIRECT_ENTER = OPTION_INDEX_START + 2;

	private Button[] mOptionsButton;
	private OnLoginOptionsClickListener mListener;
	private int mAmountOfOptions;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
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
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		for (int i = OPTION_INDEX_START; i < mAmountOfOptions; i++) {
			mOptionsButton[i].setClickable(true);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnLoginOptionsClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnLoginOptionsSelectedListener");
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
		title.setText(R.string.action_bar_title_login);
		actionBar.setCustomView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void initView(View view) {
		mAmountOfOptions = getResources().getStringArray(
				R.array.login_options).length;

		if (mAmountOfOptions != OPTION_DIRECT_ENTER - OPTION_LOG_IN + 1) {
			Log.e(TAG,
					"Amount of login options != OPTION_DIRECT_ENTER - OPTION_INDEX_START");
			((BaseActivity) getActivity()).makeSystemCrash();
			return;
		}
		mOptionsButton = new Button[mAmountOfOptions];
		for (int i = OPTION_INDEX_START; i < mAmountOfOptions; i++) {
			final int itemId;
			switch (i) {
			case OPTION_LOG_IN:
				itemId = R.id.button_login;
				break;
			case OPTION_FACEBOOK_LOG_IN:
				itemId = R.id.button_fb_login;
				break;
			case OPTION_DIRECT_ENTER:
				itemId = R.id.button_direct_enter;
				break;
			default:
				itemId = R.id.button_direct_enter;
			}
			mOptionsButton[i] = (Button) view.findViewById(itemId);
			final int position = i;
			mOptionsButton[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onLoginOptionsClick(itemId);
//					mOptionsButton[position].setClickable(false);
				}
			});
		}
	}

	public interface OnLoginOptionsClickListener {
		public void onLoginOptionsClick(int resourceId);
	}
	
}
