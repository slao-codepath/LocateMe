package com.codepath.apps.locateme.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.codepath.apps.locateme.MeetupStatusAdapter;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.User;
import com.codepath.apps.locateme.models.User.TransportMode;
import com.codepath.apps.locateme.models.UserMeetupState;

import eu.erikw.PullToRefreshListView;

public class MeetupStatusFragment extends Fragment {
	
	private static final int DIALOG_FRAGMENT = 1;
	private long meetupId;
	private PullToRefreshListView lvMeetupStatus;
	private MeetupStatusAdapter adapter;
	private User clickedUser;

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
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long rowId) {
				// Open dialog fragment
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				Fragment prev = getFragmentManager().findFragmentByTag("dialog");
				if (prev != null) {
					ft.remove(prev);
				}
				ft.addToBackStack(null);
				
				clickedUser = (User) adapter.getItemAtPosition(pos);
				Toast.makeText(getActivity(), "Got the user " + clickedUser.name, Toast.LENGTH_LONG).show();
				
				CharSequence[] options = {"Car", "Walk", "Public"};
				DialogFragment dialogFragment = TransportModeFragment.newInstance(options);
				dialogFragment.setTargetFragment(MeetupStatusFragment.this, DIALOG_FRAGMENT);
				dialogFragment.show(ft, "dialog");
				
				return false;
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK && clickedUser != null) {
                    String transportMode = data.getExtras().getString("selectedTransport");
                	if ("car".compareTo(transportMode) == 0) {
                		clickedUser.setTransportMode(TransportMode.CAR);
                	} else if ("public".compareTo(transportMode) == 0) {
                		clickedUser.setTransportMode(TransportMode.PUBLIC);
                	} else if ("walk".compareTo(transportMode) == 0) {
                		clickedUser.setTransportMode(TransportMode.WALK);
                	}
                }
                break;
        }
	}

}
