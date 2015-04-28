package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ourpower.kingofprice.BaseActivity;
import com.ourpower.kingofprice.R;

public class RegisterFragment extends Fragment {
	private final String TAG = "RegisterFragment";
	private final int OPTION_INDEX_START = 0;
	public final int OPTION_REGISTER = OPTION_INDEX_START + 0;

	private Button[] mOptionsButton;
	private OnAuthorizeAgreedClickListener mListener;
	private EditText mPasswordView;
	private EditText mPasswordConfirmView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container, false);
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
			mListener = (OnAuthorizeAgreedClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnAuthorizeAgreedClickListener");
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
		mPasswordView = (EditText) view.findViewById(R.id.edittext_password);
		mPasswordConfirmView = (EditText) view.findViewById(R.id.edittext_password_confirm);
		
		final int amountOfOptions = getResources().getStringArray(
				R.array.register_options).length;

		if (amountOfOptions != OPTION_REGISTER - OPTION_REGISTER + 1) {
			Log.e(TAG,
					"Amount of password options != OPTION_REGISTER - OPTION_REGISTER");
			((BaseActivity) getActivity()).makeSystemCrash();
			return;
		}
		mOptionsButton = new Button[amountOfOptions];
		for (int i = OPTION_INDEX_START; i < amountOfOptions; i++) {
			int itemId;
			switch (i) {
			case OPTION_REGISTER:
			default:
				itemId = R.id.button_register;
				mOptionsButton[i] = (Button) view.findViewById(itemId);
				mOptionsButton[i].setOnClickListener(onRegisterClickListener);
				break;
			}
		}
	}

	private void openAuthorizeAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
//		alertDialogBuilder.setTitle(R.string.dialog_authorizztion_remind_title);
		alertDialogBuilder.setTitle(
				Html.fromHtml("<font color='#33b5e5'>" + 
						getResources().getString(R.string.dialog_authorization_remind_title) + 
						"</font>"));
		alertDialogBuilder.setMessage(R.string.dialog_authorization_remind_message);
		// set positive button: Yes message
		
		alertDialogBuilder.setPositiveButton(R.string.dialog_authorization_remind_positive,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Bundle bundle = new Bundle();
						String password = mPasswordConfirmView.getText().toString();
						bundle.putString("register_password", password);
						mListener.onAuthorizeAgreedClick(id, password);
					}
				});

		alertDialogBuilder.setNegativeButton(R.string.dialog_authorization_remind_negative,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onAuthorizeAgreedClick(id, null);
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show alert
		alertDialog.show();
	}

	private OnClickListener onRegisterClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG, "onRegisterClick");
			// TODO if register success, show dialog
			// else show toast ""
			boolean hasError = false;
			if (!mPasswordView.getText().toString()
					.equals(mPasswordConfirmView.getText().toString())) {
				mPasswordConfirmView.setError(getString(R.string.register_password_inconsistent));
				mPasswordConfirmView.requestFocus();
				hasError = true;
			}
			if (mPasswordView.getText().toString().isEmpty()) {
				mPasswordView.setError(getString(R.string.register_password_empty));
				mPasswordView.requestFocus();
				hasError = true;
			}
			if (hasError) {
				return;
			}
			openAuthorizeAlert();
		}
	};

	public interface OnAuthorizeAgreedClickListener {
		public void onAuthorizeAgreedClick(int resourceId, String password);
	}
	
}
