package com.codepath.apps.locateme.activities;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.UserMeetupState;

public class FriendsActivity extends Activity {
	long userId;
	Meetup meetup;
	Set<String> allFriends;
	Set<String> selectedFriends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		Bundle extras = getIntent().getExtras();
		userId = extras.getLong("userId");
		meetup = (Meetup) extras.get("meetup");
		setupViews();
	}

	private void setupViews() {
		allFriends = MockData.USERS.keySet();
		selectedFriends = new HashSet<String>();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendsActivity.this, android.R.layout.simple_list_item_multiple_choice);
		ListView lvFriends = (ListView) findViewById(R.id.lvFriends);
		lvFriends.setAdapter(adapter);
		adapter.addAll(allFriends);

		lvFriends.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckedTextView checkedTextView = (CheckedTextView) view;
				checkedTextView.setChecked(!checkedTextView.isChecked());
				String friend = checkedTextView.getText().toString();
				if (checkedTextView.isChecked()) {
					selectedFriends.add(friend);
				} else {
					selectedFriends.remove(friend);
				}
			}
		});
	}

	public void onSave(View v) {
		meetup.save();
		saveUserToMeetup(userId);
		for (String friend : selectedFriends) {
			saveUserToMeetup(MockData.USERS.get(friend).getId());
		}
		Intent i = new Intent(this, MeetupStatusActivity.class);
		i.putExtra("meetupId", meetup.getId());
		startActivity(i);
		finish();
	}

	private void saveUserToMeetup(long userId) {
		UserMeetupState state = new UserMeetupState();
		state.meetupId = meetup.getId();
		state.userId = userId;
		state.save();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends, menu);
		return true;
	}

}
