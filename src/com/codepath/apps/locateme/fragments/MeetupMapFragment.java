
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
import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.ServerModel.GetMultipleObjectListener;
import com.codepath.apps.locateme.models.ServerModel.GetSingleObjectListener;
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
    private LatLng mLocation = null;
    private GoogleMap map;
    private final List<Marker> userMarkers = new ArrayList<Marker>();
    private static View view;

    // mock!
    private static Random RANDOM = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // From here
        // http://stackoverflow.com/questions/14083950/duplicate-id-tag-null-or-parent-id-with-another-fragment-for-com-google-androi
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

        removeOldMarkers();

        String meetupId = getArguments().getString("meetupId");
        if (meetupId != null) {
            Meetup.byObjectId(meetupId, Meetup.class, new GetSingleObjectListener<Meetup>() {

                @Override
                public void onSuccess(Meetup meetup) {
                    onLoadedMeetup(meetup);
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }

            });
        }

        return view;
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

    private void onLoadedMeetup(Meetup meetup) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(meetup.name);
        markerOptions.snippet("The Meetup");
        markerOptions.position(mLocation);
        map.addMarker(markerOptions);

        UserMeetupState.byMeetupId(meetup.getId(),
                new GetMultipleObjectListener<UserMeetupState>() {

            @Override
            public void onSuccess(List<UserMeetupState> userStates) {
                User.byUserStates(userStates, new GetMultipleObjectListener<User>() {

                    @Override
                    public void onSuccess(List<User> users) {
                        for (User user : users) {
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
                            double lat = mLocation.latitude
                                    + ((RANDOM.nextDouble() - 0.5) * 0.025);
                            double lng = mLocation.longitude
                                    + ((RANDOM.nextDouble() - 0.5) * 0.025);
                            LatLng location = new LatLng(lat, lng);
                            BitmapDescriptor bd = BitmapDescriptorFactory
                                    .fromResource(resourceId);
                            MarkerOptions mo = new MarkerOptions();
                            mo.title(user.name)
                            .snippet(eta + " minutes away")
                            .icon(bd)
                            .position(location);
                            userMarkers.add(map.addMarker(mo));
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

    private void removeOldMarkers() {
        for (Marker marker : userMarkers) {
            marker.remove();
        }
    }

    private void moveToLatLng(LatLng start) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(start)
        .zoom(14)
        .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
