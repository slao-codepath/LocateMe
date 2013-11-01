package com.codepath.apps.locateme.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.codepath.apps.locateme.FragmentTabListener;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.fragments.MeetupListFragment;
import com.codepath.apps.locateme.fragments.MeetupMapFragment;

public class ShowMeetupsActivity extends FragmentActivity implements TabListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meetup_list);
		setupTabs();
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
				.newTab()
				.setText("List")
				.setTag("MeetupListFragment")
				.setTabListener(
						new FragmentTabListener<MeetupListFragment>(R.id.frame_container, this, "first",
								MeetupListFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Map")
				.setTag("MeetupMapFragment")
				.setTabListener(
						new FragmentTabListener<MeetupMapFragment>(R.id.frame_container, this, "second",
								MeetupMapFragment.class));

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

}
