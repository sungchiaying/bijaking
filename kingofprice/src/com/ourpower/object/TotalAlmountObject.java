package com.ourpower.object;

public class TotalAlmountObject {
	private String channel_name;
	private String market_id;
	private String market_name;
	private String totalAmount;
	private TotalAlmountProducts[] products;
	
	public String getChannel_name(){
		return channel_name;
	}
	public String getMarket_id(){
		return market_id;
	}
	public String getMarket_name(){
		return market_name;
	}
	public String getTotalAmount(){
		return totalAmount;
	}
	public TotalAlmountProducts[] getProducts(){
		return products;
	}
}
