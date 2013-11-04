package com.codepath.apps.locateme.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.locateme.MeetupStatusAdapter;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.User;

public class MeetupStatusFragment extends Fragment {
	
	private ListView lvMeetupStatus;
	private MeetupStatusAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_meetupstatus, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View view) {
		List<User> users = new ArrayList<User>();
		// Mock users
		for (int i = 0; i < 4; i++) {
			users.add(new User());
		}
		adapter = new MeetupStatusAdapter(view.getContext());
		adapter.addAll(users);
		
		lvMeetupStatus = (ListView) view.findViewById(R.id.lvMeetupStatus);
		lvMeetupStatus.setAdapter(adapter);
	}
	
}
