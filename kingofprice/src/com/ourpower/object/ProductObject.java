package com.ourpower.object;


public class ProductObject {
	private String image1;
	private String image2;
	private String detailed;
	private String created_at;
	private String updated_at;
	private String product_id;
	private String product_name;
	private int liked;
	private String min_cost;
	private MarketsObject[] markets; 
	public void setImage1(String _image1) {
		image1 = _image1;
	}
	public void setImage2(String _image2) {
		image2 = _image2;
	}
	public void setDetailed(String _detailed) {
		detailed = _detailed;
	}
	public void setCreated_at(String _created_at) {
		created_at = _created_at;
	}
	public void setUpdated_at(String _updated_at) {
		updated_at = _updated_at;
	}
	public void setProduct_id(String _product_id) {
		product_id = _product_id;
	}
	public void setProduct_name(String _product_name) {
		product_name = _product_name;
	}
	public void setLiked(int _liked) {
		liked = _liked;
	}
	public void setMin_cost(String _min_cost) {
		min_cost = _min_cost;
	}
	public void setMarketsobj(MarketsObject[] _marketsobj) {
		markets = _marketsobj;
	}
	
	
	
	public String getImage1() {
		return image1;
	}
	public String getImage2() {
		return image2 ;
	}
	public String getDetailed() {
		return detailed ;
	}
	public String getCreated_at() {
		return created_at;
	}
	public String getUpdated_at() {
		return updated_at ;
	}
	public String getProduct_id() {
		return product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public int getLiked() {
		return liked ;
	}
	public String getMin_cost() {
		return min_cost ;
	}
	public MarketsObject[] getMarketsobj() {
		return markets;
	}

	
}
