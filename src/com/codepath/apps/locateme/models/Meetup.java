package com.codepath.apps.locateme.models;

import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.activeandroid.Model;

public class Meetup extends Model {
	// TODO: MOCK
	private static int NAME_COUNTER;
	private static final Random RANDOM = new Random();

	private String name;
	private Location location;
	private Date timestamp;

	public Meetup() {
		super();
		mockData();
	}

	// TODO: MOCK
	private void mockData() {
		name = "Location" + ++NAME_COUNTER;
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(locationProvider);
		if (location != null) {
			double newLatitude = location.getLatitude() + (0.5 * (RANDOM.nextDouble() - 0.5));
			double newLongitude = location.getLongitude() + (0.5 * (RANDOM.nextDouble() - 0.5));
			location.setLatitude(newLatitude);
			location.setLongitude(newLongitude);
		}
		timestamp = new Date(System.currentTimeMillis());
		save();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
