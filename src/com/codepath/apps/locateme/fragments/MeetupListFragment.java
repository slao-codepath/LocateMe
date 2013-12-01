
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

import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.activities.LoginActivity;
import com.codepath.apps.locateme.activities.MeetupStatusActivity;
import com.codepath.apps.locateme.adapters.MeetupsAdapter;
import com.codepath.apps.locateme.fragments.ChangeMeetupStatusDialogFragment.ChangeMeetupStatusDialogListener;
import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.ServerModel.GetMultipleObjectListener;
import com.codepath.apps.locateme.models.ServerModel.GetSingleObjectListener;
import com.codepath.apps.locateme.models.UserMeetupState;
import com.codepath.apps.locateme.models.UserMeetupState.Status;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MeetupListFragment extends Fragment implements ChangeMeetupStatusDialogListener {
    private PullToRefreshListView lvMeetups;
    private MeetupsAdapter adapter;
    private String mUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserId = LoginActivity.loggedInUser.getId();
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
        adapter = new MeetupsAdapter(getActivity(), new ArrayList<Meetup>());
        lvMeetups.setAdapter(adapter);
        lvMeetups.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Meetup meetup = adapter.getItem(position);
                UserMeetupState.byUserMeetupId(mUserId, meetup.getId(),
                        new GetSingleObjectListener<UserMeetupState>() {

                    @Override
                    public void onSuccess(UserMeetupState userState) {
                        Status status = userState.status;
                        if (status == Status.ACTIVE || status == Status.ARRIVED) {
                            Intent i = new Intent(getActivity(), MeetupStatusActivity.class);
                            i.putExtra("meetupId", userState.meetupId);
                            startActivity(i);
                        } else {
                            showDialog(meetup, status);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        lvMeetups.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Meetup meetup = adapter.getItem(position);
                UserMeetupState.byUserMeetupId(mUserId, meetup.getId(),
                        new GetSingleObjectListener<UserMeetupState>() {

                    @Override
                    public void onSuccess(UserMeetupState userState) {
                        Status status = userState.status;
                        if (ChangeMeetupStatusDialogFragment.TRANSITIONS.get(status) != null) {
                            showDialog(meetup, status);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                });
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
        UserMeetupState.byUserId(mUserId, new GetMultipleObjectListener<UserMeetupState>() {

            @Override
            public void onSuccess(final List<UserMeetupState> userStates) {
                List<String> meetupIds = new ArrayList<String>();
                for (UserMeetupState object : userStates) {
                    meetupIds.add(object.meetupId);
                }
                Meetup.byIds(meetupIds, new GetMultipleObjectListener<Meetup>() {

                    @Override
                    public void onSuccess(List<Meetup> meetups) {
                        if (adapter == null) {

                        } else {
                            adapter.clear();
                            adapter.setUserStates(userStates);
                            adapter.addAll(meetups);
                        }
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
    }

    @Override
    public void onChangeMeetupStatusSelected(Meetup meetup, final Status status) {
        UserMeetupState.byUserMeetupId(mUserId, meetup.getId(), new GetSingleObjectListener<UserMeetupState>() {

            @Override
            public void onSuccess(UserMeetupState userState) {
                userState.status = status;
                userState.save();
                adapter.setUserState(userState);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
