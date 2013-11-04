package com.codepath.apps.locateme.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.Meetup;

public class MeetupDetailActivity extends Activity {

	EditText etDate;
	EditText etTime;
	Calendar myCalendar = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meetup_detail);
		setupViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.meetup_detail, menu);
		return true;
	}

	private void setupViews() {
		// set up date picker
		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
				etDate.setText(sdf.format(myCalendar.getTime()));
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

		// set up time picker
		final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
				etTime.setText(sdf.format(myCalendar.getTime()));
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
	}

	public void onSave(View v) {
		new Meetup();
		finish();
	}
}
