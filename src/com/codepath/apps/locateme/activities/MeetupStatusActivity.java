package com.codepath.apps.locateme.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.fragments.MeetupStatusFragment;

public class MeetupStatusActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.meetup_status_frame_container, new MeetupStatusFragment());
		ft.commit();
		setContentView(R.layout.activity_meetup_status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.meetup_status, menu);
		return true;
	}

}
