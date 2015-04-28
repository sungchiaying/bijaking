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
import com.ourpower.object.WishListObj;

public class WantListAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private WishListObj[] wlo;
	
	public WantListAdapter(Context c , WishListObj[] _wlo){
		this.context = c;
		this.inflater = LayoutInflater.from(context);
		this.wlo = _wlo;
	}
	
	@Override
	public int getCount() {
		return wlo==null ? 0 : wlo.length;
	}

	@Override
	public Object getItem(int position) {
		return wlo[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WLViewHolder hv = new WLViewHolder();
		if(convertView == null){
			convertView = inflater.inflate(R.layout.wl_list_item, null);
			hv.iv_icon = (SmartImageView)convertView.findViewById(R.id.wl_list_icon12); 
			hv.tv_name = (TextView)convertView.findViewById(R.id.wl_list_name); 
//			hv.tv_store = (TextView)convertView.findViewById(R.id.wl_list_store); 
//			hv.tv_address = (TextView)convertView.findViewById(R.id.wl_list_address); 
			
			convertView.setTag(hv);
		}else {
			hv = (WLViewHolder) convertView.getTag();
		}
		hv.iv_icon.setImageUrl(wlo[position].getImage2());
		hv.tv_name.setText(wlo[position].getProduct_name());
		
		return convertView;
	}
	private class WLViewHolder {
		private TextView tv_name;
		private SmartImageView iv_icon;
	}

}
