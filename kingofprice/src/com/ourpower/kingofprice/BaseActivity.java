package com.ourpower.kingofprice;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.ourpower.fragment.AboutUsFragment;
import com.ourpower.fragment.CategorySelectionFragment;
import com.ourpower.fragment.LeadingFragment;
import com.ourpower.fragment.LoginFragment;
import com.ourpower.fragment.MemberCenterFragment;
import com.ourpower.fragment.NetworkErrorFragment;
import com.ourpower.fragment.ProductListFragment;
import com.ourpower.fragment.WantListFragment;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class BaseActivity extends FragmentActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		NavigationDrawerFragment.OnLogButtonClickListener,
		SearchView.OnQueryTextListener,
		NetworkErrorFragment.OnTryAgainClickListener {

	private final static String TAG = "BaseActivity";

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private CharSequence[] mTitleArray;
	private ArrayList<Integer> mRealPosition = new ArrayList<Integer>();
	private String[] mLoginMenu;
	private String[] mDefaultMenu;

	private SearchView mSearchView;
	private Context mContext;
	private ProgressDialog progressDialog;
	private HttpDataTransmitter mHttpDataTransmitter = new HttpDataTransmitter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		mContext = getApplication();
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		final boolean isLogin = isLogin();
		if (isLogin) {
			mTitleArray = getResources().getStringArray(R.array.menu_login);
		} else {
			mTitleArray = getResources().getStringArray(R.array.menu_default);
			// mLoginMenu = getResources().getStringArray(R.array.menu_login);
			// mDefaultMenu =
			// getResources().getStringArray(R.array.menu_default);
		}
		setRealPosition();
		mTitle = getTitle();

		// Set up the drawer.
		setupDrawer();
	}

	private void setRealPosition() {
		if (isLogin()) {
			return;
		}
		if (mLoginMenu == null) {
			mLoginMenu = getResources().getStringArray(R.array.menu_login);
		}
		if (mDefaultMenu == null) {
			mDefaultMenu = getResources().getStringArray(R.array.menu_default);
		}
		int amount = mLoginMenu.length;
		int i = 0;
		int j = 0;
		while (i < amount) {
			if (!mLoginMenu[i].equals(mDefaultMenu[j])) {
				i++;
				continue;
			}
			mRealPosition.add(i);
			// Log.d(TAG, "real position = " + i + " ###################");
			i++;
			j++;
		}
	}

	private int getRealPosition(int position) {
		if (isLogin()) {
			return position;
		}
		return mRealPosition.size() == 0 ? position : mRealPosition
				.get(position - 1) + 1;
	}

	@Override
	public void onNavigationDrawerItemSelected(int position,
			boolean addToBackStack) {
		// update the main content by replacing fragments
		Log.d(TAG, "onNavigationDrawerItemSelected(), position = " + position);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		position = getRealPosition(position);
		transaction.replace(R.id.fragment_container,
				PlaceholderFragment.newInstance(position));
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	public void setupDrawer() {
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	public void onSectionAttached(int number) {
		Log.d("Michael", "onSectionAttached, number = " + number);
		final CharSequence title = mTitleArray[number - 1];
		if (title != null) {
			mTitle = title;
		}
	}

	protected void selectItem(int position, boolean addToBackStack) {
		mNavigationDrawerFragment.selectItem(position, addToBackStack);
	}

	protected void closeDrawer() {
		mNavigationDrawerFragment.closeDrawer();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				(R.drawable.background_actionbar)));
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.

			// Ernest
			// getMenuInflater().inflate(R.menu.main, menu);

			MenuInflater inflater = getMenuInflater();
			if (MainActivity.current_page == MainActivity.COMMODITY_PAGE)
				inflater.inflate(R.menu.main_simple, menu);
			else {
				inflater.inflate(R.menu.main, menu);
				mSearchView = (SearchView) menu.findItem(R.id.action_search)
						.getActionView();
				mSearchView.setOnQueryTextListener(this);
			}

			restoreActionBar();

			return true;
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_search:
			break;
		case R.id.action_settings:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		// public static PlaceholderFragment newInstance(int sectionNumber) {
		public static Fragment newInstance(int sectionNumber) {
			Fragment fragment;
			switch (sectionNumber) {
			case NavigationDrawerFragment.POSITION_DRAWER_ITEM_SOTRE_SELECTION:
				fragment = new CategorySelectionFragment();
				break;
			case NavigationDrawerFragment.POSITION_DRAWER_ITEM_WISH_LIST:
				fragment = new WantListFragment();
				break;
			case NavigationDrawerFragment.POSITION_DRAWER_ITEM_MEMBER_CENTER:
				fragment = new MemberCenterFragment();
				break;
			/*
			 * case POSITION_DRAWER_ITEM_MANUFACTURER_LOGING: fragment = new
			 * ManufacturerLoginGoodsFragment(); break;
			 */
			case NavigationDrawerFragment.POSITION_DRAWER_ITEM_ABOUT_US:
				fragment = new AboutUsFragment();
				break;
			case NavigationDrawerFragment.POSITION_DRAWER_ITEM_LEADINGAPP:
				fragment = new LeadingFragment();
				break;
			default:
				fragment = new PlaceholderFragment();
				break;
			}
			// PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// View rootView = inflater.inflate(R.layout.fragment_main,
			// container,
			// false);
			View rootView = null;
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((BaseActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	@SuppressWarnings("null")
	public void makeSystemCrash() {
		Integer i = null;
		i.byteValue();
	}

	@Override
	public void OnLogButtonClick(boolean isLogin) {
		Log.d(TAG, "OnLogButtonClick, login = " + isLogin);
		if (isLogin) {
			HttpValue.getInstance(this).clearServiceID();
			((MainActivity)this).FBLogin(false);
			HttpValue.getInstance(this).clearMemberName();
		}
		showLoginPage();
		updateDrawerListView();
		setRealPosition();
	}

	public void updateDrawerListView() {
		HttpDataTransmitter hdt = new HttpDataTransmitter(this);
		hdt.getProfile();
		mNavigationDrawerFragment.updateDrawerMenuResources();
	}

	public boolean isLogin() {
		if (!mHttpDataTransmitter.isOnline()) {
			return false;
		}
		final String s = HttpValue.getInstance(this).getServiceID();
		if (s == null || s.equals("")) {
			return false;
		}
		return true;
	}

	private void showLoginPage() {
		FragmentManager fm = getFragmentManager();
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_container, new LoginFragment())
				.commit();
	}
	
	public void showNetworkErrorPage() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_container, new NetworkErrorFragment())
				.commit();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Log.d("", "ooxx:" + query);
		if (!isNetworkAlive()) {
			FragmentTransaction edittransaction = getFragmentManager()
					.beginTransaction();
			edittransaction.replace(R.id.fragment_container, new NetworkErrorFragment());
			edittransaction.addToBackStack(null);
			edittransaction.commit();
			return false;
		}
		String county, region;
		HttpValue httpValue = HttpValue.getInstance(mContext);
		county = httpValue.cityselectcounty;
		region = httpValue.cityselectregion;
		httpValue.ProductListproductname = query;
		if (county.length() > 0 || region.length() > 0) {
			httpValue.setProductSearchNVP(query, county, region);
		} else {
			httpValue.setProductSearchNVP(query, "", "");
		}

		HttpDataTransmitter DataTransmitter = new HttpDataTransmitter(mContext);
		DataTransmitter.getProductList(HttpValue.PRODUCTLIST_MODE_SEARCH);
		HttpValue.PRODUCTLIST_MODE = HttpValue.PRODUCTLIST_MODE_SEARCH;

		switch (MainActivity.current_page) {
		case MainActivity.PRODUCTLIST_PAGE:
			ProductListFragment.mProductlist = httpValue.getProductListInfo();
			ProductListFragment.AdapterRefresh();
			break;
			
		case MainActivity.CATERORYSELECTION_PAGE:
			Fragment newFragment;
			FragmentTransaction itemtransaction;
			newFragment = new ProductListFragment();
			itemtransaction = getFragmentManager().beginTransaction();
			itemtransaction.replace(R.id.fragment_container, newFragment);
			itemtransaction.addToBackStack(null);
			itemtransaction.commit();
			break;
		default:
			break;
		}

		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	public void showLoadingDialog(boolean show) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		if (show) {
			progressDialog = ProgressDialog.show(
					this,
					Html.fromHtml("<font color='#33b5e5'>"
							+ getResources().getString(
									R.string.dialog_loading_tile) + "</font>"),
					Html.fromHtml("<font color='#000000'>"
							+ getResources().getString(
									R.string.dialog_loading_message)
							+ "</font>"), true, false);
		}
	}

	@Override
	public void onTryAgainClick() {
		onBackPressed();
	}
	
	public boolean isNetworkAlive() {
		return mHttpDataTransmitter.isOnline();
	}
}
