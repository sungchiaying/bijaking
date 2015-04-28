package com.ourpower.object;

public class ProductListObject {
	String name="";
	String product_id ="";
	String image1 ="";
	String image2 ="";
	MarketsObject[] markets;
	String min_cost= "";
	
	public void setProduct_name(String name) {
		this.name = name;
	}
	
	public String getProduct_name() {
		return name;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage2() {
		return image2;
	}

	public void setMarket(MarketsObject[] market) {
		this.markets = market;
	}

	public MarketsObject[] getMarket() {
		return markets;
	}

	public void setMin_cost(String min_cost) {
		this.min_cost = min_cost;
	}

	public String getMin_cost() {
		return min_cost;
	}
}
