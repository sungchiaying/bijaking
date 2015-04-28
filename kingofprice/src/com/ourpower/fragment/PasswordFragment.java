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

public class PasswordFragment extends Fragment {
	private final String TAG = "LoginFragment";
	private final int OPTION_INDEX_START = 0;
	public final int OPTION_COMMIT = OPTION_INDEX_START + 0;
	public final int OPTION_FORGET = OPTION_INDEX_START + 1;

	private Button[] mOptionsButton;
	private OnPasswordCommitClickListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_password, container, false);
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
			mListener = (OnPasswordCommitClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnPasswordCommitClickListener");
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
		title.setText(R.string.action_bar_title_password);
		actionBar.setCustomView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void initView(View view) {
		// TODO password commit listener 
		
		final int amountOfOptions = getResources().getStringArray(
				R.array.password_options).length;

		if (amountOfOptions != OPTION_FORGET - OPTION_COMMIT + 1) {
			Log.e(TAG,
					"Amount of password options != OPTION_FORGET - OPTION_COMMIT");
			((BaseActivity) getActivity()).makeSystemCrash();
			return;
		}
		mOptionsButton = new Button[amountOfOptions];
		for (int i = OPTION_INDEX_START; i < amountOfOptions; i++) {
			int itemId;
			switch (i) {
			case OPTION_COMMIT:
				itemId = R.id.button_commit;
				break;
			case OPTION_FORGET:
				itemId = R.id.button_forget;
				break;
			default:
				itemId = R.id.button_commit;
				break;
			}
			mOptionsButton[i] = (Button) view.findViewById(itemId);
			mOptionsButton[i].setOnClickListener(getOnClickListener(itemId));
		}
	}

	public OnClickListener getOnClickListener(final int resourceId) {
		Log.d(TAG, "getOnClickListener");
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "onClick");
				mListener.onPasswordCommitClick(resourceId);
			}
		};
	}

	public interface OnPasswordCommitClickListener {
		public void onPasswordCommitClick(int resourceId);
	}
	
}
