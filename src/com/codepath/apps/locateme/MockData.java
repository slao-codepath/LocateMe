package com.codepath.apps.locateme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import android.location.Location;

import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.User;
import com.codepath.apps.locateme.models.User.TransportMode;
import com.codepath.apps.locateme.models.UserMeetupState;
import com.codepath.apps.locateme.models.UserMeetupState.Status;

public class MockData {
	public static final String[] MOCK_NAMES = { "Joe Montana", "Tim Lincecum", "Barry Bonds", "Jerry Rice",
		"Ronnie Lott", "Steve Young" };

	public static final Map<String, Location> LOCATIONS;
	public static final Map<String, User> USERS;
	private static final Meetup[] MEETUPS;
	private static final Calendar calendar = Calendar.getInstance();
	private static final Random RANDOM = new Random();
	private static final List<TransportMode> TRANSPORT_VALUES = Collections.unmodifiableList(Arrays
			.asList(TransportMode.values()));
	private static final int TRANSPORT_SIZE = TRANSPORT_VALUES.size();

	static {
		// clear
		UserMeetupState.delete(UserMeetupState.class);
		User.delete(User.class);
		Meetup.delete(Meetup.class);

		// create locations
		LOCATIONS = new HashMap<String, Location>();
		LOCATIONS.put("Ferry Building", createLocation(37.7955, -122.3937));
		LOCATIONS.put("Dolores Park", createLocation(37.7583, -122.4275));
		LOCATIONS.put("AT&T Park", createLocation(37.7786, -122.3892));
		LOCATIONS.put("Coit Tower", createLocation(37.8025, -122.4058));
		LOCATIONS.put("Twin Peaks", createLocation(37.7516, -122.4477));
		LOCATIONS.put("Pier 39", createLocation(37.8100, -122.4104));
		LOCATIONS.put("Union Square", createLocation(37.7881, -122.4075));
		LOCATIONS.put("Ghirardelli Square", createLocation(37.8057, -122.4218));
		LOCATIONS.put("Muir Woods", createLocation(37.8919, -122.5708));

		// create users
		USERS = new HashMap<String, User>();
		for (int i = 0; i < MOCK_NAMES.length; ++i) {
			USERS.put(MOCK_NAMES[i], createUser(MOCK_NAMES[i]));
		}

		// create meetups and user groups
		List<User> remainingUsers = new ArrayList<User>(USERS.values());

		String[] locationNames = LOCATIONS.keySet().toArray(new String[LOCATIONS.keySet().size()]);
		MEETUPS = new Meetup[LOCATIONS.size()];
		for (int i = 0; i < MEETUPS.length; ++i) {
			MEETUPS[i] = createMeetup(locationNames[i], LOCATIONS.get(locationNames[i]));
			if (remainingUsers.size() == 0) {
				remainingUsers = new ArrayList<User>(USERS.values());
			}
			createMeetupUsers(MEETUPS[i].getId(), remainingUsers);
		}
	}

	private static Location getMockLocation() {
		int pos = RANDOM.nextInt(LOCATIONS.size());
		Set<Entry<String, Location>> entrySet = LOCATIONS.entrySet();
		Entry<String, Location> entry = null;
		while (pos-- >= 0)
			entry = entrySet.iterator().next();
		return entry.getValue();
	}

	private static TransportMode getMockTransportMode() {
		return TRANSPORT_VALUES.get(RANDOM.nextInt(TRANSPORT_SIZE));
	}

	private static User createUser(String name) {
		User user = new User();
		user.name = name;
		user.setLocation(getMockLocation());
		user.currentTransitMode = getMockTransportMode();
		user.save();
		return user;
	}

	private static Location createLocation(double latitude, double longitude) {
		Location location = new Location("");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		return location;
	}

	private static Meetup createMeetup(String name, Location location) {
		Meetup meetup = new Meetup();
		meetup.name = name;
		meetup.setLocation(location);
		calendar.add(Calendar.MINUTE, 20);
		meetup.timestamp = calendar.getTime();
		meetup.save();
		return meetup;
	}

	private static void createMeetupUsers(long meetupId, List<User> remainingUsers) {
		int statusIndex = 0;
		Status[] statusValues = UserMeetupState.Status.values();
		for (int i = 0; remainingUsers.size() > 0 && i < 3; ++i) {
			UserMeetupState state = new UserMeetupState();
			state.meetupId = meetupId;
			int userIndex = RANDOM.nextInt(remainingUsers.size());
			state.userId = remainingUsers.get(userIndex).getId();
			remainingUsers.remove(userIndex);
			state.setStatus(statusValues[statusIndex]);
			if (++statusIndex >= statusValues.length)
				statusIndex = 0;
			state.save();
		}
	}

	public static void setSelectedCreateLocation(double lat, double lng) {
		LOCATIONS.put("SelectedCurrentLocation", createLocation(lat,lng));		
	}

	public static Location getCurrentSelectedPosition() {
		if(LOCATIONS.get("SelectedCurrentLocation") != null){
			return LOCATIONS.get("SelectedCurrentLocation");	
		} else {
			return LOCATIONS.get("Union Square");
		}
		
	}

}
