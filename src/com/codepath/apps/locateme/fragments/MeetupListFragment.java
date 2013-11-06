package com.codepath.apps.locateme.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.codepath.apps.locateme.ChangeMeetupStatusDialogFragment;
import com.codepath.apps.locateme.ChangeMeetupStatusDialogFragment.ChangeMeetupStatusDialogListener;
import com.codepath.apps.locateme.MeetupsAdapter;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.activities.LoginActivity;
import com.codepath.apps.locateme.activities.MeetupStatusActivity;
import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.UserMeetupState;
import com.codepath.apps.locateme.models.UserMeetupState.Status;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MeetupListFragment extends Fragment implements ChangeMeetupStatusDialogListener {
	PullToRefreshListView lvMeetups;
	MeetupsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

	@Override
	public void onResume() {
		loadData();
		super.onResume();
	}

	private void setupViews() {
		lvMeetups = (PullToRefreshListView) getActivity().findViewById(R.id.lvMeetups);
		adapter = new MeetupsAdapter(getActivity(), new ArrayList<Meetup>(), LoginActivity.loggedInUser.getId());
		lvMeetups.setAdapter(adapter);
		lvMeetups.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Meetup meetup = adapter.getItem(position);
				UserMeetupState userMeetupState = UserMeetupState.byIds(LoginActivity.loggedInUser.getId(),
						meetup.getId());
				Status status = userMeetupState.getStatus();
				if (status == Status.ACTIVE || status == Status.ARRIVED) {
					Intent i = new Intent(getActivity(), MeetupStatusActivity.class);
					i.putExtra("meetupId", meetup.getId());
					startActivity(i);
				} else {
					showDialog(meetup, status);
				}
			}
		});
		lvMeetups.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id) {
				Meetup meetup = adapter.getItem(position);
				UserMeetupState userMeetupState = UserMeetupState.byIds(LoginActivity.loggedInUser.getId(),
						meetup.getId());
				Status status = userMeetupState.getStatus();
				if (ChangeMeetupStatusDialogFragment.TRANSITIONS.get(status) != null) {
					showDialog(meetup, status);
				}
				return true;
			}
		});
		lvMeetups.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData();
				lvMeetups.onRefreshComplete();
			}
		});
	}

	private void showDialog(Meetup meetup, Status status) {
		DialogFragment newFragment = new ChangeMeetupStatusDialogFragment();
		Bundle args = new Bundle();
		args.putSerializable("meetup", meetup);
		args.putSerializable("status", status);
		newFragment.setArguments(args);
		newFragment.setTargetFragment(MeetupListFragment.this, 1);
		newFragment.show(getFragmentManager(), "status");
	}

	private void loadData() {
		List<Meetup> meetups = new ArrayList<Meetup>();
		List<UserMeetupState> states = UserMeetupState.byUserId(LoginActivity.loggedInUser.getId());
		for (UserMeetupState state : states) {
			meetups.add(Meetup.byId(state.meetupId));
		}
		adapter.clear();
		adapter.addAll(meetups);
	}

	@Override
	public void onChangeMeetupStatusSelected(Meetup meetup, Status status) {
		UserMeetupState state = UserMeetupState.byIds(LoginActivity.loggedInUser.getId(), meetup.getId());
		state.setStatus(status);
		state.save();
		adapter.notifyDataSetChanged();
	}
}
