package com.codepath.apps.locateme.fragments;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.activities.LoginActivity;
import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.User;
import com.codepath.apps.locateme.models.UserMeetupState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MeetupMapFragment extends Fragment {
	//	private MapFragment mMapFragment;
	private Meetup meetup = null;
	private LatLng mLocation = null;
	private GoogleMap map;
	private final List<Marker> userMarkers = new ArrayList<Marker>();
	private static View view;

	//mock!
	private static Random RANDOM = new Random();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		long meetupId = getArguments().getLong("meetupId");
		if (meetupId > 0) {
			meetup = Meetup.byId(meetupId);
			Location location = meetup.getLocation();
			mLocation = new LatLng(location.getLatitude(), location.getLongitude());
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//From here http://stackoverflow.com/questions/14083950/duplicate-id-tag-null-or-parent-id-with-another-fragment-for-com-google-androi
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.fragment_meetupmap, container, false);
			map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		} catch (InflateException e) {
			/* map is already there, just return view as it is */
		}

		//		View view = inflater.inflate(R.layout.fragment_meetupmap, container,
		//				false);

		// @+id/meetup_map_frame

		//
		//		mMapFragment = MapFragment.newInstance();
		//
		//		FragmentTransaction fragmentTransaction = getFragmentManager()
		//				.beginTransaction();
		//		fragmentTransaction.add(R.id.meetup_map_frame, mMapFragment);
		//		fragmentTransaction.commit();

		if (meetup != null) {
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.title(meetup.name);
			markerOptions.snippet("The Meetup");
			markerOptions.position(mLocation);
			map.addMarker(markerOptions);

			List<UserMeetupState> states = UserMeetupState.byMeetupId(meetup.getId());

			for (UserMeetupState state : states) {
				User user = User.byId(state.userId);
				if (state.userId == LoginActivity.loggedInUser.getId()) {
					user.name = "You";
				}
				int resourceId = R.drawable.unknown;
				int transit = RANDOM.nextInt(3);
				int eta = 10 + RANDOM.nextInt(20);
				switch (transit) {
				case 0:
					resourceId = R.drawable.car;
					break;
				case 1:
					resourceId = R.drawable.transit;
					break;
				case 2:
					resourceId = R.drawable.walk;
					break;
				}
				double lat = mLocation.latitude + ((RANDOM.nextDouble() - 0.5) * 0.025);
				double lng = mLocation.longitude + ((RANDOM.nextDouble() - 0.5) * 0.025);
				LatLng location = new LatLng(lat, lng);
				BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(resourceId);
				MarkerOptions mo = new MarkerOptions();
				mo.title(user.name)
				.snippet(eta + " minutes away")
				.icon(bd)
				.position(location);
				userMarkers.add(map.addMarker(mo));
			}
		}


		return view;
	}

	private void moveToLatLng(LatLng start) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(start)
		.zoom(14)
		.build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

		//		map = findFragmentById(R.id.map)).getMap();
		//		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mLocation == null) {
			Location location = MockData.getCurrentSelectedPosition();
			mLocation = new LatLng(location.getLatitude(), location.getLongitude());
		}
		moveToLatLng(mLocation);
	}
}
