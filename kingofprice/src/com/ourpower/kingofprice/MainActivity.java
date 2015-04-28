package com.ourpower.kingofprice;

import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.ourpower.fragment.CategorySelectionFragment;
import com.ourpower.fragment.LoginFragment;
import com.ourpower.fragment.ManufacturerItemFragment;
import com.ourpower.fragment.ManufacturerLoginGoodsFragment;
import com.ourpower.fragment.ManufacturerModifyItemsFragment;
import com.ourpower.fragment.MemberCenterFragment;
import com.ourpower.fragment.MemberCenterModifyFragment;
import com.ourpower.fragment.NetworkErrorFragment;
import com.ourpower.fragment.ParityDetailsFragment;
import com.ourpower.fragment.PasswordFragment;
import com.ourpower.fragment.ProductDetailsFragment;
import com.ourpower.fragment.ProductListFragment;
import com.ourpower.fragment.RegisterFragment;
import com.ourpower.fragment.SignInFragment;
import com.ourpower.fragment.SplashFragment;
import com.ourpower.fragment.StoreParityFragment;
import com.ourpower.fragment.WantListFragment;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class MainActivity extends BaseActivity implements
		LoginFragment.OnLoginOptionsClickListener,
		ManufacturerLoginGoodsFragment.OnMLogingGoodsClickListener,
		ManufacturerItemFragment.OnMItemClickListener,
		ManufacturerModifyItemsFragment.OnMModifyItemClickListener,
		PasswordFragment.OnPasswordCommitClickListener,
		RegisterFragment.OnAuthorizeAgreedClickListener,
		MemberCenterFragment.OnMemberCenterClickListener,
		MemberCenterModifyFragment.OnMemberCenterModifyClickListener,
		MemberCenterModifyFragment.OnMemberNameChangeListener,
		WantListFragment.OnWantListClickListener,
		StoreParityFragment.OnStoreParityClickListener,
		ProductDetailsFragment.OnCommodityClickListener,
		SignInFragment.OnSignInClickListener,
		ProductListFragment.OnProductItemClickListener,
		CategorySelectionFragment.OnRouteClickListener,
		NavigationDrawerFragment.OnCitySelectListener,
		NavigationDrawerFragment.OnOrderChangeListener,
		NavigationDrawerFragment.OnCitySelectDismissListener {
	private final String TAG = "MainActivity";

	private final int SPLASH_DISPLAY_LENGHT = 3000;
	public static final int CATERORYSELECTION_PAGE = 0;
	public static final int PRODUCTLIST_PAGE = 1;
	public static final int COMMODITY_PAGE = 2;

	private String mCellphoneNumber = "";
	Context mContext;
	public static int current_page = 0;
	private HttpDataTransmitter mTransmitter = new HttpDataTransmitter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
			mContext = this;
			closeDrawer();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, new SplashFragment())
					.commit();

			new Handler().postDelayed(new Runnable() {
				public void run() {
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					if (isLogin()) {
						FragmentManager fm = getFragmentManager();
						fm.popBackStack(null,
								FragmentManager.POP_BACK_STACK_INCLUSIVE);
						selectItem(
								NavigationDrawerFragment.POSITION_DRAWER_ITEM_SOTRE_SELECTION,
								false);
					} else {
						transaction.replace(R.id.fragment_container,
								new LoginFragment()).commit();
					}
				}
			}, SPLASH_DISPLAY_LENGHT);
		}

	}

	@Override
	public void onBackPressed() {
		// Catch back action and pops from backstack
		// (if you called previously to addToBackStack() in your transaction)
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
			return;
		}
		// Default action on back pressed
		super.onBackPressed();
	}

	@Override
	public void onLoginOptionsClick(int resourceId) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		Fragment newFragment;
		if (!isNetworkAlive()) {
			newFragment = new NetworkErrorFragment();
			transaction.replace(R.id.fragment_container, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			return;
		}
		switch (resourceId) {
		case R.id.button_login:
			final EditText editText = (EditText) findViewById(R.id.edittext_account);
			mCellphoneNumber = editText.getText().toString();

			if (mCellphoneNumber.length() != 10) {
				editText.setError(getString(R.string.login_phonelenth_error));
				editText.requestFocus();
				return;
			}
			if (isAccountExist(HttpDataTransmitter.PARAMETER_VALUE_CELLPHONE,
					mCellphoneNumber)) {
				newFragment = new PasswordFragment();
				transaction.replace(R.id.fragment_container, newFragment);
				transaction.addToBackStack(null);
			} else {
				newFragment = new RegisterFragment();
				transaction.replace(R.id.fragment_container, newFragment);
				transaction.addToBackStack(null);
			}
			break;
		case R.id.button_direct_enter:
			selectItem(
					NavigationDrawerFragment.POSITION_DRAWER_ITEM_SOTRE_SELECTION,
					true);
			break;
		case R.id.button_fb_login:
			FBLogin(true);
			break;
		}
		transaction.commit();
	}

	public void onPasswordCommitClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		switch (resourceId) {
		case R.id.button_commit:
			final EditText editText = (EditText) findViewById(R.id.edittext_password);
			if (!isPasswordCorrect(mCellphoneNumber, editText.getText()
					.toString())) {
				break;
			}
			updateDrawerListView();
			FragmentManager fm = getFragmentManager();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			selectItem(
					NavigationDrawerFragment.POSITION_DRAWER_ITEM_SOTRE_SELECTION,
					false);
			break;
		case R.id.button_forget:
			openForgetPasswordAlert();
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	public void OnMLogingGoodsClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		switch (resourceId) {
		case R.id.goods_btn_edit:
			ManufacturerModifyItemsFragment newItemFragment = new ManufacturerModifyItemsFragment();
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, newItemFragment);
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			break;
		case R.id.goods_btn_item:
			ManufacturerItemFragment newFragment = new ManufacturerItemFragment();
			FragmentTransaction itemtransaction = getFragmentManager()
					.beginTransaction();
			itemtransaction.replace(R.id.fragment_container, newFragment);
			itemtransaction.addToBackStack(null);
			itemtransaction.commit();
			break;

		default:
			break;
		}

	}

	@Override
	public void onMItemClick(int resourceId) {

	}

	@Override
	public void OnMModifyItemClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		switch (resourceId) {
		case R.id.mmi_delete:
			CategorySelectionFragment newFragment = new CategorySelectionFragment();
			FragmentTransaction itemtransaction = getFragmentManager()
					.beginTransaction();
			itemtransaction.replace(R.id.fragment_container, newFragment);
			itemtransaction.addToBackStack(null);
			itemtransaction.commit();
			Toast.makeText(MainActivity.this, "?�除", 3000).show();
			break;

		case R.id.mmi_save:
			CategorySelectionFragment newFragment2 = new CategorySelectionFragment();
			FragmentTransaction itemtransaction2 = getFragmentManager()
					.beginTransaction();
			itemtransaction2.replace(R.id.fragment_container, newFragment2);
			itemtransaction2.addToBackStack(null);
			itemtransaction2.commit();
			Toast.makeText(MainActivity.this, "?��?", 3000).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void OnMemberCenterClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		MemberCenterModifyFragment newFragment = new MemberCenterModifyFragment();
		FragmentTransaction itemtransaction2 = getFragmentManager()
				.beginTransaction();
		itemtransaction2.replace(R.id.fragment_container, newFragment);
		itemtransaction2.addToBackStack(null);
		itemtransaction2.commit();
	}

	@Override
	public void OnMemberCenterModifyClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		MemberCenterFragment newFragment = new MemberCenterFragment();
		FragmentTransaction itemtransaction2 = getFragmentManager()
				.beginTransaction();
		itemtransaction2.replace(R.id.fragment_container, newFragment);
		itemtransaction2.addToBackStack(null);
		itemtransaction2.commit();
	}

	@Override
	public void OnWantListClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		Fragment newFragment;
		FragmentTransaction itemtransaction2;

		switch (resourceId) {
		case R.id.wl_btn_goparity:
			newFragment = new StoreParityFragment();
			itemtransaction2 = getFragmentManager().beginTransaction();
			itemtransaction2.replace(R.id.fragment_container, newFragment);
			itemtransaction2.addToBackStack(null);
			itemtransaction2.commit();
			break;

		default:
			newFragment = new ProductDetailsFragment();
			itemtransaction2 = getFragmentManager().beginTransaction();
			itemtransaction2.replace(R.id.fragment_container, newFragment);
			itemtransaction2.addToBackStack(null);
			itemtransaction2.commit();
			break;
		}
	}

	@Override
	public void OnStoreParityClick(int postion) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		ParityDetailsFragment newFragment = new ParityDetailsFragment();
		FragmentTransaction itemtransaction2 = getFragmentManager()
				.beginTransaction();
		itemtransaction2.replace(R.id.fragment_container, newFragment);
		itemtransaction2.addToBackStack(null);
		itemtransaction2.commit();
	}

	@Override
	public void onAuthorizeAgreedClick(int resourceId, String password) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		switch (resourceId) {
		case (Dialog.BUTTON_POSITIVE):
			final EditText editText = (EditText) findViewById(R.id.edittext_password_confirm);
			final String password_confirm = editText.getText().toString();
			if (!isRegisterSuccess(mCellphoneNumber, password_confirm)) {
				break;
			}
			updateDrawerListView();
			FragmentManager fm = getFragmentManager();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.replace(R.id.fragment_container,
					new CategorySelectionFragment()).commit();
			break;
		case (Dialog.BUTTON_NEGATIVE):
			Log.d(TAG, "onAuthorizeAgreedClick BUTTON_NEGATIVE");
			break;
		}
	}

	// private void clearBackStack() {
	// Log.d(TAG, "clearBackStack")
	// FragmentManager manager = getFragmentManager();
	// if (manager.getBackStackEntryCount() > 0) {
	// FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
	// manager.popBackStack(first.getId(),
	// FragmentManager.POP_BACK_STACK_INCLUSIVE);
	// }
	// }

	@Override
	public void OnCommodityClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		switch (resourceId) {
		case R.id.comm_ll_loginprice:
			SignInFragment newFragment = new SignInFragment();
			FragmentTransaction itemtransaction2 = getFragmentManager()
					.beginTransaction();
			itemtransaction2.replace(R.id.fragment_container, newFragment);
			itemtransaction2.addToBackStack(null);
			itemtransaction2.commit();
			break;

		default:
			break;
		}
	}

	@Override
	public void OnSignInClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		switch (resourceId) {
		case R.id.si_btn_send:
			ProductDetailsFragment newFragment = new ProductDetailsFragment();
			FragmentTransaction itemtransaction2 = getFragmentManager()
					.beginTransaction();
			itemtransaction2.replace(R.id.fragment_container, newFragment);
			itemtransaction2.addToBackStack(null);
			itemtransaction2.commit();
			break;

		default:
			break;
		}
	}

	private boolean isAccountExist(String type, String account) {
		return mTransmitter.querryAccount(type, account);
	}

	private boolean isPasswordCorrect(String account, String password) {
		return mTransmitter.login(HttpDataTransmitter.ACCOUNT_TYPE_CELLPHONE,
				account, password);
	}

	private boolean isRegisterSuccess(String account, String password) {
		return mTransmitter.registerAccount(
				HttpDataTransmitter.ACCOUNT_TYPE_CELLPHONE, account, password);
	}

	@Override
	public void OnItemClickListener(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		Fragment newFragment;
		FragmentTransaction itemtransaction2;
		newFragment = new ProductDetailsFragment();
		itemtransaction2 = getFragmentManager().beginTransaction();
		itemtransaction2.replace(R.id.fragment_container, newFragment);
		itemtransaction2.addToBackStack(null);
		itemtransaction2.commit();
	}

	@Override
	public void OnRouteClick(int resourceId) {
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		Fragment newFragment;
		FragmentTransaction itemtransaction;
		newFragment = new ProductListFragment();
		itemtransaction = getFragmentManager().beginTransaction();
		itemtransaction.replace(R.id.fragment_container, newFragment);
		itemtransaction.addToBackStack(null);
		itemtransaction.commit();
	}

	@Override
	public void onCitySelect(String country, String region) {
		Log.d(TAG, "country = " + country);
		Log.d(TAG, "region = " + region);
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		httpvalue.cityselectcounty = country;
		httpvalue.cityselectregion = region;
	}

	@Override
	public void onMemberNameChange() {
		updateDrawerListView();
	}

	@Override
	public void OnOrderChange(boolean isLowOrder) {
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		httpvalue.mIsLowOrder = isLowOrder;
	}

	@Override
	public void CitySelectDismiss(boolean isDismiss) {
		Log.d("Ernest", "Ernest isDismiss");
		if (!isDismiss) {
			return;
		}
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return;
		}
		if (current_page == PRODUCTLIST_PAGE) {
			Log.d("Ernest", "Ernest isDismiss on PRODUCTLIST_PAGE");
			HttpValue httpvalue = HttpValue.getInstance(mContext);
			switch (HttpValue.PRODUCTLIST_MODE) {
			case HttpValue.PRODUCTLIST_MODE_ALLLIST:

				httpvalue.setProductListNVP(httpvalue.ProductListclassid,
						httpvalue.ProductListchannelid,
						httpvalue.cityselectcounty, httpvalue.cityselectregion);
				mTransmitter.getProductList(HttpValue.PRODUCTLIST_MODE_ALLLIST);
				ProductListFragment.mProductlist = httpvalue
						.getProductListInfo();
				ProductListFragment.AdapterRefresh();
				break;

			case HttpValue.PRODUCTLIST_MODE_SEARCH:

				httpvalue.setProductSearchNVP(httpvalue.ProductListproductname,
						httpvalue.cityselectcounty, httpvalue.cityselectregion);

				mTransmitter.getProductList(HttpValue.PRODUCTLIST_MODE_SEARCH);
				ProductListFragment.mProductlist = httpvalue
						.getProductListInfo();
				ProductListFragment.AdapterRefresh();
				break;
			}

		} else if (current_page == COMMODITY_PAGE) {
			Log.d("Ernest", "Ernest isDismiss on COMMODITY_PAGE");
			HttpValue httpvalue = HttpValue.getInstance(mContext);

			switch (HttpValue.PRODUCTLIST_MODE) {
			case HttpValue.PRODUCTLIST_MODE_ALLLIST:
				httpvalue.setCommodityNVP(httpvalue.ProductListproductid,
						httpvalue.ProductListchannelid,
						httpvalue.cityselectcounty, httpvalue.cityselectregion);
				mTransmitter.getCommodity();
				ProductDetailsFragment.productObj = httpvalue
						.getCommodityInfo();
				ProductDetailsFragment.AdapterRefresh();
				break;
			case HttpValue.PRODUCTLIST_MODE_SEARCH:
				httpvalue.setCommodityNVP(httpvalue.ProductListproductid, "",
						httpvalue.cityselectcounty, httpvalue.cityselectregion);
				mTransmitter.getCommodity();
				ProductDetailsFragment.productObj = httpvalue
						.getCommodityInfo();
				ProductDetailsFragment.AdapterRefresh();
				break;
			default:
				break;
			}

		}

	}

	private void openForgetPasswordAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// alertDialogBuilder.setTitle(
		// Html.fromHtml("<font color='#33b5e5'>" +
		// getResources().getString(R.string.dialog_forget_password_title) +
		// "</font>"));
		alertDialogBuilder.setMessage(R.string.dialog_forget_password_message);

		alertDialogBuilder.setPositiveButton(
				R.string.dialog_forget_password_positive,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent myIntentDial = new Intent(
								Intent.ACTION_DIAL,
								Uri.parse("tel:"
										+ getString(R.string.phone_number_customer_service)));
						startActivity(myIntentDial);
					}
				});

		alertDialogBuilder.setNegativeButton(
				R.string.dialog_forget_password_negative,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void FBLogin(Boolean login) {
		Session s = new Session(this);
		Session.setActiveSession(s);
		if (!login) {
			if (s.isOpened()) {
				return;
			}
			s.closeAndClearTokenInformation();
			return;
		}
		Session.OpenRequest request = new Session.OpenRequest(this);
		request.setPermissions(Arrays.asList("email"));
		request.setCallback(callback);

		s.openForRead(request);

	}
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			Log.d("", "Ernest session:" + session);
			if (!session.isOpened()) {
				return;
			}
			showLoadingDialog(true);
			Session.getActiveSession();
			Request.newMeRequest(session, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser user, Response response) {
					Log.d(TAG, "user:" + user);
					if (user == null) {

						return;
					}
					final String facebookAccountId = user.getId();
					if (!isAccountExist(
							HttpDataTransmitter.PARAMETER_VALUE_FACEBOOK,
							facebookAccountId)) {
						mTransmitter.registerAccount(
								HttpDataTransmitter.ACCOUNT_TYPE_FACEBOOK,
								facebookAccountId, "");
					}
					if (!mTransmitter.login(
							HttpDataTransmitter.ACCOUNT_TYPE_FACEBOOK,
							facebookAccountId, "")) {
						return;
					}
					updateDrawerListView();
					selectItem(
							NavigationDrawerFragment.POSITION_DRAWER_ITEM_SOTRE_SELECTION,
							false);

				}

			}).executeAsync();

		}
	};

}
