package com.codepath.apps.locateme;

import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.UserMeetupState;

public class MeetupsAdapter extends ArrayAdapter<Meetup> {
	private final long mUserId;

	public MeetupsAdapter(Context context, List<Meetup> meetups, long userId) {
		super(context, 0, meetups);
		mUserId = userId;
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
		tvDateTime.setText(DateFormat.format("MM-dd-yyyy hh:mm", meetup.timestamp));

		// update user-specific views
		TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
		UserMeetupState state = UserMeetupState.byIds(mUserId, meetup.getId());
		if (state != null) {
			tvStatus.setText(state.getStatusString());
		}

		return view;
	}
}
