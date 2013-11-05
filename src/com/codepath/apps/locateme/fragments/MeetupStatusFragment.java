package com.codepath.apps.locateme.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.locateme.MeetupStatusAdapter;
import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.User;

import eu.erikw.PullToRefreshListView;

public class MeetupStatusFragment extends Fragment {
	
	private PullToRefreshListView lvMeetupStatus;
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
		users.addAll(MockData.USERS.values());
		adapter = new MeetupStatusAdapter(view.getContext());
		adapter.addAll(users);
		
		lvMeetupStatus = (PullToRefreshListView) view.findViewById(R.id.lvMeetupStatus);
		lvMeetupStatus.setAdapter(adapter);
	}
	
}
