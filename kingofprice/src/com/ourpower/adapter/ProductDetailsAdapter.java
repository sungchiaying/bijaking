package com.ourpower.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourpower.kingofprice.R;
import com.ourpower.object.MarketsObject;
import com.ourpower.tool.HttpValue;

public class ProductDetailsAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private MarketsObject[] marketsObject;
	private HttpValue httpvalue;

	public ProductDetailsAdapter(Context c, MarketsObject[] _marketsObject) {
		mContext = c;
		mInflater = LayoutInflater.from(c);
		marketsObject = _marketsObject;
	}

	@Override
	public int getCount() {
		return marketsObject.length;
	}

	@Override
	public Object getItem(int position) {
		return marketsObject[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		commodityViewHolder vh = new commodityViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.storeparity_item, null);
			vh.tv_storename = (TextView) convertView
					.findViewById(R.id.sp_tv_storename);
			vh.tv_totalprice = (TextView) convertView
					.findViewById(R.id.sp_tv_totalprice);
			vh.thisLayout = (LinearLayout) convertView
					.findViewById(R.id.sp_item_layout);
			convertView.setTag(vh);
		} else {
			vh = (commodityViewHolder) convertView.getTag();
		}
		httpvalue = HttpValue.getInstance(mContext);
		if (position % 2 == 1) {
			vh.thisLayout.setBackgroundColor(Color.parseColor("#DDDDDD"));
		} else {
			vh.thisLayout.setBackgroundColor(Color.TRANSPARENT);
		}
		if (!httpvalue.mIsLowOrder) {
			position = marketsObject.length - 1 - position;
		}
		vh.tv_storename.setText(marketsObject[position].getChannel_name() + " "
				+ marketsObject[position].getMarket_name());
		vh.tv_totalprice.setText("" + marketsObject[position].getCost()
				+ mContext.getResources().getText(R.string.commodity_doller));

		return convertView;
	}

	private class commodityViewHolder {
		TextView tv_storename, tv_totalprice;
		LinearLayout thisLayout;
	}

}
