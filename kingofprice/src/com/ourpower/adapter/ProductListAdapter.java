package com.ourpower.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.ourpower.kingofprice.R;
import com.ourpower.object.ProductListObject;
import com.ourpower.tool.HttpValue;

public class ProductListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
 
	private ProductListObject[] mProductlist;
	private HttpValue httpvalue;
	private Context mContext;

	public ProductListAdapter(Context c, ProductListObject[] productListObject) {
		inflater = LayoutInflater.from(c);
		mProductlist = productListObject;
		httpvalue = HttpValue.getInstance(c);
		mContext = c;
	}

	@Override
	public int getCount() {
		 
		int count = (mProductlist == null) ? 0 : mProductlist.length;
		return count;
	}

	@Override
	public Object getItem(int position) {
		// return name[position];
		return mProductlist[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PLViewHolder hv = new PLViewHolder();
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.product_list_item, null);
			hv.iv_icon = (SmartImageView) convertView
					.findViewById(R.id.smartimageview_icon);
			hv.tv_name = (TextView) convertView
					.findViewById(R.id.textview_productname);
			hv.tv_store = (TextView) convertView
					.findViewById(R.id.textview_channel);
			hv.tv_address = (TextView) convertView
					.findViewById(R.id.textview_address);
			hv.tv_cost = (TextView) convertView.findViewById(R.id.textview_cost);
			convertView.setTag(hv);
		} else {
			hv = (PLViewHolder) convertView.getTag();
		}
		if (!httpvalue.mIsLowOrder) {
			position = mProductlist.length - 1 - position;
		}
		Log.d("", "Ernest httpvalue.mIsLowOrder:" + httpvalue.mIsLowOrder);
		hv.tv_name.setText(mProductlist[position].getProduct_name());

		hv.tv_store.setText(mProductlist[position].getMarket()[0]
				.getChannel_name()
				+ " "
				+ mContext.getString(R.string.productlist_productprice));

		hv.tv_address.setText(mProductlist[position].getMarket()[0]
				.getCounty()
				+ " "
				+ mProductlist[position].getMarket()[0].getZipcode_name()
				+ " "
				+ mProductlist[position].getMarket()[0].getMarket_name()
				);
		hv.iv_icon.setImageUrl(mProductlist[position].getImage2());
		hv.tv_cost.setText(" NT$."+ mProductlist[position].getMin_cost());
		return convertView;
	}

	private class PLViewHolder {
		TextView tv_name, tv_store, tv_address , tv_cost;
		SmartImageView iv_icon;
	}
}