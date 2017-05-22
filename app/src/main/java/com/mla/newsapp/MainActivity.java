package com.mla.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mla.newsapp.adapters.ViewPagerAdapter;
import com.mla.newsapp.config.Constants;
import com.mla.newsapp.fragments.ImageFragment;
import com.mla.newsapp.utils.DemoUtil;
import com.mla.newsapp.views.SlidingTabLayout;


public class MainActivity extends ActionBarActivity implements ImageFragment.OnFragmentInteractionListener {


    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mDrawerListItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CharSequence Titles[] = {getResources().getString(R.string.main_page),getResources().getString(R.string.article), getResources().getString(R.string.image), getResources().getString(R.string.video)};
        AppInit.getInstance().initialize(this);
        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            toolbar.setNavigationContentDescription(R.string.navigation_icon_desc);
            setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                    } else {
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                    }

                }
            });
        }


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Titles.length);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabs.setDistributeEvenly(true);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
            //mla
        // Setting the ViewPager For the SlidingTabsLayout India
        tabs.setViewPager(pager);


        mDrawerListItems = getResources().getStringArray(R.array.items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.menu_items, mDrawerListItems));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(Constants.IntentActionType.CATEGORY_CHANGE.toString());
                intent.putExtra(Constants.IntentExtraKey.CATEGORY.toString(), mDrawerListItems[i]);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        });


//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.i("Drawer_Click", intent.getStringExtra(Constants.IntentExtraKey.CATEGORY.toString()));
//            }
//        };
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constants.IntentActionType.CATEGORY_CHANGE.toString());
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver,filter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.exoplayer_test){

            Intent playerIntent = new Intent(this, PlayerActivity.class)
                    .setData(Uri.parse("http://html5demos.com/assets/dizzy.mp4"))
                    .putExtra(PlayerActivity.CONTENT_ID_EXTRA, "")
                    .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, DemoUtil.TYPE_MP4);
            startActivity(playerIntent);
            return true;


        }

        return super.onOptionsItemSelected(item);
    }

/*
    @Override
    public void onFragmentInteraction(Uri uri) {

    }*/


    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    protected void onDestroy() {
        super.onDestroy();
        AppInit.getInstance().destroy();
    }

}
