package com.codepath.apps.locateme.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.locateme.FragmentTabListener;
import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.fragments.MeetupListFragment;
import com.codepath.apps.locateme.fragments.MeetupMapFragment;

public class ListMeetupsActivity extends FragmentActivity implements TabListener {
    private static final int CREATE_MEETUP_LOCATION_CODE = 241;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_list);
        userId = getIntent().getStringExtra("userId");
        setupTabs();
    }

    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Bundle args = new Bundle();
        args.putString("userId", userId);

        Tab tab1 = actionBar
                .newTab()
                .setText("List")
                .setTag("MeetupListFragment")
                .setTabListener(
                        new FragmentTabListener<MeetupListFragment>(R.id.frame_container, this, "first",
                                MeetupListFragment.class, args));
        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        Tab tab2 = actionBar
                .newTab()
                .setText("Map")
                .setTag("MeetupMapFragment")
                .setTabListener(
                        new FragmentTabListener<MeetupMapFragment>(R.id.frame_container, this, "second",
                                MeetupMapFragment.class, args));
        actionBar.addTab(tab2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meetup_list, menu);
        return true;
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    public void onCreateMeetupAction(MenuItem mi) {
        Intent i = new Intent(ListMeetupsActivity.this, MeetupCreateActivity.class);
        startActivityForResult(i, CREATE_MEETUP_LOCATION_CODE);
        // showCreateActivityDetails();
    }

    // FirstActivity, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CREATE_MEETUP_LOCATION_CODE) {
            //			Toast.makeText(this, data.getExtras().getDouble("lat") + "" + data.getExtras().getDouble("lng"),
            //					Toast.LENGTH_SHORT).show();

            MockData.setSelectedCreateLocation(data.getExtras().getDouble("lat"), data.getExtras().getDouble("lng"));
            showCreateActivityDetails();
        }
    }

    private void showCreateActivityDetails() {
        Intent i = new Intent(ListMeetupsActivity.this, MeetupDetailActivity.class);
        i.putExtra("userId", userId);
        i.putExtra("location", MockData.getCurrentSelectedPosition());
        startActivity(i);
    }
}
