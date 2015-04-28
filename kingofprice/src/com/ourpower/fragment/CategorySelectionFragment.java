package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.ourpower.kingofprice.BaseActivity;
import com.ourpower.kingofprice.MainActivity;
import com.ourpower.kingofprice.R;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;
import com.ourpower.view.CategoryMenuView;
import com.ourpower.view.StoresGridView;

public class CategorySelectionFragment extends Fragment {
	private final String TAG = "CategorySelectionFragment";

	private ViewPager mPager;
	private StoresGridView[] mPages;
	private StoresGridView gridview;
	private String[] mTitleArray;
	private int mAmountOfCategory = 0;
	private OnRouteClickListener mListener;
	private HttpDataTransmitter mTransmitter;
	private Context context;
	private HttpValue httpValue;
	private int mCurrentPosition = 0;

	private CategoryMenuView mCategoryMenu;
	
	private static int mScrollX = 0;
	private static int mScrollY = -1;
	private boolean mIsFirstEntered = true;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnRouteClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnProductItemClickListener");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setHasOptionsMenu(true);
		context = getActivity();
		mTransmitter = new HttpDataTransmitter(context);
		mTransmitter.getCatrogryList();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		httpValue = HttpValue.getInstance(context);
		// get AmountofCategory
		mAmountOfCategory = httpValue.getCategory().length;
		LinearLayout view = new LinearLayout(context);
		if (mAmountOfCategory <= 0) {
			return view;
		}
		
		view.setOrientation(LinearLayout.VERTICAL);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		mCategoryMenu = new CategoryMenuView(context, mAmountOfCategory);
		view.addView(mCategoryMenu);
		View vp = inflater.inflate(R.layout.fragment_category_selection,
				container, false);
		// ViewPager vp = new ViewPager(context);
		view.addView(vp);
		mPager = (ViewPager) vp.findViewById(R.id.pager);
		// setSlideMenu(view);
		initPages();
		 
		mCategoryMenu.setSelected(mCurrentPosition);
		mPager.setAdapter(new ViewPagerAdapter());
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// this will be called when the page is changed
				mCategoryMenu.setSelected(position);
				mCurrentPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		for (int i = 0; i < mAmountOfCategory; i++) {
			final int position = i;
			mCategoryMenu.mCategoryViewArray[i]
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mPager.setCurrentItem(position, true);
						}
					});
		}
		return view;
	}
	 
	public void onPause() {
		super.onPause();
		if (mCategoryMenu != null) {
			mScrollX = mCategoryMenu.getScrollX();
			mScrollY = mCategoryMenu.getScrollY();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume(), mCurrentPosition = " + mCurrentPosition);
		resumePageStatus();
		((BaseActivity)getActivity()).showLoadingDialog(false);
		MainActivity.current_page = MainActivity.CATERORYSELECTION_PAGE;
	}
	
	private void resumePageStatus() {
		if (mIsFirstEntered) {
			mIsFirstEntered = false;
			return;
		}
		if (mCategoryMenu != null) {
			mCategoryMenu.post(new Runnable(){
				@Override
				public void run() {
					mCategoryMenu.scrollTo(mScrollX, mScrollY);
					mCategoryMenu.setSelected(mCurrentPosition);
					mPager.setCurrentItem(mCurrentPosition, true);
				}
			 });
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu()");
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
		actionBar.setTitle(R.string.drawer_menu_item_store_selection);
		Log.d(TAG, "getTitle() = " + actionBar.getTitle());
	}

	private void initPages() {
		mPages = new StoresGridView[mAmountOfCategory];
		for (int i = 0; i < mAmountOfCategory; i++) {
			// mPages[i] = new StoresGridView(getActivity());
			final int i1 = i;
			gridview = new StoresGridView(getActivity(), i);
			gridview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.d(TAG, "click:" + position);

					mListener.OnRouteClick(position);

					String class1_id = httpValue.getCategory()[i1]
							.getClass_id() + "";
					String channel_id = httpValue.getCategory()[i1]
							.getChannels()[position].getChannel_id();
					String county = httpValue.cityselectcounty;
					String region = httpValue.cityselectregion;
					if (county.length() > 0 | region.length() > 0) {
						httpValue.setProductListNVP(class1_id, channel_id,
								county, region);
					} else {
						httpValue.setProductListNVP(class1_id, channel_id, "",
								"");
					}

					Log.d("aa", "Ernesta:" + class1_id + ":" + channel_id);
					httpValue.ProductListchannelid = channel_id;
					httpValue.ProductListclassid = class1_id;
					HttpValue.PRODUCTLIST_MODE = HttpValue.PRODUCTLIST_MODE_ALLLIST;
				}
			});
			mPages[i] = gridview;
		}
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return mPages.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(mPages[position]);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitleArray[position];
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(mPages[position]);
			return mPages[position];
		}
	}

	public interface OnRouteClickListener {
		public void OnRouteClick(int resourceId);
	}

}
