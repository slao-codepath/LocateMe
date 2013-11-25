package com.codepath.apps.locateme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.codepath.apps.locateme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MeetupCreateActivity extends FragmentActivity {

    private GoogleMap map;
    private MarkerOptions markerOptions;
    private Marker currentMarker;
    private LatLng start;
    private LatLng destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_create);

        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        markerOptions = new MarkerOptions();
        markerOptions.title("Place");
        markerOptions.snippet("Place of your next meetup");

        //		et = (EditText) findViewById(R.id.editText1);
        //		et.setOnEditorActionListener(new OnEditorActionListener() {
        //
        //			@Override
        //			public boolean onEditorAction(TextView v, int actionId,
        //					KeyEvent event) {
        //				if (actionId == EditorInfo.IME_NULL
        //						&& event.getAction() == KeyEvent.ACTION_DOWN) {
        //					resetMap();
        //				}
        //				return false;
        //			}
        //
        //		});
        //
        double lat = 37.7704;
        double lng = -122.404;
        start = new LatLng(lat, lng);
        moveToLatLng(start);

        // check if map is created successfully or not
        if (map == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        } else {
            show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meetup_create, menu);
        return true;
    }


    private void moveToLatLng(LatLng start) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(start).zoom(14).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //	private void resetMap() {
    //		String address = et.getText().toString();
    //		Geocoder geocoder = new Geocoder(this);
    //		List<Address> addresses;
    //		try {
    //			addresses = geocoder.getFromLocationName(address, 1);
    //			if (!addresses.isEmpty()) {
    //				Address addr = addresses.get(0);
    //				LatLng latlng = new LatLng(addr.getLatitude(),
    //						addr.getLongitude());
    //				moveToLatLng(latlng);
    //			}
    //		} catch (IOException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		}
    //
    //	}

    private void show() {
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng arg0) {
                if(currentMarker != null) {
                    currentMarker.remove();
                }

                markerOptions.position(arg0);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(arg0).zoom(12).build();
                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                currentMarker = map.addMarker(markerOptions);
            }
        });

        map.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.getPosition();
                //				Toast.makeText(
                //						getApplicationContext(),
                //						"Loc is " + marker.getPosition().latitude + ","
                //								+ marker.getPosition().longitude,
                //						Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub
                destination = marker.getPosition();

                Intent data = new Intent();
                data.putExtra("lat", destination.latitude);
                data.putExtra("lng", destination.longitude);

                setResult(RESULT_OK, data);
                finish();
                return true;
            }
        });

    }


}
