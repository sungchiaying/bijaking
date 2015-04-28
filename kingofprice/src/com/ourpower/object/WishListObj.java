package com.ourpower.object;

import android.util.Log;

public class WishListObj {

	private String image1;
	private String image2;
	private String detailed;
	private String created_at;
	private String updated_at;
	private String quantity;
	private String product_id;
	private String product_name;

	public WishListObj(String _image1, String _image2, String _detailed,
			String _created_at, String _updated_at, String _quantity,
			String _product_id, String _product_name) {

		image1 = _image1;
		image2 = _image2;
		detailed = _detailed;
		created_at = _created_at;
		updated_at = _updated_at;
		quantity = _quantity;
		product_id = _product_id;
		product_name = _product_name;

	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String _image1) {
		this.image1 = _image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String _image2) {
		this.image2 = _image2;
	}

	public String getDetailed() {
		return detailed;
	}

	public void setDetailed(String _detailed) {
		this.detailed = _detailed;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String _created_at) {
		this.created_at = _created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String _updated_at) {
		this.updated_at = _updated_at;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String _quantity) {
		this.quantity = _quantity;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String _product_id) {
		this.product_id = _product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String _product_name) {
		this.product_name = _product_name;
	}

}
