package com.ourpower.adapter;

import com.ourpower.kingofprice.R;
import com.ourpower.object.TotalAlmountListObject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoreParityAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private TotalAlmountListObject[] totalAlmount;

	public StoreParityAdapter(Context c, TotalAlmountListObject[] _totalAlmount) {
		context = c;
		inflater = LayoutInflater.from(c);
		totalAlmount = _totalAlmount;

	}

	@Override
	public int getCount() {
		return totalAlmount.length;
	}

	@Override
	public Object getItem(int position) {
		return totalAlmount[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SPViewHolder vh = new SPViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.storeparity_item, null);
			vh.ll_storename = (LinearLayout) convertView
					.findViewById(R.id.sp_ll_storename);
			vh.ll_totalprice = (LinearLayout) convertView
					.findViewById(R.id.sp_ll_totalprice);
			vh.tv_storeprice = (TextView) convertView
					.findViewById(R.id.sp_tv_storename);
			vh.tv_totalprice = (TextView) convertView
					.findViewById(R.id.sp_tv_totalprice);

			convertView.setTag(vh);

		} else {
			vh = (SPViewHolder) convertView.getTag();
		}
		if (position % 2 == 1) {
			vh.ll_storename.setBackgroundColor(Color.parseColor("#EFEFEF"));
			vh.ll_totalprice.setBackgroundColor(Color.parseColor("#EFEFEF"));
//			vh.ll_totalprice.setBackgroundResource(R.drawable.modify_background);
		}
		vh.tv_storeprice.setText(totalAlmount[position].getChannelName() + " "
				+ totalAlmount[position].getMarketName());
		vh.tv_totalprice.setText(totalAlmount[position].getTotalAmount()
				+ context.getResources().getText(R.string.wl_doller));

		return convertView;
	}

	private class SPViewHolder {
		TextView tv_totalprice, tv_storeprice;
		LinearLayout ll_totalprice, ll_storename;
	}

}
