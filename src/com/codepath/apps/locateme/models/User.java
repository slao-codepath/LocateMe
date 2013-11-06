package com.codepath.apps.locateme.models;

import android.location.Location;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "users")
public class User extends Model {
	public enum TransportMode {
		CAR, PUBLIC, WALK, UNKNOWN
	}

	private long fbUid;
	private String fbToken;

	@Column(name = "name")
	public String name;

	@Column(name = "loc_latitude")
	private double locLatitude;

	@Column(name = "loc_longitude")
	private double locLongitude;

	@Column(name = "current_transit_mode")
	public TransportMode currentTransitMode;

	// mock!
	public int eta;

	// server side
	// private String deviceToken;
	// private List<Location> locations;

	public User() {
		super();
	}

	public void setLocation(Location location) {
		locLatitude = location.getLatitude();
		locLongitude = location.getLongitude();
	}

	public Location getLocation() {
		Location location = new Location("");
		location.setLatitude(locLatitude);
		location.setLongitude(locLongitude);
		return location;
	}

	public void setTransportMode(TransportMode transportMode) {
		currentTransitMode = transportMode;
	}

	// Record Finders
	public static User byId(long id) {
		return new Select().from(User.class).where("id = ?", id).executeSingle();
	}
}
