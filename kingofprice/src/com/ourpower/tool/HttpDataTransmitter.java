package com.ourpower.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ourpower.kingofprice.R;

public class HttpDataTransmitter {
	public static final String PARAMETER_NAME_TYPE = "type";
	public static final String PARAMETER_NAME_CELLPHONE = "cellphone";
	public static final String PARAMETER_NAME_FACEBOOK = "fbid";
	public static final String PARAMETER_NAME_PASSWORD = "password";
	public static final String SERVICE_ID = "service_id";
	public static final String PARAMETER_VALUE_CELLPHONE = "1";
	public static final String PARAMETER_VALUE_FACEBOOK = "2";

	public static final int ACCOUNT_TYPE_CELLPHONE = 1;
	public static final int ACCOUNT_TYPE_FACEBOOK = 2;

	private final int FUNCTION_GET_INTRENAL_CODE = 0;
	private final int FUNCTION_GET_LOCATION_LIST = 1;
	private final int FUNCTION_GET_CATEGORY_LIST = 20;
	private final int FUNCTION_GET_PRODUCT_LIST = 21;
	private final int FUNCTION_QUERY_ACCOUNT = 2;
	private final int FUNCTION_LOGIN = 3;
	private final int FUNCTION_REGISTER = 4;
	private final int FUNCTION_GET_WANT_LIST = 10;
	private final int FUNCTION_GET_PRODUCT = 11;
	private final int FUNCTION_GET_LIKE_PRODUCT = 12;
	private final int FUNCTION_ADD_WISH_LIST = 13;
	private final int FUNCTION_GET_TOTAL_ALMOUNT_LIST = 14;
	private final int FUNCTION_GET_TOTAL_ALMOUNT = 15;
	private final int FUNCTION_GET_PROFILE = 16;
	private final int FUNCTION_EDIT = 17;
	private final int FUNCTION_ADD_QUOTEDPRICE = 18;
	private final int FUNCTION_GET_CHANNEL = 19;
	private final int FUNCTION_GET_LOCATED_CHANNELMARKETS = 22;
	private final int FUNCTION_GET_DELETE_USERWISH_LIST = 23;
	private final int METHOD_GET = 0;
	private final int METHOD_POST = 1;

	private final int CODE_GET_WANT_LIST_SUCCESS = 200;
	private final int CODE_GET_CHANNEL_SUCCESS = 200;
	private final int CODE_GET_INTRENAL_CODE_SUCCESS = 200;
	private final int CODE_GET_LOCATION_LIST_SUCCESS = 200;
	private final int CODE_GET_CATEGORY_LIST_SUCCESS = 200;
	private final int CODE_GET_PRODUCT_LIST_SUCCESS = 200;
	private final int CODE_ADD_WISH_LIST_SUCCESS = 201;
	private final int CODE_ACCOUNT_EXIST = 302;
	private final int CODE_LOGIN_SUCCESS = 200;
	private final int CODE_REGISTER_SUCCESS = 201;
	private final int CODE_GET_LIKE_PRODUCT = 200;
	private final int CODE_EDIT = 200;
	private final int CODE_GET_TOTAL_ALMOUNT_LIST = 200;
	private final int CODE_ADD_QUOTEDPRICE = 200;
	private final int CODE_GET_DELETE_USERWISH_LIST = 200;
	private final int CODE_GET_LOCATED_CHANNELMARKETS = 200;
	private final int CODE_NOTFOUND = 404;
	private final int CODE_ERROR = 500;

	private final String URI_ADD_QUOTEDPRICE = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/addquotedprice";
	private final String URI_EDIT = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/edit";
	private final String URI_GET_PROFILE = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/getprofile";
	private final String URI_GET_INTERNAL_CODE = "http://demo.ourpower.com.tw/parity2/public/api/user/getauthtoken";
	private final String URI_GET_LOCATION_LIST = "http://demo.ourpower.com.tw/parity2/public/api/v1/locations/getlocationlist2";
	private final String URI_ADD_WISHLIST = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/adduserwishlist";
	private final String URI_GET_LIKEPRODUCT = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/likeproduct";
	private final String URI_GET_PRODUCT = "http://demo.ourpower.com.tw/parity2/public/api/v1/products/getproduct";
	private final String URI_GET_CATEGORY_LIST = "http://demo.ourpower.com.tw/parity2/public/api/v1/channels/getclassifiedchannels";
	private final String URI_GET_PRODUCT_LIST = "http://demo.ourpower.com.tw/parity2/public/api/v1/products/getproductlist";
	private final String URI_GET_TOTAL_ALMOUNT_LIST = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/gettotalamountlist";
	private final String URI_GET_TOTAL_ALMOUNT = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/gettotalamount";
	private final String URI_GET_LOCATED_CHANNELMARKETS = "http://demo.ourpower.com.tw/parity2/public/api/v1/markets/getlocatedchannelmarkets";
	private final String URI_GET_DELETE_USERWISH_LIST = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/deleteuserwishlist";

	private final int MSG_NETWORK_STATUS_OFFLINE = 0;

	public final String URI_ACCOUNT_QUERY = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/account";
	public final String URI_LOGIN = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/login";
	public final String URI_REGISTER = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/create";
	public final String URI_WANTLIST = "http://demo.ourpower.com.tw/parity2/public/api/v1/users/getuserwishlist";
	public final String URI_CHANNEL = "http://demo.ourpower.com.tw/parity2/public/api/v1/channels/getchannels";
	public final String LOGIN = "login";

	private final String TAG = "HttpDataTransmitter";
	private final String HEADER_PREFIX_SERVICE_TOKEN = "x-auth-token";
	private final String HEADER_PREFIX_SERVICE_ID = "x-service-id";
	private final String RANDOM_CODE = "random";

	private boolean mIsAccountExist = false;
	private boolean mIsLoginSuccess = false;
	private boolean mIsRegisterSuccess = false;
	private boolean mIsCategorySuccess = false;
	private boolean mIsGetWantListExist = false;
	private boolean mIsGetLikeState = false;
	private boolean mIsAddWishList = false;
	private boolean mIsEditState = false;
	private boolean mIsQuotedPriceState = false;
	private boolean mIsDeleteUserWishList = false;
	private boolean mIsLocatedChannelMarkets = false;
	private String mInternalCode = "";
	private String mLocationList = "";
	private String mCateGoryList = "";
	private String mProductSearch = "";
	private Context mContext;

	private ToastHandler mToastHandler = new ToastHandler();

	public HttpDataTransmitter(Context context) {
		mContext = context;
	}

	private void get(int function) {
		HttpGet get = null;
		String serviceID = "";
		HttpValue hv = new HttpValue(mContext);
		serviceID = hv.getServiceID();
		Log.d("ooxx", "GET serviceID: " + serviceID);
		try {
			HttpClient client = new DefaultHttpClient();
			switch (function) {
			case FUNCTION_GET_INTRENAL_CODE:
				get = new HttpGet(URI_GET_INTERNAL_CODE);
				break;
			case FUNCTION_GET_LOCATION_LIST:
				get = new HttpGet(URI_GET_LOCATION_LIST);
				get.setHeader(HEADER_PREFIX_SERVICE_TOKEN,
						parseServerCodeFromHtml(mInternalCode));

				break;
			case FUNCTION_GET_CATEGORY_LIST:
				get = new HttpGet(URI_GET_CATEGORY_LIST);
				get.setHeader(HEADER_PREFIX_SERVICE_TOKEN,
						parseServerCodeFromHtml(mInternalCode));
				break;
			case FUNCTION_GET_WANT_LIST:
				get = new HttpGet(URI_WANTLIST);
				get.setHeader(HEADER_PREFIX_SERVICE_TOKEN,
						parseServerCodeFromHtml(mInternalCode));
				get.addHeader(HEADER_PREFIX_SERVICE_ID, serviceID);
				break;
			case FUNCTION_GET_CHANNEL:
				get = new HttpGet(URI_CHANNEL);
				get.setHeader(HEADER_PREFIX_SERVICE_TOKEN,
						parseServerCodeFromHtml(mInternalCode));
				get.addHeader(HEADER_PREFIX_SERVICE_ID, serviceID);
				break;
			case FUNCTION_GET_PROFILE:
				get = new HttpGet(URI_GET_PROFILE);
				get.setHeader(HEADER_PREFIX_SERVICE_TOKEN,
						parseServerCodeFromHtml(mInternalCode));
				get.addHeader(HEADER_PREFIX_SERVICE_ID, serviceID);
				break;
			}
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == CODE_GET_INTRENAL_CODE_SUCCESS) {
				saveResposeByGet(function, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void post(int function, ArrayList<NameValuePair> pairs) {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
				false);

		try {
			HttpPost httpPost = new HttpPost(getUriByFunction(function));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
			httpPost.setHeader(HEADER_PREFIX_SERVICE_TOKEN,
					parseServerCodeFromHtml(mInternalCode));
			switch (function) {
			case FUNCTION_GET_LIKE_PRODUCT:
				httpPost.addHeader(HEADER_PREFIX_SERVICE_ID, HttpValue
						.getInstance(mContext).getServiceID());
				break;
			case FUNCTION_ADD_WISH_LIST:
				httpPost.addHeader(HEADER_PREFIX_SERVICE_ID, HttpValue
						.getInstance(mContext).getServiceID());
				break;
			case FUNCTION_GET_TOTAL_ALMOUNT_LIST:
				httpPost.addHeader(HEADER_PREFIX_SERVICE_ID, HttpValue
						.getInstance(mContext).getServiceID());
				break;
			case FUNCTION_ADD_QUOTEDPRICE:
				httpPost.addHeader(HEADER_PREFIX_SERVICE_ID, HttpValue
						.getInstance(mContext).getServiceID());
				break;
			case FUNCTION_EDIT:
				httpPost.addHeader(HEADER_PREFIX_SERVICE_ID, HttpValue
						.getInstance(mContext).getServiceID());
				break;
			case FUNCTION_GET_LOCATED_CHANNELMARKETS:
				httpPost.addHeader(HEADER_PREFIX_SERVICE_ID, HttpValue
						.getInstance(mContext).getServiceID());
				break;
			case FUNCTION_GET_TOTAL_ALMOUNT:
				httpPost.addHeader(HEADER_PREFIX_SERVICE_ID, HttpValue
						.getInstance(mContext).getServiceID());
				break;
			case FUNCTION_GET_DELETE_USERWISH_LIST:
				httpPost.addHeader(HEADER_PREFIX_SERVICE_ID, HttpValue
						.getInstance(mContext).getServiceID());
				break;

			}
			HttpResponse response = httpClient.execute(httpPost);
			saveResponeseByPost(function, response);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String parseServerCodeFromHtml(String html) {
		final String code = html.split(":")[1].split("\"")[1];
		final String code1 = code.substring(0, 6);
		final String code2 = code.substring(6, 12);
		final String code3 = code1 + RANDOM_CODE + code2;
		final String code4 = SHA1(SHA1(code3));
		final String code5 = RANDOM_CODE + ":" + code4;
		// Log.d(TAG, "code = " + code);
		// Log.d(TAG, "code1 = " + code1);
		// Log.d(TAG, "code2 = " + code2);
		// Log.d(TAG, "code3 = " + code3);
		// Log.d(TAG, "code4 = " + code4);
		Log.d(TAG, "code5 = " + code5);
		return code5;
	}

	private String SHA1(String text) {
		MessageDigest md;
		byte[] sha1hash = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return convertToHex(sha1hash);
	}

	private String convertToHex(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (byte b : data) {
			int halfbyte = (b >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte)
						: (char) ('a' + (halfbyte - 10)));
				halfbyte = b & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	private class Transmitter implements Runnable {
		private ArrayList<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();
		private int mFunction;
		private int mMethod;

		Transmitter(int function, int method, ArrayList<NameValuePair> pairs) {
			mFunction = function;
			mMethod = method;
			mNameValuePairs = pairs;
		}

		@Override
		public void run() {
//			if (!isOnline()) {
//				mToastHandler.sendEmptyMessage(MSG_NETWORK_STATUS_OFFLINE);
//				return;
//			}
			get(FUNCTION_GET_INTRENAL_CODE);
			switch (mMethod) {
			case METHOD_GET:
				get(mFunction);
				break;
			case METHOD_POST:
				post(mFunction, mNameValuePairs);
				break;
			}
		}
	}

	private class ToastHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NETWORK_STATUS_OFFLINE:
				Toast.makeText(mContext,
						mContext.getString(R.string.network_offline),
						Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
	}

	private String getUriByFunction(int function) {
		String uri = "";
		switch (function) {
		case FUNCTION_QUERY_ACCOUNT:
			uri = URI_ACCOUNT_QUERY;
			break;
		case FUNCTION_LOGIN:
			uri = URI_LOGIN;
			break;
		case FUNCTION_REGISTER:
			uri = URI_REGISTER;
			break;
		case FUNCTION_GET_WANT_LIST:
			uri = URI_WANTLIST;
			break;
		case FUNCTION_GET_PRODUCT:
			uri = URI_GET_PRODUCT;
			break;
		case FUNCTION_GET_LIKE_PRODUCT:
			uri = URI_GET_LIKEPRODUCT;
			break;
		case FUNCTION_ADD_WISH_LIST:
			uri = URI_ADD_WISHLIST;
			break;
		case FUNCTION_GET_PRODUCT_LIST:
			uri = URI_GET_PRODUCT_LIST;
			break;
		case FUNCTION_GET_TOTAL_ALMOUNT_LIST:
			uri = URI_GET_TOTAL_ALMOUNT_LIST;
			break;
		case FUNCTION_GET_TOTAL_ALMOUNT:
			uri = URI_GET_TOTAL_ALMOUNT;
			break;
		case FUNCTION_EDIT:
			uri = URI_EDIT;
			break;
		case FUNCTION_ADD_QUOTEDPRICE:
			uri = URI_ADD_QUOTEDPRICE;
			break;
		case FUNCTION_GET_LOCATED_CHANNELMARKETS:
			uri = URI_GET_LOCATED_CHANNELMARKETS;
			break;
		case FUNCTION_GET_DELETE_USERWISH_LIST:
			uri = URI_GET_DELETE_USERWISH_LIST;
			break;
		}
		return uri;
	}

	private void saveResposeByGet(int function, HttpResponse response) {
		String responseString = "";
		try {
			responseString = EntityUtils.toString(response.getEntity());
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		switch (function) {
		case FUNCTION_GET_INTRENAL_CODE:
			if (response.getStatusLine().getStatusCode() == CODE_GET_INTRENAL_CODE_SUCCESS) {
				mInternalCode = responseString;
			}
			break;
		case FUNCTION_GET_LOCATION_LIST:
			if (response.getStatusLine().getStatusCode() == CODE_GET_LOCATION_LIST_SUCCESS) {
				mLocationList = responseString;
				HttpValue hv = HttpValue.getInstance(mContext);
				hv.setLocationListInfo(responseString);
			}
			break;
		case FUNCTION_GET_CATEGORY_LIST:
			if (response.getStatusLine().getStatusCode() == CODE_GET_CATEGORY_LIST_SUCCESS) {
				// mCateGoryList = responseString;
				HttpValue hv = HttpValue.getInstance(mContext);
				hv.setCategory(responseString);
			}
			break;
		case FUNCTION_GET_WANT_LIST:
			if (response.getStatusLine().getStatusCode() == CODE_GET_WANT_LIST_SUCCESS) {
				HttpValue hv = HttpValue.getInstance(mContext);
				hv.setWantListInfo(responseString);
				// mLocationList = responseString;
			}
			break;
		case FUNCTION_GET_PROFILE:
			if (response.getStatusLine().getStatusCode() == CODE_GET_WANT_LIST_SUCCESS) {
				HttpValue hv = HttpValue.getInstance(mContext);
				hv.setProfileInfo(responseString);
				// mLocationList = responseString;
			}
		case FUNCTION_GET_CHANNEL:
			if (response.getStatusLine().getStatusCode() == CODE_GET_CHANNEL_SUCCESS) {
				HttpValue hv = HttpValue.getInstance(mContext);
				hv.setChannelInfo(responseString);
			}
			break;
		}
	}

	private void saveResponeseByPost(int function, HttpResponse response) {
		final int code = response.getStatusLine().getStatusCode();
		HttpValue httpValue = HttpValue.getInstance(mContext);

		switch (function) {
		case FUNCTION_QUERY_ACCOUNT:
			if (code == Integer.valueOf(CODE_ACCOUNT_EXIST)) {
				mIsAccountExist = true;
			} else {
				mIsAccountExist = false;
			}
			break;
		case FUNCTION_LOGIN:
			if (code == Integer.valueOf(CODE_LOGIN_SUCCESS)) {
				mIsLoginSuccess = true;
			} else {
				mIsLoginSuccess = false;
			}
			saveServiceID(getValueFromResponse(response, "service_id"));
			break;
		case FUNCTION_REGISTER:
			if (code == Integer.valueOf(CODE_REGISTER_SUCCESS)) {
				mIsRegisterSuccess = true;
			} else {
				mIsRegisterSuccess = false;
			}
			saveServiceID(getValueFromResponse(response, "service_id"));
			break;
		case FUNCTION_GET_PRODUCT:
			httpValue.setCommodityInfo(getValueFromResponse(response, null));
			break;
		case FUNCTION_ADD_WISH_LIST:
			if (code == CODE_ADD_WISH_LIST_SUCCESS) {
				mIsAddWishList = true;
				httpValue.setAddWishInfo(getValueFromResponse(response, null));
			}else {
				mIsAddWishList = false;
			}

			break;
		case FUNCTION_GET_LIKE_PRODUCT:
			if (code == CODE_GET_LIKE_PRODUCT) {
				mIsGetLikeState = true;
			} else {
				mIsGetLikeState = false;
			}
			break;
		case FUNCTION_GET_PRODUCT_LIST:
		/**	if (code == CODE_GET_PRODUCT_LIST_SUCCESS) {
				
				httpValue.setProductListInfo(getValueFromResponse(response,
						null));
			} else {
				httpValue.setProductListInfo("");
			}
			mProductSearch = response.toString();
			*/
			httpValue = HttpValue.getInstance(mContext);
			if (code == CODE_NOTFOUND | code == CODE_ERROR) {
				httpValue.setProductListInfo("");
			} else {
				httpValue.setProductListInfo(getValueFromResponse(response,
						null));
			}
			mProductSearch = response.toString();
			Log.d("", "Ernest search:" + mProductSearch);
			break;
		case FUNCTION_GET_TOTAL_ALMOUNT_LIST:
			if (code == CODE_GET_TOTAL_ALMOUNT_LIST) {
				httpValue.setTotalAlmountListInfo(getValueFromResponse(response,
						null));
			}
			
			break;
		case FUNCTION_GET_TOTAL_ALMOUNT:
			httpValue.setTotalAlmountInfo(getValueFromResponse(response, null));
			break;
		case FUNCTION_EDIT:
			if (code == CODE_EDIT) {
				mIsEditState = true;
			} else {
				mIsEditState = false;
			}
			break;
		case FUNCTION_ADD_QUOTEDPRICE:
			if (code == CODE_ADD_QUOTEDPRICE) {
				mIsQuotedPriceState = true;
			} else {
				mIsQuotedPriceState = false;
			}
			break;
		case FUNCTION_GET_DELETE_USERWISH_LIST:
			if (code == CODE_GET_DELETE_USERWISH_LIST) {
				mIsDeleteUserWishList = true;
			} else {
				mIsDeleteUserWishList = false;
			}
			break;
		case FUNCTION_GET_LOCATED_CHANNELMARKETS:
			if (code == CODE_GET_LOCATED_CHANNELMARKETS) {
				httpValue.setLocatedChannelMarketsInfo(getValueFromResponse(
						response, null));
				mIsLocatedChannelMarkets = true;
			} else {
				mIsLocatedChannelMarkets = false;
			}
			break;
		}
	}

	private void saveServiceID(String id) {
		HttpValue.getInstance(mContext).setServiceID(id);
	}

	private String getValueFromResponse(HttpResponse response, String key) {
		String s = "";
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			try {
				String responseString = EntityUtils.toString(entity, "UTF-8");
				s = key == null ? responseString : new JSONObject(
						responseString).getString(key);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	public boolean querryAccount(String type, String account) {
		Log.d(TAG, "querryAccount");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_TYPE, type));

		if (type == PARAMETER_VALUE_FACEBOOK) {

			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_FACEBOOK,
					account));
		} else if (type == PARAMETER_VALUE_CELLPHONE) {
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_CELLPHONE,
					account));
		}
		Log.d("Ernest", nameValuePairs.toString());
		Runnable r = new Transmitter(FUNCTION_QUERY_ACCOUNT, METHOD_POST,
				nameValuePairs);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "mIsAccountExist = " + mIsAccountExist);
		return mIsAccountExist;
	}

	public boolean login(int accountType, String account, String password) {
		Log.d(TAG, "login");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		switch (accountType) {
		case ACCOUNT_TYPE_CELLPHONE:
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_TYPE,
					PARAMETER_VALUE_CELLPHONE));
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_CELLPHONE,
					account));
			break;
		case ACCOUNT_TYPE_FACEBOOK:
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_TYPE,
					PARAMETER_VALUE_FACEBOOK));
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_FACEBOOK,
					account));
			break;
		}
		nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_PASSWORD,
				password));

		Runnable r = new Transmitter(FUNCTION_LOGIN, METHOD_POST,
				nameValuePairs);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "mIsLoginSuccess = " + mIsLoginSuccess);
		return mIsLoginSuccess;
	}

	public boolean registerAccount(int accountType, String account,
			String password) {
		Log.d(TAG, "registerAccount");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		switch (accountType) {
		case ACCOUNT_TYPE_CELLPHONE:
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_TYPE,
					PARAMETER_VALUE_CELLPHONE));
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_CELLPHONE,
					account));
			break;
		case ACCOUNT_TYPE_FACEBOOK:
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_TYPE,
					PARAMETER_VALUE_FACEBOOK));
			nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_FACEBOOK,
					account));
			break;
		}

		nameValuePairs.add(new BasicNameValuePair(PARAMETER_NAME_PASSWORD,
				password));
		Runnable r = new Transmitter(FUNCTION_REGISTER, METHOD_POST,
				nameValuePairs);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "mIsRegisterSuccess = " + mIsRegisterSuccess);
		return mIsRegisterSuccess;
	}

	public void getWantList() {
		Log.d(TAG, "getWantList");
		Runnable r = new Transmitter(FUNCTION_GET_WANT_LIST, METHOD_GET, null);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void getCommodity() {
		Log.d(TAG, "getCommodity");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Runnable r = new Transmitter(FUNCTION_GET_PRODUCT, METHOD_POST,
				httpvalue.getCommodityNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public boolean addWishList() {
		Log.d(TAG, "addWishList");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Runnable r = new Transmitter(FUNCTION_ADD_WISH_LIST, METHOD_POST,
				httpvalue.getAddWishNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mIsAddWishList;
	}

	public boolean getLikeState() {
		Log.d(TAG, "getLikeState");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Runnable r = new Transmitter(FUNCTION_GET_LIKE_PRODUCT, METHOD_POST,
				httpvalue.getLikeNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return mIsGetLikeState;
	}

	public void getLocationList() {
		Log.d(TAG, "getLocationList");
		Runnable r = new Transmitter(FUNCTION_GET_LOCATION_LIST, METHOD_GET,
				null);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void getTotalAlmountList() {
		Log.d(TAG, "getTotalAlmountList");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Runnable r = new Transmitter(FUNCTION_GET_TOTAL_ALMOUNT_LIST,
				METHOD_POST, httpvalue.getTotalAlmountListNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void getTotalAlmount() {
		Log.d(TAG, "getTotalAlmount");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Log.d("ooxx",
				"TotalAlmount NVP : " + httpvalue.getTotalAlmountListNVP());
		Runnable r = new Transmitter(FUNCTION_GET_TOTAL_ALMOUNT, METHOD_POST,
				httpvalue.getTotalAlmountNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean editProfile() {
		Log.d(TAG, "editProfile");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Runnable r = new Transmitter(FUNCTION_EDIT, METHOD_POST,
				httpvalue.getEditProfileNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mIsEditState;
	}

	public boolean addQuotedPrice() {
		Log.d(TAG, "addQuotedPrice");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Runnable r = new Transmitter(FUNCTION_ADD_QUOTEDPRICE, METHOD_POST,
				httpvalue.getAddQuotedPriceNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mIsQuotedPriceState;
	}

	public void getProfile() {
		Log.d(TAG, "getProfile");
		Runnable r = new Transmitter(FUNCTION_GET_PROFILE, METHOD_GET, null);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void getCatrogryList() {
		Log.d(TAG, "getCatrogryList");
		Runnable r = new Transmitter(FUNCTION_GET_CATEGORY_LIST, METHOD_GET,
				null);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "mCateGoryList = " + mCateGoryList);
	}

	public void getChannel() {
		Log.d(TAG, "getChannel");
		Runnable r = new Transmitter(FUNCTION_GET_CHANNEL, METHOD_GET, null);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean getLocatedChannelMarkets() {
		Log.d(TAG, "getLocatedChannelMarkets");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Runnable r = new Transmitter(FUNCTION_GET_LOCATED_CHANNELMARKETS,
				METHOD_POST, httpvalue.getLocatedChannelMarketsNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mIsLocatedChannelMarkets;
	}
	public boolean getDeleteUserWishList() {
		Log.d(TAG, "getDeleteUserWishList");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		Runnable r = new Transmitter(FUNCTION_GET_DELETE_USERWISH_LIST,
				METHOD_POST, httpvalue.getDeleteUserWishListNVP());
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mIsDeleteUserWishList;
	}

	public void getProductList(int mode) {
		Log.d(TAG, "getProductList");
		HttpValue httpvalue = HttpValue.getInstance(mContext);
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		switch (mode) {
		case HttpValue.PRODUCTLIST_MODE_ALLLIST:
			nvp = httpvalue.getProductListNVP();
			break;
		case HttpValue.PRODUCTLIST_MODE_SEARCH:
			nvp = httpvalue.getProductSearchNVP();
			break;
		}
		Log.d("", "Ernest+ mode:" + mode+ " : " + nvp );
		Runnable r = new Transmitter(FUNCTION_GET_PRODUCT_LIST, METHOD_POST,
				nvp);
		Thread t = new Thread(r);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "mProductList = " + mProductSearch);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
