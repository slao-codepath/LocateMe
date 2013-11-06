package com.codepath.apps.locateme.fragments;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MeetupMapFragment extends Fragment {
//	private MapFragment mMapFragment;

	private static View view;
	private MapFragment mMapFragment;
	private MarkerOptions markerOptions;
	private Marker currentMarker;
	private List<Marker> friendsMarker = new ArrayList<Marker>();
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
	        mMapFragment = MapFragment.newInstance();

			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction.add(R.id.meetups_map_frame, mMapFragment);
			fragmentTransaction.commit();
			
			markerOptions = new MarkerOptions();
			markerOptions.title("Place");
			markerOptions.snippet("Place of your next meetup");
	    }catch (InflateException e) {
	    	               /* map is already there, just return view as it is */
	   }
    
	    return view;
	    		
	}
	
	private void moveToLatLng(LatLng start) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(start).zoom(14).build();
		mMapFragment.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        
//		mMapFragment = MapFragment.newInstance();

		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.meetups_map_frame, mMapFragment);
		fragmentTransaction.commit();
		
		markerOptions = new MarkerOptions();
		markerOptions.title("Place");
		markerOptions.snippet("Place of your next meetup");

		
		Location currentMeetupPosition = MockData.getCurrentSelectedPosition();
		drawCurrentPositions(currentMeetupPosition);
	}

	private void drawCurrentPositions(Location center) {		
		LatLng currentPosition = new LatLng(center.getLatitude(), center.getLongitude());
		moveToLatLng(currentPosition);
		
		markerOptions.position(currentPosition);
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(currentPosition).zoom(10).build();
		GoogleMap map = mMapFragment.getMap();
		map.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		removeMarkers();
		currentMarker = map.addMarker(markerOptions);
		
	}

	private void removeMarkers() {
		if(currentMarker != null)
			currentMarker.remove();
		for (Marker marker : friendsMarker) {
			marker.remove();
		}
	}
	
	

}
