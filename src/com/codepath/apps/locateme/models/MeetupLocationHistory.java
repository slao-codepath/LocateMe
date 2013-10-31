package com.codepath.apps.locateme.models;

import android.location.Location;


// Server tables

public class MeetupLocationHistory {
	private Meetup meetup;
	private Location location; //assumes time
	private User user;
}
/*
public class Meetup{
	public List<MeetupLocationHistory> history;
	private enum Status {
		ACTIVE, INACTIVE
	}
	private int id;
	private String name;
	private Status status;
	private Location location;
	private List<User> users;
	private Date timestamp;
}
*/