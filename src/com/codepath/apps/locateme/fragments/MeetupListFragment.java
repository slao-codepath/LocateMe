package com.codepath.apps.locateme.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.locateme.MeetupsAdapter;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.Meetup;

import eu.erikw.PullToRefreshListView;

public class MeetupListFragment extends Fragment {
	// TODO: MOCK
	private static int MEETUP_LENGTH = 5;

	private long userId;
	List<Meetup> meetups = new ArrayList<Meetup>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userId = getArguments().getLong("userId");
		mockData();
	}

	// TODO: MOCK
	private void mockData() {
		for (int i=0; i < MEETUP_LENGTH; ++i) {
			meetups.add(new Meetup());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_meetuplist, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViews();
	}

	private void setupViews() {
		MeetupsAdapter adapter = new MeetupsAdapter(getActivity(), meetups, userId);
		PullToRefreshListView lvMeetups = (PullToRefreshListView) getActivity().findViewById(R.id.lvMeetups);
		lvMeetups.setAdapter(adapter);
	}
}
