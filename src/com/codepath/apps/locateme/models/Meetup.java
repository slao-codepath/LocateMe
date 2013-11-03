package com.codepath.apps.locateme.models;

import java.util.Date;

import android.location.Location;

import com.activeandroid.Model;

public class Meetup extends Model {

	private String name;
	private Location location;
	private Date timestamp;

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
