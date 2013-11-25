package com.codepath.apps.locateme.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.locateme.MeetupStatusAdapter;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.activities.LoginActivity;
import com.codepath.apps.locateme.models.ServerModel.GetMultipleObjectListener;
import com.codepath.apps.locateme.models.User;
import com.codepath.apps.locateme.models.User.TransportMode;
import com.codepath.apps.locateme.models.UserMeetupState;

import eu.erikw.PullToRefreshListView;

public class MeetupStatusFragment extends Fragment {
    private final Random RANDOM = new Random();
    private static final int DIALOG_FRAGMENT = 1;
    private String meetupId;
    private PullToRefreshListView lvMeetupStatus;
    private MeetupStatusAdapter adapter;
    private User clickedUser;
    private List<Timer> mTimers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meetupId = getArguments().getString("meetupId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        final View view = inflater.inflate(R.layout.fragment_meetupstatus, container, false);
        UserMeetupState.byMeetupId(meetupId, new GetMultipleObjectListener<UserMeetupState>() {

            @Override
            public void onSuccess(final List<UserMeetupState> userStates) {
                List<String> userIds = new ArrayList<String>();
                for (UserMeetupState userState : userStates) {
                    userIds.add(userState.userId);
                }
                User.byIds(userIds, new GetMultipleObjectListener<User>() {

                    @Override
                    public void onSuccess(List<User> users) {
                        setupViews(view, userStates, users);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }

                });
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }

        });
        return view;
    }

    private void setupViews(View view, List<UserMeetupState> userStates, final List<User> users) {
        List<User> initialUsers = new ArrayList<User>();
        initialUsers.add(LoginActivity.loggedInUser);
        adapter = new MeetupStatusAdapter(view.getContext(), initialUsers);

        lvMeetupStatus = (PullToRefreshListView) view.findViewById(R.id.lvMeetupStatus);
        lvMeetupStatus.setAdapter(adapter);
        lvMeetupStatus.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View item, int pos, long rowId) {
                // Open dialog fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                clickedUser = (User) a.getItemAtPosition(pos + 1);
                //Toast.makeText(getActivity(), "Position " + pos + " Got the user " + clickedUser.name, Toast.LENGTH_LONG).show();

                CharSequence[] options = {"Car", "Walk", "Public"};
                DialogFragment dialogFragment = TransportModeFragment.newInstance(options);
                dialogFragment.setTargetFragment(MeetupStatusFragment.this, DIALOG_FRAGMENT);
                dialogFragment.show(ft, "dialog");
            }
        });

        mTimers = new ArrayList<Timer>();
        Timer timer = new Timer("add");
        timer.scheduleAtFixedRate(new TimerTask() {
            private int mIndex = 0;

            @Override
            public void run() {
                if (mIndex < users.size()) {
                    final User user = users.get(mIndex++);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.add(user);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    this.cancel();
                }
            }
        }, 2000, 2000);

        Timer timer1 = new Timer("schedule");
        timer1.scheduleAtFixedRate(new TimerTask() {
            int ticks = 0;

            @Override
            public void run() {
                boolean updated = false;
                for (int i = 0; i < adapter.getCount(); ++i) {
                    User user = adapter.getItem(i);
                    if (user.eta == 0)
                        continue;
                    if (user.transportMode == User.TransportMode.WALK) {
                        user.eta -= 1;
                    } else {
                        user.eta -= RANDOM.nextInt(5);
                    }
                    if (user.eta < 0) {
                        user.eta = 0;
                    } else if (user.eta > 10 && user.transportMode == User.TransportMode.WALK) {
                        if (ticks != 3) {
                            ticks++;
                        } else {
                            if (RANDOM.nextInt(2) > 0) {
                                user.transportMode = User.TransportMode.CAR;
                            } else {
                                user.transportMode = User.TransportMode.PUBLIC;
                            }
                        }
                    } else if (user.eta <= 3) {
                        user.transportMode = User.TransportMode.WALK;
                    }
                    updated = true;
                }
                if (updated) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    this.cancel();
                }
            }
        }, 0, 2000);
        mTimers.add(timer);
        mTimers.add(timer1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK && clickedUser != null) {
                    String transportMode = data.getExtras().getString("selectedTransport");
                    if ("car".compareTo(transportMode) == 0) {
                        clickedUser.transportMode = TransportMode.CAR;
                    } else if ("public".compareTo(transportMode) == 0) {
                        clickedUser.transportMode = TransportMode.PUBLIC;
                    } else if ("walk".compareTo(transportMode) == 0) {
                        clickedUser.transportMode = TransportMode.WALK;
                    }
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        for (Timer timer : mTimers) {
            timer.cancel();
        }
        super.onDetach();
    }


}
