package com.codepath.apps.locateme.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.codepath.apps.locateme.FragmentTabListener;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.fragments.MeetupMapFragment;
import com.codepath.apps.locateme.fragments.MeetupStatusFragment;

public class MeetupStatusActivity extends FragmentActivity implements TabListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		//		ft.add(R.id.meetup_status_frame_container, new MeetupStatusFragment());
		//		ft.commit();
		setContentView(R.layout.activity_meetup_status);
		setupTabs();
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Bundle args = new Bundle();
		//		args.putLong("userId", mCurrentUser.getId());

		Tab tab1 = actionBar
				.newTab()
				.setText("List")
				.setTag("MeetupStatusFragment")
				.setTabListener(
						new FragmentTabListener<MeetupStatusFragment>(R.id.meetup_status_frame_container, this, "first",
								MeetupStatusFragment.class, args));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Map")
				.setTag("MeetupMapFragment")
				.setTabListener(
						new FragmentTabListener<MeetupMapFragment>(R.id.meetup_status_frame_container, this, "second",
								MeetupMapFragment.class, args));
		actionBar.addTab(tab2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.meetup_status, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
