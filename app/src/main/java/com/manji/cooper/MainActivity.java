package com.manji.cooper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.manji.cooper.adapter.DrawerAdapter;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.fragments.MainFragment;
import com.manji.cooper.fragments.ProductFragment;
import com.manji.cooper.fragments.ScannerFragment;
import com.manji.cooper.listeners.OnDataRetrievedListener;
import com.manji.cooper.managers.DataManager;
import com.manji.cooper.model.Constants;
import com.manji.cooper.utils.Utility;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements OnDataRetrievedListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private String[] drawerArrayList;
    private MainFragment mainFragment;
    private ScannerFragment scannerFragment;
    private ProductFragment productFragment;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private RelativeLayout drawerView;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerAdapter drawerAdapter;

    private Toolbar toolbar;
    private View frameLayout;
    private SearchView searchView;

    private DataManager dataManager;
    private ArrayList<CSVData> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        Utility.activity = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        frameLayout = findViewById(R.id.frame);
        drawerArrayList = getResources().getStringArray(R.array.drawer_array);

        // Set the Navigation Drawer up
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.drawer_listview);
        drawerView = (RelativeLayout) findViewById(R.id.drawer_view);

        // Set the drawer adapter
        drawerAdapter = new DrawerAdapter(this, drawerArrayList);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DrawerClickListener());

        // Drawer shadow
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Drawer toggle
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {

            public void onDrawerClosed(View v) {
                if (mainFragment.isVisible()) {
                    setToolbarTitle("");
                }

                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View v) {
                setToolbarTitle("");
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        // Set up the fragments
        mainFragment = new MainFragment();
        scannerFragment = new ScannerFragment();
        productFragment = new ProductFragment();

        // Load initial fragment
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frame, mainFragment)
                .commit();

        setToolbarTitle("");

        dataManager = DataManager.getInstance();
        dataManager.setOnDataRetrievedListener(this);
        dataManager.getData();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Set the ActionBar title to @title.
     */
    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void showScannerFragment() {
        getFragmentManager().beginTransaction()
                .add(R.id.frame, scannerFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showProductFragment() {
        productFragment.setData(data);

        getFragmentManager().beginTransaction()
                .add(R.id.frame, productFragment)
                .addToBackStack(null)
                .commit();
    }

    private class DrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            FragmentManager fm = getFragmentManager();

            if (i == 0) {
                if (!mainFragment.isVisible()) {
                    fm.beginTransaction().replace(R.id.frame, mainFragment).commit();
                    setToolbarTitle("");
                }
            } else if (i == 1) {
            } else if (i == 2) {
            }

            // Close the drawer
            drawerLayout.closeDrawer(drawerView);
        }
    }

    @Override
    public void onDataRetrieved(ArrayList<CSVData> data) {
        Log.d(TAG, "Data retrieved");
        this.data = data;
    }
}
