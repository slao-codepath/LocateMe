package com.codepath.apps.locateme.fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MeetupMapFragment extends Fragment {
//	private MapFragment mMapFragment;

	private static View view;
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
	    } catch (InflateException e) {
	        /* map is already there, just return view as it is */
	    }
	    return view;
	    
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

	}
	
	private void moveToLatLng(LatLng start) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(start).zoom(14).build();
//		map = findFragmentById(R.id.map)).getMap();
//		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Location currentMeetupPosition = MockData.getCurrentSelectedPosition();
		drawCurrentPositions(currentMeetupPosition);
	}

	private void drawCurrentPositions(Location center) {
		
		
	}
	
	

}
