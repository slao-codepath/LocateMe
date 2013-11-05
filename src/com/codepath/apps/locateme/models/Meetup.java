package com.codepath.apps.locateme.models;

import java.io.Serializable;
import java.util.Date;

import android.location.Location;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "meetups")
public class Meetup extends Model implements Serializable {
	private static final long serialVersionUID = 708506005927655652L;

	@Column(name = "name")
	public String name;

	@Column(name = "loc_latitude")
	private double locLatitude;

	@Column(name = "loc_longitude")
	private double locLongitude;

	@Column(name = "timestamp")
	public Date timestamp;

	public Meetup() {
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

	// Record Finders
	public static Meetup byId(long id) {
		return new Select().from(Meetup.class).where("id = ?", id).executeSingle();
	}
}
