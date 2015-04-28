package com.ourpower.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.loopj.android.image.SmartImageView;
import com.ourpower.fragment.ManufacturerModifyItemsFragment;
import com.ourpower.fragment.ProductListFragment;
import com.ourpower.kingofprice.MainActivity;
import com.ourpower.kingofprice.R;
import com.ourpower.tool.HttpValue;

public class StoresGridView extends GridView {
	private final String TAG = "StoresGridView";

	private Context mContext;

	private int mAmount;
	private String mImage[];

	public StoresGridView(Context context, int page) {
		super(context);
		mContext = context;
		this.setNumColumns(2);
		this.setPadding(145, 100, 145, 100);
		this.setHorizontalSpacing(100);
		this.setVerticalSpacing(100);
		this.setHorizontalScrollBarEnabled(false);
		this.setVerticalScrollBarEnabled(false);
		this.setOverScrollMode(View.OVER_SCROLL_NEVER);
		this.setAdapter(new GridAdapter());

		mAmount = HttpValue.getInstance(getContext()).getCategory()[page]
				.getChannels().length;
		mImage = new String[mAmount];
		for (int i = 0; i < mAmount; i++) {
			HttpValue httpValue = HttpValue.getInstance(context);
			mImage[i] = httpValue.getCategory()[page].getChannels()[i]
					.getImage();

		}

	}

	private class GridAdapter extends BaseAdapter {
		public int getCount() {
			return mAmount;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			SmartImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes

				imageView = new SmartImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);

			} else {
				imageView = (SmartImageView) convertView;
			}
			;
			imageView.setImageUrl(mImage[position]);

			return imageView;
		}
	}

}
