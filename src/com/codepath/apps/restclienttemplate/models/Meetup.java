package com.codepath.apps.restclienttemplate.models;

import java.util.Date;
import java.util.List;

import android.location.Location;

import com.activeandroid.Model;

public class Meetup extends Model {
	private enum Status {
		ACTIVE, INACTIVE
	}

	private String name;
	private Status status;
	private Location location;
	private List<User> users;
	private Date timestamp;
}
