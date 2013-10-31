package com.codepath.apps.locateme.models;

import java.util.List;

import android.location.Location;

import com.activeandroid.Model;

public class User extends Model {
	private enum TransitMode {
		BUS, TRAIN, WALK, CAR, UNKNOWN
	}

	private int id;
	private int fbUid;
	private String fbToken;
	private List<Meetup> meetups;
	private Location currentLocation;
	private TransitMode currentTransitMode;

	// server side
	private String deviceToken;
	private List<Location> locations;
}
