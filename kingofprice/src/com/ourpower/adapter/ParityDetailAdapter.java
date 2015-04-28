package com.ourpower.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourpower.kingofprice.R;
import com.ourpower.object.TotalAlmountObject;
import com.ourpower.object.TotalAlmountProducts;

public class ParityDetailAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private TotalAlmountObject totalAlmount;
	private TotalAlmountProducts[] totalAlmountProducts;
	
	public ParityDetailAdapter(Context c , TotalAlmountObject _totalAlmount){
		
		context = c;
		inflater = LayoutInflater.from(c);
		totalAlmount = _totalAlmount;
		totalAlmountProducts = totalAlmount.getProducts();
	}
	
	@Override
	public int getCount() {
		return totalAlmountProducts.length+1;
	}

	@Override
	public Object getItem(int position) {
		return totalAlmountProducts[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DetailViewHoder hv = new DetailViewHoder();
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.pd_list_item, null);
			
			hv.name = (TextView) convertView.findViewById(R.id.pd_list_name);
			hv.price = (TextView) convertView.findViewById(R.id.pd_list_price);
			hv.ll_name = (LinearLayout) convertView.findViewById(R.id.pd_ll_itemname);
			hv.ll_price = (LinearLayout) convertView.findViewById(R.id.pd_ll_price);
			
			convertView.setTag(hv);
		}else {
			hv = (DetailViewHoder) convertView.getTag();
		}
		if(position%2 == 1){
			hv.ll_price.setBackgroundResource(R.drawable.modify_background);
			hv.ll_name.setBackgroundResource(R.drawable.modify_background);
		}
		if(position == totalAlmountProducts.length){
			hv.ll_price.setBackgroundResource(R.drawable.wl_background2);
			hv.ll_name.setBackgroundResource(R.drawable.wl_background2);
			hv.name.setTextColor(context.getResources().getColor(R.color.category_background_selected));
			hv.price.setTextColor(context.getResources().getColor(R.color.category_background_selected));
			hv.name.setText("販售總價");
			hv.price.setText(totalAlmount.getTotalAmount()+context.getResources().getText(R.string.wl_doller));
		}else {
			hv.name.setText(totalAlmountProducts[position].getProduct_name());
			hv.price.setText(totalAlmountProducts[position].getCost()+context.getResources().getText(R.string.wl_doller));
		}
		
		return convertView;
	}
	
	private class DetailViewHoder{
		private TextView name,price;
		private LinearLayout ll_price,ll_name;
	}

}
