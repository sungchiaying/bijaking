package com.ourpower.object;

import com.ourpower.kingofprice.R;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class MyDialog {
	AlertDialog.Builder alertDialog;
	public static final int MESSAGE_CHOOSE_CHANNELS = 1;  
	public static final int MESSAGE_CHOOSE_STORENAME = 2; 
//	private final String MESSAGE_CHOOSE_;  
//	private final String MESSAGE_CHOOSE; 
	private Context mContext;
	private int ID;
	private GetChannels[] mGetChannel;
	private LocatedChannelMarketsObject[] mLocatedChannelMarkets;
	private String[] Markets;
	private String[] channel;
	private String chooseID;
	public myOnDialogClickListener mListener;
	
	public <T extends Context & DialogInterface.OnClickListener > MyDialog(T context , int id , Bundle bundle , myOnDialogClickListener _mListener) {
		mContext = context;
		alertDialog = new AlertDialog.Builder(context);
		ID = id;
		mListener = _mListener;
		mListener = (myOnDialogClickListener) context;
	}
	
	public void showDialog() {
		switch (ID) {
		case MESSAGE_CHOOSE_CHANNELS:
			if (mGetChannel == null || mGetChannel.length==0) {
				HttpDataTransmitter hdt = new HttpDataTransmitter(mContext);
				HttpValue httpValue = HttpValue.getInstance(mContext);
				hdt.getChannel();
				mGetChannel = httpValue.getChannelInfo();
				channel = new String[mGetChannel.length];
				for (int i = 0; i < mGetChannel.length; i++) {
					channel[i] = mGetChannel[i].getChannel_name();
				}
			}
			alertDialog.setTitle(Html.fromHtml("<font color='#5091c8'>" + mContext.getResources().getText(R.string.dialog_choose_channel) + "</font>"));
			alertDialog.setItems(channel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					chooseID = mGetChannel[which].getChannel_id();
					mListener.myOnDialogClick(chooseID , mGetChannel[which].getChannel_name());
				}
			});
			
			alertDialog.show();
			break;
		case MESSAGE_CHOOSE_STORENAME:
			HttpValue httpValue = HttpValue.getInstance(mContext);
			mLocatedChannelMarkets = httpValue.getLocatedChannelMarketsInfo();
			Markets = new String[mLocatedChannelMarkets.length];
			for (int i = 0; i < mLocatedChannelMarkets.length; i++) {
				Markets[i] = mLocatedChannelMarkets[i].getMarket_name();
			}
			
			
			alertDialog.setTitle("請選擇店家名稱");
			alertDialog.setItems(Markets, (DialogInterface.OnClickListener) mContext);
			alertDialog.setItems(Markets, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					mListener.myOnDialogClick(mLocatedChannelMarkets[which].getMarket_id(), mLocatedChannelMarkets[which].getMarket_name());
				
				}
			});
			break;
		default:
			break;
		}
		
	}
	
	public interface myOnDialogClickListener{
		void myOnDialogClick(String chooseID , String chooseName);
	}
	
}
