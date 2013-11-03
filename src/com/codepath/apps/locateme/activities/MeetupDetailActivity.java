package com.codepath.apps.locateme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.codepath.apps.locateme.R;

public class MeetupDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meetup_detail);
		setupViews();
	}

	private void setupViews() {

		//		new DatePickerDialog(MeetupDetailActivity.this, date, myCalendar
		//				.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
		//				myCalendar.get(Calendar.DAY_OF_MONTH)).show();
		//
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.meetup_detail, menu);
		return true;
	}

}
