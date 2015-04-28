package com.ourpower.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ourpower.kingofprice.R;

public class LeadingAdapter extends PagerAdapter {
	private int[] img = new int[] { R.drawable.operation1,
			R.drawable.operation2, R.drawable.operation3,
			R.drawable.operation4, R.drawable.operation5,
			R.drawable.operation6, R.drawable.operation7,
			R.drawable.operation8, R.drawable.operation9,
			R.drawable.operation10, R.drawable.operation11,
			R.drawable.operation12, R.drawable.operation13,
			R.drawable.operation14, R.drawable.operation15,
			R.drawable.operation16, R.drawable.operation17 };

	private Context mContext;

	public LeadingAdapter(Context context) {
		mContext = context.getApplicationContext();
	}

	@Override
	public int getCount() {
		return img.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == (View) obj;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.imageview = new ImageView(mContext);
		viewHolder.imageview.setAdjustViewBounds(true);
		viewHolder.imageview.setScaleType(ScaleType.CENTER_CROP);
		viewHolder.imageview.setImageResource(img[position]);
		((ViewPager) container).addView(viewHolder.imageview, 0);
		return viewHolder.imageview;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	private class ViewHolder {
		ImageView imageview;
	}
}