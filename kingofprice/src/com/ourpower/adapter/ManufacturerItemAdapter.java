package com.ourpower.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ourpower.kingofprice.R;

public class ManufacturerItemAdapter extends BaseAdapter{

	Context context;
	LayoutInflater inflater;
	String[] s = new String[]{"品項A","品項B","品項C","品項D"};
	
	public ManufacturerItemAdapter (Context m ) {
		context = m;
		inflater =LayoutInflater.from(m);
		
	}
	
	@Override
	public int getCount() {
		
		return s.length;
	}

	@Override
	public Object getItem(int position) {
		
		return s[position];
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.list_manufactureritem, null);

		TextView t1 = (TextView) convertView.findViewById(R.id.mf_text);
		LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.mf_layout);
		t1.setText(s[position]);
		if(position%2 == 0){
			layout.setBackgroundResource(R.drawable.modify_background);
		}else {
			layout.setBackgroundResource(R.drawable.modify_background2);
		}
		
		
		
		
		
		return convertView;
	}

}
