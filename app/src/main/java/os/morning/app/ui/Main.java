package os.morning.app.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import os.morning.app.R;
import os.morning.app.adapter.MainSectionedListAdapter;
import os.morning.app.fragment.BaseFragment;
import os.morning.app.fragment.DeptListFragment;

/**
 * APP 主界面
 * @author ZiQin
 * @version 1.0
 * @created 2014/7/21
 */
public class Main extends Activity {

    private ListView mDrawerList;
    private DrawerLayout mDrawer;
    private CustomActionBarDrawerToggle mDrawerToggle;
    private int mCurrentTitle= R.string.app_name;
    private int mSelectedFragment;
    private BaseFragment mBaseFragment;
    MainSectionedListAdapter mSectionedAdapter;

    private static String TAG= "APP_Main_Activity";

    //Used in savedInstanceState
    private static String BUNDLE_SELECTEDFRAGMENT = "BDL_SELFRG";

    private static final int CASE_TODOLIST = 0;
    private static final int CASE_TODOREMIND = 0;
    private static final int CASE_DONE = 1;
    private static final int CASE_UPDATEPWD = 2;


    private static final int CASE_DAY_REPORT = 3;

    private static final int CASE_WEEK_REPORT = 4;

    private static final int CASE_STATISTICS = 5;


    //private static final int CASE_REIMBURSEMENT = 7;

    private static final int CASE_APPLY_TRAVEL = 6;

    private static final int CASE_APPLY_LOAN = 7;

    private static final int CASE_TRAVEL = 8;

    private static final int CASE_DAILY_FEE = 9;



    private static final int CASE_ASK_LEAVE = 10;

    private static final int CASE_WORK_ATTENDANCE = 11;

    private static final int CASE_REGISTRATION = 12;

    private static final int CASE_CONTACTS = 13;



    private static final int CASE_HUMAN_RESOURCES = 14;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTEDFRAGMENT, mSelectedFragment);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawer.setDrawerShadow(R.drawable.cards_drawer_shadow, GravityCompat.START);

        _initMenu();
        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
        mDrawer.setDrawerListener(mDrawerToggle);


        if (savedInstanceState != null) {
            mSelectedFragment = savedInstanceState.getInt(BUNDLE_SELECTEDFRAGMENT);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (fragmentManager.findFragmentById(R.id.main)==null)
                mBaseFragment = selectFragment(mSelectedFragment);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
		 * The action bar home/up should open or close the drawer.
		 * ActionBarDrawerToggle will take care of this.
		 */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else{
            int id = item.getItemId();
            switch (id) {
                case R.id.action_refresh:
                    return true;
                case R.id.action_quit:
                    // quit App
                    SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    sp.edit().putBoolean("AUTO_LOGIN",false);
                    finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

        public CustomActionBarDrawerToggle(Activity mActivity, DrawerLayout mDrawerLayout) {
            super(
                    mActivity,
                    mDrawerLayout,
                    R.drawable.cards_navigation_drawer,
                    R.string.app_name,
                    mCurrentTitle);
        }

        @Override
        public void onDrawerClosed(View view) {
            getActionBar().setTitle(getString(mCurrentTitle));
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            getActionBar().setTitle(getString(R.string.app_name));
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            // Highlight the selected item, update the title, and close the drawer
            // update selected item and title, then close the drawer
            position = mSectionedAdapter.sectionedPositionToPosition(position);


            mDrawerList.setItemChecked(position, true);
            mBaseFragment = selectFragment(position);
            mSelectedFragment = position;

            mDrawer.closeDrawer(mDrawerList);
            if (mBaseFragment != null)
                openFragment(mBaseFragment);
        }
    }


    private BaseFragment selectFragment(int position) {
        BaseFragment baseFragment = null;

        switch (position) {
            case CASE_CONTACTS:
                baseFragment = new DeptListFragment();
                break;
            default:
                break;
        }

        return baseFragment;
    }


    private void openDialogFragment(DialogFragment dialogStandardFragment) {
        if (dialogStandardFragment != null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("carddemo_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            dialogStandardFragment.show(ft, "carddemo_dialog");
        }
    }

    private void openFragment(BaseFragment baseFragment) {
        if (baseFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.main, baseFragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            if (baseFragment.getTitleResourceId()>0)
                mCurrentTitle = baseFragment.getTitleResourceId();
        }
    }


    public static final String[] options = {
            "TODOlIST",
            "DONE",
            "CHANGE PASSWORD",
            "DAILY REPORT",
            "WEEK REPORT",
            "STATISTICS",
            "CASE1",
            "CASE2",
            "CASE3",
            "CASE4",
            "CASE5",
            "CASE6",
            "CASE7",
            "CASE8",
            "CASE9"
    };


    private void _initMenu() {
        mDrawerList = (ListView) findViewById(R.id.drawer);

        if (mDrawerList != null) {
            ArrayAdapter<String> mAdapter =new ArrayAdapter<String>(this,
                    R.layout.main_menu_item, options);

            List<MainSectionedListAdapter.Section> sections =
                    new ArrayList<MainSectionedListAdapter.Section>();

            sections.add(new MainSectionedListAdapter.Section(CASE_TODOLIST,"TODO"));
            sections.add(new MainSectionedListAdapter.Section(CASE_DAY_REPORT,"REPORT"));
            sections.add(new MainSectionedListAdapter.Section(CASE_APPLY_TRAVEL,"DEMO"));
            sections.add(new MainSectionedListAdapter.Section(CASE_ASK_LEAVE,"CASE"));
            sections.add(new MainSectionedListAdapter.Section(CASE_HUMAN_RESOURCES,"CASE2"));


            MainSectionedListAdapter.Section[] dummy = new MainSectionedListAdapter.Section[sections.size()];
            mSectionedAdapter = new MainSectionedListAdapter(this,R.layout.main_menu_section,mAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            mDrawerList.setAdapter(mSectionedAdapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
    }


}
