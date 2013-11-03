package com.codepath.apps.locateme.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.activeandroid.Model;

public class UserMeetupState extends Model {

	// TODO: MOCK
	private static final Random RANDOM = new Random();
	private static final List<Status> STATUS_VALUES = Collections.unmodifiableList(Arrays.asList(Status.values()));
	private static final int STATUS_SIZE = STATUS_VALUES.size();

	public enum Status {
		PENDING, ACTIVE, DECLINED, ARRIVED, CANCELLED, DELETED
	}
	public static String getStatusString(Status status) {
		String val = null;
		switch (status) {
		case PENDING:
			val = "Pending";
			break;
		case ACTIVE:
			val = "Active";
			break;
		case DECLINED:
			val = "Declined";
			break;
		case ARRIVED:
			val = "Arrived";
			break;
		case CANCELLED:
			val = "Cancelled";
			break;
		case DELETED:
			val = "Deleted";
			break;
		default:
			val = "Unknown";
			break;
		}
		return val;
	}

	private long userId;
	private long meetupId;
	private Status status;

	public UserMeetupState() {
		super();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getMeetupId() {
		return meetupId;
	}

	public void setMeetupId(long meetupId) {
		this.meetupId = meetupId;
	}

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	public static UserMeetupState byIds(long userId, long meetupId) {
		// TODO: MOCK
		UserMeetupState state = new UserMeetupState();
		state.setUserId(userId);
		state.setMeetupId(meetupId);
		state.setStatus(STATUS_VALUES.get(RANDOM.nextInt(STATUS_SIZE)));
		return state;

		//return new Select().from(SampleModel.class).where("meetupId = ? and userId = ?", meetupId, userId).executeSingle();
	}

}
