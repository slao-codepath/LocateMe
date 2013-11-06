package com.codepath.apps.locateme;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.locateme.models.User;
import com.codepath.apps.locateme.models.User.TransportMode;

public class MeetupStatusAdapter extends ArrayAdapter<User> {
	TextView tvEtaStatus;
	ImageView ivTransportMode;

	public MeetupStatusAdapter(Context context, List<User> users) {
		super(context, 0, users);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User user = getItem(position);
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.meetup_status_item, null);
		}

		TextView tvFriendName = (TextView) view.findViewById(R.id.tvFriendName);
		ivTransportMode = (ImageView) view.findViewById(R.id.ivTransportMode);
		tvEtaStatus = (TextView) view.findViewById(R.id.tvEtaStatus);

		tvFriendName.setText(user.name);

		int resId = getTransportModeDrawable(user.currentTransitMode);
		ivTransportMode.setImageResource(resId);

		//		String eta = getEta(user.getLocation(), user.eta);

		if (user.eta > 0) {
			if (user.eta == 1) {
				tvEtaStatus.setText(user.eta + " minute");
			} else {
				tvEtaStatus.setText(user.eta + " minutes");
			}
		} else {
			tvEtaStatus.setText("Arrived!");
		}

		return view;
	}

	private String getEta(Location currentLocation, TransportMode currentTransportMode) {
		// TODO: To be filled in
		return "1 hour";
	}

	private int getTransportModeDrawable(TransportMode currentTransportMode) {
		if (currentTransportMode == TransportMode.CAR) {
			return R.drawable.car;
		}
		if (currentTransportMode == TransportMode.PUBLIC) {
			return R.drawable.transit;
		}
		if (currentTransportMode == TransportMode.WALK) {
			return R.drawable.walk;
		}
		else {
			return R.drawable.unknown;
		}
	}

	public User getUser(int position) {
		return getItem(position);
	}
}
