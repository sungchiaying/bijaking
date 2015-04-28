package com.ourpower.object;

public class MarketsObject {
	private String address;
	private String created_at;
	private String updated_at;
	private String market_id;
	private String market_name;
	private String cost;
	private String county;
	private String zipcode_name;
	private String channel_name;
	public void setAddress(String _address) {
		address = _address;
	}
	public void setCreated_at(String _created_at) {
		created_at = _created_at;
	}
	public void setUpdated_at(String _updated_at) {
		updated_at = _updated_at;
	}
	public void setMarket_id(String _market_id) {
		market_id = _market_id;
	}
	public void setMarket_name(String _market_name) {
		market_name = _market_name;
	}
	public void setCost(String _cost) {
		cost = _cost;
	}
	public void setCountry(String _county) {
		county = _county;
	}
	public void setChannel_name(String _channel_name) {
		channel_name = _channel_name;
	}
	public void setZipcode_name(String _zipcode_name) {
		zipcode_name = _zipcode_name;
	}
	
	public String getAddress() {
		return address;
	}
	public String getCreated_at() {
		return created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public String getMarket_id() {
		return market_id;
	}
	public String getMarket_name() {
		return market_name;
	}
	public String getCost() {
		return cost;
	}
	public String getCounty() {
		return county;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public String getZipcode_name() {
		return zipcode_name;
	}
}
