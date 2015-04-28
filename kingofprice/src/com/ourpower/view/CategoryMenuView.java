package com.ourpower.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ourpower.kingofprice.BaseActivity;
import com.ourpower.kingofprice.R;
import com.ourpower.kingofprice.R.color;
import com.ourpower.tool.HttpValue;

import java.util.ArrayList;
import java.util.List;

public class CategoryMenuView extends HorizontalScrollView {
	private final String TAG = "CategoryMenuScrollView";
	private final int ONSCREEN_CATEGORY_NUMBER = 3;
	private final int CATEGORY_MENU_HEIGHT = 120;
	
	public Button[] mCategoryViewArray;
	private LinearLayout mCategoryListLayout;
	private int mSelectedPosition;
	private ArrayList<String> mList;
	private boolean mIsFirstSelect = true;
	
	public CategoryMenuView(Context context, int amountOfCategory) {
		super(context);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, CATEGORY_MENU_HEIGHT));
		setHorizontalScrollBarEnabled(false);
		setVerticalScrollBarEnabled(false);
		mCategoryViewArray = new Button[amountOfCategory];
		mCategoryListLayout = new LinearLayout(context);
		mCategoryListLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mCategoryListLayout.setPadding(0, 3, 0, 3);
		mCategoryListLayout.setBackgroundResource(R.drawable.background_category_menu);
		mCategoryListLayout.setGravity(Gravity.CENTER_VERTICAL);
		final int categoryViewWidth = getScreenWidth(context) / ONSCREEN_CATEGORY_NUMBER;
		final List<String> categoryNameArray = getCategoryName();
		if (categoryNameArray.size() != amountOfCategory) {
			Log.e(TAG, "Amount of category name != category page number");
			((BaseActivity) context).makeSystemCrash();
			return;
		}
		for (int i=0; i<amountOfCategory; i++) {
			mCategoryViewArray[i] = new Button(context);
			mCategoryViewArray[i].setWidth(categoryViewWidth);
			mCategoryViewArray[i].setBackgroundColor(Color.TRANSPARENT);
			mCategoryViewArray[i].setText(categoryNameArray.get(i));
			mCategoryViewArray[i].setTextAppearance(context, R.style.CategoryTextView);
			mCategoryListLayout.addView(mCategoryViewArray[i]);
		}
		addView(mCategoryListLayout);
	}
	
	public void setSelected(int position) {
		if (mCategoryViewArray[position] == null) {
			return;
		}
		
		if (!mIsFirstSelect && position == mSelectedPosition) {
			return;
		}
		mIsFirstSelect = false;
		mCategoryViewArray[mSelectedPosition].setBackgroundColor(Color.TRANSPARENT);
		mCategoryViewArray[mSelectedPosition].setTextColor(color.category_text);
		mCategoryViewArray[position].setBackgroundColor(color.category_background_selected);
		mCategoryViewArray[position].setTextColor(Color.WHITE);
		mCategoryViewArray[position].setSelected(true);
		if ((position > mSelectedPosition) && (position > 1)) {
			scrollTo(mCategoryListLayout.getChildAt(position - 1).getLeft(), 0);
		}
		else if ((position < mSelectedPosition) && (position > 0)) {
			scrollTo(mCategoryListLayout.getChildAt(position - 1).getLeft(), 0);
		}
		mSelectedPosition = position;
	}
	
	private List<String> getCategoryName() {
		mList = new ArrayList<String>();
		for (int i = 0; i < HttpValue.getInstance(getContext()).getCategory().length; i++) {
			mList.add(HttpValue.getInstance(getContext()).getCategory()[i].getName());
		}
		return mList;
	}
	
	private int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point size = new Point();
		Display display = wm.getDefaultDisplay();
		display.getSize(size);
		int width = size.x;
		return width;
	}
	
}
