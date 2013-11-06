package com.codepath.apps.locateme.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.codepath.apps.locateme.MeetupStatusAdapter;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.User;
import com.codepath.apps.locateme.models.UserMeetupState;

import eu.erikw.PullToRefreshListView;

public class MeetupStatusFragment extends Fragment {
	private long meetupId;
	private PullToRefreshListView lvMeetupStatus;
	private MeetupStatusAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		meetupId = getArguments().getLong("meetupId");
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
		List<UserMeetupState> userStates = UserMeetupState.byMeetupId(meetupId);
		for (UserMeetupState userState : userStates) {
			users.add(User.byId(userState.userId));
		}
		adapter = new MeetupStatusAdapter(view.getContext(), users);

		lvMeetupStatus = (PullToRefreshListView) view.findViewById(R.id.lvMeetupStatus);
		lvMeetupStatus.setAdapter(adapter);
		lvMeetupStatus.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// Open dialog fragment
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				Fragment prev = getFragmentManager().findFragmentByTag("dialog");
				if (prev != null) {
					ft.remove(prev);
				}
				ft.addToBackStack(null);
				
				CharSequence[] options = {"Car", "Walk", "Transit"};
				DialogFragment newFragment = TransportModeFragment.newInstance(options);
				newFragment.show(ft, "dialog");
				
				return false;
			}
		});
	}

}
