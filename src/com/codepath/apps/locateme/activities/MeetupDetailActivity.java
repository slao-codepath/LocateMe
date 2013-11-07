package com.codepath.apps.locateme.activities;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.Meetup;

public class MeetupDetailActivity extends Activity {
	private long userId;
	private long meetupId;
	private Location location;
	private EditText etName;
	private EditText etDate;
	private EditText etTime;
	private final Calendar myCalendar = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meetup_detail);

		meetupId = getIntent().getExtras().getLong("meetupId", 0);
		userId = getIntent().getExtras().getLong("userId");
		location = (Location) getIntent().getExtras().get("location");
		setupViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.meetup_detail, menu);
		return true;
	}

	private void setupViews() {
		if (meetupId == 0) {
			// we're creating a new meetup
			getActionBar().setTitle(getString(R.string.title_activity_meetup_detail_create));
		}
		etName = (EditText) findViewById(R.id.etName);

		TextView tvLocation = (TextView) findViewById(R.id.tvLocation);
		    
		tvLocation.setText("Location:\n[" + roundDouble(location.getLatitude()) + ", " + roundDouble(location.getLongitude()) + "]");

		// set up date picker
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				etDate.setText(dateFormat.format(myCalendar.getTime()));
			}
		};
		etDate = (EditText) findViewById(R.id.etDate);
		etDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					new DatePickerDialog(MeetupDetailActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar
							.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
				}
			}
		});
		etDate.setText(dateFormat.format(myCalendar.getTime()));

		// set up time picker
		final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
		final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);
				etTime.setText(timeFormat.format(myCalendar.getTime()));
			}
		};
		etTime = (EditText) findViewById(R.id.etTime);
		etTime.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					new TimePickerDialog(MeetupDetailActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),
							myCalendar.get(Calendar.MINUTE), true).show();
				}
			}
		});
		etTime.setText(timeFormat.format(myCalendar.getTime()));
	}

	private double roundDouble(double d) {
//		BigDecimal bd = new BigDecimal(d);
//		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		
		 BigDecimal bd = new BigDecimal(Double.toString(d));
		 bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		 return bd.doubleValue();
	}

	public void onAddFriends(View v) {
		Meetup meetup = new Meetup();
		meetup.name = etName.getText().toString();
		try {
			String timestamp = etDate.getText().toString() + " " + etTime.getText().toString();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
			Date date = format.parse(timestamp);
			meetup.timestamp = date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		meetup.setLocation(location);

		Intent i = new Intent(MeetupDetailActivity.this, FriendsActivity.class);
		i.putExtra("meetup", meetup);
		i.putExtra("userId", userId);
		startActivity(i);
		finish();
	}
}
