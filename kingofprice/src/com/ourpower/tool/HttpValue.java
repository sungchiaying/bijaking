package com.ourpower.tool;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.ourpower.object.CategoryObject;
import com.ourpower.object.GetChannels;
import com.ourpower.object.LocatedChannelMarketsObject;
import com.ourpower.object.LocationList;
import com.ourpower.object.MemberProfile;
import com.ourpower.object.ProductListObject;
import com.ourpower.object.ProductObject;
import com.ourpower.object.TotalAlmountListObject;
import com.ourpower.object.TotalAlmountObject;
import com.ourpower.object.WishListObj;

public class HttpValue {
	// HttpValue (本物件)
	private volatile static HttpValue mHttpValue;
	// SharedPreferences
	private static final String PREFERENCE_USER_INFO = "user_info";
	private static final String KEY_SERVICE_ID = "service_id";
	private static final String KEY_MEMBER_NAME = "member_name";
	// Response Parameters 名稱
	private static final String CLASS1_ID = "class1_id";
	private static final String PRODUCT_ID = "product_id";
	private static final String MARKET_ID = "market_id";
	private static final String NAME = "name";
	private static final String EMAIL = "email";
	private static final String COST = "cost";
	private static final String PASSWORD = "password";
	private static final String OPASSWORD = "opassword";
	private static final String CHANNEL_ID = "channel_id";
	private static final String SERVICE_ID = "service_id";
	private static final String COUNTY = "county";
	private static final String ZIPCODE_NAME = "zipcode_name";
	// Ernest
	private static final String PRODUCT_NAME = "product_name";

	public final static int PRODUCTLIST_MODE_ALLLIST = 1;
	public final static int PRODUCTLIST_MODE_SEARCH = 2;
	public static int PRODUCTLIST_MODE = 0;
	// 商品頁面的值
	private ArrayList<NameValuePair> wantListNVP;
	private ArrayList<NameValuePair> likeListNVP;
	private ArrayList<NameValuePair> addWishListNVP;
	private ArrayList<NameValuePair> totalAlmountListNVP;
	private ArrayList<NameValuePair> totalAlmountNVP;
	private ArrayList<NameValuePair> editProfileNVP;
	private ArrayList<NameValuePair> quotedPriceNVP;
	private ArrayList<NameValuePair> LocatedChannelMarketsNVP;
	private ArrayList<NameValuePair> DeleteUserWishListNVP;
	private ArrayList<NameValuePair> productListNVP;
	private ArrayList<NameValuePair> productSearchNVP;
	// server傳回的字串
	private String wishListMsg;
	// api 已經有回傳了(目前沒有用)
	private String likeMsg;
	private CategoryObject[] mCategory;
	// 縣市行政區
	private LocationList[] mLocationList;
	// 預購清單
	private WishListObj[] jWishList;
	private TotalAlmountListObject[] mTotalAlmountList;
	private TotalAlmountObject mTotalAlmount;
	// GET 通路
	private GetChannels[] mGetChannel;
	// 會員中心
	private MemberProfile mMemberProfile;

	// city select
	public String cityselectcounty = "";
	public String cityselectregion = "";
	public boolean mIsLowOrder = true;
	// 商家
	private LocatedChannelMarketsObject[] mLocatedChannelMarkets;

	//
	private ProductListObject[] mProductlist;
	public String ProductListchannelid;
	public String ProductListclassid;
	public String ProductListproductid;
	public String ProductListproductname;
	private ProductObject mCommodity;
	private SharedPreferences mSettings;

	public HttpValue(Context context) {
		if (mSettings == null) {
			synchronized (HttpValue.class) {
				if (mSettings == null) {
					mSettings = context.getSharedPreferences(
							PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
				}
			}
		}
	}

	public static HttpValue getInstance(Context context) {
		if (mHttpValue == null) {
			synchronized (HttpValue.class) {
				if (mHttpValue == null) {
					mHttpValue = new HttpValue(context);
				}
			}
		}
		return mHttpValue;
	}

	public void setServiceID(String id) {
		mSettings.edit().putString(KEY_SERVICE_ID, id).commit();
	}

	public void setPreference(String key, String value) {
		mSettings.edit().putString(key, value);
	}

	public void clearServiceID() {
		mSettings.edit().putString(KEY_SERVICE_ID, "").commit();
	}

	public String getServiceID() {
		String s = mSettings.getString(KEY_SERVICE_ID, "");
		return s;
	}

	public void setMemberName(String name) {
		mSettings.edit().putString(KEY_MEMBER_NAME, name).commit();
	}

	public String getMemberName() {
		return mSettings.getString(KEY_MEMBER_NAME, "");
	}
	
	public void clearMemberName() {
		mSettings.edit().putString(KEY_MEMBER_NAME, "").commit();
	}

	public void setWantListInfo(String json) {
		jWishList = new Gson().fromJson(json, WishListObj[].class);
	}

	public WishListObj[] getWantListInfo() {
		return jWishList;
	}

	public void setCategory(String json) {
		mCategory = new Gson().fromJson(json, CategoryObject[].class);
	}

	public CategoryObject[] getCategory() {
		if (mCategory == null) {
			return new CategoryObject[0];
		}
		return mCategory;
	}

	public void setProductListNVP(String class1_id, String channel_id,
			String county, String zipcode_name) {
		productListNVP = new ArrayList<NameValuePair>();
		productListNVP.add(new BasicNameValuePair(CLASS1_ID, class1_id));
		productListNVP.add(new BasicNameValuePair(CHANNEL_ID, channel_id));
		productListNVP.add(new BasicNameValuePair(COUNTY, county));
		productListNVP.add(new BasicNameValuePair(ZIPCODE_NAME, zipcode_name));
	}

	public void setProductSearchNVP(String product_name, String county,
			String zipcode_name) {
		productSearchNVP = new ArrayList<NameValuePair>();
		productSearchNVP
				.add(new BasicNameValuePair(PRODUCT_NAME, product_name));
		productSearchNVP.add(new BasicNameValuePair(COUNTY, county));
		productSearchNVP
				.add(new BasicNameValuePair(ZIPCODE_NAME, zipcode_name));
	}

	public void clearProductListNVP() {
		if (mProductlist == null)
			return;
	}

	public void setProductListInfo(String json) {
		Log.d("", "Ernest json" + json);
		if (json.equals("")) {
			mProductlist = null;
			return;
		}
		mProductlist = new Gson().fromJson(json, ProductListObject[].class);
	}

	public ProductListObject[] getProductListInfo() {
		if (mProductlist == null) {
			return new ProductListObject[0];
		}
		return mProductlist;
	}

	public ArrayList<NameValuePair> getProductListNVP() {
		return productListNVP;
	}

	public ArrayList<NameValuePair> getProductSearchNVP() {
		return productSearchNVP;
	}

	public ArrayList<NameValuePair> getCommodityNVP() {
		return wantListNVP;
	}

	public void clearWantListNVP() {
		wantListNVP.clear();
	}

	public void setCommodityNVP(String product_id, String channel_id,
			String county, String zipcode_name) {
		wantListNVP = new ArrayList<NameValuePair>();
		wantListNVP.add(new BasicNameValuePair(PRODUCT_ID, product_id));
		wantListNVP.add(new BasicNameValuePair(CHANNEL_ID, channel_id));
		wantListNVP.add(new BasicNameValuePair(COUNTY, county));
		wantListNVP.add(new BasicNameValuePair(ZIPCODE_NAME, zipcode_name));
	}

	public void setCommodityInfo(String json) {
		mCommodity = new Gson().fromJson(json, ProductObject.class);
	}

	public ProductObject getCommodityInfo() {
		return mCommodity;
	}

	public ArrayList<NameValuePair> getLikeNVP() {
		return likeListNVP;
	}

	public void setLikeNVP(String service_id, String product_id) {

		likeListNVP = new ArrayList<NameValuePair>();
		likeListNVP.add(new BasicNameValuePair(SERVICE_ID, service_id));
		likeListNVP.add(new BasicNameValuePair(PRODUCT_ID, product_id));
	}

	public void clearLikeNVP() {
		likeListNVP.clear();
	}

	public ArrayList<NameValuePair> getAddWishNVP() {
		return addWishListNVP;
	}

	public void setAddWishNVP(String product_id) {
		addWishListNVP = new ArrayList<NameValuePair>();
		addWishListNVP.add(new BasicNameValuePair(PRODUCT_ID, product_id));
	}

	public void clearAddWishNVP() {
		addWishListNVP.clear();
	}

	public String getAddWishInfo() {
		return wishListMsg;
	}

	public void setAddWishInfo(String info) {
		try {
			JSONObject json = new JSONObject(info);
			wishListMsg = json.getString("Msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getLikeInfo() {
		return likeMsg;
	}

	public void setLikeInfo(String info) {
		try {
			JSONObject json = new JSONObject(info);
			likeMsg = json.getString("Msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public LocationList[] getLocationListInfo() {
		if (mLocationList == null) {
			return new LocationList[0];
		}
		return mLocationList;
	}

	public void setLocationListInfo(String json) {
		mLocationList = new Gson().fromJson(json, LocationList[].class);
	}

	public ArrayList<NameValuePair> getTotalAlmountListNVP() {
		return totalAlmountListNVP;
	}

	public void setTotalAlmountListNVP(String channel_id, String county,
			String zipcode_name) {
		totalAlmountListNVP = new ArrayList<NameValuePair>();

		totalAlmountListNVP.add(new BasicNameValuePair(PRODUCT_ID, channel_id));
		totalAlmountListNVP.add(new BasicNameValuePair(COUNTY, county));
		totalAlmountListNVP.add(new BasicNameValuePair(ZIPCODE_NAME,
				zipcode_name));
	}

	public TotalAlmountListObject[] getTotalAlmountListInfo() {
		return mTotalAlmountList;
	}

	public void setTotalAlmountListInfo(String json) {
		mTotalAlmountList = new Gson().fromJson(json,
				TotalAlmountListObject[].class);
	}

	public void clearTotalAlmountListNVP() {
		totalAlmountListNVP.clear();
	}

	public ArrayList<NameValuePair> getTotalAlmountNVP() {
		return totalAlmountNVP;
	}

	public void setTotalAlmountNVP(String market_id) {
		totalAlmountNVP = new ArrayList<NameValuePair>();
		totalAlmountNVP.add(new BasicNameValuePair(MARKET_ID, market_id));
	}

	public TotalAlmountObject getTotalAlmountInfo() {
		return mTotalAlmount;
	}

	public void setTotalAlmountInfo(String json) {
		mTotalAlmount = new Gson().fromJson(json, TotalAlmountObject.class);
	}

	public void clearTotalAlmountNVP() {
		totalAlmountNVP.clear();
	}

	// Profile

	public MemberProfile getProfileInfo() {
		return mMemberProfile;
	}

	public void setProfileInfo(String json) {
		mMemberProfile = new Gson().fromJson(json, MemberProfile.class);
		setMemberName(mMemberProfile.getName());
	}

	// channel
	public GetChannels[] getChannelInfo() {
		return mGetChannel;
	}

	public void setChannelInfo(String json) {
		mGetChannel = new Gson().fromJson(json, GetChannels[].class);
	}

	public ArrayList<NameValuePair> getEditProfileNVP() {
		return editProfileNVP;
	}

	public void setEditProfileNVP(String service_id, String opassword,
			String password, String email, String name) {
		editProfileNVP = new ArrayList<NameValuePair>();
		editProfileNVP.add(new BasicNameValuePair(SERVICE_ID, service_id));
		editProfileNVP.add(new BasicNameValuePair(OPASSWORD, opassword));
		editProfileNVP.add(new BasicNameValuePair(PASSWORD, password));
		editProfileNVP.add(new BasicNameValuePair(EMAIL, email));
		editProfileNVP.add(new BasicNameValuePair(NAME, name));
	}

	public void clearEditProfileNVP() {
		editProfileNVP.clear();
	}

	public ArrayList<NameValuePair> getAddQuotedPriceNVP() {
		return quotedPriceNVP;
	}

	public void setAddQuotedPriceNVP(String product_id, String market_id,
			String cost) {
		quotedPriceNVP = new ArrayList<NameValuePair>();
		quotedPriceNVP.add(new BasicNameValuePair(PRODUCT_ID, product_id));
		quotedPriceNVP.add(new BasicNameValuePair(MARKET_ID, market_id));
		quotedPriceNVP.add(new BasicNameValuePair(COST, cost));
	}

	public void clearAddQuotedPriceNVP() {
		quotedPriceNVP.clear();
	}

	public void setLocatedChannelMarketsInfo(String json) {
		mLocatedChannelMarkets = new Gson().fromJson(json,
				LocatedChannelMarketsObject[].class);
	}

	public LocatedChannelMarketsObject[] getLocatedChannelMarketsInfo() {
		return mLocatedChannelMarkets;
	}

	public ArrayList<NameValuePair> getLocatedChannelMarketsNVP() {
		return LocatedChannelMarketsNVP;
	}

	public void setLocatedChannelMarketsNVP(String channel_id, String county,
			String zipcode_name) {
		LocatedChannelMarketsNVP = new ArrayList<NameValuePair>();
		LocatedChannelMarketsNVP.add(new BasicNameValuePair(CHANNEL_ID,
				channel_id));
		LocatedChannelMarketsNVP.add(new BasicNameValuePair(COUNTY, county));
		LocatedChannelMarketsNVP.add(new BasicNameValuePair(ZIPCODE_NAME,
				zipcode_name));
	}

	public void clearLocatedChannelMarketsNVP() {
		LocatedChannelMarketsNVP.clear();
	}

	public ArrayList<NameValuePair> getDeleteUserWishListNVP() {
		return DeleteUserWishListNVP;
	}

	public void setDeleteUserWishListNVP(String product_id) {
		DeleteUserWishListNVP = new ArrayList<NameValuePair>();
		DeleteUserWishListNVP
				.add(new BasicNameValuePair(PRODUCT_ID, product_id));
	}

	// public void getCommodityInfo(){
	// String info = new String("");
	//
	// info = mContentValues.getAsString(COMMODITYINFO);
	// try {
	// JSONArray jsonArray = new JSONArray(info);
	// for (int i = 0; i < jsonArray.length(); i++) {
	// JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
	// jsonArray.getString(index)
	// }
	// Log.d("ooxx" , "jsonObject : " + jsonArray);
	// Log.d("ooxx" , "jsonObject channel_name : " +
	// jsonArray.getString("channel_name"));
	// } catch (JSONException e) {
	//
	// e.printStackTrace();
	// }
	//
	// JSONArray jsonArray = new JSONArray(responseString);
	//
	// for (int i = 0; i < jsonArray.length(); i++) {
	//
	// JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
	// // class_name.add(jsonObject.getString("name").toString());
	// class_name.add(jsonObject.getString("name").toString());
	//
	// }
}
