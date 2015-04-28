package com.ourpower.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.ourpower.adapter.ProductDetailsAdapter;
import com.ourpower.kingofprice.BaseActivity;
import com.ourpower.kingofprice.MainActivity;
import com.ourpower.kingofprice.R;
import com.ourpower.object.ProductObject;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

public class ProductDetailsFragment extends Fragment implements OnClickListener {
	private OnCommodityClickListener mListener;
	private static Context mContext;
	public static ListView lv_storelist;
	private TextView tv_commname, tv_commprice, tv_goodnum;
	private SmartImageView iv_commicon;
	private LinearLayout ll_addwl, ll_pressgood, ll_login_price;
	public static ProductObject productObj;
	public static ProductDetailsAdapter adapter;
	private long mLastClickTime;
	private boolean isInWishList = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_commodity, container,
				false);
		initview(view);
		return view;
	}

	private void initview(View view) {
		ll_login_price = (LinearLayout) view
				.findViewById(R.id.comm_ll_loginprice);
		ll_pressgood = (LinearLayout) view.findViewById(R.id.comm_ll_pressgood);
		ll_addwl = (LinearLayout) view.findViewById(R.id.comm_ll_addwl);
		iv_commicon = (SmartImageView) view.findViewById(R.id.comm_iv_commicon);
		tv_commname = (TextView) view.findViewById(R.id.comm_tv_commname);
		tv_commprice = (TextView) view.findViewById(R.id.comm_tv_commprice);
		tv_goodnum = (TextView) view.findViewById(R.id.comm_tv_goodnum);
		lv_storelist = (ListView) view.findViewById(R.id.comm_lv_commlist);
		adapter = new ProductDetailsAdapter(mContext,
				productObj.getMarketsobj());
		lv_storelist.setAdapter(adapter);

		ll_login_price.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isLogin()){
					return;
				}
				Intent i = getActivity().getIntent();
				Bundle bundle = new Bundle();
				bundle.putString("product_id", productObj.getProduct_id());
				bundle.putString("product_name", productObj.getProduct_name());
				i.putExtras(bundle);
				productObj.getProduct_name();
				mListener.OnCommodityClick(R.id.comm_ll_loginprice);

			}
		});

		ll_pressgood.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isLogin()){
					return;
				}
				
				HttpValue httpValue = HttpValue.getInstance(mContext);
				HttpDataTransmitter ndt = new HttpDataTransmitter(mContext);
				httpValue.setLikeNVP(httpValue.getServiceID(),
						productObj.getProduct_id());
				if (ndt.getLikeState()) {
					ndt.getCommodity();
					productObj = httpValue.getCommodityInfo();
					tv_goodnum.setText("" + getString(R.string.commodity_likes)
							+ productObj.getLiked()
							+ getString(R.string.commodity_people));
					Toast.makeText(mContext,
							getString(R.string.commodity_toast_like),
							Toast.LENGTH_SHORT).show();
				} else {
					new AlertDialog.Builder(mContext)
							.setTitle(
									Html.fromHtml("<font color='#33b5e5'>"
											+ "提示" + "</font>"))
							.setMessage("您已按過讚")
							.setPositiveButton("確定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).show();
					// Toast.makeText(context,
					// getString(R.string.commodity_toast_liked),
					// Toast.LENGTH_SHORT).show();
				}
			}
		});
		ll_addwl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SystemClock.elapsedRealtime() - mLastClickTime < 300) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				if (!isLogin()) {
					return;
				}
				
				HttpValue httpValue = HttpValue.getInstance(mContext);
				final HttpDataTransmitter ndt = new HttpDataTransmitter(
						mContext);
			
				ndt.getWantList();
				productObj = httpValue.getCommodityInfo();
				for (int i = 0; i < httpValue.getWantListInfo().length; i++) {
					if (httpValue.getWantListInfo()[i].getProduct_id().equals(
							productObj.getProduct_id())) {
						isInWishList = true;
						break;
					}else {
						isInWishList = false;
					}
				}
				if (isInWishList) {
					new AlertDialog.Builder(mContext)
							.setTitle(Html.fromHtml("<font color='#33b5e5'>"+ "重覆囉，商品已加入欲購清單" + "</font>"))
							.setPositiveButton("確定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).show();
				} else {
					AlertDialog.Builder addLikeListDialog = new AlertDialog.Builder(
							mContext);
					addLikeListDialog.setTitle(Html.fromHtml("<font color='#33b5e5'>" + "提示"+ "</font>"));
					addLikeListDialog.setMessage("是否將商品加入預購清單");
					addLikeListDialog.setPositiveButton("確定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (ndt.addWishList()) {
										new AlertDialog.Builder(mContext).setTitle(Html.fromHtml("<font color='#33b5e5'>"
																+ "提示"
																+ "</font>"))
												.setMessage("成功加入預購清單")
												.setPositiveButton("確定",new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
															}
														}).show();
									} else {
										new AlertDialog.Builder(mContext).setTitle(Html.fromHtml("<font color='#33b5e5'>"
																+ "加入清單失敗"
																+ "</font>"))
												.setPositiveButton("確定",new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
															}
														}).show();
									}
								}
							});
					addLikeListDialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					addLikeListDialog.show();
				}

				// Toast.makeText(context, httpValue.getAddWishInfo(),
				// Toast.LENGTH_LONG).show();

				// httpValue.setLikeNVP(httpValue.getServiceID(),productObj.getProduct_id());
				// if(ndt.getLikeState() == true){
				// }else {
				// Toast.makeText(context,
				// context.getResources().getText(R.string.commodity_toast_liked),
				// 3000).show();
				// }
			}
		});
		tv_commname.setText(productObj.getProduct_name());
		iv_commicon.setImageUrl(productObj.getImage1());
		tv_goodnum.setText("" + getString(R.string.commodity_likes)
				+ productObj.getLiked() + getString(R.string.commodity_people));
		tv_commprice.setText("" + getString(R.string.commodity_price)
				+ productObj.getMin_cost()
				+ getString(R.string.commodity_doller));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mContext = getActivity();
		ActionBar actionBar = getActivity().getActionBar();
		getValue();
	}

	private void getValue() {
		HttpDataTransmitter hdt = new HttpDataTransmitter(mContext);
		HttpValue httpValue = HttpValue.getInstance(mContext);
		hdt.getCommodity();
		productObj = httpValue.getCommodityInfo();
		httpValue.setAddWishNVP(productObj.getProduct_id());
		Log.d("ooxx", "product id: " + productObj.getProduct_id());
		Log.d("ooxx", "Ernest marketobj: " + productObj.getMarketsobj().length);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnCommodityClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnCommodityClickListener");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MainActivity.current_page = MainActivity.COMMODITY_PAGE;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// menu.clear();
		setTitle();
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void setTitle() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.show();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.commodity_title);
		// Log.d(TAG, "getTitle() = " + actionBar.getTitle());
	}

	public OnClickListener getOnClickListener(final int resourceId) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.OnCommodityClick(resourceId);
			}
		};
	}

	public interface OnCommodityClickListener {
		public void OnCommodityClick(int resourceId);
	}

	public static void AdapterRefresh() {
		adapter = new ProductDetailsAdapter(mContext,
				productObj.getMarketsobj());
		lv_storelist.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.wl_btn_seletarea:
		//
		// break;
		//
		// default:
		// break;
		// }
	}
	
	private boolean isLogin() {
		if(!((BaseActivity)getActivity()).isLogin()){
			new AlertDialog.Builder(mContext)
			.setTitle(Html.fromHtml("<font color='#33b5e5'>"+ "此功能需要會員登入 " + "</font>"))
			
			.setPositiveButton("確定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
						}
					}).show();
			return false;
		}else {
			return true;
		}
		
	}

}