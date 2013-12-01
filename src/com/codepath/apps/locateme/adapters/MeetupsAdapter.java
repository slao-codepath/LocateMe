package com.codepath.apps.locateme.adapters;

import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.R.id;
import com.codepath.apps.locateme.R.layout;
import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.UserMeetupState;
import com.codepath.apps.locateme.models.UserMeetupState.Status;

public class MeetupsAdapter extends ArrayAdapter<Meetup> {
    private List<UserMeetupState> mUserStates;

    public MeetupsAdapter(Context context, List<Meetup> meetups) {
        super(context, 0, meetups);
    }

    private Status getStatus(String meetupId) {
        Status status = Status.UNKNOWN;
        for (UserMeetupState userState : mUserStates) {
            if (userState.meetupId.equals(meetupId)) {
                status = userState.status;
                break;
            }
        }
        return status;
    }

    public void setUserStates(List<UserMeetupState> userStates) {
        mUserStates = userStates;
    }

    public void setUserState(UserMeetupState newUserState) {
        for (UserMeetupState userState : mUserStates) {
            if (userState.meetupId.equals(newUserState.meetupId)) {
                userState.status = newUserState.status;
                break;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.meetup_item, null);
        }

        Meetup meetup = getItem(position);

        TextView tvName = (TextView) view.findViewById(R.id.tvMeetupName);
        tvName.setText(meetup.name);

        TextView tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
        tvDateTime.setText(DateFormat.format("MM-dd-yyyy hh:mm", meetup.time));

        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvStatus.setText(getStatus(meetup.getId()).toString());

        return view;
    }
}
