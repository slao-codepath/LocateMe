package com.codepath.apps.locateme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.codepath.apps.locateme.R;

public class MeetupStatusActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meetup_status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.meetup_status, menu);
		return true;
	}

}
