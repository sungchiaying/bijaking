package com.ourpower.kingofprice;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ourpower.object.LocationList;
import com.ourpower.tool.HttpDataTransmitter;
import com.ourpower.tool.HttpValue;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {
    private static final String TAG = "NavigationDrawerFragment";

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the
     * user manually expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    protected final static int POSITION_DRAWER_ITEM_SOTRE_SELECTION = 1;
    protected final static int POSITION_DRAWER_ITEM_WISH_LIST = 2;
    protected final static int POSITION_DRAWER_ITEM_MEMBER_CENTER = 3;
    //	protected final static int POSITION_DRAWER_ITEM_MANUFACTURER_LOGING = 4;
    protected final static int POSITION_DRAWER_ITEM_ABOUT_US = 4;
    protected final static int POSITION_DRAWER_ITEM_LEADINGAPP = 5;

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    private OnLogButtonClickListener mListener;
    private OnCitySelectListener mCitySelectListener;
    private OnOrderChangeListener mOrderChangeListener;
    private OnCitySelectDismissListener mCitySelectDismissListener;
    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 1;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private Context mContext;
    private Menu mMenu;
    // dailog----------
    private Dialog city_select_Dialog;

    // dialog Record 紀錄 dialog dismiss之前的狀態---------------------------------
    private String Dialog_Sequence_Record = "high"; //save checked 儲存選擇狀態
    private int Dialog_City_Record = 0; //save checked
    private int Dialog_district_Record = 0; //save checked
    private String Dialog_Record = "goto_city_select"; // save goto Record
    //------------------------------------------------------------------------

    private String mCountry;
    private ArrayList<String[]> mRegionList = new ArrayList<String[]>();
    private ArrayList<String> mCityList = new ArrayList<String>();
    private boolean mIsLowOrder = true;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "NavigationDrawerFragment onCreate !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // Read in the flag indicating whether or not the user has demonstrated
        // awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState
                    .getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        // /////////////////////////////////////////////////////////City_select
        mContext = getActivity();
        HttpDataTransmitter mHttpDataTransmitter = new HttpDataTransmitter(mContext);
        mHttpDataTransmitter.getLocationList();

        // TODO
        LocationList[] locationList;
        locationList = HttpValue.getInstance(mContext).getLocationListInfo();

        String[][] list_City_temp = new String[locationList.length][];

        mRegionList.add(new String[]{});
        mCityList.add("不分區");

        for (int j = 0; j < locationList.length; j++) {

            list_City_temp[j] = new String[locationList[j].getZipCode().length];
            mCityList.add(locationList[j].getName());

            for (int i = 0; i < locationList[j].getZipCode().length; i++) {
                list_City_temp[j][i] = locationList[j].getZipCode()[i]
                        .getZipcodeName();

            }
            mRegionList.add(list_City_temp[j]);// set [][]
        }

        // ///////////////////////////////////////////////////////////

        // Select either the default item (0) or the last selected item.
        // selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of
        // actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        selectItem(position, true);
                    }
                });
        // mDrawerListView.setAdapter(new ArrayAdapter<String>(getActionBar()
        // .getThemedContext(),
        // android.R.layout.simple_list_item_activated_1,
        // android.R.id.text1, getResources().getStringArray(R.array.menu)));
        mDrawerListView.setAdapter(new DrawerListAdapter(getActivity()));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    private class DrawerListAdapter extends BaseAdapter {
        private String[] mMenuText;
        private TypedArray mMenuImgIds;
        private Drawable[] mMemuImgs;
        private LayoutInflater mInflater;
        private boolean mIsLogin;

        DrawerListAdapter(Context context) {
            mIsLogin = ((BaseActivity) getActivity()).isLogin();
            mInflater = LayoutInflater.from(context);
            if (mIsLogin) {
                mMenuText = getResources().getStringArray(R.array.menu_login);
                mMenuImgIds = getResources().obtainTypedArray(
                        R.array.menu_imgs_login);
            } else {
                mMenuText = getResources().getStringArray(R.array.menu_default);
                mMenuImgIds = getResources().obtainTypedArray(
                        R.array.menu_imgs_default);
            }
            int i = 0;
            final int len = mMenuImgIds.length();
            mMemuImgs = new Drawable[len];
            while (i < len) {
                mMemuImgs[i] = getResources().getDrawable(
                        mMenuImgIds.getResourceId(i, 0));
                i++;
            }
            mMenuImgIds.recycle();
        }

        @Override
        public int getCount() {
            return mMenuText.length + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("Michael", "getView()");
            if (convertView == null) {
                if (position == 0) {
                    convertView = mInflater
                            .inflate(R.layout.drawer_menu_list_item_login,
                                    parent, false);
                } else {
                    convertView = mInflater.inflate(
                            R.layout.drawer_menu_list_item, parent, false);
                }
            }
            if (position == 0) {
                Button logButton = (Button) convertView
                        .findViewById(R.id.log_button);
                Button user_name = (Button) convertView
                        .findViewById(R.id.user_name);
                if (mIsLogin) {
                    logButton.setText(R.string.drawer_menu_item_logout);
                    String name = HttpValue.getInstance(mContext).getMemberName();
                    Log.d(TAG, "Michael, name = " + name);
                    if (name.equals("")) {
                        name = getString(R.string.user_name_default);
                    }
                    user_name.setText(name);
                    user_name.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((BaseActivity) getActivity()).selectItem(POSITION_DRAWER_ITEM_MEMBER_CENTER, true);
                        }
                    });
                } else {
                    logButton.setText(R.string.drawer_menu_item_login);
                    user_name.setText(getString(R.string.user_name_guest));
                }

                logButton.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeDrawer();
                        if (mListener != null) {
                            mListener.OnLogButtonClick(mIsLogin);
                        }
                    }
                });
                return convertView;
            }

            TextView text = (TextView) convertView.findViewById(R.id.item_name);
            text.setText(mMenuText[position - 1]);
            text.setBackgroundDrawable(mMemuImgs[position - 1]);
            return convertView;
        }
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null
                && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation
     * drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // Disable the swipe gesture
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open, /*
                                                 * "open drawer" description for
                                                 * accessibility
                                                 */
                R.string.navigation_drawer_close /*
                                                 * "close drawer" description for
                                                 * accessibility
                                                 */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls
                // onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                View searchView = null;

                try {
                    searchView = mMenu.findItem(R.id.action_search)
                            .getActionView();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (searchView != null) {
                    searchView.clearFocus();
                }
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to
                    // prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
                            .apply();
                }

                getActivity().invalidateOptionsMenu(); // calls
                // onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce
        // them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void selectItem(int position, boolean addToBackStack) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        closeDrawer();
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position, addToBackStack);
        }
    }

    public void closeDrawer() {
        if (mDrawerLayout != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mDrawerLayout.closeDrawer(mFragmentContainerView);
                }
            }, 50);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
            mListener = (OnLogButtonClickListener) activity;
            mCitySelectListener = (OnCitySelectListener) activity;
            mOrderChangeListener = (OnOrderChangeListener) activity;
            mCitySelectDismissListener = (OnCitySelectDismissListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar.
        // See also
        // showGlobalContextActionBar, which controls the top-left area of the
        // action bar.
        mMenu = menu;
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_search:
                // Toast.makeText(getActivity(), "search",
                // Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:

                // Dialog item_select_Dialog;
                // 設定模組與 Dialog 的風格
                mCitySelectDismissListener.CitySelectDismiss(false);
                city_select_Dialog = new Dialog(getActivity(),
                        R.style.selectorDialog);// (NavigationDrawerFragment.this ,
                // R.style.selectorDialog);
                city_select_Dialog.setContentView(R.layout.city_select);// 設定dialogXML
                city_select_Dialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.d("ooxx", "Ernest dissmiss" + dialog);
                        mCitySelectDismissListener.CitySelectDismiss(true);
                    }

                });
                opendialog_for_Actionbar(city_select_Dialog);// (dialog)

                // Toast.makeText(getActivity(), "settings", Toast.LENGTH_SHORT)
                // .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateDrawerMenuResources() {
        mDrawerListView.setAdapter(new DrawerListAdapter(getActivity()));
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to
     * show the global app 'context', rather than just what's in the current
     * screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must
     * implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position, boolean addToBackStack);
    }

    public static interface OnLogButtonClickListener {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void OnLogButtonClick(boolean isLogin);
    }

    // city_select_dailog---------------------------------
    // ---------------------------------------------------
    public void opendialog_for_Actionbar(Dialog dialog) {

        int actionBarHeight = 0;
        int status_bar_height = 0;
        // load出 長寬
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(
                android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    getResources().getDisplayMetrics());
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            status_bar_height = getResources()
                    .getDimensionPixelSize(resourceId);
        }

        // 由程式設定 Dialog 視窗外的明暗程度, 亮度從 0f 到 1f
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        lp.dimAmount = 0f;// dialog背景透明度
        lp.x = 0; // 新位置X坐标
        lp.y = actionBarHeight;// 設定dialogY座標
        lp.width = metrics.widthPixels; // 宽度
        lp.height = metrics.heightPixels - actionBarHeight - status_bar_height;    // 高度
        lp.alpha = 1f; // 透明度
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        goto_city_select();
        // onCreateLeftlist(list_City);// city_select_listview_left
        // onCreateRightlist(list_district_null); // city_select_listview_right

        // dialog.dismiss();//消失
        dialog.show();

        // set dismissbtn 讓listview 再觸碰listview底下透明地方 能夠執行 dismiss() 先註解目前用不到
		/*
		 * Button dismiss_btn_left = (Button) city_select_Dialog
		 * .findViewById(R.id.dismiss_btn_left);
		 * dismiss_btn_left.setOnClickListener(new Button.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { city_select_Dialog.dismiss();
		 * } }); Button dismiss_btn_right = (Button) city_select_Dialog
		 * .findViewById(R.id.dismiss_btn_right);
		 * dismiss_btn_right.setOnClickListener(new Button.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { city_select_Dialog.dismiss();
		 * } });
		 */

        if (Dialog_Record.equals("goto_city_select")) //
            goto_city_select();
        else if (Dialog_Record.equals("goto_sequence_select"))
            goto_sequence_select();
        else
            goto_city_select();
        // goto_city_select_sequence();

    }

    // 換到
    private void goto_sequence_select() {

        city_select_Dialog.setContentView(R.layout.city_select_sequence);// set
        // XML
        final RadioButton radio_hight = (RadioButton) city_select_Dialog
                .findViewById(R.id.radio_hight);
        radio_hight.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(city_select_Dialog.getContext(),
                        radio_hight.getText(), Toast.LENGTH_SHORT).show();
                Dialog_Sequence_Record = "hight"; // save checked
                if (!mIsLowOrder) {
                    mOrderChangeListener.OnOrderChange(true);
                    mIsLowOrder = true;
                }
            }
        });

        final RadioButton radio_low = (RadioButton) city_select_Dialog
                .findViewById(R.id.radio_low);
        radio_low.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(city_select_Dialog.getContext(),
                        radio_low.getText(), Toast.LENGTH_SHORT).show();
                Dialog_Sequence_Record = "low"; // save checked
                if (mIsLowOrder) {
                    mOrderChangeListener.OnOrderChange(false);
                    mIsLowOrder = false;
                }
            }
        });

        if (Dialog_Sequence_Record.equals("hight")) //判斷上一次的Checked
            radio_hight.setChecked(true);
        else if (Dialog_Sequence_Record.equals("low"))
            radio_low.setChecked(true);


        TextView city_title_Region = (TextView) city_select_Dialog
                .findViewById(R.id.city_title_Region);
        city_title_Region.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_Record = "goto_city_select";
                goto_city_select();

                Toast.makeText(city_select_Dialog.getContext(), "區域", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void goto_city_select() {

        city_select_Dialog.setContentView(R.layout.city_select);// 設定dialogXML
        onCreateLeftlist(mRegionList);// city_select_listview_left
        onCreateRightlist(mRegionList.get(Dialog_City_Record)); // city_select_listview_right

        TextView city_title_Sequence = (TextView) city_select_Dialog
                .findViewById(R.id.city_title_Sequence);
        city_title_Sequence.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Record = "goto_sequence_select";
                goto_sequence_select();
                Toast.makeText(city_select_Dialog.getContext(), "排序", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void onCreateLeftlist(final ArrayList<String[]> list_City_temp) {
        ListView listview_left = (ListView) city_select_Dialog
                .findViewById(R.id.listview_left);
        final RadioAdapter_left adapter_left = new RadioAdapter_left(
                city_select_Dialog.getContext());
        listview_left.setAdapter(adapter_left);

        listview_left.setItemChecked(Dialog_City_Record, true);// set Record Checked


        listview_left.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                adapter_left.notifyDataSetChanged();// 刷新
                //

                Dialog_City_Record = arg2; // save checked
                Dialog_district_Record = 0; // reset district checked

                //做出在不點擊 list right 的情況下會先set值
                if (arg2 == 0)
                    mCitySelectListener.onCitySelect("", "");
                else
                    mCitySelectListener.onCitySelect(mCityList.get(arg2), "");// set district in mCountry

                onCreateRightlist(list_City_temp.get(arg2));
                if (arg2 == 0) {
                    mCitySelectListener.onCitySelect("", "");
                }
                mCountry = mCityList.get(arg2);


                // Toast.makeText(arg1.getContext(),
                // "點選的縣市是.." + mCityList.get(arg2), 100).show();
            }
        });
    }

    class RadioHolder {
        private TextView radio_left;
        private TextView radio_right;

        public RadioHolder(View view) {
            TextView tv = (TextView) view.findViewById(R.id.item_radio);
            this.radio_left = tv;
            this.radio_right = tv;
            // this.item = (TextView) view.findViewById(R.id.item_text);
        }

    }

    class RadioAdapter_left extends BaseAdapter {

        private Context context;

        public RadioAdapter_left(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mCityList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mCityList.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RadioHolder holder_left;

            // item XML set 給 RadioButton
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.city_select_item, null);
                holder_left = new RadioHolder(convertView);
                convertView.setTag(holder_left);
            } else {
                holder_left = (RadioHolder) convertView.getTag();
            }

            holder_left.radio_left.setText(mCityList.get(position));// setText
            // for
            // city

            if (position % 2 == 0)// 偶數換顏色
                holder_left.radio_left
                        .setBackgroundResource(R.drawable.city_select_listview_background_white);
            else
                holder_left.radio_left
                        .setBackgroundResource(R.drawable.city_select_listview_background_gray);
            return convertView;
        }

    }

    private void onCreateRightlist(final String[] list_district) {
        ListView listview_right = (ListView) city_select_Dialog
                .findViewById(R.id.listview_right);
        final RadioAdapter_right adapter_right = new RadioAdapter_right(
                city_select_Dialog.getContext(), list_district);
        listview_right.setAdapter(adapter_right);
        listview_right.setItemChecked(Dialog_district_Record, true);// set save checked
        listview_right.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                adapter_right.notifyDataSetChanged();// 刷新

                // Toast.makeText(arg1.getContext(),
                // "點選的區域是.." + list_district[arg2], 100).show();
                if (arg2 == 0) {
                    mCitySelectListener.onCitySelect(mCountry, "");
                } else {
                    mCitySelectListener.onCitySelect(mCountry,
                            list_district[arg2]);
                }

                Dialog_district_Record = arg2; // save checked Record
            }
        });
    }

    class RadioAdapter_right extends BaseAdapter {

        private Context context;
        // private ArrayList<String> list_district = new ArrayList<String>();
        private String[] list_district = null;

        public RadioAdapter_right(Context context, String[] list_district) {
            this.list_district = list_district;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list_district.length;
        }

        @Override
        public Object getItem(int arg0) {
            return list_district[arg0];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RadioHolder holder_right;

            // item XML set 給 RadioButton
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.city_select_item, null);
                holder_right = new RadioHolder(convertView);
                convertView.setTag(holder_right);
            } else {
                holder_right = (RadioHolder) convertView.getTag();
            }

            holder_right.radio_right.setText(list_district[position]);// setText
            // for
            // city

            if (position % 2 == 0)// 偶數換顏色
                holder_right.radio_right
                        .setBackgroundResource(R.drawable.city_select_listview_background_white);
            else
                holder_right.radio_right
                        .setBackgroundResource(R.drawable.city_select_listview_background_gray);
            return convertView;


        }

    }

    // ---------------------------------------------------
    // city_select_dailog---------------------------------

    public interface OnCitySelectListener {
        void onCitySelect(String country, String region);
    }

    public interface OnOrderChangeListener {
        void OnOrderChange(boolean isLowOrder);
    }

    public interface OnCitySelectDismissListener {
        void CitySelectDismiss(boolean isDismiss);
    }
}
