package com.codepath.apps.locateme.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.location.Location;

import com.activeandroid.Model;

public class User extends Model {

	// TODO: MOCK
	private static final String[] MOCK_NAMES = {"Allen Iverson", "Randall Cunningham", "Curt Schilling", "Eric Lindros"};
	private static int NAME_COUNTER;
	private static final Random RANDOM = new Random();
	private static final List<TransportMode> TRANSPORT_VALUES = Collections.unmodifiableList(Arrays.asList(TransportMode.values()));
	private static final int TRANSPORT_SIZE = TRANSPORT_VALUES.size();

	public enum TransportMode {
		CAR, PUBLIC, WALK, UNKNOWN
	}

	private long fbUid;
	private String fbToken;
	private String name;
	private Location currentLocation;
	private TransportMode currentTransitMode;

	// server side
	//	private String deviceToken;
	//	private List<Location> locations;

	public User() {
		super();
		// TODO: MOCK
		if (NAME_COUNTER < MOCK_NAMES.length) {
			name = MOCK_NAMES[NAME_COUNTER];
		} else {
			name = MOCK_NAMES[NAME_COUNTER % MOCK_NAMES.length] + (NAME_COUNTER / MOCK_NAMES.length);
		}
		NAME_COUNTER++;
		currentTransitMode = TRANSPORT_VALUES.get(RANDOM.nextInt(TRANSPORT_SIZE));
		save();
	}

	public long getFbUid() {
		return fbUid;
	}
	public void setFbUid(long fbUid) {
		this.fbUid = fbUid;
	}
	public String getFbToken() {
		return fbToken;
	}
	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}
	public Location getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}
	public TransportMode getCurrentTransitMode() {
		return currentTransitMode;
	}
	public void setCurrentTransitMode(TransportMode currentTransitMode) {
		this.currentTransitMode = currentTransitMode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
